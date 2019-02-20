package client.presenters.displays.entity.field;

import client.core.model.*;
import client.core.model.definition.Ref;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.FormDefinition;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.model.fields.MultipleLinkField;
import client.core.model.types.Link;
import client.core.system.MonetList;
import client.presenters.displays.Display;
import client.presenters.displays.LinkFieldIndexDisplay;
import client.presenters.displays.MultipleLinkFieldIndexDisplay;
import client.presenters.displays.SetIndexDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.presenters.operations.ShowNodeViewOperation;
import client.services.callback.IndexCallback;
import client.services.callback.NodeCallback;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MultipleLinkFieldDisplay extends MultipleFieldDisplay<MultipleLinkField, LinkFieldDefinition, Link> implements IsMultipleLinkFieldDisplay {

    public static final Type TYPE = new Type("MultipleLinkFieldDisplay", MultipleFieldDisplay.TYPE);
    private MultipleLinkFieldIndexDisplay linkFieldIndexDisplay;

    public MultipleLinkFieldDisplay(Node node, MultipleLinkField field) {
        super(node, field);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public String getValueAsString() {
        return null;
    }

    @Override
    public void delete(int index) {
        linkFieldIndexDisplay.deactive(getValue(index));
        super.delete(index);
    }

    @Override
    protected void onInjectServices() {
        super.onInjectServices();
        addIndex();
    }

    @Override
    public void addChild(Presenter presenter) {
        super.addChild(presenter);
        if (!(presenter instanceof ViewDisplay)) return;
        propagateServices((ViewDisplay) presenter);
        propagateRootDisplay(presenter);
        notifyEntityLoaded((ViewDisplay) presenter);
    }

    @Override
    public void loadOptions() {
        SetIndexDisplay display = (SetIndexDisplay)getChildOfSameType(SetIndexDisplay.TYPE);
        display.loadPage(0);
    }

    @Override
    public void loadOptions(String condition) {
        SetIndexDisplay display = (SetIndexDisplay)getChildOfSameType(SetIndexDisplay.TYPE);
        display.setCondition(condition);
    }

    @Override
    public void showEntity() {
        services.getNodeService().open(getEntity().getValue().getId(), new NodeCallback() {
            @Override
            public void success(Node node) {
                final ShowNodeViewOperation operation = new ShowNodeViewOperation(context(), node, (NodeView) node.getViews().getDefaultView());
                operation.inject(services);
                operation.execute();
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    private Operation.Context context() {
        return new Operation.Context() {
            @Override
            public MultipleLinkFieldDisplay getCanvas() {
                return MultipleLinkFieldDisplay.this;
            }

            @Override
            public Operation getReferral() {
                return getVisitingDisplayOperation();
            }
        };
    }

    @Override
    public boolean allowEdit() {
        return getDefinition().allowEdit();
    }

    @Override
    public void addHook(LinkFieldDisplay.Hook hook) {
        super.addHook(hook);
    }

    @Override
    public LinkFieldIndexDisplay getLinkFieldIndexDisplay() {
        return linkFieldIndexDisplay;
    }

    @Override
    public Link getLinkByLabel(String label) {
        for (Link link : getAllValues())
            if (link.getLabel().equals(label)) return link;
        return null;
    }

    @Override
    public void addHook(Presenter.Hook hook) {
        super.addHook(hook);
        if (hook instanceof LinkFieldDisplay.Hook && getEntity().getIndex() != null)
            notifyIndexLoaded((LinkFieldDisplay.Hook) hook);
    }

    private void notifyIndexLoaded(LinkFieldDisplay.Hook hook) {
        hook.indexLoaded();
    }

    private void addIndex() {
        if (getEntity().getIndex() == null)
            services.getIndexService().open(getDefinition().getSource().getIndex(), createIndexCallback());
        else
            createLinkFieldIndexDisplay(getEntity().getIndex());
    }

    private IndexCallback createIndexCallback() {
        return new IndexCallback() {
            @Override
            public void success(Index index) {
                getEntity().setIndex(index);
                createLinkFieldIndexDisplay(index);
            }

            @Override
            public void failure(String error) {
                notifyIndexFailure();
            }
        };
    }

    private void createLinkFieldIndexDisplay(Index index) {
        linkFieldIndexDisplay = new MultipleLinkFieldIndexDisplay(getDefinition(), index, new LinkFieldIndexDisplay.Handler() {
            @Override
            public void onActivate(LinkFieldIndexDisplay display, NodeIndexEntry entry) {
                add(entry.getEntity().toLink());
            }
        });

        linkFieldIndexDisplay.inject(services);
        injectFilters(linkFieldIndexDisplay);
        addChild(linkFieldIndexDisplay);

        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                notifyIndexLoaded(hook);
            }
        });
    }

    private void injectFilters(LinkFieldIndexDisplay display) {
        for (LinkFieldDefinition.SourceDefinition.FilterDefinition filterDefinition : getDefinition().getSource().getFilters())
            display.inject(createFilter(filterDefinition));
    }

    private Filter createFilter(LinkFieldDefinition.SourceDefinition.FilterDefinition definition) {
        Filter filter = getEntityFactory().createFilter(definition.getAttribute(), definition.getAttribute());
        filter.setOptions(new MonetList<>(getEntityFactory().createFilterOption(getFilterValue(definition.getValue()), getFilterValue(definition.getValue()))));
        return filter;
    }

    private String getFilterValue(Object value) {
        if (value instanceof String) return (String) value;
        if (!(value instanceof Ref)) return null;

        FieldDefinition fieldDefinition = ((FormDefinition) node.getDefinition()).getField(((Ref) value).getValue());
        return ((Form)node).get(fieldDefinition.getCode()).getValueAsString();
    }

    private void propagateServices(Display display) {
        display.inject(services);
        for (Presenter child : display)
            if (child instanceof client.presenters.displays.Display)
                propagateServices((Display) child);
    }

    private void propagateRootDisplay(Presenter presenter) {
        for (Presenter child : presenter)
            propagateRootDisplay(child);

        presenter.setRootDisplay(getRootDisplay());
    }

    private void notifyEntityLoaded(final ViewDisplay display) {
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.entityLoaded(display);
            }
        });
    }

    private void notifyIndexFailure() {
        Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not get index " + getDefinition().getSource().getIndex());
    }

    public interface Hook extends MultipleFieldDisplay.Hook, LinkFieldDisplay.Hook {
    }
}
