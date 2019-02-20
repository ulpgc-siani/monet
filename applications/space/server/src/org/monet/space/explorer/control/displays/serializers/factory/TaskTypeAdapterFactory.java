package org.monet.space.explorer.control.displays.serializers.factory;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.monet.space.explorer.control.displays.serializers.ActivitySerializer;
import org.monet.space.explorer.control.displays.serializers.JobSerializer;
import org.monet.space.explorer.control.displays.serializers.ServiceSerializer;
import org.monet.space.kernel.model.Task;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.monet.space.explorer.control.displays.serializers.ExplorerSerializer.Helper;

public class TaskTypeAdapterFactory implements TypeAdapterFactory {
	private final Helper helper;

	public TaskTypeAdapterFactory(Helper helper) {
		this.helper = helper;
	}

	@Override
	public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
		return (TypeAdapter<T>) new TypeAdapter<Task>() {
			@Override
			public void write(JsonWriter jsonWriter, Task task) throws IOException {
				JsonSerializationContext jsonSerializationContext = getJsonSerializationContext();
				JsonSerializer serializer = null;

				switch (task.getType()) {
					case activity: serializer = new ActivitySerializer(helper); break;
					case service: serializer = new ServiceSerializer(helper); break;
					case job: serializer = new JobSerializer(helper); break;
				}

				if (serializer == null)
					return;

				JsonElement element = serializer.serialize(task, typeToken.getType(), jsonSerializationContext);
				Streams.write(element, jsonWriter);
			}

			@Override
			public Task read(JsonReader jsonReader) throws IOException {
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
