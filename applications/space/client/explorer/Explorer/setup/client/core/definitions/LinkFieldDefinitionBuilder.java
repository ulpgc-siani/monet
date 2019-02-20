package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.system.MonetList;

public class LinkFieldDefinitionBuilder {

    public static LinkFieldDefinition build() {
        return new LinkFieldDefinition() {

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
                return LinkFieldDefinition.CLASS_NAME;
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
            public SourceDefinition getSource() {
                return new SourceDefinition() {
                    @Override
                    public String getIndex() {
                        return "001";
                    }

                    @Override
                    public String getView() {
                        return "001";
                    }

                    @Override
                    public String getCollection() {
                        return null;
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
                                        return new MonetList<>("Título");
                                    }
                                };
                            }

                            @Override
                            public DimensionDefinition getDimension() {
                                return new DimensionDefinition() {
                                    public List<String> getAttributes() {
                                        return new MonetList<>("Categoría", "País");
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
            public AllowHistoryDefinition getAllowHistory() {
                return null;
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

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public String getCode() {
                return "LinkField";
            }

            @Override
            public String getName() {
                return "Campo link";
            }
        };
    }

    public static LinkFieldDefinition buildMultiple() {
        return new LinkFieldDefinition() {

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
                return LinkFieldDefinition.CLASS_NAME;
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
            public SourceDefinition getSource() {
                return new SourceDefinition() {
                    @Override
                    public String getIndex() {
                        return "001";
                    }

                    @Override
                    public String getView() {
                        return "001";
                    }

                    @Override
                    public String getCollection() {
                        return null;
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
                                        return new MonetList<>("Título");
                                    }
                                };
                            }

                            @Override
                            public DimensionDefinition getDimension() {
                                return new DimensionDefinition() {
                                    public List<String> getAttributes() {
                                        return new MonetList<>("Categoría", "País");
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
            public AllowHistoryDefinition getAllowHistory() {
                return null;
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
                return true;
            }

            @Override
            public Boundary getBoundary() {
                return null;
            }

            @Override
            public String getCode() {
                return "MultipleLinkField";
            }

            @Override
            public String getName() {
                return "Campo link multiple";
            }
        };
    }
}
