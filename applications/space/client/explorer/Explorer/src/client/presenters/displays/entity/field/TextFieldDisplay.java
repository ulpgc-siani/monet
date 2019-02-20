package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.fields.TextField;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.services.TranslatorService;
import client.services.callback.TermListCallback;
import cosmos.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextFieldDisplay extends FieldDisplay<TextField, TextFieldDefinition, String> implements IsTextFieldDisplay {

	public static final Type TYPE = new Type("TextFieldDisplay", FieldDisplay.TYPE);
    protected static final int MAX_HISTORY_ITEMS = 5;

    private final Map<TextFieldDefinition.EditionDefinition.Mode, Transformation> transformations = new HashMap<>();

	public TextFieldDisplay(Node node, TextField field) {
		super(node, field);
	}

    @Override
    protected void onInjectServices() {
        super.onInjectServices();
        createTransformations();
    }

    @Override
	public Type getType() {
		return TYPE;
	}

    @Override
    public String getValueAsString() {
        return format(getValue());
    }

    @Override
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

	public boolean minLengthWrong() {
		return minLengthWrong(getValue());
	}

	public int getMinLength() {
        return getDefinition().getLength() == null ? -1 : getDefinition().getLength().getMin();
	}

    @Override
    public boolean allowHistory() {
        return getDefinition().allowHistory();
    }

    @Override
    public void loadHistory() {
        services.getNodeService().getFieldHistory(getEntity(), getAllowHistory().getDataStore(), 0, MAX_HISTORY_ITEMS, createCallback(null));
    }

    @Override
    public void loadHistory(final String filter) {
        services.getNodeService().searchFieldHistory(getEntity(), getAllowHistory().getDataStore(), filter, 0, MAX_HISTORY_ITEMS, createCallback(filter));
    }

    public int getMaxLength() {
        return getDefinition().getLength() == null ? -1 : getDefinition().getLength().getMax();
	}

	@Override
	protected boolean check(String value) {
        return value == null || !minLengthWrong(value) && !maxLengthWrong(value) && getEntity().metaDataIsValid(value);
    }

    @Override
    public String getInvalidValueCause() {
        if (minLengthWrong() || maxLengthWrong(getValue()))
            return services.getTranslatorService().translate(TranslatorService.ErrorLabel.TEXT_FIELD_LENGTH);
        return services.getTranslatorService().translate(TranslatorService.ErrorLabel.TEXT_FIELD_PATTERN);
    }

    @Override
    public boolean isValid(String value) {
        return value != null && !minLengthWrong(value) && !maxLengthWrong(value);
    }

    @Override
    protected String format(String value) {
        return mode(length(value));
    }

    @Override
    public boolean hasValue() {
        return super.hasValue() && !getValue().isEmpty();
    }

    private TermListCallback createCallback(final String filter) {
        return new TermListCallback() {
            @Override
            public void success(TermList object) {
                notifyHistory(object);
            }

            @Override
            public void failure(String error) {
                notifyHistoryFailure(filter);
            }
        };
    }

    private void notifyHistory(final TermList options) {
        updateHooks(new Notification<Hook>() {
            @Override
            public void update(Hook hook) {
                hook.history(options);
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

    private TextFieldDefinition.AllowHistoryDefinition getAllowHistory() {
        return getDefinition().getAllowHistory();
    }

    private void createTransformations() {
        transformations.put(TextFieldDefinition.EditionDefinition.Mode.UPPERCASE, new Transformation() {
            @Override
            public String transform(String value) {
                return value.toUpperCase();
            }
        });
        transformations.put(TextFieldDefinition.EditionDefinition.Mode.LOWERCASE, new Transformation() {
            @Override
            public String transform(String value) {
                return value.toLowerCase();
            }
        });
        transformations.put(TextFieldDefinition.EditionDefinition.Mode.TITLE, new Transformation() {
            @Override
            public String transform(String value) {
                return StringUtils.title(value);
            }
        });
        transformations.put(TextFieldDefinition.EditionDefinition.Mode.SENTENCE, new Transformation() {
            @Override
            public String transform(String value) {
                return StringUtils.sentence(value);
            }
        });
    }

	private String length(String value) {
		int length = getMaxLength();

		if (length == -1 || value.length() <= length) return value;

		return value.substring(0, length);
	}

	private String mode(String value) {
		return transformations.containsKey(getMode()) ? transformations.get(getMode()).transform(value) : value;
	}

    private TextFieldDefinition.EditionDefinition.Mode getMode() {
        return getEntity().getMode();
    }

    private boolean minLengthWrong(String value) {
		return getMinLength() != -1 && (value != null && value.length() < getMinLength());
	}

	private boolean maxLengthWrong(String value) {
        return getMaxLength() != -1 && (value != null && value.length() > getMaxLength());
    }

    public interface Hook extends FieldDisplay.Hook {
        void history(TermList options);
        void historyFailure();
	}
	
    private interface Transformation {
        String transform(String value);
    }
}
