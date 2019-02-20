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
import org.monet.space.kernel.model.MonetLink;
import org.monet.space.kernel.model.news.Post;
import org.monet.space.kernel.model.news.PostComment;
import org.monet.space.kernel.sql.QueryBuilder;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerPostList extends Producer {

	public ArrayList<Post> load(int startPos, int limit) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		ArrayList<Post> postList = new ArrayList<Post>();
		HashMap<String, Post> postMap = new HashMap<String, Post>();
		ResultSet resultSet = null;

		String userId = this.getUserId();

		try {
			parameters.put(Database.QueryFields.ID_USER, userId);
			parameters.put(Database.QueryFields.START_POS, startPos);
			parameters.put(Database.QueryFields.LIMIT, limit);

			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.POST_LOAD_LIST, parameters);
			while (resultSet.next()) {
				Post post = fill(resultSet);
				postList.add(post);
				postMap.put(post.getId(), post);
			}

			this.agentDatabase.closeQuery(resultSet);

			if (postList.size() == 0)
				return postList;

			String postIds = this.agentDatabase.getRepositoryQuery(Database.Queries.POST_LOAD_LIST_IDS);
			String subquery = "";
			if (postIds.isEmpty()) {
				StringBuilder builder = new StringBuilder();
				for (Post post : postMap.values()) {
					builder.append(post.getId());
					builder.append(",");
				}
				if (builder.length() > 0) builder.delete(builder.length() - 1, builder.length());
				postIds = builder.toString();

				if (postIds.isEmpty())
					subquery = "1=1";
				else {
					QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.POST_LOAD_POSTS_COMMENTS_SUBQUERY_IDS));
					queryBuilder.insertSubQuery(Database.QueryFields.POSTS_IDS, postIds);
					subquery = queryBuilder.build();
				}

				parameters.clear();
			} else {
				QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.POST_LOAD_POSTS_COMMENTS_SUBQUERY_IDS));
				queryBuilder.insertSubQuery(Database.QueryFields.POSTS_IDS, postIds);
				subquery = queryBuilder.build();
			}

			subQueries.put(Database.QueryFields.POSTS_IDS_SUBQUERY, subquery);

			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.POST_LOAD_POSTS_COMMENTS, parameters, subQueries);
			while (resultSet.next()) {
				PostComment comment = fillComment(resultSet);
				postMap.get(comment.getPostId())
					.addComment(comment);
			}

		} catch (Exception e) {
			throw new DataException(ErrorCode.LOAD_POST, userId, e);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return postList;
	}

	private Post fill(ResultSet resultSet) throws Exception {
		Post post = new Post();
		post.setId(resultSet.getString("id"));
		post.setTitle(resultSet.getString("title"));
		post.setBody(resultSet.getString("body"));
		post.setTarget(MonetLink.from(resultSet.getString("target")));
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
		postComment.setPostId(resultSet.getString("id_post"));
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
