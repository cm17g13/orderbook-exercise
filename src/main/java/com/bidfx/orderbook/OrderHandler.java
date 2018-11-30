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
    		changedLevels = addedOrder(changedLevels, initial);
    	} else {
    		changedLevels = canceledOrder(changedLevels, initial);
    	}
        return changedLevels;
    }
    
    private Map<String, Object> addedOrder(Map<String, Object> changedLevels, Map<Double, Long> initial){
    	for(Entry<Double, Long> pair : orderBook.entrySet()) {
	        Double bidPrice = pair.getKey();
	        Long bidSize = pair.getValue();
	        int index = orderBook.getIndexFromKey(bidPrice);
	        int oldIndex = getIndexFromPrice(initial, bidPrice);
	        
	        if(index != oldIndex) {
	        	changedLevels = levelChange(changedLevels, index, bidPrice, bidSize);
	        	
	        } else if(initial.get(bidPrice) != bidSize)  {
	        	changedLevels = sizeChange(changedLevels, index, bidSize);  
	        }
    	}
    	return changedLevels;
    }
    
    private Map<String, Object> canceledOrder(Map<String, Object> changedLevels, Map<Double, Long> initial){
    	int count = 0; 
    	for(Entry<Double, Long> pair : initial.entrySet()) {
	        Double bidPrice = pair.getKey();
	        Long bidSize = pair.getValue();
	        int index = orderBook.getIndexFromKey(bidPrice);
	        int oldIndex = getIndexFromPrice(initial, bidPrice);
	        if(index != oldIndex && index != -1) {
	        	changedLevels = levelChange(changedLevels, index, bidPrice, bidSize);
	        } else if(orderBook.getIndexFromKey(bidPrice) != oldIndex) {
	        	changedLevels = levelRemoved(changedLevels, oldIndex);
	        }
    	}
    	return changedLevels;
    }
    
    private Map<String, Object> levelChange(Map<String, Object> changedLevels, int index, double bidPrice, long bidSize) {
    	String priceKey = "Bid" + addKeyIndexing(index);
    	String sizeKey = "BidSize" + addKeyIndexing(index);
    	changedLevels.put(priceKey, bidPrice);
    	changedLevels.put(sizeKey, bidSize);
    	return changedLevels;
    }
    
    private Map<String, Object> sizeChange(Map<String, Object> changedLevels, int index, long bidSize) {
    	String sizeKey = "BidSize" + addKeyIndexing(index);
    	changedLevels.put(sizeKey, bidSize);
    	return changedLevels;
    }
    
    private Map<String, Object> levelRemoved(Map<String, Object> changedLevels, int oldIndex) {
    	String priceKey = "Bid" + addKeyIndexing(oldIndex);
    	String sizeKey = "BidSize" + addKeyIndexing(oldIndex);
    	changedLevels.put(priceKey, null);
    	changedLevels.put(sizeKey, null);
    	return changedLevels;
    }
    
    
    private String addKeyIndexing(int index) {
    	return ((index > 1) ? Integer.toString(index) : "");
    }
    
    private long checkOrderSize(Order order) {
    	for (Order existingOrder : orders) {
        	if (order.getOrderId() == existingOrder.getOrderId()) {
        		return -existingOrder.getSize();
        	}
        }
    	return -1; 
    }
    
    public int getIndexFromPrice(Map<Double, Long> map, double key1) {
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

