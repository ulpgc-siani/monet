function Scale() {
}

Scale.YEAR = 0;
Scale.MONTH = 1;
Scale.DAY = 2;
Scale.HOUR = 3;
Scale.MINUTE = 4;
Scale.SECOND = 5;

Scale.fromString = function(scale) {
  if (scale == "year") return Scale.YEAR;
  else if (scale == "month") return Scale.MONTH;
  else if (scale == "day") return Scale.DAY;
  else if (scale == "hour") return Scale.HOUR;
  else if (scale == "minute") return Scale.MINUTE;
  else if (scale == "second") return Scale.SECOND;
  return -1;
};

Scale.toString = function(scale) {
  if (scale == Scale.YEAR) return "year";
  else if (scale == Scale.MONTH) return "month";
  else if (scale == Scale.DAY) return "day";
  else if (scale == Scale.HOUR) return "hour";
  else if (scale == Scale.MINUTE) return "minute";
  else if (scale == Scale.SECOND) return "second";
  return "";
};