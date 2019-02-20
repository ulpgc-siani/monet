package org.monet.bpi;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class BPIOrderExpression {

	private LinkedHashMap<BPIParam, OrderType> orderFields = new LinkedHashMap<BPIParam, OrderType>();
	
	public enum OrderType { ASC, DES }
	
	public void add(BPIParam field, OrderType type) {
		orderFields.put(field, type);
	}
	
	public void add(BPIParam field) {
		this.add(field, OrderType.ASC);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(Entry<BPIParam, OrderType> entry : this.orderFields.entrySet()) {
			builder.append(entry.getKey().getColumn());
			builder.append(" ");
			builder.append(entry.getValue().toString());
			builder.append(", ");
		}
		if(builder.length() > 0)
			builder.delete(builder.length() - 2, builder.length());
		return builder.toString();
	}
	
}
