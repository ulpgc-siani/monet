package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.ServerError;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Reference;
import org.monet.space.kernel.producers.ProducerNode;
import org.monet.space.kernel.producers.ProducerReference;
import org.monet.space.kernel.producers.ProducersFactory;

import java.util.List;

public class RecalculateDefinitionsSchema extends Stage {

	@Override
	public void execute() {

		ProducersFactory factory = ProducersFactory.getInstance();

		ComponentPersistence componentPersistence = this.globalData.getData(ComponentPersistence.class, GlobalData.COMPONENT_PERSISTENCE);
		NodeLayer nodeLayer = componentPersistence.getNodeLayer();
		ProducerNode producerNode = (ProducerNode) factory.get(Producers.NODE);
		ProducerReference producerReference = (ProducerReference) factory.get(Producers.REFERENCE);

		try {

			if (producerNode.checkTableSchemaLoaded()) {
				return;
			}

			this.deployLogger.info("Schema table is empty. Checking if index tables are created...");

			Dictionary dictionary = Dictionary.getInstance();
			for (IndexDefinition definition : dictionary.getIndexDefinitionList()) {
				if (!nodeLayer.existsReferenceTable(definition)) {
					nodeLayer.createReferenceTable(definition);
					this.deployLogger.info("Creating index table " + definition.getName());
				}
			}

			this.deployLogger.info("Generating schema for all nodes...");

			List<String> nodeIds = nodeLayer.getReferencesNodeId(DescriptorDefinition.CODE);

			long current = 0;
			long total = nodeIds.size();
			for (String nodeId : nodeIds) {
				try {
					Node<?> node = producerNode.load(nodeId);
					if (node.getDefinition().hasMappings()) {
						for (Reference reference : node.getReferences().values()) {
							producerReference.create(node, reference);
						}
						producerNode.notifyAndSaveSchema(node);
					}
					current++;
					if (current % 5 == 0)
						this.deployLogger.info("Recalculating schemas %d of %d", current, total);
				} catch (Throwable ex) {
					this.deployLogger.info("Failure generating schema on node with id: " + nodeId);
					this.deployLogger.error(ex);
				}
			}
		} catch (Exception exception) {
			problems.add(new ServerError("", exception.getMessage()));
			this.deployLogger.error(exception);
		}
	}

	@Override
	public String getStepInfo() {
		return "Recalculating schemas";
	}
}
