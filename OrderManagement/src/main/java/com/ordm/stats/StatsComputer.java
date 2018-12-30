/**
 * 
 */
package com.ordm.stats;

import com.ordm.model.OrderBook;
import com.ordm.service.response.BookStats;

/**
 * @author VIJAY
 *
 */
public interface StatsComputer {

	public BookStats computeBookStats(OrderBook order);
}
