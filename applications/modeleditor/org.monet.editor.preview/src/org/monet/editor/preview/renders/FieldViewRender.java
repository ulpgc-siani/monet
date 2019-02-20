package org.monet.editor.preview.renders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.model.Problem;
import org.monet.editor.preview.model.SummationItem;
import org.monet.editor.preview.model.SummationItemList;
import org.monet.editor.preview.model.Term;
import org.monet.editor.preview.model.TermList;
import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.CheckFieldProperty;
import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.DateFieldProperty;
import org.monet.metamodel.Definition;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FieldPropertyBase.DisplayProperty;
import org.monet.metamodel.FieldPropertyBase.DisplayProperty.WhenEnumeration;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.LinkFieldProperty;
import org.monet.metamodel.MemoFieldProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.NodeFieldProperty;
import org.monet.metamodel.NumberFieldProperty;
import org.monet.metamodel.PictureFieldProperty;
import org.monet.metamodel.SectionFieldProperty;
import org.monet.metamodel.SelectFieldProperty;
import org.monet.metamodel.SelectFieldPropertyBase.SourceProperty.FlattenEnumeration;
import org.monet.metamodel.SerialFieldProperty;
import org.monet.metamodel.SummationFieldProperty;
import org.monet.metamodel.SummationItemProperty;
import org.monet.metamodel.TextFieldProperty;
import org.monet.metamodel.ThesaurusDefinition;
import org.monet.metamodel.internal.Ref;

public class FieldViewRender extends ViewRender {
  private FieldProperty definition;

  public FieldViewRender(String language) {
    super(language);
  }

  @Override
  public void setTarget(Object target) {
    this.definition = (FieldProperty)target;
  }

  @Override
  protected String initView(String codeView) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String id = this.getParameterAsString("id");
    Boolean isTemplate = (Boolean) this.getParameter("isTemplate");
    //List<RuleDeclaration> rules = this.getRules();
    String readonly = this.getParameterAsString("readonly");
    HashMap<String, Object> declarationsMap = new HashMap<String, Object>();

    id = id + "." + this.definition.getCode();

    map.put("id", id);

    if (this.definition.isMultiple() && !codeView.equalsIgnoreCase("sectionitem"))
      this.initMultipleField(id, map, declarationsMap, (isTemplate != null) ? isTemplate : false);
    else
      map.put("field", this.initSingleField(id, map, declarationsMap, (isTemplate != null) ? isTemplate : false));

    this.initAttributes(map, id/*, rules*/);
    this.initDeclarations(map, id, declarationsMap);
    this.initLabel(map, id, codeView);
    this.initInput(map, id);

    if (readonly.equals("true")) {
    } else {
    }

