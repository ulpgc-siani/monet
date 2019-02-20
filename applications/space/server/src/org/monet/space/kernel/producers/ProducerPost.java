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

package org.monet.space.kernel.producers;

import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.MonetLink;
import org.monet.space.kernel.model.news.Post;
import org.monet.space.kernel.model.news.PostComment;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerPost extends Producer {

	public void create(Post post, boolean withNotification) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.TITLE, post.getTitle());
		parameters.put(Database.QueryFields.BODY, post.getBody());
		parameters.put(Database.QueryFields.TARGET, post.getTarget() != null ? post.getTarget().toString() : null);
		parameters.put(Database.QueryFields.TARGET_CODE, post.getTargetCode());
		parameters.put(Database.QueryFields.TAG, post.getTag());
		parameters.put(Database.QueryFields.BODY_TARGET, post.getBodyTarget() != null ? post.getBodyTarget().toString() : null);
		parameters.put(Database.QueryFields.WALL_USER_ID, post.getWallUserId());
		parameters.put(Database.QueryFields.TYPE, post.getType());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(post.getInternalCreateDate()));

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.POST_CREATE, parameters);

		post.setId(id);

		MonetEvent event = new MonetEvent(MonetEvent.POST_CREATED, null, post);
		event.addParameter(MonetEvent.PARAMETER_ADD_NOTIFICATION, withNotification);

		this.agentNotifier.notify(event);
	}

	public void save(Post post) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, post.getId());
		parameters.put(Database.QueryFields.TITLE, post.getTitle());
		parameters.put(Database.QueryFields.BODY, post.getBody());
		parameters.put(Database.QueryFields.TARGET, post.getTarget() != null ? post.getTarget().toString() : null);
		parameters.put(Database.QueryFields.TARGET_CODE, post.getTargetCode());
		parameters.put(Database.QueryFields.TAG, post.getTag());
		parameters.put(Database.QueryFields.BODY_TARGET, post.getBodyTarget() != null ? post.getBodyTarget().toString() : null);
		parameters.put(Database.QueryFields.WALL_USER_ID, post.getWallUserId());
		parameters.put(Database.QueryFields.TYPE, post.getType());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(post.getInternalCreateDate()));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.POST_SAVE, parameters);
	}

	public void addComment(String postId, PostComment comment) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.TEXT, comment.getText());
		parameters.put(Database.QueryFields.ID_AUTHOR, comment.getAuthorId());
		parameters.put(Database.QueryFields.AUTHOR, comment.getAuthor());
		parameters.put(Database.QueryFields.ID_POST, postId);
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(comment.getInternalCreateDate()));

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.POST_COMMENT_CREATE, parameters);

		comment.setId(id);

		Post post = this.load(postId);
		parameters.clear();
		parameters.put(MonetEvent.PARAMETER_POST, post);
		this.agentNotifier.notify(new MonetEvent(MonetEvent.POST_COMMENT_CREATED, null, comment, parameters));
	}

	public void addPostFilter(String postId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_USER, this.getUserId());
		parameters.put(Database.QueryFields.ID_POST, postId);

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.POST_FILTER_POST_ADD, parameters);
	}

	public void addAuthorFilter(String authorId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_USER, this.getUserId());
		parameters.put(Database.QueryFields.ID_AUTHOR, authorId);

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.POST_FILTER_AUTHOR_ADD, parameters);
	}

	public Post load(String id) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Post post = null;
		ResultSet resultSet = null;

		try {
			parameters.put(Database.QueryFields.ID, id);

			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.POST_LOAD, parameters);
			if (resultSet.next()) {
				post = fill(resultSet);
			}

			this.agentDatabase.closeQuery(resultSet);

			parameters.clear();
			parameters.put(Database.QueryFields.ID_POST, id);

			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.POST_LOAD_COMMENTS, parameters);
			ArrayList<PostComment> comments = new ArrayList<PostComment>();
			while (resultSet.next()) {
				comments.add(fillComment(resultSet));
			}
			post.setComments(comments);

		} catch (Exception e) {
			throw new DataException(ErrorCode.LOAD_POST, id, e);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return post;
	}

	private Post fill(ResultSet resultSet) throws Exception {
		Post post = new Post();
		post.setId(resultSet.getString("id"));
		post.setTitle(resultSet.getString("title"));
		post.setBody(resultSet.getString("body"));
		post.setTarget(MonetLink.from(resultSet.getString("target")));
		post.setTargetCode(resultSet.getString("target_code"));
		post.setTag(resultSet.getString("tag"));
		post.setBodyTarget(MonetLink.from(resultSet.getString("body_target")));
		post.setWallUserId(resultSet.getString("wall_user_id"));
		post.setType(resultSet.getInt("type"));
		post.setCreateDate(resultSet.getTimestamp("create_date"));
		return post;
	}

	private PostComment fillComment(ResultSet resultSet) throws Exception {
		PostComment postComment = new PostComment();
		postComment.setId(resultSet.getString("id"));
		postComment.setText(resultSet.getString("text"));
		postComment.setAuthorId(resultSet.getString("id_author"));
		postComment.setAuthor(resultSet.getString("author"));
		postComment.setCreateDate(resultSet.getTimestamp("create_date"));
		return postComment;
	}

	public Object newObject() {
		return new Post();
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
