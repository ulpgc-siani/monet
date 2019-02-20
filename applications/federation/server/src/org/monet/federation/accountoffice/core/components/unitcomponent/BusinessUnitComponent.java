package org.monet.federation.accountoffice.core.components.unitcomponent;

import org.monet.federation.accountoffice.core.model.BusinessUnit;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface BusinessUnitComponent {
  
  public void enableBusinessUnit(String name);

  public void disableBusinessUnit(String name);

  public String getBusinessUnitSecret(String name);

  public BusinessUnit getBusinessUnit(String name);

  public boolean checkInetAddres(InetAddress ip) throws UnknownHostException;

  public void disableAllBusinessUnits();

  public void setBusinessUnitLogo(String name, String logoUrl);
}
