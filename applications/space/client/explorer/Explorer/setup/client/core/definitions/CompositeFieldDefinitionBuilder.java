package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.system.MonetList;

public class CompositeFieldDefinitionBuilder {

    public static CompositeFieldDefinition build() {
        return new CompositeFieldDefinition() {
            @Override
            public boolean isExtensible() {
                return true;
            }

            @Override
            public boolean isConditional() {
                return true;
            }

            @Override
            public List<FieldDefinition> getFields() {
                return new MonetList<>(
                        TextFieldDefinitionBuilder.build(),
                        BooleanFieldDefinitionBuilder.build(),
                        NumberFieldDefinitionBuilder.build()
                );
            }

            @Override
            public FieldDefinition getField(String key) {
                for (FieldDefinition fieldDefinition : getFields()) {
                    if (fieldDefinition.is(key))
                        return fieldDefinition;
                }
                return null;
            }

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public ViewDefinition getView() {
                return new ViewDefinition() {
                    @Override
                    public Summary getSummary() {
                        return new Summary() {
                            @Override
                            public List<String> getFields() {
                                return new MonetList<>("TextField", "BooleanField", "NumberField");
                            }
                        };
                    }

                    @Override
                    public Show getShow() {
                        return new Show() {
                            @Override
                            public List<String> getFields() {
                                return new MonetList<>("TextField", "BooleanField", "NumberField");
                            }

                            @Override
                            public String getLayout() {
                                return "AAAA";
                            }

                            @Override
                            public String getLayoutExtended() {
                                return "BBCC";
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
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public String getLabel() {
                return "Campo composite";
            }

            @Override
            public String getDescription() {
                return "Campo composite";
            }

            @Override
            public Instance.ClassName getClassName() {
                return CompositeFieldDefinition.CLASS_NAME;
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
                for (Display display : getDisplays())
                    if (display.getWhen() == when) return display;
                return null;
            }

            @Override
            public String getCode() {
                return "CompositeField";
            }

            @Override
            public String getName() {
                return "Campo Composite";
            }
        };
    }

    public static CompositeFieldDefinition buildSummary() {
        return new CompositeFieldDefinition() {
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
                return new MonetList<>(
                        TextFieldDefinitionBuilder.build(),
                        BooleanFieldDefinitionBuilder.build(),
                        SerialFieldDefinitionBuilder.build()
                );
            }

            @Override
            public FieldDefinition getField(String key) {
                for (FieldDefinition fieldDefinition : getFields()) {
                    if (fieldDefinition.getCode().equals(key) || fieldDefinition.getName().equals(key))
                        return fieldDefinition;
                }
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
                                return new MonetList<>("TextField", "BooleanField", "SerialField");
                            }
                        };
                    }

                    @Override
                    public Show getShow() {
                        return new Show() {
                            @Override
                            public List<String> getFields() {
                                return new MonetList<>("TextField", "BooleanField", "SerialField");
                            }

                            @Override
                            public String getLayout() {
                                return "A\nB";
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
                return true;
            }

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public String getLabel() {
                return "Campo composite multiple";
            }

            @Override
            public String getDescription() {
                return "Campo composite multiple";
            }

            @Override
            public Instance.ClassName getClassName() {
                return CompositeFieldDefinition.CLASS_NAME;
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
                });
            }

            @Override
            public Display getDisplay(Display.When when) {
                for (Display display : getDisplays())
                    if (display.getWhen() == when) return display;
                return null;
            }

            @Override
            public String getCode() {
                return "SummaryCompositeField";
            }

            @Override
            public String getName() {
                return "Campo composite summary";
            }
        };
    }

    public static CompositeFieldDefinition buildTable() {
        return new CompositeFieldDefinition() {
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
                return new MonetList<>(
                        TextFieldDefinitionBuilder.build(),
                        BooleanFieldDefinitionBuilder.build(),
                        DateFieldDefinitionBuilder.build()
                );
            }

            @Override
            public FieldDefinition getField(String key) {
                for (FieldDefinition fieldDefinition : getFields()) {
                    if (fieldDefinition.getCode().equals(key) || fieldDefinition.getName().equals(key))
                        return fieldDefinition;
                }
                return null;
            }

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public ViewDefinition getView() {
                return new ViewDefinition() {
                    @Override
                    public Summary getSummary() {
                        return null;
                    }

                    @Override
                    public Show getShow() {
                        return new Show() {
                            @Override
                            public List<String> getFields() {
                                return new MonetList<>("TextField", "BooleanField", "DateField");
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
                return true;
            }

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public String getLabel() {
                return "Campo composite multiple";
            }

            @Override
            public String getDescription() {
                return "Campo composite multiple";
            }

            @Override
            public Instance.ClassName getClassName() {
                return CompositeFieldDefinition.CLASS_NAME;
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
                });
            }

            @Override
            public Display getDisplay(Display.When when) {
                for (Display display : getDisplays())
                    if (display.getWhen() == when) return display;
                return null;
            }

            @Override
            public String getCode() {
                return "TableCompositeField";
            }

            @Override
            public String getName() {
                return "Campo Composite Tabla";
            }
        };
    }
}
