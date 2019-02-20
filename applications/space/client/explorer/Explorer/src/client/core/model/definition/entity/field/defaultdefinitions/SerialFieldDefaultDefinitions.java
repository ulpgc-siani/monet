package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.definition.entity.field.SerialFieldDefinition;

class SerialFieldDefaultDefinitions {

    public static SerialFieldDefinition singleDefinition() {
        return new SerialFieldDefaultDefinition();
    }

    public static SerialFieldDefinition multipleDefinition() {
        return new MultipleSerialFieldDefaultDefinition();
    }

    private static class SerialFieldDefaultDefinition extends DefaultMultipleableDefinition implements SerialFieldDefinition {

        @Override
        public SerialDefinition getSerial() {
            return new SerialDefinition() {
                @Override
                public String getFormat() {
                    return "";
                }

                @Override
                public String getName() {
                    return "Name";
                }
            };
        }

        @Override
        public boolean isMultiple() {
            return false;
        }
    }

    private static class MultipleSerialFieldDefaultDefinition extends SerialFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
