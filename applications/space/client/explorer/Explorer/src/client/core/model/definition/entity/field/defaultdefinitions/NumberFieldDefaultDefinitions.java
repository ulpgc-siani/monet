package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.definition.entity.field.NumberFieldDefinition;

class NumberFieldDefaultDefinitions {

    public static NumberFieldDefinition singleDefinition() {
        return new NumberFieldDefaultDefinition();
    }

    public static NumberFieldDefinition multipleDefinition() {
        return new MultipleNumberFieldDefaultDefinition();
    }

    private static class NumberFieldDefaultDefinition extends DefaultMultipleableDefinition implements NumberFieldDefinition {

        @Override
        public String getFormat() {
            return "";
        }

        @Override
        public RangeDefinition getRange() {
            return new RangeDefinition() {
                @Override
                public long getMin() {
                    return -1;
                }

                @Override
                public long getMax() {
                    return -1;
                }
            };
        }

        @Override
        public Edition getEdition() {
            return Edition.BUTTON;
        }

        @Override
        public boolean isMultiple() {
            return false;
        }
    }

    private static class MultipleNumberFieldDefaultDefinition extends NumberFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
