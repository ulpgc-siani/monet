package client.presenters.displays.entity.field;

import client.core.model.List;
import client.core.model.types.Composite;
import client.presenters.displays.IsMultipleDisplay;
import client.presenters.displays.entity.FieldDisplay;

public interface IsMultipleCompositeFieldDisplay extends IsCompositeFieldDisplay, IsMultipleDisplay<Composite> {

    List<FieldDisplay> getDisplaysForComposite(Composite composite);

    void setCurrentComposite(Composite composite);

    List<String> getHeaders();
    List<String> getValuesToShow(Composite composite);

    boolean hasMaxNumberOfComposites();
    boolean hasCompositeWithoutValues();
    boolean isSummary();

    boolean isCurrentComposite(Composite composite);
    boolean hasSelectedComposite();
}
