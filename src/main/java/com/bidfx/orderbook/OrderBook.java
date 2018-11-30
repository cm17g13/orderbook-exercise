/*
 * Copyright (c) 2018. BidFX Systems Limited. All rights reserved.
 */

package com.bidfx.orderbook;

import java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents an order book for a share or stock. An {@code OrderBook}
 * should retain the state of the share, keeping track of the orders that are in
 * the market.
 *
 * @author BidFX Systems Limited
 */
@SuppressWarnings("all")
public class OrderBook {
	
	public LinkedHashMap<Double, Long> orderBook;
	
    public OrderBook() {
    	orderBook = new LinkedHashMap<Double, Long>();
    }
    
    
    public void addBuyOrder(double bidPrice, long bidSize) { 		
    	if(orderBook.isEmpty()) {
    		orderBook.put(bidPrice, bidSize);
    	} else {
    		if(orderBook.containsKey(bidPrice)) {
    			orderBook.put(bidPrice, orderBook.get(bidPrice) + bidSize); 
    		}
    	}
    }
    
    /*public String convertKey(String bid) {
    	if(bid.equals("bid")) {
    		return "BidSize";
    	}
    	return "BidSize" + bid.substring(3, bid.length()); 
    }*/
    
    public Double getKeyFromValue(Long value) {
        for (double key : orderBook.keySet()) {
        	if (orderBook.get(key).equals(value)) {
        		return key;
        	}
        }
        return null;
    }
    
    public int getIndexFromKey(double bidPrice) {
    	int count = 0;
    	for (double bid : orderBook.keySet()) {
        	if (bidPrice == bid) {
        		return count;
        	}
        	count++;
        }
    	return -1;
    }
    
    
    
    public Map<Double, Long> clone() {
    	return (Map<Double, Long>) orderBook.clone();
    }
    
    public boolean containsKey(double key) {
    	return orderBook.containsKey(key);
    }
    
    public boolean containsValue(long value) {
    	return orderBook.containsValue(value);
    }
    
    public Object getValueFromKey(double key) {
    	return orderBook.get(key);
    }
    
    public Set<Entry<Double, Long>> entrySet() {
    	return orderBook.entrySet();
    }
    
    

}
