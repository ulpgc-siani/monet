package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.fields.TextField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;

public class TextFieldBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"m4pu2zq\",\"type\":\"text\",\"label\":\"Campo texto\",\"value\":\"\"}";

    public void testBuildTextField() {
        final TextFieldBuilder builder = new TextFieldBuilder();
        final TextField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("m4pu2zq", field.getCode());
        assertEquals("Campo texto", field.getLabel());
        assertEquals("", field.getValue());
    }
}
