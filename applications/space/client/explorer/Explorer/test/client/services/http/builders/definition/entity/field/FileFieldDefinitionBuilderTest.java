package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.definition.entity.field.FileFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class FileFieldDefinitionBuilderTest extends ApplicationTestCase {

    public static final String JSON = "{\"code\":\"mknyzsa\",\"name\":\"FieldFileName\",\"label\":\"Campo file\",\"type\":\"file\",\"displays\":{\"totalCount\":0,\"items\":[]}}";

    @Test
    public void testBuildFileFieldDefinition() {
        final FileFieldDefinitionBuilder builder = new FileFieldDefinitionBuilder();
        final FileFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mknyzsa", definition.getCode());
        assertEquals("FieldFileName", definition.getName());
        assertEquals("Campo file", definition.getLabel());
    }
}
