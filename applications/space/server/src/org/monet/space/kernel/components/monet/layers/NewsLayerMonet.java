package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NewsLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.news.Post;
import org.monet.space.kernel.model.news.PostComment;
import org.monet.space.kernel.model.news.seed.NewsSeed;
import org.monet.space.kernel.producers.ProducerPost;
import org.monet.space.kernel.producers.ProducerPostList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NewsLayerMonet extends PersistenceLayerMonet implements NewsLayer {

	public NewsLayerMonet(ComponentPersistence componentPersistenceMonet) {
		super(componentPersistenceMonet);
	}

	@Override
	public void publish(Post post, boolean withNotification) {
		ProducerPost producerPost;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerPost = this.producersFactory.get(Producers.POST);
		producerPost.create(post, withNotification);
	}

	@Override
	public void publish(NewsSeed seed, ArrayList<String> users, ArrayList<String> roles) {
		ProducerPost producerPost;
		Post post;

		try {
			if (!this.isStarted()) {
				throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
			}

			RoleLayer roleLayer = ComponentFederation.getInstance().getRoleLayer();
			producerPost = this.producersFactory.get(Producers.POST);

			post = seed.toPost();
			HashSet<String> idUsers = new HashSet<>();
			for (String userId : users)
				idUsers.add(userId);

			for (String role : roles) {
				List<String> userIds = roleLayer.loadRoleListUsersIds(role);
				for (String userId : userIds)
					idUsers.add(userId);
			}

			for (String userId : idUsers) {
				post.setId(null);
				post.setWallUserId(userId);
				producerPost.create(post, false);
			}
		} catch (Exception e) {
			throw new DataException(ErrorCode.FEDERATION_CONNECTION, null, e);
		}
	}

	@Override
	public void publishToUser(Post post, String userId, boolean withNotification) {
		ProducerPost producerPost;

		try {

			if (!this.isStarted()) {
				throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
			}

			producerPost = this.producersFactory.get(Producers.POST);
			post.setId(null);
			post.setWallUserId(userId);
			producerPost.create(post, withNotification);

		} catch (Exception e) {
			throw new DataException(ErrorCode.FEDERATION_CONNECTION, null, e);
		}
	}

	@Override
	public void publishToRole(Post post, String role, boolean withNotification) {
		ProducerPost producerPost;

		try {

			if (!this.isStarted()) {
				throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
			}

			RoleLayer roleLayer = ComponentFederation.getInstance().getRoleLayer();
			String roleCode = Dictionary.getInstance().getDefinitionCode(role);
			List<String> userIds = roleLayer.loadRoleListUsersIds(roleCode);

			producerPost = this.producersFactory.get(Producers.POST);
			for (String userId : userIds) {
				post.setId(null);
				post.setWallUserId(userId);
				producerPost.create(post, withNotification);
			}
		} catch (Exception e) {
			throw new DataException(ErrorCode.FEDERATION_CONNECTION, null, e);
		}
	}

	@Override
	public void publishToTask(Post post, Task<?> task, boolean withNotification) {
		ProducerPost producerPost;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerPost = this.producersFactory.get(Producers.POST);

		for (String userId : task.getEnrolmentsIdUsers()) {
			post.setWallUserId(userId);
			post.setId(null);
			producerPost.create(post, withNotification);
		}
	}

	@Override
	public Post publishToSeed(NewsSeed seed) {
		ProducerPost producerPost;
		Post post = seed.toPost();

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerPost = this.producersFactory.get(Producers.POST);
		producerPost.create(post, false);

		return post;
	}

	@Override
	public Post loadPost(String postId) {
		ProducerPost producerPost;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerPost = this.producersFactory.get(Producers.POST);
		return producerPost.load(postId);
	}

	public void addCommentToPost(String postId, PostComment comment) {
		ProducerPost producerPost;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerPost = this.producersFactory.get(Producers.POST);
		producerPost.addComment(postId, comment);
	}

	@Override
	public void addFilterPost(String postId) {
		ProducerPost producerPost;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerPost = this.producersFactory.get(Producers.POST);
		producerPost.addPostFilter(postId);
	}

	@Override
	public void addFilterPostByAuthor(String authorId) {
		ProducerPost producerPost;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerPost = this.producersFactory.get(Producers.POST);
		producerPost.addAuthorFilter(authorId);
	}

	@Override
	public ArrayList<Post> getPosts(int startPos, int limit) {
		ProducerPostList producerPostList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerPostList = this.producersFactory.get(Producers.POSTLIST);
		return producerPostList.load(startPos, limit);
	}

}
