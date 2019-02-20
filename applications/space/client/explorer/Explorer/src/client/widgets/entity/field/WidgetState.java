package client.widgets.entity.field;

public class WidgetState {

    private boolean editing;
    private OnModeChanged onModeChanged;

    public WidgetState() {
        editing = false;
    }

    public void setOnChange(OnModeChanged onChanged) {
        this.onModeChanged = onChanged;
    }

    public boolean isEditing() {
        return editing;
    }

    public void enable() {
        if (editing) return;
        toggleEdition();
    }

    public void disable() {
        if (!editing) return;
        toggleEdition();
    }

    public void toggleEdition() {
        editing = !editing;
        if (onModeChanged != null) onModeChanged.onChanged();
    }

    public interface OnModeChanged {
        void onChanged();
    }
}
