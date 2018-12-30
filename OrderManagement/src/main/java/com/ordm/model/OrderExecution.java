/**
 * 
 */
package com.ordm.model;

/**
 * @author VIJAY
 *
 */
public class OrderExecution {
	
	public OrderExecution(Integer executionId, Integer quantity) {
		this.executionId=executionId;
		this.executedQuantity = quantity;
	}
	
	private Integer executionId;
	
	private Integer executedQuantity;

	public Integer getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(Integer executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	public Integer getExecutionId() {
		return executionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((executionId == null) ? 0 : executionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderExecution other = (OrderExecution) obj;
		if (executionId == null) {
			if (other.executionId != null)
				return false;
		} else if (!executionId.equals(other.executionId))
			return false;
		return true;
	}

}
