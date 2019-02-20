package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.fields.NumberField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class NumberFieldBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"mklznsw\",\"type\":\"number\",\"label\":\"Campo número\",\"value\":\"0\"}";

    @Test
    public void testBuildNumberField() {
        final NumberFieldBuilder builder = new NumberFieldBuilder();
        final NumberField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mklznsw", field.getCode());
        assertEquals("Campo número", field.getLabel());
        assertEquals(new client.core.system.types.Number<>(0), field.getValue());
    }
}
