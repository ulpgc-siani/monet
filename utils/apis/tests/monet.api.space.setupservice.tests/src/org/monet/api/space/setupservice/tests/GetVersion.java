package org.monet.api.space.setupservice.tests;

import org.junit.Test;
import org.monet.api.setupservice.SetupserviceApi;
import org.monet.api.setupservice.impl.SetupserviceApiImpl;
import org.monet.api.space.setupservice.utils.Resources;

public class GetVersion {

  @Test
  public void test() {
    SetupserviceApi api = new SetupserviceApiImpl("http://localhost/monet/setupservice", Resources.getFullPath("/monet.p12"), "1234");
    assert(api.getVersion() == "3.0");
  }

}
