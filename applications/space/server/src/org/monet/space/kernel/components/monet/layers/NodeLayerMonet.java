package org.monet.space.kernel.components.monet.layers;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import org.monet.bpi.types.Date;
import org.monet.bpi.types.Number;
import org.monet.metamodel.AbstractManifestBase.DefaultLocationProperty;
import org.monet.metamodel.*;
import org.monet.metamodel.CheckFieldProperty.SelectProperty;
import org.monet.metamodel.CheckFieldProperty.TermsProperty;
import org.monet.metamodel.SelectFieldPropertyBase.SelectProperty.FlattenEnumeration;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.FilterProperty;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.SessionException;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.map.GeometryHelper;
import org.monet.space.kernel.model.map.Location;
import org.monet.space.kernel.model.map.LocationList;
import org.monet.space.kernel.model.wrappers.NodeDocumentWrapper;
import org.monet.space.kernel.producers.*;
import org.monet.space.kernel.translators.Translator;
import org.monet.space.mobile.model.Language;

import java.io.Reader;
import java.util.*;

public class NodeLayerMonet extends PersistenceLayerMonet implements NodeLayer {

	private static final AgentLogger agentLogger = AgentLogger.getInstance();
	private static final AgentNotifier agentNotifier = AgentNotifier.getInstance();
	private static Set<String> nodesInAddPhase = new HashSet<>();

	public NodeLayerMonet(ComponentPersistence componentPersistence) {
		super(componentPersistence);
	}

	private void addNodeContainer(Node node, User owner, NodeDefinition NodeDefinition) {
		ContainerDefinition containerDefinition = (ContainerDefinition) NodeDefinition;
		Dictionary dictionary = Dictionary.getInstance();

		if (containerDefinition.getContain() != null) {
			for (Ref contain : containerDefinition.getContain().getNode()) {
				String codeNode = dictionary.getDefinitionCode(contain.getValue());
				Node newNode = this.addNode(codeNode, owner, node,null);
				addNodeContainerChild(node, newNode);
			}
		}
	}

	@Override
	public void addNodeContainerChild(Node node, Node childNode) {
		Attribute attribute;
		Indicator indicator = new Indicator();
		String codeNode = childNode.getCode();

		attribute = node.getAttribute(codeNode);
		if (attribute == null) {
			attribute = new Attribute();
			attribute.setCode(codeNode);
			node.getAttributeList().add(attribute);
		}

		indicator.setCode(Indicator.VALUE);
		indicator.setData(childNode.getId());
		attribute.getIndicatorList().add(indicator);
	}

	private void addNodeDesktop(Node node, User owner, NodeDefinition nodeDefinition) {
		Dictionary dictionary = Dictionary.getInstance();
		DesktopDefinition desktopDefinition = (DesktopDefinition) nodeDefinition;

		if (desktopDefinition.getContain() != null) {
			for (Ref contain : desktopDefinition.getContain().getNode()) {
				String codeNode = dictionary.getDefinitionCode(contain.getValue());
				Node newNode = this.addNode(codeNode, owner, node, null);

				addNodeContainerChild(node, newNode);
			}
		}
	}

	@Override
	public void setNodeDocumentSignaturesCount(Node node, String signatureKey, int count) {
		DocumentDefinition definition = (DocumentDefinition) node.getDefinition();
		String signatureCode = definition.getSignature(signatureKey).getCode();

		new NodeDocumentWrapper(node).setCountSignaturesOfType(signatureCode, count);

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.saveAttributes(node, false);
	}

	@Override
	public void clearNodeDocumentSignatureUsersRestrictions(Node node, String signatureKey, int signaturePos) {
		DocumentDefinition definition = (DocumentDefinition) node.getDefinition();
		String signatureCode = definition.getSignature(signatureKey).getCode();

		new NodeDocumentWrapper(node).clearSignatureUsersRestrictions(signatureCode, signaturePos);

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.saveAttributes(node, false);
	}

	@Override
	public void addNodeDocumentSignatureUserRestriction(Node node, String signatureKey, int signaturePos, User user) {
		DocumentDefinition definition = (DocumentDefinition) node.getDefinition();
		String signatureCode = definition.getSignature(signatureKey).getCode();

		new NodeDocumentWrapper(node).addSignatureUserRestriction(signatureCode, signaturePos, user);

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.saveAttributes(node, false);
	}

	@Override
	public void addNodeDocumentSignature(Node node, String signatureKey, int signaturePos, String label, String reason, String location, String contact, java.util.Date date) {
		DocumentDefinition definition = (DocumentDefinition) node.getDefinition();
		String signatureCode = definition.getSignature(signatureKey).getCode();

		new NodeDocumentWrapper(node).addSignature(node, signatureCode, signaturePos, label, reason, location, contact, date);

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.saveAttributes(node, false);
	}

	@Override
	public void deleteNodeDocumentSignature(Node node, String signatureKey, int signaturePos) {
		DocumentDefinition definition = (DocumentDefinition) node.getDefinition();
		String signatureCode = definition.getSignature(signatureKey).getCode();

		new NodeDocumentWrapper(node).deleteSignature(node, signatureCode, signaturePos);

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.saveAttributes(node, false);
	}

	@Override
	public void stampNodeDocumentSignature(Node node, String signId, String signature, User user) {
		String documentId = node.getId();
		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();

		componentDocuments.stampDocumentSignature(documentId, signId, signature, getAccount().getUser());

		MonetEvent event = new MonetEvent(MonetEvent.NODE_SIGNED, null, node);
		event.addParameter(MonetEvent.PARAMETER_SIGN_USER, user);
		agentNotifier.notify(event);

		notifyDocumentSignsCompleted(node);
	}

	private void notifyDocumentSignsCompleted(Node node) {
		DocumentDefinition definition = (DocumentDefinition) node.getDefinition();
		Map<String, DocumentDefinitionBase.SignaturesProperty.SignatureProperty> signaturesDefinitionMap = new HashMap<>();

		if (definition.getSignatures() != null)
			signaturesDefinitionMap = definition.getSignatures().getSignatureMap();

		if (signaturesDefinitionMap.size() == 0)
			return;

		for (DocumentDefinitionBase.SignaturesProperty.SignatureProperty signatureDefinition : signaturesDefinitionMap.values()) {
			Attribute attribute = node.getAttribute(signatureDefinition.getCode());

			if (attribute == null)
				return;
		}

		MonetEvent event = new MonetEvent(MonetEvent.NODE_SIGNS_COMPLETED, null, node);
		agentNotifier.notify(event);
	}

	private void addNodeForm(Node node, NodeDefinition nodeDefinition) {

		if (node.requirePartnerContext() && node.getPartnerContext() == null)
			return;

		this.fillNodeForm(node, nodeDefinition);
	}

