package com.ordm.staticdata;

import java.util.HashMap;
import java.util.Map;

public final class OrdMappings {

	private final Map<String,String> priceExceptionCodes = new HashMap<String,String>();
	
	private final Map<String,String> ordmServiceErrorCodes = new HashMap<String,String>();;
	
	public OrdMappings() {
		populatePriceExceptionCodes();
		populateOrdmServiceErrorCodes();
	}
	
	private void populateOrdmServiceErrorCodes() {
		ordmServiceErrorCodes.put("BKC", "Requested Book is closed. Cannot add any further orders");
		ordmServiceErrorCodes.put("BNF", "Requested Book not found");
		ordmServiceErrorCodes.put("BGEN", "General Error. Please contact admin");
		ordmServiceErrorCodes.put("BINST", "General Error. Please contact admin");
	}
	
	private void populatePriceExceptionCodes() {
		priceExceptionCodes.put("PNF", "Instrument price not found");
		priceExceptionCodes.put("INV", "Invalid Instrument");
	}
}
