var DATE_PICKER_WINDOW_SIZE = 10;
var DIALOG_HOURS = "dhours";
var DIALOG_SECONDS = "dseconds";
var DIALOG_MINUTES = "dminutes";
var DIALOG_DAYS = "ddays";
var DIALOG_MONTHS = "dmonths";
var DIALOG_YEARS = "dyears";
var DATE_FORMAT_DAYS = "fdays";
var DATE_FORMAT_MONTHS = "fmonths";
var DATE_FORMAT_YEARS = "fyears";
var DATE_FORMAT_HOURS = "fhours";
var DATE_FORMAT_MINUTES = "fminutes";
var DATE_FORMAT_SECONDS = "fseconds";
var DATE_PRECISION_DAYS = "days";
var DATE_PRECISION_MONTHS = "months";
var DATE_PRECISION_YEARS = "years";
var DATE_PRECISION_HOURS = "hours";
var DATE_PRECISION_MINUTES = "minutes";
var DATE_PRECISION_SECONDS = "seconds";
var DATE_MODE_RELATIVE = "relative";
var DATE_MODE_ABSOLUTE = "absolute";
var DatePickerLanguage = "es";
var DATE_CLASS_ACTIVE = "active";
var DATE_CLASS_INACTIVE = "inactive";

var aDatePickerTemplates = new Array();
aDatePickerTemplates["es"] = new Array();
aDatePickerTemplates["es"]["MAIN_TEMPLATE"] = "<table><tr><td width='1px'><div class='date'><table class='toolbar'><tr><td><div class='button up' title='atrás'></div></td></tr><tr><td><div class='border'></div></td></tr></table><div class='dialog days'><ul class='list'></ul></div><div class='dialog months'><ul class='list'></ul></div><div class='dialog years'><ul class='list'></ul></div><table class='dialog selected'><tr><td class='daycell' width='57px'><div class='day'></div><div class='weekday'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td><td class='monthcell' width='140px'><div class='month'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td><td class='yearcell' width='59px'><div class='year'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td></tr></table><table class='toolbar bottom'><tr><td><div class='button down' title='adelante'></div></td></tr><tr><td><div class='border'></div></td></tr></table></div></td><td><div class='time'><table class='toolbar'><tr><td><div class='button up' title='atrás'></div></td></tr><tr><td><div class='border'></div></td></tr></table><div class='dialog hours'><ul class='list'></ul></div><div class='dialog minutes'><ul class='list'></ul></div><div class='dialog seconds'><ul class='list'></ul></div><table class='dialog selected' width='100%'><tr><td class='hourcell'><div class='hour'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td><td class='minutecell'><div class='minute'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td><td class='secondcell'><div class='second'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td></tr></table><table class='toolbar bottom'><tr><td><div class='button down' title='adelante'></div></td></tr><tr><td><div class='border'></div></td></tr></table></div></td></tr><tr><td colspan='2'><div class='behaviours'><div class='label'>Opciones</div><div class='setmode'><input type='checkbox' id='dpsetmode'/><label for='dpsetmode'>Usar un comportamiento circular al cambiar de fecha</label></div><div class='setprecision'><label>Cambiar la precisión de la fecha a</label><select href='javascript:void()'><option value='years'>años</option><option value='months'>meses</option><option value='days'>días</option><option value='hours'>horas</option><option value='minutes'>minutos</option><option value='seconds'>segundos</option></select></div></div></td></tr></table>";
aDatePickerTemplates["en"] = new Array();
aDatePickerTemplates["en"]["MAIN_TEMPLATE"] = "<table><tr><td width='1px'><div class='date'><table class='toolbar'><tr><td><div class='button up' title='back'></div></td></tr><tr><td><div class='border'></div></td></tr></table><div class='dialog days'><ul class='list'></ul></div><div class='dialog months'><ul class='list'></ul></div><div class='dialog years'><ul class='list'></ul></div><table class='dialog selected'><tr><td class='daycell' width='57px'><div class='day'></div><div class='weekday'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td><td class='monthcell' width='140px'><div class='month'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td><td class='yearcell' width='59px'><div class='year'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td></tr></table><table class='toolbar bottom'><tr><td><div class='button down' title='forward'></div></td></tr><tr><td><div class='border'></div></td></tr></table></div></td><td><div class='time'><table class='toolbar'><tr><td><div class='button up' title='back'></div></td></tr><tr><td><div class='border'></div></td></tr></table><div class='dialog hours'><ul class='list'></ul></div><div class='dialog minutes'><ul class='list'></ul></div><div class='dialog seconds'><ul class='list'></ul></div><table class='dialog selected' width='100%'><tr><td class='hourcell'><div class='hour'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td><td class='minutecell'><div class='minute'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td><td class='secondcell'><div class='second'></div><div class='buttons'><a class='button up' href='javascript:void(null)'></a><a class='button down' href='javascript:void(null)'></a></div></td></tr></table><table class='toolbar bottom'><tr><td><div class='button down' title='forward'></div></td></tr><tr><td><div class='border'></div></td></tr></table></div></td></tr><tr><td colspan='2'><div class='behaviours'><div class='label'>Options</div><div class='setmode'><input type='checkbox' id='dpsetmode'/><label for='dpsetmode'>Use circular behaviour to define the date</label></div><div class='setprecision'><label>Set date precision to</label><select href='javascript:void()'><option value='years'>years</option><option value='months'>months</option><option value='days'>days</option><option value='hours'>hours</option><option value='minutes'>minutes</option><option value='seconds'>seconds</option></select></div></div></td></tr></table>";

var aDatePickerMonths = {
  "es": ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
  "en": ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
};

var aDatePickerDays = {
  "es": ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
  "en": ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
};

