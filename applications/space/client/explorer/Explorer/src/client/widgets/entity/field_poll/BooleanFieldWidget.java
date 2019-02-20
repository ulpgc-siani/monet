package client.widgets.entity.field_poll;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Term;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.BooleanFieldDisplay;
import client.presenters.displays.entity.field.IsBooleanFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.toolbox.RadioTermListWidget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class BooleanFieldWidget extends FieldWidget<BooleanFieldDisplay.Hook> {

    private CheckBox checkEditor;
    private RadioTermListWidget optionsEditor;

    public BooleanFieldWidget(IsBooleanFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.BOOLEAN);
    }

    @Override
    protected void createComponent() {
        if (booleanFieldDisplay().isCheckEdition())
            createCheckEditor();
        else
            createOptionsEditor();
    }

    @Override
    protected void bind() {
        bind(booleanFieldDisplay().isCheckEdition() ? checkEditor : optionsEditor, toRule(StyleName.EDITOR));
        super.bind();
    }

    @Override
    protected void refreshComponent() {
        if (!display.hasValue()) return;
        if (booleanFieldDisplay().isCheckEdition())
            checkEditor.setValue(booleanFieldDisplay().getValue());
        else
            optionsEditor.select(booleanFieldDisplay().booleanToTerm(booleanFieldDisplay().getValue()));
    }

    @Override
    protected BooleanFieldDisplay.Hook createHook() {
        return new BooleanFieldDisplay.Hook() {
            @Override
            public void value() {
                refresh();
            }

            @Override
            public void error(String error) {
            }
        };
    }

    private void createCheckEditor() {
        checkEditor = new CheckBox();
    }

    private void createOptionsEditor() {
        optionsEditor = new RadioTermListWidget(translator);
        for (Term option : booleanFieldDisplay().createBooleanTermList())
            optionsEditor.addItem(option);
        optionsEditor.addSelectHandler(new RadioTermListWidget.SelectHandler() {
            @Override
            public void onSelect(Term option) {
                booleanFieldDisplay().setValue(booleanFieldDisplay().termToBoolean(option));
            }
        });
    }

    private IsBooleanFieldDisplay booleanFieldDisplay() {
        return (IsBooleanFieldDisplay) display;
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(BooleanFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.POLL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new BooleanFieldWidget((IsBooleanFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-boolean-poll";
	    }
    }

    public interface StyleName extends FieldWidget.StyleName {
        String BOOLEAN = "boolean";
        String EDITOR = "editor";
    }
}
