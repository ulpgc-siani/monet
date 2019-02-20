package org.monet.mocks.businessunit;

import net.minidev.json.JSONObject;
import org.junit.Test;
import org.monet.mocks.businessunit.agents.AgentFilesystem;
import org.monet.mocks.businessunit.core.Configuration;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class ServiceTest {

	@Test
	public void requestService() {
		Service service = getService();
		String mailBoxUrl = service.requestService("com.altera.NotificadorAvisos", true, new Date(), new Date(), "Notificacion de aviso a la unidad de negocio cliente");
		assertNotNull(mailBoxUrl);
	}

	@Test
	public void sendMessage() {
		Service service = getService();
		String mailBoxUrl = service.requestService("com.altera.NotificadorAvisos", true, new Date(), new Date(), "Notificacion de aviso a la unidad de negocio cliente");
		Service.Message message = createMessage();
		boolean result = service.sendMessage(mailBoxUrl, "Formulario", message);
		assertTrue(result);
	}

	private Service.Message createMessage() {
		return new Service.Message() {

			@Override
			public Map<String, File> getAttachments() {
				Map<String, File> attachments = new HashMap<String, File>();
				return attachments;
			}

			@Override
			public void addAttachment(String key, File file) {
			}

			@Override
			public String getContent() {
				JSONObject incidencia = new JSONObject();
				incidencia.put("title", "Parque Nestor de la Torre roto");
				incidencia.put("code", "0001");
				return incidencia.toString();
			}

			@Override
			public void setContent(String content) {
			}
		};
	}

	private Service getService() {
		return new BusinessUnitService(getConfiguration(), new AgentFilesystem());
	}

	private Configuration getConfiguration() {
		return new Configuration() {
			@Override
			public String getCertificateFilename() {
				return "/Users/mcaballero/.monet/certificates/businessunit-monet.p12";
			}

			@Override
			public String getCertificatePassword() {
				return "1234";
			}

			@Override
			public String getClientUnitUrl() {
				return "http://localhost:8080/monet";
			}

			@Override
			public String getUnitName() {
				return "monet";
			}

			@Override
			public String getUnitUrl() {
				return "http://altera.com:8091/businessunitmock/";
			}

			@Override
			public String getUnitCallbackUrl() {
				return "http://altera.com:8091/businessunitmock/?action=callback";
			}
		};
	}

}