function DatePicker() {
  this.extLayer = null;
  this.dtSelected = null;
  this.dtStartAnimation = null;
  this.ActiveDateDialog = DIALOG_DAYS;
  this.ActiveTimeDialog = DIALOG_SECONDS;
}

DatePicker.prototype.initBehaviours = function () {
  var extMonthsDialog = this.extLayer.select(".dialog.months").first();
  var extYearsDialog = this.extLayer.select(".dialog.years").first();

  extMonthsDialog.on("click", this.atMonthsDialogClick, this);
  extYearsDialog.on("click", this.atYearsDialogClick, this);

  this.extSelectedHourCell.on("click", this.atSelectedHourClick, this);
  this.extSelectedMinuteCell.on("click", this.atSelectedMinuteClick, this);
  this.extSelectedSecondCell.on("click", this.atSelectedSecondClick, this);
  this.extSelectedDayCell.on("click", this.atSelectedDayClick, this);
  this.extSelectedDayCell.on("mouseover", this.atSelectedDayOver, this);
  this.extSelectedMonthCell.on("click", this.atSelectedMonthClick, this);
  this.extSelectedMonthCell.on("mouseover", this.atSelectedMonthOver, this);
  this.extSelectedYearCell.on("click", this.atSelectedYearClick, this);
  this.extSelectedYearCell.on("mouseover", this.atSelectedYearOver, this);

  var extDateDialogSelected = this.extLayer.select(".date .dialog.selected").first();
  var extTimeDialogSelected = this.extLayer.select(".time .dialog.selected").first();

  var aExtButtonsUp = this.extLayer.select(".button");
  aExtButtonsUp.each(function (extButton) {
    extButton.on("click", this.atButtonClick, this);
  }, this);

  extDateDialogSelected.removeClass("mac");
  extTimeDialogSelected.removeClass("mac");
  if (Ext.isMac) {
    extDateDialogSelected.addClass("mac");
    extTimeDialogSelected.addClass("mac");
  }

  this.extSetMode.on("change", this.atSetModeChange, this);
  this.extSetPrecision.on("change", this.atSetPrecisionChange, this);
};

DatePicker.prototype.init = function (IdLayer, Language) {

  this.Language = Language;
  this.extLayer = Ext.get(IdLayer);
  this.extLayer.dom.innerHTML = aDatePickerTemplates[this.Language]["MAIN_TEMPLATE"];
  this.extLayer.addClass("datepicker");
  this.extLayer.addClass(this.Precision);

  var extTimeSelectedDialog = this.extLayer.select(".time .dialog.selected").first();
  this.extSelectedHourCell = extTimeSelectedDialog.select(".hourcell").first();
  this.extSelectedHour = extTimeSelectedDialog.select(".hour").first();
  this.extSelectedMinuteCell = extTimeSelectedDialog.select(".minutecell").first();
  this.extSelectedMinute = extTimeSelectedDialog.select(".minute").first();
  this.extSelectedSecondCell = extTimeSelectedDialog.select(".secondcell").first();
  this.extSelectedSecond = extTimeSelectedDialog.select(".second").first();

  var extDateSelectedDialog = this.extLayer.select(".date .dialog.selected").first();
  this.extSelectedDayCell = extDateSelectedDialog.select(".daycell").first();
  this.extSelectedDay = extDateSelectedDialog.select(".day").first();
  this.extSelectedWeekDay = extDateSelectedDialog.select(".weekday").first();
  this.extSelectedMonthCell = extDateSelectedDialog.select(".monthcell").first();
  this.extSelectedMonth = extDateSelectedDialog.select(".month").first();
  this.extSelectedYearCell = extDateSelectedDialog.select(".yearcell").first();
  this.extSelectedYear = extDateSelectedDialog.select(".year").first();

  this.extSetMode = this.extLayer.select(".behaviours .setmode input").first();
  this.extSetPrecision = this.extLayer.select(".behaviours .setprecision select").first();

  this.setPrecision(DATE_PRECISION_DAYS);
  this.setMode(DATE_MODE_ABSOLUTE);

  this.initBehaviours();
};

DatePicker.prototype.registerListBehaviours = function (extDialog) {
  var extItemList = extDialog.select("li");
  extItemList.each(function (extItem) {
    extItem.on("click", DatePicker.prototype.atDialogListItemClick, this);
  }, this);
};

DatePicker.prototype.unregisterListBehaviours = function (extDialog) {
  var extItemList = extDialog.select("li");
  extItemList.each(function (extItem) {
    extItem.un("click", DatePicker.prototype.atDialogListItemClick, this);
  }, this);
};

DatePicker.prototype.getDateTimeStamp = function (dtDate, Precision) {
  if (this.Mode == DATE_MODE_RELATIVE) return dtDate.valueOf();
  if (Precision == DATE_PRECISION_HOURS) return dtDate.getHours();
  if (Precision == DATE_PRECISION_MINUTES) return dtDate.getMinutes();
  if (Precision == DATE_PRECISION_SECONDS) return dtDate.getSeconds();
  if (Precision == DATE_PRECISION_DAYS) return dtDate.getDate();
  if (Precision == DATE_PRECISION_MONTHS) return dtDate.getMonth();
  if (Precision == DATE_PRECISION_YEARS) return dtDate.getFullYear();
};

DatePicker.prototype.getFormattedTime = function (dtDate, Format) {
  var sResult = "";
  var sHour = (dtDate.getHours() < 10 ? "0" + dtDate.getHours() : dtDate.getHours());
  var sMinute = (dtDate.getMinutes() < 10 ? "0" + dtDate.getMinutes() : dtDate.getMinutes());
  var sSecond = (dtDate.getSeconds() < 10 ? "0" + dtDate.getSeconds() : dtDate.getSeconds());

  if (Format == DATE_FORMAT_HOURS) return sHour;
  else if (Format == DATE_FORMAT_MINUTES) return sMinute;
  else if (Format == DATE_FORMAT_SECONDS) return sSecond;

  return sResult;
};

