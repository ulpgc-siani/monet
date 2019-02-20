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

package org.monet.space.office.presentation.user.views;

import org.monet.space.office.core.constants.Tags;
import org.monet.space.office.core.model.Language;
import org.monet.space.office.presentation.user.agents.AgentRender;
import org.monet.space.office.presentation.user.util.Context;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskFactPage;
import org.monet.space.kernel.model.UserList;
import org.monet.space.office.core.constants.ErrorCode;

import java.io.IOException;
import java.io.Writer;

public class ViewTask extends View {
	private String codeTemplate;
	private UserList userList;

	public ViewTask(Context context, AgentRender agentRender, String codeLanguage) {
		super(context, agentRender, codeLanguage);
		this.codeTemplate = null;
		this.userList = new UserList();
	}

	public void setCodeTemplate(String codeTemplate) {
		this.codeTemplate = codeTemplate;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}

	public void execute(Writer writer) {
		String templateFilename;
		Task task = null;
		TaskFactPage factPage = null;
		String code = null;

		super.execute(writer);

		if (this.target == null) return;

		if (this.target instanceof Task) {
			task = (Task) this.target;
			code = (this.codeTemplate != null) ? this.codeTemplate : task.getCode();
		} else {
			factPage = (TaskFactPage) this.target;
			code = this.codeTemplate;
		}
		templateFilename = this.getTemplateFilename(code);
		if (templateFilename == null) {
			try {
				writer.write(Language.getInstance().getErrorMessage(ErrorCode.TEMPLATE_NOT_FOUND) + " Id: " + task.getId() + ". Mode: " + this.type + " - " + code + " - " + this.mode);
			} catch (IOException e) {
				this.agentLogger.error(e);
			}
		}

		this.context.put(templateFilename, false);
		this.context.put(Tags.TASK, task);
		this.context.put(Tags.FACTPAGE, factPage);
		this.context.put(Tags.USERLIST, this.userList);

		this.agentTemplates.mergeModelTemplate(templateFilename, this.context, writer);
	}

}