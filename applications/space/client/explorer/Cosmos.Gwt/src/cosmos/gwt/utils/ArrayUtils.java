package cosmos.gwt.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

	public static <T extends Object> List<T> union(T[] array1, T[] array2) {
		List<T> set1 = Arrays.asList(array1);
		List<T> set2 = Arrays.asList(array2);
		List<T> result = new ArrayList<>();

		result.addAll(set1);
		for (T element : set2) {
			if (set1.contains(element))
				continue;
			result.add(element);
		}

		return result;
	}

	public static <T extends Object> List<T> intersect(T[] array1, T[] array2) {
		List<T> set1 = new ArrayList<>(Arrays.asList(array1));
		List<T> set2 = Arrays.asList(array2);

		set1.retainAll(set2);

		return set1;
	}

}
