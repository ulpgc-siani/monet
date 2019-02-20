package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsSelectFieldDisplay;
import client.presenters.displays.entity.field.SelectFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.InputKeyFilter;
import client.widgets.popups.SelectOptionsPopupWidget;
import client.widgets.popups.dialogs.SelectOptionsDialog;
import client.widgets.toolbox.FluidListController;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class SelectFieldWidget extends ValueFieldWidget<InputKeyFilter, SelectOptionsPopupWidget> {

	private static final String FAILURE_OPTION_CODE = "__failure__";

    public SelectFieldWidget(IsSelectFieldDisplay display, String layout, TranslatorService translator) {
		super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.SELECT);
    }

    @Override
    protected void createComponent() {
        super.createComponent();
        new FluidListController<>(popup.getSelectOptionsScrollPanel(), new FluidListController.PageHandler() {
            @Override
            public void nextPage() {
                selectFieldDisplay().nextPage();
            }

            @Override
            public void reloadPage() {
                selectFieldDisplay().reloadPage();
            }
        });
    }

    @Override
    protected void createInput() {
        input = new InputKeyFilter().allowArrows();
        $(getElement()).find(toRule(StyleName.SELECT)).click(new Function() {
            @Override
            public void f() {
                showPopup();
            }
        });
    }

    @Override
    protected void createPopup() {
	    popup = new SelectOptionsPopupWidget(getPopupLayout(), input, translator, new SelectOptionsPopupWidget.SelectPopupDefinition() {
            @Override
            public boolean allowOther() {
                return selectFieldDisplay().allowOther();
            }

            @Override
            public boolean allowSearch() {
                return selectFieldDisplay().allowSearch();
            }

            @Override
            public String formatTerm(Term term) {
                return (selectFieldDisplay().showCode() ? term.getValue() + " - " : "") + term.getFlattenLabel();
            }
        });
        super.createPopup();
        addPopupHandlers();
    }

    protected void update(Term value) {
        selectFieldDisplay().setValue(value);
        removeStyleForOther();
    }

    private void updateOther(Term term) {
        term.setValue(selectFieldDisplay().getValueForTermOther());
        selectFieldDisplay().setValue(term);
        showStyleForOther();
    }

    @Override
    protected void refreshWhenHasValue() {
        super.refreshWhenHasValue();
        input.setValue(selectFieldDisplay().getValueFormatted());
        input.setTitle(selectFieldDisplay().getValueFormatted());
    }

    @Override
    protected SelectFieldDisplay.Hook createHook() {
        return new SelectFieldDisplay.Hook() {
            @Override
            public void value() {
                refresh();
            }

            @Override
            public void error(String error) {
            }

            @Override
            public void historyOptions(SelectFieldDisplay.HistoryOptions options) {
	            popup.refreshHistoryOptions(options);
            }

            @Override
            public void optionsFailure() {
                showFailureLoadingOptions();
            }

            @Override
            public void page(TermList options) {
                popup.addOptions(options);
            }

            @Override
            public void loading() {
                popup.showLoading();
            }
        };
    }

    protected void showFailureLoadingOptions() {
	    popup.showFailureLoading(failureTerm());
    }

    private IsSelectFieldDisplay selectFieldDisplay() {
        return (IsSelectFieldDisplay) display;
    }

    protected void addPopupHandlers() {
	    popup.setOnOptionSelected(new SelectOptionsDialog.OnOptionSelectedHandler() {
            @Override
            public void onSelected(Term term, boolean isOther) {
                if (isOther)
                    updateOther(term);
                else
                    update(term);
            }
        });
	    popup.setSelectOptionsHandler(new SelectOptionsPopupWidget.SelectOptionsHandler() {
            @Override
            public void filter(String condition) {
                SelectFieldWidget.this.filter(condition);
            }

            @Override
            public void closePopUp() {
                hidePopup();
                input.setFocus(true);
            }

            @Override
            public void selectedOption(Term term) {
                update(term);
            }

            @Override
            public void selectedOther(String label) {
                updateOther(selectFieldDisplay().createTerm(selectFieldDisplay().getValueForTermOther(), label));
            }
        });
    }

    protected void addInputHandlers() {
        input.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                input.setValue(selectFieldDisplay().getValueAsString());
            }
        });
        super.addInputHandlers();
    }

    private void filter(String condition) {
        selectFieldDisplay().loadOptions(condition);
        if (selectFieldDisplay().allowHistory())
            selectFieldDisplay().loadHistory(condition);
    }

    @Override
    protected void showPopup() {
        super.showPopup();
        if (popup.getFilter().isEmpty()) {
            selectFieldDisplay().loadOptions();
            if (selectFieldDisplay().allowHistory())
                selectFieldDisplay().loadHistory();
        } else {
            selectFieldDisplay().loadOptions(popup.getFilter());
            if (selectFieldDisplay().allowHistory())
                selectFieldDisplay().loadHistory(popup.getFilter());
        }

    }

    private Term failureTerm() {
        return selectFieldDisplay().createTerm(FAILURE_OPTION_CODE, translator.translate(TranslatorService.ErrorLabel.SOURCE_OPTIONS));
    }

    private void removeStyleForOther() {
        input.removeStyleName(StyleName.OTHER);
    }

    private void showStyleForOther() {
        input.addStyleName(StyleName.OTHER);
    }

    public interface StyleName extends ValueFieldWidget.StyleName {
        String SELECT = "select";
        String OTHER = "other";
    }

    public static class Builder extends FieldWidget.Builder {

	    public Builder() {
		    super();
	    }

	    public static void register(){
            registerBuilder(SelectFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            final IsSelectFieldDisplay display = (IsSelectFieldDisplay) presenter;
            if (display.isEmbedded())
                return new client.widgets.entity.field_poll.SelectFieldWidget(display, layout, translator);
            return new SelectFieldWidget(display, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    if (((IsSelectFieldDisplay)display).isEmbedded())
			    return "";

		    return "dialog-select";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    if (((IsSelectFieldDisplay)display).isEmbedded())
			    return "component-select-embedded";

		    return "component-select";
	    }

    }

}
