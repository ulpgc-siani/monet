package client.widgets.index.entities;

import client.core.model.Filter;
import client.core.model.List;
import client.core.model.NodeIndexEntry;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.SetIndexDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.index.IndexWidget;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.*;
import cosmos.gwt.presenters.PresenterHolder;

import static client.core.model.NodeIndexEntry.Attribute;
import static client.widgets.toolbox.HTMLListWidget.Mode;
import static com.google.gwt.dom.client.Style.Display;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.*;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;
import static cosmos.utils.StringUtils.shortContent;

public abstract class SetIndexWidget extends IndexWidget<SetIndexDisplay, NodeIndexEntry> {

	public SetIndexWidget(SetIndexDisplay display, LayoutHelper layoutHelper, TranslatorService translator) {
		super(display, layoutHelper, translator);
	}

	@Override
	public void onHold(PresenterHolder holder) {
		super.onHold(holder);
	}

	@Override
	protected IndexItem.Builder getBuilder() {
		return new NodeIndexItem.Builder(layoutHelper, translator);
	}

	@Override
	protected void addPageEntry(NodeIndexEntry entry) {
		HTMLListWidget.ListItem<NodeIndexEntry> item = list.addItem(entry);
		$(item).find(".content-panel > div > table").width("98%");
	}

	@Override
    protected void hook() {
        display.addHook(new IndexDisplay.Hook<NodeIndexEntry>() {
            @Override
            public void clear() {
                clearPages();
            }

            @Override
            public void loadingPage() {
                showLoadingPages();
            }

            @Override
            public void page(IndexDisplay.Page<NodeIndexEntry> page) {
                hideLoadingPages();
                addPage(page);
            }

            @Override
            public void pagesCount(int count) {
            }

            @Override
            public void pageEntryAdded(NodeIndexEntry entry) {
                for (Widget widget : list.getItems()) {
                    if (widget instanceof SetIndexWidget.NodeIndexListItem)
                        ((SetIndexWidget.NodeIndexListItem) widget).close();
                }
                addPageEntry(entry);
            }

            @Override
            public void pageEntryDeleted() {
            }

            @Override
            public void pageEntryUpdated(NodeIndexEntry entry) {
                updatePageEntry(entry);
            }

            @Override
            public void pageFailure() {
                hideLoadingPages();
            }

            @Override
            public void entityView(ViewDisplay entityViewDisplay) {
                facet.refreshEntityView(display.getActive(), entityViewDisplay);
            }

            @Override
            public void selectEntries(List<NodeIndexEntry> entries) {
            }

            @Override
            public void selectOptions(Filter filter) {
                refreshFilters();
            }

            @Override
            public void loadingOptions() {
            }

            @Override
            public void options(Filter filter, List<Filter.Option> options) {
            }
        });
    }

    public static class NodeIndexIconItem extends NodeIndexItem {

		public NodeIndexIconItem(NodeIndexEntry value, LayoutHelper helper, TranslatorService translator) {
			super(value, helper, translator);
		}

		@Override
		protected Widget createComponent() {
			componentPanel = new HTMLPanel(helper.getIndexEntryLayout(Mode.ICON));

			addPicture(componentPanel);
			addGeoReferenced(componentPanel);
			addLabel(componentPanel);

			refresh();

			return componentPanel;
		}

		protected void addPicture(HTMLPanel component) {
			Attribute picture = value.getPicture();
			Image image = new Image();

			if (picture != null && !picture.getValue().isEmpty())
				image.setUrl(picture.getValue());
			else
				image.setUrl(translator.getNoPhoto());

			image.setTitle(value.getLabel());

			replaceRegion(component, toRuleCheckingTags(StyleName.PICTURE, StyleName.ATTRIBUTE, StyleName.IMG), image);
		}

		@Override
		public void refresh() {
			refreshPicture();
			refreshGeoReferenced();
			refreshLabel();
		}
	}

	public static class NodeIndexListItem extends NodeIndexItem {
		private Widget entity = null;

		public NodeIndexListItem(NodeIndexEntry value, LayoutHelper helper, TranslatorService translator) {
			super(value, helper, translator);
		}

		public boolean existsEntity() {
			return entity != null;
		}

		public void setEntity(Widget entity) {
			Element entityPanel = $(componentPanel).find(toRule(StyleName.EMBEDDED_ENTITY)).get(0);
			entityPanel.getStyle().setDisplay(com.google.gwt.dom.client.Style.Display.BLOCK);
			bindWidgetToElement(componentPanel, entity, entityPanel);
			entity.addStyleName(StyleName.EMBEDDED_ENTITY);
			this.entity = entity;
		}

