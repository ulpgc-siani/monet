package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.system.MonetList;

public class NumberFieldDefinitionBuilder {

    public static NumberFieldDefinition build() {
        return new NumberFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public String getFormat() {
                return "##.## €";
            }

            @Override
            public RangeDefinition getRange() {
                return new RangeDefinition() {
                    @Override
                    public long getMin() {
                        return 0;
                    }

                    @Override
                    public long getMax() {
                        return 30;
                    }
                };
            }

            @Override
            public Edition getEdition() {
                return Edition.SLIDER;
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
                return "Campo numerico";
            }

            @Override
            public String getDescription() {
                return "Campo numerico";
            }

            @Override
            public Instance.ClassName getClassName() {
                return NumberFieldDefinition.CLASS_NAME;
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
                            return "23";
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
                    if (display.getWhen() == when) return display;
                }
                return null;
            }

            @Override
            public String getCode() {
                return "NumberField";
            }

            @Override
            public String getName() {
                return "Campo numerico";
            }
        };
    }

    public static NumberFieldDefinition buildMultiple() {
        return new NumberFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public String getFormat() {
                return "#.## €";
            }

            @Override
            public RangeDefinition getRange() {
                return new RangeDefinition() {
                    @Override
                    public long getMin() {
                        return 0;
                    }

                    @Override
                    public long getMax() {
                        return 30;
                    }
                };
            }

            @Override
            public Edition getEdition() {
                return Edition.BUTTON;
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
                return "Campo numerico multiple";
            }

            @Override
            public String getDescription() {
                return "Campo numerico";
            }

            @Override
            public Instance.ClassName getClassName() {
                return NumberFieldDefinition.CLASS_NAME;
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
                                return "23";
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
                    if (display.getWhen() == when) return display;
                }
                return null;
            }

            @Override
            public String getCode() {
                return "MultipleNumberField";
            }

            @Override
            public String getName() {
                return "Campo numerico multiple";
            }
        };
    }
}
