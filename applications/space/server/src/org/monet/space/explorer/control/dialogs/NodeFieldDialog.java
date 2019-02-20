package org.monet.space.explorer.control.dialogs;

import org.monet.space.explorer.control.dialogs.constants.Parameter;

public class NodeFieldDialog extends NodeDialog {

    public String getFieldPath() {
        return getString(Parameter.FIELD);
    }
}
