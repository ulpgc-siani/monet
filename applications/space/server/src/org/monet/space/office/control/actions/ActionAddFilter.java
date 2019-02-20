/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

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

package org.monet.space.office.control.actions;

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NewsLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.news.Post;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionAddFilter extends Action {
	private NewsLayer newsLayer;

	public ActionAddFilter() {
		super();
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.newsLayer = componentPersistence.getNewsLayer();
	}

	public String execute() {
		String nodeId = LibraryRequest.getParameter(Parameter.ID, this.request);
		String postId = LibraryRequest.getParameter(Parameter.POST_ID, this.request);
		String filterId = LibraryRequest.getParameter(Parameter.FILTER, this.request);

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if ((nodeId == null) || (postId == null) || (filterId == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.ADD_COMMENT_TO_POST);
		}

		Post post = this.newsLayer.loadPost(postId);

		Post.Filter filter = Post.Filter.valueOf(filterId);
		switch (filter) {
			case AUTHOR:
				if (post.getType() == Post.USER_POST)
					this.newsLayer.addFilterPostByAuthor(post.getTarget().getId());
				break;
			case MESSAGE:
				this.newsLayer.addFilterPost(postId);
				break;
		}

		return "done";
	}

}