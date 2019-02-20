package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class CompositeFieldDefinitionBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"mrxrl3g\", \"name\":\"FieldCompositeName\", \"label\":\"Campo composite\", \"type\":\"composite\", \"displays\":{ \"totalCount\":0, \"items\":[ ] }, \"extensible\":false, \"conditional\":false, \"fields\":{ \"totalCount\":2, \"items\":[ { \"code\":\"mcl_bmw\", \"name\":\"FieldCheckName\", \"label\":\"Campo check composite\", \"type\":\"check\", \"displays\":{ \"totalCount\":0, \"items\":[ ] }, \"allowKey\":false }, { \"code\":\"mftt6gg\", \"name\":\"FieldBooleanName\", \"label\":\"Campo booleano composite\", \"type\":\"boolean\", \"displays\":{ \"totalCount\":0, \"items\":[ ] } } ] }, \"view\":{ \"show\":{ \"fields\":[ \"mftt6gg\", \"mcl_bmw\" ] } } }";

    @Test
    public void testBuildCompositeFieldDefinition() {
        final CompositeFieldDefinitionBuilder builder = new CompositeFieldDefinitionBuilder();
        final CompositeFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON));
        final List<FieldDefinition> fields = definition.getFields();

        assertEquals("mrxrl3g", definition.getCode());
        assertEquals("FieldCompositeName", definition.getName());
        assertEquals("Campo composite", definition.getLabel());
        assertFalse(definition.isExtensible());
        assertFalse(definition.isConditional());

        assertEquals(2, fields.size());
        assertEquals("mcl_bmw", fields.get(0).getCode());
        assertEquals("Campo check composite", fields.get(0).getLabel());
        assertEquals("mftt6gg", fields.get(1).getCode());
        assertEquals("Campo booleano composite", fields.get(1).getLabel());

    }
}
