package org.monet.bpi;

public interface BehaviorNodeDocument extends BehaviorNode { 
  /**
   * Event called after document is signed.
   */
  public void onSign(User user);

  /**
   * Event called after all signs are made.
   */
  public void onSignsComplete();
}