	private void addNodeDocument(Node node, NodeDefinition nodeDefinition) {
		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();

		try {

			if (nodeDefinition.isReadonly() && node.getReferencedId() == null) {
				AgentLogger.getInstance().errorInModel(String.format("Definition %s is readonly!", nodeDefinition.getName()), null);
				return;
			}
			if (node.getReferencedId() != null){
				componentDocuments.createSharedDocument(node.getCode(), node.getId(), node.getReferencedId());
			}else {
				componentDocuments.createDocument(node.getCode(), node.getId());
			}
		} catch (Exception oException) {
			try {
				if (node.getReferencedId() == null) this.deleteAndRemoveNodeFromTrash(node.getId());
			} catch (Exception e) {
				AgentLogger.getInstance().error(e);
			}
			throw new DataException(ErrorCode.ADD_NODE, node.getId(), oException);
		}
	}

	private void fillNodeFormNodeFields(Node node, AttributeList attributeList, Collection<NodeFieldProperty> nodeFieldDefinitions) {
		Dictionary dictionary = Dictionary.getInstance();

		for (NodeFieldProperty nodeFieldDeclaration : nodeFieldDefinitions) {
			Attribute attribute = attributeList.getFirstByCode(nodeFieldDeclaration.getCode());
			IndicatorList indicatorList;
			Node newNode;

			if (attribute == null)
				continue;
			if (nodeFieldDeclaration.isMultiple())
				continue;
			if (nodeFieldDeclaration.isAggregateRelation())
				continue;

			indicatorList = attribute.getIndicatorList();
			newNode = this.addNode(dictionary.getDefinitionCode(nodeFieldDeclaration.getContain().getNode().getValue()), node.getOwner(), node, node.getReferencedId());

			this.saveNodeReference(newNode, newNode.getReference());

			Indicator indicatorNode = attribute.getIndicator(Indicator.NODE);
			if (indicatorNode == null) {
				indicatorNode = new Indicator(Indicator.NODE, -1, "");
				indicatorList.add(indicatorNode);
			}
			indicatorNode.setData(newNode.getId());

			Indicator indicatorCode = attribute.getIndicator(Indicator.CODE);
			if (indicatorCode == null) {
				indicatorCode = new Indicator(Indicator.CODE, -1, "");
				indicatorList.add(indicatorCode);
			}
			indicatorCode.setData(newNode.getId());

			Indicator indicatorValue = attribute.getIndicator(Indicator.VALUE);
			if (indicatorValue == null) {
				indicatorValue = new Indicator(Indicator.VALUE, -1, "");
				indicatorList.add(indicatorValue);
			}
			indicatorValue.setData(newNode.getLabel());
		}
	}

	private void fillNodeFormSerialFields(AttributeList attributeList, Collection<SerialFieldProperty> serialFieldDeclarations) {
		ProducerSequence producerSequence = this.producersFactory.get(Producers.SEQUENCE);

		for (SerialFieldProperty serialFieldDeclaration : serialFieldDeclarations) {
			Attribute attribute = attributeList.getFirstByCode(serialFieldDeclaration.getCode());
			IndicatorList indicatorList;
			SequenceValue value;
			String result;

			if (attribute == null)
				continue;

			indicatorList = attribute.getIndicatorList();
			value = producerSequence.createValue(serialFieldDeclaration.getSerial().getName());
			result = serialFieldDeclaration.format(value);

			Indicator indicatorValue = attribute.getIndicator(Indicator.VALUE);
			if (indicatorValue == null) {
				indicatorValue = new Indicator(Indicator.VALUE, -1, result);
				indicatorList.add(indicatorValue);
			}
			indicatorValue.setData(result);
		}
	}

	private void getNodeFormCheckFieldOption(DataRequest dataRequest, AttributeList attributeList, Term term, boolean flatten) {
		Attribute optionAttribute = new Attribute();

		optionAttribute.setCode(Attribute.OPTION);
		optionAttribute.getIndicatorList().add(new Indicator(Indicator.CHECKED, 0, "false"));
		optionAttribute.getIndicatorList().add(new Indicator(Indicator.CODE, 1, term.getCode()));
		optionAttribute.getIndicatorList().add(new Indicator(Indicator.VALUE, 2, flatten ? term.getFlattenLabel() : term.getLabel()));
		attributeList.add(optionAttribute);

		for (Term childTerm : term.getTermList()) {
			this.getNodeFormCheckFieldOption(dataRequest, attributeList, childTerm, flatten);
		}
	}

	private void fillNodeFormCheckFields(Node node, AttributeList attributeList, Collection<CheckFieldProperty> checkFieldProperties) {
		for (CheckFieldProperty checkFieldProperty : checkFieldProperties) {
			Attribute attribute = attributeList.getFirstByCode(checkFieldProperty.getCode());

			if (attribute == null)
				continue;

			attribute.setAttributeList(this.getNodeFormCheckFieldOptions(node, null, checkFieldProperty, null));
		}
	}

	private void fillNodeFormSelectFields(Node node, AttributeList nodeAttributeList, Collection<SelectFieldProperty> selectFieldDefinitions) {
		Language language = Language.getInstance();

		for (SelectFieldProperty selectFieldDefinition : selectFieldDefinitions) {
			Attribute attribute = nodeAttributeList.getFirstByCode(selectFieldDefinition.getCode());
			AttributeList attributeList;
			String code = null, label = null;
			SelectFieldPropertyBase.SelectProperty selectDefinition = selectFieldDefinition.getSelect();
			boolean isEmbedded = selectDefinition != null && selectDefinition.isEmbedded();

			if (!isEmbedded)
				continue;

			if (attribute == null)
				continue;

			attributeList = attribute.getAttributeList();

			if (selectFieldDefinition.getSource() == null) {
				SelectFieldProperty.TermsProperty termsDefinition = selectFieldDefinition.getTerms();
				TermProperty termDeclaration = termsDefinition.getTermPropertyList().get(0);
				code = termDeclaration.getKey();
				label = language.getModelResource(termDeclaration.getLabel());
			} else {
				SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
				String codeSource = Dictionary.getInstance().getDefinitionCode(selectFieldDefinition.getSource().getValue());
				SourceList sourceList = sourceLayer.loadSourceList(codeSource, node.getPartnerContext());

				if (sourceList.getCount() <= 0 || sourceList.getCount() > 1)
					continue;

				DataRequest dataRequest = new DataRequest();
				TermList termList;
				Term term;

				dataRequest.addParameter(DataRequest.FLATTEN, (selectDefinition != null && selectDefinition.getFlatten() != null) ? selectDefinition.getFlatten().toString() : FlattenEnumeration.NONE.toString());
				dataRequest.addParameter(DataRequest.DEPTH, selectDefinition != null && selectDefinition.getDepth() != null ? String.valueOf(selectDefinition.getDepth()) : "-1");
				if (selectDefinition != null && selectDefinition.getFilter() != null)
					dataRequest.addParameter(DataRequest.FILTERS, String.valueOf(selectDefinition.getFilter().getTag()));
				dataRequest.addParameter(DataRequest.FROM, (selectDefinition != null && selectDefinition.getRoot() != null) ? this.getNodeFieldFromParameter(node, selectDefinition.getRoot()) : this.getNodeFieldFromParameter(node, null));
				dataRequest.setLimit(-1);
				termList = ComponentPersistence.getInstance().getSourceLayer().loadSourceTerms(sourceList.get().values().iterator().next(), dataRequest, true);
				term = termList.first();
				if (term != null) {
					code = term.getCode();
					label = term.getLabel();
				}
			}

			if (code != null) {
				Attribute childAttribute = new Attribute();
				childAttribute.setCode(Attribute.OPTION);
				childAttribute.getIndicatorList().add(new Indicator(Indicator.CODE, 1, code));
				childAttribute.getIndicatorList().add(new Indicator(Indicator.VALUE, 2, label));
				attributeList.add(childAttribute);
			}
		}
	}

