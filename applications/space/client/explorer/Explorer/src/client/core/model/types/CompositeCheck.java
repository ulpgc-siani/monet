package client.core.model.types;

public interface CompositeCheck extends Check {

    CheckList getChecks();
    void setChecks(CheckList checks);
    boolean isSelectable();
}
