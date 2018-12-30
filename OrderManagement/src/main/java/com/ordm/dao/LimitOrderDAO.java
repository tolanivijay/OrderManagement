/**
 * 
 */
package com.ordm.dao;

import java.util.Collection;

import com.ordm.model.LimitOrder;

/**
 * @author VIJAY
 *
 */
public interface LimitOrderDAO {
	
	public void saveOrder(LimitOrder order);
	
	public Collection<LimitOrder> getAllOrders();

}
