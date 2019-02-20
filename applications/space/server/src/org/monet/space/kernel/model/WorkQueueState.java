package org.monet.space.kernel.model;


public enum WorkQueueState {
	Pending,
	Finish,
	Error;

	public static WorkQueueState valueOf(int ordinal) {
		switch (ordinal) {
			case 0:
				return Pending;
			case 1:
				return Finish;
			case 2:
				return Error;
		}
		return null;
	}
}