package client.presenters.displays.entity.field;

import client.core.FieldBuilder;
import client.presenters.displays.entity.FieldDisplay;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FieldDisplayBuilderTest {

    private static FieldDisplay.Builder builder;

    @BeforeClass
    public static void setUpClass() {
        builder = new FieldDisplay.Builder();
    }

    @Test
    public void checkBuildWhenNull() {
        assertTrue(builder.build(null, null) == null);
    }

    @Test
    public void checkBuildBooleanFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildBoolean("code", "label")) instanceof BooleanFieldDisplay);
    }

    @Test
    public void checkBuildCheckFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildCheck("code", "label")) instanceof CheckFieldDisplay);
    }

    @Test
    public void checkBuildCompositeFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildComposite("code", "label")) instanceof CompositeFieldDisplay);
    }

    @Test
    public void checkBuildDateFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildDate("code", "label")) instanceof DateFieldDisplay);
    }

    @Test
    public void checkBuildFileFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildFile("code", "label")) instanceof FileFieldDisplay);
    }

    @Test
    public void checkBuildLinkFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildLink("code", "label")) instanceof LinkFieldDisplay);
    }

    @Test
    public void checkBuildMemoFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildMemo("code", "label")) instanceof MemoFieldDisplay);
    }

    @Test
    public void checkBuildNodeFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildNode("code", "label")) instanceof NodeFieldDisplay);
    }

    @Test
    public void checkBuildNumberFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildNumber("code", "label")) instanceof NumberFieldDisplay);
    }

    @Test
    public void checkBuildPictureFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildPicture("code", "label")) instanceof PictureFieldDisplay);
    }

    @Test
    public void checkBuildSelectFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildSelect("code", "label")) instanceof SelectFieldDisplay);
    }

    @Test
    public void checkBuildSerialFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildSerial("code", "label")) instanceof SerialFieldDisplay);
    }

    @Test
    public void checkBuildSummationFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildSummation("code", "label")) instanceof SummationFieldDisplay);
    }

    @Test
    public void checkBuildTextFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildText("code", "label")) instanceof TextFieldDisplay);
    }

    @Test
    public void checkBuildUriFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildUri("code", "label")) instanceof UriFieldDisplay);
    }

    @Test
    public void checkBuildMultipleDateFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildDateMultiple("code", "label")) instanceof MultipleDateFieldDisplay);
    }

    @Test
    public void checkBuildMultipleSelectFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildSelectMultiple("code", "label")) instanceof MultipleSelectFieldDisplay);
    }

    @Test
    public void checkBuildMultipleTextFieldDisplay() {
        assertTrue(builder.build(null, FieldBuilder.buildTextMultiple("code", "label")) instanceof MultipleTextFieldDisplay);
    }

    @Test
    public void checkBuildFieldFromAnonymousClass() {
        assertTrue(builder.build(null, FieldBuilder.buildTextMultiple("code", "label")) instanceof MultipleTextFieldDisplay);
    }
}
