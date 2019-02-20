ActivityManager = {
  currentActivity : null,
};

//----------------------------------------------------------
ActivityManager.start = function(activity, data) {    
  
  if (! ActivityManager.currentActivity) ActivityManager.doStart(activity, data);
  
  else {	  
    ActivityManager.currentActivity.canStop({
      context: ActivityManager,
    
      success: function() {      
        ActivityManager.currentActivity.stop();      
        ActivityManager.doStart(activity, data);
      },

      failure : function() {
    	alert("Activity was not stopped");
      }   
    });   
  }
};

//----------------------------------------------------------
ActivityManager.doStart = function(activity, data) {
  ActivityManager.currentActivity = activity;
  activity.start(data);  	
};