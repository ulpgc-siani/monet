package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.definition.entity.field.DateFieldDefinition;

class DateFieldDefaultDefinitions {

    public static DateFieldDefinition singleDefinition() {
        return new DateFieldDefaultDefinition();
    }

    public static DateFieldDefinition multipleDefinition() {
        return new MultipleDateFieldDefaultDefinition();
    }

    private static class DateFieldDefaultDefinition extends DefaultDefinition implements DateFieldDefinition {

        @Override
        public boolean allowLessPrecision() {
            return false;
        }

        @Override
        public Precision getPrecision() {
            return Precision.SECONDS;
        }

        @Override
        public Purpose getPurpose() {
            return Purpose.DISTANT_DATE;
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
        public boolean isMultiple() {
            return false;
        }

        @Override
        public Boundary getBoundary() {
            return null;
        }
    }

    private static class MultipleDateFieldDefaultDefinition extends DateFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
