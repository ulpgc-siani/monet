package org.monet.space.kernel.model;

import java.util.*;
import java.util.Map.Entry;

public abstract class SerializerData {

	private static final String ITEM_SEPARATOR = "#";
	private static final String ITEM_CODE_SEPARATOR = "=";

	public static <T extends Map<String, String>> T deserialize(String data) {
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();

		if (data == null || data.isEmpty())
			return (T) result;

		String[] itemsArray = data.split(SerializerData.ITEM_SEPARATOR);
		for (int i = 0; i < itemsArray.length; i++) {
			String[] itemArray = itemsArray[i].split(SerializerData.ITEM_CODE_SEPARATOR);
			if (itemArray.length == 0)
				continue;
			result.put(itemArray[0], ((itemArray.length > 1) ? itemArray[1] : ""));
		}

		return (T) result;
	}

	public static <T extends Set<String>> T deserializeSet(String data) {
		LinkedHashSet<String> result = new LinkedHashSet<String>();

		if (data == null || data.isEmpty())
			return (T) result;

		String[] itemsArray = data.split(SerializerData.ITEM_SEPARATOR);
		for (int i = 0; i < itemsArray.length; i++)
			result.add(itemsArray[i]);

		return (T) result;
	}

	public static <S> String serialize(Map<String, S> data) {
		String result = "";

		if (data == null)
			return "";

		for (Entry<String, S> entry : data.entrySet())
			result += entry.getKey() + SerializerData.ITEM_CODE_SEPARATOR + entry.getValue() + SerializerData.ITEM_SEPARATOR;

		if (result.length() > 0)
			result = result.substring(0, result.length() - 1);

		return result;
	}

	public static String serializeSet(Set<String> data) {
		String result = "";

		if (data == null)
			return "";

		for (String value : data)
			result += value + SerializerData.ITEM_SEPARATOR;

		if (result.length() > 0)
			result = result.substring(0, result.length() - 1);

		return result;
	}

}
