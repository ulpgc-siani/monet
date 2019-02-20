package client.presenters.displays;

import client.core.model.BusinessUnit;
import client.core.model.BusinessUnitList;
import client.presenters.operations.ShowBusinessUnitOperation;
import client.services.AccountService;
import client.services.callback.BusinessUnitListCallback;
import cosmos.presenters.Operation;

public class BusinessUnitListDisplay extends Display {
	public static final Type TYPE = new Type("BusinessUnitListDisplay", Display.TYPE);
	private BusinessUnit[] memberList = null;
	private BusinessUnit[] partnerList = null;

	public BusinessUnitListDisplay() {
	}

	@Override
	protected void onInjectServices() {
	}

	public void loadMembers() {
		load(BusinessUnit.Type.MEMBER);
	}

	public void loadPartners() {
		load(BusinessUnit.Type.PARTNER);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	private void load(final BusinessUnit.Type type) {

		if (memberList == null || partnerList == null) {
			loadFromServer(type);
			return;
		}

		notify(type);
	}

	private void loadFromServer(final BusinessUnit.Type type) {
		AccountService service = this.services.getAccountService();

		service.loadBusinessUnits(new BusinessUnitListCallback() {
			@Override
			public void success(BusinessUnitList businessUnitList) {
				memberList = businessUnitList.getMembers();
				partnerList = businessUnitList.getPartners();
				BusinessUnitListDisplay.this.notify(type);
			}

			@Override
			public void failure(String error) {

			}
		});
	}

	private void notify(final BusinessUnit.Type type) {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				if (type == BusinessUnit.Type.MEMBER)
					hook.members(memberList);
				else
					hook.partners(memberList);
			}
		});
	}

	public void showBusinessUnit(BusinessUnit businessUnit) {
		ShowBusinessUnitOperation operation = new ShowBusinessUnitOperation(new Operation.NullContext(), businessUnit);
		operation.inject(services);
		operation.execute();
	}

	public interface Hook extends Display.Hook {
		void members(BusinessUnit[] members);
		void partners(BusinessUnit[] partners);
	}
}