		public void open() {
			addStyleName(StyleName.OPENED);
            entity.setVisible(true);
		}

		public void close() {
			removeStyleName(StyleName.OPENED);
            if (entity != null) entity.setVisible(false);
		}

		public void toggleEntity() {
			entity.setVisible(!entity.isVisible());

			if (entity.isVisible())
				open();
			else
				close();
		}

		@Override
		protected Widget createComponent() {
			componentPanel = new HTMLPanel(helper.getIndexEntryLayout(Mode.LIST));

			addPicture(componentPanel);
			addGeoReferenced(componentPanel);
			addLabel(componentPanel);
			addLines(componentPanel);
			addLinesBelow(componentPanel);
			addHighlight(componentPanel);
			addFooter(componentPanel);

			$(componentPanel).find(toRule(StyleName.EMBEDDED_ENTITY)).first().hide();

			refresh();

			return componentPanel;
		}

		private void addLines(HTMLPanel component) {
			addRegion(toClass(StyleName.ATTRIBUTES, StyleName.LINES), toRule(StyleName.LINES), component);
		}

		private void refreshLines() {
			refreshRegion("lines", toRule(StyleName.LINES), value.getLines());
		}

		private void addLinesBelow(HTMLPanel component) {
			addRegion(toClass(StyleName.ATTRIBUTES, StyleName.LINES_BELOW), toRule(StyleName.LINES_BELOW), component);
		}

		private void refreshLinesBelow() {
			refreshRegion("lines-below", toRule(StyleName.LINES_BELOW), value.getLinesBelow());
		}

		protected void addHighlight(HTMLPanel component) {
			addRegion(toClass(StyleName.ATTRIBUTES, StyleName.HIGHLIGHT), toRule(StyleName.HIGHLIGHT), component);
		}

		protected void refreshHighlight() {
			refreshRegion("highlight", toRule(StyleName.HIGHLIGHT), value.getHighlights());
		}

		private void addFooter(HTMLPanel component) {
			addRegion(toClass(StyleName.ATTRIBUTES, StyleName.FOOTER), toRule(StyleName.FOOTER), component);
		}

		private void refreshFooter() {
			refreshRegion("footer", toRule(StyleName.FOOTER), value.getFooters());
		}

		private void addRegion(String cssClass, String cssRule, HTMLPanel component) {
			FlowPanel region = new FlowPanel();
			region.addStyleName(cssClass);
			replaceRegion(component, cssRule, region);
		}

		private void refreshRegion(String regionName, String cssRegion, List<Attribute> attributes) {
			FlowPanel region = (FlowPanel) getRegion(cssRegion);

			region.clear();
			for (NodeIndexEntry.Attribute attribute : attributes) {
				HTML htmlAttribute = createAttribute(attribute, regionName);
				if (htmlAttribute != null)
					region.add(htmlAttribute);
			}

			region.getElement().getStyle().setDisplay(attributes.size() > 0 ? Display.BLOCK : Display.NONE);
		}

		private HTML createAttribute(NodeIndexEntry.Attribute attribute, String region) {
			if (attribute.getValue().length() == 0)
				return null;

			HTML attributePanel = new HTML(getAttributeTemplate(attribute, region));

			fillLabel(attributePanel, attribute);
			fillValue(attributePanel, attribute);
			fillLink(attributePanel, attribute);

			return attributePanel;
		}

		private void fillLabel(HTML attributePanel, NodeIndexEntry.Attribute attribute) {
			Element label = $(attributePanel).find(toRule(StyleName.LABEL)).get(0);

			if (label == null)
				return;

			label.setInnerHTML(attribute.getDefinition().getLabel());
		}

		private void fillValue(HTML attributePanel, NodeIndexEntry.Attribute attribute) {
			Element value = $(attributePanel).find(toRule(StyleName.VALUE)).get(0);

			if (value == null)
				return;

			value.setInnerHTML(attribute.getValue());
			value.setTitle(shortContent(attribute.getValue(), 100));
		}

