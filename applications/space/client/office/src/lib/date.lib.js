//--------------------------------------------------------------------------------

function parseServerDate(date) {
  return Date.parseDate(date, SERVER_DATE_FORMAT);
};

//--------------------------------------------------------------------------------

function toServerDate(date) {
  if (date == null) return "";
  return date.format(SERVER_DATE_FORMAT);
};

//--------------------------------------------------------------------------------

function getFormattedDate(date, Language, useFullDate, useWeekDay) {

  if (date == null) return "";

  var currentDate = new Date();
  var isCurrentYear = (date.getFullYear() == currentDate.getFullYear());
  var isCurrentMonth = ((date.getMonth() == currentDate.getMonth()) && (isCurrentYear));
  var isToday = ((date.getDate() == currentDate.getDate()) && (isCurrentMonth));
  var isYesterday = ((date.getDate() == (currentDate.getDate() - 1)) && (isCurrentMonth));
  var format;

  Date.dayNames = eval("aDays." + Language);
  Date.monthNames = eval("aMonths." + Language);

  if ((isToday) && (!useFullDate)) return (Lang && Lang.Dates && Lang.Dates.Today) ? Lang.Dates.Today : "Today";
  if ((isYesterday) && (!useFullDate)) return (Lang && Lang.Dates && Lang.Dates.Yesterday) ? Lang.Dates.Yesterday : "Yesterday";

  if (useWeekDay && useFullDate) format = "l, ";
  else format = "";

  switch (Language) {
    case "es" :
      if ((isCurrentYear) && (!useFullDate)) format += "j \\d\\e F";
      else format += "j \\d\\e F \\d\\e Y";
      break;
    case "en" :
      if ((isCurrentYear) && (!useFullDate)) format += "d F";
      else format += "F d, Y";
      break;
    default:
      if ((isCurrentYear) && (!useFullDate)) format += "d F";
      else format += "F d, Y";
  }

  return date.format(format);
};

function getFormattedDateTime(date, Language, useFullDate, useWeekDay, hideArticle) {

  if (date == null) return "";

  var currentDate = new Date();
  var isCurrentYear = (date.getFullYear() == currentDate.getFullYear());
  var isCurrentMonth = ((date.getMonth() == currentDate.getMonth()) && (isCurrentYear));
  var isToday = ((date.getDate() == currentDate.getDate()) && (isCurrentMonth));
  var isYesterday = ((date.getDate() == (currentDate.getDate() - 1)) && (isCurrentMonth));
  var format;

  Date.dayNames = eval("aDays." + Language);
  Date.monthNames = eval("aMonths." + Language);

  if ((isToday) && (!useFullDate)) return Lang.Dates.Today + " " + Lang.Dates.At + " " + date.format("H:i:s");
  if ((isYesterday) && (!useFullDate)) return Lang.Dates.Yesterday + " " + Lang.Dates.At + " " + date.format("H:i:s");

  if (useWeekDay && useFullDate) format = "l, ";
  else format = "";

  switch (Language) {
    case "es" :
      if ((isCurrentYear) && (!useFullDate)) format += (!hideArticle ? "\\e\\l " : "") + "j \\d\\e F \\a \\l\\a\\s H:i:s";
      else format += (!hideArticle ? "\\e\\l " : "") + "j \\d\\e F \\d\\e Y \\a \\l\\a\\s H:i:s";
      break;
    case "en" :
      if ((isCurrentYear) && (!useFullDate)) format += (!hideArticle ? "\\o\\n " : "") + "d F \\a\\t H:i:s";
      else format += (!hideArticle ? "\\o\\n " : "") + "F d, Y \\a\\t H:i:s";
      break;
    default:
      if ((isCurrentYear) && (!useFullDate)) format += "d F \\a\\t H:i:s";
      else format += (!hideArticle ? "\\o\\n " : "") + "F d, Y \\a\\t H:i:s";
  }

  return date.format(format);
};

//--------------------------------------------------------------------------------

function getMonthDays(date) {
  var iYear = date.getFullYear();
  var iMonth = date.getMonth();
  if (!Date.monthDays) {
    Date.monthDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
  }

  if (Date.isLeapYear(iYear)) Date.monthDays[1] = 29;
  else Date.monthDays[1] = 28;

  return Date.monthDays[iMonth];
};

//--------------------------------------------------------------------------------

Date.isLeapYear = function (iYear) {
  return ((iYear % 4 == 0) && (iYear % 100 || !(iYear % 400))) ? true : false;
};

Date.getPattern = function (Type) {
  var lowerType = Type.toLowerCase();
  return (this.patterns[lowerType]) ? Date.patterns[lowerType] : Type;
};

Date.getCurrentTimeZone = function() {
  return new Date().getTimezoneOffset()/60;
}