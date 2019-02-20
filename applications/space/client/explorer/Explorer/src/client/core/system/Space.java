package client.core.system;

import client.core.model.factory.EntityFactory;

import static client.core.model.Instance.ClassName;

public class Space implements client.core.model.Space {
	private String name;
	private String title;
	private String subTitle;
	private String modelLogoUrl;
	private String language;
	private String theme;
	private String instanceId;
	private Action initialAction;
	private Federation federation;
	private client.core.model.Account account;
	private Configuration configuration;
	private client.core.model.List<client.core.model.Space.Tab> tabs;
	private EntityFactory entityFactory;
	private client.core.model.definition.Dictionary dictionary;

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	@Override
	public String getModelLogoUrl() {
		return modelLogoUrl;
	}

	public void setModelLogoUrl(String modelLogoUrl) {
		this.modelLogoUrl = modelLogoUrl;
	}

	@Override
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Override
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public Action getInitialAction() {
		return initialAction;
	}

	public void setInitialAction(Action initialAction) {
		this.initialAction = initialAction;
	}

	@Override
	public Federation getFederation() {
		return this.federation;
	}

	public void setFederation(Federation federation) {
		this.federation = federation;
	}

	@Override
	public client.core.model.Account getAccount() {
		return this.account;
	}

	public void setAccount(client.core.model.Account account) {
		this.account = account;
	}

	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public client.core.model.List<Tab> getTabs() {
		return this.tabs;
	}

	public void setTabs(client.core.model.List<client.core.model.Space.Tab> tabs) {
		this.tabs = tabs;
	}

	@Override
	public final ClassName getInstanceClass() {
		return client.core.model.Space.CLASS_NAME;
	}

	@Override
	public final client.core.model.factory.EntityFactory getEntityFactory() {
		return this.entityFactory;
	}

	public void setEntityFactory(EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}

	@Override
	public client.core.model.definition.Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(client.core.model.definition.Dictionary dictionary) {
		this.dictionary = dictionary;
	}
}
