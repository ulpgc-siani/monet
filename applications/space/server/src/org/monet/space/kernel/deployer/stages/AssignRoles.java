package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.AbstractManifestBase.AssignRoleProperty;
import org.monet.metamodel.AbstractManifestBase.AssignRoleProperty.PartnerProperty;
import org.monet.metamodel.Distribution;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.model.*;

import java.util.ArrayList;
import java.util.Date;

public class AssignRoles extends Stage {

	@Override
	public void execute() {
		Dictionary dictionary = Dictionary.getInstance();
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Distribution distribution = businessUnit.getDistribution();
		ArrayList<AssignRoleProperty> assignmentDefinitions = distribution.getAssignRoleList();
		ComponentFederation componentFederation = this.globalData.getData(ComponentFederation.class, GlobalData.COMPONENT_FEDERATION);
		FederationLayer federationLayer = componentFederation.getDefaultLayer();
		RoleLayer roleLayer = componentFederation.getRoleLayer();

		for (AssignRoleProperty assignmentDefinition : assignmentDefinitions) {
			if (assignmentDefinition.getUser() != null)
				this.assignRoleToUser(dictionary, federationLayer, roleLayer, assignmentDefinition);
			else if (assignmentDefinition.getPartner() != null)
				this.assignRoleToService(dictionary, federationLayer, roleLayer, assignmentDefinition);
		}
	}

	private void assignRoleToUser(Dictionary dictionary, FederationLayer federationLayer, RoleLayer roleLayer, AssignRoleProperty assignmentDefinition) {
		Account account = federationLayer.locateAccount(assignmentDefinition.getUser());

		if (account == null) {
			UserInfo userInfo = new UserInfo();
			userInfo.setFullname(assignmentDefinition.getUser());
			account = federationLayer.createAccount(null, assignmentDefinition.getUser(), userInfo);
		}

		User user = account.getUser();
		for (Ref roleRef : assignmentDefinition.getRole()) {
			String codeRole = dictionary.getDefinitionCode(roleRef.getValue());
			if (roleLayer.existsNonExpiredUserRole(codeRole, user)) continue;
			roleLayer.addUserRole(codeRole, user, new Date(), null);
		}

		federationLayer.createOrUpdateAccount(account);
	}

	private void assignRoleToService(Dictionary dictionary, FederationLayer federationLayer, RoleLayer roleLayer, AssignRoleProperty assignmentDefinition) {
		PartnerProperty serviceDefinition = assignmentDefinition.getPartner();
		FederationUnit partner = federationLayer.locatePartner(serviceDefinition.getName());
		ArrayList<String> partnerServiceNames = serviceDefinition.getService();
		ArrayList<FederationUnitService> partnerServiceList = new ArrayList<FederationUnitService>();

		if (partner == null)
			return;

		for (String partnerServiceName : partnerServiceNames) {
			FederationUnitService partnerService = partner.getService(partnerServiceName);

			if (partnerService != null)
				partnerServiceList.add(partnerService);
		}

		for (FederationUnitService partnerService : partnerServiceList) {
			for (Ref roleRef : assignmentDefinition.getRole()) {
				String codeRole = dictionary.getDefinitionCode(roleRef.getValue());
				if (roleLayer.existsNonExpiredServiceRole(codeRole, partner, partnerService)) continue;
				roleLayer.addServiceRole(codeRole, partner, partnerService, new Date(), null);
			}
		}
	}

	@Override
	public String getStepInfo() {
		return "Assigning roles";
	}
}
