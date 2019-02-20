package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.definition.entity.field.SerialFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class SerialFieldDefinitionBuilderTest extends ApplicationTestCase {

    public static final String JSON = "{\"code\":\"mztycrq\",\"name\":\"FieldSerialName\",\"label\":\"Campo serial\",\"type\":\"serial\",\"displays\":{\"totalCount\":0,\"items\":[]},\"serial\":{\"name\":\"SerialName\",\"format\":\"I-NNNN/Y\"}}";

    @Test
    public void testBuildSerialFieldDefinition() {
        final SerialFieldDefinitionBuilder builder = new SerialFieldDefinitionBuilder();
        final SerialFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mztycrq", definition.getCode());
        assertEquals("FieldSerialName", definition.getName());
        assertEquals("Campo serial", definition.getLabel());

        assertEquals("SerialName", definition.getSerial().getName());
        assertEquals("I-NNNN/Y", definition.getSerial().getFormat());
    }
}
