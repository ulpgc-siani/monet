package org.monet.federation.accountoffice.core.logger.impl;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoggerImpl implements org.monet.federation.accountoffice.core.logger.Logger {

  private Logger log;
  
  public LoggerImpl(String typeName) {
    log = Logger.getLogger(typeName); 
  }
  
  public LoggerImpl(Logger logger) {
    log = logger; 
  }
  
  public void debug(String msg) {
    log.debug(msg);
  }

  public void debug(String format, Object... args) {
    log.debug(String.format(format, args));
  }
  
  public void debug(String msg, Throwable t) {
    log.debug(msg, t);
  }

  public void error(String msg) {
    log.error(msg);
  }

  public void error(String format, Object... args) {
    log.error(String.format(format, args));
  }

  public void error(String msg, Throwable t) {
    log.error(msg, t);
  }

  public String getName() {
    return log.getName();
  }

  public void info(String msg) {
    log.info(msg);
  }

  public void info(String format, Object... args) {
    log.info(String.format(format, args));
  }

  public void info(String msg, Throwable t) {
    log.info(msg, t);
  }

  public boolean isDebugEnabled() {
    return log.isDebugEnabled();
  }

  public boolean isErrorEnabled() {
    return log.isEnabledFor(Level.ERROR);
  }

  public boolean isInfoEnabled() {
    return log.isInfoEnabled();
  }

  public boolean isTraceEnabled() {
    return log.isTraceEnabled();
  }

  public boolean isWarnEnabled() {
    return log.isEnabledFor(Level.WARN);
  }

  public void trace(String msg) {
    log.trace(msg);
  }

  public void trace(String format, Object... args) {
    log.trace(String.format(format, args));
  }

  public void trace(String msg, Throwable t) {
    log.trace(msg, t);
  }

  public void warn(String msg) {
    log.warn(msg);
  }

  public void warn(String format, Object... args) {
    log.warn(String.format(format, args));
  }

  public void warn(String msg, Throwable t) {
    log.warn(msg, t);
  }

  public static org.monet.federation.accountoffice.core.logger.Logger getRootLogger() {
    return new LoggerImpl(Logger.getRootLogger());
  }

}
