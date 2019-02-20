/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2014  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.explorer.control.actions;


import com.google.inject.Inject;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.IndexDefinitionBase;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.space.explorer.control.dialogs.LoadIndexEntriesDialog;
import org.monet.space.explorer.control.displays.NodeIndexEntryDisplay;
import org.monet.space.explorer.control.exceptions.ReadEntityPermissionException;
import org.monet.space.explorer.model.*;
import org.monet.space.explorer.model.Filter;
import org.monet.space.kernel.model.DataRequest.GroupBy;
import org.monet.space.kernel.model.DataRequest.SortBy;
import org.monet.space.kernel.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadIndexEntriesAction extends Action<LoadIndexEntriesDialog, NodeIndexEntryDisplay> {
    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public void execute() throws IOException {
        LoadIndexEntriesDialog.Scope scope = dialog.getScope();
        IndexEntriesLoader loader = scope instanceof LoadIndexEntriesDialog.NodeScope ? new SetIndexEntriesLoader() : new LinkFieldIndexEntriesLoader();

        if (!loader.canRead(scope))
            throw new ReadEntityPermissionException();

        ExplorerList result = new ExplorerList<>();
        result.setTotalCount(loader.getCount(scope));

        if (result.getTotalCount() > 0)
            for (IndexEntry entry : (List<IndexEntry>)loader.getItems(scope))
                result.add(entry);

        display.writeList(result);
    }

    private List<SortBy> getSortsBy() {
        List<SortBy> result = new ArrayList<>();
        for (Order order : dialog.getOrders())
            result.add(createSortBy(order));
        return result;
    }

    private SortBy createSortBy(final Order order) {
        return new SortBy() {
            @Override
            public String attribute() {
                return order.getName();
            }

            @Override
            public String mode() {
                return order.getMode();
            }
        };
    }

    private List<GroupBy> getGroupsBy() {
        List<Filter> filters = dialog.getFilters();
        List<GroupBy> result = new ArrayList<>();

        for (Filter filter : filters) {
            final List<Object> filterKeys = getFilterKeys(filter);
            if (filterKeys.size() > 0)
                result.add(createGroupBy(filter, filterKeys));
        }

        return result;
    }

    private GroupBy createGroupBy(final Filter filter, final List<Object> filterKeys) {
        return new GroupBy() {
            @Override
            public String attribute() {
                return filter.getName();
            }

            @Override
            public List<Object> values() {
                return filterKeys;
            }

            @Override
            public <T> T value(int pos) {
                return (T) values().get(pos);
            }

            @Override
            public Operator operator() {
                return Operator.Eq;
            }
        };
    }

    private List<Object> getFilterKeys(Filter filter) {
        List<Object> result = new ArrayList<>();

        for (Filter.Option option : filter.getOptions())
            result.add(option.getValue());

        return result;
    }

    public interface IndexEntriesLoader<T extends LoadIndexEntriesDialog.Scope> {
        boolean canRead(T scope);
        int getCount(T scope);
        List<IndexEntry> getItems(T scope);
    }

    private class SetIndexEntriesLoader implements IndexEntriesLoader<LoadIndexEntriesDialog.NodeScope> {

        @Override
        public boolean canRead(LoadIndexEntriesDialog.NodeScope scope) {
            return componentProvider.getComponentSecurity().canRead(scope.getSet(), getAccount());
        }

        @Override
        public int getCount(LoadIndexEntriesDialog.NodeScope scope) {
            return layerProvider.getNodeLayer().requestNodeListItemsCount(scope.getSet().getId(), buildRequest(scope));
        }

        @Override
        public List<IndexEntry> getItems(final LoadIndexEntriesDialog.NodeScope scope) {
            List<IndexEntry> list = new ArrayList<>();
            for (Node node : layerProvider.getNodeLayer().requestNodeListItems(scope.getSet().getId(), buildRequest(scope)).values())
                list.add(createNodeIndexEntry(node));
            return list;
        }

        private NodeIndexEntry createNodeIndexEntry(final Node node) {
            return new NodeIndexEntry() {

                @Override
                public String getLabel() {
                    return node.getLabel();
                }

                @Override
                public Node getEntity() {
                    return node;
                }

                @Override
                public String getAttributeValue(String code) {
                    Reference reference = node.getReference(getDefinition().getCode());
                    return reference.getAttributeValueAsString(code);
                }

                @Override
                public IndexDefinition getDefinition() {
                    return dialog.getDefinition();
                }

                @Override
                public IndexDefinition.IndexViewProperty getViewDefinition() {
                    return dialog.getIndexView();
                }
            };
        }

        private NodeDataRequest buildRequest(LoadIndexEntriesDialog.NodeScope scope) {
            NodeDataRequest dataRequest = new NodeDataRequest();

            if (dialog.getCondition() != null)
                dataRequest.setCondition(dialog.getCondition());

            dataRequest.setStartPos(dialog.getStart());
            dataRequest.setLimit(dialog.getLimit());
            dataRequest.setCodeDomainNode(scope.getSet().getCode());
            dataRequest.setCodeReference(dialog.getDefinition().getCode());
            dataRequest.setCodeView(scope.getSetView().getCode());
            dataRequest.setSortsBy(getSortsBy());
            dataRequest.setGroupsBy(getGroupsBy());

            return dataRequest;
        }

    }

    private class LinkFieldIndexEntriesLoader implements IndexEntriesLoader<LoadIndexEntriesDialog.FieldScope> {
        private NodeItemList nodeItemList = null;

        @Override
        public boolean canRead(LoadIndexEntriesDialog.FieldScope scope) {
            return true;
        }

        @Override
        public int getCount(LoadIndexEntriesDialog.FieldScope scope) {
            return load().getTotalCount();
        }

        @Override
        public List<IndexEntry> getItems(final LoadIndexEntriesDialog.FieldScope scope) {
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
                nodeItemList = layerProvider.getNodeLayer().searchLinkNodeItems(buildRequest());

            return nodeItemList;
        }

        private NodeDataRequest buildRequest() {
            NodeDataRequest dataRequest = new NodeDataRequest();

            if (dialog.getCondition() != null)
                dataRequest.setCondition(dialog.getCondition());

            dataRequest.setStartPos(dialog.getStart());
            dataRequest.setLimit(dialog.getLimit());
            dataRequest.setCodeDomainNode(dialog.getDefinition().getCode());
            dataRequest.setSortsBy(getSortsBy());
            dataRequest.setGroupsBy(getGroupsBy());

            addRequestFilters(dataRequest);

            return dataRequest;
        }

        private void addRequestFilters(NodeDataRequest dataRequest) {
            Map<String, String> filters = new HashMap<>();

            for (GroupBy groupBy : getGroupsBy()) {

                if (groupBy.values().size() <= 0)
                    continue;

                filters.put(groupBy.attribute(), (String) groupBy.value(0));
            }

            dataRequest.addParameter("filters", SerializerData.serialize(filters));
        }
    }

}