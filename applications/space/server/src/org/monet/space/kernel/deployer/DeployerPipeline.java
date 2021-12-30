package org.monet.space.kernel.deployer;

import com.google.inject.Injector;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.deployer.problems.Problem;
import org.monet.space.kernel.deployer.stages.*;
import org.monet.space.kernel.guice.InjectorFactory;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.news.seed.BusinessModelInstalled;
import org.monet.space.kernel.model.news.seed.BusinessModelUpdated;
import org.monet.space.kernel.model.news.seed.NewsSeed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeployerPipeline implements GlobalData {

	private HashMap<String, Object> globalData = new HashMap<String, Object>();
	private ArrayList<Problem> problems = new ArrayList<Problem>();
	private ArrayList<Stage> stages = new ArrayList<Stage>();
	private int currentStage = 0;
	private DeployLogger deployLogger;

	public DeployerPipeline(DeployLogger logger) {
		this.deployLogger = logger;
		this.initialize();
	}

	private void initialize() {
		Injector injector = InjectorFactory.getInstance();

		stages.add(new LoadHashTableAndDefinitions());
		stages.add(new Decompress());
		stages.add(new CheckVersion());
		stages.add(new CheckManifests());
		stages.add(new SwapModel());
		stages.add(new AddModelLibraries());
		stages.add(new ReloadBusinessUnit());
		stages.add(new RefreshTaskDefinitions());
		stages.add(new RecalculateDefinitionsSchema());
		stages.add(new RefreshIndexDefinitions());
		stages.add(new UploadDocumentsTemplates());
		stages.add(new InstanceSingletons());
		stages.add(injector.getInstance(InstanceAndRefreshDatastores.class));
		stages.add(injector.getInstance(InstanceAndRefreshDashboards.class));
		stages.add(new SetupSpace());
		stages.add(new RefreshSourceList());
		stages.add(new AssignRoles());
		stages.add(new InstanceScriptTasks());
		stages.add(new Reset());

		for (Stage s : stages) {
			s.setGlobalData(this);
			s.setLogger(deployLogger);
		}
	}

	public boolean hasNextStage() {
		return currentStage < stages.size();
	}

	public Stage getNextStage() {
		if (currentStage < stages.size())
			return stages.get(currentStage++);
		else
			return null;
	}

	public void process() {
		int step = 1;

		this.deployLogger.info("COMPILATION OF A NEW MODEL STARTED...");
		boolean isInstalled = BusinessUnit.getInstance().isInstalled();

		try {
			while (this.hasNextStage()) {
				Stage currentStage = this.getNextStage();

				this.deployLogger.info("Stage %d: " + currentStage.getStepInfo(), step);
				currentStage.execute();
				this.deployLogger.info("Complete.");

				StageState state = currentStage.getState();
				if (state == StageState.COMPLETE_WITH_ERRORS) {
					addProblems(currentStage);
					if (currentStage instanceof CheckVersion)
						break;
				}

				step++;
			}
		} catch (RuntimeException e) {
			this.deployLogger.error(e.getMessage());
			this.deployLogger.info("Compilation failed!");
			throw e;
		}
		if (this.problems.size() == 0) {
			this.deployLogger.info("COMPILATION COMPLETED SUCCESSFULLY");
			this.createNewsPost(isInstalled);
			AgentNotifier.getInstance().notify(new MonetEvent(MonetEvent.MODEL_UPDATED, null, null));
		} else {
			this.deployLogger.info("COMPILATION COMPLETED WITH %d ERRORS", this.problems.size());
			for (Problem p : this.problems)
				this.deployLogger.info(p.toString("\n"));
		}
	}

	private String createNewsPost(boolean isInstalled) {
		NewsSeed seed = null;

		if (!isInstalled)
			seed = new BusinessModelInstalled();
		else
			seed = new BusinessModelUpdated();

		return ComponentPersistence.getInstance().getNewsLayer().publishToSeed(seed).getId();
	}

	private void addProblems(Stage currentStage) {
		for (Problem p : currentStage.getProblems()) {
			p.setStage(currentStage.getClass().getSimpleName());
			problems.add(p);
		}
	}

	public List<Problem> getProblems() {
		return this.problems;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getData(Class<T> type, String key) {
		return (T) globalData.get(key);
	}

	@Override
	public void setData(String key, Object data) {
		globalData.put(key, data);
	}
}
