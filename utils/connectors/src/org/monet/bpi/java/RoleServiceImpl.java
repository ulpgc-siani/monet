package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.Role;
import org.monet.bpi.RoleService;
import org.monet.bpi.types.Date;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;

import java.util.List;

public class RoleServiceImpl extends RoleService {

	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(RoleServiceImpl service) {
		instance = service;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	@Override
	public List<Role> getNonExpiredRolesImpl(String name) {
		throw new NotImplementedException();
	}

	@Override
	public Role getRoleImpl(String id) {
		throw new NotImplementedException();
	}

	public static void init() {
		instance = new RoleServiceImpl();
	}

	@Override
	protected Role assignRoleToUserImpl(String name, String username, Date from, Date to) {
		throw new NotImplementedException();
	}

	@Override
	protected Role assignRoleToServiceImpl(String name, String partnerName, String serviceName, Date from, Date to) {
		throw new NotImplementedException();
	}

}
