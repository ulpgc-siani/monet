package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class SelectFieldDefinitionBuilderTest extends ApplicationTestCase {

    private static final String DEFINITION = "{  \n" +
            "            \"code\":\"PaisNacionalidad\",\n" +
            "            \"name\":\"PaisNacionalidad\",\n" +
            "            \"label\":\"País de nacionalidad\",\n" +
            "            \"type\":\"select\",\n" +
            "            \"displays\":{  \n" +
            "               \"totalCount\":0,\n" +
            "               \"items\":[  ]\n" +
            "            },\n" +
            "            \"source\":\"m1rsupg\",\n" +
            "            \"allowOther\":false,\n" +
            "            \"allowKey\":false\n" +
            "         }";

    @Test
    public void testCreateDefinitionFromJson() {
        SelectFieldDefinitionBuilder builder = new SelectFieldDefinitionBuilder();
        SelectFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(DEFINITION));

        assertEquals("PaisNacionalidad", definition.getCode());
        assertEquals("PaisNacionalidad", definition.getName());
        assertEquals("País de nacionalidad", definition.getLabel());
        assertEquals("m1rsupg", definition.getSource());
    }
}
