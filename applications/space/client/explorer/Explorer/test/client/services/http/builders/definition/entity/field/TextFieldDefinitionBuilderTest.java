package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class TextFieldDefinitionBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"m4pu2zq\",\"name\":\"FieldTextName\",\"label\":\"Campo texto\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]}";

    @Test
    public void testBuildTextFieldDefinition() {
        final TextFieldDefinitionBuilder builder = new TextFieldDefinitionBuilder();
        final TextFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("m4pu2zq", definition.getCode());
        assertEquals("FieldTextName", definition.getName());
        assertEquals("Campo texto", definition.getLabel());
    }
}
