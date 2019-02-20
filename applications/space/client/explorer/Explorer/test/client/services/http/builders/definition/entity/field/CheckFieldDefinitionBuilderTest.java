package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.List;
import client.core.model.definition.entity.field.CheckFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class CheckFieldDefinitionBuilderTest extends ApplicationTestCase {

    public static final String JSON_INLINE = "{\"code\":\"mltszxq\",\"name\":\"FieldCheckName\",\"label\":\"Campo check\",\"type\":\"check\",\"displays\":{\"totalCount\":0,\"items\":[]},\"allowKey\":false,\"terms\":{\"totalCount\":3,\"items\":[{\"key\":\"Key 1\",\"label\":\"Label 1\",\"terms\":{\"totalCount\":0,\"items\":[]}},{\"key\":\"Key 2\",\"label\":\"Label 2\",\"terms\":{\"totalCount\":0,\"items\":[]}},{\"key\":\"Key 3\",\"label\":\"Label 3\",\"terms\":{\"totalCount\":0,\"items\":[]}}]}}";
    public static final String JSON_SOURCE = "{\"code\":\"mcygzhg\",\"name\":\"FieldCheckNameSource\",\"label\":\"Campo check source\",\"type\":\"check\",\"displays\":{\"totalCount\":0,\"items\":[]},\"allowKey\":true,\"source\":\"m60by_a\"}";

    @Test
    public void testBuildCheckFieldDefinitionFromInlineChecks() {
        final CheckFieldDefinitionBuilder builder = new CheckFieldDefinitionBuilder();
        final CheckFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON_INLINE));
        final List<CheckFieldDefinition.TermDefinition> terms = definition.getTerms();

        assertEquals("mltszxq", definition.getCode());
        assertEquals("FieldCheckName", definition.getName());
        assertEquals("Campo check", definition.getLabel());
        assertFalse(definition.allowKey());

        assertEquals(3, terms.size());
        assertEquals("Key 1", terms.get(0).getKey());
        assertEquals("Label 1", terms.get(0).getLabel());
        assertEquals("Key 2", terms.get(1).getKey());
        assertEquals("Label 2", terms.get(1).getLabel());
        assertEquals("Key 3", terms.get(2).getKey());
        assertEquals("Label 3", terms.get(2).getLabel());

    }

    @Test
    public void testBuildCheckFieldDefinitionFromSource() {
        final CheckFieldDefinitionBuilder builder = new CheckFieldDefinitionBuilder();
        final CheckFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON_SOURCE));

        assertEquals("mcygzhg", definition.getCode());
        assertEquals("FieldCheckNameSource", definition.getName());
        assertEquals("Campo check source", definition.getLabel());
        assertTrue(definition.allowKey());
        assertEquals("m60by_a", definition.getSource());

    }
}
