/**
 * 
 */
package com.ordm.model;

import java.math.BigDecimal;

/**
 * @author VIJAY
 *
 */
public class Execution {

	private Integer quantity;
	
	private BigDecimal price;
	
	private Integer execId;
	
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

	public Integer getExecId() {
		return execId;
	}

	public void setExecId(Integer execId) {
		this.execId = execId;
	}

	
}