DatePicker.prototype.getFormattedDate = function (dtDate, Format) {
  var sFormat = "";

  Date.dayNames = eval("aDatePickerDays." + this.Language);
  Date.monthNames = eval("aDatePickerMonths." + this.Language);

  if (this.Mode == DATE_MODE_ABSOLUTE) {
    if (Format == DATE_FORMAT_DAYS) sFormat = "d";
    else if (Format == DATE_FORMAT_MONTHS) sFormat = "F";
    else if (Format == DATE_FORMAT_YEARS) sFormat = "Y";
  }
  else {
    switch (this.Language) {
      case "es" :
        if (Format == DATE_FORMAT_DAYS) sFormat = "d F Y";
        else if (Format == DATE_FORMAT_MONTHS) sFormat = "F Y";
        else if (Format == DATE_FORMAT_YEARS) sFormat = "Y";
        break;
      case "en" :
        if (Format == DATE_FORMAT_DAYS) sFormat = "l, F d, Y";
        else if (Format == DATE_FORMAT_MONTHS) sFormat = "F, Y";
        else if (Format == DATE_FORMAT_YEARS) sFormat = "Y";
        break;
      default:
        if (Format == DATE_FORMAT_DAYS) sFormat = "l, d \\d\\e F \\d\\e Y";
        else if (Format == DATE_FORMAT_MONTHS) sFormat = "F \\d\\e Y";
        else if (Format == DATE_FORMAT_YEARS) sFormat = "Y";
    }
  }

  return dtDate.format(sFormat);
};

DatePicker.prototype.updateDateFromId = function (dtDate, sId) {
  var sNewDate = sId.replace("h", "").replace("M", "").replace("s", "").replace("d", "").replace("m", "").replace("y", "");
  ;

  if (this.Mode == DATE_MODE_RELATIVE) dtDate.setTime(sNewDate);
  else {
    if (sId.indexOf("h") != -1) dtDate.setHours(sNewDate);
    else if (sId.indexOf("M") != -1) dtDate.setMinutes(sNewDate);
    else if (sId.indexOf("s") != -1) dtDate.setSeconds(sNewDate);
    else if (sId.indexOf("d") != -1) dtDate.setDate(sNewDate);
    else if (sId.indexOf("m") != -1) dtDate.setMonth(sNewDate);
    else if (sId.indexOf("y") != -1) dtDate.setFullYear(sNewDate);
  }

  this.setDate(dtDate);
  if (this.onChange) this.onChange(this);
};

DatePicker.prototype.setDate = function (dtDate) {
  this.dtSelected = dtDate;
};

DatePicker.prototype.setRange = function (dtFromDate, dtToDate) {
  this.DateRange = {from: dtFromDate, to: dtToDate};
};

DatePicker.prototype.updateDate = function (dtDate) {
  this.setDate(dtDate);
  this.refresh();
  if (this.onChange) this.onChange(this);
};

DatePicker.prototype.getDate = function () {
  return this.dtSelected;
};

DatePicker.prototype.isHoliday = function (dtDate) {
  if (this.Precision != DATE_PRECISION_DAYS && this.Precision != DATE_PRECISION_HOURS && this.Precision != DATE_PRECISION_MINUTES && this.Precision != DATE_PRECISION_SECONDS) return false;
  var weekDay = dtDate.getDay();
  return (weekDay == 0 || weekDay == 6);
};

DatePicker.prototype.isInvalid = function (dtDate) {
  if (this.DateRange == null) return false;

  var time = dtDate.getTime();

  var fromTime = this.DateRange.from.getTime();
  if (time < fromTime) return true;

  if (this.DateRange.to == null) return false;

  var toTime = this.DateRange.to.getTime();
  if (time > toTime) return true;

  return false;
};

DatePicker.prototype.getStartHour = function () {
  var dtStart = new Date();

  dtStart.setTime(this.dtSelected.valueOf());
  dtStart.setHours(dtStart.getHours() - (DATE_PICKER_WINDOW_SIZE / 2));

  return dtStart;
};

DatePicker.prototype.getStartMinute = function () {
  var dtStart = new Date();

  dtStart.setTime(this.dtSelected.valueOf());
  dtStart.setMinutes(dtStart.getMinutes() - (DATE_PICKER_WINDOW_SIZE / 2));

  return dtStart;
};

DatePicker.prototype.getStartSecond = function () {
  var dtStart = new Date();

  dtStart.setTime(this.dtSelected.valueOf());
  dtStart.setSeconds(dtStart.getSeconds() - (DATE_PICKER_WINDOW_SIZE / 2));

  return dtStart;
};

DatePicker.prototype.getStartDay = function () {
  var dtStart = new Date();

  dtStart.setTime(this.dtSelected.valueOf());
  dtStart.setDate(dtStart.getDate() - (DATE_PICKER_WINDOW_SIZE / 2));

  return dtStart;
};

DatePicker.prototype.getStartMonth = function () {
  var dtStart = new Date();

  dtStart.setTime(this.dtSelected.valueOf());
  dtStart.setMonth(dtStart.getMonth() - (DATE_PICKER_WINDOW_SIZE / 2));

  return dtStart;
};

DatePicker.prototype.getStartYear = function () {
  var dtStart = new Date();

  dtStart.setTime(this.dtSelected.valueOf());
  dtStart.setFullYear(dtStart.getFullYear() - (DATE_PICKER_WINDOW_SIZE / 2));

  return dtStart;
};

