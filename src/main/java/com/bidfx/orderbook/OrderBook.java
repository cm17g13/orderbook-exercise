/*
 * Copyright (c) 2018. BidFX Systems Limited. All rights reserved.
 */

package com.bidfx.orderbook;

import java.util.TreeMap;
import java.util.Map.Entry;
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
	
	public TreeMap<String, Object> orderBook;
	
    public OrderBook() {
    	orderBook = new TreeMap<String, Object>();
    }
    
    public void addBuyOrder(double bidPrice, long bidSize) { 		
    	if(orderBook.isEmpty()) {
    		orderBook.put(PriceFields.BID, bidPrice);
    		orderBook.put(PriceFields.BID_SIZE, bidSize);
    	}
    }
    
    public TreeMap<String, Object> getOrderBook() {
    	return orderBook;
    }
    
    public Map<String, Object> clone() {
    	return (Map<String, Object>) orderBook.clone();
    }
    
    public boolean containsKey(String key) {
    	return orderBook.containsKey(key);
    }
    
    public boolean containsValue(Object value) {
    	return orderBook.containsValue(value);
    }
    
    public Object get(String key) {
    	return orderBook.get(key);
    }
    
    public Set<Entry<String, Object>> entrySet() {
    	return orderBook.entrySet();
    }
    
    

}
