/*
 * Copyright (c) 2018. BidFX Systems Limited. All rights reserved.
 */

package com.bidfx.orderbook;

import java.util.Map.Entry;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
	private double bestPrice;
	
    public OrderBook() {
    	orderBook = new LinkedHashMap<Double, Long>();
    }
    
    
    public void addBuyOrder(double bidPrice, long bidSize) { 		
    	if(orderBook.isEmpty()) {
    		orderBook.put(bidPrice, bidSize);
    	} else {
    		if(orderBook.containsKey(bidPrice)) {
    			orderBook.put(bidPrice, orderBook.get(bidPrice) + bidSize); 
    		} else {
    			orderBook.put(bidPrice, bidSize);
    		}
    		
    	}
    }
    
    public Double getKeyFromValue(Long value) {
        for (double key : orderBook.keySet()) {
        	if (orderBook.get(key).equals(value)) {
        		return key;
        	}
        }
        return null;
    }
    
    public int getIndexFromKey(double bidPrice) {
    	int count = 1;
    	for (double bid : orderBook.keySet()) {
        	if (bidPrice == bid) {
        		return count;
        	}
        	count++;
        }
    	return -1;
    }
    
    public void sortOrderBook() {
    	LinkedHashMap<Double, Long> sortedOrderBook = new LinkedHashMap<Double, Long>();
    	orderBook.entrySet()                   // Set<Entry<String, String>>
        .stream()                   // Stream<Entry<String, String>>
        .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
        .forEachOrdered(entry -> 
        sortedOrderBook.put(entry.getKey(), entry.getValue()));
    	orderBook = sortedOrderBook;
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
