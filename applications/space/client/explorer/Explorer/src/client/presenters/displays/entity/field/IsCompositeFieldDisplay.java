package client.presenters.displays.entity.field;

import client.core.model.List;
import client.core.model.types.Composite;
import client.presenters.displays.entity.FieldDisplay;

public interface IsCompositeFieldDisplay extends IsFieldDisplay<Composite> {

    void scroll(int position);

    boolean isExtensible();
    boolean isConditional();
    boolean getConditioned();

    String getShowLayout();
    String getShowLayoutExtended();
    boolean hasLayout();
    boolean hasLayoutExtended();

    List<FieldDisplay> getNonExtendedDisplays();
    List<FieldDisplay> getExtendedDisplays();

    String getShowAllLabel();

    void createComposite();
    void toggleShowMore();
    void toggleCondition();

    boolean getShowAllValue();
}
