//--------------------------------------------------------------------------------
// Time class
//--------------------------------------------------------------------------------

function Time(milliseconds) {
  this.milliseconds = milliseconds;
  this.partialTime = 0;
}

Time.prototype.format = function() {
  this.partialTime = this.milliseconds / 1000;
  var seconds = Math.round(this.partialTime % 60);
  
  this.partialTime = this.partialTime / 60;
  var minutes = Math.round(this.partialTime % 60);
  
  this.partialTime = this.partialTime / 60;
  var hours = Math.round(this.partialTime % 24);  

  var tuple = {};
  tuple.hours = hours;
  tuple.minutes = minutes;
  tuple.seconds = seconds;
  
  return tuple;    
};