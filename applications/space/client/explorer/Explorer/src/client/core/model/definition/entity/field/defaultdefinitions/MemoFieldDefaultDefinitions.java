package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.definition.entity.field.MemoFieldDefinition;

class MemoFieldDefaultDefinitions {

    public static MemoFieldDefinition singleDefinition() {
        return new MemoFieldDefaultDefinition();
    }

    public static MemoFieldDefinition multipleDefinition() {
        return new MultipleMemoFieldDefaultDefinition();
    }

    private static class MemoFieldDefaultDefinition extends DefaultMultipleableDefinition implements MemoFieldDefinition {

        @Override
        public boolean allowHistory() {
            return false;
        }

        @Override
        public MemoFieldDefinition.AllowHistoryDefinition getAllowHistory() {
            return new MemoFieldDefinition.AllowHistoryDefinition() {
                @Override
                public String getDataStore() {
                    return "";
                }
            };
        }

        @Override
        public LengthDefinition getLength() {
            return new LengthDefinition() {
                @Override
                public int getMax() {
                    return 0;
                }
            };
        }

        @Override
        public EditionDefinition getEdition() {
            return new EditionDefinition() {
                @Override
                public Mode getMode() {
                    return null;
                }
            };
        }

        @Override
        public boolean isMultiple() {
            return false;
        }
    }

    private static class MultipleMemoFieldDefaultDefinition extends MemoFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
