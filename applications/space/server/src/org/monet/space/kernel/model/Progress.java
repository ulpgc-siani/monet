package org.monet.space.kernel.model;


public class Progress {

	private long time;
	private long timeLeft;
	private long estimatedTime;
	private long progress;
	private int relativeProgress;

	public Progress() {
		this.time = 0;
		this.timeLeft = 0;
		this.estimatedTime = 0;
		this.progress = 0;
		this.relativeProgress = 0;
	}

	public void update(int relativeProgress, long progress, long time, long estimatedTime, long timeLeft) {
		this.relativeProgress = relativeProgress;
		this.progress = progress;
		this.time = time;
		this.estimatedTime = estimatedTime;
		this.timeLeft = timeLeft;
	}

	public int getValue() {
		return this.relativeProgress;
	}

	public long getCurrentCount() {
		return this.progress;
	}

	public long getTimeElapsed() {
		return this.time;
	}

	public long getEstimatedTime() {
		return this.estimatedTime;
	}

	public long getTimeLeft() {
		return this.timeLeft;
	}

}