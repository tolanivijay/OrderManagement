/**
 * 
 */
package com.ordm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author VIJAY
 *
 */
public class OrderBook implements Cloneable{
	
	public OrderBook(Integer bookId,Instrument instrument) {
		this.bookId=bookId;
		this.instrument = instrument;
	}
	
	/**
	 * This lock should be a distributed lock across the cluster in case
	 * this app is deployed in a cluster. We will have one lock per book
	 * and any mutations have to be guarded by the write lock.
	 */
	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	private Integer bookId;
	
	private Boolean isOpen = Boolean.TRUE;
	
	private Instrument instrument;
	
	private List<LimitOrder> limitOrders = new ArrayList<LimitOrder>();
	
	private List<MarketOrder> marketOrders = new ArrayList<MarketOrder>();
	
	private ExecutionFamily executionFamily = new ExecutionFamily();
	
	private Boolean isExecuted = Boolean.FALSE;
	
	
	public void acquireWriteLock() {
		readWriteLock.writeLock().lock();
	}
	
	public void releaseWriteLock() {
		/* Need to check this condition,else if the lock is not
		not claimed by current thread, then it would throw 
		exception, which would be caught in regression suite.
		*/
		if (readWriteLock.writeLock().isHeldByCurrentThread())
				readWriteLock.writeLock().unlock();
	}
	
	
	private void checkAndMarkExecuted() {
		if (getValidDemand() == getExecutedQuantity()) {
			isExecuted = Boolean.TRUE;
		}
	}
	
	public int getValidDemand() {
		return Stream.concat(limitOrders.stream().filter(x -> x.getIsValid()), marketOrders.stream()).mapToInt(x -> x.getQuantity()).sum();
	}
	
	public int getValidNumberOfOrders() {
		return new Long(Stream.concat(limitOrders.stream().filter(x -> x.getIsValid()), marketOrders.stream()).count()).intValue();
	}
	
	public int getExecutedQuantity() {
		return executionFamily.getExecutions().stream().mapToInt(x -> x.getQuantity()).sum();
	}
	
	public boolean canExecutionFitIn(Execution exec) {
		if (exec.getQuantity() > (getValidDemand()-getExecutedQuantity())) {
			return false;
		}
		return true;
	}
	
	public void addToOrderBook(LimitOrder ord) {
		limitOrders.add(ord);
	}
	
	public void addToOrderBook(MarketOrder ord) {
		marketOrders.add(ord);
	}
	
	public void performAddExecutionSteps(Execution exec) {
		checkAndInvalidateOrders(exec);
		distributeExecutionQuantity(exec);
		addToExecutionFamily(exec);
		checkAndMarkExecuted();
	}
	
	private boolean isExecutionFamilyEmpty() {
		if (executionFamily.getExecutions().size() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param exec
	 * Need to invalidate Orders only on first execution since
	 * all executions will have same price and executions can be added 
	 * only after order is closed
	 */
	private void checkAndInvalidateOrders(Execution exec) {
		if (isExecutionFamilyEmpty()) {
			List<LimitOrder> inValidOrderList = limitOrders.stream().filter(x -> x.getPrice().compareTo(exec.getPrice()) < 0).collect(Collectors.toList());
			inValidOrderList.forEach(x -> x.setIsValid(false));
		}
	}
	
	private void addToExecutionFamily(Execution exec) {
		if (executionFamily.getPrice() == null) {
			executionFamily.setPrice(exec.getPrice());
		}
		executionFamily.addToExecutions(exec);
	}
	
	private void distributeExecutionQuantity(Execution exec) {
		
		int distributionQuantity = exec.getQuantity()/getValidNumberOfOrders()+ 1;
		int remainingQuantity = exec.getQuantity();
		
		List<BaseOrder> mergedList =Stream.concat(limitOrders.stream().filter(x -> x.getIsValid()), marketOrders.stream()).collect(Collectors.toList());
		
		for (BaseOrder order : mergedList) {
			if (remainingQuantity <= 0) {
				break;
			}
			if (remainingQuantity < distributionQuantity) {// means execution does not have as much capacity as the distribution 
				distributionQuantity = remainingQuantity;  // so will need to reset to remaining quantity
			}
			order.addToExecutions(new OrderExecution(exec.getExecId(), distributionQuantity));
			remainingQuantity = remainingQuantity - distributionQuantity;
		}
	}
	
	public void addToLimitOrders(LimitOrder order) {
		limitOrders.add(order);
	}
	
	public void addToMarketOrders(MarketOrder order) {
		marketOrders.add(order);
	}
	
	public void closeOrder() {
		isOpen = Boolean.FALSE;
	}
	
	public Integer getBookId() {
		return bookId;
	}

	public List<LimitOrder> getLimitOrders() {
		return limitOrders;
	}

	public List<MarketOrder> getMarketOrders() {
		return marketOrders;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}

	public Boolean getIsExecuted() {
		return isExecuted;
	}

	public ExecutionFamily getExecutionFamily() {
		return executionFamily;
	}

	

	
	
}
