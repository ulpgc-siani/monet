package client.core.system.fields;

import client.core.model.List;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.system.MonetList;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TextFieldTest {

    @Test
    public void metaDataFromFieldWithoutPatternsIsAlwaysValid() {
        TextField field = textField(null);
        field.setDefinition(definitionWithPatterns());
        assertTrue(field.metaDataIsValid("ruben"));
        assertTrue(field.metaDataIsValid("ruben.diaz@siani.es"));
    }

    @Test
    public void validateValueWithPattern() {
        TextField field = textField(null);
        field.setDefinition(definitionWithPatterns("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"));
        assertFalse(field.metaDataIsValid("ruben"));
        assertTrue(field.metaDataIsValid("ruben.diaz@siani.es"));
    }

    @Test
    public void checkSinglePattern() {
        TextField field = textField("Mario caballero ramírez");
        field.setDefinition(definitionWithPatterns("([^ ]*) ([^ ]*)"));

        assertThat(field.getMeta("nombre"), is("Mario"));
        assertThat(field.getMeta("apellidos"), is("caballero"));
    }

    @Test
    public void checkMultiplePattern() {
        TextField field1 = textField("Mario caballero - ramírez");
        field1.setDefinition(definitionWithPatterns("([^-]*)-(.*)", "([^+]*)+(.*)"));
        assertThat(field1.getMeta("nombre"), is("Mario caballero "));

        TextField field2 = textField("Mario + caballero ramírez");
        field2.setDefinition(definitionWithPatterns("([^-]*)-(.*)", "([^\\+]*)\\+(.*)"));
        assertThat(field2.getMeta("nombre"), is("Mario "));
    }

    @Test
    public void checkMultiplePatternWithCollisions() {
        TextField field1 = textField("Mario caballero - ramírez");
        field1.setDefinition(definitionWithPatterns("([^-]*) (.*)", "(.*) (.*)"));
        assertThat(field1.getMeta("nombre"), is("Mario caballero"));

        TextField field2 = textField("Mario caballero ramírez");
        field2.setDefinition(definitionWithPatterns("([^ ]*) (.*)", "(.*) (.*)"));
        assertThat(field2.getMeta("nombre"), is("Mario"));

        TextField field3 = textField("Mario caballero ramírez");
        field3.setDefinition(definitionWithPatterns("(.*) (.*)", "([^ ]*) (.*)"));
        assertThat(field3.getMeta("nombre"), is("Mario caballero"));
    }

    private TextField textField(String value) {
        TextField field1 = new TextField("code", "label");
        field1.setValue(value);
        return field1;
    }

    private TextFieldDefinition definitionWithPatterns(String... patterns) {
        final TextFieldDefinition definition = mock(TextFieldDefinition.class);
        when(definition.getPatterns()).thenReturn(getPatterns(patterns));
        return definition;
    }

    private List<TextFieldDefinition.PatternDefinition> getPatterns(String[] regExps) {
        List<TextFieldDefinition.PatternDefinition> result = new MonetList<>();

        for (final String regExp : regExps) {
            TextFieldDefinition.PatternDefinition patternDefinition = new TextFieldDefinition.PatternDefinition() {
                @Override
                public String getRegExp() {
                    return regExp;
                }

                @Override
                public List<MetaDefinition> getMetaList() {
                    return new MonetList<>(metaDefinition(0, "nombre"), metaDefinition(1, "apellidos"));
                }
            };
            result.add(patternDefinition);
        }

        return result;
    }

    private TextFieldDefinition.PatternDefinition.MetaDefinition metaDefinition(final int position, final String indicator) {
        return new TextFieldDefinition.PatternDefinition.MetaDefinition() {
            @Override
            public long getPosition() {
                return position;
            }

            @Override
            public String getIndicator() {
                return indicator;
            }
        };
    }
}
