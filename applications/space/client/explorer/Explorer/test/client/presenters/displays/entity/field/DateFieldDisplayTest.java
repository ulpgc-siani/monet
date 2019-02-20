package client.presenters.displays.entity.field;

import client.core.FieldBuilder;
import client.core.NodeBuilder;
import client.core.model.definition.entity.field.DateFieldDefinition;
import client.core.model.definition.entity.field.DateFieldDefinition.RangeDefinition;
import client.core.model.fields.DateField;
import cosmos.types.Date;
import org.junit.Test;

import static client.core.model.definition.entity.field.DateFieldDefinition.Precision;
import static client.core.model.definition.entity.field.DateFieldDefinition.Purpose;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateFieldDisplayTest {

	@Test
	public void checkValue() {
		Date date = new Date(2010,11,1,8,0,0);
		DateFieldDisplay display = display(date, definition(-1, -1, null));

		Date compareDate = new cosmos.types.Date(2010,11,1,8,0,0);
		assertEquals(display.getValue(), compareDate);
	}

	@Test
	public void checkValueWhenNull() {
		DateFieldDisplay display = display(null, definition(-1, -1, null));
        assertNull(display.getValue());
	}

	@Test
	public void checkRangeMin() {
		DateFieldDisplay display = display(new Date(2010,11,1,8,0,0), definition(new Date(2010,11,1,9,0,0).getMilliseconds(), -1, null));
		assertTrue(display.rangeWrong());
	}

	@Test
	public void checkRangeMax() {
		DateFieldDisplay display = display(new Date(2010,11,1,8,0,0), definition(-1, new Date(2010,11,1,7,59,0).getMilliseconds(), null));
		assertTrue(display.rangeWrong());
	}

	@Test
	public void checkRangeMinAndMax() {
		DateFieldDisplay display = display(new Date(2010,11,1,8,0,0), definition(new Date(2010,11,1,9,0,0).getMilliseconds(), new Date(2010,11,1,7,59,0).getMilliseconds(), null));
		assertTrue(display.rangeWrong());

		display = display(new Date(2010,11,1,8,0,0), definition(new Date(2010,11,1,8,0,0).getMilliseconds(), new Date(2010,11,1,10,0,0).getMilliseconds(), null));
		assertFalse(display.rangeWrong());

		display = display(new Date(2010,11,1,8,0,0), definition(new Date(2010,11,1,8,0,0).getMilliseconds(), new Date(2010,11,1,8,0,0).getMilliseconds(), null));
		assertFalse(display.rangeWrong());
	}

    @Test
    public void isTimeValidWithSecondsPrecision() {
        DateFieldDisplay display = display(new Date(2010,11,1,8,0,0), definition(new Date(2010,11,1,9,0,0).getMilliseconds(), new Date(2010,11,1,7,59,0).getMilliseconds(), Precision.SECONDS));
        assertTrue(display.rangeWrong());

        display = display(null, definition(new Date(2010,11,1,8,0,0).getMilliseconds(), new Date(2010,11,1,10,0,0).getMilliseconds(), Precision.SECONDS));
        assertFalse(display.dateIsBetweenRange(new cosmos.types.Date(2010, 11, 1, 7, 59, 0)));
        assertTrue(display.dateIsBetweenRange(new cosmos.types.Date(2010, 11, 1, 8, 34, 0)));
    }






















	// INITIALIZERS

	private DateFieldDisplay display(Date value, DateFieldDefinition definition) {
		DateField field = FieldBuilder.buildDate("001", "Campo fecha");
		field.setDefinition(definition);
		field.setValue(value);
		DateFieldDisplay display = new DateFieldDisplay(NodeBuilder.buildForm(null, null, false), field);
		display.inject(null);
		return display;
	}

	private DateFieldDefinition definition(final long rangeMin, final long rangeMax, final Precision precision) {
		final DateFieldDefinition definition = mock(DateFieldDefinition.class);
		final RangeDefinition range = mock(RangeDefinition.class);
		when(range.getMin()).thenReturn(rangeMin);
		when(range.getMax()).thenReturn(rangeMax);
		when(definition.getRange()).thenReturn(range);
		when(definition.getPrecision()).thenReturn(precision);
		when(definition.getPurpose()).thenReturn(Purpose.NEAR_DATE);
		return definition;
	}
}
