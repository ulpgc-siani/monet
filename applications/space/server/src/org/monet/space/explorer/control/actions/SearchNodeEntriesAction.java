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
import org.monet.space.explorer.control.dialogs.SearchNodeEntriesDialog;
import org.monet.space.explorer.control.displays.NodeIndexEntryDisplay;
import org.monet.space.explorer.model.ExplorerList;
import org.monet.space.explorer.model.IndexEntry;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.explorer.model.NodeIndexEntry;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeList;
import org.monet.space.kernel.model.SearchRequest;

import java.io.IOException;

public class SearchNodeEntriesAction extends Action<SearchNodeEntriesDialog, NodeIndexEntryDisplay> {
	private LayerProvider layerProvider;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	public void execute() throws IOException {
	    display.writeList(search());
    }

	private ExplorerList<IndexEntry> search() {
		SearchRequest searchRequest = createSearchRequest();
		ExplorerList<IndexEntry> result = new ExplorerList<>();
		NodeList nodeList = layerProvider.getNodeLayer().search(dialog.getNode(), searchRequest);

		result.setTotalCount(nodeList.getTotalCount());

		for (final Node node : nodeList) {
			final DescriptorDefinition indexDefinition = new DescriptorDefinition();

			result.add(new NodeIndexEntry() {
				@Override
				public Node getEntity() {
					return node;
				}

				@Override
				public String getLabel() {
					return node.getLabel();
				}

				@Override
				public String getAttributeValue(String code) {
					return node.getReference().getAttribute(code).getValueAsString();
				}

				@Override
				public IndexDefinition getDefinition() {
					return indexDefinition;
				}

				@Override
				public IndexDefinitionBase.IndexViewProperty getViewDefinition() {
					return null;
				}

			});
		}

		return result;
	}

	private SearchRequest createSearchRequest() {
		SearchRequest request = new SearchRequest();
		request.setCondition(dialog.getCondition());
		request.setStartPos(dialog.getStart());
		request.setLimit(dialog.getLimit());
		return request;
	}

}