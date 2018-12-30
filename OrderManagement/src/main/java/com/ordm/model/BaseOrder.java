package com.ordm.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseOrder implements Cloneable{

	private Integer orderId;
	private Integer instrumentId;
	private Integer quantity;
	private BigDecimal price;
	private LocalDate entryDate;
	private LocalDateTime createdTime; 
	private Boolean isValid= Boolean.TRUE;
	
	private Set<OrderExecution> executions = new HashSet<OrderExecution>();
	
	@JsonProperty(value="remainingQuantity")
	public int getRemainingQuantity() {
		return quantity - getExecutedQuantity();
	}
	
	@JsonProperty(value="executedQuantity")
	public int getExecutedQuantity() {
		return executions.stream().mapToInt(x -> x.getExecutedQuantity()).sum();
	}
	
	public void addToExecutions(OrderExecution exec) {
		executions.add(exec);
	}
	public Integer getInstrumentId() {
		return instrumentId;
	}
	public void setInstrumentId(Integer instrumentId) {
		this.instrumentId = instrumentId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public LocalDate getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Set<OrderExecution> getExecutions() {
		return executions;
	}

}
