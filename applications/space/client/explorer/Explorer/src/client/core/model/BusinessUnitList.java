package client.core.model;

public interface BusinessUnitList extends List<BusinessUnit> {
	BusinessUnit[] getMembers();
	BusinessUnit[] getPartners();
}
