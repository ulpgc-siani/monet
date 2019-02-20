package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.List;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.system.MonetList;

class LinkFieldDefaultDefinitions {

    public static LinkFieldDefinition singleDefinition() {
        return new LinkFieldDefaultDefinition();
    }

    public static LinkFieldDefinition multipleDefinition() {
        return new MultipleLinkFieldDefaultDefinition();
    }

    private static class LinkFieldDefaultDefinition extends DefaultMultipleableDefinition implements LinkFieldDefinition {

        @Override
        public SourceDefinition getSource() {
            return new SourceDefinition() {
                @Override
                public String getIndex() {
                    return "";
                }

                @Override
                public String getView() {
                    return "";
                }

                @Override
                public String getCollection() {
                    return "";
                }

                @Override
                public List<FilterDefinition> getFilters() {
                    return new MonetList<>();
                }

                @Override
                public AnalyzeDefinition getAnalyze() {
                    return new AnalyzeDefinition() {
                        @Override
                        public SortingDefinition getSorting() {
                            return new SortingDefinition() {
                                @Override
                                public List<String> getAttributes() {
                                    return new MonetList<>();
                                }
                            };
                        }

                        @Override
                        public DimensionDefinition getDimension() {
                            return new DimensionDefinition() {
                                @Override
                                public List<String> getAttributes() {
                                    return new MonetList<>();
                                }
                            };
                        }
                    };
                }
            };
        }

        @Override
        public boolean allowHistory() {
            return false;
        }

        @Override
        public LinkFieldDefinition.AllowHistoryDefinition getAllowHistory() {
            return new LinkFieldDefinition.AllowHistoryDefinition() {
                @Override
                public String getDataStore() {
                    return "";
                }
            };
        }

        @Override
        public boolean allowSearch() {
            return false;
        }

        @Override
        public boolean allowAdd() {
            return false;
        }

        @Override
        public boolean allowEdit() {
            return false;
        }

        @Override
        public boolean isMultiple() {
            return false;
        }
    }

    private static class MultipleLinkFieldDefaultDefinition extends LinkFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