		private void fillLink(HTML attributePanel, final NodeIndexEntry.Attribute attribute) {
			Element link = $(attributePanel).find(toRule(StyleName.LINK)).get(0);

			if (link == null)
				return;

			Anchor anchor = $(link).widget();

			anchor.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					System.out.println("TODO. onclick with filllink in nodeindexwidget");
					//attribute.getLink();
				}
			});

			anchor.setTitle(attribute.getValue());
			anchor.setText(shortContent(attribute.getValue()));
		}

		private String getAttributeTemplate(NodeIndexEntry.Attribute attribute, String region) {

			String templateName = StyleName.ATTRIBUTE + " " + attribute.getDefinition().getType() + " " + region;
			if (!helper.existsIndexTemplate(templateName))
				templateName = StyleName.ATTRIBUTE + " " + region;
			if (!helper.existsIndexTemplate(templateName))
				templateName = StyleName.ATTRIBUTE + " " + attribute.getDefinition().getType();
			if (!helper.existsIndexTemplate(templateName))
				templateName = StyleName.ATTRIBUTE;

			return helper.getIndexTemplate(templateName);
		}

		@Override
		protected String getLabel() {
			return value.getLabel();
		}

		@Override
		public void refresh() {
			refreshPicture();
			refreshGeoReferenced();
			refreshLabel();
			refreshLines();
			refreshLinesBelow();
			refreshHighlight();
			refreshFooter();
		}

		public interface StyleName extends NodeIndexItem.StyleName {
			String EMBEDDED_ENTITY = "embedded-entity";
			String OPENED = "opened";
		}

	}

	public abstract static class NodeIndexItem extends IndexItem<NodeIndexEntry> {
		protected HTMLPanel componentPanel;
		protected LayoutHelper helper;

		public NodeIndexItem(NodeIndexEntry value, LayoutHelper helper, TranslatorService translator) {
			super(value, translator, false);
			this.helper = helper;
			init();
		}

		protected void addPicture(HTMLPanel component) {
			Image image = new Image();
			replaceRegion(component, toRuleCheckingTags(StyleName.PICTURE, StyleName.ATTRIBUTE, StyleName.IMG), image);
		}

		protected void refreshPicture() {
			NodeIndexEntry.Attribute picture = value.getPicture();
			Image image = (Image) getRegion(toRuleCheckingTags(StyleName.PICTURE, StyleName.ATTRIBUTE, StyleName.IMG));

			if (picture != null && !picture.getValue().isEmpty()) {
				image = new Image(picture.getValue());
				image.setTitle(value.getLabel());
			}

			image.getElement().getStyle().setDisplay(picture != null && !picture.getValue().isEmpty() ? Display.BLOCK : Display.NONE);
		}

		protected void addGeoReferenced(HTMLPanel component) {
			final Anchor geoReferencedAnchor = new Anchor();

			geoReferencedAnchor.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					value.setGeoReferenced(!value.isGeoReferenced());
					refreshGeoReferenced();
				}
			});

			replaceRegion(component, toRule(StyleName.GEO_REFERENCED), geoReferencedAnchor);
		}

		protected void refreshGeoReferenced() {
			Anchor geoReferencedAnchor = (Anchor) getRegion(toRule(StyleName.GEO_REFERENCED));

			geoReferencedAnchor.removeStyleName(StyleName.ENABLED);
			if (value.isGeoReferenced())
				geoReferencedAnchor.addStyleName(StyleName.ENABLED);
		}

		protected void addLabel(HTMLPanel component) {
			Label label = new Label();

			label.addStyleName(StyleName.LABEL);
			label.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					notifyClick();
				}
			});

			replaceRegion(component, toRule(StyleName.LABEL), label);
		}

		protected void refreshLabel() {
			Label label = (Label) getRegion(toRule(StyleName.LABEL));

			label.setText(value.getLabel());
			label.setTitle(value.getLabel());
		}



		public interface StyleName extends IndexItem.StyleName {
			String LABEL = "label";
			String VALUE = "value";
			String LINK = "link";
			String PICTURE = "pict";
			String IMG = "img";
			String LINES = "lines";
			String LINES_BELOW = "lines-below";
			String HIGHLIGHT = "highlight";
			String FOOTER = "footer";
			String ATTRIBUTE = "attribute";
			String ATTRIBUTES = "attributes";
			String GEO_REFERENCED = "georef";
			String ENABLED = "enabled";
		}

		public static class Builder extends IndexItem.Builder<NodeIndexEntry> {
			private final LayoutHelper helper;

			public Builder(LayoutHelper helper, TranslatorService translator) {
				super(translator);
				this.helper = helper;
			}

			@Override
			public HTMLListWidget.ListItem<NodeIndexEntry> build(NodeIndexEntry value, Mode mode) {
				NodeIndexItem widget = null;

				if (mode == Mode.ICON)
					widget = new NodeIndexIconItem(value, helper, translator);
				else if (mode == Mode.LIST)
					widget = new NodeIndexListItem(value, helper, translator);

				return widget;
			}

			@Override
			public Mode[] getAcceptedModes() {
				return new Mode[] { Mode.LIST, Mode.ICON };
			}
		}
	}

	public abstract static class Builder extends IndexWidget.Builder<SetIndexDisplay> {
	}

}
