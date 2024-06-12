package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.NewsService;
import org.monet.bpi.Post;
import org.monet.bpi.Task;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;
import org.apache.commons.lang.NotImplementedException;

public class NewsServiceImpl extends NewsService {

	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(NewsServiceImpl service) {
		instance = service;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public static void init() {
	}

	@Override
	protected void commentPostImpl(Post post, String text, String author) {
		throw new NotImplementedException();
	}

	@Override
	protected void postToUserImpl(Post bpiPost, String userId) {
		throw new NotImplementedException();
	}

	@Override
	protected void postAndNotifyToUserImpl(Post bpiPost, String userId) {
		throw new NotImplementedException();
	}

	@Override
	protected void postToRoleImpl(Post bpiPost, String role) {
		throw new NotImplementedException();
	}

	@Override
	protected void postAndNotifyToRoleImpl(Post bpiPost, String role) {
		throw new NotImplementedException();
	}

	@Override
	protected void postToTaskTeamImpl(Post bpiPost, Task bpiTask) {
		throw new NotImplementedException();
	}

	@Override
	protected void postAndNotifyToTaskTeamImpl(Post bpiPost, Task bpiTask) {
		throw new NotImplementedException();
	}

	@Override
	protected void postToAllImpl(Post bpiPost) {
		throw new NotImplementedException();
	}

	@Override
	protected void postAndNotifyToAllImpl(Post bpiPost) {
		throw new NotImplementedException();
	}

}