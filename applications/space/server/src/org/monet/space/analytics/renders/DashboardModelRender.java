package org.monet.space.analytics.renders;

import org.monet.metamodel.DashboardDefinition;
import org.monet.metamodel.DashboardDefinitionBase;
import org.monet.metamodel.DashboardDefinitionBase.IndicatorProperty.FilterProperty.ResolutionEnumeration;
import org.monet.metamodel.DatastoreDefinition;
import org.monet.metamodel.DatastoreDefinitionBase.CubeProperty;
import org.monet.space.analytics.model.Language;
import org.monet.space.kernel.model.Dashboard;
import org.monet.space.kernel.model.Dictionary;
import org.sumus.asset.Asset;
import org.sumus.cube.Cube;
import org.sumus.dimension.Dimension;
import org.sumus.dimension.time.TimeDimension;
import org.sumus.indicator.CompositeIndicator;
import org.sumus.indicator.Indicator;
import org.sumus.indicator.IndicatorList;
import org.sumus.measureunit.MeasureUnit;
import org.sumus.report.Folder;
import org.sumus.report.Report;
import org.sumus.source.QuerySource;
import org.sumus.taxonomy.Taxonomy;
import org.sumus.time.TimeLapse;
import org.sumus.time.TimeScale;
import org.sumus.time.TimeStamp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DashboardModelRender extends DatawareHouseRender {
	private Dashboard dashboard;
	private DashboardDefinition definition;
	private HashMap<String, org.sumus.dictionary.Dictionary> sumusDictionaries = new HashMap<String, org.sumus.dictionary.Dictionary>();
	private HashSet<String> foldersIndicators = new HashSet<String>();

	public DashboardModelRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.dashboard = (Dashboard) target;
		this.definition = this.dashboard.getDefinition();
	}

	@Override
	protected void init() {
		loadCanvas("dashboardmodel");

		if (this.dashboard == null)
			return;

		this.addCommonMarks(this.markMap);

		TimeLapse range = this.getRange();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		TimeStamp min = range != null && range.getFrom() != null ? range.getFrom() : new TimeStamp(currentYear - 1);
		TimeStamp max = range != null && range.getTo() != null ? range.getTo() : new TimeStamp(currentYear);
		Language language = Language.getInstance();
		String label = language.getModelResource(this.definition.getLabel(), Language.getCurrent());
		Report report = (Report) this.getParameter(DatawareHouseRender.Parameter.REPORT);

		addMark("id", this.definition.getCode());
		addMark("name", this.definition.getCode());
		addMark("label", label != null && !label.isEmpty() ? label : this.definition.getName());
		addMark("report", report != null ? report.getName() : "");
		addMark("timeDimension", TimeDimension.NAME);
		addMark("resolution", this.getResolution().getId());
		addMark("min", min.getDate().getTime());
		addMark("max", max.getDate().getTime());
		addMark("scale", range != null ? range.getScale().getId() : this.getMinScale().getId());

		this.foldersIndicators = new HashSet<String>();

		this.initFolderStructures();
		this.initIndicatorStructures();
		this.initMeasureUnitStructures();
		this.initDimensionStructures();
	}

	private Set<String> getSelectedCubes() {
		IndicatorList selectedIndicators = (IndicatorList) this.getParameter(DatawareHouseRender.Parameter.SELECTED_INDICATORS);
		HashSet<String> result = null;

		if (selectedIndicators == null)
			return result;

		result = new HashSet<String>();

		for (Indicator indicator : selectedIndicators) {
			for (QuerySource source : indicator.getSources())
				result.add(source.getName());
		}

		return result;
	}

	private TimeScale getResolution() {
		IndicatorList selectedIndicators = (IndicatorList) this.getParameter(DatawareHouseRender.Parameter.SELECTED_INDICATORS);
		TimeScale resolution = null;

		for (Indicator indicator : selectedIndicators) {
			for (QuerySource source : indicator.getSources()) {
				TimeScale timeScale = source.getResolution();

				if (resolution == null)
					resolution = timeScale;
				else if (resolution.compareTo(timeScale) > 0)
					resolution = timeScale;
			}
		}

		return resolution == null ? TimeScale.YEAR : resolution;
	}

	private TimeLapse getRange() {
		IndicatorList selectedIndicators = (IndicatorList) this.getParameter(DatawareHouseRender.Parameter.SELECTED_INDICATORS);
		TimeLapse range = null;

		for (Indicator indicator : selectedIndicators) {
			for (QuerySource source : indicator.getSources()) {
				TimeLapse timeLapse = source.getTimeLapse();

				if (timeLapse.isEmpty())
					continue;

				if (range == null)
					range = timeLapse;
				else
					range.union(timeLapse);
			}
		}

		return range;
	}

	private String initFolderStructure(Folder folder, boolean addComma) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		org.sumus.dictionary.Dictionary dictionary = this.getDictionary();
		StringBuffer foldersBuffer = new StringBuffer();
		StringBuffer indicatorsBuffer = new StringBuffer();
		Folder[] childFolders = folder.getFolders();
		int i = 0;

		for (Folder childFolder : childFolders) {
			boolean addChildComma = (i != childFolders.length - 1);
			foldersBuffer.append(this.initFolderStructure(childFolder, addChildComma));
			i++;
		}

		i = 0;
		for (Indicator indicator : folder.getIndicators()) {
			map.put("id", indicator.getName());
			map.put("name", indicator.getName());
			map.put("comma", i == 0 ? "" : "comma");
			indicatorsBuffer.append(block("folder$indicator", map));
			map.clear();

			this.foldersIndicators.add(indicator.getName());

			i++;
		}

		String label = dictionary.getLabel(folder);

		map.clear();
		map.put("id", folder.getName());
		map.put("name", folder.getName());
		map.put("label", label != null ? label : folder.getName());
		map.put("folders", foldersBuffer.toString());
		map.put("comma", addComma ? "comma" : "");
		map.put("indicators", indicatorsBuffer.toString());

		return block("folder", map);
	}

	private void initFolderStructures() {
		Report report = (Report) this.getParameter(DatawareHouseRender.Parameter.REPORT);
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuffer folderList = new StringBuffer();
		StringBuffer folderMap = new StringBuffer();

		if (report == null)
			return;

		for (Folder folder : report.getFolders()) {
			map.put("name", folder.getName());
			map.put("list", "folderList");
			map.put("map", "folderMap");
			map.put("element", this.initFolderStructure(folder, false));

			folderList.append(block("elementList$item", map));
			folderMap.append(block("elementMap$item", map));
		}

		map.clear();
		map.put("list", "folderList");
		map.put("items", folderList.toString());
		addMark("folderList", block("elementList", map));

		map.put("map", "folderMap");
		map.put("items", folderMap.toString());
		addMark("folderMap", block("elementMap", map));
	}

	private String initIndicatorStructure(Indicator indicator) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		MeasureUnit measureUnit = indicator.getMeasureUnit();
		org.sumus.dictionary.Dictionary dictionary = this.getDictionary();
		String label = dictionary.getLabel(indicator);
		String description = dictionary.getDescription(indicator);

		if (label == null)
			label = indicator.getName();

		map.put("id", indicator.getName());
		map.put("name", indicator.getName());
		map.put("label", label);
		map.put("description", description == null ? label : description);
		map.put("metric", measureUnit!=null?measureUnit.getName():"");
		map.put("resolutions", initIndicatorStructureResolutions(indicator));

		return block("indicator", map);
	}

	private String initIndicatorStructureResolutions(Indicator indicator) {
		StringBuffer resolutions = new StringBuffer();
		int i = 0;

		DashboardDefinitionBase.IndicatorProperty.FilterProperty filterDefinition = this.definition.getIndicator(indicator.getName()).getFilter();
		if (filterDefinition != null) {
			for (ResolutionEnumeration resolutionEnumeration : filterDefinition.getResolution()) {
				HashMap<String, Object> localMap = new HashMap<String, Object>();
				localMap.put("resolution", resolutionEnumeration.toString().toLowerCase());
				localMap.put("comma", i == 0 ? "" : "comma");
				resolutions.append(block("indicator$resolution", localMap));
				i++;
			}
		}

		return resolutions.toString();
	}

	private void addIndicatorToStructures(Indicator[] indicators, StringBuffer indicatorList, StringBuffer indicatorMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		for (Indicator indicator : indicators) {

			if (!this.foldersIndicators.contains(indicator.getName()))
				continue;

			map.put("name", indicator.getName());
			map.put("list", "indicatorList");
			map.put("map", "indicatorMap");
			map.put("element", this.initIndicatorStructure(indicator));

			indicatorList.append(block("elementList$item", map));
			indicatorMap.append(block("elementMap$item", map));
		}
	}

	private void initIndicatorStructures() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuffer indicatorList = new StringBuffer();
		StringBuffer indicatorMap = new StringBuffer();
		Asset asset = (Asset) this.dashboard.getAsset();

		this.addIndicatorToStructures(asset.getIndicators(), indicatorList, indicatorMap);

		for (Cube cube : asset.getCubes())
			this.addIndicatorToStructures(cube.getIndicators(), indicatorList, indicatorMap);

		map.clear();
		map.put("list", "indicatorList");
		map.put("items", indicatorList.toString());
		addMark("indicatorList", block("elementList", map));

		map.put("map", "indicatorMap");
		map.put("items", indicatorMap.toString());
		addMark("indicatorMap", block("elementMap", map));
	}

	private Object initMeasureUnitStructure(MeasureUnit measureUnit) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		org.sumus.dictionary.Dictionary dictionary = this.getDictionary();

		String label = dictionary.getLabel(measureUnit);

		map.clear();
		map.put("id", measureUnit.getName());
		map.put("name", measureUnit.getName());
		map.put("label", label != null ? label : measureUnit.getName());

		return block("measureUnit", map);
	}

	private void initMeasureUnitStructures() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuffer measureUnitList = new StringBuffer();
		StringBuffer measureUnitMap = new StringBuffer();
		Asset asset = (Asset) this.dashboard.getAsset();
		HashSet<String> renderedMeasureUnits = new HashSet<String>();

		for (MeasureUnit measureUnit : asset.getMeasureUnits()) {
			boolean render = this.isMeasureUnitUsedBySelectedIndicators(measureUnit);
			String name = measureUnit.getName();

			if (!render || renderedMeasureUnits.contains(name))
				continue;

			renderedMeasureUnits.add(name);

			map.put("name", name);
			map.put("list", "measureUnitList");
			map.put("map", "measureUnitMap");
			map.put("element", this.initMeasureUnitStructure(measureUnit));
			measureUnitList.append(block("elementList$item", map));
			measureUnitMap.append(block("elementMap$item", map));
		}

		map.clear();
		map.put("list", "measureUnitList");
		map.put("items", measureUnitList.toString());
		addMark("measureUnitList", block("elementList", map));

		map.put("map", "measureUnitMap");
		map.put("items", measureUnitMap.toString());
		addMark("measureUnitMap", block("elementMap", map));
	}

	private Object initDimensionStructure(Dimension dimension) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		org.sumus.dictionary.Dictionary dictionary = this.getDictionary();
		StringBuffer taxonomiesBuffer = new StringBuffer();
		int i = 0;

		for (Taxonomy taxonomy : dimension.getTaxonomies()) {
			String label = dictionary.getLabel(taxonomy);
			map.put("id", taxonomy.getName());
			map.put("name", taxonomy.getName());
			map.put("label", label != null ? label : taxonomy.getName());
			map.put("comma", i == 0 ? "" : "comma");
			taxonomiesBuffer.append(block("dimension$taxonomy", map));
			map.clear();
			i++;
		}

		String label = dictionary.getLabel(dimension);

		map.clear();
		map.put("id", dimension.getName());
		map.put("name", dimension.getName());
		map.put("label", label != null ? label : dimension.getName());
		map.put("taxonomies", taxonomiesBuffer.toString());

		return block("dimension", map);
	}

	private void initDimensionStructures() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuffer dimensionList = new StringBuffer();
		StringBuffer dimensionMap = new StringBuffer();
		Asset asset = (Asset) this.dashboard.getAsset();

		for (Dimension dimension : asset.getDimensions()) {
			boolean render = this.isDimensionUsedBySelectedCubes(dimension);

			if (!render)
				continue;

			map.put("name", dimension.getName());
			map.put("list", "dimensionList");
			map.put("map", "dimensionMap");
			map.put("element", this.initDimensionStructure(dimension));
			dimensionList.append(block("elementList$item", map));
			dimensionMap.append(block("elementMap$item", map));
		}

		map.clear();
		map.put("list", "dimensionList");
		map.put("items", dimensionList.toString());
		addMark("dimensionList", block("elementList", map));

		map.put("map", "dimensionMap");
		map.put("items", dimensionMap.toString());
		addMark("dimensionMap", block("elementMap", map));
	}

	private boolean isMeasureUnitUsedBySelectedIndicators(MeasureUnit measureUnit) {
		IndicatorList selectedIndicators = (IndicatorList) this.getParameter(DatawareHouseRender.Parameter.SELECTED_INDICATORS);
		String measureUnitName = measureUnit.getName();

		for (Indicator indicator : selectedIndicators) {

			if (indicator instanceof CompositeIndicator) {
				for (Indicator childIndicator : ((CompositeIndicator) indicator).getIncludes())
					if (childIndicator.getMeasureUnit().getName().equals(measureUnitName))
						return true;
			}

			if (indicator.getMeasureUnit().getName().equals(measureUnitName))
				return true;
		}

		return false;
	}

	private boolean isDimensionUsedBySelectedCubes(Dimension dimension) {
		Set<String> selectedCubes = this.getSelectedCubes();
		Asset asset = (Asset) this.dashboard.getAsset();

		if (selectedCubes == null || selectedCubes.size() == 0)
			return false;

		for (String cubeId : selectedCubes) {
			Cube cube = asset.getCube(cubeId);
			if (cube.getDimension(dimension.getName()) == null)
				return false;
		}

		return true;
	}

	protected org.sumus.dictionary.Dictionary getDictionary() {
		String language = Language.getCurrent();
		Asset asset = (Asset) this.dashboard.getAsset();

		if (this.sumusDictionaries.containsKey(language))
			return this.sumusDictionaries.get(language);

		this.sumusDictionaries.put(language, asset.getDictionary(language));

		return this.sumusDictionaries.get(language);
	}

	private TimeScale getMinScale() {
		DatastoreDefinition definition = Dictionary.getInstance().getDatastoreDefinition(this.definition.getUse().getValue());
		TimeScale minScale = null;

		for (CubeProperty cubeDefinition : definition.getCubeList()) {
			CubeProperty.ResolutionEnumeration resolution = cubeDefinition.getResolution();
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

			if (minScale == null)
				minScale = scale;
			else if (minScale.compareTo(scale) == 1)
				minScale = scale;
		}

		return minScale;
	}

}