package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.system.MonetList;

public class SelectFieldDefinitionBuilder {

    public static SelectFieldDefinition build() {
        return new SelectFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
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
                return SelectFieldDefinition.CLASS_NAME;
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
                            return "Indique un país";
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
            public List<TermDefinition> getTerms() {
                return new MonetList<>();
            }

            @Override
            public String getSource() {
                return "source";
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
            public boolean allowOther() {
                return true;
            }

            @Override
            public boolean allowKey() {
                return true;
            }

            @Override
            public boolean allowSearch() {
                return true;
            }

            @Override
            public SelectDefinition getSelect() {
                return null;
            }

            @Override
            public String getValueForTermOther() {
                return "Other";
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
            public String getCode() {
                return "SelectField";
            }

            @Override
            public String getName() {
                return "Campo name";
            }
        };
    }

    public static SelectFieldDefinition buildMultiple() {
        return new SelectFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
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
                return SelectFieldDefinition.CLASS_NAME;
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
                            return "Indique un país";
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
            public List<TermDefinition> getTerms() {
                return new MonetList<>();
            }

            @Override
            public String getSource() {
                return "source";
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
                return null;
            }

            @Override
            public String getValueForTermOther() {
                return "Other";
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
            public String getCode() {
                return "MultipleSelectField";
            }

            @Override
            public String getName() {
                return "Campo select multiple";
            }
        };
    }

    public static SelectFieldDefinition buildEmbedded() {
        return new SelectFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public String getLabel() {
                return "Campo select embebido";
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Instance.ClassName getClassName() {
                return SelectFieldDefinition.CLASS_NAME;
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
                            return "Indique un país";
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
            public List<TermDefinition> getTerms() {
                return new MonetList<>();
            }

            @Override
            public String getSource() {
                return "source";
            }

            @Override
            public boolean allowHistory() {
                return false;
            }

            @Override
            public AllowHistoryDefinition getAllowHistory() {
                return null;
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
                        return null;
                    }

                    @Override
                    public int getDepth() {
                        return 0;
                    }

                    @Override
                    public String getContext() {
                        return null;
                    }

                    @Override
                    public Object getRoot() {
                        return null;
                    }

                    @Override
                    public FilterDefinition getFilter() {
                        return null;
                    }

                    @Override
                    public boolean isEmbedded() {
                        return true;
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

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public String getCode() {
                return "SelectFieldEmbedded";
            }

            @Override
            public String getName() {
                return "Campo select embebido";
            }
        };
    }

    public static SelectFieldDefinition buildPoll() {
        return buildEmbedded();
    }

    public static SelectFieldDefinition buildMultiplePoll() {
        return new SelectFieldDefinition() {

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
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
                return SelectFieldDefinition.CLASS_NAME;
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
                                return "Indique un país";
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
            public List<TermDefinition> getTerms() {
                return new MonetList<>();
            }

            @Override
            public String getSource() {
                return "source";
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
                return null;
            }

            @Override
            public String getValueForTermOther() {
                return "Other";
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
            public String getCode() {
                return "MultipleSelectField";
            }

            @Override
            public String getName() {
                return "Campo select multiple";
            }
        };
    }
}
