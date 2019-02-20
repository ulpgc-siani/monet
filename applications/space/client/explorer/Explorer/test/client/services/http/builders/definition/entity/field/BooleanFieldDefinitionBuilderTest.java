package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.definition.entity.FieldDefinition.FieldType;
import client.core.model.definition.entity.field.BooleanFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class BooleanFieldDefinitionBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"m7fbykw\",\"name\":\"FieldBooleanName\",\"label\":\"Campo booleano\",\"type\":\"boolean\",\"displays\":{\"totalCount\":0,\"items\":[]}}";

    @Test
    public void testBuildBooleanFieldDefinition() {
        BooleanFieldDefinitionBuilder builder = new BooleanFieldDefinitionBuilder();
        final BooleanFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals(FieldType.NORMAL, definition.getFieldType());
        assertEquals("m7fbykw", definition.getCode());
        assertEquals("FieldBooleanName", definition.getName());
        assertEquals("Campo booleano", definition.getLabel());
    }
}
