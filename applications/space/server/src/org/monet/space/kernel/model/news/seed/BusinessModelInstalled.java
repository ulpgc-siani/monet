package org.monet.space.kernel.model.news.seed;

import org.monet.space.kernel.constants.LabelCode;
import org.monet.space.kernel.model.Language;
import org.monet.space.kernel.model.news.Post;

import java.util.Date;

public class BusinessModelInstalled implements NewsSeed {

	@Override
	public Post toPost() {
		Post post = new Post();
		post.setTitle(Language.getInstance().getLabel(LabelCode.POST_BUSINESS_MODEL_INSTALLED, Language.getCurrent()));
		post.setType(Post.BUSINESS_MODEL_INSTALLED);
		post.setBody("");
		post.setCreateDate(new Date());
		return post;
	}

}
