package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.model.definition.entity.field.NumberFieldDefinition.Edition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class NumberFieldDefinitionBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"mklznsw\",\"name\":\"FieldNumberName\",\"label\":\"Campo número\",\"type\":\"number\",\"displays\":{\"totalCount\":0,\"items\":[]},\"edition\":\"BUTTON\"}";

    @Test
    public void testBuildNumberFieldDefinition() {
        final NumberFieldDefinitionBuilder builder = new NumberFieldDefinitionBuilder();
        final NumberFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mklznsw", definition.getCode());
        assertEquals("FieldNumberName", definition.getName());
        assertEquals("Campo número", definition.getLabel());
        assertEquals(Edition.BUTTON, definition.getEdition());
    }
}
