function Resolution() {
}

Resolution.YEARS = 0;
Resolution.MONTHS = 1;
Resolution.DAYS = 2;
Resolution.HOURS = 3;
Resolution.MINUTES = 4;
Resolution.SECONDS = 5;

Resolution.fromString = function(resolution) {
  if (resolution == "years") return Resolution.YEARS;
  else if (resolution == "months") return Resolution.MONTHS;
  else if (resolution == "days") return Resolution.DAYS;
  else if (resolution == "hours") return Resolution.HOURS;
  else if (resolution == "minutes") return Resolution.MINUTES;
  else if (resolution == "seconds") return Resolution.SECONDS;
  return -1;
};

Resolution.toString = function(resolution) {
  if (resolution == Resolution.YEAR) return "years";
  else if (resolution == Resolution.MONTH) return "months";
  else if (resolution == Resolution.DAY) return "days";
  else if (resolution == Resolution.HOUR) return "hours";
  else if (resolution == Resolution.MINUTE) return "minutes";
  else if (resolution == Resolution.SECOND) return "seconds";
  return "";
};