package client.widgets.popups;

import client.core.model.List;
import client.core.model.types.Term;
import client.presenters.displays.entity.field.SelectFieldDisplay;
import client.services.TranslatorService;
import client.widgets.popups.dialogs.SelectOptionsDialog;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class SelectOptionsPopupWidget extends FieldPopupWidget<ValueBoxBase, SelectOptionsDialog> {
	private final TranslatorService translator;
    private final SelectPopupDefinition definition;

	public SelectOptionsPopupWidget(String layout, ValueBoxBase input, TranslatorService translator, SelectPopupDefinition definition) {
        super(layout, input);
		this.translator = translator;
        this.definition = definition;
        init();
	}

    public void setSelectOptionsHandler(SelectOptionsHandler handler) {
        content.setSelectOptionsHandler(handler);
    }

    public void refreshHistoryOptions(SelectFieldDisplay.HistoryOptions historyOptions) {
        content.hideLoading();
        content.refreshHistoryOptions(historyOptions);
    }

    @Override
	protected SelectOptionsDialog createDialog(Element container) {
		SelectOptionsDialog dialog = new SelectOptionsDialog(container.getInnerHTML(), translator, new SelectOptionsDialog.SelectDialogDefinition() {
            @Override
            public boolean allowOther() {
                return definition.allowOther();
            }

            @Override
            public boolean allowSearch() {
                return definition.allowSearch();
            }

            @Override
            public String formatTerm(Term term) {
                return definition.formatTerm(term);
            }
        });
		bindKeepingStyles(dialog, container);
		return dialog;
	}

	@Override
    public void show() {
        super.show();
        content.focus();
    }

    public void setOnOptionSelected(SelectOptionsDialog.OnOptionSelectedHandler handler) {
        content.setOnOptionSelectedHandler(handler);
    }

    public void showLoading() {
        content.showLoading();
    }

    public void hideLoading() {
        content.hideLoading();
    }

    public void showFailureLoading(Term term) {
        content.showFailureLoading(term);
    }

    public ScrollPanel getSelectOptionsScrollPanel() {
        return content.getSelectOptionsScrollPanel();
    }

    public void addOptions(List<Term> options) {
        content.hideLoading();
        content.addOptions(options);
    }

    public void clearInput() {
        content.clearInput();
    }

    public String getFilter() {
        return content.getFilter();
    }

    public interface SelectOptionsHandler {
        void filter(String condition);
        void closePopUp();

        void selectedOption(Term selectedTerm);
        void selectedOther(String label);
    }

    public interface SelectPopupDefinition {
        boolean allowOther();
        boolean allowSearch();
        String formatTerm(Term term);
    }
}
