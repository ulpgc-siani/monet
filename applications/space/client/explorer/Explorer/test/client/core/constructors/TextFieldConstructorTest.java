package client.core.constructors;

import client.core.model.Field;
import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.field.TextFieldDefinition;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextFieldConstructorTest {

    @Test
    public void createSingleAndMultipleTextField() {
        Field singleField = new TextFieldConstructor().construct(definition(Multiplicity.SINGLE));
        Field multipleField = new TextFieldConstructor().construct(definition(Multiplicity.MULTIPLE));
        assertFalse(singleField.isMultiple());
        assertTrue(multipleField.isMultiple());
    }

    private enum Multiplicity {
        SINGLE, MULTIPLE
    }

    private TextFieldDefinition definition(final Multiplicity multiplicity) {
        return new TextFieldDefinition() {
            @Override
            public boolean allowHistory() {
                return false;
            }

            @Override
            public AllowHistoryDefinition getAllowHistory() {
                return null;
            }

            @Override
            public LengthDefinition getLength() {
                return null;
            }

            @Override
            public EditionDefinition getEdition() {
                return null;
            }

            @Override
            public List<PatternDefinition> getPatterns() {
                return null;
            }

            @Override
            public boolean isMultiple() {
                return multiplicity == Multiplicity.MULTIPLE;
            }

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public boolean is(String key) {
                return false;
            }

            @Override
            public String getLabel() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Template getTemplate() {
                return null;
            }

            @Override
            public FieldType getFieldType() {
                return FieldType.NORMAL;
            }

            @Override
            public boolean isCollapsible() {
                return false;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public boolean isReadonly() {
                return false;
            }

            @Override
            public boolean isExtended() {
                return false;
            }

            @Override
            public boolean isSuperField() {
                return false;
            }

            @Override
            public boolean isStatic() {
                return false;
            }

            @Override
            public boolean isUnivocal() {
                return false;
            }

            @Override
            public List<Display> getDisplays() {
                return null;
            }

            @Override
            public Display getDisplay(Display.When when) {
                return null;
            }

            @Override
            public String getCode() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Instance.ClassName getClassName() {
                return null;
            }
        };
    }
}
