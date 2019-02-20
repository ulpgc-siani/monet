package org.monet.space.kernel.machines.bim;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.monet.metamodel.*;
import org.monet.metamodel.DashboardDefinitionBase.DashboardViewProperty;
import org.monet.metamodel.DashboardDefinitionBase.DashboardViewProperty.ShowProperty;
import org.monet.metamodel.DashboardDefinitionBase.DashboardViewProperty.ShowProperty.OlapProperty;
import org.monet.metamodel.DashboardDefinitionBase.IndicatorProperty;
import org.monet.metamodel.DashboardDefinitionBase.IndicatorProperty.LevelProperty;
import org.monet.metamodel.DashboardDefinitionBase.IndicatorProperty.LevelProperty.PrimaryProperty;
import org.monet.metamodel.DashboardDefinitionBase.IndicatorProperty.LevelProperty.PrimaryProperty.OperatorEnumeration;
import org.monet.metamodel.DashboardDefinitionBase.TaxonomyProperty;
import org.monet.metamodel.DashboardDefinitionBase.TaxonomyProperty.ExplicitProperty;
import org.monet.metamodel.DatastoreDefinitionBase.CubeProperty;
import org.monet.metamodel.DatastoreDefinitionBase.CubeProperty.MetricProperty;
import org.monet.metamodel.DatastoreDefinitionBase.CubeProperty.ResolutionEnumeration;
import org.monet.metamodel.DatastoreDefinitionBase.DimensionProperty;
import org.monet.metamodel.DatastoreDefinitionBase.DimensionProperty.FeatureProperty;
import org.monet.metamodel.DatastoreDefinitionBase.DimensionProperty.FeatureProperty.TypeEnumeration;
import org.monet.metamodel.MeasureUnitDefinition.ScaleProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.exceptions.CreateDatastoreSessionException;
import org.monet.space.kernel.exceptions.MountDatastoreException;
import org.monet.space.kernel.guice.InjectorFactory;
import org.monet.space.kernel.log.Logger;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;
import org.sumus.DataWarehouse;
import org.sumus.DataWarehouseException;
import org.sumus.asset.Asset;
import org.sumus.asset.AssetException;
import org.sumus.cube.CubeException;
import org.sumus.datastore.Session;
import org.sumus.dimension.DimensionException;
import org.sumus.dimension.category.CategoryException;
import org.sumus.feature.Feature;
import org.sumus.indicator.CubeIndicator;
import org.sumus.indicator.interpolation.Interpolation;
import org.sumus.indicator.interpolation.InterpolationFactory;
import org.sumus.indicator.operator.OperatorFactory;
import org.sumus.measureunit.MeasureUnitException;
import org.sumus.report.FolderException;
import org.sumus.report.ReportException;
import org.sumus.taxonomy.TaxonomyException;
import org.sumus.taxonomy.builder.ExplicitTaxonomyBuilder;
import org.sumus.taxonomy.builder.ImplicitTaxonomyBuilder;
import org.sumus.taxonomy.builder.TaxonomyBuilder;
import org.sumus.taxonomy.builder.TaxonomyBuilderException;
import org.sumus.time.TimeScale;
import sumus.builder.Builder;

import java.io.InputStream;
import java.util.*;

public class EngineMonet implements Engine {
	@Inject
	private Provider<DataWarehouse> datawareHouseProvider;
	private DataWarehouse datawareHouse = null;
	@Inject
	private Provider<Builder> builderProvider;
	private Builder builder = null;
	@Inject
	private Dictionary dictionary;
	@Inject
	private Logger logger;
	@Inject
	private Provider<Language> languageProvider;
	@Inject
	private AgentNotifier agentNotifier;
	@Inject
	private Language language;

	private Set<String> languages = null;
	private HashMap<String, org.sumus.dictionary.Dictionary> dictionaryMap = new HashMap<String, org.sumus.dictionary.Dictionary>();

	private static final String MONET_FEATURE = "instance";
	private boolean unclosedSessionsChecked = false;

	public boolean isLoaded(Asset dwhAsset) {

		if (dwhAsset.getIndicators().length > 0)
			return true;

		for (org.sumus.cube.Cube dwhCube : dwhAsset.getCubes()) {
			if (dwhCube.getIndicators().length > 0)
				return true;
		}

		for (org.sumus.dimension.Dimension dwhDimension : dwhAsset.getDimensions()) {
			if (dwhDimension.getTaxonomies().length > 0)
				return true;
		}

		return dwhAsset.getReports().length > 0;
	}

