package org.monet.metamodel.internal;


public class Time {

	private long timestamp = 0;

	public Time(String time) {
		String[] split_1 = time.split(",");
		if (split_1.length > 1) {
			this.timestamp += Integer.parseInt(split_1[1]);
		}

		split_1 = split_1[0].split(":");
		int j = 0;
		for (int i = split_1.length - 1; i > -1; i--) {
			int value = Integer.parseInt(split_1[i]);
			if (j > 2)
				value *= 86400000;
			else if (j > 1)
				value *= 3600000;
			else if (j > 0)
				value *= 60000;
			else
				value *= 1000;

			this.timestamp += value;
			j++;
		}
	}

	public Time(long time) {
		this.timestamp = time;
	}

	public long getTime() {
		return this.timestamp;
	}

}