DatePicker.prototype.getPreviousSecond = function (dtDate) {
  var iSeconds = dtDate.getSeconds() - 1;

  if (iSeconds < 0) {
    dtDate.setSeconds(59);
    if (this.Mode == DATE_MODE_RELATIVE) dtDate = this.getPreviousMinute(dtDate);
  }
  else dtDate.setSeconds(iSeconds);

  return dtDate;
};

DatePicker.prototype.getNextSecond = function (dtDate) {
  var iSeconds = dtDate.getSeconds() + 1;

  if (iSeconds > 59) {
    dtDate.setSeconds(0);
    if (this.Mode == DATE_MODE_RELATIVE) dtDate = this.getNextMinute(dtDate);
  }
  else dtDate.setSeconds(iSeconds);

  return dtDate;
};

DatePicker.prototype.getPreviousMinute = function (dtDate) {
  var iMinutes = dtDate.getMinutes() - 1;

  if (iMinutes < 0) {
    dtDate.setMinutes(59);
    if (this.Mode == DATE_MODE_RELATIVE) dtDate = this.getPreviousHour(dtDate);
  }
  else dtDate.setMinutes(iMinutes);

  return dtDate;
};

DatePicker.prototype.getNextMinute = function (dtDate) {
  var iMinutes = dtDate.getMinutes() + 1;

  if (iMinutes > 59) {
    dtDate.setMinutes(0);
    if (this.Mode == DATE_MODE_RELATIVE) dtDate = this.getNextHour(dtDate);
  }
  else dtDate.setMinutes(iMinutes);

  return dtDate;
};

DatePicker.prototype.getPreviousHour = function (dtDate) {
  var iHours = dtDate.getHours() - 1;

  if (iHours < 0) {
    dtDate.setHours(23);
    if (this.Mode == DATE_MODE_RELATIVE) dtDate = this.getPreviousDay(dtDate);
  }
  else dtDate.setHours(iHours);

  return dtDate;
};

DatePicker.prototype.getNextHour = function (dtDate) {
  var iHours = dtDate.getHours() + 1;

  if (iHours > 23) {
    dtDate.setHours(0);
    if (this.Mode == DATE_MODE_RELATIVE) dtDate = this.getNextDay(dtDate);
  }
  else dtDate.setHours(iHours);

  return dtDate;
};

DatePicker.prototype.getPreviousDay = function (dtDate) {
  var iDay = dtDate.getDate() - 1;

  if (iDay < 1) {
    var iMonthDays = getMonthDays(dtDate);
    dtDate.setDate(iMonthDays);
    if (this.Mode == DATE_MODE_RELATIVE) dtDate = this.getPreviousMonth(dtDate);
  }
  else dtDate.setDate(iDay);

  return dtDate;
};

DatePicker.prototype.getNextDay = function (dtDate) {
  var iDay = dtDate.getDate() + 1;
  var iMonthDays = getMonthDays(dtDate);

  if (iDay > iMonthDays) {
    dtDate.setDate(1);
    if (this.Mode == DATE_MODE_RELATIVE) dtDate = this.getNextMonth(dtDate);
  }
  else dtDate.setDate(iDay);

  return dtDate;
};

DatePicker.prototype.getPreviousMonth = function (dtDate) {
  var iMonth = dtDate.getMonth() - 1;

  if (iMonth < 0) {
    dtDate.setMonth(11);
    if (this.Mode == DATE_MODE_RELATIVE) dtDate = this.getPreviousYear(dtDate);
  }
  else dtDate.setMonth(iMonth);

  return dtDate;
};

DatePicker.prototype.getNextMonth = function (dtDate) {
  var iMonth = dtDate.getMonth() + 1;

  if (iMonth > 11) {
    dtDate.setMonth(0);
    if (this.Mode == DATE_MODE_RELATIVE) dtDate = this.getNextYear(dtDate);
  }
  else dtDate.setMonth(iMonth);

  return dtDate;
};

DatePicker.prototype.getPreviousYear = function (dtDate) {
  dtDate.setFullYear(dtDate.getFullYear() - 1);
  return dtDate;
};

DatePicker.prototype.getNextYear = function (dtDate) {
  dtDate.setFullYear(dtDate.getFullYear() + 1);
  return dtDate;
};

DatePicker.prototype.setPrecision = function (Precision) {
  this.extLayer.removeClass(this.Precision);
  this.Precision = Precision;
  this.extLayer.addClass(this.Precision);
  this.extSetPrecision.dom.value = Precision;
};

DatePicker.prototype.setMode = function (Mode) {
  this.extLayer.removeClass(this.Mode);
  this.Mode = Mode;
  this.extLayer.addClass(this.Mode);
  this.extSetMode.dom.checked = (Mode == DATE_MODE_RELATIVE);
};

DatePicker.prototype.refreshSelectedDialog = function () {
  var extDateDialogSelected = this.extLayer.select(".date .dialog.selected").first();

  this.extSelectedSecond.dom.innerHTML = this.dtSelected.getSeconds() >= 10 ? this.dtSelected.getSeconds() : "0" + this.dtSelected.getSeconds();
  this.extSelectedMinute.dom.innerHTML = this.dtSelected.getMinutes() >= 10 ? this.dtSelected.getMinutes() : "0" + this.dtSelected.getMinutes();
  this.extSelectedHour.dom.innerHTML = this.dtSelected.getHours() >= 10 ? this.dtSelected.getHours() : "0" + this.dtSelected.getHours();
  this.extSelectedDay.dom.innerHTML = this.dtSelected.getDate();
  this.extSelectedWeekDay.dom.innerHTML = aDatePickerDays[this.Language][this.dtSelected.getDay()];
  this.extSelectedMonth.dom.innerHTML = aDatePickerMonths[this.Language][this.dtSelected.getMonth()];
  this.extSelectedYear.dom.innerHTML = this.dtSelected.getFullYear();

  extDateDialogSelected.removeClass("holiday");
  if (this.isHoliday(this.dtSelected)) extDateDialogSelected.addClass("holiday");

  extDateDialogSelected.removeClass("valid");
  if (this.isInvalid(this.dtSelected)) extDateDialogSelected.addClass("invalid");
};

