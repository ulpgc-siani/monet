package org.monet.space.explorer.model;

import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Dictionary;

public class Space {
	private String name;
	private String title;
	private String subTitle;
	private String modelLogoUrl;
	private String language;
	private String theme;
	private String instanceId;
	private String initialAction;
	private Federation federation;
	private Account account;
	private Configuration configuration;
	private Tab[] tabs;
	private Dictionary dictionary;

	public Space(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getModelLogoUrl() {
		return modelLogoUrl;
	}

	public void setModelLogoUrl(String modelLogo) {
		this.modelLogoUrl = modelLogo;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInitialAction() {
		return initialAction;
	}

	public void setInitialAction(String initialAction) {
		this.initialAction = initialAction;
	}

	public Federation getFederation() {
		return federation;
	}

	public void setFederation(Federation federation) {
		this.federation = federation;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Tab[] getTabs() {
		return tabs;
	}

	public void setTabs(Tab[] tabs) {
		this.tabs = tabs;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}
}
