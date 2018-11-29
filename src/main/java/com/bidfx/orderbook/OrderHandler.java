/*
 * Copyright (c) 2018. BidFX Systems Limited. All rights reserved.
 */

package com.bidfx.orderbook;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * This class adapts orders coming from a remote price service and builds the
 * {@link OrderBook}. On receipt of a new order it publishes changes to the
 * order book as a map.
 *
 * @author BidFX Systems Limited
 */

@SuppressWarnings("all")
public class OrderHandler {
    private OrderBook orderBook = new OrderBook();

    public Map<String, Object> handleOrder(Order order) {
    	Map<String, Object> initial = orderBook.clone();
    	if(order.getSide() == Side.BID) {
    		orderBook.addBuyOrder(order.getPrice(), order.getSize());
    	} else {
    		//orderBook.addSellOrder(order.getPrice(), order.getSize());
    	}
    	return publishChangedLevels(initial);
    }

    private Map<String, Object> publishChangedLevels(Map<String, Object> initial) {
    	
    	Map<String, Object> changedLevels = new TreeMap<String, Object>();
    	
    	for(Entry<String, Object> pair : orderBook.entrySet()) {
	        String key = pair.getKey();
	        Object value = pair.getValue();
	        if(initial.get(key) != value) {
	        	changedLevels.put(key, value);
	        }
	
    	}    
        return changedLevels;
    }
}

