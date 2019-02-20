package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.SourceDefinition;
import org.monet.metamodel.ThesaurusDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.ServerError;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RefreshSourceList extends Stage {

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		SourceLayer sourceLayer = componentPersistence.getSourceLayer();
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Map<String, SourceDefinition> oldSourceDefinitions = this.globalData.getData(Map.class, GlobalData.SOURCE_DEFINITIONS);
		List<String> sourceList;
		Dictionary dictionary = businessUnit.getBusinessModel().getDictionary();
		List<SourceDefinition> sourceDefinitions;
		ArrayList<String> existingSourceSet, obsoleteSourceSet;

		try {
			existingSourceSet = new ArrayList<String>();
			obsoleteSourceSet = new ArrayList<String>();
			sourceDefinitions = dictionary.getSourceDefinitionList();
			sourceList = sourceLayer.loadSourceListCodes();

			for (String code : sourceList) {
				if (dictionary.existsDefinition(code))
					existingSourceSet.add(code);
				else
					obsoleteSourceSet.add(code);
			}

			for (SourceDefinition definition : sourceDefinitions) {

				if (definition.isAbstract() || (!(definition instanceof ThesaurusDefinition)))
					continue;

				try {
					if (!existingSourceSet.contains(definition.getCode())) {
						try {
							Source<SourceDefinition> source = sourceLayer.createSource(definition.getCode());
							source.setPartnerName(businessUnit.getName());
							source.setPartnerLabel(businessUnit.getLabel());
							sourceLayer.populateSource(source);
							sourceLayer.saveSource(source);
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

			if (obsoleteSourceSet.size() > 0) {
				for (String obsoleteSourceCode : obsoleteSourceSet) {
					SourceDefinition definition = oldSourceDefinitions.get(obsoleteSourceCode);

					try {
						if (definition == null)
							definition = dictionary.getSourceDefinition(obsoleteSourceCode);
					} catch (Exception e) {
						continue;
					}

					if (!(definition instanceof ThesaurusDefinition))
						continue;

					Source<SourceDefinition> source = sourceLayer.locateSource(definition.getCode(), null);
					sourceLayer.removeSource(source);
				}
			}
		} catch (Exception exception) {
			problems.add(new ServerError("", exception.getMessage()));
			this.deployLogger.error(exception);
		}
	}

	@Override
	public String getStepInfo() {
		return "Refreshing source list";
	}
}
