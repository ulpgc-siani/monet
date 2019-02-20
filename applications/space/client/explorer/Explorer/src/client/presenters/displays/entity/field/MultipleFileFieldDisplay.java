package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.FileFieldDefinition;
import client.core.model.fields.MultipleFileField;
import client.core.model.types.File;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.services.TranslatorService;
import client.services.callback.Callback;

public class MultipleFileFieldDisplay extends MultipleFieldDisplay<MultipleFileField, FileFieldDefinition, File> implements IsMultipleFileFieldDisplay {

    public static final Type TYPE = new Type("MultipleFileFieldDisplay", MultipleFieldDisplay.TYPE);

    public MultipleFileFieldDisplay(Node node, MultipleFileField field) {
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
    public String getFileLabel() {
        return fileHasLabel() ? getValue().getLabel() : getValue().getId().substring(getValue().getId().lastIndexOf("/") + 1);
    }

    @Override
    public long getLimit() {
        return megaBytesToBytes();
    }

    private long megaBytesToBytes() {
        return getDefinition().getLimit() * 1024 * 1024;
    }

    @Override
    public String getFileId() {
        return services.getNodeService().getDownloadNodeFileUrl(node, getValue().getId());
    }

    @Override
    public String getFileId(File file) {
        return services.getNodeService().getDownloadNodeFileUrl(node, file.getId());
    }

    @Override
    public boolean fileHasLabel() {
        return getValue() != null && (getValue().getLabel() != null && !getValue().getLabel().isEmpty());
    }

    @Override
    public void updateFilename(String filename) {
    }

    @Override
    public void addHook(FileFieldDisplay.Hook hook) {
        super.addHook(hook);
    }

    @Override
    public void saveFile(final String filename, String data) {
        services.getNodeService().saveFile(filename, data, node, new Callback<String>() {
            @Override
            public void success(String id) {
                add(createFile(id, filename));
            }

            @Override
            public void failure(String error) {
                notifyError(error, TranslatorService.ErrorLabel.SAVE_VALUE);
            }
        });
    }

    private File createFile(String id, String label) {
        return getTypeFactory().createFile(id, label);
    }
}
