package client.core.system.types;

public class TermCategory extends CompositeTerm implements client.core.model.types.TermCategory {

    public TermCategory() {
    }

    public TermCategory(String value, String label) {
        super(value, label);
    }

    @Override
    public boolean isSelectable() {
        return false;
    }
}
