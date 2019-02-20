package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.fields.SerialField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class SerialFieldBuilderTest extends ApplicationTestCase {

    public static final String JSON = "{\"code\":\"mztycrq\",\"type\":\"serial\",\"label\":\"Campo serial\",\"value\":\"\"}";

    @Test
    public void testBuildSerialField() {
        final SerialFieldBuilder builder = new SerialFieldBuilder();
        final SerialField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mztycrq", field.getCode());
        assertEquals("Campo serial", field.getLabel());
        assertEquals("", field.getValue());
    }
}
