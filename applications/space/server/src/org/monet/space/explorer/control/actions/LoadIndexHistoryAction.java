package org.monet.space.explorer.control.actions;

import com.google.inject.Inject;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.IndexDefinitionBase;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.space.explorer.control.dialogs.IndexHistoryDialog;
import org.monet.space.explorer.control.displays.NodeIndexEntryDisplay;
import org.monet.space.explorer.model.ExplorerList;
import org.monet.space.explorer.model.IndexEntry;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.explorer.model.NodeIndexEntry;
import org.monet.space.kernel.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadIndexHistoryAction extends Action<IndexHistoryDialog, NodeIndexEntryDisplay> {

    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public void execute() throws IOException {
        LinkFieldIndexEntriesLoader loader = new LinkFieldIndexEntriesLoader(loadTerms());
        ExplorerList<IndexEntry> result = new ExplorerList<>();
        result.setTotalCount(loader.getCount());

        if (result.getTotalCount() > 0)
            for (IndexEntry entry : loader.getItems())
                result.add(entry);

        display.writeList(result);
    }

    private TermList loadTerms() {
        DataRequest dataRequest = new DataRequest();

        if (dialog.getCondition() != null)
            dataRequest.setCondition(dialog.getCondition());

        dataRequest.setStartPos(dialog.getStart());
        dataRequest.setLimit(dialog.getLimit());

        return dialog.getStore().getTerms(dataRequest);
    }

    private class LinkFieldIndexEntriesLoader {
        private final TermList terms;
        private NodeItemList nodeItemList = null;

        public LinkFieldIndexEntriesLoader(TermList terms) {
            this.terms = terms;
        }

        public int getCount() {
            return load().getTotalCount();
        }

        public List<IndexEntry> getItems() {
            List<IndexEntry> list = new ArrayList<>();

            for (NodeItem nodeItem : load().get().values())
                list.add(createNodeIndexEntry(nodeItem));

            return list;
        }

        private NodeIndexEntry createNodeIndexEntry(final NodeItem nodeItem) {
            return new NodeIndexEntry() {
                @Override
                public Node getEntity() {
                    Node node = new Node();
                    node.setId(nodeItem.getCode());
                    node.setCode(nodeItem.getDefinitionCode());
                    Reference reference = new Reference(DescriptorDefinition.CODE);
                    reference.setLabel(nodeItem.getAttribute("label"));
                    node.setReference(reference);
                    return node;
                }

                @Override
                public String getLabel() {
                    return nodeItem.getAttribute(DescriptorDefinition.ATTRIBUTE_LABEL);
                }

                @Override
                public String getAttributeValue(String code) {
                    return nodeItem.getAttribute(code);
                }

                @Override
                public IndexDefinition getDefinition() {
                    return dialog.getDefinition();
                }

                @Override
                public IndexDefinitionBase.IndexViewProperty getViewDefinition() {
                    return dialog.getIndexView();
                }
            };
        }

        private NodeItemList load() {

            if (nodeItemList == null)
                nodeItemList = layerProvider.getNodeLayer().searchById(buildRequest());

            return nodeItemList;
        }

        private NodeDataRequest buildRequest() {
            NodeDataRequest dataRequest = new NodeDataRequest();

            if (dialog.getCondition() != null)
                dataRequest.setCondition(dialog.getCondition());

            dataRequest.setStartPos(dialog.getStart());
            dataRequest.setLimit(dialog.getLimit());
            dataRequest.setCodeDomainNode(dialog.getDefinition().getCode());
            dataRequest.setSortsBy(new ArrayList<DataRequest.SortBy>());
            dataRequest.setGroupsBy(new ArrayList<DataRequest.GroupBy>());
            dataRequest.setNodeIds(nodeIds());

            return dataRequest;
        }

        private List<String> nodeIds() {
            List<String> list = new ArrayList<>();
            for (Term term : terms)
                list.add(term.getCode());
            return list;
        }
    }
}
