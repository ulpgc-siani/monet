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

package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.monet.metamodel.JobDefinition;
import org.monet.metamodel.PlaceProperty;
import org.monet.metamodel.TaskDefinition;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.constants.TaskState;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.model.map.Location;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Task<T extends TaskDefinition> extends Entity<T> implements ISecurable {
	private User owner;
	private String idOwner;
	private User sender;
	private String idSender;
	private Node target;
	private String idTarget;
	private String partnerContext;
	private String role;
	private String roleLabel;
	private String label;
	private String description;
	private String state;
	private boolean urgent;
	private String classificator;
	private boolean isInitializer;
	private boolean isBackground;
	private int newMessagesCount;
	private Date createDate;
	private Date updateDate;
	private Date startDate;
	private Date startSuggestedDate;
	private Date endDate;
	private Date endSuggestedDate;
	private String comments;
	private String content;
	private StringBuffer data;
	private ProcessBehavior process;
	private Set<String> grantedUsers;
	private List<String> enrolmentsList;
	private Map<String, Node> shortcutInstancesMap;
	private TaskOrderList taskOrderList;
	private Location location;

	public static final class Parameter {
		public static final String TYPE = "type";
		public static final String STATE = "state";
		public static final String INBOX = "box";
		public static final String SITUATION = "situation";
		public static final String ROLE = "role";
		public static final String URGENT = "urgent";
		public static final String BACKGROUND = "background";
		public static final String OWNER = "owner";
		public static final String SENDER = "sender";
	}

	public static final class Situation {
		public static final String ALIVE = "alive";
		public static final String ACTIVE = "active";
		public static final String PENDING = "pending";
		public static final String FINISHED = "finished";
	}

    public static final class Inbox {
        public static final String TASKBOARD = "taskboard";
        public static final String TASKTRAY = "tasktray";
    }

    public static final String PROPERTIES = "Properties";
	public static final String PROCESS = "Process";
	public static final String DATA = "Data";
	public static final String OWNER = "Owner";
	public static final String SENDER = "Sender";
	public static final String TARGET = "Target";
	public static final String ENROLMENTS = "Enrolments";
	public static final String SHORTCUTS = "Shortcuts";
	public static final String ORDERLIST = "OrderList";

	public Task() {
		super();
		this.code = "none";
		this.owner = null;
		this.idOwner = null;
		this.sender = null;
		this.idSender = null;
		this.target = null;
		this.idTarget = null;
		this.partnerContext = null;
		this.role = "";
		this.roleLabel = "";
		this.label = "";
		this.description = "";
		this.state = TaskState.NEW;
		this.urgent = false;
		this.classificator = null;
		this.isInitializer = false;
		this.isBackground = false;
		this.newMessagesCount = 0;
		this.createDate = new Date();
		this.updateDate = null;
		this.startDate = null;
		this.startSuggestedDate = null;
		this.endDate = null;
		this.endSuggestedDate = null;
		this.content = "";
		this.data = new StringBuffer();
		this.process = null;
		this.grantedUsers = new HashSet<String>();
		this.enrolmentsList = new ArrayList<String>();
		this.shortcutInstancesMap = new HashMap<String, Node>();
		this.taskOrderList = new TaskOrderList();
	}

	public String getCode() {
		onLoad(this, Task.PROPERTIES);
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public DefinitionType getType() {
		return this.getDefinition().getType();
	}

	public User getOwner() {
		if (this.idOwner == null)
			return null;

		onLoad(this, Task.OWNER);

		if (!this.owner.getId().equals(this.idOwner)) {
			this.removeLoadedAttribute(Task.OWNER);
			onLoad(this, Task.OWNER);
		}

		return this.owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
		this.setOwnerId(owner != null ? owner.getId() : null);
		this.addLoadedAttribute(Task.OWNER);
	}

	public String getOwnerId() {
		onLoad(this, Task.PROPERTIES);
		return this.idOwner;
	}

	public void setOwnerId(String idOwner) {
		this.idOwner = idOwner;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public User getSender() {
		if (this.idSender == null)
			return null;

		onLoad(this, Task.SENDER);

		if (!this.sender.getId().equals(this.idSender)) {
			this.removeLoadedAttribute(Task.SENDER);
			onLoad(this, Task.SENDER);
		}

		return this.sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
		this.setSenderId(sender != null ? sender.getId() : null);
		this.addLoadedAttribute(Task.SENDER);
	}

	public String getSenderId() {
		onLoad(this, Task.PROPERTIES);
		return this.idSender;
	}

	public void setSenderId(String idSender) {
		this.idSender = idSender;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public Node getTarget() {
		onLoad(this, Task.PROPERTIES);
		onLoad(this, Task.TARGET);
		return this.target;
	}

	public void setTarget(Node target) {
		this.target = target;
		this.idTarget = target.getId();
		this.addLoadedAttribute(Task.TARGET);
	}

	public String getTargetId() {
		onLoad(this, Task.PROPERTIES);
		return this.idTarget;
	}

	public void setTargetId(String targetId) {
		this.idTarget = targetId;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getPartnerContext() {
		onLoad(this, Task.PROPERTIES);
		return this.partnerContext;
	}

	public void setPartnerContext(String partnerContext) {
		this.partnerContext = partnerContext;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getRole() {
		onLoad(this, Task.PROPERTIES);
		return this.role;
	}

	public void setRole(String role) {
		Dictionary dictionary = Dictionary.getInstance();

		this.role = role;

		if (this.role != null && dictionary.existsRoleDefinition(this.role))
			this.roleLabel = Language.getInstance().getModelResource(dictionary.getRoleDefinition(this.role).getLabel());
		else
			this.roleLabel = "";

		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getLabel() {
		onLoad(this, Task.PROPERTIES);
		return this.label;
	}

	public String getInstanceLabel() {
		return this.getLabel();
	}

	public void setLabel(String label) {
		this.label = label;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getDescription() {
		onLoad(this, Task.PROPERTIES);
		ProcessBehavior process = this.getProcess();
		if (process == null)
			return this.description;
		return this.getInternalState();
	}

	public void setDescription(String description) {
		this.description = description;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public Location getLocation() {
		onLoad(this, Task.PROPERTIES);
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getComments() {
		onLoad(this, Task.PROPERTIES);
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getState() {
		onLoad(this, Task.PROPERTIES);
		return this.state;
	}

	public String getInternalState() {
		onLoad(this, Task.PROPERTIES);
		PlaceProperty placeProperty = this.process.getCurrentPlace();
		Object internalState = placeProperty != null && placeProperty.getPlaceActionProperty() != null ? placeProperty.getPlaceActionProperty().getLabel() : "";
		return Language.getInstance().getModelResource(internalState);
	}

	public boolean isNew() {
		return (this.getState().equals(TaskState.NEW));
	}

	public boolean isPending() {
		return (this.getState().equals(TaskState.PENDING));
	}

	public boolean isWaiting() {
		return (this.getState().equals(TaskState.WAITING));
	}

	public boolean isExpired() {
		return (this.getState().equals(TaskState.EXPIRED));
	}

	public boolean isFinished() {
		return (this.getState().equals(TaskState.FINISHED));
	}

	public boolean isAborted() {
		return (this.getState().equals(TaskState.ABORTED));
	}

	public boolean isFailure() {
		return (this.getState().equals(TaskState.FAILURE));
	}

	public void setState(String state) {
		onLoad(this, Task.PROPERTIES);
		this.state = state;
	}

	public boolean isUrgent() {
		onLoad(this, Task.PROPERTIES);
		return this.urgent;
	}

	public void setUrgent(boolean value) {
		this.urgent = value;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getClassificator() {
		onLoad(this, Task.PROPERTIES);
		return this.classificator;
	}

	public void setClassificator(String value) {
		this.classificator = value;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public boolean isInitializer() {
		onLoad(this, Task.PROPERTIES);
		return this.isInitializer;
	}

	public void setIsInitializer(boolean value) {
		this.isInitializer = value;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public boolean isBackground() {
		onLoad(this, Task.PROPERTIES);
		return this.isBackground;
	}

	public void setIsBackground(boolean value) {
		this.isBackground = value;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public int getNewMessagesCount() {
		onLoad(this, Task.PROPERTIES);
		return this.newMessagesCount;
	}

	public void setNewMessagesCount(int count) {
		this.newMessagesCount = count;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getCreateDate(String format, String codeLanguage, TimeZone zone) {
		onLoad(this, Task.PROPERTIES);
		if (this.createDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.createDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getCreateDate(String format) {
		return this.getCreateDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getCreateDate() {
		return this.getCreateDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalCreateDate() {
		onLoad(this, Task.PROPERTIES);
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getUpdateDate(String format, String codeLanguage, TimeZone zone) {
		onLoad(this, Task.PROPERTIES);
		if (this.updateDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.updateDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getUpdateDate(String format) {
		return this.getUpdateDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getUpdateDate() {
		return this.getUpdateDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalUpdateDate() {
		onLoad(this, Task.PROPERTIES);
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getStartDate(String format, String codeLanguage, TimeZone zone) {
		onLoad(this, Task.PROPERTIES);
		if (this.startDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.startDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getStartDate(String format) {
		return this.getStartDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getStartDate() {
		return this.getStartDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalStartDate() {
		onLoad(this, Task.PROPERTIES);
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getStartSuggestedDate(String format, String codeLanguage, TimeZone zone) {
		onLoad(this, Task.PROPERTIES);
		if (this.startSuggestedDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.startSuggestedDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getStartSuggestedDate(String format) {
		return this.getStartSuggestedDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getStartSuggestedDate() {
		return this.getStartSuggestedDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalStartSuggestedDate() {
		onLoad(this, Task.PROPERTIES);
		return this.startSuggestedDate;
	}

	public void setStartSuggestedDate(Date startSuggestedDate) {
		this.startSuggestedDate = startSuggestedDate;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getEndDate(String format, String codeLanguage, TimeZone zone) {
		onLoad(this, Task.PROPERTIES);
		if (this.endDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.endDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getEndDate(String format) {
		return this.getEndDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getEndDate() {
		return this.getEndDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalEndDate() {
		onLoad(this, Task.PROPERTIES);
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getEndSuggestedDate(String format, String codeLanguage, TimeZone zone) {
		onLoad(this, Task.PROPERTIES);
		if (this.endSuggestedDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.endSuggestedDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getEndSuggestedDate(String format) {
		return this.getEndSuggestedDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getEndSuggestedDate() {
		return this.getEndSuggestedDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalEndSuggestedDate() {
		onLoad(this, Task.PROPERTIES);
		return this.endSuggestedDate;
	}

	public void setEndSuggestedDate(Date endSuggestedDate) {
		this.endSuggestedDate = endSuggestedDate;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public String getContent() {
		onLoad(this, Task.PROPERTIES);
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
		this.addLoadedAttribute(Task.PROPERTIES);
	}

	public StringBuffer getData() {
		onLoad(this, Task.DATA);
		return this.data;
	}

	public void setData(StringBuffer oData) {
		this.data = oData;
		this.addLoadedAttribute(Task.DATA);
	}

	public ProcessBehavior getProcess() {
		if (this.getDefinition() instanceof JobDefinition)
			return null;

		onLoad(this, Task.PROCESS);
		return this.process;
	}

	public void setProcess(ProcessBehavior process) {
		this.process = process;
		this.addLoadedAttribute(Task.PROCESS);
	}

	public Set<String> getGrantedUsers() {
		return this.grantedUsers;
	}

	public void setGrantedUsers(HashSet<String> grantedUsers) {
		this.grantedUsers = grantedUsers;
	}

	public boolean isPublic() {
		return this.getDefinition().isPublic();
	}

	public boolean isLinked() {
		return false;
	}

	public boolean isDeletable() {
		return !isLocked();
	}

	public boolean isLocked() {
		onLoad(this, Task.PROCESS);
		return this.process.isLocked();
	}

	public PermissionList getPermissionList() {
		return new PermissionList();
	}

	public Set<String> getRoles() {
		Set<String> result = new HashSet<String>();
		result.add(UserRole.HOLDER);
		return result;
	}

	public Set<String> getRules() {
		return new HashSet<String>();
	}

	public T getDefinition() {
		Dictionary dictionary = BusinessUnit.getInstance().getBusinessModel().getDictionary();
		return (T)dictionary.getTaskDefinition(this.getCode());
	}

	public boolean isActivity() {
		return this.getDefinition().isActivity();
	}

	public boolean isJob() {
		return this.getDefinition().isJob();
	}

	public boolean isSensor() {
		return this.getDefinition().isSensor();
	}

	public boolean isService() {
		return this.getDefinition().isService();
	}

	public List<String> getEnrolmentsIdUsers() {
		onLoad(this, Task.ENROLMENTS);
		return this.enrolmentsList;
	}

	public void setEnrolmentsIdUsers(List<String> enrolmentsList) {
		this.enrolmentsList = enrolmentsList;
		this.addLoadedAttribute(Task.ENROLMENTS);
	}

	public String getFlag(String name) {
		return getProcess().getFlag(name);
	}

	public boolean isFlagActive(String name) {
		return getProcess().isFlagActive(name);
	}

	public void setFlag(String name, String value) {
		getProcess().setFlag(name, value);
	}

	public void removeFlag(String name) {
		getProcess().removeFlag(name);
	}

	public Map<String, Node> getShortcutsInstances() {
		onLoad(this, Task.SHORTCUTS);
		return this.shortcutInstancesMap;
	}

	public Node getShortcutInstance(String name) {
		onLoad(this, Task.SHORTCUTS);
		if (!this.shortcutInstancesMap.containsKey(name))
			return null;
		return this.shortcutInstancesMap.get(name);
	}

	public void removeShortcutInstance(String name) {
		onLoad(this, Task.SHORTCUTS);
		if (!this.shortcutInstancesMap.containsKey(name))
			return;
		this.shortcutInstancesMap.remove(name);
	}

	public void clearShortcutInstances() {
		this.shortcutInstancesMap.clear();
		this.addLoadedAttribute(Task.SHORTCUTS);
	}

	public void addShortcutInstance(String name, Node instance) {
		onLoad(this, Task.SHORTCUTS);
		this.shortcutInstancesMap.put(name, instance);
	}

	public TaskOrderList getOrderList() {
		onLoad(this, Task.ORDERLIST);
		return this.taskOrderList;
	}

	public void setOrderList(TaskOrderList taskOrderList) {
		this.taskOrderList = taskOrderList;
		this.addLoadedAttribute(Task.ORDERLIST);
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		String content = this.getContent();
		Boolean partialLoading = this.isPartialLoading();

		if (partialLoading)
			this.disablePartialLoading();

		result.put("id", this.getId());
		result.put("code", this.getCode());
		result.put("idOwner", this.idOwner);
		if (this.getOwner() != null)
			result.put("ownerFullname", this.getOwner().getInfo().getFullname());
		result.put("idSender", this.idSender);
		if (this.getSender() != null)
			result.put("senderFullname", this.getSender().getInfo().getFullname());
		result.put("idTarget", this.idTarget);
		result.put("hasComments", this.comments != null && !this.comments.isEmpty());
		result.put("role", this.role);
		result.put("roleLabel", this.roleLabel);
		result.put("definitionLabel", this.getDefinition().getLabel());
		result.put("label", this.getLabel());
		result.put("description", this.getDescription());
		result.put("state", this.getState());
		result.put("aborted", this.isAborted());
		result.put("flag", this.getState());
		result.put("urgent", this.urgent);
		result.put("classificator", this.classificator);
		result.put("newMessagesCount", this.newMessagesCount);
		result.put("isInitializer", this.isInitializer);
		result.put("background", this.isBackground);
		result.put("isPending", this.isPending());
		result.put("isWaiting", this.isWaiting());
		result.put("createDate", this.getCreateDate());
		result.put("updateDate", this.getUpdateDate());
		result.put("startDate", this.getStartDate());
		result.put("startSuggestedDate", this.getStartSuggestedDate());
		result.put("endDate", this.getEndDate());
		result.put("endSuggestedDate", this.getEndSuggestedDate());
		result.put("content", content);
		result.put("type", this.getDefinition().getType().toString());

		if (partialLoading)
			this.enablePartialLoading();

		return result;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");
		String idTarget = this.getTargetId();

		serializer.startTag("", "task");

		serializer.attribute("", "id", this.id);
		serializer.attribute("", "type", this.getDefinition().getType().toString());
		serializer.attribute("", "code", this.getDefinition().getCode());
		serializer.attribute("", "name", this.getDefinition().getName());
		serializer.attribute("", "idTarget", idTarget != null ? idTarget : "");
		serializer.attribute("", "state", this.getState());

		if (this.createDate != null) serializer.attribute("", "createDate", dateFormat.format(this.createDate));
		if (this.updateDate != null) serializer.attribute("", "updateDate", dateFormat.format(this.updateDate));
		if (this.startDate != null) serializer.attribute("", "startDate", dateFormat.format(this.startDate));
		if (this.startSuggestedDate != null) serializer.attribute("", "startSuggestedDate", dateFormat.format(this.startSuggestedDate));
		if (this.endDate != null) serializer.attribute("", "endDate", dateFormat.format(this.endDate));
		if (this.endSuggestedDate != null) serializer.attribute("", "endSuggestedDate", dateFormat.format(this.endSuggestedDate));

		serializer.startTag("", "label");
		serializer.text(this.label);
		serializer.endTag("", "label");

		if (this.description != null) {
			serializer.startTag("", "description");
			serializer.text(this.description);
			serializer.endTag("", "description");
		}

		serializer.endTag("", "task");
	}

	public void deserializeFromXML(Element task) {
		if (task.getAttribute("id") != null)
			this.id = task.getAttributeValue("id");

		if (task.getAttribute("code") != null)
			this.code = task.getAttributeValue("code");

		if (task.getAttribute("idTarget") != null)
			this.idTarget = task.getAttributeValue("idTarget");

		if (task.getChild("label") != null)
			this.label = task.getChild("label").getText();

		if (task.getChild("description") != null)
			this.description = task.getChild("description").getText();
	}

	public void deserializeFromXML(String content) {
		SAXBuilder builder = new SAXBuilder();
		StringReader reader;
		org.jdom.Document document;
		Element task;

		if (content.equals(Strings.EMPTY)) return;

		content = content.trim();
		reader = new StringReader(content);

		try {
			document = builder.build(reader);
			task = document.getRootElement();
			this.deserializeFromXML(task);
		} catch (JDOMException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_TASK_FROM_XML, content, exception);
		} catch (IOException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_TASK_FROM_XML, content, exception);
		}
	}

}
