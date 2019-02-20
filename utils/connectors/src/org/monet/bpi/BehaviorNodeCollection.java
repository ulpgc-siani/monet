package org.monet.bpi;

public interface BehaviorNodeCollection extends BehaviorNode {

	public void onItemAdded(Node newItem);

	public void onItemRemoved(Node removedItem);

}