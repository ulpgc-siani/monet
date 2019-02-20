package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.fields.PictureField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class PictureFieldBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"mk0c_yq\",\"type\":\"picture\",\"label\":\"Campo picture\"}";

    @Test
    public void testBuildPictureField() {
        final PictureFieldBuilder builder = new PictureFieldBuilder();
        final PictureField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mk0c_yq", field.getCode());
        assertEquals("Campo picture", field.getLabel());
    }
}
