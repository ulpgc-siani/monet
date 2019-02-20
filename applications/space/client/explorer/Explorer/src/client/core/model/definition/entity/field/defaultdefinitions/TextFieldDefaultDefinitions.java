package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.List;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.system.MonetList;

class TextFieldDefaultDefinitions {

    public static TextFieldDefinition singleDefinition() {
        return new TextFieldDefaultDefinition();
    }

    public static TextFieldDefinition multipleDefinition() {
        return new MultipleTextFieldDefaultDefinition();
    }

    private static class TextFieldDefaultDefinition extends DefaultMultipleableDefinition implements TextFieldDefinition {

        @Override
        public boolean allowHistory() {
            return false;
        }

        @Override
        public TextFieldDefinition.AllowHistoryDefinition getAllowHistory() {
            return new TextFieldDefinition.AllowHistoryDefinition() {
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
                public int getMin() {
                    return -1;
                }

                @Override
                public int getMax() {
                    return -1;
                }
            };
        }

        @Override
        public EditionDefinition getEdition() {
            return new EditionDefinition() {
                @Override
                public Mode getMode() {
                    return Mode.LOWERCASE;
                }
            };
        }

        @Override
        public List<PatternDefinition> getPatterns() {
            return new MonetList<>();
        }

        @Override
        public boolean isMultiple() {
            return false;
        }
    }

    private static class MultipleTextFieldDefaultDefinition extends TextFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
