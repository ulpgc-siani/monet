package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.*;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.ServerError;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;

import java.util.ArrayList;
import java.util.List;

public class InstanceSingletons extends Stage {

	@Override
	public void execute() {
		ComponentPersistence componentPersistence = this.globalData.getData(ComponentPersistence.class, GlobalData.COMPONENT_PERSISTENCE);
		NodeLayer nodeLayer = componentPersistence.getNodeLayer();
		List<NodeDefinition> nodeDefinitions;
		Dictionary dictionary = Dictionary.getInstance();

		try {
			nodeDefinitions = dictionary.getSingletonDefinitionList();

			ArrayList<NodeDefinition> desktops = new ArrayList<NodeDefinition>();

			for (NodeDefinition definition : nodeDefinitions) {
				try {
					if (definition.isDesktop()) {
						desktops.add(definition);
						continue;
					}
					this.instanceOrUpdate(dictionary, nodeLayer, definition);
				} catch (Exception exception) {
					problems.add(new ServerError(definition.getName(), exception.getMessage()));
					this.deployLogger.error(exception);
				}
			}

			for (NodeDefinition definition : desktops) {
				DesktopDefinition desktopDefinition = (DesktopDefinition) definition;
				for (NodeViewProperty viewDefinition : desktopDefinition.getViewList()) {
					DesktopDefinition.ViewProperty desktopViewDefinition = (DesktopDefinition.ViewProperty) viewDefinition;
					if (desktopViewDefinition.getShow().getLink() != null) {
						for (Ref entity : desktopViewDefinition.getShow().getLink()) {
							Definition showNodeDefinition = dictionary.getDefinition(entity.getValue());
							if (showNodeDefinition instanceof NodeDefinition)
								this.instanceOrUpdate(dictionary, nodeLayer, (NodeDefinition) showNodeDefinition);
						}
					}
				}

				try {
					this.instanceOrUpdate(dictionary, nodeLayer, definition);
				} catch (Exception exception) {
					problems.add(new ServerError(definition.getName(), exception.getMessage()));
					this.deployLogger.error(exception);
				}
			}

		} catch (Exception exception) {
			problems.add(new ServerError("", exception.getMessage()));
			this.deployLogger.error(exception);
		}
	}

	private void instanceOrUpdate(Dictionary dictionary, NodeLayer nodeLayer, NodeDefinition definition) {
		String singletonNodeId = nodeLayer.locateNodeId(definition.getCode());
		if (singletonNodeId.isEmpty())
			nodeLayer.addNode(definition.getCode());
		else {
			Node node = nodeLayer.loadNode(singletonNodeId);
			node.setLabel(definition.getLabelString());
			node.setDescription(definition.getDescriptionString());

			if (node.isContainer()) {
				ContainerDefinition containerDefinition = (ContainerDefinition) definition;
				if (containerDefinition.getContain() != null) {
					for (Ref ref : containerDefinition.getContain().getNode()) {
						String containCode = dictionary.getNodeDefinition(ref.getValue()).getCode();
						String id = getChildNodeId(node, containCode);
						if (id == null || id.isEmpty()) {
							Node child = nodeLayer.addNode(containCode, node);
							nodeLayer.addNodeContainerChild(node, child);
						}
					}
				}
			}

			nodeLayer.saveNode(node);
		}
	}

	String getChildNodeId(Node node, String code) {
		if (code == null) return null;
		if (code.length() <= 0) return null;
		if (!code.substring(0, 1).equals("[")) code = "[" + code + "]";
		return node.getIndicatorValue(code + ".value");
	}

	@Override
	public String getStepInfo() {
		return "Instancing singletons";
	}
}
