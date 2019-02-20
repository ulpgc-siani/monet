package client.presenters.displays.entity;

import client.core.model.Field;
import client.core.model.Node;
import client.core.model.View;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.defaultdefinitions.DefaultDefinitions;
import client.core.model.factory.TypeFactory;
import client.core.model.fields.*;
import client.core.system.MonetList;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.entity.field.*;
import client.services.NotificationService;
import client.services.TranslatorService.ErrorLabel;
import client.services.callback.VoidCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static client.core.model.Instance.ClassName;
import static client.core.model.definition.entity.FieldDefinition.Display;
import static client.core.model.definition.entity.FieldDefinition.Display.When;
import static client.core.model.definition.entity.FieldDefinition.Template;
import static client.services.TranslatorService.Label;

public abstract class FieldDisplay<FieldType extends Field, Definition extends FieldDefinition, Value> extends EntityDisplay<FieldType, View> implements IsFieldDisplay<Value> {
	protected final Node node;

	public static final Type TYPE = new Type("FieldDisplay", EntityDisplay.TYPE);
    private final List<ValueChangeHandler> valueChangeHandlers;
    private final List<InvalidValueListener> invalidValueListeners;
    private final List<ValidValueListener> validValueListeners;

	public FieldDisplay(Node node, FieldType field) {
        super(field, null);
		this.node = node;
        valueChangeHandlers = new MonetList<>();
        invalidValueListeners = new MonetList<>();
        validValueListeners = new MonetList<>();
	}

    @Override
    protected void onInjectServices() {
        services.getNotificationService().registerListener(new NotificationService.UpdateFieldListener() {
            @Override
            public void notify(String fieldPath, String entityId, Object value) {
                if (node.getId().equals(entityId) && getEntity().getPath().equals(fieldPath))
                    refreshValue(value);
            }
        });
    }

    @Override
    public String getCode() {
		return getEntity().getCode();
	}

    @Override
	public String getLabel() {
		return getEntity().getLabel();
	}

    @Override
    public Value getValue() {
		return getEntity().getValue() == null ? null : format(getField().getValue());
	}

	@Override
	public String getEdition() {
		return "";
	}

    @Override
    public void focus() {
        services.getNodeService().focusNodeField(node, getEntity());
    }

    @Override
    public void blur() {
        services.getNodeService().blurNodeField(node, getEntity());
    }

    @Override
    public void onValidValue(ValidValueListener listener) {
        validValueListeners.add(listener);
    }

    @Override
    public void onInvalidValue(InvalidValueListener listener) {
        invalidValueListeners.add(listener);
    }

    @Override
	public void setValue(final Value value) {
        if (isReadonly()) return;
        if (!check(value))
            processInvalidValue(value);
        else
            saveToServer(value, getField());
    }

    private void processInvalidValue(Value value) {
        saveLocalValue(value);
        notifyInvalidValue();
    }

    private void saveLocalValue(Value value) {
        getField().setValue(value);
        notifyChanges();
    }

    private void saveToServer(Value value, final Field<Definition, Value> field) {
        if (!shouldUpdateValue(field.getValue(), value)) return;
        final Value oldValue = field.getValue();
        field.setValue(value == null ? null : format(value));
        notifyChanges();

        services.getNodeService().saveField(node, field, new VoidCallback() {
            @Override
            public void success(Void object) {
                refresh();
                notifyValidValue();
            }

            @Override
            public void failure(String error) {
                field.setValue(oldValue);
                notifyChanges();
                notifyError(error, ErrorLabel.SAVE_VALUE);
            }
        });
    }

    protected boolean shouldUpdateValue(Value oldValue, Value value) {
        return !hasSameValue(oldValue, value);
    }

    private boolean hasSameValue(Value oldValue, Value value) {
        return oldValue == null && value == null || value != null && value.equals(oldValue);
    }

    protected void refreshValue(Object value) {
        getField().setValue((Value) value);
        refresh();
    }

    public void refresh() {
        notifyValueHandlers();
        notifyChanges();
    }

    @Override
    public String getInvalidValueCause() {
        return "";
    }

