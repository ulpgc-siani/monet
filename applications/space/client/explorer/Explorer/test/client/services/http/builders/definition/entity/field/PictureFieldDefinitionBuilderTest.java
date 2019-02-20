package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class PictureFieldDefinitionBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"mk0c_yq\",\"name\":\"FieldPictureName\",\"label\":\"Campo picture\",\"type\":\"picture\",\"displays\":{\"totalCount\":0,\"items\":[]}}";
    private static final String JSON_WITH_SIZE = "{\"code\":\"mk0c_yq\",\"name\":\"FieldPictureName\",\"label\":\"Campo picture\",\"type\":\"picture\",\"displays\":{\"totalCount\":0,\"items\":[]},\"size\":{\"width\":300,\"height\":200}}";

    @Test
    public void testBuildPictureFieldDefinition() {
        final PictureFieldDefinitionBuilder builder = new PictureFieldDefinitionBuilder();
        final PictureFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mk0c_yq", definition.getCode());
        assertEquals("FieldPictureName", definition.getName());
        assertEquals("Campo picture", definition.getLabel());
    }

    @Test
    public void testBuildPictureFieldDefinitionWithSize() {
        final PictureFieldDefinitionBuilder builder = new PictureFieldDefinitionBuilder();
        final PictureFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON_WITH_SIZE));

        assertEquals("mk0c_yq", definition.getCode());
        assertEquals("FieldPictureName", definition.getName());
        assertEquals("Campo picture", definition.getLabel());
        assertEquals(300, definition.getSize().getWidth());
        assertEquals(200, definition.getSize().getHeight());
    }
}
