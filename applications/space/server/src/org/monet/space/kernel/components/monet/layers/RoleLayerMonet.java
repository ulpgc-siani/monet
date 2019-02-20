package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.components.monet.federation.ComponentFederationMonet;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.producers.ProducerRole;
import org.monet.space.kernel.producers.ProducerRoleList;
import org.monet.space.kernel.producers.ProducersFactory;

import java.util.Date;
import java.util.List;

public class RoleLayerMonet extends LayerMonet implements RoleLayer {
	protected final ComponentFederationMonet componentFederationMonet;
	protected final ProducersFactory producersFactory = ProducersFactory.getInstance();
	protected final Context context = Context.getInstance();

	public RoleLayerMonet(ComponentFederationMonet componentFederationMonet) {
		this.componentFederationMonet = componentFederationMonet;
	}

	protected boolean isStarted() {
		return ComponentFederationMonet.started();
	}

	protected Account getAccount() {
		Session oSession = this.context.getCurrentSession();
		if (oSession == null) return null;
		return oSession.getAccount();
	}

	@Override
	public RoleList loadRoleList(String code, DataRequest dataRequest) {
		ProducerRoleList producerRoleList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRoleList = (ProducerRoleList) this.producersFactory.get(Producers.ROLELIST);

		return producerRoleList.load(code, dataRequest);
	}

	@Override
	public List<String> loadRoleListUsersIds(String codeRole) {
		ProducerRoleList producerRoleList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRoleList = this.producersFactory.get(Producers.ROLELIST);

		return producerRoleList.loadUsersIds(codeRole);
	}

	@Override
	public Role loadRole(String id) {
		ProducerRole producerRole;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);

		return producerRole.load(id);
	}

	@Override
	public boolean existsNonExpiredUserRole(String code, User user) {
		ProducerRole producerRole;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);

		return producerRole.existsNonExpiredUserRole(code, user.getId());
	}

	@Override
	public boolean existsNonExpiredUserRole(String code, String userId) {
		ProducerRole producerRole;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);

		return producerRole.existsNonExpiredUserRole(code, userId);
	}

	@Override
	public boolean existsNonExpiredServiceRole(String code, FederationUnit partner, FederationUnitService partnerService) {
		ProducerRole producerRole;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);

		return producerRole.existsNonExpiredServiceRole(code, partner, partnerService);
	}

	@Override
	public boolean existsNonExpiredFeederRole(String code, FederationUnit partner, FederationUnitFeeder partnerFeeder) {
		ProducerRole producerRole;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);

		return producerRole.existsNonExpiredFeederRole(code, partner, partnerFeeder);
	}

	@Override
	public Role addUserRole(String code, User user, Date beginDate, Date expireDate) {
		ProducerRole producerRole;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRole = this.producersFactory.get(Producers.ROLE);

		return producerRole.createForUser(code, user, beginDate, expireDate);
	}

	@Override
	public Role addServiceRole(String code, FederationUnit partner, FederationUnitService partnerService, Date beginDate, Date expireDate) {
		ProducerRole producerRole;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);

		return producerRole.createForService(code, partner, partnerService, beginDate, expireDate);
	}

	@Override
	public Role addFeederRole(String code, FederationUnit partner, FederationUnitFeeder partnerFeeder, Date beginDate, Date expireDate) {
		ProducerRole producerRole;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);

		return producerRole.createForFeeder(code, partner, partnerFeeder, beginDate, expireDate);
	}

	@Override
	public boolean saveRole(Role role) {
		ProducerRole producerRole;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);

		return producerRole.save(role);
	}

	@Override
	public boolean deleteRole(String id) {
		ProducerRole producerRole;
		Role role;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);

		role = this.loadRole(id);
		return producerRole.remove(role);
	}

}
