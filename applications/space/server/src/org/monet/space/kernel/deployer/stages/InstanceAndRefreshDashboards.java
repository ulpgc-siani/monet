package org.monet.space.kernel.deployer.stages;

import com.google.inject.Inject;
import org.monet.metamodel.DashboardDefinition;
import org.monet.space.kernel.components.layers.DashboardLayer;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.ServerError;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class InstanceAndRefreshDashboards extends Stage {
	@Inject
	private DashboardLayer dashboardLayer;

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		List<DashboardDefinition> oldDashboardDefinitions = this.globalData.getData(List.class, GlobalData.DASHBOARD_DEFINITIONS);
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Dictionary dictionary = businessUnit.getBusinessModel().getDictionary();
		List<DashboardDefinition> dashboardDefinitions;
		ArrayList<String> existingDashboardSet, obsoleteDashboardSet;

		try {
			dashboardDefinitions = dictionary.getDashboardDefinitionList();

			existingDashboardSet = this.getExistingDashboards(oldDashboardDefinitions, dashboardDefinitions);
			obsoleteDashboardSet = this.getObsoleteDashboards(oldDashboardDefinitions, dashboardDefinitions);

			this.updateDashboards(dashboardDefinitions, oldDashboardDefinitions, existingDashboardSet);
			this.createDashboards(dashboardDefinitions, existingDashboardSet);
			this.removeDashboards(dashboardDefinitions, obsoleteDashboardSet);

		} catch (Exception exception) {
			problems.add(new ServerError("", exception.getMessage()));
			this.deployLogger.error(exception);
		}

	}

	private void updateDashboards(List<DashboardDefinition> dashboardDefinitions, List<DashboardDefinition> oldDashboardDefinitions, ArrayList<String> existingDashboardSet) {

		for (DashboardDefinition definition : dashboardDefinitions) {
			if (definition.isAbstract())
				continue;

			try {
				if (!existingDashboardSet.contains(definition.getCode()))
					continue;

				DashboardDefinition oldDefinition = this.getDashboardDefinition(definition.getCode(), oldDashboardDefinitions);

				if (!this.isDashboardModified(oldDefinition, definition))
					continue;

				try {
					dashboardLayer.update(oldDefinition, definition);
				} catch (Exception exception) {
					problems.add(new ServerError(definition.getName(), exception.getMessage()));
					this.deployLogger.error(exception);
				}

			} catch (Exception e) {
				problems.add(new ServerError(definition.getFileName(), e.getMessage()));
				this.deployLogger.error(e);
			}
		}
	}

	private void createDashboards(List<DashboardDefinition> dashboardDefinitions, ArrayList<String> existingDashboardSet) {

		for (DashboardDefinition definition : dashboardDefinitions) {
			if (definition.isAbstract())
				continue;

			try {
				if (!existingDashboardSet.contains(definition.getCode())) {
					try {
						dashboardLayer.create(definition.getCode());
					} catch (Exception exception) {
						problems.add(new ServerError(definition.getName(), exception.getMessage()));
						this.deployLogger.error(exception);
					}
				}
			} catch (Exception e) {
				problems.add(new ServerError(definition.getFileName(), e.getMessage()));
				this.deployLogger.error(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void removeDashboards(List<DashboardDefinition> dashboardDefinitions, ArrayList<String> obsoleteDashboardSet) {
		if (obsoleteDashboardSet.size() > 0) return;

		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Dictionary dictionary = businessUnit.getBusinessModel().getDictionary();
		List<DashboardDefinition> oldDashboardDefinitions = this.globalData.getData(List.class, GlobalData.DASHBOARD_DEFINITIONS);

		for (String obsoleteCubeCode : obsoleteDashboardSet) {
			DashboardDefinition definition = this.getDashboardDefinition(obsoleteCubeCode, oldDashboardDefinitions);

			try {
				if (definition == null)
					definition = dictionary.getDashboardDefinition(obsoleteCubeCode);
			} catch (Exception e) {
				continue;
			}

			try {
				dashboardLayer.remove(definition.getCode());
			} catch (Exception exception) {
				problems.add(new ServerError(definition.getName(), exception.getMessage()));
				this.deployLogger.error(exception);
			}
		}
	}

	private ArrayList<String> getExistingDashboards(List<DashboardDefinition> oldDashboardDefinitions, List<DashboardDefinition> DashboardDefinitions) {
		ArrayList<String> result = new ArrayList<String>();

		for (DashboardDefinition oldDashboardDefinition : oldDashboardDefinitions) {
			String code = oldDashboardDefinition.getCode();
			DashboardDefinition DashboardDefinition = this.getDashboardDefinition(code, DashboardDefinitions);
			if (DashboardDefinition != null)
				result.add(code);
		}

		return result;
	}

	private ArrayList<String> getObsoleteDashboards(List<DashboardDefinition> oldDashboardDefinitions, List<DashboardDefinition> DashboardDefinitions) {
		ArrayList<String> result = new ArrayList<String>();

		for (DashboardDefinition oldDashboardDefinition : oldDashboardDefinitions) {
			String code = oldDashboardDefinition.getCode();
			DashboardDefinition DashboardDefinition = this.getDashboardDefinition(code, DashboardDefinitions);
			if (DashboardDefinition == null)
				result.add(code);
		}

		return result;
	}

	private boolean isDashboardModified(DashboardDefinition oldDefinition, DashboardDefinition definition) {
		return true;
	}

	private DashboardDefinition getDashboardDefinition(String code, List<DashboardDefinition> DashboardDefinitions) {

		for (DashboardDefinition DashboardDefinition : DashboardDefinitions)
			if (DashboardDefinition.getCode().equals(code))
				return DashboardDefinition;

		return null;
	}

	@Override
	public String getStepInfo() {
		return "Instancing and refreshing dashboards";
	}
}
