package org.monet.space.mobile.mvp;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusProvider {

  private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

  public static Bus get() {
    return BUS;
  }

  private BusProvider() {

  }

}
