package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.model.fields.NumberField;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NumberFieldDisplayTest {

    @Test
    public void getUnitFromFormat() {
        IsNumberFieldDisplay display = new NumberFieldDisplay(mock(Node.class), field("# €"));
        IsNumberFieldDisplay display1 = new NumberFieldDisplay(mock(Node.class), field("## euros"));
        assertEquals("€", display.getUnit());
        assertEquals("#", display.getFormatNumber());
        assertEquals("euros", display1.getUnit());
        assertEquals("##", display1.getFormatNumber());
    }

    @Test
    public void getUnitFromFormatWhenIsAtTheBeginningOfTheFormat() {
        IsNumberFieldDisplay display = new NumberFieldDisplay(mock(Node.class), field("$ #.#"));
        assertEquals("$", display.getUnit());
        assertEquals("#.#", display.getFormatNumber());
    }

    @Test
    public void getUnitPosition() {
        IsNumberFieldDisplay displayUnitOnLeft = new NumberFieldDisplay(mock(Node.class), field("$ #.#"));
        IsNumberFieldDisplay displayUnitOnRight = new NumberFieldDisplay(mock(Node.class), field("## €"));
        assertTrue(displayUnitOnLeft.unitIsOnLeft());
        assertFalse(displayUnitOnRight.unitIsOnLeft());
    }




    private NumberField field(String format) {
        final NumberFieldDefinition definition = definition(format);
        NumberField field = mock(NumberField.class);
        when(field.getDefinition()).thenReturn(definition);
        return field;
    }

    private NumberFieldDefinition definition(String format) {
        NumberFieldDefinition definition = mock(NumberFieldDefinition.class);
        when(definition.getFormat()).thenReturn(format);
        return definition;
    }
}
