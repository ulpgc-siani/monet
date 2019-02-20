TimeLapse = new Object();

TimeLapse.getBounds = function(timeLapse, range) {
  var scale = timeLapse.scale;
  
  if (scale == Scale.YEAR) factor = YEAR;
  else if (scale == Scale.MONTH) factor = MONTH;
  else if (scale == Scale.DAY) factor = DAY;
  else if (scale == Scale.HOUR) factor = HOUR;
  else if (scale == Scale.MINUTE) factor = MINUTE;
  else if (scale == Scale.SECOND) factor = SECOND;
  
  return { min: range.min-factor, max: range.max+factor }; 
};

TimeLapse.getWindowSizeMinValue = function(bounds, scale, factor) {
  var result = Math.round((bounds.max-bounds.min)/scale);
  result = (result<factor)?result:factor;
  result = result>0?result:10;
  return result;
};

TimeLapse.getWindowSizeMilliseconds = function(milliseconds, maxMilliseconds, freeSize) {
  
  if (milliseconds == null || (!freeSize && milliseconds > maxMilliseconds))
    return Math.round(maxMilliseconds/2);
  
  return milliseconds;
};

TimeLapse.getWindowSize = function(timeLapse, bounds, freeSize) {
  var windowSize = new Object();
  var scale = timeLapse.scale;
  var milliseconds = null;

  
  if (scale == Scale.YEAR) {
    var range = { min: 1, max: 50};
    var minYears = this.getWindowSizeMinValue(bounds, YEAR, range.min);
    windowSize = { min: { years: minYears } };
    if (!freeSize) windowSize.max = { years: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*YEAR, freeSize);
  }
  else if (scale == Scale.MONTH) {
    var range = { min: 1, max: 60};
    var minMonths = this.getWindowSizeMinValue(bounds, MONTH, range.min);
    windowSize = { min: { months: minMonths } };
    if (!freeSize) windowSize.max = { months: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*MONTH, freeSize);
  }
  else if (scale == Scale.DAY) {
    var range = { min: 1, max: 150};
    var minDays = this.getWindowSizeMinValue(bounds, DAY, range.min);
    windowSize = { min: { days: minDays } };
    if (!freeSize) windowSize.max = { days: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*DAY, freeSize);
  }
  else if (scale == Scale.HOUR) {
    var range = { min: 1, max: 120};
    var minHours = this.getWindowSizeMinValue(bounds, HOUR, range.min);
    windowSize = { min: { hours: minHours } };
    if (!freeSize) windowSize.max = { hours: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*HOUR, freeSize);
  }
  else if (scale == Scale.MINUTE) {
    var range = { min: 1, max: 300};
    var minMinutes = this.getWindowSizeMinValue(bounds, MINUTE, range.min);
    windowSize = { min: { minutes: minMinutes } };
    if (!freeSize) windowSize.max = { minutes: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*MINUTE, freeSize);
  }
  else if (scale == Scale.SECOND) {
    var range = { min: 1, max: 400};
    var minSeconds = this.getWindowSizeMinValue(bounds, SECOND, range.min);
    windowSize = { min: { seconds: minSeconds } };
    if (!freeSize) windowSize.max = { seconds: range.max };
    windowSize.milliseconds = this.getWindowSizeMilliseconds(milliseconds, range.max*SECOND, freeSize);
  }

  return windowSize;
};

TimeLapse.getValues = function(timeLapse, windowSize, bounds) {
  var valueMin = timeLapse.from!=null?timeLapse.from:(bounds.max-windowSize.milliseconds);
  var valueMax = timeLapse.to!=null?timeLapse.to/*timeLapse.from+windowSize.milliseconds*/:bounds.max;
  
  if (valueMin < bounds.min) {
    valueMin = bounds.max-windowSize.milliseconds;
    valueMax = bounds.max;
  }
  
  if (valueMax > bounds.max) {
    valueMax = bounds.max;
    valueMin = valueMax-windowSize.milliseconds;
  }

  if (valueMin < bounds.min)
    valueMin = bounds.min;

  return { min: valueMin, max: valueMax };
};

TimeLapse.toScale = function(timeLapse, scale) {
  
  if (timeLapse.from == null)
    return timeLapse;
  
  var fromDate = new Date(timeLapse.from);
  var toDate = new Date(timeLapse.to);
  var isSameYear = (fromDate.getFullYear() == toDate.getFullYear());
  var isSameMonth = isSameYear && (fromDate.getMonth() == toDate.getMonth());
  var isSameDate = isSameYear && isSameMonth && (fromDate.getDate() == toDate.getDate());
  var isSameHour = isSameYear && isSameMonth && isSameDate && (fromDate.getHours() == toDate.getHours());
  var isSameMinute = isSameYear && isSameMonth && isSameDate && isSameHour && (fromDate.getMinutes() == toDate.getMinutes());
  var isSameSecond = isSameYear && isSameMonth && isSameDate && isSameHour && isSameMinute && (fromDate.getSeconds() == toDate.getSeconds());
  var factor = 0;
 
  if (scale == Scale.YEAR && isSameYear) factor = YEAR;
  else if (scale == Scale.MONTH && isSameMonth) factor = MONTH;
  else if (scale == Scale.DAY && isSameDate) factor = DAY;
  else if (scale == Scale.HOUR && isSameHour) factor = HOUR;
  else if (scale == Scale.MINUTE && isSameMinute) factor = MINUTE;
  else if (scale == Scale.SECOND && isSameSecond) factor = SECOND;
  
  return { from: timeLapse.from, to: timeLapse.to+factor, scale: scale };
}