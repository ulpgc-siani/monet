function DataTable() {
  this.jLayer = null;
};

DataTable.templates = new Object();
DataTable.templates = {
  layout: "<table style='width:${width};'><thead><tr class='header_groups'></tr><tr class='header_columns'></tr></thead><tbody></tbody></table>",
  headerColumn: "<th class='${clazz}'>${label}</th>",
  headerColumnWithUnit: "<th>${label}<br/>(${unit})<div class='colorbox' style='background:${color};display:${displayColorBox}'>&nbsp;</div></th>",
  headerGroup: "<th colspan='${colspan}'>${label}</th>",
  row: "<tr class='${clazz}'></tr>",
  rowItem: "<td class='${clazz}'>${value}</td>"
};
DataTable.translations = {
  "es": {
    rowItemNull: "(sin valor)"
  },
  "en": {
    rowItemNull: "(no value)"
  }
};

DataTable.prototype.renderHeader = function () {
  var columnTemplate = DataTable.templates.headerColumn;
  var columnTemplateWithUnit = DataTable.templates.headerColumnWithUnit;
  var groupTemplate = DataTable.templates.headerGroup;
  var lastGroup = null;
  var colspan = 0;
  var jColumns = this.jLayer.find("table > thead > .header_columns");
  var jGroups = this.jLayer.find("table > thead > .header_groups");
  var colorPos = -1;

  for (var i = 0; i < this.columns.length; i++) {
    var group = this.columns[i];
    var hasChildren = group.children != null && group.children.length > 0;
    var template = group.unit != null ? columnTemplateWithUnit : columnTemplate;
    var displayColorBox = "none";
    var color = "#BBBBBB";

    if (this.colors != null && this.colors[colorPos] != null) {
      displayColorBox = "block";
      color = this.colors[colorPos];
    }

    if (hasChildren) {
      for (var j = 0; j < group.children.length; j++) {
        var column = group.children[j];
        var jColumn = $.tmpl(template, { label: this.grouped ? column : group.label + " - " + column, unit: group.unit, clazz: group.type, displayColorBox: displayColorBox, color: color });
        jColumns.append(jColumn);
        colorPos++;
      }
    }
    else {
      var jColumn = $.tmpl(template, { label: group.label, unit: group.unit, clazz: group.type, displayColorBox: displayColorBox, color: color });
      jColumns.append(jColumn);
    }

    if (this.grouped) {
      var label = (hasChildren) ? group.label : "";
      var colspan = (hasChildren) ? group.children.length : 1;
      var jGroup = $.tmpl(groupTemplate, { label: label, colspan: colspan });
      jGroups.append(jGroup);
    }

    colorPos++;
  }
};

DataTable.prototype.getRowType = function (position) {
  if (position == 0)
    return this.columns[0].type;

  var minPosition = 1;
  for (var i = 0; i < this.columns.length; i++) {
    var column = this.columns[i];
    var childrenCount = column.children ? column.children.length : 0;
    var maxPosition = minPosition + i + childrenCount - 1;
    if (maxPosition < 0) maxPosition = 0;
    if (position >= minPosition && position <= maxPosition)
      return column.type;
    minPosition = maxPosition + 1;
  }

  return "string";
};

DataTable.prototype.renderRows = function (rows) {
  for (var i = 0; i < rows.length; i++)
    this.renderRow(rows[i], i % 2 == 0 ? "even" : "odd");
};

DataTable.prototype.renderRow = function (row, clazz) {
  var jTable = this.jLayer.find("table > tbody");
  var rowTemplate = DataTable.templates.row;
  var rowItemTemplate = DataTable.templates.rowItem;
  var jRow = $.tmpl(rowTemplate, { clazz: clazz });

  for (var i = 0; i < row.length; i++) {
    var type = this.getRowType(i);
    var value = DataTable.translations[this.language].rowItemNull;

    if (row[i] != null)
      value = type == "number" ? row[i].formatted : row[i].value;

    var jRowItem = $.tmpl(rowItemTemplate, { value: value, clazz: type });
    jRow.append(jRowItem);
  }

  jRow.click(DataTable.prototype.atRowClick.bind(this));
  jTable.append(jRow);
};

DataTable.prototype.load = function (DOMLayer) {

  this.jLayer.find("table tbody").html("");

  if (this.rows != null) {
    this.renderRows(this.rows);
    return;
  }

};

DataTable.prototype.render = function (DOMLayer) {
  var width = this.width ? this.width : "100%";
  var height = this.height ? this.height : "100%";
  var jLayout = $.tmpl(DataTable.templates.layout, { width: width, height: height});

  this.jLayer = $(DOMLayer);
  this.jLayer.append(jLayout);
  this.jLayer.addClass("datatable");

  this.renderHeader();
  this.load();
};

DataTable.prototype.atRowClick = function (event) {
  var jRow = $(event.target).parents("tr");
  this.jLayer.find("table > tbody > tr").removeClass("selected");
  jRow.addClass("selected");
};