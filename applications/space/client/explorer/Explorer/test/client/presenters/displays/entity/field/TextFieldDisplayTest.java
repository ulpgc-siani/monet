package client.presenters.displays.entity.field;

import client.core.FieldBuilder;
import client.core.NodeBuilder;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.fields.TextField;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TextFieldDisplayTest {

	@Test
	public void checkValue() {
		TextFieldDisplay display = display("Mario caballero ramírez", definition(-1, -1, null));
		assertEquals(display.getValue().length(), "mario caballero ramírez".length());
	}

	@Test
	public void checkValueWhenNull() {
		TextFieldDisplay display = display(null, definition(-1, -1, null));
		assertNull(display.getValue());
	}

	@Test
	public void checkMinLength() {
		TextFieldDisplay display = display("mario", definition(10, -1, null));
		assertTrue(display.minLengthWrong());
	}

	@Test
	public void checkMaxLength() {
		TextFieldDisplay display = display("Mario caballero ramírez", definition(-1, 10, null));
		assertEquals(display.getValue().length(), "mario caba".length());
	}

	@Test
	public void checkModeUpperCase() {
		TextFieldDisplay display = display("Mario caballero ramírez", definition(-1, -1, TextFieldDefinition.EditionDefinition.Mode.UPPERCASE));
		assertEquals(display.getValue(), "MARIO CABALLERO RAMÍREZ");
	}

	@Test
	public void checkModeLowerCase() {
		TextFieldDisplay display = display("Mario caballero ramírez", definition(-1, -1, TextFieldDefinition.EditionDefinition.Mode.LOWERCASE));
		assertEquals(display.getValue(), "mario caballero ramírez");
	}

	@Test
	public void checkTitleCase() {
		TextFieldDisplay display = display("Mario caballero ramírez", definition(-1, -1, TextFieldDefinition.EditionDefinition.Mode.TITLE));
		assertEquals(display.getValue(), "Mario caballero ramírez");
	}

	@Test
	public void checkSentenceCase() {
		TextFieldDisplay display = display("Mario caballero ramírez", definition(-1, -1, TextFieldDefinition.EditionDefinition.Mode.SENTENCE));
		assertEquals(display.getValue(), "Mario Caballero Ramírez");
	}


























	// INITIALIZERS

	private TextFieldDisplay display(String value, TextFieldDefinition definition) {
		TextField field = FieldBuilder.buildText("001", "Campo texto");
		field.setDefinition(definition);
		field.setValue(value);
		TextFieldDisplay display = new TextFieldDisplay(NodeBuilder.buildForm(null, null, false), field);
		display.inject(null);
		return display;
	}

	private TextFieldDefinition definition(final int minLength, final int maxLength, final TextFieldDefinition.EditionDefinition.Mode mode) {
		final TextFieldDefinition definition = mock(TextFieldDefinition.class);
		final TextFieldDefinition.LengthDefinition lengthDefinition = getLengthDefinition(minLength, maxLength);
		when(definition.getLength()).thenReturn(lengthDefinition);
		final TextFieldDefinition.EditionDefinition edition = getEditionDefinition(mode);
		when(definition.getEdition()).thenReturn(edition);
		return definition;
	}

	private TextFieldDefinition.LengthDefinition getLengthDefinition(int minLength, int maxLength) {
		final TextFieldDefinition.LengthDefinition lengthDefinition = mock(TextFieldDefinition.LengthDefinition.class);
		when(lengthDefinition.getMin()).thenReturn(minLength);
		when(lengthDefinition.getMax()).thenReturn(maxLength);
		return lengthDefinition;
	}

	private TextFieldDefinition.EditionDefinition getEditionDefinition(TextFieldDefinition.EditionDefinition.Mode mode) {
		TextFieldDefinition.EditionDefinition edition = mock(TextFieldDefinition.EditionDefinition.class);
		when(edition.getMode()).thenReturn(mode);
		return edition;
	}
}
