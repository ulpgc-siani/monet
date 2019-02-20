package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.definition.entity.FieldDefinition;
import client.presenters.displays.entity.field.*;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

public class DefaultDefinitions {

    private static final Map<Presenter.Type, FieldDefinition> definitions = new HashMap<Presenter.Type, FieldDefinition>(){{
        put(BooleanFieldDisplay.TYPE, BooleanFieldDefaultDefinitions.singleDefinition());
        put(CompositeFieldDisplay.TYPE, CompositeFieldDefaultDefinitions.singleDefinition());
        put(MultipleCompositeFieldDisplay.TYPE, CompositeFieldDefaultDefinitions.multipleDefinition());
        put(DateFieldDisplay.TYPE, DateFieldDefaultDefinitions.singleDefinition());
        put(MultipleDateFieldDisplay.TYPE, DateFieldDefaultDefinitions.multipleDefinition());
        put(FileFieldDisplay.TYPE, FileFieldDefaultDefinitions.singleDefinition());
        put(MultipleFileFieldDisplay.TYPE, FileFieldDefaultDefinitions.multipleDefinition());
        put(LinkFieldDisplay.TYPE, LinkFieldDefaultDefinitions.singleDefinition());
        put(MultipleLinkFieldDisplay.TYPE, LinkFieldDefaultDefinitions.multipleDefinition());
        put(MemoFieldDisplay.TYPE, MemoFieldDefaultDefinitions.singleDefinition());
        put(MultipleMemoFieldDisplay.TYPE, MemoFieldDefaultDefinitions.multipleDefinition());
        put(NumberFieldDisplay.TYPE, NumberFieldDefaultDefinitions.singleDefinition());
        put(MultipleNumberFieldDisplay.TYPE, NumberFieldDefaultDefinitions.multipleDefinition());
        put(PictureFieldDisplay.TYPE, PictureFieldDefaultDefinitions.singleDefinition());
        put(MultiplePictureFieldDisplay.TYPE, PictureFieldDefaultDefinitions.multipleDefinition());
        put(SelectFieldDisplay.TYPE, SelectFieldDefaultDefinitions.singleDefinition());
        put(MultipleSelectFieldDisplay.TYPE, SelectFieldDefaultDefinitions.multipleDefinition());
        put(SerialFieldDisplay.TYPE, SerialFieldDefaultDefinitions.singleDefinition());
        put(MultipleSerialFieldDisplay.TYPE, SerialFieldDefaultDefinitions.multipleDefinition());
        put(TextFieldDisplay.TYPE, TextFieldDefaultDefinitions.singleDefinition());
        put(MultipleTextFieldDisplay.TYPE, TextFieldDefaultDefinitions.multipleDefinition());
        put(UriFieldDisplay.TYPE, UriFieldDefaultDefinitions.singleDefinition());
        //put(MultipleUriFieldDisplay.TYPE, UriFieldDefaultDefinitions.multipleDefinition());
    }};

    public static FieldDefinition getDefaultDefinition(Presenter.Type type) {
        return definitions.get(type);
    }
}
