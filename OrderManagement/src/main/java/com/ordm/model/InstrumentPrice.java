package com.ordm.model;

import java.math.BigDecimal;

import com.ordm.enums.Exchanges;

public class InstrumentPrice {

	private Integer instrumentId;
	
	private BigDecimal price;
	
	private Exchanges exchange;

	public Integer getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Integer instrumentId) {
		this.instrumentId = instrumentId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Exchanges getExchange() {
		return exchange;
	}

	public void setExchange(Exchanges exchange) {
		this.exchange = exchange;
	}
}
