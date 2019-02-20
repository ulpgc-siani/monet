package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.definition.entity.field.MemoFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class MemoFieldDefinitionBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"mt_f_cg\",\"name\":\"FieldMemoName\",\"label\":\"Campo memo\",\"type\":\"memo\",\"displays\":{\"totalCount\":0,\"items\":[]},\"length\":{\"max\":50}}";

    @Test
    public void testBuildMemoFieldDefinition() {
        final MemoFieldDefinitionBuilder builder = new MemoFieldDefinitionBuilder();
        final MemoFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mt_f_cg", definition.getCode());
        assertEquals("FieldMemoName", definition.getName());
        assertEquals("Campo memo", definition.getLabel());
        assertEquals(50, definition.getLength().getMax());
    }
}