DatePicker.prototype.refreshHoursDialog = function () {
  var extActive;
  var sHours = "";
  var dtStart = this.getStartHour();
  var extHoursDialog = this.extLayer.select(".dialog.hours .list").first();

  if (this.Mode == DATE_MODE_RELATIVE && this.Precision != DATE_PRECISION_HOURS && this.Precision != DATE_PRECISION_MINUTES && this.Precision != DATE_PRECISION_SECONDS) {
    extHoursDialog.dom.style.display = "none";
    return;
  }

  extHoursDialog.dom.style.display = "block";

  this.unregisterListBehaviours(extHoursDialog);
  for (var i = 0; i <= DATE_PICKER_WINDOW_SIZE; i++) {
    sInvalid = (this.isInvalid(dtStart)) ? " invalid" : "";
    sHours += "<li class='h" + this.getDateTimeStamp(dtStart, DATE_PRECISION_HOURS) + sInvalid + "'>" + this.getFormattedTime(dtStart, DATE_FORMAT_HOURS) + "</li>";
    dtStart = this.getNextHour(dtStart);
  }
  extHoursDialog.dom.innerHTML = sHours;
  this.registerListBehaviours(extHoursDialog);

  extActive = extHoursDialog.select(".h" + this.getDateTimeStamp(this.dtSelected, DATE_PRECISION_HOURS)).first();
  if (extActive != null) extActive.addClass(DATE_CLASS_ACTIVE);
};

DatePicker.prototype.refreshMinutesDialog = function () {
  var extActive;
  var sMinutes = "";
  var dtStart = this.getStartMinute();
  var extMinutesDialog = this.extLayer.select(".dialog.minutes .list").first();

  if (this.Mode == DATE_MODE_RELATIVE && this.Precision != DATE_PRECISION_MINUTES && this.Precision != DATE_PRECISION_SECONDS) {
    extMinutesDialog.dom.style.display = "none";
    return;
  }

  extMinutesDialog.dom.style.display = "block";

  this.unregisterListBehaviours(extMinutesDialog);
  for (var i = 0; i <= DATE_PICKER_WINDOW_SIZE; i++) {
    sInvalid = (this.isInvalid(dtStart)) ? " invalid" : "";
    sMinutes += "<li class='M" + this.getDateTimeStamp(dtStart, DATE_PRECISION_MINUTES) + sInvalid + "'>" + this.getFormattedTime(dtStart, DATE_FORMAT_MINUTES) + "</li>";
    dtStart = this.getNextMinute(dtStart);
  }
  extMinutesDialog.dom.innerHTML = sMinutes;
  this.registerListBehaviours(extMinutesDialog);

  extActive = extMinutesDialog.select(".M" + this.getDateTimeStamp(this.dtSelected, DATE_PRECISION_MINUTES)).first();
  if (extActive != null) extActive.addClass(DATE_CLASS_ACTIVE);
};

DatePicker.prototype.refreshSecondsDialog = function () {
  var extActive;
  var sSeconds = "";
  var dtStart = this.getStartSecond();
  var extSecondsDialog = this.extLayer.select(".dialog.seconds .list").first();

  if (this.Mode == DATE_MODE_RELATIVE && this.Precision != DATE_PRECISION_SECONDS) {
    extSecondsDialog.dom.style.display = "none";
    return;
  }

  extSecondsDialog.dom.style.display = "block";

  this.unregisterListBehaviours(extSecondsDialog);
  for (var i = 0; i <= DATE_PICKER_WINDOW_SIZE; i++) {
    sInvalid = (this.isInvalid(dtStart)) ? " invalid" : "";
    sSeconds += "<li class='s" + this.getDateTimeStamp(dtStart, DATE_PRECISION_SECONDS) + sInvalid + "'>" + this.getFormattedTime(dtStart, DATE_FORMAT_SECONDS) + "</li>";
    dtStart = this.getNextSecond(dtStart);
  }
  extSecondsDialog.dom.innerHTML = sSeconds;
  this.registerListBehaviours(extSecondsDialog);

  extActive = extSecondsDialog.select(".s" + this.getDateTimeStamp(this.dtSelected, DATE_PRECISION_SECONDS)).first();
  if (extActive != null) extActive.addClass(DATE_CLASS_ACTIVE);
};

DatePicker.prototype.refreshDaysDialog = function () {
  var extActive;
  var sDays = "";
  var dtStart = this.getStartDay();
  var extDaysDialog = this.extLayer.select(".dialog.days .list").first();

  if (this.Mode == DATE_MODE_RELATIVE && this.ActiveDateDialog != DIALOG_DAYS) {
    extDaysDialog.dom.style.display = "none";
    return;
  }

  extDaysDialog.dom.style.display = "block";

  this.unregisterListBehaviours(extDaysDialog);
  for (var i = 0; i <= DATE_PICKER_WINDOW_SIZE; i++) {
    sHolidays = (this.isHoliday(dtStart)) ? " holiday" : "";
    sInvalid = (this.isInvalid(dtStart)) ? " invalid" : "";
    sDays += "<li class='d" + this.getDateTimeStamp(dtStart, DATE_PRECISION_DAYS) + sHolidays + sInvalid + "'>" + this.getFormattedDate(dtStart, DATE_FORMAT_DAYS) + "</li>";
    dtStart = this.getNextDay(dtStart);
  }
  extDaysDialog.dom.innerHTML = sDays;
  this.registerListBehaviours(extDaysDialog);

  extActive = extDaysDialog.select(".d" + this.getDateTimeStamp(this.dtSelected, DATE_PRECISION_DAYS)).first();
  if (extActive != null) extActive.addClass(DATE_CLASS_ACTIVE);
};

