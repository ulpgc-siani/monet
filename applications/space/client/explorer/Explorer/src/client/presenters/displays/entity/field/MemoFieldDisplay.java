package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.MemoFieldDefinition;
import client.core.model.fields.MemoField;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.services.callback.TermListCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

import static client.core.model.definition.entity.field.MemoFieldDefinition.EditionDefinition;

public class MemoFieldDisplay extends FieldDisplay<MemoField, MemoFieldDefinition, String> implements IsMemoFieldDisplay {

	public static final Type TYPE = new Type("MemoFieldDisplay", FieldDisplay.TYPE);

	public MemoFieldDisplay(Node node, MemoField field) {
		super(node, field);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getValueAsString() {
		return getValue();
	}

	@Override
    public boolean allowHistory() {
        return getDefinition().allowHistory();
    }

    @Override
    public void loadHistory() {
		services.getNodeService().getFieldHistory(getEntity(), getAllowHistory().getDataStore(), 0, -1, createCallback(null));
    }

    @Override
    public void loadHistory(String filter) {
		services.getNodeService().searchFieldHistory(getEntity(), getAllowHistory().getDataStore(), filter, 0, -1, createCallback(filter));
    }

	@Override
	public boolean hasValue() {
		return super.hasValue() && !getValue().isEmpty();
	}

    public int getMaxLength() {
	    MemoFieldDefinition.LengthDefinition lengthDefinition = getDefinition().getLength();

	    if (lengthDefinition == null)
		    return -1;

		return lengthDefinition.getMax();
	}

    @Override
    public boolean minLengthWrong() {
        return false;
    }

    @Override
    public void addHook(TextFieldDisplay.Hook hook) {
		super.addHook(hook);
    }

    @Override
	public boolean isRichEditionMode() {
		return getDefinition().getEdition() != null && getDefinition().getEdition().getMode() == EditionDefinition.Mode.RICH;
    }

	@Override
	protected boolean check(String value) {
        return !maxLengthWrong(value);
    }

	@Override
	protected String format(String value) {
		return length(value);
	}

	private String length(String value) {
		if (getMaxLength() == -1) return value;
		if (value.length() <= getMaxLength()) return value;
		return value.substring(0, getMaxLength());
	}

	private boolean maxLengthWrong(String value) {
		return getMaxLength() != -1 && value.length() > getMaxLength();
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
		updateHooks(new Notification<TextFieldDisplay.Hook>() {
			@Override
			public void update(TextFieldDisplay.Hook hook) {
				hook.history(options);
			}
		});
	}

	private void notifyHistoryFailure(String filter) {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load source " + getAllowHistory().getDataStore() + " options for condition " + filter);
		updateHooks(new Notification<TextFieldDisplay.Hook>() {
			@Override
			public void update(TextFieldDisplay.Hook hook) {
				hook.historyFailure();
			}
		});
	}

	private MemoFieldDefinition.AllowHistoryDefinition getAllowHistory() {
		return getDefinition().getAllowHistory();
	}
}
