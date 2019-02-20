package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.library.LibraryFile;
import org.monet.space.kernel.model.Node;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadNodeResourceDialog extends HttpDialog {
	private LayerProvider layerProvider;

	private static final String FILE_NAME_EMPTY_CHARACTERS_REPLACEMENT = "[^a-zA-Z\\._\\/0-9\\- \\(\\)]";
	private static final String BASE64 = "base64,";
	private Map<String, String> parameters;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	public Node getNode() {
		return layerProvider.getNodeLayer().loadNode(getEntityId());
	}

	public String getResourceName() {
		Map<String, String> parameters = loadParameters(request);
		String resourceName = LibraryEncoding.decode(parameters.get(Parameter.RESOURCE));
		return LibraryFile.getFilename(resourceName).replaceAll(FILE_NAME_EMPTY_CHARACTERS_REPLACEMENT, "");
	}

	public byte[] getResource() {
		Map<String, String> parameters = loadParameters(request);
		String data = LibraryEncoding.decode(parameters.get(Parameter.DATA));

		if (data == null)
			return null;

		return getBase64Data(data);
	}

	private Map<String, String> loadParameters(HttpServletRequest request) {
		if (parameters != null)
			return parameters;

		parameters = split(readRequest());

		return parameters;
	}

	private String readRequest() {
		StringBuilder result = new StringBuilder();

		try {
			String line;
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				result.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();
	}

	private Map<String, String> split(String parameters) {
		Map<String, String> result = new HashMap<>();

		String[] parametersArray = parameters.toString().split("&");
		for (String parameter : parametersArray) {
			String[] parameterArray = parameter.split("=");
			result.put(parameterArray[0], parameterArray[1]);
		}

		return result;
	}

	private byte[] getBase64Data(String data) {
		return DatatypeConverter.parseBase64Binary((data.substring(data.indexOf(BASE64) + BASE64.length())));
	}

}
