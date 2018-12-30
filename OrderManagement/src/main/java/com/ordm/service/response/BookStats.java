/**
 * 
 */
package com.ordm.service.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ordm.model.BaseOrder;

/**
 * @author VIJAY
 *
 */
public class BookStats {
	
	
	//Derived;
	
	private Integer noOfMarketOrders;
	
	private Integer noOfOrders;
	
	private Integer noOfValidOrders;
	
	private Integer noOfInvalidOrders;
	
	private Integer validDemand;// addition of all valid quantities
	
	private Integer inValidDemand; // addition of all invalid quanties
	
	private Integer totalDemand;
	
	private BaseOrder biggestOrder;
	
	private BaseOrder smallestOrder;
	
	private BaseOrder earliestOrder;
	
	private BaseOrder latestOrder;
	
	private Integer accumulatedExecQuantity;
	
	private BigDecimal execPrice;
	
	private List<LimitBreak> limitBreakDown;

	
	public void setNoOfOrders(Integer noOfOrders) {
		this.noOfOrders = noOfOrders;
	}

	public Integer getNoOfValidOrders() {
		return noOfValidOrders;
	}

	public void setNoOfValidOrders(Integer noOfValidOrders) {
		this.noOfValidOrders = noOfValidOrders;
	}

	public Integer getNoOfInvalidOrders() {
		return noOfInvalidOrders;
	}

	public void setNoOfInvalidOrders(Integer noOfInvalidOrders) {
		this.noOfInvalidOrders = noOfInvalidOrders;
	}

	public Integer getValidDemand() {
		return validDemand;
	}

	public void setValidDemand(Integer validDemand) {
		this.validDemand = validDemand;
	}

	public Integer getInValidDemand() {
		return inValidDemand;
	}

	public void setInValidDemand(Integer inValidDemand) {
		this.inValidDemand = inValidDemand;
	}

	public Integer getTotalDemand() {
		return totalDemand;
	}

	public void setTotalDemand(Integer totalDemand) {
		this.totalDemand = totalDemand;
	}

	public Integer getNoOfOrders() {
		return noOfOrders;
	}

	public BaseOrder getBiggestOrder() {
		return biggestOrder;
	}

	public void setBiggestOrder(BaseOrder biggestOrder) {
		this.biggestOrder = biggestOrder;
	}

	public BaseOrder getSmallestOrder() {
		return smallestOrder;
	}

	public void setSmallestOrder(BaseOrder smallestOrder) {
		this.smallestOrder = smallestOrder;
	}

	public BaseOrder getEarliestOrder() {
		return earliestOrder;
	}

	public void setEarliestOrder(BaseOrder earliestOrder) {
		this.earliestOrder = earliestOrder;
	}

	public BaseOrder getLatestOrder() {
		return latestOrder;
	}

	public void setLatestOrder(BaseOrder latestOrder) {
		this.latestOrder = latestOrder;
	}

	public Integer getAccumulatedExecQuantity() {
		return accumulatedExecQuantity;
	}

	public void setAccumulatedExecQuantity(Integer accumulatedExecQuantity) {
		this.accumulatedExecQuantity = accumulatedExecQuantity;
	}

	public BigDecimal getExecPrice() {
		return execPrice;
	}

	public void setExecPrice(BigDecimal execPrice) {
		this.execPrice = execPrice;
	}

	public Integer getNoOfMarketOrders() {
		return noOfMarketOrders;
	}

	public void setNoOfMarketOrders(Integer noOfMarketOrders) {
		this.noOfMarketOrders = noOfMarketOrders;
	}

	public List<LimitBreak> getLimitBreakDown() {
		return limitBreakDown;
	}

	public void setLimitBreakDown(List<LimitBreak> limitBreakDown) {
		this.limitBreakDown = limitBreakDown;
	}

		
	
	//

}
