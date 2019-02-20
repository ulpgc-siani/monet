package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.system.MonetList;

public class TextFieldDefinitionBuilder {

    public static TextFieldDefinition build() {
        return new TextFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public boolean allowHistory() {
                return true;
            }

            @Override
            public AllowHistoryDefinition getAllowHistory() {
                return new AllowHistoryDefinition() {
                    @Override
                    public String getDataStore() {
                        return "DataStore";
                    }
                };
            }

            @Override
            public LengthDefinition getLength() {
                return new LengthDefinition() {
                    @Override
                    public int getMin() {
                        return 3;
                    }

                    @Override
                    public int getMax() {
                        return 20;
                    }
                };
            }

            @Override
            public EditionDefinition getEdition() {
                return new EditionDefinition() {
                    @Override
                    public Mode getMode() {
                        return Mode.UPPERCASE;
                    }
                };
            }

            @Override
            public List<PatternDefinition> getPatterns() {
                return new MonetList<>();
            }

            @Override
            public String getLabel() {
                return "Campo texto";
            }

            @Override
            public String getDescription() {
                return "Descripcion de un campo de texto";
            }

            @Override
            public Instance.ClassName getClassName() {
                return TextFieldDefinition.CLASS_NAME;
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
                return true;
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
                            return "nombre - apellidos";
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
            public boolean isMultiple() {
                return false;
            }

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public String getCode() {
                return "TextField";
            }

            @Override
            public String getName() {
                return "Campo texto";
            }
        };

    }
    public static TextFieldDefinition buildMultiple() {
        return new TextFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public boolean allowHistory() {
                return true;
            }

            @Override
            public AllowHistoryDefinition getAllowHistory() {
                return new AllowHistoryDefinition() {
                    @Override
                    public String getDataStore() {
                        return "DataStore";
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
                        return Mode.SENTENCE;
                    }
                };
            }

            @Override
            public List<PatternDefinition> getPatterns() {
                return new MonetList<>();
            }

            @Override
            public String getLabel() {
                return "Campo texto multiple";
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Instance.ClassName getClassName() {
                return TextFieldDefinition.CLASS_NAME;
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
                return true;
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
            public boolean isMultiple() {
                return false;
            }

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public String getCode() {
                    return "MultipleTextField";
            }

            @Override
            public String getName() {
                return "Campo texto multiple";
            }
        };
    }
}
