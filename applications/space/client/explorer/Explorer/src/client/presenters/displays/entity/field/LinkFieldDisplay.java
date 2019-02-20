package client.presenters.displays.entity.field;

import client.core.model.*;
import client.core.model.definition.Ref;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.FormDefinition;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition;
import client.core.model.fields.LinkField;
import client.core.model.types.Link;
import client.core.system.MonetList;
import client.presenters.displays.Display;
import client.presenters.displays.LinkFieldIndexDisplay;
import client.presenters.displays.SetIndexDisplay;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.presenters.operations.ShowNodeViewOperation;
import client.services.callback.IndexCallback;
import client.services.callback.NodeCallback;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LinkFieldDisplay extends FieldDisplay<LinkField, LinkFieldDefinition, Link> implements IsLinkFieldDisplay {

	public static final Type TYPE = new Type("LinkFieldDisplay", FieldDisplay.TYPE);
	private LinkFieldIndexDisplay linkFieldIndexDisplay;

	public LinkFieldDisplay(Node node, LinkField field) {
		super(node, field);
	}

	@Override
	public void removeValue() {
		linkFieldIndexDisplay.deactive();
		super.removeValue();
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
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getValueAsString() {
		return "";
	}

	@Override
	public void loadOptions() {
        ((SetIndexDisplay)getChildOfSameType(SetIndexDisplay.TYPE)).loadPage(0);
	}

	@Override
	public void loadOptions(String condition) {
        ((SetIndexDisplay)getChildOfSameType(SetIndexDisplay.TYPE)).setCondition(condition);
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
            public LinkFieldDisplay getCanvas() {
                return LinkFieldDisplay.this;
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
	protected Link format(Link link) {
		return link;
	}

	@Override
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

	@Override
	public LinkFieldIndexDisplay getLinkFieldIndexDisplay() {
		return linkFieldIndexDisplay;
	}

	@Override
	public void addHook(Presenter.Hook hook) {
		super.addHook(hook);
		if (hook instanceof LinkFieldDisplay.Hook && getEntity().getIndex() != null)
			notifyIndexLoaded((LinkFieldDisplay.Hook) hook);
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
		linkFieldIndexDisplay = new LinkFieldIndexDisplay(getDefinition(), index, new LinkFieldIndexDisplay.Handler() {
			@Override
			public void onActivate(LinkFieldIndexDisplay display, NodeIndexEntry entry) {
				setValue(entry.getEntity().toLink());
			}

			@Override
			public void onSelect(LinkFieldIndexDisplay display, NodeIndexEntry entry) {
			}

			@Override
			public void onDelete(LinkFieldIndexDisplay display, NodeIndexEntry entry) {
			}

			@Override
			public void onUnSelect(LinkFieldIndexDisplay display, NodeIndexEntry entry) {
			}

			@Override
			public void onUnSelectAll(LinkFieldIndexDisplay display) {
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

	private void notifyIndexLoaded(Hook hook) {
		hook.indexLoaded();
	}

    private void notifyEntityLoaded(final ViewDisplay display) {
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.entityLoaded(display);
            }
        });
    }

	private void injectFilters(LinkFieldIndexDisplay display) {
        for (SourceDefinition.FilterDefinition filterDefinition : getDefinition().getSource().getFilters())
			display.inject(createFilter(filterDefinition));
	}

    private Filter createFilter(final SourceDefinition.FilterDefinition definition) {
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

    private void notifyIndexFailure() {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not get index " + getDefinition().getSource().getIndex());
	}

	public interface Hook extends FieldDisplay.Hook {
		void indexLoaded();
        void entityLoaded(ViewDisplay viewDisplay);
	}
}
