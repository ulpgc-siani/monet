package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.SerialFieldDefinition;
import client.core.system.MonetList;

public class SerialFieldDefinitionBuilder {

    public static SerialFieldDefinition build() {
        return new SerialFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public client.core.model.definition.entity.field.SerialFieldDefinition.SerialDefinition getSerial() {
                return new client.core.model.definition.entity.field.SerialFieldDefinition.SerialDefinition() {
                    @Override
                    public String getFormat() {
                        return "I-NNNN/Y";
                    }

                    @Override
                    public String getName() {
                        return "Serial";
                    }
                };
            }

            @Override
            public boolean isMultiple() {
                return false;
            }

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public String getLabel() {
                return "Campo serial";
            }

            @Override
            public String getDescription() {
                return "Campo serial";
            }

            @Override
            public Instance.ClassName getClassName() {
                return SerialFieldDefinition.CLASS_NAME;
            }

            @Override
            public Template getTemplate() {
                return Template.INLINE;
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
                return true;
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
                            return "I-NNNN/Y";
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
            public FieldDefinition.Display getDisplay(FieldDefinition.Display.When when) {
                for (FieldDefinition.Display display : getDisplays()) {
                    if (display.getWhen() == when)
                        return display;
                }
                return null;
            }

            @Override
            public String getCode() {
                return "SerialField";
            }

            @Override
            public String getName() {
                return "Campo serial";
            }
        };
    }

    public static SerialFieldDefinition buildMultiple() {
        return new SerialFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public client.core.model.definition.entity.field.SerialFieldDefinition.SerialDefinition getSerial() {
                return new client.core.model.definition.entity.field.SerialFieldDefinition.SerialDefinition() {
                    @Override
                    public String getFormat() {
                        return "I-NNNN/Y";
                    }

                    @Override
                    public String getName() {
                        return "Serial";
                    }
                };
            }

            @Override
            public boolean isMultiple() {
                return true;
            }

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public String getLabel() {
                return "Campo serial multiple";
            }

            @Override
            public String getDescription() {
                return "Campo serial multiple";
            }

            @Override
            public Instance.ClassName getClassName() {
                return SerialFieldDefinition.CLASS_NAME;
            }

            @Override
            public Template getTemplate() {
                return Template.INLINE;
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
                return true;
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
                                return "I-NNNN/Y";
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
            public FieldDefinition.Display getDisplay(FieldDefinition.Display.When when) {
                for (FieldDefinition.Display display : getDisplays()) {
                    if (display.getWhen() == when)
                        return display;
                }
                return null;
            }

            @Override
            public String getCode() {
                return "MultipleSerialField";
            }

            @Override
            public String getName() {
                return "Campo serial multiple";
            }
        };
    }
}
