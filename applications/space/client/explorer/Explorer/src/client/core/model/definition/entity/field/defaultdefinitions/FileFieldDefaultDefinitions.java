package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.definition.entity.field.FileFieldDefinition;

class FileFieldDefaultDefinitions {

    public static FileFieldDefinition singleDefinition() {
        return new FileFieldDefaultDefinition();
    }

    public static FileFieldDefinition multipleDefinition() {
        return new MultipleFileFieldDefaultDefinition();
    }

    private static class FileFieldDefaultDefinition extends DefaultMultipleableDefinition implements FileFieldDefinition {

        @Override
        public boolean isMultiple() {
            return false;
        }

        @Override
        public long getLimit() {
            return -1;
        }
    }

    private static class MultipleFileFieldDefaultDefinition extends FileFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
