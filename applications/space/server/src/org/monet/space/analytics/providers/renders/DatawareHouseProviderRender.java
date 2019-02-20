package org.monet.space.analytics.providers.renders;

import org.apache.commons.lang.StringUtils;
import org.monet.space.analytics.configuration.Configuration;
import org.monet.space.analytics.model.DatawareHouseLink;
import org.monet.space.analytics.model.DatawareHouseLinkProvider;
import org.monet.space.analytics.model.Language;
import org.monet.space.analytics.utils.KeyUtil;
import org.monet.space.analytics.utils.NumberUtil;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Dashboard;
import org.monet.templation.CanvasLogger;
import org.monet.templation.Render;
import org.sumus.asset.Asset;
import org.sumus.dictionary.Dictionary;
import org.sumus.dimension.Member;
import org.sumus.dimension.category.Category;
import org.sumus.indicator.IndicatorList;
import org.sumus.indicatorfigure.IndicatorFigure;
import org.sumus.query.Chart;
import org.sumus.query.ChartScaler;
import org.sumus.query.Drill;
import org.sumus.time.TimeScale;
import org.sumus.time.TimeStamp;

import java.text.DateFormatSymbols;
import java.util.*;

public abstract class DatawareHouseProviderRender extends Render {
	private DatawareHouseLink link = null;
	private HashMap<String, Dictionary> sumusDictionaries = new HashMap<String, Dictionary>();
	private HashMap<String, String[]> weekDaysMap = new HashMap<String, String[]>();
	private HashMap<String, String[]> monthsMap = new HashMap<String, String[]>();
	private Calendar lastTimeStampCalendar;
	protected Chart chart;
	protected ChartScaler chartScaler;
	protected RenderProvidersFactory renderProvidersFactory;

	public static final class Parameter {
		public static final String CHART_TYPE = "chartType";
		public static final String FORMAT = "format";
		public static final String INDICATOR_LIST = "indicatorList";
		public static final String RANGE_LIST = "rangeList";
		public static final String FILTERS = "filters";
		public static final String DASHBOARD = "dashboard";
	}

	private static class Logger implements CanvasLogger {
		@Override
		public void debug(String message, Object... args) {
			AgentLogger.getInstance().debug(message, args);
		}
	}

	public DatawareHouseProviderRender() {
		super(new Logger(), Configuration.getInstance().getTemplatesProvidersDir(Language.getCurrent()));
		this.renderProvidersFactory = RenderProvidersFactory.Instance();
	}

	@Override
	public void setTemplate(String template) {
		Integer pos = template.lastIndexOf("?");
		if (pos == -1) pos = template.length();
		this.template = template.substring(0, pos);
		this.template = this.template.replaceAll(".html", "");
		this.template = this.template.replaceAll(".js", "");
		if (pos < template.length()) this.setParameters(template.substring(pos + 1));
	}

	@Override
	public void setTarget(Object target) {
		this.chart = (Chart) target;

		if (this.chart != null) {
			this.chartScaler = new ChartScaler(this.chart);
			this.chartScaler.scale();
		}
	}

	;

	protected void addCommonMarks(HashMap<String, Object> map) {
		Long thread = Thread.currentThread().getId();
		Context context = Context.getInstance();
		Configuration configuration = Configuration.getInstance();
		String url = configuration.getUrl();

		map.put("domain", context.getDomain(thread));
		map.put("url", configuration.getServletUrl());
		map.put("imagesUrl", url + "/images");
		map.put("stylesUrl", url + "/styles");
		map.put("javascriptUrl", url + "/javascript");
		map.put("port", String.valueOf(context.getPort(thread)));
		map.put("language", Language.getCurrent());
		map.put("keyTemplate", KeyUtil.KEY_TEMPLATE);
		map.put("keySeparator", KeyUtil.KEY_SEPARATOR);
	}

	protected DatawareHouseLink getLink() {

		if (this.link == null)
			this.link = new DatawareHouseLinkProvider();

		return this.link;
	}

	protected boolean isDateTime() {
		TimeScale scale = this.chart.getTimeLapse().getScale();
		if (scale.compareTo(TimeScale.HOUR) == 0)
			return true;
		if (scale.compareTo(TimeScale.MINUTE) == 0)
			return true;
		if (scale.compareTo(TimeScale.SECOND) == 0)
			return true;
		return false;
	}

	protected org.sumus.dictionary.Dictionary getDictionary(Asset asset) {
		String language = Language.getCurrent();

		if (this.sumusDictionaries.containsKey(language))
			return this.sumusDictionaries.get(language);

		this.sumusDictionaries.put(language, asset.getDictionary(language));

		return this.sumusDictionaries.get(language);
	}

