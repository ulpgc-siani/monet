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

import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.utils.PersisterHelper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

public class ProducerFactTask extends Producer {

	public ProducerFactTask() {
		super();
	}

	public TaskFactList load(String taskId) {
		ResultSet result;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		TaskFactList factList = null;

		parameters.put(Database.QueryFields.ID_TASK, taskId);
		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_FACTS_LOAD, parameters);

		try {
			factList = new TaskFactList();

			while (result.next()) {
				TaskFact fact = new TaskFact();
				String extraDataSerialization = result.getString("extra_data");

				if (extraDataSerialization != null && !extraDataSerialization.isEmpty())
					fact = PersisterHelper.load(extraDataSerialization, TaskFact.class);
				else
					fact = new TaskFact();

				this.fillFact(result, fact);
				factList.add(fact);
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_FACTS, taskId, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return factList;
	}

	public List<Fact> load(String taskId, int startPos, int limit) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		List<Fact> entryList;

		parameters.put(Database.QueryFields.ID_TASK, taskId);
		parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(startPos));
		parameters.put(Database.QueryFields.LIMIT, limit);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_FACTS_LOAD_ENTRIES, parameters);

			entryList = new ArrayList<Fact>();
			while (result.next()) {
				TaskFact fact = null;
				String extraDataSerialization = result.getString("extra_data");

				if (extraDataSerialization != null && !extraDataSerialization.isEmpty())
					fact = PersisterHelper.load(extraDataSerialization, TaskFact.class);
				else
					fact = new TaskFact();

				fillFact(result, fact);
				entryList.add(fact);
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_FACTS, taskId, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return entryList;
	}

	private void fillFact(ResultSet result, TaskFact fact) throws Exception {
		fact.setId(String.valueOf(result.getLong("id")));
		fact.setTaskId(result.getString("id_task"));
		fact.setUserId(result.getString("id_user"));
		fact.setTitle(result.getString("title"));
		fact.setSubTitle(result.getString("subtitle"));
		fact.setCreateDate(this.agentDatabase.parseDateInMilliseconds(result, "create_date"));
	}

	public void addEntry(Fact entry) {
		Account account = this.getAccount();
		UserInfo info = account != null ? account.getUser().getInfo() : null;
		String fullName = info != null && !info.getFullname().isEmpty() ? info.getFullname() : agentDatabase.getEmptyString();

		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.ID_TASK, entry.getTaskId());
		parameters.put(Database.QueryFields.ID_USER, fullName);
		parameters.put(Database.QueryFields.TITLE, entry.getTitle());
		parameters.put(Database.QueryFields.SUBTITLE, entry.getSubTitle());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsMilliseconds(entry.getInternalCreateDate()));

		Serializer serializer = new Persister();
		StringWriter writer = new StringWriter();
		try {
			serializer.write(entry, writer);
		} catch (Exception e) {
			this.agentLogger.error(e);
		}
		parameters.put(Database.QueryFields.EXTRA_DATA, writer.toString());

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_FACTS_CREATE_ENTRY, parameters);
	}

	public int getCount(String idTask) {
		ResultSet result = null;
		HashMap<String, Object> hmParameters = new HashMap<String, Object>();
		int count;

		hmParameters.put(Database.QueryFields.ID_TASK, idTask);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_FACTS_LOAD_ENTRIES_COUNT, hmParameters);

			if (!result.next())
				throw new Exception(String.format("Can't get task facts total count of task '%s'", idTask));
			count = result.getInt("counter");
		} catch (Exception oException) {
			throw new DataException(ErrorCode.TASK_FACTS_ENTRIES_COUNT, idTask, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return count;
	}

	public Object newObject() {
		return null;
	}

	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
