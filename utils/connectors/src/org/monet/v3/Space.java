package org.monet.v3;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.Node;
import org.monet.api.space.backservice.impl.model.Task;
import org.monet.bpi.Source;
import org.monet.bpi.java.*;
import org.monet.metamodel.Definition;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.SourceDefinition;
import org.monet.v3.model.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class Space extends org.monet.services.Space {
	private String businessModelDir;
	private BackserviceApi api;
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;

	public static final String VERSION = "version3";

	public Space() {
	}

	public void injectBusinessModelDirectory(String businessModelDirectory) {
		this.businessModelDir = businessModelDirectory;
		this.dictionary = new Dictionary();
		org.monet.metamodel.Dictionary.injectCurrentInstance(dictionary);
		dictionary.initialize(this.businessModelDir);
		this.bpiClassLocator = new BPIClassLocator();
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void init() {
		this.initServices();
	}

	public Dictionary getDictionary() {
		return this.dictionary;
	}

	public <T extends org.monet.bpi.Node> List<T> getSingletons() {
		List<T> result = new ArrayList<>();
		List<NodeDefinition> singletonDefinitionList = this.dictionary.getSingletonDefinitionList();

		for (NodeDefinition nodeDefinition : singletonDefinitionList)
			result.add((T) getSingleton(nodeDefinition.getCode()));

		return result;
	}

	public <T extends org.monet.bpi.Node> T getSingleton(String code) {
		Definition definition = this.dictionary.getDefinition(code);
		Node node = this.api.locateNode(definition.getCode(), 0);

		NodeImpl bpiNode = bpiClassLocator.instantiateBehaviour(definition);
		bpiNode.injectNode(node);
		bpiNode.injectApi(this.api);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);
		bpiNode.injectDictionary(this.dictionary);

		return (T)bpiNode;
	}

	public boolean existsNode(String id) {
		return this.api.existsNode(id);
	}

	public <T extends org.monet.bpi.Node> T get(String id) {
		Node node = this.api.openNode(id);
		Definition definition = this.dictionary.getDefinition(node.getCode());

		NodeImpl bpiNode = bpiClassLocator.instantiateBehaviour(definition);
		bpiNode.injectNode(node);
		bpiNode.injectApi(this.api);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);
		bpiNode.injectDictionary(this.dictionary);

		return (T)bpiNode;
	}

	public <T extends org.monet.bpi.Task> T getTask(String id) {
		Task task = this.api.openTask(id);
		Definition definition = this.dictionary.getDefinition(task.getCode());

		TaskImpl bpiTask = bpiClassLocator.instantiateBehaviour(definition);
		bpiTask.injectTask(task);
		bpiTask.injectApi(this.api);
		bpiTask.injectBPIClassLocator(this.bpiClassLocator);
		bpiTask.injectDictionary(this.dictionary);

		return (T)bpiTask;
	}

	public <T extends org.monet.bpi.Source> List<T> locateSources() {
		List<T> result = new ArrayList<>();
		List<SourceDefinition> sourceDefinitionList = this.dictionary.getSourceDefinitionList();

		for (SourceDefinition sourceDefinition : sourceDefinitionList)
			result.add((T) locateSource(sourceDefinition.getCode()));

		return result;
	}

	public Source locateSource(String key) {
		SourceDefinition definition = (SourceDefinition)this.dictionary.getDefinition(key);
		org.monet.api.space.backservice.impl.model.Source source = this.api.locateSource(definition.getCode(), null);

		if (source == null) return null;

		return sourceToBpiSource(definition, source);
	}

	public Source getSource(String id) {
		org.monet.api.space.backservice.impl.model.Source source = this.api.loadSource(id);
		SourceDefinition definition = (SourceDefinition)this.dictionary.getDefinition(source.getCode());

		if (source == null) return null;

		return sourceToBpiSource(definition, source);
	}

	private SourceImpl sourceToBpiSource(SourceDefinition definition, org.monet.api.space.backservice.impl.model.Source source) {
		SourceImpl bpiSource = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiSource.injectSource(source);
		bpiSource.injectApi(this.api);
		bpiSource.injectDictionary(this.dictionary);
		bpiSource.injectBPIClassLocator(this.bpiClassLocator);
		return bpiSource;
	}

	private void initServices() {
		ClientServiceImpl clientService = new ClientServiceImpl();
		clientService.injectApi(this.api);
		clientService.injectDictionary(this.dictionary);
		clientService.injectBPIClassLocator(this.bpiClassLocator);
		clientService.injectInstance(clientService);

		ConsoleServiceImpl consoleService = new ConsoleServiceImpl();
		consoleService.injectApi(this.api);
		consoleService.injectDictionary(this.dictionary);
		consoleService.injectBPIClassLocator(this.bpiClassLocator);
		consoleService.injectInstance(consoleService);

		DatastoreServiceImpl datastoreService = new DatastoreServiceImpl();
		datastoreService.injectApi(this.api);
		datastoreService.injectDictionary(this.dictionary);
		datastoreService.injectBPIClassLocator(this.bpiClassLocator);
		datastoreService.injectInstance(datastoreService);

		DelivererServiceImpl delivererService = new DelivererServiceImpl();
		delivererService.injectApi(this.api);
		delivererService.injectDictionary(this.dictionary);
		delivererService.injectBPIClassLocator(this.bpiClassLocator);
		delivererService.injectInstance(delivererService);

		ExporterServiceImpl exporterService = new ExporterServiceImpl();
		exporterService.injectApi(this.api);
		exporterService.injectDictionary(this.dictionary);
		exporterService.injectBPIClassLocator(this.bpiClassLocator);
		exporterService.injectInstance(exporterService);

		FileServiceImpl fileService = new FileServiceImpl();
		fileService.injectApi(this.api);
		fileService.injectDictionary(this.dictionary);
		fileService.injectBPIClassLocator(this.bpiClassLocator);
		fileService.injectInstance(fileService);
		fileService.injectBusinessModelResourcesDir(this.businessModelDir + "/resources");

		ImporterServiceImpl importerService = new ImporterServiceImpl();
		importerService.injectApi(this.api);
		importerService.injectDictionary(this.dictionary);
		importerService.injectBPIClassLocator(this.bpiClassLocator);
		importerService.injectInstance(importerService);

		JobServiceImpl jobService = new JobServiceImpl();
		jobService.injectApi(this.api);
		jobService.injectDictionary(this.dictionary);
		jobService.injectBPIClassLocator(this.bpiClassLocator);
		jobService.injectInstance(jobService);

		MailServiceImpl mailService = new MailServiceImpl();
		mailService.injectApi(this.api);
		mailService.injectDictionary(this.dictionary);
		mailService.injectBPIClassLocator(this.bpiClassLocator);
		mailService.injectInstance(mailService);

		NewsServiceImpl newsService = new NewsServiceImpl();
		newsService.injectApi(this.api);
		newsService.injectDictionary(this.dictionary);
		newsService.injectBPIClassLocator(this.bpiClassLocator);
		newsService.injectInstance(newsService);

		NodeServiceImpl nodeService = new NodeServiceImpl();
		nodeService.injectApi(this.api);
		nodeService.injectDictionary(this.dictionary);
		nodeService.injectBPIClassLocator(this.bpiClassLocator);
		nodeService.injectInstance(nodeService);

		RoleServiceImpl roleService = new RoleServiceImpl();
		roleService.injectApi(this.api);
		roleService.injectDictionary(this.dictionary);
		roleService.injectBPIClassLocator(this.bpiClassLocator);
		roleService.injectInstance(roleService);

		SourceServiceImpl sourceService = new SourceServiceImpl();
		sourceService.injectApi(this.api);
		sourceService.injectDictionary(this.dictionary);
		sourceService.injectBPIClassLocator(this.bpiClassLocator);
		sourceService.injectInstance(sourceService);

		TaskServiceImpl taskService = new TaskServiceImpl();
		taskService.injectApi(this.api);
		taskService.injectDictionary(this.dictionary);
		taskService.injectBPIClassLocator(this.bpiClassLocator);
		taskService.injectInstance(taskService);

		TranslationServiceImpl translationService = new TranslationServiceImpl();
		translationService.injectApi(this.api);
		translationService.injectDictionary(this.dictionary);
		translationService.injectBPIClassLocator(this.bpiClassLocator);
		translationService.injectInstance(translationService);

		FieldFactory fieldFactory = FieldFactory.getInstance();
		fieldFactory.injectApi(this.api);
		fieldFactory.injectDictionary(this.dictionary);
		fieldFactory.injectBPIClassLocator(this.bpiClassLocator);
	}
}
