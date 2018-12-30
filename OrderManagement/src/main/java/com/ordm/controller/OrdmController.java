/**
 * 
 */
package com.ordm.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ordm.dao.InstrumentDAO;
import com.ordm.dao.OrderBookDAO;
import com.ordm.model.BaseOrder;
import com.ordm.model.Execution;
import com.ordm.model.Instrument;
import com.ordm.model.LimitOrder;
import com.ordm.model.MarketOrder;
import com.ordm.model.OrderBook;
import com.ordm.service.OrdmService;
import com.ordm.service.response.BookStats;
import com.ordm.service.response.OrdmResponse;
import com.ordm.staticdata.OrdmResponseErrorCodes;
import com.ordm.validator.OrdmServiceValidator;

import io.micrometer.core.annotation.Timed;

/**
 * @author VIJAY
 *
 */
@RestController
@RequestMapping(value="ordm")
@EnableAspectJAutoProxy
public class OrdmController {
	
	private Logger logger = LoggerFactory.getLogger(OrdmController.class);
	
	@Autowired
	private OrdmService ordmService;
	
	@Autowired
	private OrdmServiceValidator validator;
	
	@Autowired
	private InstrumentDAO instDAO = null; 
	
	@Autowired
	private OrderBookDAO orderBookDAO = null;
	
	@PostMapping("addExecution/{orderBookId}")
	@Timed(value = "OrdmService",extraTags= {"serviceCall","addExecution"})
	public OrdmResponse<String> addExecution(@PathVariable Integer orderBookId,@RequestBody Execution exec) {
		OrdmResponse<String> response = new OrdmResponse<String>();
		OrderBook book =orderBookDAO.getOrderBook(orderBookId);
		try {
			if (book == null) {
				response.addToError(OrdmResponseErrorCodes.BOOK_NOT_FOUND);
				return response;
			}
			book.acquireWriteLock();
			validator.performAddExecValidations(response, book, exec);
			if (response.hasErrors()) {
				return response;
			}
			ordmService.addExecution(book, exec);
			return response;
		}catch (Exception e) {
			logger.error("Exception while saving executions",e);
			response.addToError(OrdmResponseErrorCodes.GENERAL_ERROR);
			return response;
		} finally{
			if (book !=null)
				book.releaseWriteLock();
		}
	}
	
	@GetMapping("getStats/{orderBookId}")
	@Timed(value = "OrdmService",extraTags= {"serviceCall","getStats"})
	public OrdmResponse<BookStats> getBookStats(@PathVariable Integer orderBookId){
		OrdmResponse<BookStats> response = new OrdmResponse<BookStats>();
		try {
			OrderBook book =orderBookDAO.getOrderBook(orderBookId);
			if (book == null) {
				response.addToError(OrdmResponseErrorCodes.BOOK_NOT_FOUND);
				return response;
			}
			response.setBody(ordmService.getBookStats(book));
		}catch(Exception e) {
			logger.error("Exception while saving executions",e);
			response.addToError(OrdmResponseErrorCodes.GENERAL_ERROR);
		}
		return response;
	}
	
	@GetMapping("getOrder")
	@Timed(value = "OrdmService",extraTags= {"serviceCall","getOrder"})
	public OrdmResponse<BaseOrder> getOrder(@RequestParam(value="orderId") Integer orderId) {
		OrdmResponse<BaseOrder> response = new OrdmResponse<BaseOrder>();
		BaseOrder order = ordmService.getOrder(orderId);
		if (order == null) {
			response.addToError(OrdmResponseErrorCodes.ORDER_NOT_FOUND);
			return response;
		}
		response.setBody(order);
		return response;
		
	}
	
	@PostMapping("openNewBook/{instrumentId}")
	@Timed(value = "OrdmService", extraTags = { "serviceCall", "openNewBook" })
	public OrdmResponse<OrderBook> openNewOrderBook(@PathVariable(value="instrumentId") Integer instrumentId) {
		OrdmResponse<OrderBook> response = new OrdmResponse<OrderBook>();
		try {
			Instrument instrument =instDAO.getInstrument(instrumentId);
			if (instrument == null) {
				response.addToError(OrdmResponseErrorCodes.CANNOT_OPEN_BOOK_INST_NOT_FOUND);
				return response;
			}
			response.setBody(ordmService.openNewBook(instrument));
		}	
		catch (Exception e) {
			logger.error("Exception while opening new book",e);
			response.addToError(OrdmResponseErrorCodes.GENERAL_ERROR);
		}
		return response;
	}
	
	@GetMapping("getBook")
	@Timed(value = "OrdmService",extraTags= {"serviceCall","getBook"})
	public OrdmResponse<OrderBook> getOrderBook(@RequestParam(value="bookId") Integer orderBookId) {
		OrdmResponse<OrderBook> response = new OrdmResponse<OrderBook>();
		try {
			OrderBook book =orderBookDAO.getOrderBook(orderBookId);
			if (book == null) {
				response.addToError(OrdmResponseErrorCodes.BOOK_NOT_FOUND);
				return response;
			}
			response.setBody(orderBookDAO.getOrderBook(orderBookId));
		}catch(Exception e) {
			logger.error("Exception while getting book",e);
			response.addToError(OrdmResponseErrorCodes.GENERAL_ERROR);
		}
		return response;
	}
	