	@Override
	public Dashboard getDashboard(String code, final OnLoad onLoad) {
		DataWarehouse datawareHouse = this.getDatawareHouse();
		final DashboardDefinition definition = dictionary.getDashboardDefinition(code);
		String datastoreCode = dictionary.getDefinitionCode(definition.getUse().getValue());
		final Dashboard dashboard = new Dashboard(definition);
		final org.sumus.asset.Asset dwhAsset = datawareHouse.getAsset(datastoreCode);

		dashboard.setCode(dwhAsset.getName());

		if (!this.isLoaded(dwhAsset)) {
			new Timer("Monet-BIM-Engine-Monet-" + dwhAsset.getName()).schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						logger.info("Loading dashboard asset %s", dwhAsset.getName());
						loadIndicators(dwhAsset, definition);
						loadTaxonomies(dwhAsset, definition);
						loadReports(dwhAsset, definition);
						dashboard.injectAsset(dwhAsset);
						onLoad.loaded(dashboard);
						logger.info("Dashboard asset %s loaded", dwhAsset.getName());
					} catch (AssetException e) {
						logger.error(e.getMessage());
					}
				}
			}, 5000);
		}

		return dashboard;
	}

	@Override
	public Dashboard createDashboard(String code, final OnCreate onCreate) {
		return this.getDashboard(code, new OnLoad() {
			@Override
			public void loaded(Dashboard dashboard) {
				onCreate.created(dashboard);
			}
		});
	}

	@Override
	public boolean existsDashboard(String code) {
		DataWarehouse datawareHouse = this.getDatawareHouse();
		DashboardDefinition definition = dictionary.getDashboardDefinition(code);
		String datastoreCode = dictionary.getDefinitionCode(definition.getUse().getValue());
		return datawareHouse.getAsset(datastoreCode) != null;
	}

	@Override
	public Dashboard updateDashboard(DashboardDefinition oldDefinition, DashboardDefinition definition) {
		this.datawareHouse = null;
		return null;
	}

	@Override
	public void removeDashboard(String code) {
	}

	@Override
	public Datastore getDatastore(String code) {
		DataWarehouse datawareHouse = this.getDatawareHouse();
		Datastore datastore = new Datastore();

		org.sumus.asset.Asset dwhAsset = datawareHouse.getAsset(code);
		datastore.setCode(dwhAsset.getName());

		return datastore;
	}

	@Override
	public Datastore updateDatastore(DatastoreDefinition oldDefinition, DatastoreDefinition definition) {
		return null;
	}

	@Override
	public void removeDatastore(String code) {
	}

	@Override
	public synchronized void mountDatastore(String code, List<DatastoreTransaction> transactions) throws CreateDatastoreSessionException, MountDatastoreException {
		DataWarehouse datawareHouse = this.getDatawareHouse();
		Datastore datastore = this.getDatastore(code);
		org.sumus.asset.Asset dwhAsset = datawareHouse.getAsset(code);
		String datastoreLabel = datastore.getDefinition().getLabelString();

		try {
			Session session = createSessionWithTransactions(datastore, transactions);
			if (session == null || !session.hasEvents())
				return;
		}
		catch (Throwable exception) {
			this.logger.error("Error creating sumus session", exception);
			throw new CreateDatastoreSessionException("Sumus mounting error");
		}

		this.logger.info(String.format("Mounting datastore %s of space %s...", datastoreLabel, Context.getInstance().getFrameworkName()));

		try {
			checkUnclosedSessionsAlreadyClosed();
			dwhAsset.getSessionMounter().mount();
			dwhAsset.save();
			agentNotifier.notify(new MonetEvent(MonetEvent.DATASTORE_MOUNTED, null, datastore));
			this.datawareHouse = null;
		} catch (Throwable exception) {
			this.logger.error("Sumus mounting error", exception);
			throw new MountDatastoreException("Sumus mounting error");
		}

		this.logger.info(String.format("Datastore %s of space %s mounted", datastoreLabel, Context.getInstance().getFrameworkName()));
	}

	@Override
	public Datastore createDatastore(String code) {
		DataWarehouse datawareHouse = this.getDatawareHouse();
		Datastore datastore = null;

		try {
			org.sumus.asset.Asset dwhAsset = datawareHouse.createAsset(code);
			DatastoreDefinition definition = dictionary.getDatastoreDefinition(code);

			for (DimensionProperty dimensionDefinition : definition.getDimensionList())
				this.createDimension(dwhAsset, definition, dimensionDefinition);

			for (CubeProperty cubeDefinition : definition.getCubeList())
				this.createCube(dwhAsset, definition, cubeDefinition);

			dwhAsset.save();

			return this.getDatastore(dwhAsset.getName());
		} catch (DataWarehouseException e) {
			this.logger.error(e.getMessage());
		} catch (AssetException e) {
			this.logger.error(e.getMessage());
		}

		return datastore;
	}

	@Override
	public boolean existsDatastore(String code) {
		DataWarehouse datawareHouse = this.getDatawareHouse();
		return datawareHouse.getAsset(code) != null;
	}

	@Override
	public void create() {
	}

	@Override
	public void reset() {
		this.datawareHouse = null;
		this.builder = null;
	}

	@Override
	public void destroy() {
	}

	private void loadIndicators(Asset dwhAsset, DashboardDefinition definition) throws AssetException {

		// Priorize primary indicators
		for (IndicatorProperty indicatorDefinition : definition.getIndicatorList()) {
			LevelProperty levelDefinition = indicatorDefinition.getLevel();
			if (levelDefinition.getPrimary() != null)
				this.createPrimaryIndicator(dwhAsset, definition, indicatorDefinition);
		}

		for (IndicatorProperty indicatorDefinition : definition.getIndicatorList()) {
			LevelProperty levelDefinition = indicatorDefinition.getLevel();
			if (levelDefinition.getSecondary() != null)
				this.createSecondaryIndicator(dwhAsset, definition, indicatorDefinition);
		}

		for (IndicatorProperty indicatorDefinition : definition.getIndicatorList()) {
			LevelProperty levelDefinition = indicatorDefinition.getLevel();
			if (levelDefinition.getSecondaryLocation() != null)
				this.createLocationIndicator(dwhAsset, definition, indicatorDefinition);
		}
	}

	private void createPrimaryIndicator(Asset dwhAsset, DashboardDefinition definition, IndicatorProperty indicatorDefinition) {

		try {
			PrimaryProperty primaryDefinition = indicatorDefinition.getLevel().getPrimary();
			DatastoreDefinition datastoreDefinition = this.dictionary.getDatastoreDefinition(definition.getUse().getValue());
			String cubeCode = this.getIndicatorCubeCode(datastoreDefinition, primaryDefinition);
			CubeProperty cubeDefinition = datastoreDefinition.getCube(cubeCode);
			MetricProperty metricDefinition = cubeDefinition.getMetricMap().get(primaryDefinition.getMetric().getValue());
			String operatorName = this.getOperatorName(primaryDefinition.getOperator());

			org.sumus.cube.Cube dwhCube = dwhAsset.getCube(cubeCode);
			org.sumus.metric.Metric dwhMetric = dwhCube.getMetric(metricDefinition.getCode());
			org.sumus.indicator.CubeIndicatorOperator dwhOperator = org.sumus.indicator.operator.OperatorFactory.createOperator(operatorName, dwhMetric);
			org.sumus.indicator.CubeIndicator dwhIndicator = new org.sumus.indicator.CubeIndicator(indicatorDefinition.getCode(), dwhCube, dwhMetric, dwhOperator);

			this.addInterpolation(dwhIndicator, primaryDefinition);

			for (String languageCode : this.getLanguages()) {
				org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
				dwhDictionary.addLabel(dwhIndicator, language.getModelResource(indicatorDefinition.getLabel(), languageCode));
			}

			dwhCube.add(dwhIndicator);
		} catch (CubeException e) {
			this.logger.error(e.getMessage());
		}

	}

	private void addInterpolation(CubeIndicator dwhIndicator, PrimaryProperty primaryDefinition) {
		Class<? extends Interpolation> clazz = null;
		PrimaryProperty.InterpolationEnumeration interpolationEnumeration = primaryDefinition.getInterpolation();

		if (interpolationEnumeration == null) {
			dwhIndicator.setInterpolationClass(InterpolationFactory.getInterpolationClass(InterpolationFactory.NULL));
			return;
		}

		switch(interpolationEnumeration) {
			case LEFT:
				clazz = InterpolationFactory.getInterpolationClass(InterpolationFactory.LEFT);
				break;
			case LINEAR:
				clazz = InterpolationFactory.getInterpolationClass(InterpolationFactory.LINEAR);
				break;
			case NEAREST:
				clazz = InterpolationFactory.getInterpolationClass(InterpolationFactory.MIDDLE);
				break;
			case NULL:
				clazz = InterpolationFactory.getInterpolationClass(InterpolationFactory.NULL);
				break;
			case RIGHT:
				clazz = InterpolationFactory.getInterpolationClass(InterpolationFactory.RIGHT);
				break;
		}

		if (clazz != null)
			dwhIndicator.setInterpolationClass(clazz);
	}

	private List<Ref> getIndicatorDependencies(IndicatorProperty indicatorDefinition) {
		LevelProperty levelDefinition = indicatorDefinition.getLevel();

		if (levelDefinition.getSecondaryLocation() != null) {
			final LevelProperty.SecondaryLocationProperty locationDefinition = levelDefinition.getSecondaryLocation();
			return new ArrayList<Ref>() {{
				add(locationDefinition.getUseX()); add(locationDefinition.getUseY()); add(locationDefinition.getUseAmount());
			}};
		}
		else if (levelDefinition.getSecondary() != null)
			return levelDefinition.getSecondary().getUse();

		return null;
	}

	private ArrayList<org.sumus.indicator.Indicator> getOtherIndicatorDependencies(Asset dwhAsset, DashboardDefinition definition, IndicatorProperty indicatorDefinition) {
		List<Ref> indicatorsRef = getIndicatorDependencies(indicatorDefinition);
		Map<String, IndicatorProperty> indicatorMap = definition.getIndicatorMap();
		ArrayList<org.sumus.indicator.Indicator> result = new ArrayList<org.sumus.indicator.Indicator>();
		DatastoreDefinition datastoreDefinition = this.dictionary.getDatastoreDefinition(definition.getUse().getValue());

		for (Ref indicatorRef : indicatorsRef) {
			IndicatorProperty dependencyDefinition = indicatorMap.get(indicatorRef.getValue());
			LevelProperty levelDefinition = dependencyDefinition.getLevel();

			if (levelDefinition.getPrimary() != null) {
				String cubeCode = this.getIndicatorCubeCode(datastoreDefinition, levelDefinition.getPrimary());
				result.add(dwhAsset.getCube(cubeCode).getIndicator(dependencyDefinition.getCode()));
			} else if (levelDefinition.getSecondary() != null)
				result.add(dwhAsset.getIndicator(dependencyDefinition.getCode()));
			else if (levelDefinition.getSecondaryLocation() != null)
				result.add(dwhAsset.getIndicator(dependencyDefinition.getCode()));
		}

		return result;
	}

	private void createSecondaryIndicator(Asset dwhAsset, DashboardDefinition definition, IndicatorProperty indicatorDefinition) {

		try {
			String code = indicatorDefinition.getCode();
			MeasureUnitDefinition measureUnitDefinition = this.dictionary.getMeasureUnitDefinition(indicatorDefinition.getMeasureUnit().getValue());

			org.sumus.measureunit.MeasureUnit dwhMeasureUnit = this.registerAssetMeasureUnit(dwhAsset, measureUnitDefinition);
			ArrayList<org.sumus.indicator.Indicator> dwhDependencies = this.getOtherIndicatorDependencies(dwhAsset, definition, indicatorDefinition);
			String indicatorCode = indicatorDefinition.getCode();
			String formulaClassName = dictionary.getDashboardIndicatorFormulaClass(definition, indicatorCode).getName();
			InputStream formulaInputStream = dictionary.getDashboardIndicatorFormulaClassAsStream(definition, indicatorCode);
			org.sumus.indicator.formula.Formula dwhFormula = dwhAsset.registerFormula(formulaClassName, formulaInputStream);

			org.sumus.indicator.Indicator dwhIndicator = dwhAsset.createIndicator(code, dwhDependencies.toArray(new org.sumus.indicator.Indicator[0]), dwhFormula, dwhMeasureUnit);

			for (String languageCode : this.getLanguages()) {
				org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
				dwhDictionary.addLabel(dwhIndicator, language.getModelResource(indicatorDefinition.getLabel(), languageCode));
			}

		} catch (AssetException e) {
			this.logger.error(e.getMessage());
		}

	}

	private void createLocationIndicator(Asset dwhAsset, DashboardDefinition definition, IndicatorProperty indicatorDefinition) {

		try {
			String code = indicatorDefinition.getCode();
			ArrayList<org.sumus.indicator.Indicator> dwhDependencies = this.getOtherIndicatorDependencies(dwhAsset, definition, indicatorDefinition);

			org.sumus.indicator.Indicator dwhIndicator = dwhAsset.createIndicator(code, dwhDependencies.toArray(new org.sumus.indicator.Indicator[0]));

			for (String languageCode : this.getLanguages()) {
				org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
				dwhDictionary.addLabel(dwhIndicator, language.getModelResource(indicatorDefinition.getLabel(), languageCode));
			}

		} catch (AssetException e) {
			this.logger.error(e.getMessage());
		}

	}

	private String getOperatorName(OperatorEnumeration operator) {

		switch (operator) {
			case AVERAGE:
				return OperatorFactory.AVERAGE;
			case COUNT:
				return OperatorFactory.COUNT;
			case DEVIATION:
				return OperatorFactory.DEVIATION;
			case TIME_INTEGRATION:
				return OperatorFactory.TIME_INTEGRATION;
			case SUM:
				return OperatorFactory.SUM;
		}

		return null;
	}

	private void loadTaxonomies(Asset dwhAsset, DashboardDefinition definition) {

		for (TaxonomyProperty taxonomyDefinition : definition.getTaxonomyList()) {
			try {
				String dimensionCode = this.getTaxonomyDimensionCode(definition, taxonomyDefinition);
				String featureCode = this.getTaxonomyFeatureCode(definition, taxonomyDefinition);
				String taxonomyCode = taxonomyDefinition.getCode();
				org.sumus.dimension.Dimension dwhDimension = dwhAsset.getDimension(dimensionCode);
				org.sumus.feature.Feature dwhFeature = dwhDimension.getFeature(featureCode);
				org.sumus.taxonomy.builder.TaxonomyBuilder dwhTaxonomyBuilder;

				if (taxonomyDefinition.getExplicit() != null) {
					String classifierClassName = dictionary.getDashboardTaxonomyClassifierClass(definition, taxonomyCode).getName();
					InputStream classifierInputStream = dictionary.getDashboardTaxonomyClassifierClassAsStream(definition, taxonomyCode);
					org.sumus.taxonomy.classifier.Classifier dwhClassifier = dwhAsset.registerClassifier(classifierClassName, classifierInputStream);
					dwhTaxonomyBuilder = new ExplicitTaxonomyBuilder(taxonomyCode, dwhFeature, dwhClassifier, dwhAsset.getComponentProvider(dwhDimension));
				} else
					dwhTaxonomyBuilder = new ImplicitTaxonomyBuilder(taxonomyCode, dwhFeature, dwhAsset.getComponentProvider(dwhDimension), DashboardDefinition.TaxonomyProperty.HIERARCHY_SEPARATOR);

				this.fillExplicitTaxonomy(dwhAsset, dwhTaxonomyBuilder, taxonomyDefinition);

				org.sumus.taxonomy.Taxonomy dwhTaxonomy = dwhTaxonomyBuilder.build();
				for (String languageCode : this.getLanguages()) {
					org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
					dwhDictionary.addLabel(dwhTaxonomy, language.getModelResource(taxonomyDefinition.getLabel(), languageCode));
				}

				dwhDimension.add(dwhTaxonomy);

			} catch (AssetException e) {
				this.logger.error(e.getMessage());
			} catch (TaxonomyBuilderException e) {
				this.logger.error(e.getMessage());
			} catch (TaxonomyException e) {
				this.logger.error(e.getMessage());
			} catch (CategoryException e) {
				this.logger.error(e.getMessage());
			} catch (DimensionException e) {
				this.logger.error(e.getMessage());
			}
		}

	}

	private org.sumus.indicator.Indicator findDashboardIndicator(String name, Asset dwhAsset) {

		if (dwhAsset.getIndicator(name) != null)
			return dwhAsset.getIndicator(name);

		for (org.sumus.cube.Cube dwhCube : dwhAsset.getCubes()) {
			if (dwhCube.getIndicator(name) != null)
				return dwhCube.getIndicator(name);
		}

		return null;
	}

	private org.sumus.report.Folder createReportFolder(Asset dwhAsset, DashboardDefinition definition, FolderProperty folderDefinition) throws FolderException {
		String folderLabel = language.getModelResource(folderDefinition.getLabel());
		org.sumus.report.Folder dwhFolder = new org.sumus.report.Folder(folderLabel);

		for (Ref indicatorRef : folderDefinition.getIndicator()) {
			String indicatorCode = definition.getIndicatorMap().get(indicatorRef.getValue()).getCode();
			org.sumus.indicator.Indicator dwhIndicator = this.findDashboardIndicator(indicatorCode, dwhAsset);
			dwhFolder.add(dwhIndicator);
		}

		for (FolderProperty childFolderDefinition : folderDefinition.getFolderPropertyList()) {
			org.sumus.report.Folder dwhChildFolder = this.createReportFolder(dwhAsset, definition, childFolderDefinition);
			dwhFolder.add(dwhChildFolder);
		}

		return dwhFolder;
	}

	private void loadReports(Asset dwhAsset, DashboardDefinition definition) {

		try {
			for (DashboardViewProperty viewDefinition : definition.getViewList()) {
				ShowProperty showDefinition = viewDefinition.getShow();
				org.sumus.report.Report dwhReport = dwhAsset.createReport(viewDefinition.getCode());

				if (showDefinition != null && showDefinition.getOlap() != null) {
					OlapProperty olapDefinition = showDefinition.getOlap();
					for (FolderProperty folderDefinition : olapDefinition.getFolderPropertyList()) {
						org.sumus.report.Folder dwhFolder = this.createReportFolder(dwhAsset, definition, folderDefinition);
						dwhReport.add(dwhFolder);
					}
				}
			}
		} catch (FolderException exception) {
			this.logger.error(exception.getMessage());
		} catch (AssetException exception) {
			this.logger.error(exception.getMessage());
		} catch (ReportException exception) {
			this.logger.error(exception.getMessage());
		}

	}

	private org.sumus.dimension.category.Category createExplicitTaxonomyCategory(Asset dwhAsset, CategoryProperty categoryDefinition) throws CategoryException {
		org.sumus.dimension.category.Category dwhCategory = new org.sumus.dimension.category.Category(categoryDefinition.getName());

		for (String languageCode : this.getLanguages()) {
			org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
			dwhDictionary.addLabel(dwhCategory, language.getModelResource(categoryDefinition.getLabel(), languageCode));
		}

		for (CategoryProperty childCategoryDefinition : categoryDefinition.getCategoryPropertyList()) {
			org.sumus.dimension.category.Category dwhChildCategory = this.createExplicitTaxonomyCategory(dwhAsset, childCategoryDefinition);
			dwhCategory.add(dwhChildCategory);
		}

		return dwhCategory;
	}

	private String getTaxonomyDimensionCode(DashboardDefinition dashboardDefinition, TaxonomyProperty taxonomyDefinition) {
		DatastoreDefinition datastoreDefinition = this.dictionary.getDatastoreDefinition(dashboardDefinition.getUse().getValue());
		Ref featureRef = taxonomyDefinition.getFeature();
		String[] fullyQualifiedName = featureRef.getFullQualifiedName().split(Definition.FULLY_QUALIFIED_NAME_SEPARATOR);
		String dimensionName = fullyQualifiedName[fullyQualifiedName.length - 2];
		DimensionProperty dimensionDefinition = datastoreDefinition.getDimension(dimensionName);
		return dimensionDefinition.getCode();
	}

	private String getTaxonomyFeatureCode(DashboardDefinition dashboardDefinition, TaxonomyProperty taxonomyDefinition) {
		DatastoreDefinition datastoreDefinition = this.dictionary.getDatastoreDefinition(dashboardDefinition.getUse().getValue());
		Ref featureRef = taxonomyDefinition.getFeature();
		String[] fullyQualifiedName = featureRef.getFullQualifiedName().split(Definition.FULLY_QUALIFIED_NAME_SEPARATOR);
		String dimensionName = fullyQualifiedName[fullyQualifiedName.length - 2];
		DimensionProperty dimensionDefinition = datastoreDefinition.getDimension(dimensionName);
		FeatureProperty featureDefinition = dimensionDefinition.getFeatureMap().get(featureRef.getValue());
		return featureDefinition.getCode();
	}

	private String getIndicatorCubeCode(DatastoreDefinition datastoreDefinition, PrimaryProperty primaryDefinition) {
		Ref metricRef = primaryDefinition.getMetric();
		String[] fullyQualifiedName = metricRef.getFullQualifiedName().split(Definition.FULLY_QUALIFIED_NAME_SEPARATOR);
		String cubeName = fullyQualifiedName[fullyQualifiedName.length - 2];
		CubeProperty cubeDefinition = datastoreDefinition.getCube(cubeName);
		return cubeDefinition.getCode();
	}

	private void fillExplicitTaxonomy(Asset dwhAsset, TaxonomyBuilder taxonomyBuilder, TaxonomyProperty taxonomyDefinition) throws CategoryException, TaxonomyException, TaxonomyBuilderException {
		ExplicitProperty explicitDefinition = taxonomyDefinition.getExplicit();

		if (explicitDefinition == null)
			return;

		ExplicitTaxonomyBuilder explicitTaxonomyBuilder = (ExplicitTaxonomyBuilder) taxonomyBuilder;
		for (CategoryProperty categoryDefinition : explicitDefinition.getCategoryPropertyList()) {
			org.sumus.dimension.category.Category dwhCategory = this.createExplicitTaxonomyCategory(dwhAsset, categoryDefinition);
			explicitTaxonomyBuilder.add(dwhCategory);
		}
	}

	private void componentToEvent(Datastore datastore, Dimension dimension, DimensionComponent component, org.sumus.datastore.events.ComponentAdded dwhEvent) {
		HashMap<String, ArrayList<Object>> featuresMap = component.getFeatures();

		for (FeatureProperty featureDefinition : dimension.getDefinition().getFeatureList()) {
			String code = featureDefinition.getCode();
			String name = featureDefinition.getName();
			TypeEnumeration type = featureDefinition.getType();
			boolean addExtraFeature = (type == TypeEnumeration.TERM);

			dwhEvent.put(code, this.getEventValues(featuresMap, code, name));

			if (addExtraFeature)
				dwhEvent.put(code + DatastoreDefinition.FeatureProperty.EXTRA_FEATURE_SUFFIX, this.getEventValues(featuresMap, code + DatastoreDefinition.FeatureProperty.EXTRA_FEATURE_SUFFIX, name + DatastoreDefinition.FeatureProperty.EXTRA_FEATURE_SUFFIX));
		}
	}

	private String[] getEventValues(HashMap<String, ArrayList<Object>> featuresMap, String code, String name) {
		ArrayList<Object> values = null;

		if (featuresMap.containsKey(code))
			values = featuresMap.get(code);
		else if (featuresMap.containsKey(name))
			values = featuresMap.get(name);

		if (values == null || values.size() == 0)
			return new String[] { "-" };

		ArrayList<String> result = new ArrayList<String>();
		for (Object value : values) {
			String stringValue = String.valueOf(value);
			result.add(!stringValue.isEmpty() ? stringValue : "-");
		}

		return result.toArray(new String[0]);
	}

	private void factToEvent(Datastore datastore, Cube cube, CubeFact fact, org.sumus.datastore.events.FactAdded dwhEvent) {
		HashMap<String, String> components = fact.getComponents();
		CubeProperty cubeDefinition = cube.getDefinition();
		DatastoreDefinition datastoreDefinition = datastore.getDefinition();
		Map<String, DimensionProperty> dimensionDefinitionMap = datastoreDefinition.getDimensionMap();

		for (Ref dimensionRef : cubeDefinition.getUse()) {
			DimensionProperty dimensionDefinition = dimensionDefinitionMap.get(dimensionRef.getValue());
			String code = dimensionDefinition.getCode();
			String name = dimensionDefinition.getName();
			String value = "-";

			if (components.containsKey(code))
				value = components.get(code);
			else if (components.containsKey(name))
				value = components.get(name);

			if (value.isEmpty())
				value = "-";

			dwhEvent.putComponent(code, value);
		}

		HashMap<String, Double> measures = fact.getMeasures();
		for (MetricProperty metricDefinition : cubeDefinition.getMetricList()) {
			String code = metricDefinition.getCode();
			String name = metricDefinition.getName();
			Double value = 0.0;

			if (measures.containsKey(code))
				value = measures.get(code);
			else if (measures.containsKey(name))
				value = measures.get(name);

			dwhEvent.putMeasure(code, value);
		}
	}

	private synchronized org.sumus.datastore.Session createSessionWithTransactions(Datastore datastore, List<DatastoreTransaction> transactions) {
		org.sumus.datastore.Session dwhSession = createSession(datastore);

		if (dwhSession == null)
			return null;

		for (DatastoreTransaction transaction : transactions) {
			if (transaction.isFact())
				insertFactInSession(datastore, datastore.getCube(transaction.getCode()), (CubeFact)transaction.getItem(), dwhSession);
			else if (transaction.isDimensionComponent())
				insertComponentInSession(datastore, datastore.getDimension(transaction.getCode()), (DimensionComponent) transaction.getItem(), dwhSession);
		}

		dwhSession.commit();
		dwhSession.close();

		return dwhSession;
	}

	private org.sumus.datastore.Session createSession(Datastore datastore) {

		if (datastore.getDefinition().isExternalFed())
			return null;

		Builder builder = this.getBuilder();
		checkUnclosedSessionsAlreadyClosed();

		return builder.createSession(datastore.getCode());
	}

	private void insertComponentInSession(Datastore datastore, Dimension dimension, DimensionComponent component, org.sumus.datastore.Session dwhSession) {
		org.sumus.datastore.events.ComponentAdded dwhEvent = new org.sumus.datastore.events.ComponentAdded(dimension.getCode(), component.getId());
		this.componentToEvent(datastore, dimension, component, dwhEvent);
		dwhSession.register(dwhEvent);
	}

	private void insertFactInSession(Datastore datastore, Cube cube, CubeFact fact, org.sumus.datastore.Session dwhSession) {
		org.sumus.datastore.events.FactAdded dwhEvent = new org.sumus.datastore.events.FactAdded(cube.getCode(), fact.getTime());
		this.factToEvent(datastore, cube, fact, dwhEvent);
		dwhSession.register(dwhEvent);
	}

	private void checkUnclosedSessionsAlreadyClosed() {
		Builder builder = this.getBuilder();
		Engine engine = InjectorFactory.getInstance().getInstance(Engine.class);
		Dictionary dictionary = Dictionary.getInstance();

		if (this.unclosedSessionsChecked)
			return;

		for (DatastoreDefinition datastoreDefinition : dictionary.getDatastoreDefinitionList()) {
			String datastoreCode = datastoreDefinition.getCode();

			if (engine.existsDatastore(datastoreCode)) {
				Datastore datastore = engine.getDatastore(datastoreCode);

				if (datastore.getDefinition().isExternalFed())
					return;

				String datastoreLabel = datastore.getDefinition().getLabelString();;
				this.logger.info(String.format("Closing unclosed sessions of datastore %s of space %s...", datastoreLabel, Context.getInstance().getFrameworkName()));
				builder.closeSessions(datastoreCode);
				this.logger.info(String.format("Unclosed sessions of datastore %s of space %s closed!", datastoreLabel, Context.getInstance().getFrameworkName()));
			}
		}

		this.unclosedSessionsChecked = true;
	}

	private Dimension createDimension(org.sumus.asset.Asset dwhAsset, DatastoreDefinition datastoreDefinition, DimensionProperty definition) {
		Dimension dimension = new Dimension(definition.getCode());
		org.sumus.dimension.Dimension dwhDimension;

		try {
			dwhDimension = dwhAsset.createDimension(definition.getCode());

			for (String languageCode : this.getLanguages()) {
				org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
				dwhDictionary.addLabel(dwhDimension, language.getModelResource(definition.getLabel(), languageCode));
			}

			for (String languageCode : this.getLanguages()) {
				org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
				dwhDictionary.addDescription(dwhDimension, language.getModelResource(definition.getLabel(), languageCode));
			}

			this.addDimensionFeatures(dwhAsset, dwhDimension, definition);

		} catch (AssetException e) {
			this.logger.error(e.getMessage());
		} catch (DimensionException e) {
			this.logger.error(e.getMessage());
		}

		return dimension;
	}

	private void addDimensionFeatures(org.sumus.asset.Asset dwhAsset, org.sumus.dimension.Dimension dwhDimension, DimensionProperty definition) throws DimensionException {

		for (FeatureProperty featureDefinition : definition.getFeatureList()) {
			FeatureProperty.TypeEnumeration typeDefinition = featureDefinition.getType();
			int type = -1;
			boolean addExtraFeature = false;

			switch (typeDefinition) {
				case INTEGER:
					type = Feature.INT;
					break;
				case REAL:
					type = Feature.DOUBLE;
					break;
				case BOOLEAN:
				case STRING:
					type = Feature.ENUM;
					break;
				case TERM:
					addExtraFeature = true;
					type = Feature.ENUM;
					break;
			}

			this.addDimensionFeature(dwhAsset, dwhDimension, featureDefinition.getCode(), featureDefinition.getLabel(), type);

			if (addExtraFeature)
				this.addDimensionFeature(dwhAsset, dwhDimension, featureDefinition.getCode() + DatastoreDefinition.FeatureProperty.EXTRA_FEATURE_SUFFIX, featureDefinition.getLabel(), Feature.ENUM);
		}

		Feature dwhFeature = new Feature(MONET_FEATURE, Feature.ENUM);
		dwhDimension.add(dwhFeature);
	}

	private void addDimensionFeature(org.sumus.asset.Asset dwhAsset, org.sumus.dimension.Dimension dwhDimension, String code, Object label, int type) throws DimensionException {
		Language language = languageProvider.get();
		Feature dwhFeature = new Feature(code, type);

		for (String languageCode : this.getLanguages()) {
			org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
			dwhDictionary.addLabel(dwhFeature, language.getModelResource(label, languageCode));
		}

		dwhDimension.add(dwhFeature);
	}

	private Cube createCube(org.sumus.asset.Asset dwhAsset, DatastoreDefinition datastoreDefinition, CubeProperty definition) {
		Cube cube = new Cube(definition.getCode());
		org.sumus.cube.Cube dwhCube;

		try {
			dwhCube = dwhAsset.createCube(definition.getCode(), this.getCubeDefinitionScale(definition));

			this.addCubeDimensions(dwhAsset, dwhCube, datastoreDefinition, definition);
			this.addCubeMetrics(dwhAsset, dwhCube, datastoreDefinition, definition);

		} catch (AssetException e) {
			this.logger.error(e.getMessage());
		} catch (CubeException e) {
			this.logger.error(e.getMessage());
		}

		return cube;
	}

	private void addCubeDimensions(org.sumus.asset.Asset dwhAsset, org.sumus.cube.Cube dwhCube, DatastoreDefinition datastoreDefinition, CubeProperty definition) throws CubeException {
		for (Ref dimensionRef : definition.getUse()) {
			DimensionProperty dimensionDefinition = datastoreDefinition.getDimensionMap().get(dimensionRef.getValue());
			org.sumus.dimension.Dimension dwhDimension = dwhAsset.getDimension(dimensionDefinition.getCode());
			dwhCube.add(dwhDimension);
		}
	}

	private void addCubeMetrics(org.sumus.asset.Asset dwhAsset, org.sumus.cube.Cube dwhCube, DatastoreDefinition datastoreDefinition, CubeProperty definition) throws CubeException {
		Language language = languageProvider.get();

		for (MetricProperty metricDefinition : definition.getMetricList()) {
			String measureUnitKey = metricDefinition.getScale().getDefinition();
			String scaleKey = metricDefinition.getScale().getValue();
			MeasureUnitDefinition measureUnitDefinition = dictionary.getMeasureUnitDefinition(measureUnitKey);
			org.sumus.measureunit.MeasureUnit dwhMeasureUnit = this.registerAssetMeasureUnit(dwhAsset, measureUnitDefinition);
			ScaleProperty scaleDefinition = measureUnitDefinition.getScaleMap().get(scaleKey);
			org.sumus.measureunit.Scale dwhScale = dwhMeasureUnit.getScale(scaleDefinition.getCode());
			org.sumus.metric.Metric dwhMetric = new org.sumus.metric.Metric(metricDefinition.getCode(), dwhMeasureUnit, dwhScale);

			for (String languageCode : this.getLanguages()) {
				org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
				dwhDictionary.addLabel(dwhMetric, language.getModelResource(metricDefinition.getLabel(), languageCode));
			}

			dwhCube.add(dwhMetric);
		}
	}

	private org.sumus.measureunit.MeasureUnit registerAssetMeasureUnit(org.sumus.asset.Asset dwhAsset, MeasureUnitDefinition definition) {
		org.sumus.measureunit.MeasureUnit dwhMeasureUnit = null;

		try {
			String measureUnitCode = definition.getCode();
			dwhMeasureUnit = dwhAsset.getMeasureUnit(measureUnitCode);

			if (dwhMeasureUnit != null)
				return dwhMeasureUnit;

			dwhMeasureUnit = dwhAsset.createMeasureUnit(definition.getCode());

			for (String languageCode : this.getLanguages()) {
				org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
				dwhDictionary.addLabel(dwhMeasureUnit, language.getModelResource(definition.getLabel(), languageCode));
			}

			for (ScaleProperty scaleDefinition : definition.getScaleList()) {
				org.sumus.measureunit.Scale dwhScale = new org.sumus.measureunit.Scale(scaleDefinition.getCode(), Double.valueOf(scaleDefinition.getMax()), Double.valueOf(scaleDefinition.getFactor()));

				for (String languageCode : this.getLanguages()) {
					org.sumus.dictionary.Dictionary dwhDictionary = this.getDictionary(dwhAsset, languageCode);
					dwhDictionary.addLabel(dwhScale, language.getModelResource(scaleDefinition.getLabel(), languageCode));
				}

				dwhMeasureUnit.addScale(dwhScale);
			}

			dwhAsset.save();

		} catch (AssetException e) {
			this.logger.error(e.getMessage());
		} catch (MeasureUnitException e) {
			this.logger.error(e.getMessage());
		}

		return dwhMeasureUnit;
	}

	private TimeScale getCubeDefinitionScale(CubeProperty definition) {
		ResolutionEnumeration resolution = definition.getResolution();
		TimeScale scale = null;

		switch (resolution) {
			case YEARS:
				scale = TimeScale.YEAR;
				break;
			case MONTHS:
				scale = TimeScale.MONTH;
				break;
			case DAYS:
				scale = TimeScale.DAY;
				break;
			case HOURS:
				scale = TimeScale.HOUR;
				break;
			case MINUTES:
				scale = TimeScale.MINUTE;
				break;
			case SECONDS:
				scale = TimeScale.SECOND;
				break;
		}

		return scale;
	}

	private Set<String> getLanguages() {
		if (this.languages != null)
			return this.languages;

		this.languages = dictionary.getLanguages();

		return this.languages;
	}

	private DataWarehouse getDatawareHouse() {

		if (this.datawareHouse == null)
			this.datawareHouse = datawareHouseProvider.get();

		return this.datawareHouse;
	}

	private Builder getBuilder() {

		if (this.builder == null)
			this.builder = builderProvider.get();

		return this.builder;
	}

	private org.sumus.dictionary.Dictionary getDictionary(org.sumus.asset.Asset dwhAsset, String language) {
		if (this.dictionaryMap.containsKey(language))
			return this.dictionaryMap.get(language);

		org.sumus.dictionary.Dictionary dwhDictionary = dwhAsset.getDictionary(language);
		this.dictionaryMap.put(language, dwhDictionary);

		return dwhDictionary;
	}

}
