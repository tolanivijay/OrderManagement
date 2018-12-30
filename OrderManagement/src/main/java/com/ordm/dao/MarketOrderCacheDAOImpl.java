/**
 * 
 */
package com.ordm.dao;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.ordm.model.MarketOrder;
import com.ordm.util.OrdmUtil;

/**
 * @author VIJAY
 *
 */

@Component
public class MarketOrderCacheDAOImpl implements MarketOrderDAO{
	
	Map<Integer,MarketOrder> orderCache = new ConcurrentHashMap<Integer,MarketOrder>();

	@Override
	public void saveOrder(MarketOrder order) {
		order.setOrderId(OrdmUtil.getNextSequence());
		order.setCreatedTime(LocalDateTime.now());
		orderCache.put(order.getOrderId(), order);
	}

	@Override
	public Collection<MarketOrder> getAllOrders() {
		return orderCache.values();
	}

}
