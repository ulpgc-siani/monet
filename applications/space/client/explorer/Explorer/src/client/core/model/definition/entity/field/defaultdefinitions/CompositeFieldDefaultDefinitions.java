package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.system.MonetList;

class CompositeFieldDefaultDefinitions {

    public static CompositeFieldDefinition singleDefinition() {
        return new CompositeFieldDefaultDefinition();
    }

    public static CompositeFieldDefinition multipleDefinition() {
        return new MultipleCompositeFieldDefaultDefinition();
    }

    private static class CompositeFieldDefaultDefinition extends DefaultMultipleableDefinition implements CompositeFieldDefinition {

        public CompositeFieldDefaultDefinition() {
        }

        @Override
        public boolean isExtensible() {
            return false;
        }

        @Override
        public boolean isConditional() {
            return false;
        }

        @Override
        public List<FieldDefinition> getFields() {
            return new MonetList<>();
        }

        @Override
        public FieldDefinition getField(String key) {
            return null;
        }

        @Override
        public ViewDefinition getView() {
            return new ViewDefinition() {
                @Override
                public Summary getSummary() {
                    return new Summary() {
                        @Override
                        public List<String> getFields() {
                            return new MonetList<>();
                        }
                    };
                }

                @Override
                public Show getShow() {
                    return new Show() {
                        @Override
                        public List<String> getFields() {
                            return new MonetList<>();
                        }

                        @Override
                        public String getLayout() {
                            return null;
                        }

                        @Override
                        public String getLayoutExtended() {
                            return null;
                        }
                    };
                }
            };
        }

        @Override
        public boolean isMultiple() {
            return false;
        }

        @Override
        public Instance.ClassName getClassName() {
            return CompositeFieldDefinition.CLASS_NAME;
        }
    }

    private static class MultipleCompositeFieldDefaultDefinition extends CompositeFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
