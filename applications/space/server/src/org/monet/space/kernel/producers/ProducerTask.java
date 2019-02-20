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

import com.google.inject.Injector;
import com.vividsolutions.jts.geom.Geometry;
import org.monet.metamodel.ProcessDefinition;
import org.monet.metamodel.TaskDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.guice.InjectorFactory;
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.machines.ttm.Engine;
import org.monet.space.kernel.machines.ttm.model.Snapshot;
import org.monet.space.kernel.machines.ttm.model.Timer;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.map.Location;
import org.monet.space.kernel.utils.PersisterHelper;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProducerTask extends Producer {

	public ProducerTask() {
		super();
	}

	public void updateDefinition(TaskDefinition taskDefinition) {
		Dictionary dictionary = Dictionary.getInstance();
		Map<String, Object> parameters = new HashMap<>();

		for (String language : dictionary.getLanguages()) {
			String text = taskDefinition.getLabelString(language);

			parameters.put(Database.QueryFields.CODE, taskDefinition.getCode());
			parameters.put(Database.QueryFields.LANGUAGE, language);
			parameters.put(Database.QueryFields.LABEL, text);

			this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_DEFINITION_INSERT, parameters);
		}
	}

	public void cleanDefinitions() {
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_DEFINITION_CLEAN);
	}

	public void fillProperties(Task task, ResultSet result) throws Exception {
		User owner = new User();
		owner.setId(result.getString("id_owner"));
		owner.getInfo().setFullname(result.getString("owner_fullname"));

		User sender = new User();
		sender.setId(result.getString("id_sender"));
		sender.getInfo().setFullname(result.getString("sender_fullname"));

		task.setId(result.getString("id"));
		task.setCode(result.getString("code"));
		task.setOwnerId(result.getString("id_owner"));
		task.setOwner(owner);
		task.setSenderId(result.getString("id_sender"));
		task.setSender(sender);
		task.setTargetId(result.getString("id_target"));
		task.setPartnerContext(result.getString("partner_context"));
		task.setRole(result.getString("code_role"));
		task.setLabel(result.getString("label"));
		task.setDescription(result.getString("description"));
		task.setState(result.getString("state"));
		task.setUrgent(result.getBoolean("urgent"));
		task.setClassificator(result.getString("classificator"));
		task.setIsInitializer(result.getBoolean("initializer"));
		task.setIsBackground(result.getBoolean("background"));
		task.setNewMessagesCount(result.getInt("new_messages"));
		task.setCreateDate(result.getTimestamp("create_date"));
		task.setUpdateDate(result.getTimestamp("update_date"));
		task.setStartDate(result.getTimestamp("start_date"));
		task.setStartSuggestedDate(result.getTimestamp("suggested_start_date"));
		task.setEndDate(result.getTimestamp("end_date"));
		task.setEndSuggestedDate(result.getTimestamp("suggested_end_date"));
		task.setComments(result.getString("comments"));

		Geometry geometry = this.agentDatabase.getGeometryColumn(result, "geometry");
		if (geometry != null) {
			Location location = new Location();
			location.setGeometry(geometry);
			location.setDescription(result.getString("geometry_label"));
			task.setLocation(location);
		}
	}

	public String getTaskIdIfUnique(String taskCode, String classificator) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();
		String query = null;

		parameters.put(Database.QueryFields.CODE, taskCode);

		if (classificator != null) {
			parameters.put(Database.QueryFields.CLASSIFICATOR, classificator);
			query = Database.Queries.TASK_FIND_WITH_CLASSIFICATOR;
		} else {
			query = Database.Queries.TASK_FIND;
		}

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(query, parameters);
			if (result.next()) {
				String id = result.getString("id");
				if (!result.next())
					return id;
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return null;
	}

	public Task getCurrentInitializerTask() {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();
		String id;

		try {
			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_FIND_CURRENT_INITIALIZER, parameters);
			if (!result.next())
				return null;

			id = result.getString("id");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			agentDatabase.closeQuery(result);
		}

		return this.load(id);
	}

	public Task load(String id) {
		Task task;

		task = new Task();
		task.setId(id);
		task.linkLoadListener(this);
		task.enablePartialLoading();

		return task;
	}

	public void save(Task task) {
		saveProperties(task);
		saveLinks(task);
		saveState(task);
	}

	public void saveUrgency(Task task) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID, task.getId());
		parameters.put(Database.QueryFields.URGENT, task.isUrgent());

		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_URGENCY, parameters);
	}

	public void saveState(Task task) {
		Map<String, Object> parameters = new HashMap<>();
        String id = task.getId();
		String state = task.getState();

		parameters.put(Database.QueryFields.ID, id);
		parameters.put(Database.QueryFields.STATE, state);
		parameters.put(Database.QueryFields.DESCRIPTION, task.getDescription());
		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_STATE, parameters);

		if (state.equals(TaskState.FINISHED) || state.equals(TaskState.ABORTED))
			finishTask(task);

		agentNotifier.notify(new MonetEvent(MonetEvent.TASK_STATE_UPDATED, null, task));
	}

	public void saveOwner(Task task, User owner, String reason) {
		Map<String, Object> parameters = new HashMap<>();
		User sender = getAccount().getUser();
		String ownerFullName = task.getOwner() != null ? task.getOwner().getInfo().getFullname() : "";
		String senderFullName = sender.getInfo().getFullname();
		String message;

		task.setOwner(owner);
		task.setSender(owner != null ? sender : null);

		parameters.put(Database.QueryFields.ID, task.getId());
		parameters.put(Database.QueryFields.ID_OWNER, owner != null ? owner.getId() : null);
		parameters.put(Database.QueryFields.OWNER_FULLNAME, owner != null ? owner.getInfo().getFullname() : null);
		parameters.put(Database.QueryFields.ID_SENDER, sender != null ? sender.getId() : null);
		parameters.put(Database.QueryFields.SENDER_FULLNAME, sender != null ? sender.getInfo().getFullname() : null);

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_OWNER, parameters);

		if (task.isJob()) {
			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(Database.QueryFields.JOBS, task.getId());

			parameters.clear();
			parameters.put(Database.QueryFields.CREATE_DATE, agentDatabase.getDateAsTimestamp(new Date()));

			agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_JOB_REFRESH_TIME_MARK, parameters, subQueries);
		}

		if (owner != null) {
			MonetEvent event = new MonetEvent(MonetEvent.TASK_ASSIGNED, null, task);
			event.addParameter(MonetEvent.PARAMETER_USER, owner);
			event.addParameter(MonetEvent.PARAMETER_REASON, reason);
			agentNotifier.notify(event);
			message = MessageCode.TASK_ASSIGNED;
			ownerFullName = owner.getInfo().getFullname();
		} else {
			agentNotifier.notify(new MonetEvent(MonetEvent.TASK_UNASSIGNED, null, task));
			message = MessageCode.TASK_UNASSIGNED;
		}

		TaskFact fact = new TaskFact();
		fact.setTaskId(task.getId());
		fact.setTitle(String.format(Language.getInstance().getMessage(message), ownerFullName, senderFullName));
		fact.setSubTitle(reason);
		fact.setCreateDate(new Date());
		addFact(fact);
	}

	public void saveProcess(Task task) {
		Map<String, Object> parameters = new HashMap<>();
		String data = task.getData().toString();

		parameters.put(Database.QueryFields.ID, task.getId());
		parameters.put(Database.QueryFields.DATA, data);
		parameters.put(Database.QueryFields.STATE, task.getState());
		parameters.put(Database.QueryFields.UPDATE_DATE, agentDatabase.getDateAsTimestamp(new Date()));
		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_PROCESS, parameters);
	}

	public Task create(String code, Node input, boolean initializer) {
		Task task = new Task();
		task.setCode(code);
		return this.create(task, input, initializer);
	}

	public Task create(Task task, Node input, boolean initializer) {
		Map<String, Object> parameters = new HashMap<>();
		Injector injector = InjectorFactory.getInstance();
		Engine engine;
		Dictionary dictionary = this.getDictionary();
		TaskDefinition definition = dictionary.getTaskDefinition(task.getCode());
		Ref roleRef = definition.getRole();

		task.setCreateDate(new Date());
		task.setLabel((input != null) ? input.getLabel() : definition.getLabelString());
		task.setDescription(definition.getLabelString());

		parameters.put(Database.QueryFields.ID_OWNER, null);
		parameters.put(Database.QueryFields.OWNER_FULLNAME, null);
		parameters.put(Database.QueryFields.ID_SENDER, null);
		parameters.put(Database.QueryFields.SENDER_FULLNAME, null);
		parameters.put(Database.QueryFields.ID_TARGET, null);
		parameters.put(Database.QueryFields.PARTNER_CONTEXT, task.getPartnerContext());
		parameters.put(Database.QueryFields.ROLE, roleRef != null ? dictionary.getDefinitionCode(roleRef.getValue()) : null);
		parameters.put(Database.QueryFields.LABEL, task.getLabel());
		parameters.put(Database.QueryFields.DESCRIPTION, LibraryString.cleanSpecialChars(task.getDescription().replace("\n", " ")));
		parameters.put(Database.QueryFields.SHORTCUTS, null);
		parameters.put(Database.QueryFields.CODE, task.getCode());
		parameters.put(Database.QueryFields.STATE, task.getState());
		parameters.put(Database.QueryFields.URGENT, task.isUrgent());
		parameters.put(Database.QueryFields.CLASSIFICATOR, task.getClassificator());
		parameters.put(Database.QueryFields.INITIALIZER, initializer);
		parameters.put(Database.QueryFields.BACKGROUND, definition.isBackground());
		parameters.put(Database.QueryFields.NEW_MESSAGES, task.getNewMessagesCount());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalCreateDate()));
		parameters.put(Database.QueryFields.AVAILABLE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
		parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
		parameters.put(Database.QueryFields.START_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalStartDate()));
		parameters.put(Database.QueryFields.SUGGESTED_START_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalStartSuggestedDate()));
		parameters.put(Database.QueryFields.END_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalEndDate()));
		parameters.put(Database.QueryFields.SUGGESTED_END_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalEndSuggestedDate()));
		parameters.put(Database.QueryFields.COMMENTS, task.getComments());
		parameters.put(Database.QueryFields.DATA, null);

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.TASK_CREATE, parameters);
		task.setId(id);

		if (definition instanceof ProcessDefinition) {
			engine = injector.getInstance(Engine.class);
			task.setProcess(engine.buildProcess(id, (ProcessDefinition) definition));
		}

		this.agentNotifier.notify(new MonetEvent(MonetEvent.TASK_CREATED, null, task));

		task.linkLoadListener(this);

		return task;
	}

	public void remove(String id) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID, id);
		parameters.put(Database.QueryFields.END_DATE, agentDatabase.getDateAsTimestamp(new Date()));
		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_END_DATE, parameters);

		agentNotifier.notify(new MonetEvent(MonetEvent.TASK_MOVED_TO_TRASH, null, id));
	}

	public void deleteTask(String id) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID, id);
		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_REMOVE, parameters);
	}

	public void abortTask(String id) {
		Map<String, Object> parameters = new HashMap<>();
		Task task = this.load(id);

		if (task.isJob()) {
			parameters.put(Database.QueryFields.ID, id);
			parameters.put(Database.QueryFields.END_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
			this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_ABORT_DATE, parameters);
		} else
			task.getProcess().abort();
	}

	public void recoverFromTrash(String id) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID, id);
		parameters.put(Database.QueryFields.END_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_END_DATE, parameters);
		agentNotifier.notify(new MonetEvent(MonetEvent.TASK_RECOVERED_FROM_TRASH, null, id));
	}

	public void addFact(Fact fact) {
		ProducerFactTask producerFactBookTask = producersFactory.get(Producers.FACTBOOKTASK);
		producerFactBookTask.addEntry(fact);
	}

	public List<Timer> loadTimers() {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<>();
		List<Timer> timers = new ArrayList<>();

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_TIMER_LOAD, parameters);

			while (result.next()) {
				Timer timer = new Timer();
				timer.setId(result.getString("id"));
				timer.setTaskId(result.getString("id_task"));
				timer.setTag(result.getString("tag"));
				timer.setCreated(result.getTimestamp("create_date"));
				timer.setDelay(result.getLong("delay"));
				timers.add(timer);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return timers;
	}

	public void createTimer(Timer timer) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.ID_TASK, timer.getTaskId());
		parameters.put(Database.QueryFields.TAG, timer.getTag());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(timer.getCreated()));
		parameters.put(Database.QueryFields.DELAY, timer.getDelay());
		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.TASK_TIMER_CREATE, parameters);
		timer.setId(id);
	}

	public void deleteTimer(String id) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.ID, id);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_TIMER_DELETE, parameters);
	}

	public void run(Task task) {
		Injector injector = InjectorFactory.getInstance();
		Engine engine;

		engine = injector.getInstance(Engine.class);
		engine.getProcess(task.getId()).resume();
	}

	public Snapshot loadLastSnapshot(String taskId) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();
		Snapshot snapshot = null;

		parameters.put(Database.QueryFields.ID_TASK, taskId);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_PROCESS_SNAPSHOT_LOAD_LAST, parameters);
			if (result.next()) {
				snapshot = PersisterHelper.load(result.getString("data"), Snapshot.class);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return snapshot;
	}

	public Snapshot loadAndDeleteLastSnapshot(String taskId) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();
		Snapshot snapshot = null;

		parameters.put(Database.QueryFields.ID_TASK, taskId);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_PROCESS_SNAPSHOT_LOAD_LAST, parameters);
			if (result.next()) {
				String id = result.getString("id");
				snapshot = PersisterHelper.load(result.getString("data"), Snapshot.class);

				this.agentDatabase.closeQuery(result);
				parameters.clear();
				parameters.put(Database.QueryFields.ID, id);
				this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_PROCESS_SNAPSHOT_DELETE, parameters);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return snapshot;
	}

	public boolean hasSnapshots(String taskId) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.ID_TASK, taskId);

		try {
			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_PROCESS_SNAPSHOT_COUNT, parameters);
			if (result.next()) {
				return result.getInt("counter") > 0;
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			agentDatabase.closeQuery(result);
		}

		return false;
	}

	public void saveSnapshot(String taskId, Snapshot snapshot) {
		Map<String, Object> parameters = new HashMap<>();
		StringWriter writer = new StringWriter();
		try {
			PersisterHelper.save(writer, snapshot);
		} catch (Exception e) {
			agentLogger.error(e);
			throw new DataException(ErrorCode.SAVE_SNAPSHOT, null);
		}

		parameters.put(Database.QueryFields.ID_TASK, taskId);
		parameters.put(Database.QueryFields.CREATE_DATE, agentDatabase.getDateAsTimestamp(new Date()));
		parameters.put(Database.QueryFields.DATA, writer.toString());
		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_PROCESS_SNAPSHOT_CREATE, parameters);
	}

	public Object newObject() {
		return new Task();
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
        if (attribute.equals(Task.PROPERTIES))
			loadProperties((Task) eventObject.getSource());
		else if (attribute.equals(Task.PROCESS))
			loadProcess((Task) eventObject.getSource());
		else if (attribute.equals(Task.DATA))
			loadData((Task) eventObject.getSource());
		else if (attribute.equals(Task.TARGET))
			loadTarget((Task) eventObject.getSource());
		else if (attribute.equals(Task.OWNER))
			loadOwner((Task) eventObject.getSource());
		else if (attribute.equals(Task.SENDER))
			loadSender((Task) eventObject.getSource());
		else if (attribute.equals(Task.ENROLMENTS))
			loadEnrolments((Task) eventObject.getSource());
		else if (attribute.equals(Task.SHORTCUTS))
			loadShortcuts((Task) eventObject.getSource());
		else if (attribute.equals(Task.ORDERLIST))
			loadOrderList((Task) eventObject.getSource());
	}

	private void loadProperties(Task task) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_TASK, task.getId());

		try {
			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LOAD, parameters);

			if (!result.next())
				throw new Exception(String.format("Task '%s' not found", task.getId()));

			fillProperties(task, result);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			agentDatabase.closeQuery(result);
		}
	}

	private void loadTarget(Task task) {
		ProducerNode producerNode = producersFactory.get(Producers.NODE);
		String idTarget = task.getTargetId();
		if ((idTarget == null) || (idTarget.equals("-1")))
			return;
		task.setTarget(producerNode.load(idTarget));
	}

	private void loadOwner(Task task) {
		ProducerFederation producerFederation = producersFactory.get(Producers.FEDERATION);
		String idOwner = task.getOwnerId();
		if ((idOwner == null) || (idOwner.equals("-1")))
			return;
		task.setOwner(producerFederation.loadUser(idOwner));
	}

	private void loadSender(Task task) {
		ProducerFederation producerFederation = producersFactory.get(Producers.FEDERATION);
		String idSender = task.getSenderId();
		if ((idSender == null) || (idSender.equals("-1")))
			return;
		task.setSender(producerFederation.loadUser(idSender));
	}

	private void loadEnrolments(Task task) {
		ProducerRoleList producerRoleList = producersFactory.get(Producers.ROLELIST);
		String codeRole = task.getRole();
		List<String> enrolmentsList = producerRoleList.loadUsersIds(codeRole);
		Account account = this.getAccount();
		RoleList roleList = account.getRoleList();

		for (Role role : roleList) {
			if (role.getCode().equals(codeRole)) {
				enrolmentsList.add(account.getUser().getId());
				break;
			}
		}

		task.setEnrolmentsIdUsers(enrolmentsList);
	}

	private void loadShortcuts(Task task) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_TASK, task.getId());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LOAD_SHORTCUTS, parameters);

			if (!result.next())
				throw new Exception(String.format("Task '%s' not found", task.getId()));

			String shortcutInstances = result.getString("shortcuts");

			if (shortcutInstances == null)
				return;

			String[] instancesArray = shortcutInstances.split(",");
			ProducerNode producerNode = producersFactory.get(Producers.NODE);

			this.agentDatabase.closeQuery(result);

			task.clearShortcutInstances();
            for (String instances : instancesArray) {
                String[] instanceArray = instances.split("=");
                if (instanceArray.length < 2)
                    continue;
                Node node = producerNode.load(instanceArray[1]);
                task.addShortcutInstance(instanceArray[0], node);
            }

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	private void loadOrderList(Task task) {
		ProducerTaskOrderList producerTaskOrderList;
		TaskOrderList taskOrderList;

		producerTaskOrderList = producersFactory.get(Producers.TASKORDERLIST);
		taskOrderList = (TaskOrderList) producerTaskOrderList.newObject();
		task.setOrderList(taskOrderList);
		taskOrderList.setIdTask(task.getId());
	}

	private void loadData(Task task) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();
		StringBuffer dataBuilder = new StringBuffer();
		String data;

		parameters.put(Database.QueryFields.ID, task.getId());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LOAD_DATA, parameters);

			if (!result.next())
				throw new Exception(String.format("Task '%s' not found", task.getId()));

			data = result.getString("data");

			if (data == null)
				data = Strings.EMPTY;

			dataBuilder.append(data);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, task.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		task.setData(dataBuilder);
	}

	private String serializeShortcutInstances(Task task) {
		Map<String, Node> shortcutInstancesMap = task.getShortcutsInstances();
		List<String> shortcutsArray = new ArrayList<>();

		for (String shortcutName : shortcutInstancesMap.keySet()) {
			Node node = shortcutInstancesMap.get(shortcutName);
			shortcutsArray.add(shortcutName + "=" + node.getId());
		}

		return LibraryArray.implode(shortcutsArray, ",");
	}

	private void saveProperties(Task task) {
		Map<String, Object> parameters = new HashMap<>();
		User owner = task.getOwner();
		User sender = task.getSender();

		parameters.put(Database.QueryFields.ID, task.getId());
		parameters.put(Database.QueryFields.CODE, task.getCode());
		parameters.put(Database.QueryFields.ID_OWNER, task.getOwnerId());
		parameters.put(Database.QueryFields.OWNER_FULLNAME, owner != null ? owner.getInfo().getFullname() : null);
		parameters.put(Database.QueryFields.ID_SENDER, task.getSenderId());
		parameters.put(Database.QueryFields.SENDER_FULLNAME, sender != null ? sender.getInfo().getFullname() : null);
		parameters.put(Database.QueryFields.ID_TARGET, task.getTargetId());
		parameters.put(Database.QueryFields.PARTNER_CONTEXT, task.getPartnerContext());
		parameters.put(Database.QueryFields.ROLE, task.getRole());
		parameters.put(Database.QueryFields.LABEL, task.getLabel());
		parameters.put(Database.QueryFields.DESCRIPTION, LibraryString.cleanSpecialChars(task.getDescription().replace("\n", " ")));
		parameters.put(Database.QueryFields.SHORTCUTS, this.serializeShortcutInstances(task));
		parameters.put(Database.QueryFields.STATE, task.getState());
		parameters.put(Database.QueryFields.URGENT, task.isUrgent());
		parameters.put(Database.QueryFields.CLASSIFICATOR, task.getClassificator());
		parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
		parameters.put(Database.QueryFields.START_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalStartDate()));
		parameters.put(Database.QueryFields.SUGGESTED_START_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalStartSuggestedDate()));
		parameters.put(Database.QueryFields.END_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalEndDate()));
		parameters.put(Database.QueryFields.SUGGESTED_END_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalEndSuggestedDate()));
		parameters.put(Database.QueryFields.COMMENTS, task.getComments());
		Location location = task.getLocation();
		String updateQuery = null;
		if (location != null) {
			parameters.put(Database.QueryFields.GEOMETRY, location.getGeometry());
			parameters.put(Database.QueryFields.GEOMETRY_LABEL, location.getDescription());
			updateQuery = Database.Queries.TASK_SAVE_GEOMETRY;
		} else {
			updateQuery = Database.Queries.TASK_SAVE;
		}

		agentDatabase.executeRepositoryUpdateQuery(updateQuery, parameters);
	}

	private void saveLinks(Task<?> task) {
		Map<String, Object> parameters = new HashMap<>();
		Set<DatabaseRepositoryQuery> queries = new LinkedHashSet<>();

		parameters.put(Database.QueryFields.ID_SOURCE, task.getId());
		parameters.put(Database.QueryFields.ID_SOURCE_COMPONENT, task.getId());
		parameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.TASK);
		queries.add(new DatabaseRepositoryQuery(Database.Queries.LINK_DELETE, parameters));

		if ((task.getTargetId() != null) && (!task.getTargetId().equals(Strings.UNDEFINED_ID))) {
			HashMap<String, Object> queryParameters = new HashMap<>();
			queryParameters.put(Database.QueryFields.ID_SOURCE, task.getId());
			queryParameters.put(Database.QueryFields.ID_SOURCE_COMPONENT, null);
			queryParameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.TASK);
			queryParameters.put(Database.QueryFields.ID_TARGET, task.getTargetId());
			queryParameters.put(Database.QueryFields.TYPE_TARGET, LinkType.NODE);
			queryParameters.put(Database.QueryFields.DATA, LinkType.TASK);
			queryParameters.put(Database.QueryFields.DELETE_DATE, null);
			queries.add(new DatabaseRepositoryQuery(Database.Queries.LINK_ADD, queryParameters));
		}

		for (Node node : task.getShortcutsInstances().values()) {
			Map<String, Object> queryParameters = new HashMap<>();
			queryParameters.put(Database.QueryFields.ID_SOURCE, task.getId());
			queryParameters.put(Database.QueryFields.ID_SOURCE_COMPONENT, null);
			queryParameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.TASK);
			queryParameters.put(Database.QueryFields.ID_TARGET, node.getId());
			queryParameters.put(Database.QueryFields.TYPE_TARGET, LinkType.NODE);
			queryParameters.put(Database.QueryFields.DATA, LinkType.TASK);
			queryParameters.put(Database.QueryFields.DELETE_DATE, null);
			queries.add(new DatabaseRepositoryQuery(Database.Queries.LINK_ADD, queryParameters));
		}

		try {
			agentDatabase.executeRepositoryUpdateTransaction(queries.toArray(new DatabaseRepositoryQuery[0]));
		} catch (SQLException e) {
			agentLogger.error(e);
		}
	}

	private void loadProcess(Task task) {
		Injector injector = InjectorFactory.getInstance();
		Engine engine = injector.getInstance(Engine.class);
		task.setProcess(engine.getProcess(task.getId()));
	}

	private void finishTask(Task task) {
		Map<String, Object> parameters = new HashMap<>();
		String id = task.getId();

		agentNotifier.notify(new MonetEvent(MonetEvent.TASK_FINISHING, null, task));

		parameters.put(Database.QueryFields.ID, id);
		parameters.put(Database.QueryFields.END_DATE, agentDatabase.getDateAsTimestamp(new Date()));
		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_END_DATE, parameters);

		parameters.clear();
		parameters.put(Database.QueryFields.ID_TASK, id);
		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_ORDER_CLOSE_ALL_OF_TASK, parameters);

		agentNotifier.notify(new MonetEvent(MonetEvent.TASK_FINISHED, null, task));
	}

	public void updateLocation(Task task) {
		saveProperties(task);
	}
}
