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

import org.monet.metamodel.SerialFieldProperty;
import org.monet.space.kernel.agents.AgentDatabase;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.model.SequenceValue;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Calendar;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class ProducerSequence extends Producer {
	private static final int CompatibilityYear = 2018;
	private static Scheduler scheduler;

	public ProducerSequence() {
		super();
		createTimer();
	}

	public Object newObject() {
		return new Indicator();
	}

	public void resetYearSequences() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);

		if (sequencesAlreadyReset(currentYear)) return;
		List<String> sequencesWithYear = getSequencesWithYear();
		for(String sequence : sequencesWithYear) resetValue(sequence);

		AgentDatabase agentDatabase = AgentDatabase.getInstance();

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.VALUE, String.valueOf(currentYear));

		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.INFO_UPDATE_YEAR_SEQUENCE, parameters);
	}

	public Boolean exists(String codeSequence) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;

		parameters.put(Database.QueryFields.CODE, codeSequence);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SEQUENCE_EXISTS, parameters);
			return result.next();
		} catch (SQLException oException) {
			AgentLogger.getInstance().error(oException);
			return false;
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public void create(String codeSequence, String codeSubSequence, Integer value) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		if (this.exists(codeSequence)) return;

		parameters.put(Database.QueryFields.CODE, codeSequence);
		parameters.put(Database.QueryFields.CODE_SUBSEQUENCE, codeSubSequence);
		parameters.put(Database.QueryFields.VALUE, value);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.SEQUENCE_CREATE, parameters);
	}

	public synchronized void resetValue(String codeSequence) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		this.create(codeSequence, "", 0);
		parameters.put(Database.QueryFields.CODE, codeSequence);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.SEQUENCE_RESET_VALUE, parameters);
	}

	public synchronized SequenceValue createValue(String codeSequence) {
		ResultSet result = null;
		SequenceValue sequenceValue;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		this.create(codeSequence, "", 0);

		parameters.put(Database.QueryFields.CODE, codeSequence);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.SEQUENCE_CREATE_VALUE, parameters);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SEQUENCE_SELECT_LAST_VALUE, parameters);

			if (!result.next())
				throw new Exception(String.format("Sequence '%s' not found", codeSequence));

			sequenceValue = new SequenceValue(codeSequence, result.getString("code_subsequence"), result.getInt("value"));
		} catch (Exception oException) {
			throw new DataException(ErrorCode.CREATE_SEQUENCE_VALUE, ErrorCode.GENERATE_ID, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return sequenceValue;
	}

	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

	private void createTimer() {
		try {
			if (scheduler == null) scheduler = StdSchedulerFactory.getDefaultScheduler();
			JobDetail job = newJob(SequenceYearTrigger.class).withIdentity("SequenceYearJob", "group1").build();
			job.getJobDataMap().put("producer", this);
			scheduler.scheduleJob(job, newTrigger().withIdentity("SequenceYearTrigger", "group1").withSchedule(cronSchedule("0 0 0 1 1 ?")).build());
			scheduler.start();
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
	}

	private boolean sequencesAlreadyReset(int currentYear) {
		if (currentYear <= CompatibilityYear) return true;

		ResultSet result = null;
		AgentDatabase agentDatabase = AgentDatabase.getInstance();

		try {
			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.INFO_LOAD_YEAR_SEQUENCE, new HashMap<String, Object>());
			if (!result.next()) {
				Map<String, Object> parameters = new HashMap<>();
				parameters.put(Database.QueryFields.VALUE, String.valueOf(currentYear));
				agentDatabase.executeRepositoryUpdateQuery(Database.Queries.INFO_INSERT_YEAR_SEQUENCE, parameters);
				return false;
			}
			return Integer.valueOf(result.getString("value")) >= currentYear;
		} catch (Exception exception) {
			return false;
		} finally {
			agentDatabase.closeQuery(result);
		}
	}

	private List<String> getSequencesWithYear() {
		Dictionary dictionary = getDictionary();
		List<SerialFieldProperty> serialFieldProperties = dictionary.getSerialFieldPropertyList();
		HashSet<String> serialsWithSequenceAndYear = new HashSet<>();
		for (SerialFieldProperty property : serialFieldProperties) {
			boolean hasSequencePart = property.hasSequencePart();
			boolean hasYearPart = property.hasYearPart();
			if (!hasYearPart) continue;
			if (hasSequencePart) serialsWithSequenceAndYear.add(property.getSerial().getName());
		}
		return new ArrayList<>(serialsWithSequenceAndYear);
	}

	private static Set<Trigger> newSet(Trigger... triggers) {
		LinkedHashSet<Trigger> set = new LinkedHashSet<>();
		java.util.Collections.addAll(set, triggers);
		return set;
	}

}