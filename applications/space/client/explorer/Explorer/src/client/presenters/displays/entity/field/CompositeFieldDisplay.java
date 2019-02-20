package client.presenters.displays.entity.field;

import client.core.constructors.CompositeConstructor;
import client.core.model.Field;
import client.core.model.List;
import client.core.model.Node;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.model.fields.CompositeField;
import client.core.model.types.Composite;
import client.core.system.MonetList;
import client.presenters.displays.entity.FieldDisplay;
import cosmos.presenters.RootDisplay;

import static client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition;
import static client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Show;
import static client.services.TranslatorService.OperationLabel;

public class CompositeFieldDisplay extends FieldDisplay<CompositeField, CompositeFieldDefinition, Composite> implements IsCompositeFieldDisplay {

	public static final Type TYPE = new Type("CompositeFieldDisplay", FieldDisplay.TYPE);
	private final List<FieldDisplay> allDisplays = new MonetList<>();
	private boolean showMore = false;

	public CompositeFieldDisplay(Node node, CompositeField field) {
		super(node, field);
	}

	@Override
	protected void onInjectServices() {
        super.onInjectServices();
		allDisplays.addAll(getAllDisplays());
    }

    @Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getValueAsString() {
		return "";
	}

	@Override
	public Composite getValue() {
		return super.getValue() == null ? getEntityFactory().createComposite() : super.getValue();
	}

	public List<FieldDisplay> getAll() {
		return allDisplays;
	}

	@Override
	protected Composite format(Composite fields) {
		return fields;
	}

	@Override
	public void scroll(int position) {
		getLayoutDisplay().scroll(position);
	}

	@Override
	public boolean isExtensible() {
		return getDefinition().isExtensible();
	}

	@Override
	public boolean isConditional() {
		return getDefinition().isConditional();
	}

	@Override
	public boolean getConditioned() {
		return getEntity().getConditioned();
	}

	@Override
	public String getShowLayout() {
		return getShow().getLayout();
	}

	@Override
	public String getShowLayoutExtended() {
		return getShow().getLayoutExtended();
	}

	@Override
	public boolean hasLayout() {
		return getShowLayout() != null;
	}

	@Override
	public boolean hasLayoutExtended() {
		return getShowLayoutExtended() != null;
	}

	@Override
	public List<FieldDisplay> getNonExtendedDisplays() {
		return getDisplays(new Condition() {
			@Override
			public boolean evaluate(FieldDisplay display) {
				return !display.isExtended();
			}
		});
	}

	@Override
	public List<FieldDisplay> getExtendedDisplays() {
		if (!isExtensible())
			return new MonetList<>();
		return getDisplays(new Condition() {
            @Override
            public boolean evaluate(FieldDisplay display) {
                return display.isExtended();
            }
        });
	}

    @Override
    protected boolean shouldUpdateValue(Composite oldValue, Composite composite) {
        return true;
    }

    @Override
	public String getShowAllLabel() {
		return services.getTranslatorService().translate(showMore ? OperationLabel.SHOW_LESS_FIELDS : OperationLabel.SHOW_MORE_FIELDS);
	}

    private List<FieldDisplay> getDisplays(Condition condition) {
		List<String> keys = getShow().getFields();
		FieldDisplay[] displays = new FieldDisplay[keys.size()];
		for (FieldDisplay display : allDisplays) {
			if (!keys.contains(display.getCode()) || !condition.evaluate(display)) continue;
			displays[keys.indexOf(display.getCode())] = display;
		}
		return new MonetList<>(displays);
	}

	private List<FieldDisplay> getAllDisplays() {
		if (!hasValue() || getValue().isEmpty()) createComposite();
		List<FieldDisplay> displays = new MonetList<>();
		List<FieldDefinition> definitions = getDefinition().getFields();
		for (Field field : getValue()) {
			field.setDefinition(definitions.get(displays.size()));
			displays.add(buildDisplay(field));
		}
		return displays;
	}

	@Override
	public void createComposite() {
		setValue(CompositeConstructor.construct(getDefinition()));
	}

	@Override
	public void toggleShowMore() {
		showMore = !showMore;
		notifyShow();
		notifyScroll();
	}

	@Override
	public void toggleCondition() {
		getEntity().setConditioned(!getEntity().getConditioned());
		setValue(getValue());
	}

	@Override
	public boolean getShowAllValue() {
		return showMore;
	}

	@Override
	public String getMessageWhenInvalid() {
		for (FieldDisplay fieldDisplay : allDisplays)
			if (fieldDisplay.getMessageWhenInvalid() != null) return fieldDisplay.getMessageWhenInvalid();
		return super.getMessageWhenInvalid();
	}

	@Override
	public void setRootDisplay(RootDisplay rootDisplay) {
		super.setRootDisplay(rootDisplay);
		for (FieldDisplay display : allDisplays)
			display.setRootDisplay(rootDisplay);
	}

	private FieldDisplay buildDisplay(Field field) {
		FieldDisplay display = new Builder().build(node, field);
		display.setRootDisplay(getRootDisplay());
		display.inject(services);
		display.addValueChangeHandler(new ValueChangeHandler() {
			@Override
			public void onChange() {
				notifyValueHandlers();
			}
		});
		return display;
	}

	private void notifyScroll() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.scroll();
			}
		});
	}

	private Show getShow() {

		if (getViewDefinition() != null)
			return getViewDefinition().getShow();
		return new Show() {
			@Override
			public List<String> getFields() {
				List<String> result = new MonetList<>();
				for (FieldDefinition fieldDefinition : getDefinition().getFields())
					result.add(fieldDefinition.getCode());
				return result;
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

	private ViewDefinition getViewDefinition() {
		return getDefinition().getView();
	}

	private void notifyShow() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.show();
			}
		});
	}

	public interface Hook extends FieldDisplay.Hook {
		void show();
		void scroll();
	}

	protected interface Condition {
		boolean evaluate(FieldDisplay display);
	}
}
