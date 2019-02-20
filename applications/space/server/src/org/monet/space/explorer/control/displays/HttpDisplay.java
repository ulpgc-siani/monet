package org.monet.space.explorer.control.displays;

import com.google.inject.Inject;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.explorer.configuration.Configuration;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.model.Language;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.explorer.model.List;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class HttpDisplay<T> implements Display<T> {
	private Dictionary dictionary;
	private Language language;
	private Configuration configuration;
	private LayerProvider layerProvider;
	private AgentLogger logger;
	private AgentFilesystem agentFilesystem;
	protected HttpServletResponse response;

	public void inject(HttpServletResponse response) {
		this.response = response;
	}

	@Inject
	public void inject(AgentLogger logger) {
		this.logger = logger;
	}

	@Inject
	public void inject(AgentFilesystem agentFilesystem) {
		this.agentFilesystem = agentFilesystem;
	}

	@Inject
	public void inject(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	@Inject
	public void inject(Language language) {
		this.language = language;
	}

	@Inject
	public void inject(Configuration configuration) {
		this.configuration = configuration;
	}

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	@Override
	public void write(T result) {
		getWriter().print(LibraryEncoding.encode(getSerializer(result).serialize(result)));
	}

	@Override
	public void writeList(List result) {
		getWriter().print(LibraryEncoding.encode(getSerializer().serializeList(result)));
	}

	@Override
	public void writeError(String error) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		getWriter().print(error);
	}

	@Override
	public OutputStream getOutputStream() {
		try {
			return response.getOutputStream();
		} catch (IOException exception) {
			this.logger.error(exception);
			throw new RuntimeException(exception.getMessage());
		}
	}

	@Override
	public PrintWriter getWriter() {
		try {
			return response.getWriter();
		} catch (IOException exception) {
			this.logger.error(exception);
			throw new RuntimeException(exception.getMessage());
		}
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public void setContentLength(int length) {
		response.setContentLength(length);
	}

	@Override
	public void setContentType(String contentType) {
		response.setContentType(contentType);
	}

	@Override
	public void redirectTo(String location) {
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", location);
		response.setContentType("text/html");
		response.addDateHeader("Expires", 0);

		response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0

		response.setDateHeader("Expires", 0);
	}

	@Override
	public void addHeader(String name, String value) {
		response.addHeader(name, value);
	}

	protected ExplorerSerializer.Helper createSerializerHelper() {
		return new ExplorerSerializer.Helper() {
			@Override
			public Dictionary getDictionary() {
				return dictionary;
			}

			@Override
			public Language getLanguage() {
				return language;
			}

			@Override
			public String constructApiUrl(String operationWithParams) {
				return configuration.getApiUrl() + "/" + operationWithParams;
			}

			@Override
			public Node loadContainerChildNode(Node node, String childDefinitionCode) {
				String idChild = node.getIndicatorValue("[" + childDefinitionCode + "].value");
				return layerProvider.getNodeLayer().loadNode(idChild);
			}

			@Override
			public Node locateNode(String code) {
				return layerProvider.getNodeLayer().locateNode(code);
			}

			@Override
			public Node loadNode(String id) {
				return layerProvider.getNodeLayer().loadNode(id);
			}

			@Override
			public TaskOrder loadTaskOrder(String orderId) {
				return layerProvider.getTaskLayer().loadTaskOrder(orderId);
			}

			@Override
			public Source<SourceDefinition> loadSource(String id) {
				return layerProvider.getSourceLayer().loadSource(id);
			}

			@Override
			public Source<SourceDefinition> locateSource(String code, FeederUri uri) {
				return layerProvider.getSourceLayer().locateSource(code, uri);
			}

			@Override
			public TermList loadSourceTerms(String id, DataRequest dataRequest) {
				SourceLayer sourceLayer = layerProvider.getSourceLayer();
				Source<SourceDefinition> source = sourceLayer.loadSource(id);
				return sourceLayer.loadSourceTerms(source, dataRequest, true);
			}

			@Override
			public AgentFilesystem getAgentFilesystem() {
				return agentFilesystem;
			}
		};
	}

	protected Serializer getSerializer() {
		return getSerializer(null);
	}

	protected abstract Serializer getSerializer(T object);

}
