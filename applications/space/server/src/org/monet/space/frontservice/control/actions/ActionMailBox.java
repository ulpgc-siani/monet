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

package org.monet.space.frontservice.control.actions;

import com.twmacinta.util.MD5;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.monet.space.frontservice.configuration.Configuration;
import org.monet.space.frontservice.control.constants.Parameter;
import org.monet.space.frontservice.core.constants.ErrorCode;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.exceptions.SessionException;
import org.monet.space.kernel.guice.InjectorFactory;
import org.monet.space.kernel.machines.ttm.CourierService;
import org.monet.space.kernel.machines.ttm.model.MailBox;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Signaling;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.utils.MessageHelper;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.*;

public class ActionMailBox extends Action {
	private CourierService courierService;

	public ActionMailBox() {
		super();
		this.courierService = InjectorFactory.getInstance().getInstance(CourierService.class);
	}

	public String execute() {
		String mailBoxId = (String) this.parameters.get(Parameter.ADDRESS);
		String signaling = (String) this.parameters.get(Parameter.SIGNALING);
		InputStream messageStream = (InputStream) this.parameters.get(Parameter.MESSAGE);
		String messageCode = (String) this.parameters.get(Parameter.MESSAGE_CODE);
		String messageType = (String) this.parameters.get(Parameter.MESSAGE_TYPE);
		String messageHash = (String) this.parameters.get(Parameter.MESSAGE_HASH);
        MailBoxUri addressUri = mailBoxId!=null && !mailBoxId.isEmpty()? MailBoxUri.build(BusinessUnit.getInstance().getName(), mailBoxId):null;
		User sender = Context.getInstance().getCurrentSession().getAccount().getUser();

		if (signaling != null && !signaling.isEmpty()) {
			this.courierService.signaling(sender, addressUri, Signaling.valueOf(signaling));
			return "OK";
		}

		if (mailBoxId == null || mailBoxId.trim().isEmpty())
			throw new SessionException(ErrorCode.INCORRECT_PARAMETERS, Parameter.ADDRESS);

		if (messageStream == null)
			throw new SessionException(ErrorCode.INCORRECT_PARAMETERS, Parameter.MESSAGE);

		if (messageCode == null)
			throw new SessionException(ErrorCode.INCORRECT_PARAMETERS, Parameter.MESSAGE_CODE);

		if (messageType == null)
			throw new SessionException(ErrorCode.INCORRECT_PARAMETERS, Parameter.MESSAGE_TYPE);

		if (messageHash == null)
			throw new SessionException(ErrorCode.INCORRECT_PARAMETERS, Parameter.MESSAGE_HASH);

		File messageFile = null;
		File messageDir = null;

		try {
			File tempDir = new File(Configuration.getInstance().getTempDir());
			if (!tempDir.exists())
				tempDir.mkdirs();
			messageFile = File.createTempFile("Message", "", tempDir);
			messageDir = new File(messageFile.getAbsolutePath() + "_content/");
			messageDir.mkdirs();

			this.saveMessageAndValidateHash(messageStream, messageHash, messageFile);

			Message message = new Message();
			message.setSubject(messageCode);
			message.setType(messageType);
			message.setTo(addressUri.toString());

			MessageHelper.parseMessageContent(messageFile, messageDir, message);

            if (this.isMailBoxClosed(mailBoxId))
                return "CLOSED";

			this.courierService.deliver(sender, message);

			return "OK";
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new RuntimeException("Internal error. See log for details.");
		} finally {
			AgentFilesystem.removeDir(messageDir);

			if (messageFile != null)
				messageFile.delete();
		}
	}

	private void saveMessageAndValidateHash(InputStream message, String messageHash, File messageFile) throws FileNotFoundException, IOException {
		MD5 md5Hasher = new MD5();
		md5Hasher.Init();

		FileOutputStream messageFileOutputStream = null;
		try {
			messageFileOutputStream = new FileOutputStream(messageFile);
			byte[] buffer = new byte[8192];
			int bytesRead = 0;
			while ((bytesRead = message.read(buffer)) > 0) {
				md5Hasher.Update(buffer, 0, bytesRead);
				messageFileOutputStream.write(buffer, 0, bytesRead);
			}
		} finally {
			StreamHelper.close(messageFileOutputStream);
		}

		md5Hasher.Final();
		if (!md5Hasher.asHex().equals(messageHash)) {
			throw new SessionException(ErrorCode.INCORRECT_PARAMETERS, Parameter.MESSAGE_HASH);
		}
	}

    private boolean isMailBoxClosed(String mailBoxId) {
        ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
        MailBox mailBox = componentPersistence.getMailBoxLayer().load(mailBoxId);

        if (mailBox == null)
            return true;

        if (mailBox.getTaskId() == null)
            return false;

        Task task = componentPersistence.getTaskLayer().loadTask(mailBox.getTaskId());

        if (task.isFinished() || task.isAborted())
            return true;

        return false;
    }

}