DatePicker.prototype.refreshMonthsDialog = function () {
  var extActive;
  var sMonths = "";
  var dtStart = this.getStartMonth();
  var extMonthsList = this.extLayer.select(".dialog.months .list").first();

  if (this.Mode == DATE_MODE_RELATIVE && this.ActiveDateDialog != DIALOG_MONTHS) {
    extMonthsList.dom.style.display = "none";
    return;
  }

  extMonthsList.dom.style.display = "block";

  this.unregisterListBehaviours(extMonthsList);
  for (var i = 0; i <= DATE_PICKER_WINDOW_SIZE; i++) {
    sInvalid = (this.isInvalid(dtStart)) ? " invalid" : "";
    sMonths += "<li class='m" + this.getDateTimeStamp(dtStart, DATE_PRECISION_MONTHS) + sInvalid + "'>" + this.getFormattedDate(dtStart, DATE_FORMAT_MONTHS) + "</li>";
    dtStart = this.getNextMonth(dtStart);
  }
  extMonthsList.dom.innerHTML = sMonths;
  this.registerListBehaviours(extMonthsList);

  extActive = extMonthsList.select(".m" + this.getDateTimeStamp(this.dtSelected, DATE_PRECISION_MONTHS)).first();
  if (extActive != null) extActive.addClass(DATE_CLASS_ACTIVE);
};

DatePicker.prototype.refreshYearsDialog = function () {
  var extActive;
  var sYears = "";
  var dtStart = this.getStartYear();
  var extYearsDialog = this.extLayer.select(".dialog.years .list").first();

  if (this.Mode == DATE_MODE_RELATIVE && this.ActiveDateDialog != DIALOG_YEARS) {
    extYearsDialog.dom.style.display = "none";
    return;
  }

  extYearsDialog.dom.style.display = "block";

  this.unregisterListBehaviours(extYearsDialog);
  for (var i = 0; i <= DATE_PICKER_WINDOW_SIZE; i++) {
    sInvalid = (this.isInvalid(dtStart)) ? " invalid" : "";
    sYears += "<li class='y" + this.getDateTimeStamp(dtStart, DATE_PRECISION_YEARS) + sInvalid + "'>" + this.getFormattedDate(dtStart, DATE_FORMAT_YEARS) + "</li>";
    dtStart = this.getNextYear(dtStart);
  }
  extYearsDialog.dom.innerHTML = sYears;
  this.registerListBehaviours(extYearsDialog);

  extActive = extYearsDialog.select(".y" + this.getDateTimeStamp(this.dtSelected, DATE_PRECISION_YEARS)).first();
  if (extActive != null) extActive.addClass(DATE_CLASS_ACTIVE);
};

DatePicker.prototype.isVisible = function () {
  return this.extLayer.dom.style.display != "none";
};

DatePicker.prototype.show = function () {
  this.extLayer.dom.style.display = "block";
};

DatePicker.prototype.hide = function () {
  this.extLayer.dom.style.display = "none";
};

DatePicker.prototype.refresh = function () {
  this.ActiveDateDialog = DIALOG_DAYS;
  this.ActiveTimeDialog = DIALOG_SECONDS;

  if (this.dtSelected == null) this.dtSelected = new Date();

  if (this.Precision == DATE_PRECISION_MINUTES && this.ActiveTimeDialog == DIALOG_SECONDS) {
    this.ActiveTimeDialog = DIALOG_MINUTES;
  }
  else if (this.Precision == DATE_PRECISION_HOURS && this.ActiveTimeDialog != DIALOG_HOURS) {
    this.ActiveTimeDialog = DIALOG_HOURS;
  }

  if (this.Precision == DATE_PRECISION_MONTHS && this.ActiveDateDialog == DIALOG_DAYS) {
    this.ActiveDateDialog = DIALOG_MONTHS;
  }
  else if (this.Precision == DATE_PRECISION_YEARS && this.ActiveDateDialog != DIALOG_YEARS) {
    this.ActiveDateDialog = DIALOG_YEARS;
  }

  this.refreshSelectedDialog();
  this.refreshHoursDialog();
  this.refreshMinutesDialog();
  this.refreshSecondsDialog();
  this.refreshDaysDialog();
  this.refreshMonthsDialog();
  this.refreshYearsDialog();
};

DatePicker.prototype.getTimeoutMilliseconds = function () {

  if (!this.dtStartAnimation) return 300;

  var dtNow = new Date();
  var iLeft = dtNow.valueOf() - this.dtStartAnimation.valueOf();

  if (iLeft < 2000) return 300;
  if (iLeft < 4000) return 100;

  return 10;
};

DatePicker.prototype.decreaseDate = function () {

  if (this.ActiveDateDialog == DIALOG_DAYS) this.dtSelected = this.getPreviousDay(this.dtSelected);
  else if (this.ActiveDateDialog == DIALOG_MONTHS) this.dtSelected = this.getPreviousMonth(this.dtSelected);
  else if (this.ActiveDateDialog == DIALOG_YEARS) this.dtSelected = this.getPreviousYear(this.dtSelected);

  if (this.onChange) this.onChange(this);
  this.refresh();

  if (!this.dtStartAnimation) return;

  var iMilliseconds = this.getTimeoutMilliseconds();
  this.IdTimeout = window.setTimeout(DatePicker.prototype.decreaseDate.bind(this), iMilliseconds);
};

