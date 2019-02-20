package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.fields.MultipleTextField;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.services.callback.TermListCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MultipleTextFieldDisplay extends MultipleFieldDisplay<MultipleTextField, TextFieldDefinition, String> implements IsMultipleTextFieldDisplay {

	public static final Type TYPE = new Type("MultipleTextFieldDisplay", MultipleFieldDisplay.TYPE);

	public MultipleTextFieldDisplay(Node node, MultipleTextField field) {
		super(node, field);
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
    protected String format(String s) {
        return getValue();
    }

    @Override
    public boolean allowHistory() {
        return getDefinition().allowHistory();
    }

    @Override
    public void loadHistory() {
        services.getNodeService().getFieldHistory(getEntity(), getAllowHistory().getDataStore(), 0, TextFieldDisplay.MAX_HISTORY_ITEMS, createCallback(null));
    }

    @Override
    public void loadHistory(final String filter) {
        services.getNodeService().searchFieldHistory(getEntity(), getAllowHistory().getDataStore(), filter, 0, TextFieldDisplay.MAX_HISTORY_ITEMS, createCallback(filter));
    }

    @Override
    protected boolean check(String value) {
        return value == null || !minLengthWrong(value) && !maxLengthWrong(value) && getEntity().metaDataIsValid(value);
    }

    @Override
	public int getMaxLength() {
        TextFieldDefinition.LengthDefinition lengthDefinition = getDefinition().getLength();

        if (lengthDefinition == null)
            return -1;

        return lengthDefinition.getMax();
	}

    @Override
	public boolean minLengthWrong() {
		for (FieldDisplay display : getAll())
			if (((TextFieldDisplay)display).minLengthWrong()) return true;
		return false;
	}

    private boolean minLengthWrong(String value) {
        return getMinLength() != -1 && (value != null && value.length() < getMinLength());
    }

    private boolean maxLengthWrong(String value) {
        return getMaxLength() != -1 && (value != null && value.length() > getMaxLength());
    }

    private int getMinLength() {
        TextFieldDefinition.LengthDefinition lengthDefinition = getDefinition().getLength();

        if (lengthDefinition == null)
            return -1;

        return lengthDefinition.getMin();
    }

    @Override
    public void addHook(TextFieldDisplay.Hook hook) {
        super.addHook(hook);
    }

    private TextFieldDefinition.AllowHistoryDefinition getAllowHistory() {
        return getDefinition().getAllowHistory();
    }

    private TermListCallback createCallback(final String filter) {
        return new TermListCallback() {
            @Override
            public void success(final TermList options) {
                notifyHistory(options);
            }

            @Override
            public void failure(String error) {
                notifyHistoryFailure(filter);
            }
        };
    }

    private void notifyHistory(final TermList options) {
        final TermList unselectedOptions = getEntityFactory().createTermList();
        for (Term option : options)
            if (!getAllValues().contains(option.getLabel()))
                unselectedOptions.add(option);
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.history(unselectedOptions);
            }
        });
    }

    private void notifyHistoryFailure(String filter) {
        Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load source " + getAllowHistory().getDataStore() + " options for condition " + filter);
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.historyFailure();
            }
        });
    }

    public interface Hook extends MultipleFieldDisplay.Hook, TextFieldDisplay.Hook {
    }
}
