/**
 * 
 */
package com.ordm.service.response;

import java.math.BigDecimal;

/**
 * @author VIJAY
 *
 */
public class LimitBreak {
	
	private BigDecimal limitPrice;
	
	private BigDecimal demandPerPrice;
	
	public LimitBreak(BigDecimal limitPrice,BigDecimal demandPerPrice) {
		this.limitPrice=limitPrice;
		this.demandPerPrice=demandPerPrice;
	}

	public BigDecimal getLimitPrice() {
		return limitPrice;
	}

	public BigDecimal getDemandPerPrice() {
		return demandPerPrice;
	}

}
