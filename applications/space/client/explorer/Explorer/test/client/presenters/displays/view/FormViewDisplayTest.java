package client.presenters.displays.view;

import client.core.model.*;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.field.BooleanFieldDefinition;
import client.core.model.definition.entity.field.SerialFieldDefinition;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.fields.BooleanField;
import client.core.model.fields.SerialField;
import client.core.model.fields.TextField;
import client.core.system.MonetList;
import client.services.Services;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormViewDisplayTest {

    @Test
    public void getFieldsBeforeShowMore() {
        FormViewDisplay display = display();
        assertEquals(1, display.getNonExtendedFields().size());
        assertEquals(0, display.getExtendedFields().size());
    }

    @Test
    public void getFieldsAfterShowMore() {
        FormViewDisplay display = display();
        display.toggleShowMore();
        assertEquals(1, display.getNonExtendedFields().size());
        assertEquals(2, display.getExtendedFields().size());
    }


    private FormViewDisplay display() {
        FormViewDisplay display = new FormViewDisplay(mock(Node.class), formView());
        display.inject(mock(Services.class));
        return display;
    }

    private FormView formView() {
        final FormView formView = mock(FormView.class);
        final List<Field> fields = fields();
        when(formView.getShows()).thenReturn(fields);
        when(formView.getScope()).thenReturn(mock(Form.class));
        return formView;
    }

    private List<Field> fields() {
        final List<Field> fields = new MonetList<>();
        fields.add(textField());
        fields.add(booleanField());
        fields.add(serialField());
        return fields;
    }

    private TextField textField() {
        return (TextField) setUpField(mock(TextField.class), textFieldDefinition(), TextField.CLASS_NAME);
    }

    private BooleanField booleanField() {
        return (BooleanField) setUpField(mock(BooleanField.class), booleanFieldDefinition(), BooleanField.CLASS_NAME);
    }

    private SerialField serialField() {
        return (SerialField) setUpField(mock(SerialField.class), serialFieldDefinition(), SerialField.CLASS_NAME);
    }

    private Field setUpField(Field field, EntityDefinition definition, Instance.ClassName className) {
        when(field.getDefinition()).thenReturn(definition);
        when(field.getClassName()).thenReturn(className);
        return field;
    }

    private TextFieldDefinition textFieldDefinition() {
        final TextFieldDefinition definition = mock(TextFieldDefinition.class);
        when(definition.isExtended()).thenReturn(false);
        return definition;
    }

    private BooleanFieldDefinition booleanFieldDefinition() {
        final BooleanFieldDefinition definition = mock(BooleanFieldDefinition.class);
        when(definition.isExtended()).thenReturn(true);
        return definition;
    }

    private SerialFieldDefinition serialFieldDefinition() {
        SerialFieldDefinition definition = mock(SerialFieldDefinition.class);
        when(definition.isExtended()).thenReturn(true);
        return definition;
    }
}
