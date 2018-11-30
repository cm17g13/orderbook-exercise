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
    	
    	Map<Double, Long> initial = orderBook.clone();
    	System.out.println();
    	if(order.getSide() == Side.BID) {
    		orderBook.addBuyOrder(order.getPrice(), order.getSize());
    	} else {
    		//orderBook.addSellOrder(order.getPrice(), order.getSize());
    	}
    	return publishChangedLevels(initial);
    }

    private Map<String, Object> publishChangedLevels(Map<Double, Long> initial) {
    	orderBook.sortOrderBook();
    	Map<String, Object> changedLevels = new TreeMap<String, Object>();
    	for(Entry<Double, Long> pair : orderBook.entrySet()) {
	        Double bidPrice = pair.getKey();
	        Long bidSize = pair.getValue();
	        int index = orderBook.getIndexFromKey(bidPrice);
	        int oldIndex = getIndexFromKey(initial, bidPrice);
	        if(index != oldIndex) {
	        	String priceKey = "Bid" + ((index > 1) ? index : "");
	        	String sizeKey = "BidSize" + ((index > 1) ? index : "");
	        	changedLevels.put(priceKey, bidPrice);
	        	changedLevels.put(sizeKey, bidSize);
	        } else {
	        
		        if(initial.get(bidPrice) != bidSize) {	        	
		        	String key = "BidSize" + ((index > 1) ? index : "");
		        	changedLevels.put(key, bidSize);
		        }
	        }
	
    	}    
        return changedLevels;
    }
    
    public int getIndexFromKey(Map<Double, Long> map, double key1) {
    	int count = 1;
    	for (Double key2 : map.keySet()) {
        	if (key1 == key2) {
        		return count;
        	}
        	count++;
        }
    	return -1;
    }
    
}

