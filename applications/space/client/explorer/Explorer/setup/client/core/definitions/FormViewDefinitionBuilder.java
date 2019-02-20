package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.Ref;
import client.core.model.definition.views.FormViewDefinition;

public class FormViewDefinitionBuilder {

    public static FormViewDefinition buildLayout() {
        return new FormViewDefinition() {
            @Override
            public ShowDefinition getShow() {
                return new ShowDefinition() {
                    @Override
                    public String getLayout() {
                        return "AAABBB\n" +
                                "CCCCDD\n" +
                                "EEEEEE\n" +
                                "FFFFFF";
                    }

                    @Override
                    public String getLayoutExtended() {
                        return null;
                    }

                    @Override
                    public List<Ref> getFields() {
                        return null;
                    }
                };
            }

            @Override
            public boolean isDefault() {
                return false;
            }

            @Override
            public Design getDesign() {
                return null;
            }

            @Override
            public String getCode() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getLabel() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Instance.ClassName getClassName() {
                return null;
            }
        };
    }
}
