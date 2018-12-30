/**
 * 
 */
package com.ordm.dao;

import java.util.Collection;

import com.ordm.model.MarketOrder;

/**
 * @author VIJAY
 *
 */
public interface MarketOrderDAO {
	
	public void saveOrder(MarketOrder order);
	
	public Collection<MarketOrder> getAllOrders();

}
