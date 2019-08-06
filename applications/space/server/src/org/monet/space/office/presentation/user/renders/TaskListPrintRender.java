package org.monet.space.office.presentation.user.renders;

import org.apache.commons.io.IOUtils;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.library.LibraryHTML;
import org.monet.space.kernel.library.LibraryXSL;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskSearchRequest;
import org.monet.space.kernel.utils.Resources;
import org.monet.space.kernel.utils.StreamHelper;
import org.monet.space.office.ApplicationOffice;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.core.model.Language;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class TaskListPrintRender extends PrintRender {

	private static final class Attribute {
		private static final String Label = "label";
		private static final String Description = "description";
		private static final String CreateDate = "createDate";
		private static final String UpdateDate = "updateDate";
		private static final String State = "state";
		private static final String Urgent = "urgent";
		private static final String Owner = "owner";
		private static final String Comments = "comments";
	}

	public static final List<String> attributes = new ArrayList<>(Arrays.asList(Attribute.Label, Attribute.Description, Attribute.CreateDate, Attribute.UpdateDate, Attribute.State, Attribute.Urgent, Attribute.Owner, Attribute.Comments));
	public static final List<String> attributesTypes = new ArrayList<>(Arrays.asList("String", "String", "Date", "Date", "String", "Boolean", "String", "String"));
	public static final List<Integer> attributesPercentages = new ArrayList<>(Arrays.asList(17, 14, 12, 12, 7, 7, 15, 14));

	public static final Map<String, List<String>> attributesLabels = new HashMap<String, List<String>>() {{
		put("es", new ArrayList<>(Arrays.asList("Título", "Descripción", "Fecha creación", "Fecha actualización", "Estado", "Urgente", "Asignado a", "Comentarios")));
		put("en", new ArrayList<>(Arrays.asList("Title", "Description", "Create date", "Update date", "State", "Urgent", "Assigned to", "Comments")));
	}};

	public TaskListPrintRender() {
		super();
	}

	@Override
	public boolean isTimeConsumptionExcessive() {
		return false;
	}

	@Override
	protected void init() {
		loadCanvas("print.tasklist");

		addMark("stylesUrl", Configuration.getInstance().getApiUrl() + "?op=loadthemefile&path=_styles");

		this.initContent();
	}

	protected void initContent() {
		HashMap<String, Object> map = new HashMap<>();
		TaskSearchRequest request = (TaskSearchRequest) this.getParameter("request");
		int count;

		if (request == null)
			request = new TaskSearchRequest();

		count = this.renderLink.searchTasksCount(this.account, request);

		request.setStartPos(0);
		request.setLimit(count);
		addMark("orientation", "landscape");

		map.put("label", block("content$label", new HashMap<String, Object>()));
		map.put("date", LibraryDate.getDateAndTimeString(new Date(), org.monet.space.office.core.model.Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.TEXT, true, "/"));

		int rowCount = count;
		String blockName = (count > 0) ? "content$offsetRowCount" : "content.empty$offsetRowCount";
		if (existsBlock(blockName))
			rowCount += Integer.valueOf(block(blockName, new HashMap<String, Object>()));
		map.put("rowCount", String.valueOf(rowCount));

		if (count > 0) {
			HashMap<String, Integer> columnDataSizes;

			columnDataSizes = this.initBody(map, request);
			this.initHeader(map, columnDataSizes, count);
			this.initColumns(map, columnDataSizes);

			addMark("content", block("content", map));
		} else
			addMark("content", block("content.empty", map));
	}

	private void initColumns(HashMap<String, Object> map, HashMap<String, Integer> columnDataSizes) {
		HashMap<String, Integer> columnPercentages = calculateColumnPercentages(columnDataSizes);
		HashMap<String, Object> localMap = new HashMap<>();
		StringBuilder columns = new StringBuilder();

		for (String attribute : getAttributesToShow()) {
			int width = columnPercentages.containsKey(attribute)?columnPercentages.get(attribute):getAttributePercentage(attribute);

			localMap.put("width", width);

			columns.append(block("content$column", localMap));
		}

		map.put("columns", columns.toString());
	}

	private Set<String> getAttributesToShow() {
		TaskSearchRequest request = (TaskSearchRequest) this.getParameter("request");
		String attributes = request.getParameter("attributes");
		LinkedHashSet<String> result = new LinkedHashSet<String>();
		if (attributes == null) return result;
		Collections.addAll(result, attributes.split(","));
		return result;
	}

	private void initHeader(HashMap<String, Object> map, HashMap<String, Integer> columnDataSizes, int count) {
		int headerCount = getAttributesToShow().size();
		String blockName = (count > 0) ? "content$offsetColumnCount" : "content.empty$offsetColumnCount";

		if (existsBlock(blockName))
			headerCount += Integer.valueOf(block(blockName, new HashMap<String, Object>()));

		map.put("header", this.initIndexHeader(columnDataSizes));
		map.put("headerCount", String.valueOf(headerCount));
	}

	private String initIndexHeader(HashMap<String, Integer> columnDataSizes) {
		HashMap<String, Object> map = new HashMap<>();
		String attributes = "";
		int showPos = 0;
		Set<String> attributesToShow = getAttributesToShow();

		for (String attribute : attributesToShow) {
			map.put("label", getAttributeLabel(attribute));
			map.put("comma", (showPos < attributesToShow.size() - 1) ? "comma" : "");
			attributes += block("content$header$attribute", map);
			showPos++;
		}

		map.clear();
		map.put("attributes", attributes);

		return block("content$header", map);
	}

	private HashMap<String, Integer> initBody(HashMap<String, Object> map, TaskSearchRequest request) {
		return this.initIndex(map, request);
	}

	private HashMap<String, Integer> initIndex(HashMap<String, Object> map, TaskSearchRequest request) {
		HashMap<String, Integer> columnDataSizes = new HashMap<>();
		HashMap<String, Object> localMap = new HashMap<>();
		String rows = "";
		int pos = 0;
		Set<String> attributesToShow = getAttributesToShow();

		for (Task task : this.renderLink.searchTasks(this.account, request).get().values()) {
			String attributes = "";
			int showPos = 0;

			for (String attributeCode : attributesToShow) {
				String value = format(attributeCode, getAttributeValue(task, attributeCode));
				int size = columnDataSizes.containsKey(attributeCode)?columnDataSizes.get(attributeCode):0;
				int length = value.length();

				if (size == 0)
					columnDataSizes.put(attributeCode, length);
				else if (length > size)
					columnDataSizes.put(attributeCode, length);

				localMap.put("label", getAttributeLabel(attributeCode));
				localMap.put("value", value.replace("&", "&amp;"));
				localMap.put("comma", (showPos < attributesToShow.size() - 1) ? "comma" : "");

				String attributeBlock = "content$row$attribute." + attributeCode;
				if (!existsBlock(attributeBlock)) attributeBlock = "content$row$attribute";

				attributes += block(attributeBlock, localMap);
				localMap.clear();

				showPos++;
			}

			localMap.clear();
			localMap.put("odd", (pos % 2 != 0) ? "odd" : "");
			localMap.put("attributes", attributes);
			rows += block("content$row", localMap);
			pos++;
		}

		map.put("rows", rows);

		return columnDataSizes;
	}

	private String format(String code, String value) {

		if (!code.equals(Attribute.Comments))
			return value;

		if (value == null)
			return null;

		value = "<!DOCTYPE feed [" + LibraryHTML.getEntities() + "]>\n<div>" + value + "</div>";

		Reader reader = LibraryXSL.compile(Resources.getFullPath("/" + ApplicationOffice.NAME + "/xhtml-to-xslfo.xsl"), value);
		try {
			String result = IOUtils.toString(reader);

			if (result == null)
				return null;

			return result.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		} catch (IOException e) {
			return null;
		}
		finally {
			StreamHelper.close(reader);
		}
	}

	private String getAttributeLabel(String attribute) {
		String language = Language.getCurrent();

		if (! TaskListPrintRender.attributesLabels.containsKey(language))
			language = "es";

		return TaskListPrintRender.attributesLabels.get(language).get(positionOf(attribute));
	}

	private Integer getAttributePercentage(String attribute) {
		return TaskListPrintRender.attributesPercentages.get(positionOf(attribute));
	}

	private int positionOf(String attribute) {
		int pos = 0;
		for (String currentAttribute : attributes) {
			if (currentAttribute.equals(attribute))
				return pos;
			pos++;
		}
		return -1;
	}

	private HashMap<String, Integer> calculateColumnPercentages(HashMap<String, Integer> dataSizes) {
		HashMap<String, Integer> result = new HashMap<>();

		int totalSize = this.getTotalSize(dataSizes);

		if (totalSize == 0)
			return result;

		totalSize = fixColumnsWithShortOrLongData(dataSizes, totalSize);
		Set<String> attributesToShow = getAttributesToShow();
		int maxColumnSize = maxColumnPercentageWith(attributesToShow.size());

		for (String attribute : attributesToShow) {
			int wordLength = this.getWordLength(getAttributeLabel(attribute));
			int dataSize = dataSizes.containsKey(attribute)?dataSizes.get(attribute):getAttributePercentage(attribute);

			if (dataSize > 0 && wordLength > dataSize)
				dataSize = wordLength > maxColumnSize ? maxColumnSize : wordLength;

			int percentage = (dataSize*100)/totalSize;

			result.put(attribute, percentage);
		}

		return result;
	}

	private int fixColumnsWithShortOrLongData(HashMap<String, Integer> dataSizes, int totalSize) {
		totalSize = fixShortDataColumns(dataSizes, totalSize);
		totalSize = fixLongDataColumns(dataSizes, totalSize);
		return totalSize;
	}

	private int fixShortDataColumns(HashMap<String, Integer> dataSizes, int totalSize) {
		for (String attribute : getAttributesToShow()) {
			int wordLength = this.getWordLength(getAttributeLabel(attribute));
			int dataSize = dataSizes.containsKey(attribute)?dataSizes.get(attribute):0;

			if (dataSize > 0 && wordLength > dataSize) {
				dataSizes.put(attribute, wordLength);

				totalSize = totalSize-dataSize;
				dataSize = wordLength;
				totalSize = totalSize+dataSize;
			}
		}
		return totalSize;
	}

	private int fixLongDataColumns(HashMap<String, Integer> dataSizes, int totalSize) {
		Set<String> attributesToShow = getAttributesToShow();
		int maxColumnSize = maxColumnPercentageWith(attributesToShow.size());

		for (String attribute : attributesToShow) {
			int dataSize = dataSizes.containsKey(attribute)?dataSizes.get(attribute):0;

			if ((dataSize*100/totalSize) > maxColumnSize) {
				int newSize = Math.abs(totalSize*maxColumnSize/100);
				dataSizes.put(attribute, newSize);

				totalSize = totalSize-dataSize;
				dataSize = newSize;
				totalSize = totalSize+dataSize;
			}
		}
		return totalSize;
	}

	private int getTotalSize(HashMap<String, Integer> dataSizes) {
		int totalSize = 0;

		for (int size : dataSizes.values())
			totalSize += size;

		return totalSize;
	}

	private String getAttributeValue(Task task, String attributeCode) {

		if (attributeCode.equals(Attribute.Label))
			return task.getLabel();

		if (attributeCode.equals(Attribute.Description))
			return String.format("%s. %s", task.getDefinition().getLabel(), task.getDescription());

		if (attributeCode.equals(Attribute.CreateDate))
			return LibraryDate.getDateAndTimeString(task.getInternalCreateDate(), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.NUMERIC, false, "/");

		if (attributeCode.equals(Attribute.UpdateDate))
			return LibraryDate.getDateAndTimeString(task.getInternalUpdateDate(), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.NUMERIC, false, "/");

		if (attributeCode.equals(Attribute.State))
			return block("content$row$attribute.state." + task.getState(), new HashMap<String, Object>());

		if (attributeCode.equals(Attribute.Urgent))
			return block("content$row$attribute.urgent." + String.valueOf(task.isUrgent()), new HashMap<String, Object>());

		if (attributeCode.equals(Attribute.Owner))
			return String.valueOf(task.getOwner() != null ? task.getOwner().getInfo().getFullname() : "-");

		if (attributeCode.equals(Attribute.Comments))
			return task.getComments() != null ? task.getComments() : "";

		return null;
	}

}
