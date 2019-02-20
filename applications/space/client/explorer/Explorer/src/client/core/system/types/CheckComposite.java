package client.core.system.types;

import client.core.model.types.CheckList;

public abstract class CheckComposite extends Check implements client.core.model.types.CompositeCheck {

    private CheckList checks;

    public CheckComposite() {
    }

    public CheckComposite(String value, String label, boolean checked) {
        super(value, label, checked);
    }

    @Override
    public CheckList getChecks() {
        return checks;
    }

    @Override
    public void setChecks(CheckList checks) {
        this.checks = checks;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
