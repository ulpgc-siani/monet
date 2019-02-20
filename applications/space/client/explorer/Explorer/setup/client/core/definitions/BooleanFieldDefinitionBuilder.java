package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.BooleanFieldDefinition;
import client.core.system.MonetList;

public class BooleanFieldDefinitionBuilder {
    public static BooleanFieldDefinition build() {
        return new BooleanFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public String getLabel() {
                return "Campo booleano";
            }

            @Override
            public String getDescription() {
                return "Descripción";
            }

            @Override
            public Instance.ClassName getClassName() {
                return BooleanFieldDefinition.CLASS_NAME;
            }

            @Override
            public Template getTemplate() {
                return Template.INLINE;
            }

            @Override
            public Edition getEdition() {
                return null;
            }

            @Override
            public FieldType getFieldType() {
                return FieldType.NORMAL;
            }

            @Override
            public boolean isCollapsible() {
                return false;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public boolean isReadonly() {
                return false;
            }

            @Override
            public boolean isExtended() {
                return true;
            }

            @Override
            public boolean isSuperField() {
                return false;
            }

            @Override
            public boolean isStatic() {
                return false;
            }

            @Override
            public boolean isUnivocal() {
                return false;
            }

            @Override
            public List<Display> getDisplays() {
                return new MonetList<>(
                    new FieldDefinition.Display() {
                        @Override
                        public String getMessage() {
                            return "El formato del campo es nombre - apellidos";
                        }

                        @Override
                        public When getWhen() {
                            return When.EMPTY;
                        }
                    },
                    new FieldDefinition.Display() {
                        @Override
                        public String getMessage() {
                            return "El campo tiene un valor incorrecto";
                        }

                        @Override
                        public When getWhen() {
                            return When.INVALID;
                        }
                    },
                    new FieldDefinition.Display() {
                        @Override
                        public String getMessage() {
                            return "El campo es de solo lectura";
                        }

                        @Override
                        public When getWhen() {
                            return When.READONLY;
                        }
                    },
                    new FieldDefinition.Display() {
                        @Override
                        public String getMessage() {
                            return "El campo es requerido";
                        }

                        @Override
                        public When getWhen() {
                            return When.REQUIRED;
                        }
                    }
                );
            }

            @Override
            public Display getDisplay(Display.When when) {
                for (Display display : getDisplays()) {
                    if (display.getWhen() == when)
                        return display;
                }
                return null;
            }

            @Override
            public String getCode() {
                return "BooleanField";
            }

            @Override
            public String getName() {
                return "Campo booleano";
            }
        };
    }

    public static BooleanFieldDefinition buildPoll() {
        return new BooleanFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public String getLabel() {
                return "Campo booleano";
            }

            @Override
            public String getDescription() {
                return "Descripción";
            }

            @Override
            public Instance.ClassName getClassName() {
                return BooleanFieldDefinition.CLASS_NAME;
            }

            @Override
            public Template getTemplate() {
                return Template.INLINE;
            }

            @Override
            public FieldType getFieldType() {
                return FieldType.POLL;
            }

            @Override
            public boolean isCollapsible() {
                return false;
            }

            @Override
            public Edition getEdition() {
                return Edition.OPTION;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public boolean isReadonly() {
                return false;
            }

            @Override
            public boolean isExtended() {
                return false;
            }

            @Override
            public boolean isSuperField() {
                return false;
            }

            @Override
            public boolean isStatic() {
                return false;
            }

            @Override
            public boolean isUnivocal() {
                return false;
            }

            @Override
            public List<Display> getDisplays() {
                return new MonetList<>(
                        new FieldDefinition.Display() {
                            @Override
                            public String getMessage() {
                                return "El formato del campo es nombre - apellidos";
                            }

                            @Override
                            public When getWhen() {
                                return When.EMPTY;
                            }
                        },
                        new FieldDefinition.Display() {
                            @Override
                            public String getMessage() {
                                return "El campo tiene un valor incorrecto";
                            }

                            @Override
                            public When getWhen() {
                                return When.INVALID;
                            }
                        },
                        new FieldDefinition.Display() {
                            @Override
                            public String getMessage() {
                                return "El campo es de solo lectura";
                            }

                            @Override
                            public When getWhen() {
                                return When.READONLY;
                            }
                        },
                        new FieldDefinition.Display() {
                            @Override
                            public String getMessage() {
                                return "El campo es requerido";
                            }

                            @Override
                            public When getWhen() {
                                return When.REQUIRED;
                            }
                        }
                );
            }

            @Override
            public Display getDisplay(Display.When when) {
                for (Display display : getDisplays()) {
                    if (display.getWhen() == when)
                        return display;
                }
                return null;
            }

            @Override
            public String getCode() {
                return "BooleanFieldPoll";
            }

            @Override
            public String getName() {
                return "Campo booleano poll";
            }
        };
    }
}
