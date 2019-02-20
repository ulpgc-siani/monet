package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.field.SummationFieldDefinition;
import client.core.system.MonetList;

public class SummationFieldDefinitionBuilder {

    public static SummationFieldDefinition build() {
        return new SummationFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public List<SummationItemDefinition> getTerms() {
                return new MonetList<>();
            }

            @Override
            public String getSource() {
                return null;
            }

            @Override
            public SelectDefinition getSelect() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public RangeDefinition getRange() {
                return null;
            }

            @Override
            public boolean isMultiple() {
                return false;
            }

            @Override
            public Boundary getBoundary() {
                return null;
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
            public Instance.ClassName getClassName() {
                return SummationFieldDefinition.CLASS_NAME;
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
                return "SummationField";
            }

            @Override
            public String getName() {
                return "Campo summation";
            }
        };
    }
}
