/*
 * Copyright (c) 2018. BidFX Systems Limited. All rights reserved.
 */

package com.bidfx.orderbook;

import java.util.ArrayList;
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
    private ArrayList<Order> orders = new ArrayList<Order>();
    
    public Map<String, Object> handleOrder(Order order) {
    	
    	Map<Double, Long> initial = orderBook.clone();
    	
    	long orderSize = order.getSize();
    	if(orderSize == 0) {
    		orderSize = checkOrderSize(order);
    	} 
    	
    	if(order.getSide() == Side.BID) {
    		orderBook.addBuyOrder(order.getPrice(), orderSize);
    	} else {
    		//orderBook.addSellOrder(order.getPrice(), order.getSize());
    	}
    	orders.add(order);
    	return publishChangedLevels(initial);
    }

    private Map<String, Object> publishChangedLevels(Map<Double, Long> initial) {
    	orderBook.sortOrderBook();
    	Map<String, Object> changedLevels = new TreeMap<String, Object>();
    	
    	if(orderBook.size() >= initial.size()) {
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
    	} else {
    		for(Entry<Double, Long> pair : initial.entrySet()) {
		        Double bidPrice = pair.getKey();
		        Long bidSize = pair.getValue();
		        int index = orderBook.getIndexFromKey(bidPrice);
		        int oldIndex = getIndexFromKey(initial, bidPrice);
		        
		        if(index != oldIndex) {
		        	String priceKey = "Bid" + ((oldIndex > 1) ? oldIndex : "");
		        	String sizeKey = "BidSize" + ((oldIndex > 1) ? oldIndex : "");
		        	changedLevels.put(priceKey, null);
		        	changedLevels.put(sizeKey, null);
		        } 
		
	    	}
    	}
        return changedLevels;
    }
    
    private long checkOrderSize(Order order) {
    	for (Order existingOrder : orders) {
        	if (order.getOrderId() == existingOrder.getOrderId()) {
        		return -existingOrder.getSize();
        	}
        }
    	return -1; 
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

