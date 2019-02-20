var PushClient = {
  DELIMITER: "/--push--/",
  END_DELIMITER: "/--end--/",
  ON_FAILED_RETRY_TIME: 5000,

  index: 0,

  init: function (instanceId) {
    if (Context.Config.PushEnabled == "false") return;

    this.instanceId = instanceId;
    if (Ext.isIE) {
      $('pushClientHolder').innerHTML = "<iframe id='pushClient'></iframe>";
      $('pushClient').onload = this.onFrameLoad.bind(this);
      window.setTimeout(this.checkChannel.bind(this), 500);
    }
    else
      window.setTimeout(this.poll.bind(this), 500);
  },

  poll: function () {
    this.ajaxRequest = new Ajax.Request(Context.Config.Push, {
      method: 'POST',
      parameters: "i=" + this.instanceId + "&t=" + Math.random(),
      asynchronous: true,
      onComplete: this.onComplete.bind(this),
      onInteractive: this.onPartial.bind(this)
    });
    this.channelOpen = true;
  },

  doProcess: function (xmlHttpRequest) {
    var i;
    while ((i = xmlHttpRequest.responseText.indexOf(PushClient.DELIMITER, PushClient.index)) >= PushClient.index) {

      var j = xmlHttpRequest.responseText.indexOf(PushClient.END_DELIMITER, i);
      if (j < i) return;

      var currentMsgStart = i + PushClient.DELIMITER.length;
      var newMsg = xmlHttpRequest.responseText.substr(currentMsgStart, j - currentMsgStart);

      PushClient.index = j;

      try {
        this.onPushReceived(Ext.util.JSON.decode(newMsg));
      } catch (err) {
        alert(err);
      }
    }
  },

  onPartial: function (xmlHttpRequest, responseHeader) {
    this.doProcess(xmlHttpRequest);
  },

  onComplete: function (xmlHttpRequest, responseHeader) {
    if (xmlHttpRequest.status >= 200 && xmlHttpRequest.status < 400) {
      if (xmlHttpRequest.responseText.indexOf("ERR_USER_NOT_LOGGED") > -1) {
        window.location = location;
        return;
      }
      this.doProcess(xmlHttpRequest);
      PushClient.index = 0;
      window.setTimeout(this.poll.bind(this), 0);
    } else {
      PushClient.index = 0;
      window.setTimeout(this.poll.bind(this), this.ON_FAILED_RETRY_TIME);
    }
    this.channelOpen = false;
  },

  onPushReceived: function (pushMsg) {
    CommandDispatcher.execute(pushMsg.op, null, pushMsg.data);
  },

  onFrameLoad: function (evt) {
    this.channelRefreshStarted();
  },

  checkChannel: function () {
    if (!this.channelOpen) {
      window.setTimeout(this.checkChannel.bind(this), this.ON_FAILED_RETRY_TIME);
      $('pushClient').src = Context.Config.Push + "?c=1&i=" + this.instanceId + "&t=" + Math.random();
    }
  },

  channelRefreshStarted: function () {
    this.channelOpen = false;
    this.checkChannel();
  },

  isChannelOpen: function () {
    return this.channelOpen;
  }
};

function goPush(newNotification) {
  PushClient.onPushReceived(newNotification);
};

function openChannel() {
  PushClient.channelOpen = true;
};