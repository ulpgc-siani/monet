package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.List;
import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.system.MonetList;

class SelectFieldDefaultDefinitions {

    public static SelectFieldDefinition singleDefinition() {
        return new SelectFieldDefaultDefinition();
    }

    public static SelectFieldDefinition multipleDefinition() {
        return new MultipleSelectFieldDefaultDefinition();
    }

    private static class SelectFieldDefaultDefinition extends DefaultMultipleableDefinition implements SelectFieldDefinition {

        @Override
        public List<TermDefinition> getTerms() {
            return new MonetList<>();
        }

        @Override
        public String getSource() {
            return "";
        }

        @Override
        public boolean allowHistory() {
            return false;
        }

        @Override
        public SelectFieldDefinition.AllowHistoryDefinition getAllowHistory() {
            return new SelectFieldDefinition.AllowHistoryDefinition() {
                @Override
                public String getDataStore() {
                    return "";
                }
            };
        }

        @Override
        public boolean allowOther() {
            return false;
        }

        @Override
        public boolean allowKey() {
            return false;
        }

        @Override
        public boolean allowSearch() {
            return false;
        }

        @Override
        public SelectDefinition getSelect() {
            return new SelectDefinition() {
                @Override
                public Flatten getFlatten() {
                    return Flatten.NONE;
                }

                @Override
                public int getDepth() {
                    return 0;
                }

                @Override
                public String getContext() {
                    return "";
                }

                @Override
                public Object getRoot() {
                    return "";
                }

                @Override
                public FilterDefinition getFilter() {
                    return new FilterDefinition() {
                        @Override
                        public List<Object> getTags() {
                            return new MonetList<>();
                        }
                    };
                }

                @Override
                public boolean isEmbedded() {
                    return false;
                }
            };
        }

        @Override
        public String getValueForTermOther() {
            return "Other";
        }

        @Override
        public boolean isMultiple() {
            return false;
        }
    }

    private static class MultipleSelectFieldDefaultDefinition extends SelectFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
