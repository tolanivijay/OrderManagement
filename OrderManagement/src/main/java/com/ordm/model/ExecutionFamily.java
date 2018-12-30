/**
 * 
 */
package com.ordm.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author VIJAY
 *
 */
public class ExecutionFamily {
	
	private BigDecimal price;
	
	private Set<Execution> executions = new HashSet<Execution>();

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Set<Execution> getExecutions() {
		return executions;
	}

	public void addToExecutions(Execution exec) {
		executions.add(exec);
	}
	
	

}