    public void addValueChangeHandler(ValueChangeHandler handler) {
        valueChangeHandlers.add(handler);
    }

    @Override
    public boolean hasValue() {
        return getValue() != null;
    }

    @Override
    public void removeValue() {
        setValue(null);
    }

    @Override
    public FieldDefinition.FieldType getFieldType() {
        return getDefinition().getFieldType() == null ? FieldDefinition.FieldType.NORMAL : getDefinition().getFieldType();
    }

    public boolean isRequired() {
        return getDefinition().isRequired() && getField().isNullOrEmpty();
    }

    @Override
    public boolean isExtended() {
        return getDefinition().isExtended();
    }

    @Override
    public boolean isPoll() {
        return getFieldType() == FieldDefinition.FieldType.POLL;
    }

    @Override
	public String getMessageWhenEmpty() {
        if (getDefinition() == null || !getField().isNullOrEmpty()) return null;
		Display display = getDefinition().getDisplay(When.EMPTY);
		return display == null ? null : display.getMessage();
	}

    @Override
	public String getMessageWhenRequired() {
        if (!getDefinition().isRequired()) return null;
		Display display = getDefinition().getDisplay(When.REQUIRED);
        return display == null ? null : display.getMessage();
	}

    @Override
    public boolean isCollapsible() {
        return getDefinition().isCollapsible();
    }

    @Override
    public boolean isReadonly() {
        return getDefinition().isReadonly();
	}

    @Override
    public void showDescription() {
        getMessageDisplay().alertWithTimeout(services.getTranslatorService().translate(Label.DESCRIPTION), getDescription());
    }

    @Override
    public String getDescription() {
        return getDefinition().getDescription();
    }

    @Override
	public String getMessageWhenReadOnly() {
        if (!getDefinition().isReadonly()) return null;
		Display display = getDefinition().getDisplay(When.READONLY);
        return display == null ? null : display.getMessage();
	}

    @Override
	public String getMessageWhenInvalid() {
        if (getDefinition() == null || isValid(getField().getValue())) return null;
		Display display = getDefinition().getDisplay(When.INVALID);
		return display == null ? null : display.getMessage();
	}

    public boolean isValid(Value value) {
        return check(value);
    }

    @Override
	public Type getType() {
		return TYPE;
	}

    @Override
    public boolean equals(Object object) {

	    if (object == null || !(object instanceof FieldDisplay))
	        return false;

	    FieldDisplay<FieldType, Definition, Value> display = (FieldDisplay) object;
	    if (!display.getType().equals(getType()))
		    return false;

        return format(display.getValue()).equals(getValue());
    }

    @Override
    public Template getTemplate() {
        return getDefinition().getTemplate();
    }

    @Override
    public String toString() {
        return !hasValue() ? services.getTranslatorService().translate(Label.NO_VALUE) : getValueAsString();
    }

    @Override
    public TypeFactory getTypeFactory() {
        return services.getSpaceService().getEntityFactory();
    }

    protected Definition getDefinition() {
        return (Definition) (hasDefinition() ?  getEntity().getDefinition() : DefaultDefinitions.getDefaultDefinition(getType()));
    }

    private boolean hasDefinition() {
        return getEntity().getDefinition() != null;
    }

    private Field<Definition, Value> getField() {
        return getEntity();
    }

