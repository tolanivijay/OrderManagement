/**
 * 
 */
package com.ordm.dao;

import java.util.Collection;

import com.ordm.model.Instrument;
import com.ordm.model.LimitOrder;
import com.ordm.model.MarketOrder;
import com.ordm.model.OrderBook;

/**
 * @author VIJAY
 *
 */
public interface OrderBookDAO {
	
	public OrderBook openNewBook(Instrument instrument);
	
	public void saveBook(OrderBook book);
	
	public OrderBook getOrderBook(Integer bookId);
	
	//public Boolean closeBook(OrderBook book);
	
	public Collection<OrderBook> getAllBooks();
	
	public Boolean addLimitOrderToBook(OrderBook book,LimitOrder order);
	
	public Boolean addMarketOrderToBook(OrderBook book,MarketOrder order);

}
