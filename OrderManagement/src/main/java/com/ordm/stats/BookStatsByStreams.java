/**
 * 
 */
package com.ordm.stats;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.ordm.model.BaseOrder;
import com.ordm.model.LimitOrder;
import com.ordm.model.OrderBook;
import com.ordm.service.response.BookStats;
import com.ordm.service.response.LimitBreak;

/**
 * @author VIJAY
 *
 */
@Component
public class BookStatsByStreams implements StatsComputer{

	@Override
	public BookStats computeBookStats(OrderBook book) {
		List<BaseOrder> mergedList = Stream.concat(book.getLimitOrders().stream(), book.getMarketOrders().stream()).collect(Collectors.toList());
		BookStats stats = new BookStats();
		Integer noOfLimitOrders = book.getLimitOrders().size();
		Integer noOfMarketOrders = book.getMarketOrders().size();
		
		List<BaseOrder> validLimitOrders = book.getLimitOrders().stream().filter(x -> x.getIsValid()).collect(Collectors.toList());
		List<BaseOrder> inValidLimitOrders = book.getLimitOrders().stream().filter(x -> !x.getIsValid()).collect(Collectors.toList());
		
		
		BaseOrder biggestOrder = mergedList.stream().sorted((x,y) -> x.getQuantity() > y.getQuantity() ? -1 :1).findFirst().orElse(null);
		BaseOrder smallestOrder = mergedList.stream().sorted((x,y) -> x.getQuantity() > y.getQuantity() ? 1 :-1).findFirst().orElse(null);
		
		BaseOrder earliestOrder = mergedList.stream().sorted((x,y) -> x.getCreatedTime().isBefore(y.getCreatedTime()) ? -1 :1).findFirst().orElse(null);
		BaseOrder latestOrder = mergedList.stream().sorted((x,y) -> x.getCreatedTime().isBefore(y.getCreatedTime()) ? 1 :-1).findFirst().orElse(null);
		
		List<BaseOrder> validMarketOrders = book.getMarketOrders().stream().filter(x -> x.getIsValid()).collect(Collectors.toList());
		List<BaseOrder> inValidMarketOrders = book.getMarketOrders().stream().filter(x -> !x.getIsValid()).collect(Collectors.toList());
		
		Integer validLimitDemand = validLimitOrders.stream().mapToInt(x -> x.getQuantity()).sum();
		Integer validMarketDemand = validMarketOrders.stream().mapToInt(x -> x.getQuantity()).sum();
		
		Integer invalidLimitDemand = inValidLimitOrders.stream().mapToInt(x -> x.getQuantity()).sum();
		Integer invalidMarketDemand = inValidMarketOrders.stream().mapToInt(x -> x.getQuantity()).sum();
		

		stats.setNoOfOrders(noOfLimitOrders + noOfMarketOrders);
		stats.setNoOfValidOrders(validLimitOrders.size()+validMarketOrders.size());
		stats.setNoOfMarketOrders(noOfMarketOrders);
		stats.setNoOfInvalidOrders(inValidLimitOrders.size() + inValidMarketOrders.size());
		stats.setValidDemand(validLimitDemand + validMarketDemand);
		stats.setInValidDemand(invalidLimitDemand + invalidMarketDemand);
		stats.setTotalDemand(stats.getValidDemand() + stats.getInValidDemand());
		stats.setBiggestOrder(biggestOrder);
		stats.setSmallestOrder(smallestOrder);
		stats.setEarliestOrder(earliestOrder);
		stats.setLatestOrder(latestOrder);
		stats.setAccumulatedExecQuantity(book.getExecutedQuantity());
		stats.setExecPrice(book.getExecutionFamily().getPrice());
		stats.setLimitBreakDown(getLimitBreakDown(book));
		return stats;
	}
	
	private List<LimitBreak> getLimitBreakDown(OrderBook book){
		List<LimitBreak> breakDown = new ArrayList<LimitBreak>();
		Map<BigDecimal,BigDecimal> map = new HashMap<BigDecimal,BigDecimal>();
		for (LimitOrder l :book.getLimitOrders()) {
			breakDown.add(new LimitBreak(l.getPrice(), new BigDecimal(book.getValidDemand()/l.getPrice().doubleValue())));
		}
		return breakDown;
	}
}