DatePicker.prototype.increaseDate = function () {

  if (this.ActiveDateDialog == DIALOG_DAYS) this.dtSelected = this.getNextDay(this.dtSelected);
  else if (this.ActiveDateDialog == DIALOG_MONTHS) this.dtSelected = this.getNextMonth(this.dtSelected);
  else if (this.ActiveDateDialog == DIALOG_YEARS) this.dtSelected = this.getNextYear(this.dtSelected);

  if (this.onChange) this.onChange(this);
  this.refresh();

  if (!this.dtStartAnimation) return;

  var iMilliseconds = this.getTimeoutMilliseconds();
  this.IdTimeout = window.setTimeout(DatePicker.prototype.increaseDate.bind(this), iMilliseconds);
};

DatePicker.prototype.decreaseTime = function () {

  if (this.ActiveTimeDialog == DIALOG_HOURS) this.dtSelected = this.getPreviousHour(this.dtSelected);
  else if (this.ActiveTimeDialog == DIALOG_MINUTES) this.dtSelected = this.getPreviousMinute(this.dtSelected);
  else if (this.ActiveTimeDialog == DIALOG_SECONDS) this.dtSelected = this.getPreviousSecond(this.dtSelected);

  if (this.onChange) this.onChange(this);
  this.refresh();

  if (!this.dtStartAnimation) return;

  var iMilliseconds = this.getTimeoutMilliseconds();
  this.IdTimeout = window.setTimeout(DatePicker.prototype.decreaseTime.bind(this), iMilliseconds);
};

DatePicker.prototype.increaseTime = function () {

  if (this.ActiveTimeDialog == DIALOG_HOURS) this.dtSelected = this.getNextHour(this.dtSelected);
  else if (this.ActiveTimeDialog == DIALOG_MINUTES) this.dtSelected = this.getNextMinute(this.dtSelected);
  else if (this.ActiveTimeDialog == DIALOG_SECONDS) this.dtSelected = this.getNextSecond(this.dtSelected);

  if (this.onChange) this.onChange(this);
  this.refresh();

  if (!this.dtStartAnimation) return;

  var iMilliseconds = this.getTimeoutMilliseconds();
  this.IdTimeout = window.setTimeout(DatePicker.prototype.increaseTime.bind(this), iMilliseconds);
};

// ---------------------------------------------------------------------------------
DatePicker.prototype.atDialogListItemClick = function (EventLaunched, DOMItem) {
  var sClassName = DOMItem.className.replace("active", "").replace("holiday", "").replace("invalid", "");
  this.updateDateFromId(this.dtSelected, sClassName);
  this.refresh();
};

DatePicker.prototype.atButtonClick = function (EventLaunched, DOMButton) {
  var extButton = Ext.get(DOMButton);
  var extParent = extButton.up("td");

  window.clearTimeout(this.IdTimeout);

  if (extParent.hasClass("daycell")) this.ActiveDateDialog = DIALOG_DAYS;
  else if (extParent.hasClass("monthcell")) this.ActiveDateDialog = DIALOG_MONTHS;
  else if (extParent.hasClass("yearcell")) this.ActiveDateDialog = DIALOG_YEARS;
  else if (extParent.hasClass("hourcell")) this.ActiveTimeDialog = DIALOG_HOURS;
  else if (extParent.hasClass("minutecell")) this.ActiveTimeDialog = DIALOG_MINUTES;
  else if (extParent.hasClass("secondcell")) this.ActiveTimeDialog = DIALOG_SECONDS;

  if (extButton.hasClass("up")) {
    if (extParent.hasClass("daycell") || extParent.hasClass("monthcell") || extParent.hasClass("yearcell"))
      this.decreaseDate();
    else
      this.decreaseTime();
  }
  else {
    if (extParent.hasClass("daycell") || extParent.hasClass("monthcell") || extParent.hasClass("yearcell"))
      this.increaseDate();
    else
      this.increaseTime();
  }

};

DatePicker.prototype.atSelectedHourClick = function () {

  if (this.onChange) this.onChange(this);

  if (this.extSelectedHourCell.hasClass(DATE_CLASS_ACTIVE)) {
    this.extSelectedHourCell.removeClass(DATE_CLASS_ACTIVE);
    this.ActiveTimeDialog = DIALOG_SECONDS;
  }
  else {
    this.ActiveTimeDialog = DIALOG_HOURS;
    this.extSelectedHourCell.addClass(DATE_CLASS_ACTIVE);
  }

  this.extSelectedMinuteCell.removeClass(DATE_CLASS_ACTIVE);
  this.extSelectedSecondCell.removeClass(DATE_CLASS_ACTIVE);

  this.refresh();
};

DatePicker.prototype.atSelectedMinuteClick = function () {

  if (this.onChange) this.onChange(this);

  if (this.extSelectedMinuteCell.hasClass(DATE_CLASS_ACTIVE)) {
    this.extSelectedMinuteCell.removeClass(DATE_CLASS_ACTIVE);
    this.ActiveTimeDialog = DIALOG_SECONDS;
  }
  else {
    this.ActiveTimeDialog = DIALOG_MINUTES;
    this.extSelectedMinuteCell.addClass(DATE_CLASS_ACTIVE);
  }

  this.extSelectedHourCell.removeClass(DATE_CLASS_ACTIVE);
  this.extSelectedSecondCell.removeClass(DATE_CLASS_ACTIVE);

  this.refresh();
};

DatePicker.prototype.atSelectedSecondClick = function () {

  if (this.onChange) this.onChange(this);

  if (this.extSelectedSecondCell.hasClass(DATE_CLASS_ACTIVE)) this.extSelectedSecondCell.removeClass(DATE_CLASS_ACTIVE);
  else this.extSelectedSecondCell.addClass(DATE_CLASS_ACTIVE);

  this.ActiveTimeDialog = DIALOG_SECONDS;

  this.extSelectedHourCell.removeClass(DATE_CLASS_ACTIVE);
  this.extSelectedMinuteCell.removeClass(DATE_CLASS_ACTIVE);

  this.refresh();
};

