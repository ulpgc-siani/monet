package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.FileFieldDefinition;
import client.core.model.fields.FileField;
import client.core.model.types.File;
import client.presenters.displays.entity.FieldDisplay;
import client.services.TranslatorService;
import client.services.callback.Callback;

public class FileFieldDisplay extends FieldDisplay<FileField, FileFieldDefinition, File> implements IsFileFieldDisplay {

	public static final Type TYPE = new Type("FileFieldDisplay", FieldDisplay.TYPE);

	public FileFieldDisplay(Node node, FileField field) {
		super(node, field);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getValueAsString() {
		return getValue().getLabel();
	}

	@Override
	protected File format(File file) {
		return file;
	}

	@Override
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

    @Override
    public long getLimit() {
        return megaBytesToBytes();
    }

    private long megaBytesToBytes() {
        return getDefinition().getLimit() * 1024 * 1024;
    }

    @Override
	public String getFileLabel() {
		return fileHasLabel() ? getValue().getLabel() : getValue().getId().substring(getValue().getId().lastIndexOf("/") + 1);
	}

	@Override
	public String getFileId() {
		return services.getNodeService().getDownloadNodeFileUrl(node, getValue().getId());
	}

	@Override
	public boolean fileHasLabel() {
		return getValue() != null && (getValue().getLabel() != null && !getValue().getLabel().isEmpty());
	}

	@Override
	public void updateFilename(String filename) {
		if (hasValue() && !filename.equals(getValue().getLabel()))
			super.setValue(createFile(getValue().getId(), filename));
	}

	@Override
	public void saveFile(final String filename, String data) {
        services.getNodeService().saveFile(filename, data, node, new Callback<String>() {
            @Override
            public void success(String id) {
                if (!hasValue() || labelIsSameAsFilename())
                    setValue(createFile(id, filename));
                else
                    setValue(createFile(id, getFileLabel()));
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

	private boolean labelIsSameAsFilename() {
		return getName(getFileLabel()).equals(getName(getValue().getId()));
	}

	private String getName(String label) {
		return label.substring(label.lastIndexOf("/") + 1).replaceAll("\\s", "");
	}
}
