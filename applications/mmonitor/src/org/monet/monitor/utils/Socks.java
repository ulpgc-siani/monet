package org.monet.monitor.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monet.monitor.configuration.Configuration;

public class Socks {

  private Logger logger;

  Socks() {
    logger = LogManager.getLogger(this.getClass());
  }

  public void enable() throws Configuration.ConfigurationException {
    logger.info("Proxy socks enabled.");
    System.setProperty("socksProxyHost", Configuration.getSocksHost());
    System.setProperty("socksProxyPort", Configuration.getSocksPort());
  }

  public void disabled() {
    logger.debug("Proxy socks disabled.");
    System.setProperty("socksProxyHost", "");
    System.setProperty("socksProxyPort", "");
  }
}
