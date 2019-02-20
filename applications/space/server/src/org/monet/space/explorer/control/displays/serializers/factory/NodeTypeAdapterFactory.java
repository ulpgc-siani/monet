package org.monet.space.explorer.control.displays.serializers.factory;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.monet.space.explorer.control.displays.serializers.*;
import org.monet.space.kernel.model.Node;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.monet.space.explorer.control.displays.serializers.ExplorerSerializer.*;

public class NodeTypeAdapterFactory implements TypeAdapterFactory {
	private final Helper helper;

	public NodeTypeAdapterFactory(Helper helper) {
		this.helper = helper;
	}

	@Override
	public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
		return (TypeAdapter<T>) new TypeAdapter<Node>() {
			@Override
			public void write(JsonWriter jsonWriter, Node node) throws IOException {
				JsonSerializationContext jsonSerializationContext = getJsonSerializationContext();
				JsonSerializer serializer = null;

				switch (node.getType()) {
					case desktop: serializer = new DesktopSerializer(helper); break;
					case container: serializer = new ContainerSerializer(helper); break;
					case collection: serializer = new CollectionSerializer(helper); break;
					case catalog: serializer = new CatalogSerializer(helper); break;
					case form: serializer = new FormSerializer(helper); break;
					case document: serializer = new DocumentSerializer(helper); break;
				}

				if (serializer == null)
					return;

				JsonElement element = serializer.serialize(node, typeToken.getType(), jsonSerializationContext);
				Streams.write(element, jsonWriter);
			}

			@Override
			public Node read(JsonReader jsonReader) throws IOException {
				return null;
			}

			private JsonSerializationContext getJsonSerializationContext() {
				return new JsonSerializationContext() {
					public JsonElement serialize(Object src) {
						return gson.toJsonTree(src);
					}

					public JsonElement serialize(Object src, Type typeOfSrc) {
						return gson.toJsonTree(src, typeOfSrc);
					}
				};
			}
		};
	}

}
