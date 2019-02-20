EDITOR_DIALOG_DATE_WINDOW_SIZE = 14;
INIT = "ini";
BEFORE = "bef";
AFTER = "aft";

CGEditorDialogDate = function (extLayer) {
  this.base = CGEditorDialog;
  this.base(extLayer);
  this.init();
  this.timeout = null;
};

CGEditorDialogDate.prototype = new CGEditorDialog;

CGEditorDialogDate.prototype.init = function () {

  this.dtSelected = new Date();

  this.datePicker = new DatePicker();
  this.datePicker.onChange = CGEditorDialogDate.prototype.atDatePickerChange.bind(this);
  this.datePicker.init(this.extLayer.select(".datepicker").first(), Context.Config.Language);
  this.datePicker.setDate(this.dtSelected);
};

CGEditorDialogDate.prototype.isTimeHour = function () {
  var oRegExp = new RegExp(/H/g);
  return (oRegExp.exec(this.Configuration.Format) != null);
};

CGEditorDialogDate.prototype.isTimeMinute = function () {
  var oRegExp = new RegExp(/i/g);
  return (oRegExp.exec(this.Configuration.Format) != null);
};

CGEditorDialogDate.prototype.isTimeSecond = function () {
  var oRegExp = new RegExp(/s/g);
  return (oRegExp.exec(this.Configuration.Format) != null);
};

CGEditorDialogDate.prototype.isDateYear = function () {
  var oRegExp = new RegExp(/L|Y|y/g);
  return (oRegExp.exec(this.Configuration.Format) != null);
};

CGEditorDialogDate.prototype.isDateMonth = function () {
  var oRegExp = new RegExp(/W|F|m|M|n|t/g);
  return (oRegExp.exec(this.Configuration.Format) != null);
};

CGEditorDialogDate.prototype.isDateDay = function () {
  var oRegExp = new RegExp(/d|D|j|l|S|w|z/g);
  return (oRegExp.exec(this.Configuration.Format) != null);
};

CGEditorDialogDate.prototype.refresh = function () {
  var Precision = DATE_PRECISION_HOURS;
  var Mode = DATE_MODE_RELATIVE;

  if (this.Configuration.Edition == "random")
    Mode = DATE_MODE_ABSOLUTE;

  if (this.isTimeSecond()) Precision = DATE_PRECISION_SECONDS;
  else if (this.isTimeMinute()) Precision = DATE_PRECISION_MINUTES;
  else if (this.isTimeHour()) Precision = DATE_PRECISION_HOURS;
  else if (this.isDateDay()) Precision = DATE_PRECISION_DAYS;
  else if (this.isDateMonth()) Precision = DATE_PRECISION_MONTHS;
  else if (this.isDateYear()) Precision = DATE_PRECISION_YEARS;

  this.datePicker.setDate(this.dtSelected);
  this.datePicker.setPrecision(Precision);
  this.datePicker.setMode(Mode);
  this.datePicker.refresh();
};

CGEditorDialogDate.prototype.getDateAsString = function () {
  return this.dtSelected.format(Date.getPattern(this.Configuration.Format));
};

CGEditorDialogDate.prototype.updateSelectedDate = function (iDay, iMonth, iYear, sTime) {
  this.dtSelected = this.datePicker.getDate();
  if (this.onSelect) this.onSelect({code: null, value: this.getDateAsString()});
};

CGEditorDialogDate.prototype.getData = function () {
  return this.getDateAsString();
};

CGEditorDialogDate.prototype.setData = function (Data) {
  if (Data.value == "") return;

  var dtDate = Date.parseDate(Data.value, Date.getPattern(this.Configuration.Format));
  if (dtDate != null) this.dtSelected = dtDate;
};

// #############################################################################################################
CGEditorDialogDate.prototype.atDatePickerChange = function () {
  if (this.timeout != null) window.clearTimeout(this.timeout);
  this.timeout = window.setTimeout(CGEditorDialogDate.prototype.updateSelectedDate.bind(this), 300);
};