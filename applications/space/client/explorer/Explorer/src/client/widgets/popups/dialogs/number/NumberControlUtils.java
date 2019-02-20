package client.widgets.popups.dialogs.number;

import static client.core.model.definition.entity.field.NumberFieldDefinition.Edition;
import static client.core.model.definition.entity.field.NumberFieldDefinition.RangeDefinition;

public class NumberControlUtils {

    private final Edition edition;
    private final RangeDefinition range;
    private final double minStep;

    public NumberControlUtils(Edition edition, RangeDefinition range, double minStep) {
        this.edition = edition;
        this.range = range;
        this.minStep = minStep;
    }

    public Edition getEdition() {
        return edition;
    }

    public RangeDefinition getRange() {
        return range;
    }

    public double getMinStep() {
        return minStep;
    }
}
