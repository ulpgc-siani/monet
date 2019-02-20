package client.core.system;

import client.core.model.BusinessUnit;

import java.util.ArrayList;

public class BusinessUnitList extends MonetList<BusinessUnit> implements client.core.model.BusinessUnitList {

	public BusinessUnitList() {
	}

	public BusinessUnitList(BusinessUnit[] elements) {
		super(elements);
	}

	@Override
	public BusinessUnit[] getMembers() {
		java.util.List<BusinessUnit> result = new ArrayList<>();

		for (BusinessUnit businessUnit : this) {
			if (businessUnit.isMember())
				result.add(businessUnit);
		}

		return result.toArray(new BusinessUnit[result.size()]);
	}

	@Override
	public final BusinessUnit[] getPartners() {
		ArrayList<BusinessUnit> result = new ArrayList<>();

		for (BusinessUnit businessUnit : this) {
			if (businessUnit.isPartner())
				result.add(businessUnit);
		}

		return result.toArray(new BusinessUnit[result.size()]);
	}

}
