package client.widgets.entity.field;

import client.presenters.displays.entity.field.IsMemoFieldDisplay;
import client.services.TranslatorService;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.RichTextArea;

public class RichMemoFieldWidget extends MemoFieldWidget {

	protected RichTextArea valueInput;

	public RichMemoFieldWidget(IsMemoFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
	}

    private IsMemoFieldDisplay memoFieldDisplay() {
        return (IsMemoFieldDisplay) display;
    }

    @Override
    protected void createField() {
        createTextArea();
    }

    private void createTextArea() {
        valueInput = new RichTextArea();
        valueInput.setSize("100%", "10em");
        valueInput.addHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                update(valueInput.getText());
            }
        }, ChangeEvent.getType());
        valueInput.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                //input.selectAll();
            }
        });
    }

    @Override
	protected void refreshComponent() {
		valueInput.setText(memoFieldDisplay().getValue());
	}
}
