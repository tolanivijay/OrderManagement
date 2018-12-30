/**
 * 
 */
package com.ordm.dao;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.ordm.model.LimitOrder;
import com.ordm.util.OrdmUtil;

/**
 * @author VIJAY
 *
 */
@Component
public class LimitOrderDAOImpl implements LimitOrderDAO{
	
	Map<Integer,LimitOrder> orderCache = new ConcurrentHashMap<Integer,LimitOrder>();

	@Override
	public void saveOrder(LimitOrder order) {
		order.setOrderId(OrdmUtil.getNextSequence());
		order.setCreatedTime(LocalDateTime.now());
		orderCache.put(order.getOrderId(), order);
	}

	@Override
	public Collection<LimitOrder> getAllOrders() {
		return orderCache.values();
	}
}
