/**
 * 
 */
package com.ordm.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import com.ordm.dao.ExecutionDAO;
import com.ordm.dao.LimitOrderDAO;
import com.ordm.dao.MarketOrderDAO;
import com.ordm.dao.OrderBookDAO;
import com.ordm.model.BaseOrder;
import com.ordm.model.Execution;
import com.ordm.model.Instrument;
import com.ordm.model.LimitOrder;
import com.ordm.model.MarketOrder;
import com.ordm.model.OrderBook;
import com.ordm.service.response.BookStats;
import com.ordm.stats.StatsComputer;

/**
 * @author VIJAY
 *
 */

@Service
public class OrdmService {

	@Autowired
	private OrderBookDAO orderBookDAO = null;

	@Autowired
	private ExecutionDAO executionDAO = null;

	@Autowired
	private LimitOrderDAO limitOrderDAO = null;

	@Autowired
	private MarketOrderDAO marketOrderDAO = null;

	@Autowired
	private StatsComputer statsComputer = null;

	public BookStats getBookStats(OrderBook book) {
		return statsComputer.computeBookStats(book);
	}

	public void addExecution(OrderBook book, Execution exec) {
		executionDAO.saveExection(exec);
		book.performAddExecutionSteps(exec);
		orderBookDAO.saveBook(book);
	}

	public BaseOrder getOrder(@RequestParam(value = "orderId") Integer orderId) {
		BaseOrder order = null;
		List<BaseOrder> orders = limitOrderDAO.getAllOrders().stream().filter(x -> x.getOrderId().equals(orderId))
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(orders)) {
			orders = marketOrderDAO.getAllOrders().stream().filter(x -> x.getOrderId().equals(orderId))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(orders)) {
			order = orders.get(0);

		}
		return order;

	}

	public OrderBook openNewBook(Instrument instrument) {
		return orderBookDAO.openNewBook(instrument);
	}

	public void closeOrderBook(OrderBook book) {
		book.closeOrder();
		orderBookDAO.saveBook(book);
	}

	public void addMarketOrderToOrderBook(OrderBook book, MarketOrder order) {
		marketOrderDAO.saveOrder(order);
		book.addToMarketOrders(order);
		orderBookDAO.saveBook(book);
	}

	public void addLimitOrderToOrderBook(OrderBook book, LimitOrder order) {
		limitOrderDAO.saveOrder(order);
		book.addToLimitOrders(order);
		orderBookDAO.saveBook(book);
	}

	

}
