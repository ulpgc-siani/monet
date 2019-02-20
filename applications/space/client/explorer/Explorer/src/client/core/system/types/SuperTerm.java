package client.core.system.types;

public class SuperTerm extends CompositeTerm implements client.core.model.types.SuperTerm {

    public SuperTerm() {
    }

    public SuperTerm(String value, String label) {
        super(value, label);
    }

    @Override
    public boolean isSelectable() {
        return true;
    }
}
