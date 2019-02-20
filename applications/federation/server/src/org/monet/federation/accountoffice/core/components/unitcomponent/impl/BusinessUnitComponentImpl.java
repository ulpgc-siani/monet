package org.monet.federation.accountoffice.core.components.unitcomponent.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.federation.accountoffice.core.components.unitcomponent.BusinessUnitComponent;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.BusinessUnit;
import org.monet.federation.accountoffice.core.model.BusinessUnitList;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Singleton
public class BusinessUnitComponentImpl implements BusinessUnitComponent {

  private BusinessUnitList businessUnitList;
  private DataRepository   dataRepository;

  @Inject
  public BusinessUnitComponentImpl(Logger logger, DataRepository dataRepository) {
    this.dataRepository = dataRepository;
    this.businessUnitList = this.dataRepository.loadBusinessUnitsOfType(BusinessUnit.Type.MEMBER);
  }

  @Override
  public synchronized void enableBusinessUnit(String name) {
    BusinessUnit businessUnit = this.getBusinessUnitFromCache(name);
    if (businessUnit != null) {
      businessUnit.setEnable(true);
      this.businessUnitList.add(businessUnit);
    }
  }

  @Override
  public synchronized void disableBusinessUnit(String name) {
    BusinessUnit unit = this.getBusinessUnitFromCache(name);
    if (unit != null) {
      unit.setEnable(false);
      this.businessUnitList.add(unit);
    }
  }

  @Override
  public String getBusinessUnitSecret(String name) {
    BusinessUnit unit = this.getBusinessUnitFromCache(name);
    return unit.getSecret();
  }

  @Override
  public BusinessUnit getBusinessUnit(String name) {
    return this.getBusinessUnitFromCache(name);
  }

  @Override
  public boolean checkInetAddres(InetAddress ip) throws UnknownHostException {
    this.businessUnitList = this.dataRepository.loadBusinessUnitsOfType(BusinessUnit.Type.MEMBER);
    for (BusinessUnit businessUnit : this.businessUnitList.getAll()) {
      InetAddress host = null;
      if (ip instanceof Inet4Address)
        host = Inet4Address.getByName(businessUnit.getUri().getHost());
      else
        host = Inet6Address.getByName(businessUnit.getUri().getHost());
      if (host.equals(ip))
        return true;
    }
    return false;
  }

  @Override
  public void disableAllBusinessUnits() {
    for (BusinessUnit businessUnit : this.businessUnitList.getAll()) {
      businessUnit.setEnable(false);
    }
  }

  @Override
  public void setBusinessUnitLogo(String name, String logoUrl) {
    this.businessUnitList.getByName(name).setLogoUrl(logoUrl);
  }

  private BusinessUnit getBusinessUnitFromCache(String name) {
    BusinessUnit oldBusinessUnit = this.businessUnitList.getByName(name);
    if (oldBusinessUnit == null) {
      oldBusinessUnit = this.dataRepository.loadBusinessUnit(name);
      if (oldBusinessUnit != null)
        this.businessUnitList.add(oldBusinessUnit);
    } else {
      BusinessUnit unit = this.dataRepository.loadBusinessUnit(name);
      unit.setLabel(oldBusinessUnit.getLabel());
      unit.setLogoUrl(oldBusinessUnit.getLogoUrl());
      unit.setEnable(oldBusinessUnit.isEnable());
      unit.setVisible(oldBusinessUnit.isVisible());
      this.businessUnitList.remove(name);
      this.businessUnitList.add(unit);
      return unit;
    }
    return oldBusinessUnit;
  }

}
