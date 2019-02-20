
var NotificationManager = {
  id : '',
   
  showNotification : function(message, time) {
    this.extNotificationBar.dom.innerHTML = message;
    $(this.extNotificationBar.dom).show();
    
    if (time) {           
      var context = this;
      setTimeout(function() { context.hideNotification();}, time);       
    }
  },
  
  hideNotification : function() {
    this.extNotificationBar.dom.innerHTML = "";
    $(this.extNotificationBar.dom).hide();
  },
 
  init : function(id) {
    this.extNotificationBar = Ext.get(id);  
  }  
};
