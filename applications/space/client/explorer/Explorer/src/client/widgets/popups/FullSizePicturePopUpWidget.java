package client.widgets.popups;

import client.core.model.List;
import client.core.system.MonetList;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import cosmos.gwt.utils.ImageUtils;

import static com.google.gwt.dom.client.Style.Unit;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElementAndKeepStyles;

public class FullSizePicturePopUpWidget extends PopUpWidget<Image>{

    private static final int WIDTH = 0;
    private static final int HEIGHT = 1;
    private final String filename;
	private final String url;

	public FullSizePicturePopUpWidget(String filename, String url) {
        super();
        this.filename = filename;
		this.url = url;
		init(url);
    }

	private void init(String url) {
		super.init();

		ImageUtils.getImageDimension(url, new Callback<JsArrayInteger, String>() {
			@Override
			public void onFailure(String reason) {
			}

			@Override
			public void onSuccess(JsArrayInteger result) {
				createComponent(result);
			}
		});
		addStyleName(StyleName.PICTURE_POPUP);
	}

	private void createComponent(final JsArrayInteger dimension) {
        final List<Integer> dimensionAdjusted = adjustDimensionToScreen(dimension);
        setSizeCalculator(new SizeCalculator() {
            @Override
            public int getWidth() {
                return dimensionAdjusted.get(WIDTH) + 20;
            }

            @Override
            public int getHeight() {
                return dimensionAdjusted.get(HEIGHT);
            }
        });
        show();
        getElement().getStyle().setLeft(getHorizontalCenter(dimensionAdjusted.get(WIDTH)), Unit.PX);
        getElement().getStyle().setTop(getVerticalCenter(dimensionAdjusted.get(HEIGHT)), Unit.PX);
        content.setWidth(dimensionAdjusted.get(WIDTH) + Unit.PX.toString());
        content.setHeight(dimensionAdjusted.get(HEIGHT) + Unit.PX.toString());
        add(new Label(filename));
        add(createCloseButton());
    }

    private List<Integer> adjustDimensionToScreen(JsArrayInteger dimension) {
        if (dimension.get(WIDTH) < getScreenWidth() && dimension.get(HEIGHT) < getScreenHeight()) return new MonetList<>(dimension.get(WIDTH), dimension.get(HEIGHT));
        if (dimension.get(WIDTH) > getScreenWidth() && dimension.get(HEIGHT) <= getScreenHeight()) return scaleWidth(dimension);
        if (dimension.get(WIDTH) <= getScreenWidth() && dimension.get(HEIGHT) > getScreenHeight()) return scaleHeight(dimension);
        return scaleBoth(dimension);
    }

    private List<Integer> scaleWidth(JsArrayInteger dimension) {
        return scale(dimension, dimension.get(WIDTH) / (getScreenWidth() * 0.9));
    }

    private List<Integer> scaleHeight(JsArrayInteger dimension) {
        return scale(dimension, dimension.get(HEIGHT) / (getScreenWidth() * 0.9));
    }

    private List<Integer> scaleBoth(JsArrayInteger dimension) {
        if (dimension.get(WIDTH) > dimension.get(HEIGHT))
            return scaleWidth(dimension);
        return scaleHeight(dimension);
    }

    private List<Integer> scale(JsArrayInteger dimension, double scale) {
        return new MonetList<>((int) (dimension.get(WIDTH) / scale), (int) (dimension.get(HEIGHT) / scale));
    }

    private HTML createCloseButton() {
        final HTML closeButton = new HTML();
        closeButton.addStyleName(StyleName.CLOSE_BUTTON);
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RootPanel.get().remove(FullSizePicturePopUpWidget.this);
                event.stopPropagation();
            }
        });
        return closeButton;
    }

    private int getScreenWidth() {
        return RootPanel.get().getOffsetWidth();
    }

    private int getScreenHeight() {
        return RootPanel.get().getOffsetHeight();
    }

    private int getHorizontalCenter(int contentWidth) {
        return (getScreenWidth() - contentWidth) / 2;
    }

    private int getVerticalCenter(int contentHeight) {
        return (getScreenHeight() - contentHeight) / 2;
    }

	@Override
	protected Image createContent(Element container) {
		Image dialog = new Image(url);
		bindKeepingStyles(dialog, container);
		return dialog;
	}

	public interface StyleName {
        String PICTURE_POPUP = "picture-popup";
        String CLOSE_BUTTON = "close-button";
    }
}
