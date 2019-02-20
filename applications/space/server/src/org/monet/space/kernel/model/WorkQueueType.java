package org.monet.space.kernel.model;


public enum WorkQueueType {
	None,;

	public static WorkQueueType valueOf(int ordinal) {
		switch (ordinal) {
			case 0:
				return None;
		}
		return null;
	}
}