	protected boolean existsTimeChange(TimeStamp timeStamp, int pos, int spotsCount) {
		Calendar calendar = new GregorianCalendar();
		TimeScale scale = timeStamp.getScale();
		calendar.setTime(timeStamp.getDate());

		if (pos == 0/* || pos == (spotsCount-1)*/) {
			this.lastTimeStampCalendar = calendar;
			return true;
		}

		if (scale == TimeScale.YEAR) {
			this.lastTimeStampCalendar = calendar;
			return true;
		}

		int lastYear = this.lastTimeStampCalendar.get(Calendar.YEAR);
		int year = calendar.get(Calendar.YEAR);

		if (scale == TimeScale.MONTH && lastYear != year) {
			this.lastTimeStampCalendar = calendar;
			return true;
		}

		int lastMonth = this.lastTimeStampCalendar.get(Calendar.MONTH);
		int month = calendar.get(Calendar.MONTH);

		if (scale == TimeScale.DAY && (lastMonth != month || lastYear != year)) {
			this.lastTimeStampCalendar = calendar;
			return true;
		}

		int lastDate = this.lastTimeStampCalendar.get(Calendar.DATE);
		int date = calendar.get(Calendar.DATE);

		if ((scale == TimeScale.HOUR || scale == TimeScale.MINUTE || scale == TimeScale.SECOND) && (lastDate != date || lastMonth != month || lastYear != year)) {
			this.lastTimeStampCalendar = calendar;
			return true;
		}

		this.lastTimeStampCalendar = calendar;

		return false;
	}

	protected String getDateIndicator(TimeStamp timeStamp, boolean addComma, boolean fullDate) {
		HashMap<String, Object> map = new HashMap<>();
		Calendar calendar = new GregorianCalendar();
		String scale = timeStamp.getScale().getLabel().toLowerCase();

		calendar.setTime(timeStamp.getDate());

		int month = calendar.get(Calendar.MONTH) + 1;
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		map.put("year", calendar.get(Calendar.YEAR));
		map.put("month", month);
		map.put("formattedMonth", StringUtils.capitalize(this.getMonths()[month - 1]));
		map.put("day", calendar.get(Calendar.DATE));
		map.put("formattedDay", StringUtils.capitalize(this.getWeekDays()[dayOfWeek]));
		map.put("hour", NumberUtil.leftPad(calendar.get(Calendar.HOUR_OF_DAY), 2));
		map.put("minute", NumberUtil.leftPad(calendar.get(Calendar.MINUTE), 2));
		map.put("second", NumberUtil.leftPad(calendar.get(Calendar.SECOND), 2));

		String valueBlockName = "row$value.date." + scale;
		String formattedBlockName = "row$value.date." + scale;
		if (this.isDateTime()) {
			valueBlockName = "row$value.time." + scale;
			if (!existsBlock(valueBlockName))
				valueBlockName = "row$value.time";
			formattedBlockName = "row$formatted.date." + scale;
			if (!existsBlock(formattedBlockName))
				formattedBlockName = "row$formatted.date";
		} else {
			valueBlockName = "row$value.date." + scale;
			if (!existsBlock(valueBlockName))
				valueBlockName = "row$value.date";
			formattedBlockName = "row$formatted.date." + scale;
			if (!existsBlock(formattedBlockName))
				formattedBlockName = "row$formatted.date";
		}

		if (fullDate && existsBlock(formattedBlockName + ".full"))
			formattedBlockName += ".full";

		map.put("value", block(formattedBlockName, map));
		map.put("formattedValue", block(formattedBlockName, map));
		map.put("link", "");
		map.put("difference", 0);
		map.put("comma", addComma ? "true" : "");
		map.put("primitiveValue", map.get("value"));
		map.put("type", "String");

		return block("row$indicator", map);
	}

	protected String getIdIndicator() {
		return block("row$indicator.id", new HashMap<String, Object>());
	}

	protected boolean existsDrill() {
		if (this.chart == null)
			return false;
		Drill drill = this.chart.getDrill();
		return drill != null && drill.getMembers().length > 0;
	}

	protected List<Double> getValues(IndicatorFigure indicatorFigure) {
		Drill drill = this.chart.getDrill();
		ArrayList<Double> result = new ArrayList<Double>();

		if (this.existsDrill()) {
			for (Member member : drill.getMembers())
				result.add(indicatorFigure.getScaled(member));
		} else
			result.add(indicatorFigure.getScaled());

		return result;
	}

	protected int getRowSize(IndicatorList indicatorList) {
		return indicatorList.size() * (this.existsDrill() ? this.chart.getDrill().getMembers().length : 1);
	}

	protected String[] getWeekDays() {
		String language = Language.getCurrent();

		if (this.weekDaysMap.containsKey(language))
			return this.weekDaysMap.get(language);

		String[] weekDays = new DateFormatSymbols(new Locale(language)).getWeekdays();
		this.weekDaysMap.put(language, weekDays);

		return weekDays;
	}

	protected String[] getMonths() {
		String language = Language.getCurrent();

		if (this.monthsMap.containsKey(language))
			return this.monthsMap.get(language);

		String[] months = new DateFormatSymbols(new Locale(language)).getMonths();
		this.monthsMap.put(language, months);

		return months;
	}

	protected Asset getAsset() {
		Dashboard dashboard = (Dashboard) this.getParameter(DatawareHouseProviderRender.Parameter.DASHBOARD);
		return (Asset) dashboard.getAsset();
	}

	protected String getCategoryLabel(Dictionary dictionary, Category category) {
		Category parent = category.getParent();
		String label = dictionary.getLabel(category);

		if (label == null)
			label = category.getName();

		while (parent != null) {
			String parentLabel = dictionary.getLabel(parent);

			if (parentLabel == null)
				parentLabel = parent.getName();

			label = parentLabel + " : " + label;
			parent = parent.getParent();
		}

		return label;
	}

}
