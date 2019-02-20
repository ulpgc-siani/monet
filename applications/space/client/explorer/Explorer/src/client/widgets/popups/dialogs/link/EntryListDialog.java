package client.widgets.popups.dialogs.link;

import client.core.model.List;
import client.core.model.NodeIndexEntry;
import client.core.model.definition.entity.IndexDefinition;
import client.core.system.MonetList;
import client.presenters.displays.IndexDisplay;
import client.widgets.toolbox.NavigableScrollPanel;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class EntryListDialog extends NavigableScrollPanel<EntryListDialog.EntryOption> {

    private static final int OPTION_PADDING = 2;
    private static final String IMAGE_WIDTH = "30px";
    private EntrySelectedHandler entrySelectedHandler;

    public EntryListDialog(String emptyMessage, String loadingMessage) {
        super(emptyMessage, loadingMessage);
        addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                if (Window.Navigator.getPlatform().contains("Win"))
                    RootPanel.get().addStyleName(StyleName.NO_SCROLL_WIN);
                else
                    RootPanel.get().addStyleName(StyleName.NO_SCROLL_MAC);
            }
        }, MouseOverEvent.getType());
        addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                RootPanel.get().removeStyleName(StyleName.NO_SCROLL_WIN);
                RootPanel.get().removeStyleName(StyleName.NO_SCROLL_MAC);
            }
        }, MouseOutEvent.getType());
    }

    public void refresh(NodeIndexEntry active) {
        for (EntryOption option : options) {
            if (option.getEntry().equals(active))
                option.addStyleName(StyleName.ACTIVE);
            else
                option.removeStyleName(StyleName.ACTIVE);
        }
    }

    public void setEntrySelectedHandler(EntrySelectedHandler handler) {
        this.entrySelectedHandler = handler;
    }

    protected int calculateScrollPositionForOption() {
        return visibleRange[0] + (OPTION_PADDING * selectedOption);
    }

    public void addPage(IndexDisplay.Page<NodeIndexEntry> page) {
        addEntries(page.getEntries());
    }

    public void addEntries(List<NodeIndexEntry> entries) {
        List<EntryOption> options = new MonetList<>();
        for (NodeIndexEntry entry : entries)
            options.add(new EntryOption(entry));
        addOptions(options);
    }

    public interface EntrySelectedHandler {
        void selectedEntry(NodeIndexEntry entry);
    }

    public class EntryOption extends Grid implements NavigableScrollPanel.Option {

        private final NodeIndexEntry entry;

        public EntryOption(final NodeIndexEntry entry) {
            super(1, entry.getAttributes().size());
            this.entry = entry;
            int column = 0;
            for (NodeIndexEntry.Attribute attribute : entry.getAttributes())
                setWidget(0, column++, createAttribute(attribute));
            addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (entrySelectedHandler != null) entrySelectedHandler.selectedEntry(entry);
                }
            });
        }

        @Override
        public boolean isSelectable() {
            return true;
        }

        public NodeIndexEntry getEntry() {
            return entry;
        }

        private Widget createAttribute(NodeIndexEntry.Attribute attribute) {
            if (attribute.is(IndexDefinition.ReferenceDefinition.AttributeDefinition.Type.PICTURE))
                return createImageAttribute(attribute);
            return createTextAttribute(attribute.getValue());
        }

        private Label createTextAttribute(String attribute) {
            final Label label = new Label(attribute);
            label.setTitle(attribute);
            label.addStyleName(StyleName.ATTRIBUTE);
            return label;
        }

        private Widget createImageAttribute(final NodeIndexEntry.Attribute attribute) {
            Image image = new Image(attribute.getValue());
            image.setWidth(IMAGE_WIDTH);
            image.addStyleName(StyleName.ATTRIBUTE);
            return image;
        }
    }

    public interface StyleName {
        String ACTIVE = "active";
        String ATTRIBUTE = "attribute";
        String NO_SCROLL_WIN = "no-scroll-win";
        String NO_SCROLL_MAC = "no-scroll-mac";
    }
}
