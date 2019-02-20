package client.widgets.entity.field;

import client.presenters.displays.entity.field.IsMemoFieldDisplay;
import client.presenters.displays.entity.field.IsMultipleMemoFieldDisplay;
import client.services.TranslatorService;

public class MultipleRichMemoFieldWidget extends RichMemoFieldWidget {

    public MultipleRichMemoFieldWidget(IsMemoFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.MULTIPLE);
    }

    private IsMultipleMemoFieldDisplay multipleMemoFieldDisplay() {
        return (IsMultipleMemoFieldDisplay) display;
    }

    public interface StyleName extends MemoFieldWidget.StyleName { }
}
