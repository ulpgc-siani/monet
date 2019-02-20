package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.definition.entity.field.BooleanFieldDefinition;

class BooleanFieldDefaultDefinitions {

    public static BooleanFieldDefinition singleDefinition() {
        return new BooleanFieldDefaultDefinition();
    }

    private static class BooleanFieldDefaultDefinition extends DefaultDefinition implements BooleanFieldDefinition {

        @Override
        public Edition getEdition() {
            return Edition.CHECK;
        }
    }
}