    return block("view", map);
  }

  protected boolean isTableView() {
    boolean isTableView = false;

    if (this.definition.isSection()) {
      SectionFieldProperty.ViewProperty view = ((SectionFieldProperty) this.definition).getView();
      isTableView = (view != null && view.getMode().equals(SectionFieldProperty.ViewProperty.ModeEnumeration.COMPACT));
    }

    return isTableView;
  }

  protected void initAttributes(HashMap<String, Object> viewMap, String id/*, List<RuleDeclaration> rules*/) {

    viewMap.put("code", this.definition.getCode());
    viewMap.put("type", this.getFieldType());
    viewMap.put("super", this.definition.isSuperfield() ? "super" : "");

    /*if (rules.size() > 0)
      viewMap.put("invisible", (this.isHidden()) ? "hidden" : "");
    else*/
      viewMap.put("invisible", (this.isInvisible()) ? "hidden" : "");

    viewMap.put("required", (this.definition.isRequired()) ? "required" : "");
    viewMap.put("extended", (this.definition.isExtended()) ? "extended" : "");
    viewMap.put("multiple", (this.definition.isMultiple()) ? "multiple" : "");
    viewMap.put("tableView", (this.isTableView()) ? "tableView" : "");
    viewMap.put("allowOthers", "false");

    if (this.definition.isSection()) {
      SectionFieldProperty section = (SectionFieldProperty) this.definition;
      viewMap.put("conditional", section.isConditional() ? "conditional" : "");
      viewMap.put("extensible", section.isExtensible() ? "extensible" : "");
    }
  }

  protected void initLabel(HashMap<String, Object> viewMap, String id, String codeView) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String conditionedValue = "";

    String label = language.getModelResource(this.definition.getLabel(), this.codeLanguage);
    if (label == null || label.isEmpty()) {
      viewMap.put("label", block("label.empty", map));
      return;
    }

    if (this.definition.isSection()) {
      SectionFieldProperty section = (SectionFieldProperty) this.definition;
      if (section.isConditional()) {
        conditionedValue = block("label$conditioned.no", map);
      }
    }

    map.put("label", (!codeView.equalsIgnoreCase("sectionitem")) ? label : "");
    map.put("conditioned", conditionedValue);

    viewMap.put("label", block("label", map));
  }

  private void initDeclarations(HashMap<String, Object> viewMap, String id, HashMap<String, Object> declarationsMap) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String attributes = "";

    if (this.definition.isMultiple()) {
      map.put("code", this.definition.getCode());
      map.put("attributes", attributes);
      declarationsMap.put("default", block("declarations$default.multiple", map));
    } else
      declarationsMap.put("default", attributes);

    declarationsMap.put("description", language.getModelResource(this.definition.getDescription(), this.codeLanguage));

    declarationsMap.put("messageIfEmpty", "");
    declarationsMap.put("messageIfEditing", "");
    declarationsMap.put("messageIfRequired", "");
    for (DisplayProperty displayDefinition : this.definition.getDisplayList()) {
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      localMap.put("message", language.getModelResource(displayDefinition.getMessage(), this.codeLanguage));
      if (displayDefinition.getWhen() == null)
        declarationsMap.put("messageIfEditing", block("declarations$messageIfEditing", localMap));
      else if (displayDefinition.getWhen() == WhenEnumeration.EMPTY)
        declarationsMap.put("messageIfEmpty", block("declarations$messageIfEmpty", localMap));
      else if (displayDefinition.getWhen() == WhenEnumeration.REQUIRED)
        declarationsMap.put("messageIfRequired", block("declarations$messageIfRequired", localMap));
      localMap.clear();
    }

    if (!declarationsMap.containsKey("concreteDeclarations"))
      declarationsMap.put("concreteDeclarations", "");

    viewMap.put("declarations", block("declarations", declarationsMap));
  }

  protected void initInput(HashMap<String, Object> viewMap, String id) {
    String isRootValue = this.getParameterAsString("isRoot");
    if (isRootValue.isEmpty())
      isRootValue = "true";
    viewMap.put("root", (Boolean.valueOf(isRootValue)) ? "root" : "");
  }

  protected String getMultipleFieldTableViewLabel(int pos) {
    String label = "";

    if (this.definition.isSection()) {
      if (label.equals(""))
        label = block("field.multiple.table$tableView$emptyLabel", "position", String.valueOf(pos));
    } else if (this.definition.isNode()) {
        label = block("field.multiple.table$tableView$emptyLabel", "position", String.valueOf(pos));
    } else {
      label = "";
    }

    return label;
  }

  protected String getMultipleFieldTableViewValue(FieldProperty fieldDefinition) {

    if (fieldDefinition == null)
      return "";

    if (fieldDefinition.isCheck()) {
      String result = "";
      result += "check 1,&nbsp;";
      result += "check 2,&nbsp;";
      result += "check 3";
      return result;
    } else if (fieldDefinition.isSelect()) {
      String result = "select";
      return result;
    } else
      return "";
  }

  protected HashMap<Integer, Integer> calculateMultipleFieldTableViewColumnWidths(ArrayList<Ref> fieldList) {
    HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
    int restWidth = 1;
    int defaultWidth = 0;

    defaultWidth = Math.round(restWidth / fieldList.size());
    for (Integer columnIndex : result.keySet()) {
      Integer width = result.get(columnIndex);
      if (width == null) {
        width = defaultWidth;
        result.put(columnIndex, width);
      }
    }

    return result;
  }

  protected String initMultipleFieldTableView(HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    int rowPos = 0, columnPos = 0;
    String elements = "";
    String headerColumns = "", templateColumns = "";
    LinkedHashMap<Integer, LinkedHashMap<Integer, String>> rows = new LinkedHashMap<Integer, LinkedHashMap<Integer, String>>();
    ArrayList<Ref> fieldList;
    HashMap<Integer, Integer> columnWidths;
    FormDefinition formDefinition = (FormDefinition)this.getParameter("formDefinition");

    if (this.definition.isSection()) {
      SectionFieldProperty.ViewProperty viewDefinition = ((SectionFieldProperty) this.definition).getView();

      fieldList = viewDefinition.getSummary().getField();
      columnWidths = this.calculateMultipleFieldTableViewColumnWidths(fieldList);
      for (Ref fieldRef : fieldList) {
        FieldProperty fieldDefinition = formDefinition.getField(fieldRef.getValue());
        map.put("code", fieldDefinition.getCode());
        map.put("label", language.getModelResource(fieldDefinition.getLabel(), this.codeLanguage));
        map.put("width", String.valueOf(columnWidths.get(columnPos)));
        map.put("link", (columnPos == 0) ? "link" : "");
        headerColumns += block("field.multiple.table$tableView$headerColumn", map);
        templateColumns += block("field.multiple.table$tableView$templateColumn", map);

        rowPos = 0;
        for (int i=0; i<3; i++) {
          LinkedHashMap<Integer, String> row = rows.get(rowPos);
          String value;

          if (row == null) {
            row = new LinkedHashMap<Integer, String>();
            rows.put(rowPos, row);
          }

          if (columnPos == 0)
            value = this.getMultipleFieldTableViewLabel(0);
          else
            value = this.getMultipleFieldTableViewValue(fieldDefinition);

          if (value.isEmpty())
            value = "&nbsp;";

          map.put("code", fieldDefinition.getCode());
          map.put("value", value);
          map.put("width", String.valueOf(columnWidths.get(columnPos)));
          map.put("link", (columnPos == 0) ? "link" : "");
          row.put(columnPos, block("field.multiple.table$tableView$element$column", map));
          map.clear();
          rowPos++;
        }

        columnPos++;
      }

      rowPos = 0;
      for (HashMap<Integer, String> row : rows.values()) {
        String columns = "";

        for (String value : row.values())
          columns += value;

        map.put("code", this.definition.getCode());
        map.put("position", String.valueOf(rowPos));
        map.put("columns", columns);
        elements += block("field.multiple.table$tableView$element", map);

        rowPos++;
      }

    } else if (this.definition.isNode()) {
      // TODO
    }

    map.put("headerColumns", headerColumns);
    map.put("templateColumns", templateColumns);
    map.put("elements", elements);

    return block("field.multiple.table$tableView", map);
  }

  protected void initMultipleField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    boolean isSection = this.definition.isSection();
    String elements = "";
    int pos = 0;
    boolean isTableView = this.isTableView();

    if (isTableView) {
      map.put("label", this.getMultipleFieldTableViewLabel(pos));
      map.put("fieldSingle", this.initSingleField(id + ".0", viewMap, declarationsMap, isTemplate));
      elements += block("field.multiple.table$element", map);
      map.clear();
    } else {
      for (int i=0; i<1; i++) {
        map.put("label", "");
        map.put("fieldSingle", this.initSingleField(id + "." + pos, viewMap, declarationsMap, isTemplate));
        elements += block("field.multiple$element", map);
        map.clear();
        pos++;
      }
    }

    map.put("id", id);
    map.put("extended", (isSection) ? "false" : "");
    map.put("conditioned", (isSection) ? "false" : "");
    map.put("elements", elements);
    map.put("template", this.initSingleField(id, viewMap, declarationsMap, true));
    map.put("tableView", "");
    if (isTableView)
      map.put("tableView", this.initMultipleFieldTableView(viewMap, declarationsMap));

    viewMap.put("field", isTableView ? block("field.multiple.table", map) : block("field.multiple", map));
  }

  protected String initSingleField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    boolean isSection = this.definition.isSection();
    String body = "";
    SectionFieldProperty sectionDeclaration = isSection ? (SectionFieldProperty) this.definition : null;
    String conditioned = (isSection) ? "false" : "";
    boolean sectionHidden = false;

    if (isSection && sectionDeclaration.isConditional())
      sectionHidden = conditioned.equals("conditioned") ? false : true;

    map.put("super", this.definition.isSuperfield() ? "false" : "");
    map.put("extended", (isSection) ? "false" : "");
    map.put("sectionHidden", sectionHidden ? "yes" : "");
    map.put("conditioned", conditioned);
    map.put("type", this.getFieldType());
    map.put("memo", (this.definition.isMemo()) ? "memo" : "");

    if (this.definition.isSection())
      body = this.initSectionField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isText())
      body = this.initTextField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isMemo())
      body = this.initMemoField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isBoolean())
      body = this.initBooleanField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isNumber())
      body = this.initNumberField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isDate())
      body = this.initDateField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isFile())
      body = this.initFileField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isPicture())
      body = this.initPictureField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isSelect())
      body = this.initSelectField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isLink())
      body = this.initLinkField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isCheck())
      body = this.initCheckField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isNode())
      body = this.initNodeField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isSerial())
      body = this.initSerialField(id, viewMap, declarationsMap, isTemplate);
    else if (this.definition.isSummation())
      body = this.initSummationField(id, viewMap, declarationsMap, isTemplate);
    else
      body = block("field.unknown", map);

    map.put("body", body);

    return block("field.single", map);
  }

  private String initSectionField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    SectionFieldProperty sectionDeclaration = (SectionFieldProperty) this.definition;
    String index = "";
    String fields = "";
    SectionFieldProperty.ViewProperty viewDefinition = sectionDeclaration.getView();
    ArrayList<String> fieldNames = new ArrayList<String>();
    FormDefinition formDefinition = (FormDefinition)this.getParameter("formDefinition");

    if (viewDefinition != null && viewDefinition.getShow() != null && viewDefinition.getShow().getField().size() > 0) {
      for (Ref show : viewDefinition.getShow().getField())
        fieldNames.add(show.getValue());
    } else {
      for (FieldProperty fieldDefinition : sectionDeclaration.getAllFieldPropertyList())
        fieldNames.add(fieldDefinition.getName());
    }
    
    for (String fieldName : fieldNames) {
      FieldProperty fieldDefinition = formDefinition.getField(fieldName);

      try {
        PreviewRender render = this.rendersFactory.get(fieldDefinition, "view.html", this.codeLanguage);
        render.setParameters(this.getParameters());
        render.setParameter("id", id);
        render.setParameter("isRoot", "false");
        render.setParameter("isTemplate", isTemplate);
        map.put("render(view.field)", render.getOutput());
      }
      catch (Exception exception) {
        this.problems.add(new Problem(String.format("Compiling definition %s", fieldDefinition.getName()), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
      }
      
      fields += block("field.section$field", map);
      
      map.clear();
    }

    map.put("labelField", "");
    if (viewDefinition != null && viewDefinition.getSummary() != null && viewDefinition.getSummary().getField().size() > 0) {
      Ref firstFieldRef = viewDefinition.getSummary().getField().get(0);
      String codeField = sectionDeclaration.getField(firstFieldRef.getValue()).getCode();
      map.put("labelField", codeField);
    }
    declarationsMap.put("concreteDeclarations", block("field.section$concreteDeclarations", map));

    map.put("id", id);
    map.put("value", "");
    map.put("index", index);
    map.put("fields", fields);

    return block("field.section", map);
  }

  private String initTextField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    TextFieldProperty definition = (TextFieldProperty) this.definition;
    TextFieldProperty.EditionProperty editionDefinition = definition.getEdition();
    TextFieldProperty.LengthProperty lengthDefinition = definition.getLength();
    String datastore = "";

    if (definition.allowHistory()) {
      datastore = definition.getAllowHistory().getDatastore();
      if (datastore == null || datastore.isEmpty())
        datastore = definition.getCode();
    }

    map.put("historyDatastore", datastore);
    map.put("edition", (editionDefinition != null) ? editionDefinition.getMode().toString().toLowerCase() : "");
    map.put("length", (lengthDefinition != null) ? String.valueOf(lengthDefinition.getMax()) : "");

    String patterns = "";
    for (TextFieldProperty.PatternProperty patternDefinition : definition.getPatternList()) {
      int pos = 0;
      String indicators = "";
      HashMap<String, Object> localMap = new HashMap<String, Object>();

      for (TextFieldProperty.PatternProperty.MetaProperty metaDefinition : patternDefinition.getMetaList()) {
        localMap.put("comma", (pos != 0) ? "comma" : "");
        localMap.put("indicator", metaDefinition.getIndicator());
        indicators += block("field.text$pattern$indicator", localMap);
        localMap.clear();
        pos++;
      }

      localMap.put("indicators", indicators);
      localMap.put("regexp", language.getModelResource(patternDefinition.getRegexp(), this.codeLanguage));

      patterns += block("field.text$pattern", localMap);
    }
    map.put("patterns", patterns);
    declarationsMap.put("concreteDeclarations", block("field.text$concreteDeclarations", map));

    map.put("id", id);
    map.put("code", "");
    map.put("value", "");

    return block("field.text", map);
  }

  private String initMemoField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    MemoFieldProperty definition = (MemoFieldProperty)this.definition;
    MemoFieldProperty.EditionProperty editionDefinition = definition.getEdition();
    MemoFieldProperty.LengthProperty lengthDefinition = definition.getLength();

    map.put("historyDatastore", definition.allowHistory() ? definition.getAllowHistory().getDatastore() : "");
    map.put("edition", (editionDefinition != null) ? editionDefinition.getMode().toString().toLowerCase() : "");
    map.put("length", (lengthDefinition != null) ? String.valueOf(lengthDefinition.getMax()) : "");
    declarationsMap.put("concreteDeclarations", block("field.memo$concreteDeclarations", map));

    map.put("id", id);
    map.put("value", "");

    return block("field.memo", map);
  }

  private String initBooleanField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String code, checkedBlock, label;
    boolean checked;

    code = "";
    checked = (code.equals("true") || code.equals("yes"));

    map.put("id", id);
    map.put("imagesPath", this.getParameterAsString("imagesPath"));
    checkedBlock = checked ? block("field.boolean$checked", map) : block("field.boolean$unchecked", map);

    map.clear();
    map.put("unchecked", checked ? "" : "unchecked");
    map.put("declarationLabel", language.getModelResource(this.definition.getLabel(), this.codeLanguage).toLowerCase());
    label = block("field.boolean$label", map);

    map.clear();
    map.put("id", id);
    map.put("label", label);
    map.put("checked", checkedBlock);

    return block("field.boolean", map);
  }

  private String initNumberField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    NumberFieldProperty definition = (NumberFieldProperty) this.definition;
    NumberFieldProperty.RangeProperty rangeDefinition = definition.getRange();
    String metrics = "";
    String metricLabel = "";

    map.put("range", "");
    if (definition.getRange() != null) {
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      localMap.put("min", String.valueOf(rangeDefinition.getMin()));
      localMap.put("max", String.valueOf(rangeDefinition.getMax()));
      map.put("range", block("field.number$range", map));
    }
    map.put("format", definition.getFormat() != null ? definition.getFormat() : "");
    declarationsMap.put("concreteDeclarations", block("field.number$concreteDeclarations", map));

    map.put("id", id);
    map.put("value", "");
    map.put("metrics", block("field.number$metrics", "items", metrics));
    map.put("metricLabel", metricLabel);

    return block("field.number", map);
  }

  private String initDateField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    DateFieldProperty declaration = (DateFieldProperty) this.definition;

    map.put("format", declaration.getFormat(this.codeLanguage));
    declarationsMap.put("concreteDeclarations", block("field.date$concreteDeclarations", map));

    map.put("id", id);
    map.put("value", "");
    map.put("internal", "");

    return block("field.date", map);
  }

  private String initFileField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();

    map.put("id", id);
    map.put("action", "javascript:void(null)");
    map.put("filename", "");
    map.put("value", "");

    return block("field.file.empty", map);
  }

  private String initPictureField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String title = "", lightBox = "", photoLink = "", thumbPhotoLink = "";
    PictureFieldProperty definition = (PictureFieldProperty) this.definition;

    photoLink = "../images/no-picture.jpg";
    thumbPhotoLink = photoLink;
    lightBox = "";

    if (definition.getSize() != null) {
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      localMap.put("width", String.valueOf(definition.getSize().getWidth()));
      localMap.put("height", String.valueOf(definition.getSize().getHeight()));
      map.put("size", block("field.picture$size", localMap));
    } else
      map.put("size", "");
    declarationsMap.put("concreteDeclarations", block("field.picture$concreteDeclarations", map));

    map.put("id", id);
    map.put("value", "");
    map.put("title", title);
    map.put("lightBox", lightBox);
    map.put("photoLink", photoLink);
    map.put("thumbPhotoLink", thumbPhotoLink);

    return (lightBox.isEmpty()) ? block("field.picture.noLightBox", map) : block("field.picture", map);
  }

  private HashMap<String, String> initSelectFieldTerm(Term term, boolean flatten, String id, String code, String value, String other, int pos) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String termCode = term.getCode();
    HashMap<String, String> result = new HashMap<String, String>();
    SelectFieldProperty definition = (SelectFieldProperty) this.definition;

    map.put("id", id);
    map.put("code", term.getCode());
    map.put("label", flatten ? term.getFlattenLabel() : language.getModelResource(term.getLabel(), this.codeLanguage));
    map.put("ancestorLevel", String.valueOf(term.getAncestorLevel()));

    map.put("checked", ((pos == 0 && (code.isEmpty() || other.isEmpty())) || (termCode.equals(code) || termCode.equals(other))) ? "true" : "");
    map.put("allowCode", definition.allowKey() ? block("field.select$allowCode", map) : "");

    if (term.isCategory()) {
      result.put("terms", block("field.select$declarationTerms.inline$term.category", map));
      result.put("radioTerms", block("field.select.inline$item.category", map));
    } else if (term.isSuperTerm()) {
      result.put("terms", block("field.select$declarationTerms.inline$term.super", map));
      result.put("radioTerms", block("field.select.inline$item.super", map));
    } else {
      result.put("terms", block("field.select$declarationTerms.inline$term", map));
      result.put("radioTerms", block("field.select.inline$item", map));
    }

    map.clear();
    if (value.isEmpty() && pos == 0) {
      code = term.getCode();
      value = language.getModelResource(term.getLabel(), this.codeLanguage);
    }

    for (Term childTerm : term.getTermList()) {
      pos++;
      HashMap<String, String> termsMap = this.initSelectFieldTerm(childTerm, flatten, id, code, value, other, pos);
      result.put("terms", result.get("terms") + termsMap.get("terms"));
      result.put("radioTerms", result.get("radioTerms") + termsMap.get("radioTerms"));
    }

    return result;
  }

  private HashMap<String, String> initSelectFieldTermList(TermList termList, boolean flatten, String id, String code, String value, String other) {
    int pos = 0;
    HashMap<String, String> result = new HashMap<String, String>();
    String terms = "", radioTerms = "";

    for (Term term : termList) {
      HashMap<String, String> termMap = this.initSelectFieldTerm(term, flatten, id, code, value, other, pos);
      terms += termMap.get("terms");
      radioTerms += termMap.get("radioTerms");
      pos++;
    }

    result.put("terms", terms);
    result.put("radioTerms", radioTerms);

    return result;
  }

  private String getSelectFieldThesaurusFrom(Object from) {
    String fromParameter = "-1";
    if(from != null) {
      if(from instanceof String)
        fromParameter = (String)from;
      else if(from instanceof Ref) {
        fromParameter = "";
      }
    }
    return fromParameter;
  }
  
  private String initSelectField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    SelectFieldProperty declaration = (SelectFieldProperty) this.definition;
    boolean allowOther = declaration.allowOther();
    SelectFieldProperty.SourceProperty sourceDefinition = declaration.getSource();
    String code, value, other, datastore = "";
    String blockName = "field.select";
    HashMap<String, String> termsMap = null;
    boolean flatten = false;

    code = "";
    value = "";
    other = "";

    if (declaration.allowHistory()) {
      datastore = declaration.getAllowHistory().getDatastore();
      if (datastore == null || datastore.isEmpty())
        datastore = declaration.getCode();
    }

    if (allowOther && (!other.isEmpty())) {
      value = "";
      blockName = "field.select.other";
    }

    if (declaration.isEmbedded()) {
      TermList termList;
      HashMap<String, Object> localMap = new HashMap<String, Object>();

      if (declaration.getTerms() != null)
        termList = new TermList(declaration.getTerms().getTermPropertyList());
      else {
        ThesaurusDefinition thesaurusDefinition = dictionary.getThesaurusDefinition(declaration.getSource().getThesaurus().getValue());
        termList = new TermList(thesaurusDefinition.getTerms().getTermPropertyList());
      }

      termsMap = this.initSelectFieldTermList(termList, flatten, id, code, value, other);

      localMap.put("allowCode", declaration.allowKey() ? "allowCode" : "");
      localMap.put("terms", termsMap.get("terms"));

      blockName = "field.select.inline";

      map.put("declarationTerms", block("field.select$declarationTerms.inline", localMap));
    } else if (sourceDefinition != null) {
      HashMap<String, Object> localMap = new HashMap<String, Object>();

      localMap.put("thesaurus", dictionary.getDefinition(sourceDefinition.getThesaurus().getValue()).getCode());
      localMap.put("flatten", (sourceDefinition.getFlatten() != null) ? sourceDefinition.getFlatten().toString().toLowerCase() : FlattenEnumeration.ALL.toString());
      localMap.put("depth", String.valueOf(sourceDefinition.getDepth()));
      localMap.put("from", this.getSelectFieldThesaurusFrom(sourceDefinition.getFrom()));
      localMap.put("allowCode", declaration.allowKey() ? "allowCode" : "");
      localMap.put("filters", "");

      map.put("declarationTerms", block("field.select$declarationTerms", localMap));
    }

    map.put("allowCode", declaration.allowKey() ? "allowCode" : "");
    map.put("historyDatastore", datastore);
    map.put("searchThesaurus", declaration.allowSearch() ? dictionary.getDefinition(sourceDefinition.getThesaurus().getValue()).getCode() : "");
    declarationsMap.put("concreteDeclarations", block("field.select$concreteDeclarations", map));

    map.put("id", id);
    map.put("code", code);
    map.put("value", value);
    map.put("other", other);
    map.put("items", termsMap != null ? termsMap.get("radioTerms") : "");

    return block(blockName, map);
  }
  
  private String initLinkField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String code = "";
    LinkFieldProperty fieldDefinition = (LinkFieldProperty) this.definition;
    LinkFieldProperty.SourceProperty fieldSourceDefinition = fieldDefinition.getSource();
    IndexDefinition indexDefinition = dictionary.getIndexDefinition(fieldSourceDefinition.getIndex().getValue());
    String datastore = "", attributes = "", header;
    int pos = 0;
    String nodeTypes = "";
    String value = "";

    if (fieldDefinition.allowHistory()) {
      datastore = fieldDefinition.getAllowHistory().getDatastore();
      if (datastore == null || datastore.isEmpty())
        datastore = fieldDefinition.getCode();
    }
    
    if (indexDefinition.getReference() != null) {
      for (AttributeProperty attributeDefinition : indexDefinition.getReference().getAttributePropertyList()) {
        HashMap<String, Object> localMap = new HashMap<String, Object>();
  
        localMap.put("code", attributeDefinition.getCode());
        localMap.put("label", language.getModelResource(attributeDefinition.getLabel(), this.codeLanguage));
        localMap.put("class", (pos == 0) ? "valuecl" : "");
  
        attributes += block("field.link$concreteDeclarations$header$attribute", localMap);
        pos++;
      }
    }

    if (fieldDefinition.allowAdd()) {
      String codeSourceDefinition = dictionary.getDefinition(fieldDefinition.getAllowAdd().getCollection().getValue()).getCode();
      NodeDefinition sourceDefinition = dictionary.getNodeDefinition(codeSourceDefinition);
    
      pos = 0;
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      if (((CollectionDefinition) sourceDefinition).getAdd() != null) {
        for (Ref add : ((CollectionDefinition) sourceDefinition).getAdd().getNode()) {
          for (Definition definition : dictionary.getAllImplementersOfNodeDefinition(add.getValue())) {
            String codeNode = definition.getCode();
            localMap.put("comma", (pos > 0)?"comma":"");
            localMap.put("nodeType", codeNode);
            nodeTypes += block("field.link$concreteDeclarations$nodeType", localMap);
            pos++;
          }
        }
      }
    }
    
    map.put("attributes", attributes);
    header = block("field.link$concreteDeclarations$header", map);

    map.clear();
    map.put("source", indexDefinition.getCode());
    map.put("nodeTypes", nodeTypes);
    map.put("allowOther", (fieldDefinition.allowAdd()) ? "other" : "");
    map.put("allowHistory", datastore);
    map.put("allowSearch", fieldDefinition.allowSearch() ? indexDefinition.getCode() : "");
    map.put("header", header);
    map.put("filters", "");
    declarationsMap.put("concreteDeclarations", block("field.link$concreteDeclarations", map));

    map.put("id", id);
    map.put("code", code);
    map.put("value", value);

    return value.isEmpty() ? block("field.link.empty", map) : block("field.link", map);
  }

  private HashMap<String, String> initCheckFieldTerm(Term term, Term parent, boolean flatten, String id, HashMap<String, Boolean> checkedAttributes, int pos) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    HashMap<String, String> result = new HashMap<String, String>();
    Boolean checked = checkedAttributes.get(term.getCode());
    String checkedBlock;
    CheckFieldProperty definition = (CheckFieldProperty) this.definition;

    map.put("id", id + pos);
    map.put("code", term.getCode());
    map.put("label", flatten ? term.getFlattenLabel() : language.getModelResource(term.getLabel(), this.codeLanguage));
    map.put("ancestorLevel", String.valueOf(term.getAncestorLevel()));
    map.put("checked", checked != null ? "true" : "");
    map.put("parent", parent != null ? parent.getCode() : "");
    map.put("imagesPath", this.getParameterAsString("imagesPath"));

    checkedBlock = (checked != null && checked) ? block("field.check$item$checked", map) : block("field.check$item$unchecked", map);
    map.put("checked", checkedBlock);
    map.put("allowCode", definition.allowKey() ? block("field.check$allowCode", map) : "");

    if (term.isCategory()) {
      result.put("checkTerms", block("field.check$item.category", map));
    } else if (term.isSuperTerm()) {
      result.put("checkTerms", block("field.check$item.super", map));
    } else {
      result.put("checkTerms", block("field.check$item", map));
    }

    map.clear();

    for (Term childTerm : term.getTermList()) {
      pos++;
      HashMap<String, String> termsMap = this.initCheckFieldTerm(childTerm, term, flatten, id, checkedAttributes, pos);
      result.put("checkTerms", result.get("checkTerms") + termsMap.get("checkTerms"));
    }

    return result;
  }

  private HashMap<String, String> initCheckFieldTermList(TermList termList, boolean flatten, String id, HashMap<String, Boolean> checkedAttributes) {
    int pos = 0;
    HashMap<String, String> result = new HashMap<String, String>();
    String checkTerms = "";

    for (Term term : termList) {
      HashMap<String, String> termMap = this.initCheckFieldTerm(term, null, flatten, id, checkedAttributes, pos);
      checkTerms += termMap.get("checkTerms");
      pos++;
    }

    result.put("checkTerms", checkTerms);

    return result;
  }

  private String initCheckField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    CheckFieldProperty definition = (CheckFieldProperty) this.definition;
    TermList termList;
    HashMap<String, String> termsMap;
    HashMap<String, Boolean> checkedAttributes = new HashMap<String, Boolean>();
    boolean flatten = false;

    if (definition.getTerms() != null)
      termList = new TermList(definition.getTerms().getTermPropertyList());
    else {
      ThesaurusDefinition thesaurusDefinition = dictionary.getThesaurusDefinition(definition.getSource().getThesaurus().getValue());
      termList = new TermList(thesaurusDefinition.getTerms().getTermPropertyList());
    }

    termsMap = this.initCheckFieldTermList(termList, flatten, id, checkedAttributes);

    map.put("items", termsMap.get("checkTerms"));

    return block("field.check", map);
  }

  private String initNodeField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String nodeContent = "";
    NodeFieldProperty fieldDefinition = (NodeFieldProperty) this.definition;
    List<Ref> addList = fieldDefinition.getAdd() != null ? fieldDefinition.getAdd().getNode() : null;
    NodeFieldProperty.ContainProperty containDefinition = fieldDefinition.getContain();
    String nameNode = null;
    FormDefinition formDefinition = (FormDefinition)this.getParameter("formDefinition");

    map.put("nodeTypes", "");
    if (addList.size() > 0)
      nameNode = fieldDefinition.getAdd().getNode().get(0).getValue();
    else if (containDefinition != null)
      nameNode = containDefinition.getNode().getValue();

    if (nameNode != null) {
      NodeDefinition definition = dictionary.getNodeDefinition(nameNode);
      map.put("nodeTypes", definition.getCode());
      if (definition.getDefaultWidgetView() != null) map.put("codeView", definition.getDefaultWidgetView().getCode());
      declarationsMap.put("concreteDeclarations", (formDefinition.isDocument()) ? block("field.node$concreteDeclarations.document", map) : block("field.node$concreteDeclarations", map));
    }

    nodeContent = block("field.node$node.empty", map);

    map.put("id", id);
    map.put("code", "");
    map.put("value", "");
    map.put("node", nodeContent);

    return block("field.node", map);
  }

  private String initSerialField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    SerialFieldProperty definition = (SerialFieldProperty) this.definition;
    SerialFieldProperty.SerialProperty serialDefinition = definition.getSerial();

    map.put("format", serialDefinition != null ? serialDefinition.getFormat() : "");
    declarationsMap.put("concreteDeclarations", block("field.serial$concreteDeclarations", map));

    map.put("id", id);
    map.put("code", "");
    map.put("value", "");

    return block("field.serial", map);
  }

  private String initSummationFieldItem(SummationItem item, int position) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String items = "";
    String blockName = "", labelBlock = "";

    if (item.Children.size() > 0)
      blockName = "field.summation$item.composite";
    else {
      if (item.Type == SummationItemProperty.TypeEnumeration.SIMPLE)
        blockName = "field.summation$item.simple";
    }

    if (item.IsMultiple)
      labelBlock = "field.summation$item.simple$label.multiple";
    else {
      if (item.Children.size() > 0)
        labelBlock = "field.summation$item.simple$label.single$expandable";
      else
        labelBlock = "field.summation$item.simple$label.single";
    }

    map.put("value", String.valueOf(item.Value));
    map.put("type", item.Type.toString().toLowerCase());
    map.put("imagesPath", this.getParameterAsString("imagesPath"));
    map.put("label", language.getModelResource(item.Label, this.codeLanguage)); // Do not delete this map attribute although
                                                                                // been override on next line
    map.put("label", block(labelBlock, map));
    map.put("multiple", item.IsMultiple ? "multiple" : "");
    map.put("negative", item.IsNegative ? "negative" : "");
    map.put("evenOdd", (position % 2 == 0) ? "even" : "odd");

    for (SummationItem child : item.Children) {
      position = position + 1;
      items += this.initSummationFieldItem(child, position);
    }
    map.put("items", items);

    return block(blockName, map);
  }

  private String initSummationField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String items = "";
    SummationFieldProperty definition = (SummationFieldProperty) this.definition;
    SummationFieldProperty.SourceProperty sourceDefinition = definition.getSource();
    String formatDefinition = definition.getFormat();
    SummationItemList summationItemList = null;

    summationItemList = new SummationItemList(definition.getItems().getSummationItemPropertyList());
    declarationsMap.put("concreteDeclarations", block("field.summation$concreteDeclarations", map));

    for (SummationItem item : summationItemList.Items) {
      items += this.initSummationFieldItem(item, 0);
    }

    map.put("id", id);
    map.put("items", items);

    map.put("thesaurus", dictionary.getDefinition(sourceDefinition.getThesaurus().getValue()).getCode());
    map.put("flatten", (sourceDefinition.getFlatten() != null) ? sourceDefinition.getFlatten().toString().toLowerCase() : FlattenEnumeration.ALL.toString());
    map.put("depth", String.valueOf(sourceDefinition.getDepth()));
    map.put("from", (sourceDefinition.getFrom() != null) ? sourceDefinition.getFrom() : "");
    map.put("format", formatDefinition != null ? formatDefinition : "");
    declarationsMap.put("concreteDeclarations", block("field.summation$concreteDeclarations", map));

    map.put("value", "");

    return block("field.summation", map);
  }

  private String getFieldType() {

    if (this.definition.isBoolean())
      return "boolean";
    else if (this.definition.isCheck())
      return "check";
    else if (this.definition.isDate())
      return "date";
    else if (this.definition.isFile())
      return "file";
    else if (this.definition.isLink())
      return "link";
    else if (this.definition.isText() || this.definition.isMemo())
      return "text";
    else if (this.definition.isNode())
      return "node";
    else if (this.definition.isNumber())
      return "number";
    else if (this.definition.isPicture())
      return "picture";
    else if (this.definition.isSection())
      return "section";
    else if (this.definition.isSelect())
      return "select";
    else if (this.definition.isSerial())
      return "serial";
    else if (this.definition.isSummation())
      return "summation";

    return "";
  }

  private boolean isInvisible() {
    // TODO este método depende de lo que digan las reglas
    return false;
  }

  @Override
  protected void init() {
    loadCanvas("field");
    super.init();
  }

}