	@GetMapping("getAllBooks")
	@Timed(value = "OrdmService",extraTags= {"serviceCall","getAllBooks"})
	public OrdmResponse<Collection<OrderBook>> getAllBooks(){
		OrdmResponse<Collection<OrderBook>> response = new OrdmResponse<Collection<OrderBook>>(); 
		try {
			response.setBody(orderBookDAO.getAllBooks());
		}catch(Exception e) {
			logger.error("Exception while getting all books",e);
			response.addToError(OrdmResponseErrorCodes.GENERAL_ERROR);
		}
		return response;
	}
	
	@PostMapping("closeBook/{orderBookId}")
	@Timed(value = "OrdmService",extraTags= {"serviceCall","closeBook"})
	public OrdmResponse<String> closeOrderBook(@PathVariable Integer orderBookId) {
		OrdmResponse<String> response = new OrdmResponse<String>();
		OrderBook book = orderBookDAO.getOrderBook(orderBookId);
		try {
			if (book == null) {
				response.addToError(OrdmResponseErrorCodes.BOOK_NOT_FOUND);
				return response;
			}
			book.acquireWriteLock();
			if (Boolean.FALSE.equals(book.getIsOpen())) {
				response.addToError(OrdmResponseErrorCodes.BOOK_CLOSED_ALREADY);
				return response;
			}
			ordmService.closeOrderBook(book);
		}catch(Exception e) {
			logger.error("Exception while closing book",e);
			response.addToError(OrdmResponseErrorCodes.GENERAL_ERROR);
		}
		finally {
			if (book != null)
				book.releaseWriteLock();
		}
		return response;
	}
	
	@PostMapping("addToMarketOrder/{bookId}")
	@Timed(value = "OrdmService",extraTags= {"serviceCall","addMarketOrder"})
	public OrdmResponse<String> addMarketOrderToOrderBook(@PathVariable Integer bookId,@RequestBody MarketOrder order) {
		OrdmResponse<String> response = new OrdmResponse<String>();
		OrderBook book = orderBookDAO.getOrderBook(bookId);
		try {
			if (book == null) {
				response.addToError(OrdmResponseErrorCodes.BOOK_NOT_FOUND);
				return response;
			}
			if (!book.getIsOpen()) {
				response.addToError(OrdmResponseErrorCodes.BOOK_CLOSED);
				return response;
			}
			OrdmResponseErrorCodes errorCode = instrumentCheck(book,order);
			if (errorCode != null) {
				response.addToError(errorCode);
				return response;
			}
			book.acquireWriteLock();
			ordmService.addMarketOrderToOrderBook(book, order);
		}catch(Exception e) {
			logger.error("Exception while adding Market Order ",e);
			response.addToError(OrdmResponseErrorCodes.GENERAL_ERROR);
		}finally {
			if (book != null)
			book.releaseWriteLock();
		}
		return response;
	}
	
	
	@PostMapping("addToLimitOrder/{bookId}")
	@Timed(value = "OrdmService",extraTags= {"serviceCall","addLimitOrder"})
	public OrdmResponse<String> addLimitOrderToOrderBook(@PathVariable Integer bookId,@RequestBody LimitOrder order) {
		OrdmResponse<String> response = new OrdmResponse<String>();
		OrderBook book = orderBookDAO.getOrderBook(bookId);
		try {
			if (book == null) {
				response.addToError(OrdmResponseErrorCodes.BOOK_NOT_FOUND);
				return response;
			}
			if (!book.getIsOpen()) {
				response.addToError(OrdmResponseErrorCodes.BOOK_CLOSED);
				return response;
			}
			OrdmResponseErrorCodes errorCode = instrumentCheck(book,order);
			if (errorCode != null) {
				response.addToError(errorCode);
				return response;
			}
			book.acquireWriteLock();
			ordmService.addLimitOrderToOrderBook(book, order);
		}catch(Exception e) {
			logger.error("Exception while adding Limit Order ",e);
			response.addToError(OrdmResponseErrorCodes.GENERAL_ERROR);
		}finally {
			if (book != null)
			book.releaseWriteLock();
		}
		return response;
		
	}
	
	private OrdmResponseErrorCodes instrumentCheck(OrderBook book,BaseOrder order) {
		Instrument insturment =instDAO.getInstrument(order.getInstrumentId());
		if (insturment == null) {
			return OrdmResponseErrorCodes.CANNOT_OPEN_BOOK_INST_NOT_FOUND;
		}
		if (!insturment.getInstrumentId().equals(book.getInstrument().getInstrumentId())) {
			return OrdmResponseErrorCodes.CANNOT_ADD_ORDER_INST_MISMATCH;
		}
		return null;
	}
}
