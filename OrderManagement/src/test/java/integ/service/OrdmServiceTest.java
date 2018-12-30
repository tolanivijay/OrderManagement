/**
 * 
 */
package integ.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.ordm.app.OrdmApp;
import com.ordm.controller.OrdmController;
import com.ordm.enums.ResponseStatus;
import com.ordm.model.BaseOrder;
import com.ordm.model.Execution;
import com.ordm.model.LimitOrder;
import com.ordm.model.MarketOrder;
import com.ordm.model.OrderBook;
import com.ordm.model.OrderExecution;
import com.ordm.service.response.BookStats;
import com.ordm.service.response.ErrorDetails;
import com.ordm.service.response.OrdmHeader;
import com.ordm.service.response.OrdmResponse;
import com.ordm.staticdata.OrdmResponseErrorCodes;


/**
 * @author VIJAY
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes=OrdmApp.class,webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdmServiceTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private OrdmController service;
	
	private static Integer newBookId=null;
	
	@Test
	public void test1CreateNewBookAndReturned() {
		OrdmResponse<OrderBook> response =service.openNewOrderBook(1);
		OrderBook book =response.getBody();
		assertNotNull(book);
		System.out.println("Book Created is "+ book.getBookId());
		newBookId= book.getBookId();
	}
	
	@Test 
	public void test2VerifyBookReturned() {
		OrdmResponse<OrderBook> response =service.getOrderBook(newBookId);
		OrderBook book =response.getBody();
		System.out.println("Book Returned is "+ book);
		assertNotNull(book);
	}
	
	@Test
	public void test3Create3LimitOrders() {
		LimitOrder order1 = getLimitOrder(20,new BigDecimal(15.0));
		OrdmResponse<String> response =service.addLimitOrderToOrderBook(newBookId, order1);
		assertEquals(ResponseStatus.SUCCESS.getStatus(),response.getHeader().getStatus());
		
		order1 = getLimitOrder(20,new BigDecimal(18.0));
		response =service.addLimitOrderToOrderBook(newBookId, order1);
		assertEquals(ResponseStatus.SUCCESS.getStatus(),response.getHeader().getStatus());
		
		order1 = getLimitOrder(20,new BigDecimal(30.0));
		response =service.addLimitOrderToOrderBook(newBookId, order1);
		assertEquals(ResponseStatus.SUCCESS.getStatus(),response.getHeader().getStatus());
		
		OrdmResponse<OrderBook> response1 =service.getOrderBook(newBookId);
		OrderBook book = response1.getBody();
		List<LimitOrder> limitOrders =book.getLimitOrders();
		
		assertEquals(limitOrders.size(),3);
		assertEquals(book.getIsOpen(),Boolean.TRUE);
		assertEquals(book.getValidDemand(),60);
		assertEquals(book.getValidNumberOfOrders(),3);
		
		BookStats stats =service.getBookStats(newBookId).getBody();
		assertTrue(stats.getEarliestOrder().getPrice().compareTo(new BigDecimal(15.0)) == 0);
		assertTrue(stats.getLatestOrder().getPrice().compareTo(new BigDecimal(30.0)) == 0);
		assertEquals(stats.getInValidDemand().intValue(),0);
		assertEquals(stats.getValidDemand().intValue(),60);
		assertEquals(stats.getNoOfOrders().intValue(),3);
	}
	
	@Test
	public void test4Create2MarketOrders() {
		MarketOrder order1 = getMarketOrder(20);
		OrdmResponse<String> response =service.addMarketOrderToOrderBook(newBookId, order1);
		assertEquals(ResponseStatus.SUCCESS.getStatus(),response.getHeader().getStatus());
		
		order1 = getMarketOrder(20);
		response =service.addMarketOrderToOrderBook(newBookId, order1);
		assertEquals(ResponseStatus.SUCCESS.getStatus(),response.getHeader().getStatus());
		
		OrdmResponse<OrderBook> response1 =service.getOrderBook(newBookId);
		OrderBook book = response1.getBody();
		List<MarketOrder> marketOrders =book.getMarketOrders();
		
		assertEquals(marketOrders.size(),2);
		assertEquals(book.getIsOpen(),Boolean.TRUE);
		assertEquals(book.getValidDemand(),100);
		assertEquals(book.getValidNumberOfOrders(),5);
		
	}
	
	@Test
	public void test5CloseOrder() {
		OrdmResponse<String> response =service.closeOrderBook(newBookId);
		assertEquals(ResponseStatus.SUCCESS.getStatus(),response.getHeader().getStatus());
		
		OrdmResponse<OrderBook> response1 =service.getOrderBook(newBookId);
		OrderBook book = response1.getBody();
		assertEquals(book.getIsOpen(),Boolean.FALSE);
		assertEquals(book.getValidDemand(),100);
		assertEquals(book.getValidNumberOfOrders(),5);
	}
	
	@Test
	public void test6AddExecutions() {
		Execution exec = getExecution(25,new BigDecimal(20));
		
		OrdmResponse<String> response =service.addExecution(newBookId, exec);
		assertEquals(ResponseStatus.SUCCESS.getStatus(),response.getHeader().getStatus());
		
		OrdmResponse<OrderBook> response1 =service.getOrderBook(newBookId);
		OrderBook book = response1.getBody();
		assertEquals(book.getIsOpen(),Boolean.FALSE);
		assertEquals(book.getIsExecuted(),Boolean.FALSE);
		assertEquals(book.getValidDemand(),60);
		assertEquals(book.getValidNumberOfOrders(),3);
		assertEquals(book.getExecutedQuantity(),25);
		assertEquals(getSumOfOrderExecutions(book),25);
		
		exec = getExecution(25,new BigDecimal(20));
		response =service.addExecution(newBookId, exec);
		assertEquals(ResponseStatus.SUCCESS.getStatus(),response.getHeader().getStatus());
		
		response1 =service.getOrderBook(newBookId);
		book = response1.getBody();
		assertEquals(book.getIsOpen(),Boolean.FALSE);
		assertEquals(book.getIsExecuted(),Boolean.FALSE);
		assertEquals(book.getValidDemand(),60);
		assertEquals(book.getValidNumberOfOrders(),3);
		assertEquals(book.getExecutedQuantity(),50);
		assertEquals(getSumOfOrderExecutions(book),50);
		
		exec = getExecution(30,new BigDecimal(20));
		response =service.addExecution(newBookId, exec);
		
		assertTrue(containsError(response.getHeader(),OrdmResponseErrorCodes.EXECUTION_QTY_EXECEEDS_DEMAND));
		exec = getExecution(10,new BigDecimal(20));
		response =service.addExecution(newBookId, exec);
		assertEquals(ResponseStatus.SUCCESS.getStatus(),response.getHeader().getStatus());
		
		response1 =service.getOrderBook(newBookId);
		book = response1.getBody();
		assertEquals(book.getIsOpen(),Boolean.FALSE);
		assertEquals(book.getIsExecuted(),Boolean.TRUE);
		assertEquals(book.getValidDemand(),60);
		assertEquals(book.getValidNumberOfOrders(),3);
		assertEquals(book.getExecutedQuantity(),60);
		assertEquals(getSumOfOrderExecutions(book),60);
		
		exec = getExecution(30,new BigDecimal(20));
		response =service.addExecution(newBookId, exec);
		assertTrue(containsError(response.getHeader(),OrdmResponseErrorCodes.BOOK_ALREADY_EXECUTED));
	}
	
	private boolean containsError(OrdmHeader header,OrdmResponseErrorCodes error) {
		boolean hasError = false;
		if (!CollectionUtils.isEmpty(header.getErrorDetails())) {
			for (ErrorDetails errorD : header.getErrorDetails()) {
				if (errorD != null && errorD.getErrorCode().equals(error.getErrorCode())) {
					hasError=true;
					break;
				}
			}
		}
		return hasError;
	}
	
	
	
	private int getSumOfOrderExecutions(OrderBook book) {
		List<BaseOrder> mergedValidList =Stream.concat(book.getLimitOrders().stream(),book.getMarketOrders().stream()).collect(Collectors.toList());
		int sumOfOrderExecutions = 0;
		for (BaseOrder order : mergedValidList) {
			for (OrderExecution exec1 :order.getExecutions()) {
				sumOfOrderExecutions+=exec1.getExecutedQuantity();
			}
		}
		return sumOfOrderExecutions;
	}
	
	private Execution getExecution(int quantity, BigDecimal price) {
		Execution exec = new Execution();
		exec.setPrice(price);
		exec.setQuantity(quantity);
		return exec;
	}
	
	private MarketOrder getMarketOrder(int quantity) {
		MarketOrder order = new MarketOrder();
		order.setQuantity(quantity);
		order.setInstrumentId(1);
		return order;
	}
	
	private LimitOrder getLimitOrder(int quantity, BigDecimal price) {
		LimitOrder order1 = new LimitOrder();
		order1.setQuantity(quantity);
		order1.setInstrumentId(1);
		order1.setPrice(price);
		return order1;
	}
}