package client.services.http.builders.definition.entity.field;

import client.ApplicationTestCase;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class LinkFieldDefinitionBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{ \"code\":\"F041\", \"name\":\"Fi041\", \"label\":\"Entidades participantes\", \"type\":\"link\", \"displays\":{ \"totalCount\":0, \"items\":[ ] }, \"source\":{ \"index\":\"mu_sfca\", \"filters\":[ ] }, \"allowAdd\":false, \"allowSearch\":false, \"allowEdit\":true }";

    @Test
    public void testBuildLinkFieldDefinition() {
        final LinkFieldDefinitionBuilder builder = new LinkFieldDefinitionBuilder();
        final LinkFieldDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mu_sfca", definition.getSource().getIndex());
        assertTrue(definition.allowEdit());
    }
}