DatePicker.prototype.atSelectedDayClick = function () {
  if (this.onChange) this.onChange(this);
};

DatePicker.prototype.atSelectedDayOver = function () {
  if (this.Mode == DATE_MODE_RELATIVE) return;

  this.extSelectedDayCell.removeClass(DATE_CLASS_INACTIVE);
  this.extSelectedMonthCell.removeClass(DATE_CLASS_ACTIVE);
  this.extSelectedMonthCell.removeClass(DATE_CLASS_INACTIVE);
  this.extSelectedYearCell.removeClass(DATE_CLASS_ACTIVE);
  this.extSelectedYearCell.removeClass(DATE_CLASS_INACTIVE);

  if (this.Mode == DATE_MODE_ABSOLUTE) {
    if (this.extSelectedDayCell.hasClass(DATE_CLASS_ACTIVE)) this.extSelectedDayCell.removeClass(DATE_CLASS_ACTIVE);
    else this.extSelectedDayCell.addClass(DATE_CLASS_ACTIVE);

    this.ActiveDateDialog = DIALOG_DAYS;
  }
  else {
    this.ActiveDateDialog = DIALOG_DAYS;
    this.extSelectedDayCell.addClass(DATE_CLASS_ACTIVE);
    this.extSelectedMonthCell.addClass(DATE_CLASS_ACTIVE);
    this.extSelectedYearCell.addClass(DATE_CLASS_ACTIVE);
  }

  this.refresh();
};

DatePicker.prototype.atSelectedMonthClick = function () {
  if (this.onChange) this.onChange(this);
};

DatePicker.prototype.atSelectedMonthOver = function () {
  if (this.Mode == DATE_MODE_RELATIVE) return;

  this.extSelectedDayCell.removeClass(DATE_CLASS_ACTIVE);
  this.extSelectedDayCell.removeClass(DATE_CLASS_INACTIVE);
  this.extSelectedMonthCell.removeClass(DATE_CLASS_INACTIVE);
  this.extSelectedYearCell.removeClass(DATE_CLASS_ACTIVE);
  this.extSelectedYearCell.removeClass(DATE_CLASS_INACTIVE);

  if (this.Mode == DATE_MODE_ABSOLUTE) {
    if (this.extSelectedMonthCell.hasClass(DATE_CLASS_ACTIVE)) {
      this.extSelectedMonthCell.removeClass(DATE_CLASS_ACTIVE);
      this.ActiveDateDialog = DIALOG_DAYS;
    }
    else {
      this.ActiveDateDialog = DIALOG_MONTHS;
      this.extSelectedMonthCell.addClass(DATE_CLASS_ACTIVE);
    }
  }
  else {
    this.ActiveDateDialog = DIALOG_MONTHS;
    this.extSelectedDayCell.addClass(DATE_CLASS_INACTIVE);
    this.extSelectedMonthCell.addClass(DATE_CLASS_ACTIVE);
    this.extSelectedYearCell.addClass(DATE_CLASS_ACTIVE);
  }

  this.refresh();
};

DatePicker.prototype.atSelectedYearClick = function () {
  if (this.onChange) this.onChange(this);
};

DatePicker.prototype.atSelectedYearOver = function () {
  if (this.Mode == DATE_MODE_RELATIVE) return;

  this.extSelectedDayCell.removeClass(DATE_CLASS_ACTIVE);
  this.extSelectedDayCell.removeClass(DATE_CLASS_INACTIVE);
  this.extSelectedMonthCell.removeClass(DATE_CLASS_ACTIVE);
  this.extSelectedMonthCell.removeClass(DATE_CLASS_INACTIVE);
  this.extSelectedYearCell.removeClass(DATE_CLASS_INACTIVE);

  if (this.Mode == DATE_MODE_ABSOLUTE) {
    if (this.extSelectedYearCell.hasClass(DATE_CLASS_ACTIVE)) {
      this.extSelectedYearCell.removeClass(DATE_CLASS_ACTIVE);
      this.ActiveDateDialog = DIALOG_DAYS;
    }
    else {
      this.ActiveDateDialog = DIALOG_YEARS;
      this.extSelectedYearCell.addClass(DATE_CLASS_ACTIVE);
    }
  }
  else {
    this.ActiveDateDialog = DIALOG_YEARS;
    this.extSelectedDayCell.addClass(DATE_CLASS_INACTIVE);
    this.extSelectedMonthCell.addClass(DATE_CLASS_INACTIVE);
    this.extSelectedYearCell.addClass(DATE_CLASS_ACTIVE);
  }

  this.refresh();
};

DatePicker.prototype.atMonthsDialogClick = function (Event) {
  if (!Event.xy || Event.xy[0] >= 85) return;

  this.ActiveDateDialog = DIALOG_DAYS;
  this.refresh();
};

DatePicker.prototype.atYearsDialogClick = function (Event) {
  var iMaxThreshold = 230;

  if (this.Precision == DATE_PRECISION_MONTHS) iMaxThreshold = 210;

  if (!Event.xy || Event.xy[0] >= iMaxThreshold) return;

  if (Event.xy[0] < 85) this.ActiveDateDialog = DIALOG_DAYS;
  else this.ActiveDateDialog = DIALOG_MONTHS;

  this.refresh();
};

DatePicker.prototype.atSetModeChange = function (Event) {
  this.setMode(this.extSetMode.dom.checked ? DATE_MODE_RELATIVE : DATE_MODE_ABSOLUTE);
  this.refresh();
};

DatePicker.prototype.atSetPrecisionChange = function (Event) {
  this.setPrecision(this.extSetPrecision.dom.value);
  this.refresh();
};