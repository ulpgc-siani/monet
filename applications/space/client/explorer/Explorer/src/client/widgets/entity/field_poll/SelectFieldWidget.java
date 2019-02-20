package client.widgets.entity.field_poll;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsSelectFieldDisplay;
import client.presenters.displays.entity.field.SelectFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.RadioTermOptionsWidget;
import client.widgets.toolbox.RadioTermListWidget;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class SelectFieldWidget extends FieldWidget<SelectFieldDisplay.Hook> {

    private RadioTermOptionsWidget termListWidget;

    public SelectFieldWidget(IsSelectFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
        display.loadOptions();
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.RADIO);
    }

    @Override
    protected void createComponent() {
        createTermList();
    }

    @Override
    protected void bind() {
        bindKeepingStyles(termListWidget, toRule(StyleName.TERMS));
        super.bind();
    }

    @Override
    protected void refreshComponent() {
        if (display.hasValue())
            termListWidget.select(selectFieldDisplay().getValue());
        else
            termListWidget.selectFirst();
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
            public void historyOptions(SelectFieldDisplay.HistoryOptions historyOptions) {
                fillTermList(historyOptions.getOptions());
            }

            @Override
            public void optionsFailure() {
            }

            @Override
            public void page(TermList options) {
                fillTermList(options);
            }

            @Override
            public void loading() {
            }
        };
    }

    private void createTermList() {
        termListWidget = new RadioTermOptionsWidget(translator);
        termListWidget.setSelectHandler(new RadioTermListWidget.SelectHandler() {
            @Override
            public void onSelect(Term term) {
                selectFieldDisplay().setValue(term);
            }
        });
    }

    private void fillTermList(TermList termList) {
        termListWidget.setTerms(termList);
        refresh();
    }

    private IsSelectFieldDisplay selectFieldDisplay() {
        return (IsSelectFieldDisplay) display;
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(SelectFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.POLL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new SelectFieldWidget((IsSelectFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-select-poll";
	    }

    }

    public interface StyleName extends FieldWidget.StyleName {
        String RADIO = "radio";
        String TERMS = "terms";
    }
}
