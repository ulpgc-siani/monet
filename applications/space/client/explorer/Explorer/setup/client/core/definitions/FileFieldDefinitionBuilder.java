package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.field.FileFieldDefinition;
import client.core.system.MonetList;

public class FileFieldDefinitionBuilder {

    public static FileFieldDefinition build() {
        return new FileFieldDefinition() {

            @Override
            public long getLimit() {
                return -1;
            }

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
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
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Instance.ClassName getClassName() {
                return FileFieldDefinition.CLASS_NAME;
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
                    new Display() {
                        @Override
                        public String getMessage() {
                            return "fichero";
                        }

                        @Override
                        public When getWhen() {
                            return When.EMPTY;
                        }
                    },
                    new Display() {
                        @Override
                        public String getMessage() {
                            return "El campo tiene un valor incorrecto";
                        }

                        @Override
                        public When getWhen() {
                            return When.INVALID;
                        }
                    },
                    new Display() {
                        @Override
                        public String getMessage() {
                            return "El campo es de solo lectura";
                        }

                        @Override
                        public When getWhen() {
                            return When.READONLY;
                        }
                    },
                    new Display() {
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
                return "FileField";
            }

            @Override
            public String getName() {
                return "Campo file";
            }
        };
    }

    public static FileFieldDefinition buildMultiple() {
        return new FileFieldDefinition() {

            @Override
            public long getLimit() {
                return -1;
            }

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
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
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Instance.ClassName getClassName() {
                return FileFieldDefinition.CLASS_NAME;
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
                        new Display() {
                            @Override
                            public String getMessage() {
                                return "+";
                            }

                            @Override
                            public When getWhen() {
                                return When.EMPTY;
                            }
                        },
                        new Display() {
                            @Override
                            public String getMessage() {
                                return "El campo tiene un valor incorrecto";
                            }

                            @Override
                            public When getWhen() {
                                return When.INVALID;
                            }
                        },
                        new Display() {
                            @Override
                            public String getMessage() {
                                return "El campo es de solo lectura";
                            }

                            @Override
                            public When getWhen() {
                                return When.READONLY;
                            }
                        },
                        new Display() {
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
                return "MultipleFileField";
            }

            @Override
            public String getName() {
                return "Campo file multiple";
            }
        };
    }
}
