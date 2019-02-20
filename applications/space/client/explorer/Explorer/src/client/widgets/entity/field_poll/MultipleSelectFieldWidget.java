package client.widgets.entity.field_poll;

import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsMultipleSelectFieldDisplay;
import client.presenters.displays.entity.field.MultipleSelectFieldDisplay;
import client.presenters.displays.entity.field.SelectFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.toolbox.CheckListWidget;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.model.HasValue;
import cosmos.presenters.Presenter;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultipleSelectFieldWidget extends FieldWidget<MultipleSelectFieldDisplay.Hook> {

    private CheckListWidget<ValueCheck> checkListWidget;

    public MultipleSelectFieldWidget(IsMultipleSelectFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.CHECK);
    }

    @Override
    protected void createComponent() {

        checkListWidget = new CheckListWidget<>(translator);
        checkListWidget.addToggleHandler(new CheckListWidget.ToggleHandler<Term>() {
            @Override
            public void onToggle(Term value, boolean checked) {
                update(value);
            }

            @Override
            public void onChange(List<Term> selection) {
            }
        }, 0);

        loadTermListOptions();
    }

    private void update(Term term) {
        multipleSelectFieldDisplay().toggle(term);
    }

    private void loadTermListOptions() {
        new Timer() {
            @Override
            public void run() {
                multipleSelectFieldDisplay().loadOptions();
            }
        }.schedule(100);
    }

    @Override
    protected void bind() {
        bind(checkListWidget, toRule(StyleName.TERM_LIST));
        super.bind();
    }

    @Override
    protected void refreshComponent() {
        for (Term term : multipleSelectFieldDisplay().getAllValues())
            checkListWidget.select(new ValueCheck(term));
    }

    @Override
    protected MultipleSelectFieldDisplay.Hook createHook() {
        return new MultipleSelectFieldDisplay.Hook() {
            @Override
            public void values() {
                refresh();
            }

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

    private void fillTermList(TermList terms) {
        for (Term term : terms)
            checkListWidget.addItem(new ValueCheck(term));
        refresh();
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MultipleSelectFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.POLL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new MultipleSelectFieldWidget((IsMultipleSelectFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-select-multiple-poll";
	    }

    }

    private IsMultipleSelectFieldDisplay multipleSelectFieldDisplay() {
        return (IsMultipleSelectFieldDisplay) display;
    }

    public interface StyleName extends FieldWidget.StyleName {
        String CHECK = "check";
        String TERM_LIST = "terms";
    }

    public class ValueCheck extends client.core.system.types.Term implements HasValue {
        public ValueCheck(Term check) {
            super(check.getValue(), check.getLabel());
        }
    }
}
