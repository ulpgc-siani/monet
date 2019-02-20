package client.core.definitions;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.core.system.MonetList;

public class PictureFieldDefinitionBuilder {

    public static PictureFieldDefinition build() {
        return new PictureFieldDefinition() {

            @Override
            public long getLimit() {
                return -1;
            }

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public String getDefault() {
                return "";
            }

	        @Override
	        public boolean isProfilePhoto() {
		        return false;
	        }

	        @Override
            public SizeDefinition getSize() {
                return new SizeDefinition() {
                    @Override
                    public long getWidth() {
                        return 400;
                    }

                    @Override
                    public long getHeight() {
                        return 250;
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
                return "Campo picture";
            }

            @Override
            public String getDescription() {
                return "Description";
            }

            @Override
            public Instance.ClassName getClassName() {
                return PictureFieldDefinition.CLASS_NAME;
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
                return "PictureField";
            }

            @Override
            public String getName() {
                return "Campo picture";
            }
        };
    }

    public static PictureFieldDefinition buildMultiple() {
        return new PictureFieldDefinition() {

            @Override
            public long getLimit() {
                return -1;
            }

            @Override
            public boolean is(String key) {
                return key.equals(getCode()) || key.equals(getName());
            }

            @Override
            public String getDefault() {
                return "";
            }

	        @Override
	        public boolean isProfilePhoto() {
		        return false;
	        }

	        @Override
            public SizeDefinition getSize() {
                return new SizeDefinition() {
                    @Override
                    public long getWidth() {
                        return 400;
                    }

                    @Override
                    public long getHeight() {
                        return 250;
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
                return "Campo picture";
            }

            @Override
            public String getDescription() {
                return "Description";
            }

            @Override
            public Instance.ClassName getClassName() {
                return PictureFieldDefinition.CLASS_NAME;
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
                return "MultiplePictureField";
            }

            @Override
            public String getName() {
                return "Campo picture multiple";
            }
        };
    }
}
