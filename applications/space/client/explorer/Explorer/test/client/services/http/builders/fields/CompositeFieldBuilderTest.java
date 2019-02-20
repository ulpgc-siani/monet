package client.services.http.builders.fields;

import client.ApplicationTestCase;
import client.core.model.Field;
import client.core.model.fields.BooleanField;
import client.core.model.fields.CheckField;
import client.core.model.fields.CompositeField;
import client.core.model.fields.MultipleCompositeField;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class CompositeFieldBuilderTest extends ApplicationTestCase {

    private static final String JSON = "{\"code\":\"mrxrl3g\", \"type\":\"composite\", \"label\":\"Campo composite\", \"value\":{\"fields\":[{\"code\":\"mcl_bmw\", \"type\":\"check\", \"label\":\"Campo check composite\", \"value\":{ \"terms\":{ \"totalCount\":0, \"items\":[ ]}}}, { \"code\":\"mftt6gg\", \"type\":\"boolean\", \"label\":\"Campo booleano composite\", \"value\":false}]}}";
    private static final String JSON_WITH_CONDITION = "{\"code\":\"mrxrl3g\", \"type\":\"composite\", \"label\":\"Campo composite\", \"value\":{\"fields\":[{\"code\":\"mcl_bmw\", \"type\":\"check\", \"label\":\"Campo check composite\", \"value\":{ \"terms\":{ \"totalCount\":0, \"items\":[ ]}}}, { \"code\":\"mftt6gg\", \"type\":\"boolean\", \"label\":\"Campo booleano composite\", \"value\":false}], \"conditioned\":true}}";

    private static final String JSON_COMPOSITE_WITH_MULTIPLE_COMPOSITE = "{\"code\": \"F007\", \"type\": \"composite\", \"label\": \"Publicación\", \"value\": { \"conditioned\": false, \"fields\": [ { \"code\": \"F060\", \"type\": \"composite\", \"multiple\": true, \"label\": \"Categoría de la publicación\", \"value\": [ { \"conditioned\": false, \"fields\": [ { \"code\": \"F066\", \"type\": \"text\", \"label\": \"Publicación, índice de impacto de año de publicación\", \"value\": \"\" } ] }, { \"conditioned\": false, \"fields\": [ { \"code\": \"F066\", \"type\": \"text\", \"label\": \"Publicación, índice de impacto de año de publicación\", \"value\": \"\"}]}]}]}}";

    @Test
    public void testBuildCompositeField() {
        final CompositeFieldBuilder builder = new CompositeFieldBuilder();
        final CompositeField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON));

        assertEquals("mrxrl3g", field.getCode());
        assertEquals("Campo composite", field.getLabel());

        assertNotNull(field.getValue());
        assertTrue(field.getValue().get(0) instanceof CheckField);
        assertTrue(field.getValue().get(1) instanceof BooleanField);
    }

    @Test
    public void testBuildCompositeFieldWithCondition() {
        final CompositeFieldBuilder builder = new CompositeFieldBuilder();
        final CompositeField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON_WITH_CONDITION));

        assertTrue(field.getConditioned());
    }

    @Test
    public void testBuildCompositeWithMultipleCompositeInside() {
        final CompositeFieldBuilder builder = new CompositeFieldBuilder();
        final CompositeField field = builder.build((HttpInstance) JsonUtils.safeEval(JSON_COMPOSITE_WITH_MULTIPLE_COMPOSITE));
        MultipleCompositeField child = (MultipleCompositeField) field.getValue().get(0);
        CompositeField compositeInsideMultiple = child.get(0);
        Field childOfInnerComposite = compositeInsideMultiple.getValue().get(0);

        assertTrue(childOfInnerComposite.getOwner() instanceof CompositeField);
        assertTrue(compositeInsideMultiple.getOwner() instanceof MultipleCompositeField);
        assertTrue(child.getOwner() instanceof CompositeField);
    }
}
