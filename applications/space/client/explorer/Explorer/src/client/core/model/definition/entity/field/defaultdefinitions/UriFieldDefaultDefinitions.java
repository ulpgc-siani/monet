package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.definition.entity.field.UriFieldDefinition;

class UriFieldDefaultDefinitions {

    public static UriFieldDefinition singleDefinition() {
        return new UriFieldDefaultDefinition();
    }

    public static UriFieldDefinition multipleDefinition() {
        return new MultipleUriFieldDefaultDefinition();
    }

    private static class UriFieldDefaultDefinition extends DefaultMultipleableDefinition implements UriFieldDefinition {

        @Override
        public boolean isMultiple() {
            return false;
        }
    }

    private static class MultipleUriFieldDefaultDefinition extends UriFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
