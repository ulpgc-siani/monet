package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.*;
import org.monet.metamodel.DocumentDefinitionBase.MappingProperty;
import org.monet.metamodel.IndexDefinitionBase.ReferenceProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.ServerError;
import org.monet.space.kernel.model.Dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class RefreshIndexDefinitions extends Stage {

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		ComponentPersistence componentPersistence = this.globalData.getData(ComponentPersistence.class, GlobalData.COMPONENT_PERSISTENCE);
		NodeLayer nodeLayer = componentPersistence.getNodeLayer();

		try {
			Dictionary dictionary = Dictionary.getInstance();

			HashMap<String, IndexDefinition> oldReferences = new HashMap<String, IndexDefinition>();
			for (IndexDefinition reference : (Collection<IndexDefinition>) this.globalData.getData(Collection.class, GlobalData.REFERENCE_DEFINITIONS))
				oldReferences.put(reference.getCode(), reference);

			for (IndexDefinition definition : dictionary.getIndexDefinitionList()) {
				try {
					if (oldReferences.containsKey(definition.getCode())) {
						IndexDefinition oldReferenceDefinition = oldReferences.remove(definition.getCode());
						if (hasChanges(definition, oldReferenceDefinition)) {
							this.deployLogger.info("Changes detected in reference %s. Starting the update...", definition.getName());

							//Get all node ids
							List<String> nodeIds = new ArrayList<String>();
							try {
								nodeIds = nodeLayer.getReferencesNodeId(definition.getCode());
							} catch (Exception ex) {
							}

							//refresh table
							this.deployLogger.info("Refreshing tables...");
							nodeLayer.refreshReferenceTable(definition);
							//call calculate reference for every node
							updateNodeReferences(nodeLayer, definition, nodeIds);
						}
					} else {
						for (Definition d : dictionary.getAllDefinitions()) {
							ArrayList<Ref> mappings = new ArrayList<Ref>();

							if (d instanceof FormDefinition)
								for (org.monet.metamodel.FormDefinitionBase.MappingProperty p : ((FormDefinition) d).getMappingList())
									mappings.add(p.getIndex());
							else if (d instanceof DocumentDefinition)
								for (MappingProperty p : ((DocumentDefinition) d).getMappingList())
									mappings.add(p.getIndex());
							else
								continue;

							for (Ref index : mappings) {
								if (index.getValue().equals(definition.getName())) {
									List<String> nodeIds = new ArrayList<String>();
									try {
										nodeIds = nodeLayer.loadNodeIds(definition.getCode());
										this.updateNodeReferences(nodeLayer, definition, nodeIds);
									} catch (Exception ex) {
									}
									break;
								}
							}
						}
					}
					nodeLayer.createReferenceTable(definition);
				} catch (Exception exception) {
					problems.add(new ServerError(definition.getName(), exception.getMessage()));
					this.deployLogger.error(exception);
				}
			}

			for (IndexDefinition oldReference : oldReferences.values())
				nodeLayer.deleteReferenceTable(oldReference);

		} catch (Exception exception) {
			problems.add(new ServerError("", exception.getMessage()));
			this.deployLogger.error(exception);
		}
	}

	// call calculate reference for every node
	private void updateNodeReferences(NodeLayer nodeLayer, IndexDefinition definition, List<String> nodeIds) {
		long current = 0;
		long total = nodeIds.size();
		if (total == 0)
			return;
		this.deployLogger.info("Recalculating references %d of %d", current, total);
		for (String nodeId : nodeIds) {
			try {
				nodeLayer.refreshReference(nodeId, definition);
			} catch (Throwable exception) {
				this.deployLogger.info("Failure recalculating reference on %s", nodeId);
			}
			current++;
			if (current % 5 == 0)
				this.deployLogger.info("Recalculating references %d of %d", current, total);
		}
		this.deployLogger.info("Recalculating references %d of %d", current, total);
	}

	private boolean hasChanges(IndexDefinition definition, IndexDefinition oldDefinition) {
		if (!areEqual(definition.getName(), oldDefinition.getName()))
			return true;

		ReferenceProperty reference = definition.getReference();
		ReferenceProperty oldReference = oldDefinition.getReference();
		if (reference != null && oldReference != null) {
			if (reference.getAttributePropertyList().size() != oldReference.getAttributePropertyList().size())
				return true;

			for (AttributeProperty newAttribute : reference.getAttributePropertyList()) {
				AttributeProperty oldAttribute = oldDefinition.getAttribute(newAttribute.getCode());
				if (oldAttribute == null) return true;
				if (!areEqual(newAttribute.getCode(), oldAttribute.getCode()) ||
					!areEqual(newAttribute.getName(), oldAttribute.getName()) ||
					newAttribute.getType() != oldAttribute.getType() ||
					newAttribute.getPrecision() != oldAttribute.getPrecision())
					return true;
			}
		} else {
			return !(reference == null && oldDefinition == null);
		}

		return false;
	}

	public static boolean areEqual(String a, String b) {
		if (a == null && b == null) return true;
		if (a == null || b == null) return false;
		return a.equals(b);
	}

	@Override
	public String getStepInfo() {
		return "Refreshing indexes";
	}
}
