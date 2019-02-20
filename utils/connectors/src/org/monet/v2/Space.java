package org.monet.v2;

import org.monet.api.backservice.BackserviceApi;
import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPITask;
import org.monet.bpi.java.*;
import org.monet.v2.metamodel.Definition;
import org.monet.v2.model.Dictionary;

public class Space extends org.monet.services.Space {
	private String businessModelDir;
	private BackserviceApi api;
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;

	public static final String VERSION = "version2";

	public Space() {
	}

	public void injectBusinessModelDirectory(String businessModelDirectory) {
		this.businessModelDir = businessModelDirectory;
		this.dictionary = new Dictionary();
		dictionary.loadDefinitionList(this.businessModelDir);
		this.bpiClassLocator = new BPIClassLocator(this.businessModelDir + "/classes", dictionary);
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void init() {
		this.initServices();
	}

	public <T extends BPIBaseNode<?>> T getSingleton(String code) {
		Definition definition = this.dictionary.getDefinition(code);
		Node node = this.api.locateNode(definition.getCode());

		BPIBaseNodeImpl<?> bpiNode = this.bpiClassLocator.getDefinitionInstance(definition.getName());
		bpiNode.injectNode(node);
		bpiNode.injectDictionary(this.dictionary);
		bpiNode.injectApi(this.api);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);

		return (T)bpiNode;
	}

	public boolean existsNode(String id) {
		return this.api.existsNode(id);
	}

	public <T extends BPIBaseNode<?>> T get(String id) {
		Node node = this.api.openNode(id);
		Definition definition = this.dictionary.getDefinition(node.getCode());

		BPIBaseNodeImpl<?> bpiNode = this.bpiClassLocator.getDefinitionInstance(definition.getName());
		bpiNode.injectNode(node);
		bpiNode.injectDictionary(this.dictionary);
		bpiNode.injectApi(this.api);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);

		return (T)bpiNode;
	}

	public BPITask getTasks(String code) {
		return null;
	}

	private void initServices() {
		BPIDelivererServiceImpl delivererService = new BPIDelivererServiceImpl();
		delivererService.injectApi(this.api);
		delivererService.injectDictionary(this.dictionary);
		delivererService.injectBPIClassLocator(this.bpiClassLocator);
		BPIDelivererServiceImpl.injectInstance(delivererService);

		BPITaskServiceImpl taskService = new BPITaskServiceImpl();
		taskService.injectApi(this.api);
		taskService.injectDictionary(this.dictionary);
		taskService.injectBPIClassLocator(this.bpiClassLocator);
		BPITaskServiceImpl.injectInstance(taskService);

		BPINodeServiceImpl nodeService = new BPINodeServiceImpl();
		nodeService.injectApi(this.api);
		nodeService.injectDictionary(this.dictionary);
		nodeService.injectBPIClassLocator(this.bpiClassLocator);
		nodeService.injectInstance(nodeService);

		BPIMailServiceImpl mailService = new BPIMailServiceImpl();
		mailService.injectApi(this.api);
		mailService.injectDictionary(this.dictionary);
		mailService.injectBPIClassLocator(this.bpiClassLocator);
		mailService.injectInstance(mailService);

		BPINotificationServiceImpl notificationService = new BPINotificationServiceImpl();
		notificationService.injectApi(this.api);
		notificationService.injectDictionary(this.dictionary);
		notificationService.injectBPIClassLocator(this.bpiClassLocator);
		notificationService.injectInstance(notificationService);

		BPIThesaurusServiceImpl thesaurusService = new BPIThesaurusServiceImpl();
		thesaurusService.injectApi(this.api);
		thesaurusService.injectDictionary(this.dictionary);
		thesaurusService.injectBPIClassLocator(this.bpiClassLocator);
		thesaurusService.injectInstance(thesaurusService);

		BPIClientServiceImpl clientService = new BPIClientServiceImpl();
		clientService.injectApi(this.api);
		clientService.injectDictionary(this.dictionary);
		clientService.injectBPIClassLocator(this.bpiClassLocator);
		clientService.injectInstance(clientService);

		BPIBusinessUnitImpl businessUnit = new BPIBusinessUnitImpl();
		businessUnit.injectApi(this.api);
		businessUnit.injectDictionary(this.dictionary);
		businessUnit.injectBPIClassLocator(this.bpiClassLocator);
		businessUnit.injectInstance(businessUnit);

		BPIFieldFactory fieldFactory = BPIFieldFactory.getInstance();
		fieldFactory.injectApi(this.api);
		fieldFactory.injectDictionary(this.dictionary);
		fieldFactory.injectBPIClassLocator(this.bpiClassLocator);
	}
}
