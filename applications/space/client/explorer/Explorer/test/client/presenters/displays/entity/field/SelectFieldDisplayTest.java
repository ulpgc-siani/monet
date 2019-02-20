package client.presenters.displays.entity.field;

import client.core.FieldBuilder;
import client.core.NodeBuilder;
import client.core.TypeBuilder;
import client.core.model.List;
import client.core.model.Source;
import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.model.factory.EntityFactory;
import client.core.model.fields.SelectField;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.core.system.MonetList;
import client.services.Services;
import client.services.SourceService;
import client.services.SpaceService;
import client.services.callback.SourceCallback;
import client.services.callback.TermListCallback;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static client.services.SourceService.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SelectFieldDisplayTest {

    @Test
    public void checkValue() {
        SelectFieldDisplay display = display(TypeBuilder.buildTerm("001", "Ejemplo"), definition());

        Term compareTerm = TypeBuilder.buildTerm("001", "Ejemplo");
        assertEquals(display.getValue().getValue(), compareTerm.getValue());
    }

	@Test
	public void checkFilter() {
		final TermList filterOptions = TypeBuilder.buildTermList();
		SelectFieldDisplay display = display(null, definition());

		display.addHook(createHook(filterOptions));

		display.inject(createServices());
		display.loadOptions("l");

		assertEquals(1, filterOptions.size());
	}

	@Test
	public void whenConditionIsEmptyFilterReturnsAllOptions() {
		final TermList filterOptions = TypeBuilder.buildTermList();
		SelectFieldDisplay display = display(null, definition());

		display.addHook(createHook(filterOptions));

		display.inject(createServices());
		display.loadOptions("");

		assertEquals(3, filterOptions.size());
	}

	@Test
    public void checkDefinitionEmbedded() {
        SelectFieldDisplay display = display(null, definition(true));
        assertTrue(display.isEmbedded());

        display = display(null, definition(false));
        assertFalse(display.isEmbedded());
    }



	


	// INITIALIZERS


	private SelectFieldDisplay.Hook createHook(final TermList filterOptions) {
		final SelectFieldDisplay.Hook hook = mock(SelectFieldDisplay.Hook.class);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				filterOptions.clear();
				for (Term term : ((SelectFieldDisplay.HistoryOptions)invocation.getArguments()[0]).getOptions())
					filterOptions.add(term);
				return null;
			}
		}).when(hook).historyOptions(any(SelectFieldDisplay.HistoryOptions.class));
		return hook;
	}

	private Services createServices() {
		final Services services = mock(Services.class);
		final EntityFactory factory = mock(EntityFactory.class);

		when(factory.createTerm(anyString(), anyString())).thenAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				return createTerm((String) invocation.getArguments()[0], (String) invocation.getArguments()[1]);
			}
		});

		when(factory.createTermList()).thenReturn(new client.core.system.types.TermList());
		final SpaceService space = mock(SpaceService.class);
		when(space.getEntityFactory()).thenReturn(factory);

		final SourceService source = mock(SourceService.class);

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				final Source source = mock(Source.class);
				((SourceCallback) invocation.getArguments()[2]).success(source);
				return null;
			}
		}).when(source).locate(anyString(), anyString(), any(SourceCallback.class));

		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				final TermListCallback callback = (TermListCallback) invocation.getArguments()[4];
				callback.success(TypeBuilder.buildTermList(new Term[]{
						createTerm("001", "Limpieza"),
						createTerm("002", "Vaciado"),
						createTerm("003", "Reparación")
				}));
				return null;
			}
		}).when(source).getTerms(any(Source.class), any(Mode.class), anyInt(), anyInt(), anyString(), anyString(), any(TermListCallback.class));

		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				final TermListCallback callback = (TermListCallback) invocation.getArguments()[5];
				String condition = (String) invocation.getArguments()[2];
				List<Term> result = new MonetList<>();

				if (condition.isEmpty() || condition.substring(0,1).toLowerCase().equals("l")) result.add(createTerm("001", "Limpieza"));
				if (condition.isEmpty() || condition.substring(0,1).toLowerCase().equals("v")) result.add(createTerm("002", "Vaciado"));
				if (condition.isEmpty() || condition.substring(0,1).toLowerCase().equals("r")) result.add(createTerm("003", "Reparación"));

				callback.success(TypeBuilder.buildTermList(result.toArray(new Term[result.size()])));
				return null;
			}
		}).when(source).searchTerms(any(Source.class), any(Mode.class), anyString(), anyInt(), anyInt(), anyString(), anyString(), any(TermListCallback.class));

		when(services.getSpaceService()).thenReturn(space);
		when(services.getSourceService()).thenReturn(source);

		return services;
	}

	private Term createTerm(String value, String label) {
		final Term term = mock(Term.class);
		when(term.getValue()).thenReturn(value);
		when(term.getLabel()).thenReturn(label);
		return term;
	}

	private SelectFieldDisplay display(Term value, SelectFieldDefinition definition) {
		SelectField field = FieldBuilder.buildSelect("001", "Campo select");
		field.setDefinition(definition);
		field.setValue(value);
		return new SelectFieldDisplay(NodeBuilder.buildForm(null, null, false), field);
	}

    private SelectFieldDefinition definition() {
        return definition(false);
    }

	private SelectFieldDefinition definition(final boolean embedded) {
		final SelectFieldDefinition definition = mock(SelectFieldDefinition.class);
		final SelectFieldDefinition.SelectDefinition selectDefinition = mock(SelectFieldDefinition.SelectDefinition.class);
		when(selectDefinition.isEmbedded()).thenReturn(embedded);
		when(definition.getSelect()).thenReturn(selectDefinition);
		when(definition.getSource()).thenReturn("DataStore");
		return definition;
	}

}
