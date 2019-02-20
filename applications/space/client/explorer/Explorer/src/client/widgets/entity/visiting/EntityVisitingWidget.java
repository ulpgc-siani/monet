package client.widgets.entity.visiting;

import client.presenters.displays.EntityVisitingDisplay;
import client.services.TranslatorService;
import client.widgets.entity.VisitingWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class EntityVisitingWidget extends VisitingWidget<EntityVisitingDisplay> {
	private Anchor upButton;
	private Anchor backButton;

	public EntityVisitingWidget(EntityVisitingDisplay display, String layout, TranslatorService translator) {
		super(display, layout, translator);
	}

	@Override
	protected void createComponents() {
		createUpButton();
		createBackButton();
		bind();
	}

	@Override
	protected void refresh() {
		refreshUpButton();
		refreshLabel();
		refreshBackButton();
	}

    @Override
    protected void hook() {
        display.addHook(new EntityVisitingDisplay.Hook() {
            @Override
            public void label() {
                refreshLabel();
            }

            @Override
            public void labelFailure(String error) {
            }
        });
    }

    private void createUpButton() {
		upButton = new Anchor();
		upButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				up();
			}
		});
		upButton.setTitle(translator.translate(TranslatorService.Label.UP).toLowerCase());
	}

	private void createBackButton() {
		backButton = new Anchor();
		backButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				back();
			}
		});
		backButton.setHTML(translator.translate(TranslatorService.Label.BACK).toLowerCase());
	}

	private void up() {
		display.up();
	}

	private void bind() {
		if (!$(getElement()).find(toRule(StyleName.UP)).isEmpty()) {
			bindKeepingStyles(upButton, toRule(StyleName.UP));
			bindKeepingStyles(backButton, toRule(StyleName.BACK));
		}
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private void refreshUpButton() {
		upButton.setVisible(display.isUpEnabled());
	}

	private void refreshBackButton() {
		backButton.setVisible(display.isBackEnabled());
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(EntityVisitingDisplay.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			return new EntityVisitingWidget((EntityVisitingDisplay) presenter, getLayout((EntityVisitingDisplay)presenter, layout), translator);
		}

		private String getLayout(EntityVisitingDisplay display, String layout) {
			String layoutName = layout + (display.isEntityComponent()?"-component":"");

			if (! theme.existsLayout(layoutName))
				layoutName = layout;

			return layout.isEmpty() ? "<div class='error'>Layout not defined for entity visiting widget</div>" : theme.getLayout(layoutName);
		}
	}

	private interface StyleName extends VisitingWidget.StyleName {
		String UP = "up";
		String BACK = "back";
	}

}
