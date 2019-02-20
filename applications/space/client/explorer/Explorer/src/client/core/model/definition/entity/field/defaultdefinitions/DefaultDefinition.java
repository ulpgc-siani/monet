package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.system.MonetList;

class DefaultDefinition implements FieldDefinition {

    @Override
    public boolean is(String key) {
        return key.equals(getCode()) || key.equals(getName());
    }

    @Override
    public String getLabel() {
        return "Label";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public Instance.ClassName getClassName() {
        return FieldDefinition.CLASS_NAME;
    }

    @Override
    public Template getTemplate() {
        return Template.INLINE;
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
        return new MonetList<>();
    }

    @Override
    public Display getDisplay(Display.When when) {
        return null;
    }

    @Override
    public String getCode() {
        return "Code";
    }

    @Override
    public String getName() {
        return "Name";
    }

}
