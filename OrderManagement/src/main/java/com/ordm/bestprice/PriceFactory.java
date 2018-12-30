/**
 * 
 */
package com.ordm.bestprice;

import java.util.HashMap;
import java.util.Map;

import com.ordm.dao.InstrumentDAO;
import com.ordm.enums.PricePickerTypes;
import com.ordm.model.Instrument;
import com.ordm.model.InstrumentPrice;

/**
 * @author VIJAY
 *
 */
public final class PriceFactory {
	
	private static PriceFactory factory = new PriceFactory();
	
	private InstrumentDAO instDAO = null;
	
	private Map<PricePickerTypes,PricePicker> pricePickers = new HashMap<PricePickerTypes,PricePicker>();
	

	private void populatePricePickers() {
		pricePickers.put(PricePickerTypes.PRICEBYEXCHANGE, new PriceByExchange());
	}
	

	private PriceFactory() {
		populatePricePickers();
	}
		
	public InstrumentPrice fetchPrice(Integer instumentId) {
		Instrument itr =instDAO.getInstrument(instumentId);
		return pricePickers.get(PricePickerTypes.PRICEBYEXCHANGE).fetchPrice(itr);
		
	}
	
	public static PriceFactory getInstance() {
		return factory;
	}
}
