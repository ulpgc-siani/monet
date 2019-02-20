package client.presenters.displays.entity;

import client.core.model.Field;
import client.core.model.List;
import client.core.model.MultipleField;
import client.core.model.Node;
import client.core.model.definition.entity.MultipleableFieldDefinition;
import client.core.system.MonetList;
import client.presenters.displays.IsMultipleDisplay;
import client.services.NodeService;
import client.services.NotificationService;
import client.services.TranslatorService.ErrorLabel;
import client.services.callback.VoidCallback;

public abstract class MultipleFieldDisplay<FieldType extends MultipleField, Definition extends MultipleableFieldDefinition, Value> extends FieldDisplay<FieldType, Definition, Value> implements IsMultipleDisplay<Value> {

	private final Builder displayBuilder;
	private List<FieldDisplay> displays;

	public static final Type TYPE = new Type("MultipleFieldDisplay", FieldDisplay.TYPE);

	public MultipleFieldDisplay(Node node, FieldType field) {
		super(node, field);
		displays = new MonetList<>();
		displayBuilder = new Builder();
	}

	@Override
	protected void onInjectServices() {
        super.onInjectServices();
		addDisplays();
        services.getNotificationService().registerListener(new NotificationService.AddFieldListener() {
            @Override
            public void notify(String fieldPath, String entityId, Field field, int position) {
                if (node.getId().equals(entityId) && getEntity().getPath().equals(fieldPath))
                    addFieldAtPosition(field, position);
            }
        });
        services.getNotificationService().registerListener(new NotificationService.DeleteFieldListener() {
            @Override
            public void notify(String fieldPath, String entityId, int position) {
                if (node.getId().equals(entityId) && getEntity().getPath().equals(fieldPath))
                    removeFieldAtPosition(position);
            }
        });
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public FieldDisplay get(int index) {
		return displays.get(index);
	}

	@Override
	public Value getValue(int index) {
        return displays.size() <= index ? null : (Value)displays.get(index).getValue();
	}

    @Override
    public int getValueIndex(Value value) {
		for (int i = 0; i < displays.size(); i++)
			if (displays.get(i).getValue().equals(value)) return i;
        return -1;
    }

    @Override
	public List<FieldDisplay> getAll() {
		return displays;
	}

	@Override
	public List<Value> getAllValues() {
		List<Value> result = new MonetList<>();
		for (FieldDisplay display : displays)
			result.add((Value)display.getValue());
		return result;
	}

	@Override
	public void add(Value value) {
        if (hasMaxNumberOfFields()) {
            notifyMaxNumberOfFields();
            return;
        }
		if (!containsValue((Value[])getEntity().getAllValues(), value) && check(value))
			addField(getEntity().createField(value));
		else
			notifyInvalidValue();
	}

    private boolean hasMaxNumberOfFields() {
        return getDefinition().getBoundary() != null && getDefinition().getBoundary().getMax() != -1 && getDefinition().getBoundary().getMax() <= size();
    }

    protected void addField(final Field<Definition, Value> field) {
		addLocalField(field);
		saveToServer(field, services.getNodeService());
	}

	private void saveToServer(final Field<Definition, Value> field, NodeService service) {
		service.addField(node, getEntity(), field, size(), new VoidCallback() {
			@Override
			public void success(Void object) {
                if (hasMinNumberOfFields())
				    notifyValidValue();
			}

			@Override
			public void failure(String error) {
				deleteLocalField(getValueIndex(field.getValue()), get(getValueIndex(field.getValue())));
				notifyError(error, ErrorLabel.ADD_FIELD);
			}
		});
	}

	private void addLocalField(Field field) {
		addLocalField(field, size());
	}

	private void addLocalField(Field field, int position) {
		getEntity().add(position, field);
		addDisplay(position, field);
		notifyValues();
		notifyValueHandlers();
	}

	private boolean containsValue(Value[] allValues, Value value) {
		for (Value childValue : allValues) {
			if (value.equals(childValue))
				return true;
		}
		return false;
	}

    @Override
    public void changeOrder(final Value value, final int newOrder) {
        final int previousOrder = getValueIndex(value);

		changeLocalOrder(previousOrder, newOrder, value);
	    services.getNodeService().changeFieldOrder(node, getEntity(), previousOrder, newOrder, new VoidCallback() {
            @Override
            public void success(Void object) {
            }

            @Override
            public void failure(String error) {
                changeLocalOrder(newOrder, previousOrder, value);
                notifyError(error, ErrorLabel.CHANGE_FIELD_ORDER);
            }
        });
    }

	private void changeLocalOrder(int previousOrder, int newOrder, Value value) {
		deleteValueAtIndex(previousOrder);
		addDisplay(newOrder, value);
		notifyValues();
	}

	@Override
	public void delete(final int index) {
		final FieldDisplay<FieldType, Definition, Value> display = get(index);
		deleteLocalField(index, display);

		services.getNodeService().deleteField(node, getEntity(), index, new VoidCallback() {
			@Override
			public void success(Void object) {
                if (!hasMinNumberOfFields())
                    notifyInvalidValue();
			}

			@Override
			public void failure(String error) {
				addLocalField(display.getEntity(), index);
				notifyError(error, ErrorLabel.DELETE_FIELD);
			}
		});
    }

    private boolean hasMinNumberOfFields() {
        return getDefinition().getBoundary() == null || getDefinition().getBoundary().getMin() == -1 || getDefinition().getBoundary().getMin() <= size();
    }

    private void deleteLocalField(int index, FieldDisplay display) {
		deleteValueAtIndex(index);
		notifyValues();
		notifyValueHandlers();
	}

	@Override
	public int size() {
		return displays.size();
	}

	@Override
	public boolean isEmpty() {
		return getAllValues().isEmpty();
	}

	@Override
	protected Value format(Value value) {
		return value;
	}

	@Override
	public void clear() {
		NodeService service = services.getNodeService();

		service.clearField(node, getEntity(), new VoidCallback() {
			@Override
			public void success(Void object) {
				deleteDisplays();
				notifyValueHandlers();
			}

			@Override
			public void failure(String error) {
				notifyError(error, ErrorLabel.CLEAR_FIELD);
			}
		});
	}

	@Override
	public String getMessageWhenEmpty() {
		if (super.getMessageWhenEmpty() == null || super.getMessageWhenEmpty().isEmpty())
			return "+";
		return "+ (" + super.getMessageWhenEmpty() + ")";
	}

	private void addDisplays() {
		for (Object field : getEntity().getAll())
			addDisplay(createDisplay((Field) field));
	}

	private FieldDisplay createDisplay(Field field) {
		return displayBuilder.build(node, field);
	}

	private void addDisplay(FieldDisplay display) {
		display.inject(services);
		displays.add(display);
	}

	private FieldDisplay addDisplay(int index, Field field) {
		FieldDisplay display = displayBuilder.build(node, field);
		display.inject(services);
		display.setRootDisplay(getRootDisplay());
		displays.add(index, display);
		return display;
	}

	private void addDisplay(int index, Value value) {
		Field field = getEntity().createField(value);
		getEntity().add(index, field);
		addDisplay(index, field);
	}

	private void deleteValueAtIndex(int index) {
		getEntity().delete(index);
		deleteDisplay(displays.get(index));
	}

	private void deleteDisplays() {
		for (FieldDisplay display : displays)
			display.remove();
		displays.clear();
	}

	private void deleteDisplay(FieldDisplay fieldDisplay) {
		displays.remove(fieldDisplay);
		fieldDisplay.remove();
	}

	private void notifyValues() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.values();
			}
		});
	}

    private void notifyMaxNumberOfFields() {
        getMessageDisplay().showMessage("", services.getTranslatorService().translate(ErrorLabel.MAX_NUMBER_OF_FIELDS));
    }

    private void addFieldAtPosition(Field field, int position) {
        field.setDefinition(getDefinition());
        addLocalField(field, position);
    }

    private void removeFieldAtPosition(int position) {
        deleteLocalField(position, get(position));
    }

    public interface Hook extends FieldDisplay.Hook {
		void values();
	}
}
