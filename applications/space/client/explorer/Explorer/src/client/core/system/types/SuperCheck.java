package client.core.system.types;

public class SuperCheck extends CheckComposite implements client.core.model.types.SuperCheck {

    public SuperCheck() {
    }

    public SuperCheck(String value, String label, boolean checked) {
        super(value, label, checked);
    }

    @Override
    public boolean isSelectable() {
        return true;
    }
}
