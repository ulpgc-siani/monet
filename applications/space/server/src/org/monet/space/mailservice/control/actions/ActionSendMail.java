package org.monet.space.mailservice.control.actions;

import org.apache.commons.codec.binary.Base64;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ActionSendMail extends Action {

	private Map<String, String> files = new HashMap<String, String>();

	public ActionSendMail() {

	}

	@Override
	public String execute() {
		String from = (String) parameters.get("from");
		String body = (String) parameters.get("body");
		deserializeFiles((String) parameters.get("files"));

		// TODO Mario pendiente
		// El ejemplo siguiente muestra en la consola la dirección origen del correo, el cuerpo del mensaje y guarda en /tmp todos los ficheros adjuntos

		System.out.println("From: " + from);
		System.out.println("Body: " + body);

		for (Iterator<Entry<String, String>> it = files.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, String> file = (Entry<String, String>) it.next();

			Base64 decoder = new Base64();
			byte[] imgBytes = decoder.decode(file.getValue());
			FileOutputStream osf;
			try {
				osf = new FileOutputStream(new File("/tmp/" + file.getKey()));
				osf.write(imgBytes);
				osf.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "";
	}

	private void deserializeFiles(String content) {
		SAXBuilder oBuilder = new SAXBuilder();
		StringReader reader;
		org.jdom.Document document;
		Element element;

		if (content.equals(Strings.EMPTY))
			return;
		reader = new StringReader(content);

		files.clear();

		try {
			document = oBuilder.build(reader);
			element = document.getRootElement();

			Element file = null;

			@SuppressWarnings("unchecked")
			List<Element> paramElements = element.getChildren("file");
			for (Iterator<Element> i = paramElements.iterator(); i.hasNext(); ) {
				file = (Element) i.next();

				files.put(file.getAttribute("name").getValue(), file.getText());
			}
		} catch (JDOMException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, content, exception);
		} catch (IOException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, content, exception);
		}
	}
}
