/**
 * 
 */
package com.ordm.dao;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Component;

import com.ordm.model.Instrument;
import com.ordm.model.LimitOrder;
import com.ordm.model.MarketOrder;
import com.ordm.model.OrderBook;
import com.ordm.util.OrdmUtil;

/**
 * @author VIJAY
 *
 */
@Component
public class OrderBookCacheDAOImpl implements OrderBookDAO{
	
	private Map<Integer, OrderBook> orderBookCache = new ConcurrentHashMap<Integer, OrderBook>();
	
	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	@Override
	public void saveBook(OrderBook book) {
		orderBookCache.put(book.getBookId(), book);
	}

	@Override
	public OrderBook openNewBook(Instrument instrument) {
		Integer nextInt =OrdmUtil.getNextSequence();
		OrderBook book = new OrderBook(nextInt, instrument);
		orderBookCache.put(nextInt, book);
		return book;
	}

	@Override
	public OrderBook getOrderBook(Integer bookId) {
		return orderBookCache.get(bookId);
	}

	@Override
	public Collection<OrderBook> getAllBooks() {
		return orderBookCache.values();
	}

	@Override
	public Boolean addLimitOrderToBook(OrderBook book, LimitOrder order) {
		try {
			readWriteLock.writeLock().lock();
			OrderBook bookInCache = orderBookCache.get(book.getBookId());
			if (bookInCache == null) {
				return Boolean.FALSE;
			}
			order.setOrderId(OrdmUtil.getNextSequence());
			bookInCache.addToLimitOrders(order);
			orderBookCache.put(book.getBookId(), bookInCache);// This statement is necessary if the cache is non-distributed but replicated.
			
		}catch (Exception e) {
			return Boolean.FALSE;
		}
		finally {
			readWriteLock.writeLock().unlock();
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean addMarketOrderToBook(OrderBook book, MarketOrder order) {
		try {
			readWriteLock.writeLock().lock();
			OrderBook bookInCache = orderBookCache.get(book.getBookId());
			if (bookInCache == null) {
				return Boolean.FALSE;
			}
			order.setOrderId(OrdmUtil.getNextSequence());
			bookInCache.addToMarketOrders(order);
			orderBookCache.put(book.getBookId(), bookInCache);// This statement is necessary if the cache is non-distributed but replicated.
		}catch (Exception e) {
			return Boolean.FALSE;
		}
		finally {
			readWriteLock.writeLock().unlock();
		}
		return Boolean.TRUE;
	}

	

	
}
