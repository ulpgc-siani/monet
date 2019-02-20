package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.MemoFieldDefinition;
import client.core.model.fields.MultipleMemoField;
import client.core.model.types.TermList;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.services.callback.TermListCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

import static client.core.model.definition.entity.field.MemoFieldDefinition.EditionDefinition;

public class MultipleMemoFieldDisplay extends MultipleFieldDisplay<MultipleMemoField, MemoFieldDefinition, String> implements IsMultipleMemoFieldDisplay {

    public static final Type TYPE = new Type("MultipleMemoFieldDisplay", MultipleFieldDisplay.TYPE);

    public MultipleMemoFieldDisplay(Node node, MultipleMemoField field) {
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
    public boolean isRichEditionMode() {
        return getDefinition().getEdition() != null && getDefinition().getEdition().getMode() == EditionDefinition.Mode.RICH;
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
