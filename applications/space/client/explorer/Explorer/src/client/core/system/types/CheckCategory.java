package client.core.system.types;

public class CheckCategory extends CheckComposite implements client.core.model.types.CheckCategory {

    public CheckCategory() {
    }

    public CheckCategory(String value, String label, boolean checked) {
        super(value, label, checked);
    }

    @Override
    public boolean isSelectable() {
        return false;
    }
}
