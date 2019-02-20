package client.presenters.displays.entity.field;

import client.core.constructors.CompositeConstructor;
import client.core.model.Field;
import client.core.model.List;
import client.core.model.Node;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.model.fields.MultipleCompositeField;
import client.core.model.types.Composite;
import client.core.system.MonetList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import cosmos.presenters.RootDisplay;

import static client.services.TranslatorService.OperationLabel;

public class MultipleCompositeFieldDisplay extends MultipleFieldDisplay<MultipleCompositeField, CompositeFieldDefinition, Composite> implements IsMultipleCompositeFieldDisplay {

    public static final Type TYPE = new Type("MultipleCompositeFieldDisplay", MultipleFieldDisplay.TYPE);
    private Composite currentComposite;
    private boolean showMore = false;

    public MultipleCompositeFieldDisplay(Node node, MultipleCompositeField field) {
        super(node, field);
    }

    @Override
    protected void onInjectServices() {
        super.onInjectServices();
        for (FieldDisplay display : getAll())
            addHandlers(((CompositeFieldDisplay) display).getAll());
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

    @Override
    public void scroll(int position) {
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
        return getShow().getLayout() != null;
    }

    @Override
    public boolean hasLayoutExtended() {
        return false;
    }

    @Override
    public List<FieldDisplay> getNonExtendedDisplays() {
        final List<FieldDisplay> nonExtendedDisplays = new MonetList<>();
        for (FieldDisplay display : getDisplaysForComposite(currentComposite))
            if (!display.isExtended()) nonExtendedDisplays.add(display);
        return nonExtendedDisplays;
    }

    @Override
    public List<FieldDisplay> getExtendedDisplays() {
        final List<FieldDisplay> extendedDisplays = new MonetList<>();
        for (FieldDisplay display : getDisplaysForComposite(currentComposite))
            if (display.isExtended()) extendedDisplays.add(display);
        return extendedDisplays;
    }

    @Override
    protected boolean shouldUpdateValue(Composite oldValue, Composite composite) {
        return true;
    }

    @Override
    public String getShowAllLabel() {
        return services.getTranslatorService().translate(showMore ? OperationLabel.SHOW_LESS_FIELDS : OperationLabel.SHOW_MORE_FIELDS);
    }

    @Override
    public List<FieldDisplay> getDisplaysForComposite(Composite composite) {
        if (composite == null || getValueIndex(composite) < 0) return new MonetList<>();

        List<String> keys = getShow().getFields();
        FieldDisplay[] displays = new FieldDisplay[keys.size()];
        for (FieldDisplay display : ((CompositeFieldDisplay) getAll().get(getValueIndex(composite))).getAll()) {
            if (keys.contains(display.getCode()))
                displays[keys.indexOf(display.getCode())] = display;
        }
        return new MonetList<>(displays);
    }

    @Override
    public void createComposite() {
        if (hasCompositeWithoutValues() || hasMaxNumberOfComposites()) return;
        currentComposite = CompositeConstructor.construct(getDefinition());
        add(currentComposite);

        addHandlers(getDisplaysForComposite(currentComposite));
        notifyChanges();
    }

    @Override
    public boolean isCurrentComposite(Composite composite) {
        return composite.equals(currentComposite);
    }

    @Override
    public boolean hasSelectedComposite() {
        return currentComposite != null;
    }

    @Override
    public void delete(int index) {
        if (isCurrentComposite(getValue(index)))
            currentComposite = null;
        super.delete(index);
    }

    private void addHandlers(List<FieldDisplay> displays) {
        addValueChangeHandlers(displays);
    }

    @Override
    public void setRootDisplay(RootDisplay rootDisplay) {
        super.setRootDisplay(rootDisplay);
        for (FieldDisplay compositeDisplay : getAll())
            for (FieldDisplay display : ((CompositeFieldDisplay) compositeDisplay).getAll())
                display.setRootDisplay(rootDisplay);
    }

    private void addValueChangeHandlers(List<FieldDisplay> displays) {
        for (FieldDisplay display : displays)
            display.addValueChangeHandler(createValueChangeHandler(display));
    }

    private ValueChangeHandler createValueChangeHandler(final FieldDisplay display) {
        return new ValueChangeHandler() {
            @Override
            public void onChange() {
                notifyValueHandlers();
                if (isInSummary((Field) display.getEntity())) notifyCompositeValue();
            }
        };
    }

    @Override
    public void toggleShowMore() {
        showMore = !showMore;
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.show();
            }
        });
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
    public boolean hasMaxNumberOfComposites() {
        return getDefinition().getBoundary() != null && getDefinition().getBoundary().getMax() > 0 && getAllValues().size() == getDefinition().getBoundary().getMax();
    }

    @Override
    public boolean hasCompositeWithoutValues() {
        for (Composite composite : getAllValues())
            if (composite.valuesEmpty()) return true;
        return false;
    }

    @Override
    public List<String> getHeaders() {
        final List<String> headers = new MonetList<>();
        final Composite composite = CompositeConstructor.construct(getDefinition());
        for (String code : getViewDefinition().getShow().getFields())
            headers.add(composite.get(code).getLabel());
        return headers;
    }

    @Override
    public List<String> getValuesToShow(Composite composite) {
        final List<String> values = new MonetList<>();
        for (FieldDisplay fieldDisplay : getDisplaysForComposite(composite))
            if (isInSummary((Field) fieldDisplay.getEntity())) values.add(fieldDisplay.toString());
        return values;
    }

    @Override
    public boolean isSummary() {
        return getSummary() != null;
    }

    @Override
    public void setCurrentComposite(Composite composite) {
        this.currentComposite = composite;
    }

    private boolean isInSummary(Field field) {
        return getSummary() != null && new MonetList<>(getSummary().getFields()).contains(field.getCode());
    }

    private void notifyCompositeValue() {
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.compositeValue(currentComposite);
            }
        });
    }

    private CompositeFieldDefinition.ViewDefinition.Summary getSummary() {
        return getViewDefinition().getSummary();
    }

    private CompositeFieldDefinition.ViewDefinition.Show getShow() {
        return getViewDefinition().getShow();
    }

    private CompositeFieldDefinition.ViewDefinition getViewDefinition() {
        if (getDefinition().getView() == null)
            return getDefaultViewDefinition();
        return getDefinition().getView();
    }

    private CompositeFieldDefinition.ViewDefinition getDefaultViewDefinition() {
        return new CompositeFieldDefinition.ViewDefinition() {
            @Override
            public Summary getSummary() {
                return null;
            }

            @Override
            public Show getShow() {
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
        };
    }

    public interface Hook extends CompositeFieldDisplay.Hook, MultipleFieldDisplay.Hook {
        void compositeValue(Composite composite);
    }
}
