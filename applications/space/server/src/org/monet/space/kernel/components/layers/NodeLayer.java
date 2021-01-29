package org.monet.space.kernel.components.layers;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import org.monet.metamodel.CheckFieldProperty;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.FilterProperty;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.map.LocationList;

import java.io.Reader;
import java.util.*;

public interface NodeLayer extends Layer {

	boolean existsNode(String id);

	void addNodeContainerChild(Node node, Node childNode);

	Node addPrototype(String code, Node parent);

	Node addNode(String code);

	Node addNode(String code, User owner);

	Node addNode(String code, Node parent);

	Node addNodeInteroperable(String code, String documentReferenced);

	void copyNode(Node node, Node parent, String label, String description);

	void deleteNode(String id);

	void deleteNode(Node node);

	void deleteAndRemoveNodeFromTrash(String id);

	void deleteAndRemoveNodesFromTrash(List<Node> nodes);

	void emptyTrash();

	NodeList filterNodeList(Node node, String nodes, String sNodeTypes, Date fromDate, Date toDate);

	String exportNode(Node node, String codeFormat);

	String exportNode(Node node);

	String exportNode(String idNode, String codeFormat);

	String exportNode(String idNode);

	void importNode(String importerKey, Node scope, Reader source, long fileSize, ProgressCallback callback);

	AttributeList loadAttributes(Node node);

	Map<String, Attribute> loadNodesAttribute(String[] nodesIds, String codeAttribute);

	Node loadNode(String id);

	Node loadNode(String id, boolean onlyFromMemory);

	NodeList loadNodeAncestors(Node node);

	Node loadNodeRevision(String id, String idRevision);

	void restoreNodeRevision(String id, String idRevision);

	Revision loadRevision(String id);

	void restoreNode(Revision revision);

	Node loadNodeFromData(String id, String data);

	AttributeList getNodeFormCheckFieldOptions(Node node, String sourceId, CheckFieldProperty declaration, String from);

	void setNodeDocumentSignaturesCount(Node node, String signature, int count);

	void clearNodeDocumentSignatureUsersRestrictions(Node node, String signature, int signaturePos);

	void addNodeDocumentSignatureUserRestriction(Node node, String signature, int signaturePos, User user);

	void addNodeDocumentSignature(Node node, String code, int position, String label, String reason, String location, String contact, Date date);

	void deleteNodeDocumentSignature(Node node, String code, int position);

	void stampNodeDocumentSignature(Node node, String signId, String signature, User user);

	Reference loadNodeReference(Node node);

	Reference loadNodeReference(Node node, String code);

	Map<String, Node> requestNodeListItems(String idNode, NodeDataRequest dataRequest);

	int requestNodeListItemsCount(String idNode, NodeDataRequest dataRequest);

	LocationList requestNodeListItemsLocations(String idNode, NodeDataRequest dataRequest);

	int requestNodeListItemsLocationsCount(String idNode, NodeDataRequest dataRequest);

	Map<String, Node> requestNodeSetItems(String idNode, String contentType, NodeDataRequest dataRequest);

	Integer requestNodeSetItemsCount(String idNode, String contentType, NodeDataRequest dataRequest);

	NodeList search(Node node, SearchRequest searchRequest);

	NodeList search(String idNode, SearchRequest searchRequest);

	NodeItemList searchLinkNodeItems(NodeDataRequest dataRequest);

	NodeItemList searchById(NodeDataRequest dataRequest);

	int countLinkNodeItemsLocations(NodeDataRequest dataRequest);

	LocationList searchLinkNodeItemsLocations(NodeDataRequest dataRequest);

	String locateNodeId(String code);

	Node locateNode(String code);

	NodeList loadNodes(String idNode);

	NodeList loadNodes();

	List<String> loadNodeIds(String code);

	NodeList loadNodesFromTrash(DataRequest dataRequest);

	void recoverNodeFromTrash(String id);

	void recoverNodesFromTrash(String data);

	void removeNodeFromTrash(String id);

	void saveNode(Node node);

	void saveNode(Node node, String data);

	void saveNodeAttributes(Node node, String data);

	void saveNodeAttribute(Node node, String data);

	void saveNodesAttribute(String[] nodesIds, Attribute attribute);

	void saveNodeFlags(Node node);

	void saveNodeNotes(Node node);

	void saveNodeSchema(Node node);

	void saveNodePartnerContext(Node node, String businessUnitContext);

	void resetNodeForm(Node node);

	void makeEditable(Node node);

	void makeUneditable(Node node);

	void makeDeletable(Node node);

	void makeUndeletable(Node node);

	String loadNodeReferenceAttributes(Node node);

	ReferenceList getReferences(String codeReference, String filter, String orderBy, Map<String, Object> parameters, int startPos, int limit);

	int getReferencesCount(String code, String filter, Map<String, Object> parameters);

	List<String> getReferencesNodeId(String codeReference);

	List<String> loadReferenceAttributeValues(String ownerId, String codeReference, String codeAttribute, List<String> filterNodes, List<FilterProperty> filtersDefinition);

	Map<String, Integer> loadReferenceAttributeValuesCount(String ownerId, String codeReference, String codeAttribute, List<String> filterNodes, List<FilterProperty> filtersDefinitions);

	void createReferenceTable(IndexDefinition definition);

	boolean existsReferenceTable(IndexDefinition definition);

	void refreshReferenceTable(IndexDefinition definition);

	void deleteReferenceTable(IndexDefinition definition);

	void refreshReference(String idNode, IndexDefinition definition);

	void saveNodeReferenceAttributes(Node node, String data);

	void saveNodeReference(Node node, Reference nodeReference);

	void saveNodePermissions(Node node, PermissionList permissionList);

	Map<String, String> getNodesSorting(String idNode);

	void setNodesSorting(String idNode, String field, String mode);

	void shareNode(Node node, String idUsers, String description, Date expireDate);

	LogSubscriberList loadNodeSubscribers(Integer type);

	void addNodeSubscriber(ServerConfiguration configuration, Integer eventType);

	void removeNodeSubscriber(ServerConfiguration configuration, Integer eventType);

	LogBookNode loadNodeBook();

	BookEntryList requestNodeLogBookEntries(DataRequest dataRequest);

	int requestNodeLogBookEntriesCount(String idNode);

	BookEntryList searchNodeLogBookEntries(Integer eventType, Date fromDate, Date toDate);

	void makeNodePublic(Node node);

	void makeNodePrivate(Node node);

	void makeNodePrototype(Node node);

	void makeNodePrototyped(Node node, Node prototype);

	void resetYearSequences();

	SequenceValue createSequenceValue(String code);

	Integer requestRevisionListItemsCount(String idNode, DataRequest dataRequest);

	Map<String, Revision> requestRevisionListItems(String idNode, DataRequest dataRequest);

	void setParentNode(Node node, Node parent);

	void updateNodeLocation(Node node, Geometry geometry);

	void cleanNodeLocation(Node node);

	LocationList loadLocationsInNode(Node node, Polygon boundingBox, String indexCode);

	LocationList loadLocationsInNode(Node node, String withName, Polygon boundingBox, String indexCode);

	List<Attachment> requestNodeAttachmentItems(String nodeId, NodeDataRequest dataRequest);

	int requestNodeAttachmentItemsCount(String nodeId, NodeDataRequest dataRequest);

}
