/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package org.monet.space.kernel.producers;

import org.monet.bpi.types.Date;
import org.monet.bpi.types.*;
import org.monet.bpi.types.Number;
import org.monet.bpi.types.Term;
import org.monet.metamodel.*;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty.ShowProperty.AttachmentsProperty.FieldNodeProperty;
import org.monet.metamodel.FormDefinitionBase.MappingProperty;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.agents.AgentRuleManager;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.extension.Enricher;
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.utils.cache.Cache;
import org.monet.space.kernel.utils.cache.CacheFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProducerNode extends Producer {
	private ProducerLogBookNode producerLogBookNode;
	private ProducerReference producerReference;
	private ProducerNodeRevision producerNodeRevision;
	private ProducerLocation producerLocation;
	private final Cache<String, Node> cache;

	public ProducerNode() {
		super();
		this.producerLogBookNode = this.producersFactory.get(Producers.LOGBOOKNODE);
		this.producerReference = this.producersFactory.get(Producers.REFERENCE);
		this.producerNodeRevision = this.producersFactory.get((Producers.REVISION));
		this.producerLocation = this.producersFactory.get(Producers.LOCATION);

		try {
			cache = CacheFactory.getInstance().get("NodeCache");
			cache.addListener(AgentRuleManager.getInstance());
		} catch (Exception e) {
			agentLogger.error(e);
			throw new RuntimeException("Can't initialize NodeLayerMonet", e);
		}
	}

	private void loadProperties(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		Dictionary dictionary = Dictionary.getInstance();

		parameters.put(Database.QueryFields.ID, node.getId());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD, parameters);

			if (!result.next())
				throw new DataException(ErrorCode.NODE_NOT_FOUND, node.getId());

			node.setId(result.getString("id"));
			node.setParentId(result.getString("id_parent"));
			node.setOwnerId(result.getString("id_owner"));
			node.setPrototypeId(result.getString("id_prototype"));
			node.setCode(result.getString("code"));
			node.setType(NodeDefinition.getType(result.getString("definition_type")));
			node.setOrder(result.getInt("ordering"));
			node.setIsValid(result.getInt("valid") == 1);

			if (dictionary.existsDefinition(node.getCode()))
				node.setName(dictionary.getNodeDefinition(node.getCode()).getName());
		} catch (DataException exception) {
			throw exception;
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODE, node.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	private HashMap<String, SuperDataItem> loadSuperDataFromDB(Node node) {
		ResultSet resultSet = null;
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, SuperDataItem> result;

		parameters.put(Database.QueryFields.ID_NODE, node.getId());

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_SUPER_DATA_LOAD, parameters);

			result = new HashMap<>();
			while (resultSet.next()) {
				SuperDataItem superData = new SuperDataItem(resultSet.getString("id"), resultSet.getString("code_attribute"), resultSet.getString("code"), resultSet.getString("value"), resultSet.getString("data"));
				result.put(superData.getId(), superData);
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_NODE, node.getId(), oException);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return result;
	}

	private Boolean loadSuperData(Node node) {
		FormDefinition definition;
		HashMap<String, SuperDataItem> superData;

		if (!node.isForm())
			return false;

		definition = (FormDefinition) node.getDefinition();
		if (!definition.hasSuperFieldProperties())
			return false;

		superData = this.loadSuperDataFromDB(node);
		node.setSuperData(superData);

		return true;
	}

	private Boolean saveSuperData(Node node) {
		if (!node.isForm())
			return false;

		FormDefinition definition = (FormDefinition) node.getDefinition();
		if (!definition.hasSuperFieldProperties())
			return false;

		Set<DatabaseRepositoryQuery> queries = new HashSet<>();
		Map<String, SuperDataItem> newSuperData = node.getSuperData();
		Iterator<String> iterator = newSuperData.keySet().iterator();
		String userId = this.getUserId();

		while (iterator.hasNext()) {
			HashMap<String, Object> saveParameters = new HashMap<>();
			HashMap<String, Object> addParameters = new HashMap<>();
			String idSuperDataItem = iterator.next();
			SuperDataItem newSuperDataItem = newSuperData.get(idSuperDataItem);

			saveParameters.put(Database.QueryFields.ID, idSuperDataItem);
			saveParameters.put(Database.QueryFields.CODE, newSuperDataItem.getCode());
			saveParameters.put(Database.QueryFields.VALUE, newSuperDataItem.getValue());
			saveParameters.put(Database.QueryFields.DATA, newSuperDataItem.getData());
			queries.add(new DatabaseRepositoryQuery(Database.Queries.NODE_SUPER_DATA_SAVE, saveParameters));

			addParameters.put(Database.QueryFields.ID_NODE, node.getId());
			addParameters.put(Database.QueryFields.ID_SUPER_DATA, idSuperDataItem);
			addParameters.put(Database.QueryFields.ID_USER, userId);
			addParameters.put(Database.QueryFields.CODE_ATTRIBUTE, newSuperDataItem.getCodeAttribute());
			addParameters.put(Database.QueryFields.CODE, newSuperDataItem.getCode());
			addParameters.put(Database.QueryFields.VALUE, newSuperDataItem.getValue());
			addParameters.put(Database.QueryFields.DATA, newSuperDataItem.getData());
			addParameters.put(Database.QueryFields.MERGED, 1);
			addParameters.put(Database.QueryFields.REVISION_DATE, this.agentDatabase.getDateAsTimestamp(new java.util.Date()));
			queries.add(new DatabaseRepositoryQuery(Database.Queries.NODE_SUPER_DATA_ADD_REVISION, addParameters));
		}

		if (queries.size() > 0) {
			this.agentDatabase.executeRepositoryQueries(queries.toArray(new DatabaseRepositoryQuery[0]));
		}

		return true;
	}

	private Boolean loadParent(Node node) {
		Node parent;

		parent = this.load(node.getParentId());
		node.setParent(parent);

		return true;
	}

	private Boolean loadOwner(Node node) {
		ProducerFederation producerFederation;
		User owner;

		if (node.getOwnerId() == null)
			return false;

		producerFederation = (ProducerFederation) this.producersFactory.get(Producers.FEDERATION);
		owner = producerFederation.loadUser(node.getOwnerId());
		node.setOwner(owner);

		return true;
	}

	private Boolean loadAncestors(Node node) {
		NodeList ancestors = new NodeList();
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		boolean isFirstAncestor = true;
		Node rootNode = rootNodeOfAccount();
		boolean includeAncestor = false;

		if (rootNode != null && rootNode.getId().equals(node.getId())) {
			node.setAncestors(ancestors);
			return true;
		}

		try {
			parameters.put(Database.QueryFields.ID_NODE, node.getId());
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_PARENTS_LABELS, parameters);

			while (result.next()) {
				String code = result.getString("code");
				String nodeId = result.getString("id_node");

				if (rootNode == null || !nodeId.equals(rootNode.getId()))
					includeAncestor = true;

				if (!includeAncestor)
					continue;

				NodeDefinition definition = Dictionary.getInstance().getNodeDefinition(code);
				if (isFirstAncestor && (definition.isDesktop() || (definition.isContainer() && ((ContainerDefinition) definition).isEnvironment())))
					continue;

				Node ancestor = new Node();
				ancestor.setId(nodeId);
				ancestor.setCode(code);
				ancestor.addReference(new Reference(DescriptorDefinition.CODE));
				ancestor.setLabel(result.getString("label"));
				ancestors.add(ancestor);
				isFirstAncestor = false;

			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODE_PARENTS, node.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		node.setAncestors(ancestors);

		return true;
	}

	private Node rootNodeOfAccount() {
		Account account = this.getAccount();
		return account != null ? account.getRootNode() : null;
	}

	private String ownerIdOfAccount() {
		Account account = this.getAccount();
		return account != null ? account.getUser().getId() : "-1";
	}

	private Boolean loadPartnerContext(Node node) {
		NodeList ancestors = new NodeList();
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		Node rootNode = rootNodeOfAccount();
		String partnerContext = null;

		if (rootNode != null && rootNode.getId().equals(node.getId())) {
			node.setAncestors(ancestors);
			return true;
		}

		parameters.put(Database.QueryFields.ID, node.getId());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_PARTNER_CONTEXT, parameters);

			if (result.next())
				partnerContext = result.getString("partner_context");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODE_PARTNER_CONTEXT, node.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		node.setPartnerContext(partnerContext);

		return true;
	}

	private Boolean loadPrototypes(Node node) {

		if (!(node.getDefinition() instanceof CollectionDefinition))
			return true;

		NodeList ownedPrototypes = new NodeList(), sharedPrototypes = new NodeList();
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, String> queries = new HashMap<String, String>();
		ArrayList<String> codesArray = new ArrayList<String>();
		HashMap<String, String> codesMap = new HashMap<String, String>();
		HashMap<String, String> parentsMap = new HashMap<String, String>();
		HashMap<String, String> labelsMap = new HashMap<String, String>();
		HashMap<String, String> descriptionsMap = new HashMap<String, String>();
		CollectionDefinition definition = (CollectionDefinition) node.getDefinition();

		for (Ref add : definition.getAdd().getNode()) {
			for (Definition addDefinition : Dictionary.getInstance().getAllImplementersOfNodeDefinition(add.getValue()))
				codesArray.add(addDefinition.getCode());
		}

		parameters.put(Database.QueryFields.ID_NODE, node.getId());
		queries.put(Database.QueryFields.CODE_NODES, "'" + LibraryArray.implode(codesArray.toArray(new String[0]), "','") + "'");

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_PROTOTYPES_INFO, parameters, queries);

			while (result.next()) {
				String id = result.getString("id_node");
				String parentId = result.getString("id_parent");
				String code = result.getString("code");

				codesMap.put(id, code);
				parentsMap.put(id, parentId);
				labelsMap.put(id, result.getString("label"));
				descriptionsMap.put(id, result.getString("description"));
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODE_PROTOTYPES, node.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		for (String id : codesMap.keySet()) {
			Node prototype = this.lightLoad(id, codesMap.get(id));
			prototype.setLabel(labelsMap.get(id));
			prototype.setDescription(descriptionsMap.get(id));

			String parentId = parentsMap.get(id);
			if (parentId == null || parentId.equals("-1"))
				sharedPrototypes.add(prototype);
			else
				ownedPrototypes.add(prototype);
		}

		node.setOwnedPrototypes(ownedPrototypes);
		node.setSharedPrototypes(sharedPrototypes);

		return true;
	}

	private void encryptSuperData(Node node) {
		FormDefinition definition;
		AttributeList attributeList;
		List<FieldProperty> superFieldDefinitions;
		HashMap<String, SuperDataItem> result;

		if (!node.isForm())
			return;

		definition = (FormDefinition) node.getDefinition();
		if (!definition.hasSuperFieldProperties())
			return;

		attributeList = node.getAttributeList();
		superFieldDefinitions = definition.getSuperFieldPropertyList();
		result = new HashMap<>();

		for (FieldProperty superFieldDefinition : superFieldDefinitions) {
			LinkedHashMap<String, Attribute> attributes = attributeList.getByCode(superFieldDefinition.getCode());

			for (Attribute attribute : attributes.values()) {
				IndicatorList indicatorList;
				String idSuperData = attribute.getIndicatorValue(Indicator.SUPER);
				SuperDataItem superDataItem = new SuperDataItem();

				superDataItem.setCodeAttribute(attribute.getCode());
				superDataItem.setCode(attribute.getIndicatorValue(Indicator.CODE));
				superDataItem.setData(attribute.getIndicatorList().serializeToXML().toString());
				if (superFieldDefinition instanceof SelectFieldProperty) {
					String value = attribute.getIndicatorValue(Indicator.VALUE);
					if (value.equals(""))
						superDataItem.setValue(attribute.getIndicatorValue(Indicator.OTHER));
					else
						superDataItem.setValue(value);
				} else
					superDataItem.setValue(attribute.getIndicatorValue(Indicator.VALUE));

				if ((idSuperData.equals(Strings.EMPTY)) || (idSuperData.equals("-1")))
					idSuperData = this.createSuperDataItem(node.getId(), superDataItem);

				indicatorList = attribute.getIndicatorList();
				for (Indicator indicator : indicatorList) {
					indicator.setData(Indicator.getSuperDataAddress(indicator.getCode(), idSuperData));
				}
				indicatorList.delete(Indicator.SUPER);
				indicatorList.add(new Indicator(Indicator.SUPER, -1, idSuperData));

				result.put(idSuperData, superDataItem);
			}
		}

		node.setSuperData(result);
	}

	private String createSuperDataItem(String nodeId, SuperDataItem superDataItem) {
		HashMap<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_NODE, nodeId);
		parameters.put(Database.QueryFields.CODE_ATTRIBUTE, superDataItem.getCodeAttribute());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_SUPER_DATA_DELETE, parameters);

		parameters.clear();
		parameters.put(Database.QueryFields.ID_NODE, nodeId);
		parameters.put(Database.QueryFields.CODE_ATTRIBUTE, superDataItem.getCodeAttribute());
		parameters.put(Database.QueryFields.CODE, superDataItem.getCode());
		parameters.put(Database.QueryFields.VALUE, superDataItem.getValue());
		parameters.put(Database.QueryFields.DATA, superDataItem.getData());

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.NODE_SUPER_DATA_ADD, parameters);
		superDataItem.setId(id);

		return id;
	}

	private void decryptSuperData(Node node) {
		FormDefinition definition;
		HashMap<String, SuperDataItem> superData;
		Boolean foundAnySuperData = false;
		String data;
		Pattern pattern;
		Matcher matcher;

		if (!node.isForm())
			return;

		definition = (FormDefinition) node.getDefinition();
		if (!definition.hasSuperFieldProperties())
			return;

		superData = this.loadSuperDataFromDB(node);
		data = node.getData().toString();
		pattern = Pattern.compile(Indicator.SUPER_DATA_PATTERN);
		matcher = pattern.matcher(data);

		while (matcher.find()) {
			String codeIndicator = matcher.group(1);
			String idSuperDataItem = matcher.group(2);

			if (superData.containsKey(idSuperDataItem)) {
				SuperDataItem superDataItem = superData.get(idSuperDataItem);
				IndicatorList indicatorList = superDataItem.getIndicatorList();
				data = LibraryString.replaceAll(data, matcher.group(0), ((Indicator) indicatorList.get(codeIndicator)).getData());
			} else
				data = LibraryString.replaceAll(data, matcher.group(0), "");

			foundAnySuperData = true;
			matcher = pattern.matcher(data);
		}

		if (foundAnySuperData)
			node.setData(data);
	}

	private void decryptSuperDataFromReference(Node node, Reference reference) {
		FormDefinition definition;
		HashSet<String> attributes;
		IndexDefinition referenceDefinition = reference.getDefinition();

		if (!node.isForm())
			return;

		definition = (FormDefinition) node.getDefinition();
		if (!definition.hasSuperFieldProperties())
			return;

		attributes = definition.getSchemaAttributesUsingSuperFields();
		this.loadSuperData(node);

		for (String codeAttribute : attributes) {
			if (reference.existsAttribute(codeAttribute)) {
				String idSuperDataItem = reference.getAttributeValue(codeAttribute);
				SuperDataItem superDataItem = node.getSuperDataItem(idSuperDataItem);

				if (superDataItem == null)
					continue;

				switch (referenceDefinition.getAttribute(codeAttribute).getType()) {
					case BOOLEAN:
						reference.setAttributeValue(codeAttribute, Boolean.valueOf(superDataItem.getCode()));
						break;
					case INTEGER:
						reference.setAttributeValue(codeAttribute, new Number(Double.valueOf(superDataItem.getCode()), superDataItem.getValue()));
						break;
					case REAL:
						reference.setAttributeValue(codeAttribute, new Number(Double.valueOf(superDataItem.getCode()), superDataItem.getValue()));
						break;
					case STRING:
					case CATEGORY:
						reference.setAttributeValue(codeAttribute, superDataItem.getValue());
						break;
					case DATE:
						Date dateTime = new Date(LibraryDate.parseDate(superDataItem.getCode()));
						dateTime.setFormattedValue(superDataItem.getValue());
						reference.setAttributeValue(codeAttribute, dateTime);
						break;
					case PICTURE:
						Picture picture = new Picture(superDataItem.getValue());
						picture.setFilename(superDataItem.getValue());
						reference.setAttributeValue(codeAttribute, picture);
						break;
					case TERM:
						reference.setAttributeValue(codeAttribute, new Term(superDataItem.getCode(), superDataItem.getValue()));
						break;
					case LINK:
						reference.setAttributeValue(codeAttribute, new Link(superDataItem.getCode(), superDataItem.getValue()));
						break;
					case CHECK:
						break;
				}
			}
		}
	}

	private AttributeList mergeFormUnivocalAttributes(AttributeList attributeList, AttributeList prototypeAttributeList, FormDefinition definition) {
		MonetHashMap<Attribute> prototypeAttributes = prototypeAttributeList.get();
		Iterator<Attribute> iter = prototypeAttributes.values().iterator();
		AttributeList result = new AttributeList();

		while (iter.hasNext()) {
			Attribute resultAttribute;
			Attribute prototypeAttribute = iter.next();
			Attribute attribute = attributeList.get(prototypeAttribute.getCode());
			FieldProperty fieldDefinition = definition.getField(prototypeAttribute.getCode());

			if (prototypeAttribute.getCode().equals(Attribute.OPTION)) {
				result.add(prototypeAttribute);
				continue;
			}

			if (fieldDefinition == null)
				continue;

			if (!fieldDefinition.isStatic() || fieldDefinition.isUnivocal() || fieldDefinition.isSerial()) {
				if (attribute != null)
					result.add(attribute);
				continue;
			}

			resultAttribute = new Attribute();
			resultAttribute.setCode(prototypeAttribute.getCode());
			resultAttribute.setOrder(prototypeAttribute.getOrder());
			resultAttribute.setIndicatorList(prototypeAttribute.getIndicatorList());

			if (fieldDefinition.isComposite() && fieldDefinition.isMultiple()) {
				LinkedHashMap<String, Attribute> prototypeAttributesByCode = prototypeAttribute.getAttributeList().getByCode(prototypeAttribute.getCode());
				LinkedHashMap<String, Attribute> attributesByCode = attribute.getAttributeList().getByCode(prototypeAttribute.getCode());
				Iterator<Attribute> prototypeAttributesIter = prototypeAttributesByCode.values().iterator();
				Iterator<Attribute> attributesIter = attributesByCode.values().iterator();
				AttributeList resultAttributeList = new AttributeList();

				while (prototypeAttributesIter.hasNext()) {
					Attribute prototypeMultipleAttribute = prototypeAttributesIter.next();
					Attribute multipleAttribute = null;

					if (attributesIter.hasNext())
						multipleAttribute = attributesIter.next();

					if (multipleAttribute == null) {
						resultAttributeList.add(prototypeMultipleAttribute);
						continue;
					}

					AttributeList multipleAttributeList = multipleAttribute.getAttributeList();
					if (prototypeMultipleAttribute.getAttributeList().getCount() > 0)
						multipleAttributeList = this.mergeFormUnivocalAttributes(multipleAttribute.getAttributeList(), prototypeMultipleAttribute.getAttributeList(), definition);

					multipleAttribute.setCode(prototypeMultipleAttribute.getCode());
					multipleAttribute.setOrder(prototypeMultipleAttribute.getOrder());
					multipleAttribute.setIndicatorList(prototypeMultipleAttribute.getIndicatorList());
					multipleAttribute.setAttributeList(multipleAttributeList);

					resultAttributeList.add(multipleAttribute);
				}

				resultAttribute.setAttributeList(resultAttributeList);
			} else {
				Attribute currentAttribute = attributeList.get(prototypeAttribute.getCode());
				if (currentAttribute == null)
					resultAttribute.setAttributeList(prototypeAttribute.getAttributeList());
				else
					resultAttribute.setAttributeList(this.mergeFormUnivocalAttributes(currentAttribute.getAttributeList(), prototypeAttribute.getAttributeList(), definition));
			}

			result.add(resultAttribute);
		}

		if (result.getCount() < attributeList.getCount()) {
			for (Attribute attribute : attributeList.get().values()) {
				if (result.exist(attribute.getId()))
					continue;
				if (result.exist(attribute.getCode()))
					continue;
				result.add(attribute);
			}
		}

		return result;
	}

	private Boolean loadData(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		String data;

		parameters.put(Database.QueryFields.ID, node.getId());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_DATA, parameters);

			if (!result.next())
				throw new Exception(String.format("Node '%s' not found", node.getId()));
			data = result.getString("data");
			this.agentDatabase.closeQuery(result);

			if (data == null)
				data = Strings.EMPTY;

			if (node.isForm() && node.isPrototyped()) {
				Node prototype = this.load(node.getPrototypeId());
				AttributeList attributeList = new AttributeList();
				AttributeList prototypeAttributeList = prototype.getAttributeList();
				attributeList.deserializeFromXML(data);
				attributeList = this.mergeFormUnivocalAttributes(attributeList, prototypeAttributeList, (FormDefinition) node.getDefinition());
				data = attributeList.serializeToXML();
			}

		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_NODE, node.getId(), oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		node.setData(data);
		this.decryptSuperData(node);

		return true;
	}

	private void loadAttributeList(Node node) {
		this.loadData(node);

		AttributeList attributeList = new AttributeList();
		attributeList.deserializeFromXML(node.getData().substring(0), new NodeDefinitionChangesResolver(node.getDefinition()));
		node.setAttributeList(attributeList);
	}

	private String saveData(Node node) {
		HashMap<String, Object> parameters = new HashMap<>();
		String data;

		data = node.getAttributeList().serializeToXML().toString();

		parameters.put(Database.QueryFields.ID, node.getId());
		parameters.put(Database.QueryFields.ID_PARENT, node.getParentId());
		parameters.put(Database.QueryFields.ID_OWNER, node.getOwnerId());
		parameters.put(Database.QueryFields.ID_PROTOTYPE, node.getPrototypeId());
		parameters.put(Database.QueryFields.CODE, node.getCode());
		parameters.put(Database.QueryFields.DEFINITION_TYPE, node.getType().toString());
		parameters.put(Database.QueryFields.ORDERING, String.valueOf(node.getOrder()));
		parameters.put(Database.QueryFields.DATA, data);
		parameters.put(Database.QueryFields.VALID, node.isValid() ? "1" : "0");
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_SAVE, parameters);

		this.producerNodeRevision.create(node, data);

		return data;
	}

	private String saveDataWithoutMerge(Node node) {
		HashMap<String, Object> parameters = new HashMap<>();
		String data;

		data = node.getAttributeList().serializeToXML().toString();

		parameters.put(Database.QueryFields.ID, node.getId());
		parameters.put(Database.QueryFields.ID_PARENT, node.getParentId());
		parameters.put(Database.QueryFields.ID_OWNER, node.getOwnerId());
		parameters.put(Database.QueryFields.ID_PROTOTYPE, node.getPrototypeId());
		parameters.put(Database.QueryFields.CODE, node.getCode());
		parameters.put(Database.QueryFields.DEFINITION_TYPE, node.getType().toString());
		parameters.put(Database.QueryFields.ORDERING, String.valueOf(node.getOrder()));
		parameters.put(Database.QueryFields.DATA, data);
		parameters.put(Database.QueryFields.VALID, node.isValid() ? "1" : "0");
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_SAVE, parameters);

		this.producerNodeRevision.create(node, data);

		return data;
	}

	private Boolean loadFlags(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		String flags;
		LinkedHashMap<String, String> flagsMap = new LinkedHashMap<String, String>();

		parameters.put(Database.QueryFields.ID, node.getId());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_FLAGS, parameters);

			if (!result.next())
				throw new Exception(String.format("Flags of node '%s' not found", node.getId()));

			flags = result.getString("flags");

			if (flags != null)
				flagsMap = SerializerData.deserialize(flags);

		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_NODE, node.getId(), oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		node.setFlags(flagsMap);

		return true;
	}

	private Boolean loadNotes(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		String notes;
		LinkedHashMap<String, String> notesMap = new LinkedHashMap<String, String>();

		parameters.put(Database.QueryFields.ID, node.getId());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_NOTES, parameters);

			if (!result.next())
				throw new Exception(String.format("Notes of node '%s' not found", node.getId()));

			notes = result.getString("notes");

			if (notes != null)
				notesMap = SerializerData.deserialize(notes);

		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_NODE, node.getId(), oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		node.setNotes(notesMap);

		return true;
	}

	private Boolean loadLinksCounter(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		Integer counter;

		parameters.put(Database.QueryFields.ID_TARGET, node.getId());
		parameters.put(Database.QueryFields.TYPE_TARGET, LinkType.NODE);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.LINK_COUNT_SOURCES, parameters);

			if (!result.next())
				throw new Exception(String.format("Can't get links total count of '%s'", node.getId()));
			counter = result.getInt("counter");
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LINK_COUNT_SOURCES, node.getId(), oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		node.setLinksCounter(counter);

		return true;
	}

	private void loadLinksFromTaskCounter(Node node) {
		ProducerTaskList producerTaskList;
		int counter;

		producerTaskList = (ProducerTaskList) this.producersFactory.get(Producers.TASKLIST);

		counter = producerTaskList.loadLinkedWithNodeCount(node.getId());

		node.setLinksFromTasksCounter(counter);
	}

	private void loadAttachmentsCounter(Node node) {
		int counter = this.loadAttachmentItemsCount(node.getId(), new NodeDataRequest());
		node.setAttachmentsCounter(counter);
	}

	private void loadLinkedTasks(Node node) {
		ProducerTaskList producerTaskList = this.producersFactory.get(Producers.TASKLIST);
		TaskList taskList = producerTaskList.loadLinkedWithNode(node.getId());
		node.setLinkedTasks(taskList);
	}

	private Boolean saveLinks(Node node) {
		AttributeList attributeList = node.getAttributeList();
		HashMap<String, Object> parameters = new HashMap<>();
		FormDefinition formDefinition = Dictionary.getInstance().getFormDefinition(node.getCode());
		Collection<LinkFieldProperty> linkDefinitions = formDefinition.getLinkFieldPropertyList();
		ArrayList<DatabaseRepositoryQuery> queries = new ArrayList<DatabaseRepositoryQuery>();
		Node mainNode = node.getMainNode();

		parameters.put(Database.QueryFields.ID_SOURCE, mainNode.getId());
		parameters.put(Database.QueryFields.ID_SOURCE_COMPONENT, node.getId());
		parameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.NODE);
		queries.add(new DatabaseRepositoryQuery(Database.Queries.LINK_DELETE, parameters));

		for (LinkFieldProperty fieldDefinition : linkDefinitions) {
			LinkedHashSet<Attribute> attributes = attributeList.searchByCode(fieldDefinition.getCode());
			HashSet<String> linkedNodes = new HashSet<String>();

			for (Attribute attribute : attributes) {
				String idTarget = attribute.getIndicatorValue(Indicator.NODE_LINK);
				HashMap<String, Object> queryParameters = new HashMap<>();

				if ((idTarget.equals(Strings.UNDEFINED_ID)) || (idTarget.equals(Strings.EMPTY)))
					continue;

				if (linkedNodes.contains(idTarget))
					continue;

				Node target = this.load(idTarget);
				queryParameters.put(Database.QueryFields.ID_SOURCE, mainNode.getId());
				queryParameters.put(Database.QueryFields.ID_SOURCE_COMPONENT, node.getId());
				queryParameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.NODE);
				queryParameters.put(Database.QueryFields.ID_TARGET, target.getMainNode().getId());
				queryParameters.put(Database.QueryFields.TYPE_TARGET, LinkType.NODE);
				queryParameters.put(Database.QueryFields.DATA, fieldDefinition.getName());
				queryParameters.put(Database.QueryFields.DELETE_DATE, null);
				queries.add(new DatabaseRepositoryQuery(Database.Queries.LINK_ADD, queryParameters));

				linkedNodes.add(idTarget);
			}
		}

		try {
			this.agentDatabase.executeRepositoryUpdateTransaction(queries.toArray(new DatabaseRepositoryQuery[0]));
		} catch (SQLException e) {
			this.agentLogger.error(e);
		}

		return true;
	}

	private synchronized Boolean saveAttachments(Node node) {
		AttributeList attributeList = node.getAttributeList();
		HashMap<String, Object> parameters = new HashMap<>();
		FormDefinition formDefinition = Dictionary.getInstance().getFormDefinition(node.getCode());
		Collection<NodeFieldProperty> nodeDefinitions = formDefinition.getNodeFieldPropertyList();
		Collection<FileFieldProperty> fileDefinitions = formDefinition.getFileFieldPropertyList();
		ArrayList<DatabaseRepositoryQuery> queries = new ArrayList<DatabaseRepositoryQuery>();
		HashSet<String> targets = new HashSet<String>();

		parameters.put(Database.QueryFields.ID_SOURCE, node.getId());
		queries.add(new DatabaseRepositoryQuery(Database.Queries.ATTACHMENT_DELETE, parameters));

		for (NodeFieldProperty fieldDefinition : nodeDefinitions) {
			LinkedHashSet<Attribute> attributes = attributeList.searchByCode(fieldDefinition.getCode());

			for (Attribute attribute : attributes) {
				String idTarget = attribute.getIndicatorValue(Indicator.CODE);
				HashMap<String, Object> queryParameters = new HashMap<>();

				if (idTarget.equals(Strings.UNDEFINED_ID) || idTarget.equals(Strings.EMPTY) || targets.contains(idTarget))
					continue;

				Node target = this.load(idTarget);
				queryParameters.put(Database.QueryFields.ID_SOURCE, node.getId());
				queryParameters.put(Database.QueryFields.ID_TARGET, idTarget);
				queryParameters.put(Database.QueryFields.TYPE_TARGET, AttachmentType.NODE);
				queryParameters.put(Database.QueryFields.CODE_TARGET, fieldDefinition.getCode());
				queryParameters.put(Database.QueryFields.DATA, target.getLabel());
				queryParameters.put(Database.QueryFields.DELETE_DATE, null);
				queries.add(new DatabaseRepositoryQuery(Database.Queries.ATTACHMENT_ADD, queryParameters));

				targets.add(idTarget);
			}
		}

		for (FileFieldProperty fieldDefinition : fileDefinitions) {
			LinkedHashSet<Attribute> attributes = attributeList.searchByCode(fieldDefinition.getCode());

			for (Attribute attribute : attributes) {
				String idTarget = attribute.getIndicatorValue(Indicator.VALUE);
				String details = attribute.getIndicatorValue(Indicator.DETAILS);
				HashMap<String, Object> queryParameters = new HashMap<>();

				if (idTarget.equals(Strings.UNDEFINED_ID) || idTarget.equals(Strings.EMPTY) || targets.contains(idTarget))
					continue;

				queryParameters.put(Database.QueryFields.ID_SOURCE, node.getId());
				queryParameters.put(Database.QueryFields.ID_TARGET, idTarget);
				queryParameters.put(Database.QueryFields.TYPE_TARGET, AttachmentType.FILE);
				queryParameters.put(Database.QueryFields.CODE_TARGET, fieldDefinition.getCode());
				queryParameters.put(Database.QueryFields.DATA, details != null && !details.isEmpty() ? details : idTarget);
				queryParameters.put(Database.QueryFields.DELETE_DATE, null);
				queries.add(new DatabaseRepositoryQuery(Database.Queries.ATTACHMENT_ADD, queryParameters));

				targets.add(idTarget);
			}
		}

		try {
			this.agentDatabase.executeRepositoryUpdateTransaction(queries.toArray(new DatabaseRepositoryQuery[0]));
		} catch (SQLException e) {
			this.agentLogger.error(e);
		}

		return true;
	}

	private boolean loadSchema(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		String schema;

		parameters.put(Database.QueryFields.ID_NODE, node.getId());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_SCHEMA_LOAD, parameters);

			if (!result.next())
				schema = Strings.EMPTY;
			else {
				schema = result.getString("data");
				if (schema == null)
					schema = Strings.EMPTY;
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODE, node.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		node.setSchema(schema);

		return true;
	}

	private Boolean loadNodeList(Node node) {
		ProducerNodeList producerNodeList;
		NodeList nodeList;

		producerNodeList = (ProducerNodeList) this.producersFactory.get(Producers.NODELIST);
		nodeList = (NodeList) producerNodeList.newObject();
		node.setNodeList(nodeList);
		nodeList.setIdNode(node.getId());

		return true;
	}

	private Boolean loadClientNodeList(Node node) {
		ProducerNodeList producerNodeList;
		NodeList nodeList;

		producerNodeList = (ProducerNodeList) this.producersFactory.get(Producers.NODELIST);
		nodeList = (NodeList) producerNodeList.newObject();
		node.setClientNodeList(nodeList);
		nodeList.setIdNode(node.getId());

		return true;
	}

	public ArrayList<String> loadNodeIds(Node node, String query) {
		return loadNodeIds(node.getId(), query);
	}

	public ArrayList<String> loadNodeIds(String nodeId, String query) {
		HashMap<String, Object> parameters = new HashMap<>();
		ArrayList<String> nodesIds = new ArrayList<String>();
		ResultSet resultSet = null;

		try {
			parameters.put(Database.QueryFields.ID_NODE, nodeId);
			resultSet = this.agentDatabase.executeRepositorySelectQuery(query, parameters, null);

			while (resultSet.next())
				nodesIds.add(resultSet.getString("id_node"));

			nodesIds.add(nodeId);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODE_IDS, nodeId, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return nodesIds;
	}

	private Boolean loadPermissionList(Node node) {
		PermissionList permissionList;
		ResultSet result;
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		ArrayList<String> nodesIds = this.loadNodeIds(node, Database.Queries.NODE_PERMISSION_LIST_LOAD_SUBQUERY);

		subQueries.put(Database.QueryFields.ID_NODES, "'" + LibraryArray.implode(nodesIds.toArray(new String[0]), "','") + "'");
		parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new java.util.Date()));
		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_PERMISSION_LIST_LOAD, parameters, subQueries);

		try {
			permissionList = new PermissionList();
			while (result.next()) {
				Permission nodePermission = new Permission();
				nodePermission.setId(result.getString("id"));
				nodePermission.setIdObject(result.getString("id_node"));
				nodePermission.setIdUser(result.getString("id_user"));
				nodePermission.setType(result.getInt("type"));
				nodePermission.setBeginDate(result.getTimestamp("begin_date"));
				nodePermission.setExpireDate(result.getTimestamp("expire_date"));
				permissionList.add(nodePermission);
			}
			node.setPermissionList(permissionList);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODE, node.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return true;
	}

	private Boolean enrich(Node node, boolean newNode) {
		Session session = Context.getInstance().getCurrentSession();

		if (session == null || session.getAccount() == null)
			return true;

		Enricher enricher = Enricher.getInstance();
		ProducerTaskList producerTaskList = this.producersFactory.get(Producers.TASKLIST);
		TaskList taskList;

		if (newNode)
			taskList = new TaskList();
		else
			taskList = producerTaskList.load(node, Strings.ALL, TaskCode.REVISION);

		enricher.completeNode(node, taskList);
		if (taskList.getCount() > 0)
			producerTaskList.save(taskList, node.getId(), TaskCode.REVISION);

		node.setIsValid((taskList.getCount() <= 0));

		return true;
	}

	private Boolean addToLogBook(String nodeId, Integer eventType) {
		this.producerLogBookNode.addEntry(new NodeLogBookEntry(nodeId, eventType));
		return true;
	}

	private Boolean isNodeExportable(Node node, String[] nodeTypes, java.util.Date from, java.util.Date to) {
		Integer pos = 0;
		java.util.Date created = node.getReference().getCreateDate().getValue();

		if (nodeTypes != null) {
			while (pos < nodeTypes.length) {
				if (nodeTypes[pos].equals(node.getCode()))
					break;
				pos++;
			}
			if (pos >= nodeTypes.length)
				return false;
		}

		if ((from != null) && (created.compareTo(from) < 1))
			return false;
		if ((to != null) && (created.compareTo(from) > 1))
			return false;

		return true;
	}

	private void saveContainer(Node node) {
		this.saveData(node);
	}

	private void saveDesktop(Node node) {
	}

	private void saveForm(Node node) {
		this.enrich(node, false);
		this.encryptSuperData(node);
		this.saveData(node);
		this.saveSuperData(node);

		node.setAttributeList(new AttributeList());
		node.removeLoadedAttribute(Node.ATTRIBUTELIST);
		node.setData("");
		node.removeLoadedAttribute(Node.DATA);

		this.saveLinks(node);
		this.saveAttachments(node);
	}

	private void saveCollection(Node node) {
		this.saveReferenceAttributes(node, node.getAttributeList());
		this.saveData(node);

		node.setAttributeList(new AttributeList());
		node.removeLoadedAttribute(Node.ATTRIBUTELIST);
		node.setData("");
		node.removeLoadedAttribute(Node.DATA);
	}

	private void saveCatalog(Node node) {
		this.saveData(node);

		node.setAttributeList(new AttributeList());
		node.removeLoadedAttribute(Node.ATTRIBUTELIST);
		node.setData("");
		node.removeLoadedAttribute(Node.DATA);
	}

	private void saveDocument(Node node) {
	}

	private Boolean createReferences(Node node) {
		NodeDefinition nodeDefinition = node.getDefinition();

		if (nodeDefinition.isForm()) {
			ArrayList<MappingProperty> _implements = ((FormDefinition) nodeDefinition).getMappingList();
			for (MappingProperty _index : _implements) {
				IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(_index.getIndex().getValue());
				this.createReference(node, referenceDefinition);
			}
		} else if (nodeDefinition.isDocument()) {
			ArrayList<DocumentDefinition.MappingProperty> _implements = ((DocumentDefinition) nodeDefinition).getMappingList();
			if (_implements != null) {
				for (DocumentDefinition.MappingProperty _index : _implements) {
					IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(_index.getIndex().getValue());
					this.createReference(node, referenceDefinition);
				}
			}
		}

		this.createReference(node, new DescriptorDefinition());

		return true;
	}

	private Boolean saveReferences(Node node) {
		NodeDefinition nodeDefinition = node.getDefinition();

		if (nodeDefinition.isForm()) {
			FormDefinition formDefinition = ((FormDefinition) nodeDefinition);
			for (MappingProperty _implements : formDefinition.getMappingList()) {
				IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(_implements.getIndex().getValue());
				this.saveReference(node, node, referenceDefinition);
			}
		} else if (nodeDefinition.isDocument()) {
			DocumentDefinition documentDefinition = ((DocumentDefinition) nodeDefinition);
			for (org.monet.metamodel.DocumentDefinitionBase.MappingProperty _implements : documentDefinition.getMappingList()) {
				IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(_implements.getIndex().getValue());
				this.saveReference(node, node, referenceDefinition);
			}
		}

		this.saveReference(node, node, new DescriptorDefinition());

		return true;
	}

	public void refreshReference(Node node, IndexDefinition referenceDefinition) {
		Node mappingNode = this.searchReferenceNodeOf(node, referenceDefinition);
		this.saveReference(node, mappingNode, referenceDefinition);
	}

	private Node searchReferenceNodeOf(Node node, IndexDefinition referenceDefinition) {
		NodeDefinition definition = node.getDefinition();

		if (definition.isForm()) {
			for (MappingProperty mapping : ((FormDefinition) definition).getMappingList()) {
				if (mapping.getIndex().getValue().equals(referenceDefinition.getName()))
					return node;
			}
		} else if (definition.isDocument()) {
			for (org.monet.metamodel.DocumentDefinitionBase.MappingProperty mapping : ((DocumentDefinition) definition).getMappingList()) {
				if (mapping.getIndex().getValue().equals(referenceDefinition.getName()))
					return node;
			}
		}

		if (definition.isContainer()) {
			for (Node child : node.getNodeList()) {
				Node schemaNode = searchReferenceNodeOf(child, referenceDefinition);
				if (schemaNode != null)
					return schemaNode;
			}
		}
		return null;
	}

	private Boolean createReference(Node node, IndexDefinition referenceDefinition) {
		Reference nodeReference = node.getReference(referenceDefinition.getCode());

		String code = referenceDefinition instanceof DescriptorDefinition ? MonetEvent.NODE_REFRESH_PROPERTIES : MonetEvent.NODE_REFRESH_MAPPING;
		MonetEvent event = new MonetEvent(code, null, node);
		event.addParameter(MonetEvent.PARAMETER_REFERENCE, nodeReference);
		this.agentNotifier.notify(event);

		this.producerReference.create(node, nodeReference);

		return true;
	}

	private Boolean saveReference(Node node, Node mappingNode, IndexDefinition referenceDefinition) {
		Reference nodeReference = mappingNode.getReference(referenceDefinition.getCode());

		String code = referenceDefinition instanceof DescriptorDefinition ? MonetEvent.NODE_REFRESH_PROPERTIES : MonetEvent.NODE_REFRESH_MAPPING;
		MonetEvent event = new MonetEvent(code, null, mappingNode);
		event.addParameter(MonetEvent.PARAMETER_REFERENCE, nodeReference);
		this.agentNotifier.notify(event);

		if (nodeReference.isDirty()) {
			this.producerReference.save(node, nodeReference);
			if (node.isComponent()) {
				Node mainNode = node.getMainNode();
				mainNode.setReference(nodeReference);
				this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.REFERENCE, node.getMainNode()));
			} else
				this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.REFERENCE, node));
		}

		return true;
	}

	private AttributeProperty findReferenceAttributeDefinition(Node node, String codeAttribute) {
		NodeDefinition definition = node.getDefinition();
		AttributeProperty result;

		if (definition.isForm()) {
			for (MappingProperty mapping : ((FormDefinition) definition).getMappingList()) {
				IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(mapping.getIndex().getValue());
				result = referenceDefinition.getAttribute(codeAttribute);
				if (result != null)
					return result;
			}
		} else if (definition.isDocument()) {
			for (org.monet.metamodel.DocumentDefinitionBase.MappingProperty mapping : ((DocumentDefinition) definition).getMappingList()) {
				IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(mapping.getIndex().getValue());
				result = referenceDefinition.getAttribute(codeAttribute);
				if (result != null)
					return result;
			}
		}

		return null;
	}

	public boolean exists(String id) {

		if (id == null)
			return false;

		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		boolean exists = false;

		parameters.put(Database.QueryFields.ID, id);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_EXISTS, parameters);
			exists = result.next();
		} catch (DataException exception) {
			throw exception;
		} catch (Exception exception) {
			throw new DataException(ErrorCode.EXIST_NODE, id, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return exists;
	}

	public Node load(String id) {
		Node node;

		if (id == null)
			return null;

		node = this.lightLoad(id, null);
		node.onLoad(node, Node.PROPERTIES);
		this.loadPermissionList(node);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_VISITED, null, node));
		this.addToLogBook(id, LogBookNodeEvent.VISITED);

		return node;
	}

	public NodeList getAncestors(Node node) {
		this.loadAncestors(node);
		return node.getAncestors();
	}

	public Node loadRevision(String id, String idRevision) {
		Node node = this.load(id);
		Revision revision = this.producerNodeRevision.load(idRevision);

		if (!revision.getIdNode().equals(id))
			return node;

		Node revisionNode = node.clone();
		revisionNode.setId(node.getId());
		revisionNode.setData(revision.getData().toString());

		AttributeList attributeList = new AttributeList();
		attributeList.deserializeFromXML(revision.getData().toString());
		revisionNode.setAttributeList(attributeList);

		return revisionNode;
	}

	public void restoreRevision(String id, String idRevision) {
		Revision revision = this.producerNodeRevision.load(idRevision);
		Node node = this.load(revision.getIdNode());
		NodeDefinition nodeDefinition = node.getDefinition();

		AttributeList attributeList = new AttributeList();
		attributeList.deserializeFromXML(revision.getData().toString());
		node.setAttributeList(attributeList);

		this.saveDataWithoutMerge(node);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.ATTRIBUTELIST, node));
		this.addToLogBook(node.getId(), LogBookNodeEvent.MODIFIED);

		if (nodeDefinition.isDocument() && nodeDefinition.isReadonly())
			node.removeLoadedAttribute(Node.SCHEMA);
		else
			this.saveSchema(node);
	}

	public Node loadAttributes(String id) {
		Node node;

		node = this.lightLoad(id, null);
		this.loadData(node);
		this.loadPermissionList(node);

		return node;
	}

	public Reference loadReference(Node node) {
		Reference reference;

		reference = this.producerReference.load(node);
		this.decryptSuperDataFromReference(node, reference);

		return reference;
	}

	public Reference loadReference(Node node, String code) {
		Reference reference;

		reference = this.producerReference.load(node, code);
		this.decryptSuperDataFromReference(node, reference);

		return reference;
	}

	public Boolean save(Node node) {
		NodeDefinition nodeDefinition;
		BusinessModel businessModel = BusinessUnit.getInstance().getBusinessModel();

		this.agentNotifier.notify(new MonetEvent(MonetEvent.BEFORE_MODIFY_NODE, ModelProperty.ATTRIBUTELIST, node));

		nodeDefinition = businessModel.getDictionary().getNodeDefinition(node.getCode());
		node.getReference().setUpdateDate(new Date());

		if (node.getPartnerContext() != null)
			this.savePartnerContext(node, node.getPartnerContext());

		if (nodeDefinition instanceof ContainerDefinition)
			this.saveContainer(node);
		else if (nodeDefinition instanceof DesktopDefinition)
			this.saveDesktop(node);
		else if (nodeDefinition instanceof FormDefinition)
			this.saveForm(node);
		else if (nodeDefinition instanceof CollectionDefinition)
			this.saveCollection(node);
		else if (nodeDefinition instanceof CatalogDefinition)
			this.saveCatalog(node);
		else if (nodeDefinition instanceof DocumentDefinition)
			this.saveDocument(node);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.ATTRIBUTELIST, node));
		this.addToLogBook(node.getId(), LogBookNodeEvent.MODIFIED);

		if (nodeDefinition.isDocument() && nodeDefinition.isReadonly())
			node.removeLoadedAttribute(Node.SCHEMA);
		else
			this.saveSchema(node);

		this.saveReferences(node);

		return true;
	}

	public Boolean notifyAndSaveSchema(Node node) {
		NodeDefinition nodeDefinition = node.getDefinition();

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.ATTRIBUTELIST, node));

		if (nodeDefinition.isDocument() && nodeDefinition.isReadonly())
			node.removeLoadedAttribute(Node.SCHEMA);
		else
			this.saveSchema(node);

		return true;
	}

	public Boolean saveAttributes(Node node) {
		return saveAttributes(node, true);
	}

	public Boolean saveAttributes(Node node, boolean merge) {
		HashMap<String, Object> parameters = new HashMap<>();
		String data;
		this.encryptSuperData(node);

		if (merge) {
			Node currentNode = this.load(node.getId());
			currentNode.merge(node);

			data = currentNode.getAttributeList().serializeToXML().toString();
			data = data.replace("'", "''");
		} else {
			data = node.getAttributeList().serializeToXML().toString();
			data = data.replace("'", "''");
		}

		parameters.put(Database.QueryFields.ID, node.getId());
		parameters.put(Database.QueryFields.DATA, data);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_SAVE_DATA, parameters);

		data = node.getAttributeList().serializeToXML().toString();
		data = data.replace("'", "''");

		parameters.clear();
		parameters.put(Database.QueryFields.ID_NODE, node.getId());
		parameters.put(Database.QueryFields.ID_USER, this.getUserId());
		parameters.put(Database.QueryFields.DATA, data);
		parameters.put(Database.QueryFields.MERGED, 1);
		parameters.put(Database.QueryFields.REVISION_DATE, this.agentDatabase.getDateAsTimestamp(new java.util.Date()));
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_REVISION_CREATE, parameters);

		this.saveSuperData(node);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.ATTRIBUTELIST, node));
		this.addToLogBook(node.getId(), LogBookNodeEvent.MODIFIED);

		node.setAttributeList(new AttributeList());
		node.removeLoadedAttribute(Node.ATTRIBUTELIST);
		node.setData("");
		node.removeLoadedAttribute(Node.DATA);

		this.saveSchema(node);

		return true;
	}

	public Boolean saveSchema(Node node) {
		HashMap<String, Object> deleteParameters = new HashMap<>();
		HashMap<String, Object> insertParameters = new HashMap<>();

		if (node.isDirty(Node.SCHEMA)) {

			DatabaseRepositoryQuery[] queries = new DatabaseRepositoryQuery[2];

			deleteParameters.put(Database.QueryFields.ID_NODE, node.getId());

			queries[0] = new DatabaseRepositoryQuery(Database.Queries.NODE_SCHEMA_DELETE, deleteParameters);

			insertParameters.put(Database.QueryFields.ID_NODE, node.getId());
			insertParameters.put(Database.QueryFields.DATA, node.getSchema());

			queries[1] = new DatabaseRepositoryQuery(Database.Queries.NODE_SCHEMA_ADD, insertParameters);

			try {
				this.agentDatabase.executeRepositoryUpdateTransaction(queries);
			} catch (SQLException e) {
				this.agentLogger.error(e);
			}

			node.setDirty(Node.SCHEMA, false);
		}

		return true;
	}

	public void saveFlags(Node node) {
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> flagsMap = node.getFlags();
		String flags = SerializerData.serialize(flagsMap);

		parameters.put(Database.QueryFields.ID, node.getId());
		parameters.put(Database.QueryFields.FLAGS, flags);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_SAVE_FLAGS, parameters);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.ATTRIBUTELIST, node));
		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_REFRESH_STATE, null, node));
	}

	public void saveNotes(Node node) {
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> notesMap = node.getNotes();
		String notes = SerializerData.serialize(notesMap);

		parameters.put(Database.QueryFields.ID, node.getId());
		parameters.put(Database.QueryFields.NOTES, notes);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_SAVE_NOTES, parameters);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.ATTRIBUTELIST, node));
	}

	public void saveParent(Node node, Node parent) {
		HashMap<String, Object> parameters = new HashMap<>();
		NodeDefinition definition = node.getDefinition();
		Dictionary dictionary = Dictionary.getInstance();

		node.setParent(parent);

		parameters.put(Database.QueryFields.ID, node.getId());
		parameters.put(Database.QueryFields.ID_PARENT, parent.getId());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_SAVE_PARENT, parameters);

		List<String> mappings = getDefinitionMappings(node, definition);
		for (String mapping : mappings) {
			IndexDefinition referenceDefinition = dictionary.getIndexDefinition(mapping);
			this.producerReference.updateParent(node, referenceDefinition);
		}

		this.producerReference.updateParent(node, new DescriptorDefinition());

		this.updateNodeAncestors(node);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.PARENT, node));
		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.REFERENCE, node));
	}

	public List<String> getDefinitionMappings(Node node, NodeDefinition definition) {
		Dictionary dictionary = Dictionary.getInstance();
		List<String> result = new ArrayList<>();

		if (definition.isContainer() ) {
			ContainerDefinition.ContainProperty containDefinition = ((ContainerDefinition)definition).getContain();

			if (containDefinition == null)
				return result;

			for (Ref contain : containDefinition.getNode()) {
				String codeNode = dictionary.getDefinitionCode(contain.getValue());
				NodeDefinition childDefinition = dictionary.getNodeDefinition(codeNode);
				Attribute attribute = node.getAttribute(codeNode);

				if (attribute == null)
					continue;

				String childId = attribute.getIndicatorValue(Indicator.VALUE);
				result.addAll(getDefinitionMappings(load(childId), childDefinition));
			}
		}
		else if (definition.isForm()) {
			for (MappingProperty mapping : ((FormDefinition) definition).getMappingList())
				result.add(mapping.getIndex().getValue());
		} else if (definition.isDocument()) {
			for (org.monet.metamodel.DocumentDefinitionBase.MappingProperty mapping : ((DocumentDefinition) definition).getMappingList())
				result.add(mapping.getIndex().getValue());
		}

		return result;
	}

	public void savePartnerContext(Node node, String partnerContext) {
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		ArrayList<String> nodesIds = this.loadNodeIds(node, Database.Queries.NODE_LOAD_DESCENDANTS);

		node.setPartnerContext(partnerContext);

		subQueries.put(Database.QueryFields.ID_NODES, "'" + LibraryArray.implode(nodesIds.toArray(new String[0]), "','") + "'");
		parameters.put(Database.QueryFields.PARTNER_CONTEXT, partnerContext);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_SAVE_PARTNER_CONTEXT, parameters, subQueries);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.PARTNERCONTEXT, node));
	}

	public Boolean makeDeletable(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		LinkedHashMap<String, String> children = new LinkedHashMap<String, String>();

		if (node.isContainer() || node.isForm() || node.isDesktop()) {
			parameters.put(Database.QueryFields.ID, node.getId());

			try {
				result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_CHILDREN, parameters);

				while (result.next())
					children.put(result.getString("id"), result.getString("code"));
			} catch (Exception exception) {
				throw new DataException(ErrorCode.LOAD_NODE, node.getId(), exception);
			} finally {
				this.agentDatabase.closeQuery(result);
			}

			for (Entry<String, String> entry : children.entrySet()) {
				Node child = this.lightLoad(entry.getKey(), entry.getValue());
				this.makeDeletable(child);
			}
		}

		node.getReference().setDeletable(true);
		this.saveReference(node, node.getReference());
		this.addToLogBook(node.getId(), LogBookNodeEvent.MODIFIED);

		return true;
	}

	public Boolean makeUndeletable(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		LinkedHashMap<String, String> children = new LinkedHashMap<String, String>();

		if (node.isContainer() || node.isForm() || node.isDesktop()) {
			parameters.put(Database.QueryFields.ID, node.getId());

			try {
				result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_CHILDREN, parameters);

				while (result.next())
					children.put(result.getString("id"), result.getString("code"));
			} catch (Exception exception) {
				throw new DataException(ErrorCode.LOAD_NODE, node.getId(), exception);
			} finally {
				this.agentDatabase.closeQuery(result);
			}

			for (Entry<String, String> entry : children.entrySet()) {
				Node child = this.lightLoad(entry.getKey(), entry.getValue());
				this.makeUndeletable(child);
			}
		}

		node.getReference().setDeletable(false);
		this.saveReference(node, node.getReference());
		this.addToLogBook(node.getId(), LogBookNodeEvent.MODIFIED);

		return true;
	}

	public Boolean makeEditable(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		LinkedHashMap<String, String> children = new LinkedHashMap<String, String>();

		if (node.isContainer() || node.isForm() || node.isDesktop()) {
			parameters.put(Database.QueryFields.ID, node.getId());

			try {
				result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_CHILDREN, parameters);

				while (result.next())
					children.put(result.getString("id"), result.getString("code"));
			} catch (Exception exception) {
				throw new DataException(ErrorCode.LOAD_NODE, node.getId(), exception);
			} finally {
				this.agentDatabase.closeQuery(result);
			}

			for (Entry<String, String> entry : children.entrySet()) {
				Node child = this.lightLoad(entry.getKey(), entry.getValue());
				this.makeEditable(child);
			}
		}

		node.getReference().setEditable(true);
		this.saveReference(node, node.getReference());
		this.addToLogBook(node.getId(), LogBookNodeEvent.MODIFIED);

		return true;
	}

	public Boolean makeUneditable(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		LinkedHashMap<String, String> children = new LinkedHashMap<String, String>();

		if (node.isContainer() || node.isForm() || node.isDesktop()) {
			parameters.put(Database.QueryFields.ID, node.getId());

			try {
				result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_CHILDREN, parameters);

				while (result.next())
					children.put(result.getString("id"), result.getString("code"));
			} catch (Exception exception) {
				throw new DataException(ErrorCode.LOAD_NODE, node.getId(), exception);
			} finally {
				this.agentDatabase.closeQuery(result);
			}

			for (Entry<String, String> entry : children.entrySet()) {
				Node child = this.lightLoad(entry.getKey(), entry.getValue());
				this.makeUneditable(child);
			}
		}

		node.getReference().setEditable(false);
		this.saveReference(node, node.getReference());
		this.addToLogBook(node.getId(), LogBookNodeEvent.MODIFIED);

		return true;
	}

	public Boolean saveReferenceAttribute(Node oNode, String codeAttribute, String sData) {
		AttributeList attributeList = new AttributeList();
		Attribute attribute = new Attribute();

		attribute.deserializeFromXML(sData, null);
		attributeList.add(attribute);

		return this.saveReferenceAttributes(oNode, attributeList);
	}

	public AttributeList loadReferenceAttributes(Node node) {
		Iterator<ReferenceAttribute<?>> iter;
		Reference nodeReference;
		AttributeList attributeList;

		nodeReference = node.getReference();
		iter = nodeReference.getAttributes().values().iterator();
		attributeList = new AttributeList();

		while (iter.hasNext()) {
			ReferenceAttribute<?> referenceAttribute = iter.next();
			Attribute attribute = new Attribute();
			Indicator indicator = new Indicator();

			attribute.setCode(referenceAttribute.getCode());
			indicator.setCode(Indicator.VALUE);
			indicator.setData(referenceAttribute.getValueAsString());
			attribute.getIndicatorList().add(indicator);

			attributeList.add(attribute);
		}

		return attributeList;
	}

	public Boolean saveReferenceAttributes(Node node, AttributeList attributeList) {
		Reference nodeReference;
		DescriptorDefinition descriptorDefinition = new DescriptorDefinition();

		try {
			nodeReference = node.getReference();

			for (Attribute attribute : attributeList) {
				AttributeProperty attributeDefinition;
				String codeAttribute = attribute.getCode();
				String value = attribute.getIndicatorValue(Indicator.VALUE);

				attributeDefinition = descriptorDefinition.getAttribute(attribute.getCode());
				if (attributeDefinition == null)
					attributeDefinition = this.findReferenceAttributeDefinition(node, attribute.getCode());

				switch (attributeDefinition.getType()) {
					case BOOLEAN:
						nodeReference.setAttributeValue(codeAttribute, Boolean.valueOf(value));
						break;
					case INTEGER:
						nodeReference.setAttributeValue(codeAttribute, new Number(Double.valueOf(attribute.getIndicatorValue(Indicator.INTERNAL)), value));
						break;
					case STRING:
					case CATEGORY:
						nodeReference.setAttributeValue(codeAttribute, value);
						break;
					case DATE:
						Date dateTime = new Date(LibraryDate.parseDate(attribute.getIndicatorValue(Indicator.INTERNAL)));
						dateTime.setFormattedValue(value);
						nodeReference.setAttributeValue(codeAttribute, dateTime);
						break;
					case PICTURE:
						Picture picture = new Picture(attribute.getIndicatorValue(Indicator.VALUE));
						picture.setFilename(value);
						nodeReference.setAttributeValue(codeAttribute, picture);
						break;
					case TERM:
						nodeReference.setAttributeValue(codeAttribute, new Term(attribute.getIndicatorValue(Indicator.CODE), value));
						break;
					case LINK:
						nodeReference.setAttributeValue(codeAttribute, new Link(attribute.getIndicatorValue(Indicator.CODE), value));
						break;
					case REAL:
						nodeReference.setAttributeValue(codeAttribute, new Number(Double.valueOf(attribute.getIndicatorValue(Indicator.INTERNAL)), value));
						break;
					case CHECK:
						break;
				}
			}

			nodeReference.setUpdateDate(new Date());

			this.producerReference.save(node, nodeReference);

			this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.REFERENCE, node));
			this.addToLogBook(node.getId(), LogBookNodeEvent.MODIFIED);

		} catch (Exception exception) {
			throw new DataException(ErrorCode.SAVE_NODE_REFERENCE, node.getId(), exception);
		}

		return true;
	}

	public Boolean saveReference(Node node, Reference reference) {

		this.producerReference.save(node, reference);
		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.REFERENCE, node));
		this.addToLogBook(node.getId(), LogBookNodeEvent.MODIFIED);

		return true;
	}

	public Boolean savePermissionList(Node node, PermissionList permissionList) {
		Iterator<String> iter = permissionList.get().keySet().iterator();
		String query;
		HashMap<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_NODE, node.getId());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_PERMISSION_LIST_DELETE, parameters);

		while (iter.hasNext()) {
			Permission permission = (Permission) permissionList.get(iter.next());

			query = Database.Queries.NODE_PERMISSION_LIST_ADD;

			parameters.clear();
			parameters.put(Database.QueryFields.ID_NODE, node.getId());
			parameters.put(Database.QueryFields.ID_USER, permission.getIdUser());
			parameters.put(Database.QueryFields.TYPE, permission.getType().toString());
			parameters.put(Database.QueryFields.BEGIN_DATE, this.agentDatabase.getDateAsTimestamp(permission.getInternalBeginDate()));
			parameters.put(Database.QueryFields.EXPIRE_DATE, this.agentDatabase.getDateAsTimestamp(permission.getInternalExpireDate()));

			this.agentDatabase.executeRepositoryUpdateQuery(query, parameters);
		}

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.PERMISSIONLIST, node));

		return true;
	}

	public NodeList filterNodeList(Node sourceNode, String[] nodes, String[] nodeTypes, java.util.Date from, java.util.Date to) {
		NodeList nodeList = new NodeList();
		NodeList sourceNodeList = sourceNode.getNodeList();

		nodeList.setIdNode(sourceNode.getId());

		if (nodes != null) {
			for (int iPos = 0; iPos < nodes.length; iPos++) {
				Node node = (Node) sourceNodeList.get(nodes[iPos]);
				if (node == null)
					continue;
				if (this.isNodeExportable(node, nodeTypes, from, to))
					nodeList.add(node);
			}
		} else {
			for (Node node : sourceNodeList) {
				if (this.isNodeExportable(node, nodeTypes, from, to))
					nodeList.add(node);
			}
		}

		return nodeList;
	}

	public String exportNode(Node sourceNode, String codeFormat) {
		Pattern pattern = Pattern.compile(Kernel.getInstance().getConfiguration().getValue(Configuration.INDICATOR_NODE_LINK));
		String result, nodeLinks;
		Matcher matcher;
		String nodeId;
		int tagEndNode;
		HashMap<String, String> includedNodes = new HashMap<String, String>();

		result = sourceNode.serializeToXML(true).toString();
		matcher = pattern.matcher(result);

		nodeLinks = "";
		while (matcher.find()) {
			nodeId = matcher.group(1);
			if ((nodeId != null) && (!nodeId.equals(Strings.EMPTY)) && (!includedNodes.containsKey(nodeId))) {
				Node node = this.load(nodeId);
				nodeLinks += this.exportNode(node, codeFormat);
				includedNodes.put(nodeId, nodeId);
			}
		}
		result = matcher.replaceAll(Strings.EMPTY);

		tagEndNode = result.lastIndexOf("</node>");
		if (tagEndNode != -1) {
			result = result.substring(0, tagEndNode);
			result += (!nodeLinks.equals(Strings.EMPTY)) ? "<nodelinks>" + nodeLinks + "</nodelinks>" : "";
			result += "</node>";
		}

		return result;
	}

	public Boolean share(Node node, String[] users, String description, java.util.Date expireDate) {
		ProducerTask producerTask;
		PermissionList permissionList = node.getPermissionList();
		Integer pos;
		java.util.Date startDate = new java.util.Date();

		producerTask = (ProducerTask) this.producersFactory.get(Producers.TASK);

		for (pos = 0; pos < users.length; pos++) {
			Permission nodePermission = new Permission();
			nodePermission.setIdObject(node.getId());
			nodePermission.setIdUser(users[pos]);
			nodePermission.setType(PermissionType.READ);
			nodePermission.setBeginDate(new java.util.Date());
			nodePermission.setExpireDate(expireDate);
			permissionList.add(nodePermission);

			Task task = producerTask.create(TaskCode.SHARE, null, false);
			task.setTarget(node);
			task.setLabel(node.getLabel());
			task.setDescription(description);
			task.setStartDate(startDate);
			task.setStartSuggestedDate(startDate);
			task.setEndSuggestedDate(expireDate);
			producerTask.save(task);
		}

		this.savePermissionList(node, permissionList);

		return true;
	}

	public Node create(String code, String parentId, String partnerContext) {
		HashMap<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_PARENT, parentId);
		parameters.put(Database.QueryFields.ID_OWNER, null);
		parameters.put(Database.QueryFields.ID_PROTOTYPE, null);
		parameters.put(Database.QueryFields.CODE, code);
		parameters.put(Database.QueryFields.DEFINITION_TYPE, "none");
		parameters.put(Database.QueryFields.PARTNER_CONTEXT, partnerContext);
		parameters.put(Database.QueryFields.ORDERING, 0);
		parameters.put(Database.QueryFields.DATA, "");
		parameters.put(Database.QueryFields.VALID, 0);

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.NODE_CREATE, parameters);

		Node node = new Node();
		node.setId(id);
		node.setCode(code);
		node.linkLoadListener(this);
		node.setNodeLink(NodeProvider.getInstance());
		node.setReferenceLink(ReferenceProvider.getInstance());
		node.addLoadedAttribute(Node.PROPERTIES);

		this.registerNodeInCache(node);

		return node;
	}

	public void create(Node node) {
		PermissionList permissionList;
		Permission permission;
		HashMap<String, Object> saveParameters = new HashMap<>(), permissionParameters = new HashMap<>(), schemaParameters = new HashMap<>();
		HashSet<DatabaseRepositoryQuery> queries = new HashSet<DatabaseRepositoryQuery>();

		this.registerNodeInCache(node);

		permissionList = new PermissionList();
		permission = new Permission();
		permission.setIdObject(node.getId());
		permission.setIdUser(node.getOwnerId());
		permission.setType(PermissionType.READ_WRITE_CREATE_DELETE);
		permission.setExpireDate(null);
		permissionList.add(permission);
		node.setPermissionList(permissionList);

		this.enrich(node, true);

		String data = node.getAttributeList().serializeToXML().toString();

		saveParameters.put(Database.QueryFields.ID, node.getId());
		saveParameters.put(Database.QueryFields.ID_PARENT, node.getParentId());
		saveParameters.put(Database.QueryFields.ID_OWNER, node.getOwnerId());
		saveParameters.put(Database.QueryFields.ID_PROTOTYPE, null);
		saveParameters.put(Database.QueryFields.CODE, node.getCode());
		saveParameters.put(Database.QueryFields.DEFINITION_TYPE, node.getType().toString());
		saveParameters.put(Database.QueryFields.ORDERING, String.valueOf(node.getOrder()));
		saveParameters.put(Database.QueryFields.DATA, data);
		saveParameters.put(Database.QueryFields.VALID, node.isValid());
		queries.add(new DatabaseRepositoryQuery(Database.Queries.NODE_SAVE, saveParameters));

		permissionParameters.put(Database.QueryFields.ID_NODE, node.getId());
		permissionParameters.put(Database.QueryFields.ID_USER, node.getOwnerId());
		permissionParameters.put(Database.QueryFields.TYPE, PermissionType.READ_WRITE);
		permissionParameters.put(Database.QueryFields.BEGIN_DATE, this.agentDatabase.getDateAsTimestamp(new java.util.Date()));
		permissionParameters.put(Database.QueryFields.EXPIRE_DATE, null);
		queries.add(new DatabaseRepositoryQuery(Database.Queries.NODE_PERMISSION_LIST_ADD, permissionParameters));

		schemaParameters.put(Database.QueryFields.ID_NODE, node.getId());
		schemaParameters.put(Database.QueryFields.DATA, node.getSchema());
		queries.add(new DatabaseRepositoryQuery(Database.Queries.NODE_SCHEMA_ADD, schemaParameters));

		this.agentDatabase.executeRepositoryQueries(queries.toArray(new DatabaseRepositoryQuery[0]));

		this.createReferences(node);

		//if (node.getParent() == null || !node.getParent().isContainer())
		this.updateNodeAncestors(node);

		this.addToLogBook(node.getId(), LogBookNodeEvent.CREATED);
	}

	public Boolean remove(Node node) {

		if (node.isLinked())
			throw new DataException(ErrorCode.REMOVE_NODE, ErrorCode.COULD_NODE_REMOVE_LINKED_NODE);

		HashMap<String, Object> trashParameters = new HashMap<>(), linkParameters = new HashMap<>();
		HashMap<String, String> trashSubqueries = new HashMap<String, String>();
		java.util.Date date = new java.util.Date();
		Node parent = node.getParent();
		ArrayList<DatabaseRepositoryQuery> queries = new ArrayList<DatabaseRepositoryQuery>();
		ArrayList<String> nodesIds = this.loadNodeIds(node, Database.Queries.NODE_TRASH_SUBQUERY);

		trashSubqueries.put(Database.QueryFields.ID_NODES, "'" + LibraryArray.implode(nodesIds.toArray(new String[0]), "','") + "'");
		trashParameters.put(Database.QueryFields.DELETE_DATE, this.agentDatabase.getDateAsTimestamp(new java.util.Date()));
		queries.add(new DatabaseRepositoryQuery(Database.Queries.NODE_MOVE_TO_TRASH, trashParameters, trashSubqueries));

		linkParameters.put(Database.QueryFields.ID_SOURCE, node.getMainNode().getId());
		linkParameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.NODE);
		linkParameters.put(Database.QueryFields.DELETE_DATE, this.agentDatabase.getDateAsTimestamp(date));
		queries.add(new DatabaseRepositoryQuery(Database.Queries.LINK_SAVE, linkParameters));

		try {
			this.agentDatabase.executeRepositoryUpdateTransaction(queries.toArray(new DatabaseRepositoryQuery[0]));
		} catch (SQLException e) {
			this.agentLogger.error(e);
		}

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MOVED_TO_TRASH, null, node));

		if (parent != null && parent.isCollection()) {
			parent.getReference().setUpdateDate(new Date(new java.util.Date()));
			this.producerReference.save(parent, parent.getReference());

			MonetEvent event = new MonetEvent(MonetEvent.NODE_REMOVED_FROM_COLLECTION, null, node);
			event.addParameter(MonetEvent.PARAMETER_COLLECTION, parent);
			this.agentNotifier.notify(event);
		}

		this.agentLogger.info("NODE REMOVED! Id: " + node.getId());
		Thread.dumpStack();
		this.addToLogBook(node.getId(), LogBookNodeEvent.DELETED);

		return true;
	}

	public Boolean recoverFromTrash(Node node) {
		HashMap<String, Object> linkParameters = new HashMap<>();
		HashMap<String, String> trashSubqueries = new HashMap<String, String>();
		Reference nodeReference = node.getReference();
		ArrayList<DatabaseRepositoryQuery> queries = new ArrayList<DatabaseRepositoryQuery>();
		ArrayList<String> nodesIds = this.loadNodeIds(node, Database.Queries.NODE_TRASH_SUBQUERY);

		trashSubqueries.put(Database.QueryFields.ID_NODES, "'" + LibraryArray.implode(nodesIds.toArray(new String[0]), "','") + "'");
		queries.add(new DatabaseRepositoryQuery(Database.Queries.NODE_RECOVER_FROM_TRASH, null, trashSubqueries));

		linkParameters.put(Database.QueryFields.ID_SOURCE, node.getMainNode().getId());
		linkParameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.NODE);
		linkParameters.put(Database.QueryFields.DELETE_DATE, null);
		queries.add(new DatabaseRepositoryQuery(Database.Queries.LINK_SAVE, linkParameters));

		try {
			this.agentDatabase.executeRepositoryUpdateTransaction(queries.toArray(new DatabaseRepositoryQuery[0]));
		} catch (SQLException e) {
			this.agentLogger.error(e);
		}

		nodeReference.setAttributeValue(DescriptorDefinition.ATTRIBUTE_DELETE_DATE, null);
		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_RECOVERED_FROM_TRASH, null, node));

		return true;
	}

	public Boolean removeFromTrash(Node node) {
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, String> subQueries = new HashMap<>();

		try {
			if (node.isLinked())
				throw new DataException(ErrorCode.REMOVE_NODE, ErrorCode.COULD_NODE_REMOVE_LINKED_NODE);

			this.producerReference.remove(node);

			parameters.put(Database.QueryFields.ID_NODE, node.getId());
			this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_PERMISSION_LIST_DELETE, parameters);
			this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_SCHEMA_DELETE, parameters);

			parameters.clear();
			parameters.put(Database.QueryFields.ID_SOURCE, node.getMainNode().getId());
			parameters.put(Database.QueryFields.ID_SOURCE_COMPONENT, node.getId());
			parameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.NODE);
			this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.LINK_DELETE, parameters);

			parameters.clear();
			parameters.put(Database.QueryFields.ID_SOURCE, node.getId());
			this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.ATTACHMENT_DELETE, parameters);

			parameters.clear();
			parameters.put(Database.QueryFields.ID, node.getId());

			ResultSet resultSet = null;
			ArrayList<String> nodesIds = new ArrayList<String>();

			try {
				resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_REMOVE_LOAD_ANCESTORS, parameters);

				while (resultSet.next())
					nodesIds.add(resultSet.getString("id_node"));
			} catch (Exception oException) {
				throw new DataException(ErrorCode.REMOVE_NODE, node.getId(), oException);
			} finally {
				this.agentDatabase.closeQuery(resultSet);
			}

			subQueries.put(Database.QueryFields.ID_NODES, "'" + LibraryArray.implode(nodesIds.toArray(new String[0]), "','") + "'");
			this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_REMOVE, parameters, subQueries);

		} catch (Exception oException) {
			throw new DataException(ErrorCode.REMOVE_NODE, ErrorCode.GENERATE_ID, oException);
		}

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_REMOVED, null, node));

		Node parentNode = this.load(node.getParentId());
		if (parentNode != null && parentNode.isContainer()) {
			MonetEvent event = new MonetEvent(MonetEvent.NODE_REMOVED, null, node);
			event.addParameter(MonetEvent.PARAMETER_COLLECTION, parentNode);
			this.agentNotifier.notify(event);
		}

		return true;
	}

	private void updateNodeAncestors(Node node) {
		String id = node.getId();
		Node parent = node.getParent();
		String parentId = parent != null ? parent.getId() : null;

		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.ID_NODE, Integer.parseInt(id));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_ANCESTORS_CLEAN, parameters);

		if (parentId != null) {
			parameters.put(Database.QueryFields.ID_PARENT, Integer.parseInt(parentId));
			this.insertAncestors(parameters);
		}

		if (node.isDesktop()) {
			ProducerNodeList producerNodeList = this.producersFactory.get(Producers.NODELIST);

			processDesktopViews(id, parameters, producerNodeList, node.getDefinition().getViewDefinitionList());
		}

		if (node.isContainer() && ((ContainerDefinition) node.getDefinition()).getContain() != null) {
			ContainerDefinition containerDefinition = (ContainerDefinition) node.getDefinition();
			for (Ref contain : containerDefinition.getContain().getNode()) {
				Attribute attribute;
				String codeNode = Dictionary.getInstance().getDefinitionCode(contain.getValue());
				attribute = node.getAttribute(codeNode);
				if (attribute == null)
					continue;
				String nodeId = attribute.getIndicatorValue(Indicator.VALUE);
				this.updateNodeAncestors(this.load(nodeId));
			}
		}
	}

	private void processDesktopViews(String id, HashMap<String, Object> parameters, ProducerNodeList producerNodeList, List<NodeViewProperty> viewsDefinitions) {
		for (NodeViewProperty viewDefinition : viewsDefinitions) {
			DesktopDefinition.ViewProperty desktopViewDefinition = (DesktopDefinition.ViewProperty) viewDefinition;
			if (desktopViewDefinition.getShow().getLink() != null) {
				for (Ref reference : desktopViewDefinition.getShow().getLink()) {
					Definition definition = Dictionary.getInstance().getDefinition(reference.getValue());
					if (definition instanceof NodeDefinition && !((NodeDefinition) definition).isSingleton()) {
						String childId = producerNodeList.locateId(definition.getCode());

						parameters.clear();
						parameters.put(Database.QueryFields.ID_NODE, Integer.parseInt(childId));
						parameters.put(Database.QueryFields.ID_PARENT, Integer.parseInt(id));
						this.insertAncestors(parameters);
					}
				}
			}
		}
	}

	private void insertAncestors(HashMap<String, Object> parameters) {
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_ANCESTORS_INSERT, parameters);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_ANCESTORS_INSERT_FROM_PARENT, parameters);
	}

	public Boolean makePublic(Node node) {
		HashMap<String, Object> parameters = new HashMap<>();

		node.setOwner(null);

		parameters.put(Database.QueryFields.ID, node.getId());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_MAKE_PUBLIC, parameters);

		parameters.clear();
		parameters.put(Database.QueryFields.ID_NODE, node.getId());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_REFERENCE_MAKE_PUBLIC, parameters);

		this.registerNodeInCache(node);
		this.unregisterChildNodes(node);

		return true;
	}

	public Boolean makePrivate(Node node) {
		HashMap<String, Object> parameters = new HashMap<>();
		String idOwner = ownerIdOfAccount();

		node.setOwnerId(idOwner);

		parameters.put(Database.QueryFields.ID, node.getId());
		parameters.put(Database.QueryFields.ID_OWNER, idOwner);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_MAKE_PRIVATE, parameters);

		parameters.clear();
		parameters.put(Database.QueryFields.ID_NODE, node.getId());
		parameters.put(Database.QueryFields.ID_OWNER, idOwner);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_REFERENCE_MAKE_PRIVATE, parameters);

		this.registerNodeInCache(node);
		this.unregisterChildNodes(node);

		return true;
	}

	public Boolean makePrototype(Node node) {
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, String> queries = new HashMap<String, String>();

		node.getReference().setPrototype(true);

		parameters.put(Database.QueryFields.ID_NODE, node.getId());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_MAKE_PROTOTYPE, parameters, queries);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.REFERENCE, node));

		this.registerNodeInCache(node);
		this.unregisterChildNodes(node);

		return true;
	}

	public Boolean makePrototyped(Node node, Node prototype) {
		HashMap<String, Object> parameters = new HashMap<>();
		String prototypeId = prototype.getId();

		if (prototypeId == null)
			return true;

		node.setPrototypeId(prototypeId);

		parameters.put(Database.QueryFields.ID_SOURCE, node.getMainNode().getId());
		parameters.put(Database.QueryFields.ID_SOURCE_COMPONENT, node.getId());
		parameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.NODE);
		parameters.put(Database.QueryFields.ID_TARGET, prototypeId);
		parameters.put(Database.QueryFields.TYPE_TARGET, LinkType.NODE);
		parameters.put(Database.QueryFields.DATA, "none");
		parameters.put(Database.QueryFields.DELETE_DATE, null);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.LINK_ADD, parameters);

		parameters.clear();
		parameters.put(Database.QueryFields.ID, node.getId());
		parameters.put(Database.QueryFields.ID_PROTOTYPE, prototypeId);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_SAVE_PROTOTYPE, parameters);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_MODIFIED, ModelProperty.ATTRIBUTELIST, node));

		this.registerNodeInCache(node);
		this.unregisterChildNodes(node);

		return true;
	}

	private void loadLocation(Node node) {
		node.setLocation(node.isGeoReferenced() ? this.producerLocation.load(node, null) : null);
	}

	public boolean checkTableSchemaLoaded() {
		int count = 0;
		MonetResultSet resultSet = null;

		try {

			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.CHECK_NODE_SCHEMA_COUNT);

			if (resultSet.next()) {
				count = resultSet.getInt("count");
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.DATABASE_SELECT_QUERY, Database.Queries.CHECK_NODE_SCHEMA_COUNT, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return count > 0;
	}

	public Boolean clearLinks(Node node) {
		HashMap<String, Object> parameters = new HashMap<>();
		Node mainNode = node.getMainNode();

		parameters.put(Database.QueryFields.ID, mainNode.getId());
		parameters.put(Database.QueryFields.TYPE, LinkType.NODE);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.LINK_CLEAR, parameters);

		return true;
	}

	public Node lightLoad(String id, String code) {
		Node node = this.cache.get(id);
		if (node == null) {
			node = new Node();
			node.setId(id);
			node.setCode(code);
			node.linkLoadListener(this);
			node.setNodeLink(NodeProvider.getInstance());
			node.setReferenceLink(ReferenceProvider.getInstance());

			try {
				this.cache.put(id, node);
			} catch (Exception e) {
				this.agentLogger.error(e);
			}
		}
		return node;
	}

	private void registerNodeInCache(Node node) {
		try {
			this.cache.remove(node.getId());
			this.cache.put(node.getId(), node);
		} catch (Exception e) {
			this.agentLogger.error(e);
		}
	}

	private void unregisterNodeFromCache(String id) {
		try {
			this.cache.remove(id);
		} catch (Exception e) {
			this.agentLogger.error(e);
		}
	}

	private void unregisterChildNodes(Node node) {
		if (!node.isContainer())
			return;

		Dictionary dictionary = Dictionary.getInstance();
		for (Ref componentDefinition : ((ContainerDefinition) node.getDefinition()).getContain().getNode()) {
			String childDefinitionCode = dictionary.getDefinitionCode(componentDefinition.getValue());
			String childId = node.getIndicatorValue("[" + childDefinitionCode + "].value");
			this.unregisterNodeFromCache(childId);
		}
	}

	private void loadState(Node node) {
		this.agentNotifier.notify(new MonetEvent(MonetEvent.NODE_REFRESH_STATE, null, node));
	}

	private String getAttachmentCodes(String nodeId, NodeDataRequest dataRequest) {
		Node node = this.load(nodeId);
		String codeView = dataRequest.getCodeView();
		FormDefinition definition = (FormDefinition) node.getDefinition();
		FormViewProperty viewDefinition = (FormViewProperty) definition.getNodeView(codeView);
		StringBuilder result = new StringBuilder();

		if (viewDefinition.getShow().getAttachments() != null) {
			for (Ref field : viewDefinition.getShow().getAttachments().getFieldFile()) {
				result.append("'");
				result.append(definition.getField(field.getValue()).getCode());
				result.append("',");
			}
			for (FieldNodeProperty fieldNodeDefinition : viewDefinition.getShow().getAttachments().getFieldNodeList()) {
				result.append("'");
				result.append(definition.getField(fieldNodeDefinition.getField().getValue()).getCode());
				result.append("',");
			}
		}

		result.deleteCharAt(result.length() - 1);

		return result.toString();
	}

	public List<Attachment> loadAttachmentItems(String nodeId, NodeDataRequest dataRequest) {
		ResultSet resultSet = null;
		LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, String> subQueries = new LinkedHashMap<String, String>();
		ArrayList<Attachment> attachmentList;

		parameters.put(Database.QueryFields.ID_SOURCE, nodeId);
		subQueries.put(Database.QueryFields.CODES, this.getAttachmentCodes(nodeId, dataRequest));

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ATTACHMENT_LOAD_ITEMS, parameters, subQueries);
			attachmentList = new ArrayList<Attachment>();

			while (resultSet.next()) {
				Attachment attachment = new Attachment();

				attachment.setSourceId(nodeId);
				attachment.setTargetId(resultSet.getString("id_target"));
				attachment.setTargetType(resultSet.getString("type_target"));
				attachment.setTargetCode(resultSet.getString("code_target"));
				attachment.setData(resultSet.getString("data"));
				attachment.setDeleteDate(resultSet.getTimestamp("delete_date"));

				attachmentList.add(attachment);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, dataRequest.getCode(), exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return attachmentList;
	}

	public int loadAttachmentItemsCount(String nodeId, NodeDataRequest dataRequest) {
		ResultSet resultSet = null;
		LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		Integer count = 0;

		parameters.put(Database.QueryFields.ID_SOURCE, nodeId);
		subQueries.put(Database.QueryFields.CODES, this.getAttachmentCodes(nodeId, dataRequest));

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ATTACHMENT_LOAD_ITEMS_COUNT, parameters, subQueries);
			resultSet.next();
			count = resultSet.getInt("counter");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_ATTACHMENT_ITEMS, nodeId, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return count;
	}

	public void reset() {
		try {
			this.cache.reset();
		} catch (Exception e) {
			agentLogger.error(e);
		}
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
		Node node = (Node) eventObject.getSource();

		if (attribute.equals(Node.ATTRIBUTELIST))
			this.loadAttributeList(node);
		else if (attribute.equals(Node.PROPERTIES))
			this.loadProperties(node);
		else if (attribute.equals(Node.SUPERDATA))
			this.loadSuperData(node);
		else if (attribute.equals(Node.PARENT))
			this.loadParent(node);
		else if (attribute.equals(Node.OWNER))
			this.loadOwner(node);
		else if (attribute.equals(Node.ANCESTORS))
			this.loadAncestors(node);
		else if (attribute.equals(Node.PARTNER_CONTEXT))
			this.loadPartnerContext(node);
		else if (attribute.equals(Node.PROTOTYPES))
			this.loadPrototypes(node);
		else if (attribute.equals(Node.NODE_LINK))
			node.setNodeLink(NodeProvider.getInstance());
		else if (attribute.equals(Node.REFERENCE_LINK))
			node.setReferenceLink(ReferenceProvider.getInstance());
		else if (attribute.equals(Node.DATA))
			this.loadData(node);
		else if (attribute.equals(Node.NOTES))
			this.loadNotes(node);
		else if (attribute.equals(Node.FLAGS))
			this.loadFlags(node);
		else if (attribute.equals(Node.PERMISSIONLIST))
			this.loadPermissionList(node);
		else if (attribute.equals(Node.NODELIST))
			this.loadNodeList(node);
		else if (attribute.equals(Node.CLIENTNODELIST))
			this.loadClientNodeList(node);
		else if (attribute.equals(Node.LINKS_COUNTER))
			this.loadLinksCounter(node);
		else if (attribute.equals(Node.LINKS_FROM_TASKS_COUNTER))
			this.loadLinksFromTaskCounter(node);
		else if (attribute.equals(Node.ATTACHMENTS_COUNTER))
			this.loadAttachmentsCounter(node);
		else if (attribute.equals(Node.LINKED_TASKS))
			this.loadLinkedTasks(node);
		else if (attribute.equals(Node.SCHEMA))
			this.loadSchema(node);
		else if (attribute.equals(Node.LOCATION))
			this.loadLocation(node);
		else if (attribute.equals(Node.STATE))
			this.loadState(node);

	}

}