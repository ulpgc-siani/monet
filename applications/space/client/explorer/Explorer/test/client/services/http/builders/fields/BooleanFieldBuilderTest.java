package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.fields.BooleanField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class BooleanFieldBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"m7fbykw\",\"type\":\"boolean\",\"label\":\"Campo booleano\",\"value\":false}";

    @Test
    public void testBuildBooleanField() {
        final BooleanFieldBuilder builder = new BooleanFieldBuilder();
        final BooleanField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("m7fbykw", field.getCode());
        assertEquals("Campo booleano", field.getLabel());
        assertFalse(field.getValue());
    }
}
