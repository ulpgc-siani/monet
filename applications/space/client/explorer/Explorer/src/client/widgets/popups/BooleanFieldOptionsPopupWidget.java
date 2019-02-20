package client.widgets.popups;

import client.services.TranslatorService;
import client.widgets.entity.field.WidgetState;
import client.widgets.popups.dialogs.OptionListDialog;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextBoxBase;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class BooleanFieldOptionsPopupWidget extends FieldOptionsPopupWidget {

	public BooleanFieldOptionsPopupWidget(String layout, TextBoxBase input, WidgetState state, TranslatorService translator) {
        super(layout, input, state, translator);
		init();
    }

	@Override
	protected OptionListDialog createDialog(Element container) {
		OptionListDialog options = new OptionListDialog(translator.translate(TranslatorService.Label.LIST_EMPTY), translator.translate(TranslatorService.Label.LOADING)) {
			@Override
			protected void resetScroll() {
				super.resetScroll();
				visibleRange[1] = 50;
			}
		};

		bindKeepingStyles(options, toRule(StyleName.OPTIONS));

		return options;
	}
}
