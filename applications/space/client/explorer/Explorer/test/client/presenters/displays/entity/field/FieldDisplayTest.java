package client.presenters.displays.entity.field;

import client.core.FieldBuilder;
import client.core.NodeBuilder;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.fields.TextField;
import client.core.system.MonetList;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FieldDisplayTest {

	@Test
	public void checkRequired() {
		TextFieldDisplay display = display("", definitionWithRequired());
		assertTrue(display.isRequired());

		display = display("Hola Mundo", definitionWithRequired());
		assertFalse(display.isRequired());

		display = display("", definitionWithDefaultValues());
		assertFalse(display.isRequired());
	}

	@Test
	public void checkReadOnly() {
		TextFieldDisplay display = display("Mario Caballero", definitionWithReadOnly());
		display.setValue("Mario");
		assertEquals("Mario Caballero", display.getValue());
	}

	@Test
	public void checkDisplayWhenEmpty() {
		TextFieldDisplay display = display("", definitionWithReadOnly());
		assertTrue(display.getMessageWhenEmpty() != null);

		display = display("Mario Caballero", definitionWithReadOnly());
		assertFalse(display.getMessageWhenEmpty() != null);
	}

	@Test
	public void checkDisplayWhenRequired() {
		TextFieldDisplay display = display("", definitionWithRequired());
		assertTrue(display.getMessageWhenRequired() != null);
	}

	@Test
	public void checkDisplayWhenReadOnly() {
		TextFieldDisplay display = display("", definitionWithReadOnly());
		assertTrue(display.getMessageWhenReadOnly() != null);

		display = display("", definitionWithDefaultValues());
		assertFalse(display.getMessageWhenReadOnly() != null);
	}

	@Test
	public void checkDisplayWhenInvalid() {
		TextFieldDisplay display = display("", definitionWithDefaultValues());
		assertFalse(display.getMessageWhenInvalid() != null);

		display = display("1234567890123456789012345", definitionWithDefaultValues());
		assertTrue(display.getMessageWhenInvalid() != null);
	}


























	// INITIALIZERS

	private TextFieldDisplay display(String value, FieldDefinition definition) {
		TextField field = FieldBuilder.buildText("001", "Campo texto");
		field.setDefinition((TextFieldDefinition) definition);
		field.setValue(value);
		TextFieldDisplay display = new TextFieldDisplay(NodeBuilder.buildForm(null, null, false), field);
		display.inject(null);
		return display;
	}

	private FieldDefinition definitionWithDefaultValues() {
		return definition(false, false);
	}

	private FieldDefinition definitionWithRequired() {
		return definition(true, false);
	}

	private FieldDefinition definitionWithReadOnly() {
		return definition(false, true);
	}

	private TextFieldDefinition definition(final boolean required, final boolean readOnly) {
		final TextFieldDefinition definition = mock(TextFieldDefinition.class);
		when(definition.isRequired()).thenReturn(required);
		when(definition.isReadonly()).thenReturn(readOnly);
		when(definition.getDisplay(any(FieldDefinition.Display.When.class))).thenAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				for (FieldDefinition.Display display : getDisplays()) {
					if (display.getWhen() == invocation.getArguments()[0])
						return display;
				}
				return null;
			}
		});
		final TextFieldDefinition.LengthDefinition lengthDefinition = getLengthDefinition(-1, 20);
		when(definition.getLength()).thenReturn(lengthDefinition);
		return definition;
	}

	private TextFieldDefinition.LengthDefinition getLengthDefinition(int minLength, int maxLength) {
		final TextFieldDefinition.LengthDefinition lengthDefinition = mock(TextFieldDefinition.LengthDefinition.class);
		when(lengthDefinition.getMin()).thenReturn(minLength);
		when(lengthDefinition.getMax()).thenReturn(maxLength);
		return lengthDefinition;
	}

	private List<FieldDefinition.Display> getDisplays() {
		return new MonetList<>(
			new FieldDefinition.Display() {
				@Override
				public String getMessage() {
					return "El formato del campo es nombre - apellidos";
				}

				@Override
				public When getWhen() {
					return When.EMPTY;
				}
			},
			new FieldDefinition.Display() {
				@Override
				public String getMessage() {
					return "El campo tiene un valor incorrecto";
				}

				@Override
				public When getWhen() {
					return When.INVALID;
				}
			},
			new FieldDefinition.Display() {
				@Override
				public String getMessage() {
					return "El campo es de solo lectura";
				}

				@Override
				public When getWhen() {
					return When.READONLY;
				}
			},
			new FieldDefinition.Display() {
				@Override
				public String getMessage() {
					return "El campo es requerido";
				}

				@Override
				public When getWhen() {
					return When.REQUIRED;
				}
			}
		);
	}

}