	private void fillNode(Node node, NodeDefinition nodeDefinition) {
		Dictionary dictionary = Dictionary.getInstance();

		if (node.isCollection()) {
			for (Node childNode : this.loadNodes(node.getId()))
				this.fillNode(childNode, childNode.getDefinition());
		} else if (node.isContainer()) {
			ContainerDefinition containerDefinition = (ContainerDefinition) nodeDefinition;
			for (Ref contain : containerDefinition.getContain().getNode()) {
				String childDefinitionCode = dictionary.getDefinitionCode(contain.getValue());
				String idChild = node.getIndicatorValue("[" + childDefinitionCode + "].value");
				Node childNode = this.loadNode(idChild);
				this.fillNode(childNode, childNode.getDefinition());
			}
		} else if (node.isForm())
			this.fillNodeForm(node, node.getDefinition());
	}

	private void fillNodeForm(Node node, NodeDefinition definition) {
		FormDefinition formDefinition = (FormDefinition) definition;
		Collection<NodeFieldProperty> nodeFieldDefinitions;
		Collection<SerialFieldProperty> serialFieldDefinitions;
		Collection<CheckFieldProperty> checkFieldDefinitions;
		Collection<SelectFieldProperty> selectFieldDefinitions;

		nodeFieldDefinitions = formDefinition.getNodeFieldPropertyList();
		serialFieldDefinitions = formDefinition.getSerialFieldPropertyList();
		checkFieldDefinitions = formDefinition.getCheckFieldPropertyList();
		selectFieldDefinitions = formDefinition.getSelectFieldPropertyList();

		if (nodeFieldDefinitions.size() > 0)
			this.fillNodeFormNodeFields(node, node.getAttributeList(), nodeFieldDefinitions);

		if (serialFieldDefinitions.size() > 0)
			this.fillNodeFormSerialFields(node.getAttributeList(), serialFieldDefinitions);

		if (checkFieldDefinitions.size() > 0)
			this.fillNodeFormCheckFields(node, node.getAttributeList(), checkFieldDefinitions);

		if (selectFieldDefinitions.size() > 0)
			this.fillNodeFormSelectFields(node, node.getAttributeList(), selectFieldDefinitions);
	}

	private void savePrepareNodeForm(Node node, NodeDefinition nodeDefinition) {
		FormDefinition declaration = (FormDefinition) nodeDefinition;
		Collection<NodeFieldProperty> nodeFieldDefinitions;
		AttributeList attributeList;
		NodeList nodeList;
		HashSet<String> childNodes = new HashSet<>();
		List<Node> orphanNodes = new ArrayList<>();

		nodeFieldDefinitions = declaration.getNodeFieldPropertyList();
		if (nodeFieldDefinitions.size() == 0)
			return;

		attributeList = node.getAttributeList();

		for (NodeFieldProperty nodeFieldDeclaration : nodeFieldDefinitions) {
			Set<Attribute> attributes = attributeList.searchByCode(nodeFieldDeclaration.getCode());
			for (Attribute currentAttribute : attributes) {
				Indicator indicatorNode = currentAttribute.getIndicator(Indicator.NODE);
				if (indicatorNode != null)
					childNodes.add(indicatorNode.getData());
			}
		}

		nodeList = this.loadNodes(node.getId());
		for (Node currentNode : nodeList) {
			if (childNodes.contains(currentNode.getId()))
				continue;
			orphanNodes.add(currentNode);
		}

		for (Node orphanNode : orphanNodes) deleteNode(orphanNode);
	}

	private void clearLinks(Node node) {
		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.clearLinks(node);
	}

