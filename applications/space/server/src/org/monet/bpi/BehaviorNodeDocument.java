package org.monet.bpi;

public interface BehaviorNodeDocument extends BehaviorNode {
	public void onSign(User user);
	public void onSignsComplete();
}