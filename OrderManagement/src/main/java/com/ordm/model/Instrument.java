package com.ordm.model;

import java.util.Map;
import java.util.Set;

import com.ordm.enums.Exchanges;
import com.ordm.enums.InstrumentIdentifier;

public class Instrument {

	private Integer instrumentId;
	
	private Map<InstrumentIdentifier,String> identifiers;
	
	private Set<Exchanges> listedExchanges;
	
	public Instrument(Integer instrumentId) {
		this.instrumentId = instrumentId;
	}

	public Integer getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Integer instrumentId) {
		this.instrumentId = instrumentId;
	}

	public Map<InstrumentIdentifier, String> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(Map<InstrumentIdentifier, String> identifiers) {
		this.identifiers = identifiers;
	}

	public Set<Exchanges> getListedExchanges() {
		return listedExchanges;
	}

	public void setListedExchanges(Set<Exchanges> listedExchanges) {
		this.listedExchanges = listedExchanges;
	}
}
