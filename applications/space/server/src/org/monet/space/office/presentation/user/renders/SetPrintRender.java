package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.AttributeProperty.TypeEnumeration;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.IndexDefinitionBase.IndexViewProperty;
import org.monet.metamodel.SetDefinition;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.ShowProperty;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.DataRequest.GroupBy;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.core.model.Language;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SetPrintRender extends NodePrintRender {
	private static DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	private static final int RecommendedCountItems = 1000;

	public SetPrintRender() {
		super();
	}

	@Override
	public boolean isTimeConsumptionExcessive() {
		NodeDataRequest dataRequest = (NodeDataRequest) this.getParameter("dataRequest");
		SetDefinition definition = (SetDefinition) this.definition;
		IndexDefinition referenceDefinition = this.dictionary.getIndexDefinition(definition.getIndex().getValue());

		if (dataRequest == null) {
			dataRequest = new NodeDataRequest();
			dataRequest.setCodeDomainNode(this.definition.getCode());
			dataRequest.setCodeView(this.definition.getDefaultView().getCode());
		}
		addRange(dataRequest);

		dataRequest.setCodeReference(referenceDefinition.getCode());
		int count = this.renderLink.requestNodeListItemsCount(this.node.getId(), dataRequest);

		return count > RecommendedCountItems;
	}

	@Override
	protected void initContent() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		NodeDataRequest dataRequest = (NodeDataRequest) this.getParameter("dataRequest");
		SetDefinition definition = (SetDefinition) this.definition;
		IndexDefinition referenceDefinition = this.dictionary.getIndexDefinition(definition.getIndex().getValue());
		IndexViewProperty referenceViewDefinition = null;
		SetViewProperty setViewDefinition;
		String nameReferenceView;
		int count;

		if (dataRequest == null) {
			dataRequest = new NodeDataRequest();
			dataRequest.setCodeDomainNode(this.definition.getCode());
			dataRequest.setCodeView(this.definition.getDefaultView().getCode());
		}
		addRange(dataRequest);

		setViewDefinition = (SetViewProperty) node.getDefinition().getNodeView(dataRequest.getCodeView());

		if (setViewDefinition.getShow().getIndex() == null) {
			addMark("content", block("content.empty", map));
			return;
		}

		ShowProperty showDefinition = setViewDefinition.getShow();
		nameReferenceView = showDefinition.getIndex().getWithView().getValue();
		dataRequest.setCodeReference(referenceDefinition.getCode());
		count = this.renderLink.requestNodeListItemsCount(this.node.getId(), dataRequest);

		referenceViewDefinition = referenceDefinition.getView(nameReferenceView);

		dataRequest.setStartPos(0);
		dataRequest.setLimit(count);
		addMark("orientation", (referenceDefinition.getReference() != null && referenceDefinition.getReference().getAttributePropertyList().size() > 5) ? "landscape" : "portrait");

		map.put("label", this.definition.getLabelString());
		map.put("filters", getFilters());
		map.put("date", LibraryDate.getDateAndTimeString(new Date(), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.TEXT, true, "/"));
		map.put("description", this.definition.getDescription());

		int referencesCount = count;
		String blockName = (count > 0) ? "content$offsetRowCount" : "content.empty$offsetRowCount";
		if (existsBlock(blockName))
			referencesCount += Integer.valueOf(block(blockName, new HashMap<String, Object>()));
		map.put("referencesCount", String.valueOf(referencesCount));

		if (count > 0) {
			HashMap<String, Integer> columnDataSizes;

			columnDataSizes = this.initBody(map, dataRequest, referenceDefinition, referenceViewDefinition);
			this.initHeader(map, referenceDefinition, referenceViewDefinition, columnDataSizes, count, dataRequest);
			this.initColumns(map, referenceDefinition, referenceViewDefinition, columnDataSizes, count, dataRequest);

			addMark("content", block("content", map));
		} else
			addMark("content", block("content.empty", map));
	}

	private String getFilters() {
		SetDefinition definition = (SetDefinition) this.definition;
		IndexDefinition indexDefinition = this.dictionary.getIndexDefinition(definition.getIndex().getValue());
		HashMap<String, Object> map = new HashMap<>();
		NodeDataRequest dataRequest = (NodeDataRequest) getParameter("dataRequest");
		String condition = dataRequest.getCondition();

		if (dataRequest.getGroupsBy().size() == 0 && (condition == null || condition.isEmpty()))
			return block("content$filters.empty", new HashMap<String, Object>());

		StringBuffer filters = new StringBuffer();

		if (condition != null && !condition.isEmpty()) {
			map.put("value", condition);
			filters.append(block("content$filter.condition", map));
		}

		for (GroupBy groupBy : dataRequest.getGroupsBy()) {
			AttributeProperty attributeDefinition = getAttributeDefinition(indexDefinition, groupBy.attribute());
			map.put("label", Language.getInstance().getModelResource(attributeDefinition.getLabel()));
			map.put("values", filterValuesOfGroupBy(groupBy));
			filters.append(block(filterBlockOfGroupBy(groupBy), map));
			map.clear();
		}

		map.put("filters", filters.toString());

		return block("content$filters", map);
	}

	private String filterValuesOfGroupBy(GroupBy groupBy) {
		StringBuffer result = new StringBuffer();

		for (Object value : groupBy.values()) {
			if (result.length() > 0)
				result.append(", ");

			value = format(groupBy, value);

			if (value instanceof Date)
				result.append(LibraryDate.getDateAndTimeString((Date) value, Language.getCurrent(), BusinessUnit.getTimeZone(), LibraryDate.Format.TEXT, false, "/").trim());
			else result.append(value);
		}

		return result.toString();
	}

	private Object format(GroupBy groupBy, Object value) {
		if (!groupBy.attribute().equals(DescriptorDefinition.ATTRIBUTE_CODE))
			return value;

		return dictionary.getDefinition((String)value).getLabelString();
	}

	private String filterBlockOfGroupBy(GroupBy groupBy) {
		String result = "content$filter." + groupBy.operator().toString().toLowerCase();
		if (existsBlock(result)) return result;
		return "content$filter";
	}

	private void addRange(NodeDataRequest dataRequest) {
		final Range range = getRange();

		if (range == null || range.attribute() == null || range.attribute().isEmpty())
			return;

		if (range.from() != null)
			dataRequest.getGroupsBy().add(new GroupBy() {
				@Override
				public String attribute() {
					return range.attribute();
				}

				@Override
				public List<Object> values() {
					return new ArrayList<Object>() {{
						add(range.from());
					}};
				}

				@Override
				public <T> T value(int pos) {
					return (T) values().get(pos);
				}

				@Override
				public Operator operator() {
					return Operator.Gt;
				}
			});

		if (range.to() != null)
			dataRequest.getGroupsBy().add(new GroupBy() {
				@Override
				public String attribute() {
					return range.attribute();
				}

				@Override
				public List<Object> values() {
					return new ArrayList<Object>() {{
						add(range.to());
					}};
				}

				@Override
				public <T> T value(int pos) {
					return (T) values().get(pos);
				}

				@Override
				public Operator operator() {
					return Operator.Lt;
				}
			});

	}

	private Set<String> getAttributesToShow(DataRequest dataRequest) {
		String attributes = dataRequest.getParameter("attributes");
		LinkedHashSet<String> result = new LinkedHashSet<String>();
		if (attributes == null) return result;
		Collections.addAll(result, attributes.split(","));
		return result;
	}

	private void initColumns(HashMap<String, Object> map, IndexDefinition referenceDefinition, IndexViewProperty referenceViewDefinition, HashMap<String, Integer> columnDataSizes, int numRows, DataRequest dataRequest) {
		HashMap<String, Integer> columnPercentages = calculateColumnPercentages(referenceDefinition, referenceViewDefinition, columnDataSizes, numRows, dataRequest);
		IndexViewProperty.ShowProperty showDefinition = referenceViewDefinition.getShow();
		ArrayList<Ref> showList = this.mergeIndexShows(showDefinition);
		HashMap<String, Object> localMap = new HashMap<String, Object>();
		StringBuilder columns = new StringBuilder();
		Set<String> attributesToShow = this.getAttributesToShow(dataRequest);

		for (String attributeKey : attributesToShow) {
			AttributeProperty attributeDefinition = getAttributeDefinition(referenceDefinition, attributeKey);
			String code = attributeDefinition.getCode();
			int width = columnPercentages.containsKey(code)?columnPercentages.get(code):0;

			localMap.put("width", width);

			columns.append(block("content$reference.column", localMap));
		}

		map.put("referencesColumns", columns.toString());
	}

	private void initHeader(HashMap<String, Object> map, IndexDefinition referenceDefinition, IndexViewProperty referenceViewDefinition, HashMap<String, Integer> columnDataSizes, int count, DataRequest dataRequest) {
		int referencesHeaderCount = referenceDefinition.getReference().getAttributePropertyList().size();
		String blockName = (count > 0) ? "content$offsetColumnCount" : "content.empty$offsetColumnCount";

		if (existsBlock(blockName))
			referencesHeaderCount += Integer.valueOf(block(blockName, new HashMap<String, Object>()));

		map.put("referencesHeader", this.initIndexHeader(referenceDefinition, referenceViewDefinition, columnDataSizes, dataRequest));
		map.put("referencesHeaderCount", String.valueOf(referencesHeaderCount));
	}

	private String initIndexHeader(IndexDefinition referenceDefinition, IndexViewProperty referenceViewDefinition, HashMap<String, Integer> columnDataSizes, DataRequest dataRequest) {
		IndexViewProperty.ShowProperty showDefinition = referenceViewDefinition.getShow();
		ArrayList<Ref> showList = this.mergeIndexShows(showDefinition);
		HashMap<String, Object> map = new HashMap<String, Object>();
		String attributes = "";
		int showPos = 0;
		org.monet.space.office.core.model.Language language = Language.getInstance();
		Set<String> attributesToShow = this.getAttributesToShow(dataRequest);

		for (String attributeKey : attributesToShow) {
			AttributeProperty attributeDefinition = getAttributeDefinition(referenceDefinition, attributeKey);
			String code = attributeDefinition.getCode();
			String label = language.getModelResource(attributeDefinition.getLabel());

			if (columnDataSizes.get(code) == 0 && existsBlock("content$reference.header$attribute.emptyLabel"))
				label = block("content$reference.header$attribute.emptyLabel", map);

			map.put("label", label);
			map.put("comma", (showPos < showList.size() - 1) ? "comma" : "");
			attributes += block("content$reference.header$attribute", map);
			showPos++;
		}

		map.clear();
		map.put("attributes", attributes);

		return block("content$reference.header", map);
	}

	private HashMap<String, Integer> initBody(HashMap<String, Object> map, NodeDataRequest dataRequest, IndexDefinition referenceDefinition, IndexViewProperty referenceViewDefinition) {
		return this.initIndex(map, dataRequest, referenceDefinition, referenceViewDefinition);
	}

	private HashMap<String, Integer> initIndex(HashMap<String, Object> map, NodeDataRequest dataRequest, IndexDefinition referenceDefinition, IndexViewProperty referenceViewDefinition) {
		Language language = Language.getInstance();
		HashMap<String, Integer> columnDataSizes = new HashMap<String, Integer>();
		HashMap<String, Object> localMap = new HashMap<String, Object>();
		StringBuilder references = new StringBuilder();
		int pos = 0;
		Set<String> attributesToShow = this.getAttributesToShow(dataRequest);

		for (String attribute : attributesToShow) {
			String label = language.getModelResource(getAttributeDefinition(referenceDefinition, attribute).getLabel());
			columnDataSizes.put(attribute, label.length());
		}

		Collection<Node> values = this.renderLink.requestNodeListItems(this.node.getId(), dataRequest).values();
		for (Node node : values) {
			String attributes = "";
			Reference reference = node.getReference(referenceDefinition.getCode());
			IndexViewProperty.ShowProperty showDefinition = referenceViewDefinition.getShow();
			ArrayList<Ref> showList = this.mergeIndexShows(showDefinition);
			int showPos = 0;

			for (String attributeKey : attributesToShow) {
				AttributeProperty attributeDeclaration = getAttributeDefinition(referenceDefinition, attributeKey);
				String code = attributeDeclaration.getCode();
				ReferenceAttribute<?> attribute = reference.getAttribute(attributeDeclaration.getCode());
				String value = attribute.getValueAsString();

				if (attributeDeclaration.getType() == TypeEnumeration.PICTURE) {
					value = Configuration.getInstance().getFmsServletUrl() + "?op=downloadimage&nid=" + node.getId() + "&f=" + value.replace(" ", "%20") + "&thumb=1&r=" + Math.random();
					columnDataSizes.put(code, 20);
				}

				if (attributeDeclaration.getType() == TypeEnumeration.BOOLEAN)
					value = value.toLowerCase().equals("true") ? "X" : "";

				int size = columnDataSizes.containsKey(code)?columnDataSizes.get(code):0;

				if (size == 0)
					columnDataSizes.put(code, value.length());
				else if (attributeDeclaration.getType() != TypeEnumeration.PICTURE && value.length() > size)
					columnDataSizes.put(code, value.length());

				localMap.put("value", value.replace("&", "&amp;").replace("\n", " "));
				localMap.put("comma", (showPos < showList.size() - 1) ? "comma" : "");

				String blockName = "content$reference$attribute." + attributeDeclaration.getType().toString().toLowerCase();
				if (!existsBlock(blockName))
					blockName = "content$reference$attribute";

				attributes += block(blockName, localMap);

				localMap.clear();

				showPos++;
			}

			localMap.clear();
			localMap.put("odd", (pos % 2 != 0) ? "odd" : "");
			localMap.put("attributes", attributes);
			references.append(block("content$reference", localMap));
			pos++;
		}

		map.put("references", references.toString());

		return columnDataSizes;
	}

	private HashMap<String, Integer> calculateColumnPercentages(IndexDefinition referenceDefinition, IndexViewProperty referenceViewDefinition, HashMap<String, Integer> dataSizes, int numRows, DataRequest dataRequest) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		IndexViewProperty.ShowProperty showDefinition = referenceViewDefinition.getShow();
		ArrayList<Ref> showList = this.mergeIndexShows(showDefinition);
		Language language = Language.getInstance();
		Set<String> attributesToShow = this.getAttributesToShow(dataRequest);

		if (showList.size() == 0)
			return result;

		int totalSize = this.getTotalSize(dataSizes, numRows);

		if (totalSize == 0)
			return result;

		totalSize = fixColumnsWithShortOrLongData(referenceDefinition, dataSizes, showList, language, totalSize, dataRequest);
		int maxColumnSize = maxColumnPercentageWith(attributesToShow.size());

		for (String attributeKey : attributesToShow) {
			AttributeProperty attributeDefinition = getAttributeDefinition(referenceDefinition, attributeKey);
			String code = attributeDefinition.getCode();
			int wordLength = this.getWordLength(language.getModelResource(attributeDefinition.getLabel()));
			int dataSize = dataSizes.containsKey(code)?dataSizes.get(code):0;

			if (dataSize > 0 && wordLength > dataSize)
				dataSize = wordLength > maxColumnSize ? maxColumnSize : wordLength;

			int percentage = (dataSize*100)/totalSize;

			result.put(code, percentage);
		}

		return result;
	}

	private int fixColumnsWithShortOrLongData(IndexDefinition referenceDefinition, HashMap<String, Integer> dataSizes, ArrayList<Ref> showList, Language language, int totalSize, DataRequest dataRequest) {
		Set<String> attributesToShow = this.getAttributesToShow(dataRequest);

		totalSize = fixShortDataColumns(referenceDefinition, dataSizes, language, totalSize, attributesToShow);
		totalSize = fixLongDataColumns(referenceDefinition, dataSizes, totalSize, attributesToShow);

		return totalSize;
	}

	private int fixShortDataColumns(IndexDefinition referenceDefinition, HashMap<String, Integer> dataSizes, Language language, int totalSize, Set<String> attributesToShow) {
		for (String attributeKey : attributesToShow) {
			AttributeProperty attributeDefinition = getAttributeDefinition(referenceDefinition, attributeKey);
			String code = attributeDefinition.getCode();
			int wordLength = this.getWordLength(language.getModelResource(attributeDefinition.getLabel())) + 10;
			int dataSize = dataSizes.containsKey(code)?dataSizes.get(code):0;

			if (dataSize > 0 && wordLength > dataSize) {
				dataSizes.put(code, wordLength);

				totalSize = totalSize-dataSize;
				dataSize = wordLength;
				totalSize = totalSize+dataSize;
			}
		}
		return totalSize;
	}

	private int fixLongDataColumns(IndexDefinition referenceDefinition, HashMap<String, Integer> dataSizes, int totalSize, Set<String> attributesToShow) {
		int maxColumnSize = maxColumnPercentageWith(attributesToShow.size());

		for (String attributeKey : attributesToShow) {
			AttributeProperty attributeDefinition = getAttributeDefinition(referenceDefinition, attributeKey);
			String code = attributeDefinition.getCode();
			int dataSize = dataSizes.containsKey(code)?dataSizes.get(code):0;

			if ((dataSize*100/totalSize) > maxColumnSize) {
				int newSize = Math.abs(totalSize*maxColumnSize/100);
				dataSizes.put(code, newSize);

				totalSize = totalSize-dataSize;
				dataSize = newSize;
				totalSize = totalSize+dataSize;
			}
		}
		return totalSize;
	}

	private int getTotalSize(HashMap<String, Integer> dataSizes, int numRows) {
		int totalSize = 0;

		for (int size : dataSizes.values())
			totalSize += size;

		return totalSize;
	}

	private AttributeProperty getAttributeDefinition(IndexDefinition definition, String attribute) {
		AttributeProperty attributeDefinition = definition.getAttribute(attribute);

		if (attributeDefinition != null)
			return attributeDefinition;

		attributeDefinition = new DescriptorDefinition().getAttribute(attribute);
		attributeDefinition.setLabel(block("attribute$label." + attributeDefinition.getCode(), new HashMap<String, Object>()));

		return attributeDefinition;
	}
}