	private void saveNodeDocument(Node node, Boolean async) {
		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();

		try {
			if (node.getReferencedId() != null && !node.getReferencedId().equals("-1")) return;
			componentDocuments.updateDocument(node.getId(), node.getSchema(), async);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.SAVE_NODE, node.getId(), exception);
		}
	}

	private void copyNodeContainer(Node node, Node sourceNode, String label, String description) {
		MonetHashMap<Node> childNodes, sourceNodes;
		Set<String> copiedNodes = new HashSet<>();

		node.removeLoadedAttribute(Node.NODELIST);
		childNodes = node.getNodeList().get();
		sourceNodes = sourceNode.getNodeList().get();

		for (Node childNode : childNodes.values()) {
			for (String key : sourceNodes.keySet()) {
				Node sourceChildNode = sourceNodes.get(key);
				if (sourceChildNode.getCode().equals(childNode.getCode())) {
					if (copiedNodes.contains(key))
						continue;
					copiedNodes.add(key);
					this.copyNode(childNode, sourceChildNode, label, description);
					break;
				}
			}
		}
	}

	private void copyNodeCollection(Node node, Node sourceNode, String label, String description) {
		MonetHashMap<Node> sourceNodes;

		node.removeLoadedAttribute(Node.NODELIST);
		sourceNodes = sourceNode.getNodeList().get();

		for (String key : sourceNodes.keySet()) {
			Node sourceChildNode = sourceNodes.get(key);
			Node newNode = this.addNode(sourceChildNode.getCode(), node.getOwner(), node, node.getReferencedId());
			this.copyNode(newNode, sourceChildNode, label, description);
		}

		node.setNodeList(new NodeList());
	}

	private void copyNodeFormNodeField(Node node, AttributeList attributeList, NodeFieldProperty nodeFieldDeclaration) {
		Set<Attribute> attributes = attributeList.searchByCode(nodeFieldDeclaration.getCode());

		for (Attribute attribute : attributes) {
			Node newNode, sourceNode;

			Indicator indicatorNode = attribute.getIndicator(Indicator.NODE);
			if (indicatorNode == null)
				continue;

			Indicator indicatorCode = attribute.getIndicator(Indicator.CODE);
			if (indicatorCode == null)
				continue;

			Indicator indicatorValue = attribute.getIndicator(Indicator.VALUE);
			if (indicatorValue == null)
				continue;

			sourceNode = this.loadNode(indicatorNode.getData());
			newNode = this.addNode(sourceNode.getCode(), node.getOwner(), node, node.getReferencedId());
			this.copyNode(newNode, sourceNode, indicatorValue.getData(), indicatorValue.getData());
			this.saveNodeReference(newNode, newNode.getReference());

			indicatorNode.setData(newNode.getId());
			indicatorCode.setData(newNode.getId());
			indicatorValue.setData(newNode.getLabel());
		}
	}

	private void copyNodeFormSerialField(AttributeList attributeList, AttributeList oldAttributeList, SerialFieldProperty serialFieldDeclaration) {
		Set<Attribute> attributes = oldAttributeList.searchByCode(serialFieldDeclaration.getCode());
		for (Attribute attribute : attributes)
			attributeList.add(attribute);
	}

	private void copyNodeForm(Node node, Node sourceNode, AttributeList oldAttributeList) {
		FormDefinition definition = (FormDefinition) node.getDefinition();
		AttributeList attributeList = node.getAttributeList();

		for (FieldProperty fieldDefinition : definition.getAllFieldPropertyList()) {
			if (fieldDefinition.isUnivocal()) {
				attributeList.delete(fieldDefinition.getCode());
				continue;
			}

			if (fieldDefinition.isNode())
				this.copyNodeFormNodeField(sourceNode, attributeList, (NodeFieldProperty) fieldDefinition);
			else if (fieldDefinition.isSerial())
				this.copyNodeFormSerialField(attributeList, oldAttributeList, (SerialFieldProperty) fieldDefinition);
		}
	}

	private String getNodeFieldFromParameter(Node node, Object from) {
		String fromParameter = "-1";
		if (from != null) {
			if (from instanceof String)
				fromParameter = (String) from;
			else if (from instanceof Ref) {
				String fieldName = ((Ref) from).getValue();
				fromParameter = node.getFieldValue(fieldName);
			}
		}
		return fromParameter;
	}

	@Override
	public boolean existsNode(String id) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		return producerNode.exists(id);
	}

	private Geometry getDefaultLocationGeometry() {
		GeometryFactory factory = GeometryHelper.getFactory();
		Geometry defaultGeometry = null;
		double latitude = 0, longitude = 0;

		DefaultLocationProperty defaultLocation = BusinessUnit.getInstance().getDistribution().getDefaultLocation();
		if (defaultLocation == null) defaultLocation = BusinessModel.getInstance().getProject().getDefaultLocation();

		if (defaultLocation != null) {
			latitude = defaultLocation.getLatitude();
			longitude = defaultLocation.getLongitude();
		}

		try {
			String wkt = String.format(Locale.ENGLISH, "POINT(%f %f)", latitude, longitude);
			WKTReader reader = new WKTReader(factory);
			defaultGeometry = reader.read(wkt);
		} catch (Exception e) {
			agentLogger.errorInModel(e.getMessage(), e);
		}

		return defaultGeometry;
	}

	@Override
	public Node addPrototype(String code, Node parent) {
		Node node = this.addNode(code, parent != null ? parent.getOwner() : null, parent, null);
		this.makeNodePrototype(node);
		return node;
	}

	private Node addNode(String key, User owner, Node parent, String referencedId) {
		ProducerNode producerNode;
		ProducerReference producerReference;
		Node node;
		String code;
		NodeDefinition nodeDefinition;
		BusinessModel businessModel;
		Reference reference;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		businessModel = BusinessUnit.getInstance().getBusinessModel();
		producerNode = this.producersFactory.get(Producers.NODE);
		producerReference = this.producersFactory.get(Producers.REFERENCE);

		nodeDefinition = businessModel.getDictionary().getNodeDefinition(key);
		code = nodeDefinition.getCode();

		if (nodeDefinition.isSingleton()) {
			String id = this.locateNodeId(nodeDefinition.getCode());
			if ((id != null) && (!id.equals(Strings.EMPTY))) {
				return this.loadNode(id);
			}
		}

		if (parent != null)
			node = producerNode.create(code, parent.getId(), parent.getPartnerContext());
		else
			node = producerNode.create(code, null, null);

		NodeLayerMonet.nodesInAddPhase.add(node.getId());

		try {
			node.setName(nodeDefinition.getName());
			node.setType(nodeDefinition.getType());
			node.setOwnerId(getOwnerId(owner, parent));
			node.setOrder(0);
			node.setParent(parent);
			node.setCode(code);
			node.setReferencedId(referencedId);
			node.setAttributeList(nodeDefinition.buildAttributes());

			reference = node.getReference();
			reference.setLabel(nodeDefinition.getLabelString());
			reference.setDescription(nodeDefinition.getDescriptionString());
			reference.setOrdering(new Number(0));
			reference.setCreateDate(new Date());
			reference.setUpdateDate(new Date());
			reference.setDeleteDate(null);
			reference.setHighlighted(false);
			reference.setEditable(true);
			reference.setDeletable(true);
			reference.setPrototype(false);
			reference.setGeoReferenced(nodeDefinition.isGeoreferenced() ? new Number(0) : new Number(-1));
			reference.setAttributeValue(DescriptorDefinition.ATTRIBUTE_ID_NODE, new Number(node.getId()));
			reference.setAttributeValue(DescriptorDefinition.ATTRIBUTE_ID_OWNER, node.getOwnerId() != null ? new Number(node.getOwnerId()) : null);
			reference.setAttributeValue(DescriptorDefinition.ATTRIBUTE_ID_PARENT, node.getParentId() != null ? new Number(node.getParentId()) : null);
			reference.setAttributeValue(DescriptorDefinition.ATTRIBUTE_CODE, code);

			if (nodeDefinition.isGeoreferenced()) {
				ProducerLocation producerLocation = this.producersFactory.get(Producers.LOCATION);
				Geometry defaultGeometry = this.getDefaultLocationGeometry();
				Location location = new Location();

				location.setNodeId(node.getId());
				location.setLocationId(nodeDefinition.getCode());
				location.setGeometry(defaultGeometry);
				location.setCreateDate(new java.util.Date());
				node.setLocation(location);

				producerLocation.create(node.getLocation());
			}

			if (nodeDefinition instanceof ContainerDefinition)
				this.addNodeContainer(node, owner, nodeDefinition);
			else if (nodeDefinition instanceof DesktopDefinition)
				this.addNodeDesktop(node, owner, nodeDefinition);
			else if (nodeDefinition instanceof FormDefinition)
				this.addNodeForm(node, nodeDefinition);
			else if (nodeDefinition instanceof DocumentDefinition)
				this.addNodeDocument(node, nodeDefinition);

			try {
				agentNotifier.notify(new MonetEvent(MonetEvent.NODE_CREATED, null, node));
			}
			catch(Exception exception) {
				AgentLogger.getInstance().error(exception);
				throw exception;
			}

			producerNode.create(node);

			if (parent != null && parent.isCollection()) {
				parent.getReference().setUpdateDate(new Date(new java.util.Date()));
				producerReference.saveUpdateDate(parent);

				MonetEvent event = new MonetEvent(MonetEvent.NODE_ADDED_TO_COLLECTION, null, node);
				event.addParameter(MonetEvent.PARAMETER_COLLECTION, parent);
				agentNotifier.notify(event);
			}

			if (nodeDefinition.isReadonly()) {
				this.makeUneditable(node);
				this.makeUndeletable(node);
			}
		} finally {
			NodeLayerMonet.nodesInAddPhase.remove(node.getId());
		}

		return producerNode.lightLoad(node.getId(), code);
	}

	private String getOwnerId(User owner, Node parent) {
		Project manifest = BusinessUnit.getInstance().getBusinessModel().getProject();
		boolean isFront = manifest.getType() != null && manifest.getType().equals(ProjectBase.TypeEnumeration.FRONT);

		if (isFront && parent != null && parent.isSingleton())
			return null;

		return owner != null ? owner.getId() : this.getAccount().getUser().getId();
	}

	@Override
	public Node addNode(String code) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		return this.addNode(code, null, null, null);
	}

	@Override
	public Node addNode(String code, User owner) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		return this.addNode(code, owner, null, null);
	}

	@Override
	public Node addNode(String code, Node parent) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		return this.addNode(code, parent != null ? parent.getOwner() : null, parent, null);
	}

	@Override
	public Node addSharedNode(String code, String documentReference) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		return this.addNode(code, null, null, documentReference);
	}

	@Override
	public void setParentNode(Node node, Node parent) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.saveParent(node, parent);
	}

	@Override
	public void copyNode(Node node, Node sourceNode, String label, String description) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		if (!sourceNode.getDefinition().isPrototypable())
			return;

		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();

		AttributeList oldAttributeList = node.getAttributeList();
		node.clone(sourceNode);
		node.setParentId(node.getParentId());
		node.getReference().setLabel(label);
		node.getReference().setDescription(description);
		node.getReference().setPrototype(false);

		if (sourceNode.isPrototype())
			this.makeNodePrototyped(node, sourceNode);

		if (node.isContainer() || node.isDesktop()) {
			node.setAttributeList(oldAttributeList);
			this.copyNodeContainer(node, sourceNode, label, description);
		} else if (node.isCollection())
			this.copyNodeCollection(node, sourceNode, label, description);
		else if (node.isForm())
			this.copyNodeForm(node, sourceNode, oldAttributeList);
		else if (node.isDocument()) {
			try {
				componentDocuments.copyDocument(sourceNode.getId(), node.getId(), true);
			} catch (Exception oException) {
				throw new DataException(ErrorCode.COPY_NODE, "source: " + sourceNode.getId() + ", target: " + node.getId(), oException);
			} finally {
				this.deleteNode(node);
				this.removeNodeFromTrash(node.getId());
			}
		}

		this.saveNode(node);
	}

	@Override
	public void deleteNode(String id) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		this.deleteNode(this.loadNode(id));
	}

	@Override
	public void deleteNode(Node node) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);

		if (node.getDefinition().isSingleton()) {
			return;
		}

		producerNode.remove(node);
	}

	@Override
	public void deleteAndRemoveNodeFromTrash(String id) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);

		Node node = this.loadNode(id);

		if (node.getDefinition().isSingleton())
			return;

		producerNode.remove(node);

		try {
			componentDocuments.removeDocument(id);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		producerNode.removeFromTrash(node);
	}

	@Override
	public void deleteAndRemoveNodesFromTrash(List<Node> nodes) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		for (Node node : nodes) {
			clearLinks(node);
			deleteAndRemoveNodeFromTrash(node.getId());
		}
	}

	@Override
	public void emptyTrash() {

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		this.producersFactory.<ProducerNodeList>get(Producers.NODELIST).emptyTrash();
	}

	@Override
	public NodeList filterNodeList(Node node, String nodeValues, String nodeTypesValues, java.util.Date fromDate, java.util.Date toDate) {
		String[] nodes = null, nodeTypes = null;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		if (nodeValues != null)
			nodes = nodeValues.split(Strings.COMMA);
		if (nodeTypesValues != null)
			nodeTypes = nodeTypesValues.split(Strings.COMMA);

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);

		return producerNode.filterNodeList(node, nodes, nodeTypes, fromDate, toDate);
	}

	@Override
	public String exportNode(Node oNode, String codeFormat) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		String result = producerNode.exportNode(oNode, codeFormat);

		if (!codeFormat.toLowerCase().equals(FormatCode.MONET.toLowerCase())) {
			Translator translator = BusinessUnit.getInstance().getBusinessModel().getTranslator(codeFormat.toLowerCase());
			if ((result = translator.directTranslation(result, oNode.getCode())) == null) {
				throw new DataException(ErrorCode.EXPORT_FORMAT, codeFormat);
			}
		}

		return result;
	}

	@Override
	public String exportNode(Node oNode) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		return this.exportNode(oNode, FormatCode.MONET);
	}

	@Override
	public String exportNode(String nodeId, String codeFormat) {
		return this.exportNode(this.loadNode(nodeId), codeFormat);
	}

	@Override
	public String exportNode(String nodeId) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		return this.exportNode(nodeId, FormatCode.MONET);
	}

	@Override
	public void importNode(String importerKey, Node scope, Reader source, long fileSize, ProgressCallback callback) {
		MonetEvent event = new MonetEvent(MonetEvent.NODE_IMPORT, null, importerKey);
		event.addParameter(MonetEvent.PARAMETER_SCOPE, scope);
		event.addParameter(MonetEvent.PARAMETER_STREAM, source);
		event.addParameter(MonetEvent.PARAMETER_STREAM_SIZE, fileSize);
		event.addParameter(MonetEvent.PARAMETER_CALLBACK, callback);
		AgentNotifier.getInstance().notify(event);
	}

	@Override
	public AttributeList loadAttributes(Node node) {

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		return node.getAttributeList();
	}

	@Override
	public Map<String, Attribute> loadNodesAttribute(String[] nodesIds, String codeAttribute) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		return producerNodeList.loadAttribute(nodesIds, codeAttribute);
	}

	@Override
	public Node loadNode(String id) {
		if (!isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		Node node = producerNode.load(id);

		if (!node.isContainer())
			return node;

		if (NodeLayerMonet.nodesInAddPhase.contains(node.getId()))
			return node;

		ContainerDefinition containerDefinition = (ContainerDefinition) node.getDefinition();
		Dictionary dictionary = Dictionary.getInstance();
		for (Ref contain : containerDefinition.getContain().getNode()) {
			String childDefinitionCode = dictionary.getDefinitionCode(contain.getValue());
			String idChild = node.getIndicatorValue("[" + childDefinitionCode + "].value");
			if (!this.existsNode(idChild)) {
				Node childNode = this.addNode(childDefinitionCode, node.getOwner(), node, node.getReferencedId());
				this.addNodeContainerChild(node, childNode);
				this.saveNode(node);
			}
		}

		return node;
	}

	@Override
	public Node loadNode(String id, boolean onlyFromMemory) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		if (onlyFromMemory)
			return producerNode.lightLoad(id, null);
		return producerNode.load(id);
	}

	@Override
	public NodeList loadNodeAncestors(Node node) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		ProducerNode producerNode = producersFactory.get(Producers.NODE);
		return producerNode.getAncestors(node);
	}

	@Override
	public Node loadNodeRevision(String id, String idRevision) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		return producerNode.loadRevision(id, idRevision);
	}

	@Override
	public void restoreNodeRevision(String id, String idRevision) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.restoreRevision(id, idRevision);
	}

	@Override
	public Revision loadRevision(String id) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		ProducerNodeRevision producerRevision = this.producersFactory.get(Producers.REVISION);
		return producerRevision.load(id);
	}

	@Override
	public void restoreNode(Revision revision) {
	}

	@Override
	public Node loadNodeFromData(String id, String data) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		Node node = this.loadNode(id);
		node.deserializeFromXML(data, new NodeDefinitionChangesResolver(node.getDefinition()));
		return node;
	}

	@Override
	public AttributeList getNodeFormCheckFieldOptions(Node node, String sourceId, CheckFieldProperty fieldDefinition, String from) {
		Dictionary dictionary = Dictionary.getInstance();
		AttributeList attributeList = new AttributeList();
		Language language = Language.getInstance();

		if (fieldDefinition.getTerms() != null) {
			TermsProperty termsDefinition = fieldDefinition.getTerms();
			for (TermProperty termDeclaration : termsDefinition.getTermPropertyList()) {
				Attribute childAttribute = new Attribute();
				childAttribute.setCode(Attribute.OPTION);
				childAttribute.getIndicatorList().add(new Indicator(Indicator.CHECKED, 0, "false"));
				childAttribute.getIndicatorList().add(new Indicator(Indicator.CODE, 1, termDeclaration.getKey()));
				childAttribute.getIndicatorList().add(new Indicator(Indicator.VALUE, 2, language.getModelResource(termDeclaration.getLabel())));
				attributeList.add(childAttribute);
			}
		} else if (fieldDefinition.getSource() != null) {
			SelectProperty selectDefinition = fieldDefinition.getSelect();
			SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
			String codeSource = dictionary.getDefinitionCode(fieldDefinition.getSource().getValue());
			Source<SourceDefinition> source = null;

			if (sourceId != null)
				source = sourceLayer.loadSource(sourceId);
			else {
				SourceList sourceList = sourceLayer.loadSourceList(codeSource, node.getPartnerContext());
				if (sourceList.getCount() == 1)
					source = sourceList.get().values().iterator().next();
			}

			if (source == null)
				return attributeList;

			DataRequest dataRequest = new DataRequest();
			TermList termList;

			dataRequest.addParameter(DataRequest.MODE, DataRequest.Mode.TREE);

			if (from == null)
				from = (selectDefinition != null && selectDefinition.getRoot() != null) ? this.getNodeFieldFromParameter(node, selectDefinition.getRoot()) : this.getNodeFieldFromParameter(node, null);

			boolean isFlatten = (selectDefinition != null && selectDefinition.getFlatten() != null && selectDefinition.getFlatten().equals(CheckFieldProperty.SelectProperty.FlattenEnumeration.ALL));

			dataRequest.addParameter(DataRequest.FLATTEN, (selectDefinition != null && selectDefinition.getFlatten() != null) ? selectDefinition.getFlatten().toString() : CheckFieldProperty.SelectProperty.FlattenEnumeration.NONE.toString());
			dataRequest.addParameter(DataRequest.FROM, from);
			dataRequest.addParameter(DataRequest.DEPTH, (selectDefinition != null && selectDefinition.getDepth() != null) ? String.valueOf(selectDefinition.getDepth()) : "-1");
			dataRequest.setLimit(-1);
			termList = ComponentPersistence.getInstance().getSourceLayer().loadSourceTerms(source, dataRequest, true);

			for (Term term : termList.get().values()) {
				getNodeFormCheckFieldOption(dataRequest, attributeList, term, isFlatten);
			}
		} else {
			Set<Attribute> attributes = node.getAttributeList().searchByCode(fieldDefinition.getCode());

			if (attributes.size() > 0) {
				Attribute attribute = attributes.iterator().next();
				attributeList = attribute.getAttributeList();
			}
		}

		return attributeList;
	}

	@Override
	public Reference loadNodeReference(Node node) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNode producerNode = producersFactory.get(Producers.NODE);
		return producerNode.loadReference(node);
	}

	@Override
	public Reference loadNodeReference(Node node, String code) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNode producerNode = producersFactory.get(Producers.NODE);
		return producerNode.loadReference(node, code);
	}

	@Override
	public Map<String, Node> requestNodeListItems(String nodeId, NodeDataRequest dataRequest) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
        return producersFactory.<ProducerNodeList>get(Producers.NODELIST).loadItems(nodeId, dataRequest);
	}

	@Override
	public int requestNodeListItemsCount(String nodeId, NodeDataRequest dataRequest) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		return producerNodeList.loadItemsCount(nodeId, dataRequest);
	}

	@Override
	public LocationList requestNodeListItemsLocations(String nodeId, NodeDataRequest dataRequest) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		return producerNodeList.loadItemsLocations(nodeId, dataRequest);
	}

	@Override
	public int requestNodeListItemsLocationsCount(String nodeId, NodeDataRequest dataRequest) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		return producerNodeList.loadItemsLocationsCount(nodeId, dataRequest);
	}

	@Override
	public Map<String, Node> requestNodeSetItems(String nodeId, String contentType, NodeDataRequest dataRequest) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        return producersFactory.<ProducerNodeList>get(Producers.NODELIST).loadSetItems(nodeId, contentType, dataRequest);
	}

	@Override
	public Integer requestNodeSetItemsCount(String nodeId, String contentType, NodeDataRequest dataRequest) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		return producerNodeList.loadSetItemsCount(nodeId, contentType, dataRequest);
	}

	@Override
	public Integer requestRevisionListItemsCount(String nodeId, DataRequest dataRequest) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNodeRevisionList producerNodeRevisions = this.producersFactory.get(Producers.REVISIONLIST);

		return producerNodeRevisions.loadItemsCount(nodeId, dataRequest);
	}

	@Override
	public Map<String, Revision> requestRevisionListItems(String nodeId, DataRequest dataRequest) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNodeRevisionList producerNodeRevisions = this.producersFactory.get(Producers.REVISIONLIST);
		return producerNodeRevisions.loadItems(nodeId, dataRequest);
	}

	@Override
	public NodeItemList searchLinkNodeItems(NodeDataRequest dataRequest) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerDataLink producerDataLink = this.producersFactory.get(Producers.DATALINK);

		DataLink dataLink = (DataLink) producerDataLink.newObject();
		dataLink.setIndexCode(dataRequest.getCodeDomainNode());
		dataLink.setCodeNode(dataRequest.getCode());

		return producerDataLink.searchItems(dataLink, dataRequest.getCode(), dataRequest);
	}

	@Override
	public NodeItemList searchById(NodeDataRequest dataRequest) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerDataLink producerDataLink = this.producersFactory.get(Producers.DATALINK);

		DataLink dataLink = (DataLink) producerDataLink.newObject();
		dataLink.setIndexCode(dataRequest.getCodeDomainNode());
		dataLink.setCodeNode(dataRequest.getCode());

		return producerDataLink.searchItemsByIds(dataLink, dataRequest, dataRequest.getNodeIds());
	}

	@Override
	public int countLinkNodeItemsLocations(NodeDataRequest dataRequest) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		ProducerDataLink producerDataLink = this.producersFactory.get(Producers.DATALINK);
		DataLink dataLink = (DataLink) producerDataLink.newObject();
		dataLink.setIndexCode(dataRequest.getCodeDomainNode());
		dataLink.setCodeNode(dataRequest.getCode());
		return producerDataLink.countItemsLocations(dataLink, dataRequest);
	}

	@Override
	public LocationList searchLinkNodeItemsLocations(NodeDataRequest dataRequest) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		ProducerDataLink producerDataLink = this.producersFactory.get(Producers.DATALINK);
		DataLink dataLink = (DataLink) producerDataLink.newObject();
		dataLink.setIndexCode(dataRequest.getCodeDomainNode());
		dataLink.setCodeNode(dataRequest.getCode());
		return producerDataLink.searchItemsLocations(dataLink, dataRequest);
	}

	@Override
	public String locateNodeId(String code) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		return producerNodeList.locateId(code);
	}

	@Override
	public Node locateNode(String code) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		String nodeId = this.producersFactory.<ProducerNodeList>get(Producers.NODELIST).locateId(code);
		return nodeId.isEmpty() ? null : loadNode(nodeId);
	}

	@Override
	public NodeList loadNodes(String nodeId) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		return producerNode.load(nodeId).getNodeList();
	}

	@Override
	public NodeList loadNodes() {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		return producersFactory.<ProducerNodeList>get(Producers.NODELIST).load();
	}

	@Override
	public List<String> loadNodeIds(String code) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		return producerNodeList.loadIds(code);
	}

	@Override
	public NodeList loadNodesFromTrash(DataRequest oDataRequest) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		return producerNodeList.loadFromTrash(oDataRequest);
	}

	@Override
	public void recoverNodeFromTrash(String id) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.recoverFromTrash(this.loadNode(id));
	}

	@Override
	public void recoverNodesFromTrash(String data) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		for (String node : data.split(Strings.COMMA))
			recoverNodeFromTrash(node);
	}

	@Override
	public void removeNodeFromTrash(String id) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		Node node = this.loadNode(id);

		if (node.getDefinition().isSingleton()) {
			return;
		}

		try {
			componentDocuments.removeDocument(id);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		producerNode.removeFromTrash(this.loadNode(id));
	}

	@Override
	public void saveNode(Node node) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		BusinessModel businessModel = BusinessUnit.getInstance().getBusinessModel();
		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		NodeDefinition nodeDefinition = businessModel.getDictionary().getNodeDefinition(node.getCode());

		if (nodeDefinition instanceof FormDefinition)
			this.savePrepareNodeForm(node, nodeDefinition);

		producerNode.save(node);

		if (nodeDefinition instanceof DocumentDefinition && !node.isLocked() && !nodeDefinition.isReadonly())
			this.saveNodeDocument(node, false);
	}

	@Override
	public void saveNode(Node node, String sData) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		node.deserializeFromXML(sData, null);
		saveNode(node);
	}

	@Override
	public void saveNodeAttributes(Node node, String data) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		AttributeList attributeList = new AttributeList();
		attributeList.deserializeFromXML(data);
		node.setAttributeList(attributeList);
		saveNode(node);
	}

	@Override
	public void saveNodeAttribute(Node node, String data) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		AttributeList attributeList = new AttributeList();
		Attribute attribute = new Attribute();
		attribute.deserializeFromXML(data, null);
		attributeList.add(attribute);
		node.getAttributeList().merge(attributeList);

		this.saveNode(node);
	}

	@Override
	public void saveNodesAttribute(String[] nodesIds, Attribute attribute) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		producersFactory.<ProducerNodeList>get(Producers.NODELIST).saveAttribute(nodesIds, attribute);
	}

	@Override
	public void saveNodeFlags(Node node) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		producersFactory.<ProducerNode>get(Producers.NODE).saveFlags(node);
	}

	@Override
	public void saveNodeNotes(Node node) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		producersFactory.<ProducerNode>get(Producers.NODE).saveNotes(node);
	}

	@Override
	public void saveNodeSchema(Node node) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		producersFactory.<ProducerNode>get(Producers.NODE).saveSchema(node);
	}

	@Override
	public void saveNodePartnerContext(Node node, String partnerContext) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		producersFactory.<ProducerNode>get(Producers.NODE).savePartnerContext(node, partnerContext);
		fillNode(node, node.getDefinition());
		saveNode(node);
	}

	@Override
	public void resetNodeForm(Node node) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		addNodeForm(node, node.getDefinition());
		node.setAttributeList(node.getDefinition().buildAttributes());
		producersFactory.<ProducerNode>get(Producers.NODE).saveAttributes(node, false);
	}

	@Override
	public void updateNodeLocation(Node node, Geometry geometry) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerLocation producerLocation = producersFactory.get(Producers.LOCATION);
		Location location;

		if (!producerLocation.exists(node)) {
			location = new Location();
			location.setNodeId(node.getId());
			location.setLocationId(node.getCode());
			location.setGeometry(geometry);
			location.setCreateDate(new java.util.Date());
			producerLocation.create(location);
		} else {
			location = producerLocation.load(node, null);
			location.setGeometry(geometry);
			producerLocation.save(location);
		}

		node.setLocation(location);

		Reference nodeReference = node.getReference();
		nodeReference.setGeoReferenced(new Number(1));
		this.saveNodeReference(node, nodeReference);
	}

	@Override
	public void cleanNodeLocation(Node node) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		NodeDefinition nodeDefinition = node.getDefinition();
		Geometry geometry = this.getDefaultLocationGeometry();

		ProducerLocation producerLocation = this.producersFactory.get(Producers.LOCATION);

		Location location = node.getLocation();
		if (location == null)
			return;

		location.setGeometry(geometry);
		producerLocation.save(location);

		Reference nodeReference = node.getReference();
		nodeReference.setGeoReferenced(new Number(0));
		this.saveNodeReference(node, nodeReference);
	}

	@Override
	public void makeEditable(Node node) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		producersFactory.<ProducerNode>get(Producers.NODE).makeEditable(node);
	}

	@Override
	public void makeUneditable(Node node) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		producersFactory.<ProducerNode>get(Producers.NODE).makeUneditable(node);
	}

	@Override
	public void makeDeletable(Node node) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		producersFactory.<ProducerNode>get(Producers.NODE).makeDeletable(node);
	}

	@Override
	public void makeUndeletable(Node node) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		producersFactory.<ProducerNode>get(Producers.NODE).makeUndeletable(node);
	}

	@Override
	public String loadNodeReferenceAttributes(Node node) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		AttributeList attributeList = producerNode.loadReferenceAttributes(node);

		return attributeList.serializeToXML().toString();
	}

	@Override
	public ReferenceList getReferences(String codeReference, String filter, String orderBy, Map<String, Object> parameters, int startPos, int limit) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerReferenceList producerReferenceList = this.producersFactory.get(Producers.REFERENCELIST);
		return producerReferenceList.load(codeReference, filter, orderBy, parameters, startPos, limit);
	}

	@Override
	public int getReferencesCount(String codeReference, String filter, Map<String, Object> parameters) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerReferenceList producerReferenceList = this.producersFactory.get(Producers.REFERENCELIST);
		return producerReferenceList.loadTotalCount(codeReference, filter, parameters);
	}

	@Override
	public List<String> getReferencesNodeId(String codeReference) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerReferenceList producerReferenceList = this.producersFactory.get(Producers.REFERENCELIST);
		return producerReferenceList.loadNodeId(codeReference);
	}

	@Override
	public List<String> loadReferenceAttributeValues(String ownerId, String codeReference, String codeAttribute, List<String> filterNodes, List<FilterProperty> filterAttributesDefinition) {
		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
        ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);
		return producerReference.loadAttributeValues(ownerId, codeReference, codeAttribute, filterNodes, filterAttributesDefinition);
	}

	@Override
	public Map<String, Integer> loadReferenceAttributeValuesCount(String ownerId, String codeReference, String codeAttribute, List<String> filterNodes, List<FilterProperty> filterAttributesDefinition) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);
		return producerReference.loadAttributeValuesCount(ownerId, codeReference, codeAttribute, filterNodes, filterAttributesDefinition);
	}

	@Override
	public void createReferenceTable(IndexDefinition definition) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);
		producerReference.createReferenceTable(definition);
	}

	@Override
	public boolean existsReferenceTable(IndexDefinition definition) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);
		return producerReference.existsReferenceTable(producerReference.getReferenceTableName(definition.getCode()));
	}

	@Override
	public void refreshReferenceTable(IndexDefinition definition) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);
		producerReference.refreshReferenceTable(definition);

		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		producerNodeList.removeItemsView(definition);
	}

	@Override
	public void deleteReferenceTable(IndexDefinition definition) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);
		producerReference.deleteReferenceTable(definition);

		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		producerNodeList.removeItemsView(definition);
	}

	@Override
	public void refreshReference(String nodeId, IndexDefinition definition) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.refreshReference(this.loadNode(nodeId), definition);
	}

	@Override
	public void saveNodeReferenceAttributes(Node node, String data) {
		AttributeList attributeList = new AttributeList();

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		attributeList.deserializeFromXML(data);
		producerNode.saveReferenceAttributes(node, attributeList);
	}

	@Override
	public void saveNodeReference(Node node, Reference nodeReference) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.saveReference(node, nodeReference);
	}

	@Override
	public void saveNodePermissions(Node node, PermissionList permissionList) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.savePermissionList(node, permissionList);
	}

	@Override
	public NodeList search(Node node, SearchRequest searchRequest) {
		return this.search(node.getId(), searchRequest);
	}

	@Override
	public NodeList search(String nodeId, SearchRequest searchRequest) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);
		return producerNodeList.search(nodeId, searchRequest);
	}

	@Override
	public Map<String, String> getNodesSorting(String nodeId) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		Session session = this.oContext.getCurrentSession();

		if (session == null) {
			throw new SessionException(ErrorCode.GET_NODESSORTING, this.oContext.getIdSession(Thread.currentThread().getId()));
		}

		Map<String, String> result = new HashMap<>();
		result.put(Common.Sorting.ATTRIBUTE, (String) session.getVariable(nodeId + "_" + SessionVariable.SORTING_ATTRIBUTE));
		result.put(Common.Sorting.MODE, (String) session.getVariable(nodeId + "_" + SessionVariable.SORTING_MODE));

		return result;
	}

	@Override
	public void setNodesSorting(String nodeId, String attribute, String mode) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		Session session = this.oContext.getCurrentSession();
		if (session == null) {
			throw new SessionException(ErrorCode.SET_NODESSORTING, this.oContext.getIdSession(Thread.currentThread().getId()));
		}

		session.setVariable(nodeId + "_" + SessionVariable.SORTING_ATTRIBUTE, attribute);
		session.setVariable(nodeId + "_" + SessionVariable.SORTING_MODE, mode);
	}

	@Override
	public void shareNode(Node node, String idUsers, String description, java.util.Date expireDate) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.share(node, idUsers.split(","), description, expireDate);
	}

	@Override
	public LogBookNode loadNodeBook() {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerLogBookNode producerLogBookNode = this.producersFactory.get(Producers.LOGBOOKNODE);
		return producerLogBookNode.load();
	}

	@Override
	public BookEntryList requestNodeLogBookEntries(DataRequest dataRequest) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerLogBookNode producerLogBookNode = this.producersFactory.get(Producers.LOGBOOKNODE);
		return producerLogBookNode.request(dataRequest);
	}

	@Override
	public int requestNodeLogBookEntriesCount(String nodeId) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerLogBookNode producerLogBookNode = this.producersFactory.get(Producers.LOGBOOKNODE);
		return producerLogBookNode.getCount(nodeId);
	}

	@Override
	public BookEntryList searchNodeLogBookEntries(Integer eventType, java.util.Date from, java.util.Date to) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerLogBookNode producerLogBookNode = this.producersFactory.get(Producers.LOGBOOKNODE);

		return producerLogBookNode.search(eventType, from, to);
	}

	@Override
	public void makeNodePublic(Node node) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.makePublic(node);
	}

	@Override
	public void makeNodePrivate(Node node) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.makePrivate(node);

	}

	@Override
	public void makeNodePrototype(Node node) {
		ProducerNode producerNode;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.makePrototype(node);
	}

	@Override
	public void makeNodePrototyped(Node node, Node prototype) {
		ProducerNode producerNode;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerNode = this.producersFactory.get(Producers.NODE);
		producerNode.makePrototyped(node, prototype);
	}

	@Override
	public void resetYearSequences() {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerSequence producerSequence = this.producersFactory.get(Producers.SEQUENCE);
		producerSequence.resetYearSequences();
	}

	@Override
	public SequenceValue createSequenceValue(String code) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerSequence producerSequence = this.producersFactory.get(Producers.SEQUENCE);

		return producerSequence.createValue(code);
	}

	@Override
	public LogSubscriberList loadNodeSubscribers(Integer type) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerLogBookNode producerLogBookNode = this.producersFactory.get(Producers.LOGBOOKNODE);

		return producerLogBookNode.loadSubscribers();
	}

	@Override
	public void addNodeSubscriber(ServerConfiguration configuration, Integer eventType) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerLogBookNode producerLogBookNode = this.producersFactory.get(Producers.LOGBOOKNODE);
		producerLogBookNode.addSubscriber(configuration, eventType);
	}

	@Override
	public void removeNodeSubscriber(ServerConfiguration configuration, Integer eventType) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerLogBookNode producerLogBookNode = this.producersFactory.get(Producers.LOGBOOKNODE);
		producerLogBookNode.removeSubscriber(configuration, eventType);
	}

	@Override
	public LocationList loadLocationsInNode(Node node, Polygon boundingBox, String indexCode) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerLocationList producerLocationList = this.producersFactory.get(Producers.LOCATIONLIST);
		return producerLocationList.loadInNode(node, null, boundingBox, indexCode);
	}

	@Override
	public LocationList loadLocationsInNode(Node node, String withName, Polygon boundingBox, String indexCode) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerLocationList producerLocationList = this.producersFactory.get(Producers.LOCATIONLIST);
		return producerLocationList.loadInNode(node, withName, boundingBox, indexCode);
	}

	@Override
	public List<Attachment> requestNodeAttachmentItems(String nodeId, NodeDataRequest dataRequest) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		ProducerNode producerNode = this.producersFactory.get(Producers.NODE);

		return producerNode.loadAttachmentItems(nodeId, dataRequest);
	}

	@Override
	public int requestNodeAttachmentItemsCount(String nodeId, NodeDataRequest dataRequest) {
		ProducerNode producerNode;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerNode = this.producersFactory.get(Producers.NODE);

		return producerNode.loadAttachmentItemsCount(nodeId, dataRequest);
	}

	@Override
	public void reset() {
		this.producersFactory.get(Producers.NODE).reset();
	}

}