    protected void notifyChanges() {
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.value();
            }
        });
    }

    protected void notifyValueHandlers() {
        for (ValueChangeHandler valueChangeHandler : valueChangeHandlers)
            valueChangeHandler.onChange();
    }

    protected void notifyValidValue() {
        for (ValidValueListener listener : validValueListeners)
            listener.update();
    }

    protected void notifyInvalidValue() {
        for (InvalidValueListener listener : invalidValueListeners)
            listener.update();
    }

    protected void notifyError(final String error, ErrorLabel errorMessage) {
        getMessageDisplay().showMessage(services.getTranslatorService().translate(ErrorLabel.TITLE), services.getTranslatorService().translate(errorMessage));
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.error(error);
            }
        });
    }

    public static class Builder {

        private final Map<ClassName, FieldDisplayBuilder> fieldBuilders;

        public Builder() {
            fieldBuilders = new HashMap<>();
            addBuilders();
        }

        public FieldDisplay build(Node node, Field field) {
            if (field == null || !fieldBuilders.containsKey(field.getClassName())) return null;
            return fieldBuilders.get(field.getClassName()).build(node, field);
        }

        private void addBuilders() {
            fieldBuilders.put(BooleanField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new BooleanFieldDisplay(node, (BooleanField) field);
                }
            });
            fieldBuilders.put(CheckField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new CheckFieldDisplay(node, (CheckField) field);
                }
            });
            fieldBuilders.put(CompositeField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new CompositeFieldDisplay(node, (CompositeField) field);
                }
            });
            fieldBuilders.put(DateField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new DateFieldDisplay(node, (DateField) field);
                }
            });
            fieldBuilders.put(FileField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new FileFieldDisplay(node, (FileField) field);
                }
            });
            fieldBuilders.put(LinkField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new LinkFieldDisplay(node, (LinkField) field);
                }
            });
            fieldBuilders.put(MemoField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MemoFieldDisplay(node, (MemoField) field);
                }
            });
            fieldBuilders.put(NodeField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new NodeFieldDisplay(node, (NodeField) field);
                }
            });
            fieldBuilders.put(NumberField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new NumberFieldDisplay(node, (NumberField) field);
                }
            });
            fieldBuilders.put(PictureField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new PictureFieldDisplay(node, (PictureField) field);
                }
            });
            fieldBuilders.put(SelectField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new SelectFieldDisplay(node, (SelectField) field);
                }
            });
            fieldBuilders.put(SerialField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new SerialFieldDisplay(node, (SerialField) field);
                }
            });
            fieldBuilders.put(SummationField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new SummationFieldDisplay(node, (SummationField) field);
                }
            });
            fieldBuilders.put(TextField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new TextFieldDisplay(node, (TextField) field);
                }
            });
            fieldBuilders.put(UriField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new UriFieldDisplay(node, (UriField) field);
                }
            });
            fieldBuilders.put(MultipleCompositeField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MultipleCompositeFieldDisplay(node, (MultipleCompositeField) field);
                }
            });
            fieldBuilders.put(MultipleDateField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MultipleDateFieldDisplay(node, (MultipleDateField) field);
                }
            });
            fieldBuilders.put(MultipleFileField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MultipleFileFieldDisplay(node, (MultipleFileField) field);
                }
            });
            fieldBuilders.put(MultipleLinkField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MultipleLinkFieldDisplay(node, (MultipleLinkField) field);
                }
            });
            fieldBuilders.put(MultipleMemoField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MultipleMemoFieldDisplay(node, (MultipleMemoField) field);
                }
            });
            fieldBuilders.put(MultipleNumberField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MultipleNumberFieldDisplay(node, (MultipleNumberField) field);
                }
            });
            fieldBuilders.put(MultiplePictureField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MultiplePictureFieldDisplay(node, (MultiplePictureField) field);
                }
            });
            fieldBuilders.put(MultipleSelectField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MultipleSelectFieldDisplay(node, (MultipleSelectField) field);
                }
            });
            fieldBuilders.put(MultipleSerialField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MultipleSerialFieldDisplay(node, (MultipleSerialField) field);
                }
            });
            fieldBuilders.put(MultipleTextField.CLASS_NAME, new FieldDisplayBuilder() {
                @Override
                public FieldDisplay build(Node node, Field field) {
                    return new MultipleTextFieldDisplay(node, (MultipleTextField) field);
                }
            });

        }

        private interface FieldDisplayBuilder {
            FieldDisplay build(Node node, Field field);
        }
    }

    public abstract String getValueAsString();

	protected boolean check(Value value) {
        return true;
    }

    protected abstract Value format(Value value);

	public interface Hook extends EntityDisplay.Hook {
		void value();
        void error(String error);
	}

    public interface ValueChangeHandler {
        void onChange();
    }
}
