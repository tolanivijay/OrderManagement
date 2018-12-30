package com.ordm.bestprice;

import com.ordm.model.Instrument;
import com.ordm.model.InstrumentPrice;

public interface PricePicker {

	public InstrumentPrice fetchPrice(Instrument instrument) ;
}
