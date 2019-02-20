package client.services.http.dialogs;

import client.core.model.Entity;
import client.services.Dialog;
import com.google.gwt.http.client.URL;

import java.util.HashMap;
import java.util.Map;

public abstract class HttpDialog implements Dialog {

    private final Map<String, Object> parameters;

    public HttpDialog() {
        this.parameters = new HashMap<>();
    }

    @Override
    public void add(String name, Object value) {
        parameters.put(name, value);
    }

    @Override
    public String serialize() {
        String result = "";
        for (Map.Entry<String, Object> entry : parameters.entrySet())
            result += (!result.isEmpty() ? "&" : "") + serialize(entry);
        return result;
    }

    private String serialize(Map.Entry<String, Object> entry) {
        if (entry.getValue() == null)
            return "";
        return entry.getKey() + "=" + URL.encodeQueryString(entry.getValue().toString());
    }

    protected String serializeIds(Entity[] entities) {
        String result = "";
        for (Entity entity : entities)
            result += (!result.isEmpty() ? "," : "") + entity.getId();
        return result;
    }

}
