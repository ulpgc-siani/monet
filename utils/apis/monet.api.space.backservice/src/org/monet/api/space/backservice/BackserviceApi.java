package org.monet.api.space.backservice;

import org.monet.api.space.backservice.impl.model.*;
import org.monet.api.space.backservice.impl.model.workmap.Process;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;

public interface BackserviceApi {

	boolean addDatastoreCubeFact(String datastore, String cube, CubeFact fact);

	boolean addDatastoreCubeFacts(String datastore, String cube, CubeFactList factList);

	boolean addDatastoreDimensionComponent(String datastore, String dimension, DimensionComponent dimensionComponent);

	boolean addNodeFlag(String nodeId, String name, String value);

	boolean addNodeNote(String nodeId, String name, String value);

	boolean addSourceTerm(String sourceId, String code, String label, int type, String parentCode, LinkedHashSet<String> tags);

	boolean addTaskFact(String taskId, String title, String subTitle, String userId, MonetLink[] links);

	boolean addTaskFlag(String taskId, String name, String value);

	boolean addTaskShortCut(String taskId, String name, String nodeId);

	boolean consolidateNode(String nodeId);

	Account createAccount(String code, String fullName, String email, String[] roles);

	Node createNode(String type, String parentId);

	Task createTask(String type);

	boolean deleteNodeFlag(String nodeId, String name);

	boolean deleteNodeNote(String nodeId, String name);

	boolean deleteTaskFlag(String taskId, String name);

	boolean deleteTaskShortCut(String taskId, String name);

	File downloadDistribution();

	boolean emptyTrash();

	byte[] executeExporter(String exporter, String scope);

	boolean executeNodeCommand(String idNode, String command);

	boolean existsNode(String idNode);

	ExportItem exportNode(String id);

	ExportList exportNodes(String[] ids);

	NodeList getNodeAncestors(String id);

	InputStream getNodeDocument(String id);

	String getNodeDocumentContentType(String id);

	InputStream getNodeFile(String id, String name);

	Map<String, String> getNodeFlags(String id);

	Location getNodeLocation(String id);

	Map<String, String> getNodeNotes(String id);

	Reference getNodeReference(String nodeId, String name);

	int getNodeReferencesCount(String codeReference, String filter, Map<String, Object> parameters);

	ReferenceList getNodeReferences(String codeReference, String filter, String orderBy, Map<String, Object> parameters, int start, int limit);

	String getNodeSchema(String id);

	TaskList getNodeTasks(String id);

	TermList getSourceTerms(String id, String parent);

	Term getSourceParentTerm(String id, String code);

	TaskList getTasks(String inbox, String folder, String condition, String owner, int start, int limit);

	TaskList getTaskTray(String folder, String condition, String owner, int start, int limit);

	TaskList getTaskBoard(String folder, String condition, int start, int limit);

	TaskFactList getTaskFacts(String id);

	Map<String, String> getTaskFlags(String id);

	Process getTaskProcess(String id);

	Map<String, String> getTaskShortCuts(String id);

	User getUserLinkedToNode(String idNode);

	Node getUserNode(String codeUser, int depth);

	TaskList getUserTasks(String codeUser);

	boolean hasPermissions(String username);

	boolean importNode(String importer, String scopeNodeId, String data);

	User loadUser(String code);

	Node locateNode(String code);

	Node locateNode(String code, int depth);

	Source loadSource(String id);

	Source locateSource(String code, String url);

	boolean makeNodeDeleteable(String id);

	boolean makeNodeEditable(String id);

	boolean makeNodePrivate(String id);

	boolean makeNodePublic(String id);

	boolean makeNodeUnDeleteable(String id);

	boolean makeNodeUnEditable(String id);

	Datastore openDatastore(String name);

	Node openNode(String id);

	NodeList openNodes(String[] ids);

	Task openTask(String id);

	Node recoverNode(String id);

	boolean removeNode(String id);

	boolean removeTask(String id);

	boolean resetNodeForm(String id);

	Task resumeTask(String id);

	boolean runTask(String id);

	boolean saveNode(Node node);

	boolean saveNodeDocument(String id, InputStream data, String contentType);

	boolean saveNodeFile(String id, String name, InputStream data);

	boolean saveNodePicture(String id, String name, InputStream data);

	boolean saveNodePartnerContext(String idNode, String context);

	boolean saveNodeParent(String nodeId, String parentId);

	boolean saveNodeReference(String idNode, Reference reference);

	boolean saveNodesAttribute(String[] idNodes, Attribute attribute);

	boolean saveTask(Task task);

	boolean saveUser(User user);

	LogHistory searchEvent(String type, Date from, Date to);

	NodeList searchNodes(String id, Date from, Date to);

	boolean subscribe(String type, String data);

	boolean unLockTask(String id, String place, String stop);

	boolean gotoPlaceInTask(String id, String place, String history);

}
