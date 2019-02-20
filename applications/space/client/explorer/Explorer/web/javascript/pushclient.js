var isIE = (function () {
    var doc = document, cache = {}, elem;
    return function (version, comparison) {
        if (/*@cc_on!@*/true) {
            return false;
        }
        var key = [comparison || "", "IE", version || ""].join(" ");
        if (cache[key] === undefined) {
            elem = elem || doc.createElement("B");
            elem.innerHTML = "<!--[if " + key + "]><b></b><![endif]-->";
            cache[key] = !!elem.getElementsByTagName("b").length;
        }
        return cache[key];
    };
})();

function PushClient(instanceId, pushServerUrl) {
    this.index = 0;
    this.instanceId = instanceId;
    this.pushServerUrl = pushServerUrl;
    this.frame = null;
    this.listeners = [];
}

PushClient.DELIMITER = "/--push--/";
PushClient.END_DELIMITER = "/--end--/";
PushClient.ON_FAILED_RETRY_TIME = 5000;

PushClient.prototype.init = function () {
    if (isIE()) {
        this.createFrame();
        window.setTimeout(this.checkChannel.bind(this), 500);
    }
    else
        window.setTimeout(this.poll.bind(this), 500);
};

PushClient.prototype.addListener = function (listener) {
    this.listeners.push(listener);
};

PushClient.prototype.createFrame = function () {
    this.frame = document.createElement("IFRAME");
    this.frame.onLoad = this.onFrameLoad.bind(this);
    document.body.appendChild(this.frame);
};

PushClient.prototype.poll = function () {
    new Ajax.Request(this.pushServerUrl, {
        method: 'POST',
        parameters: "i=" + this.instanceId + "&t=" + Math.random(),
        asynchronous: true,
        onComplete: this.onComplete.bind(this),
        onInteractive: this.onPartial.bind(this)
    });
    this.channelOpen = true;
};

PushClient.prototype.doProcess = function (xmlHttpRequest) {
    var i;
    while ((i = xmlHttpRequest.responseText.indexOf(PushClient.DELIMITER, this.index)) >= this.index) {

        var j = xmlHttpRequest.responseText.indexOf(PushClient.END_DELIMITER, i);
        if (j < i) return;

        var currentMsgStart = i + PushClient.DELIMITER.length;
        var newMsg = xmlHttpRequest.responseText.substr(currentMsgStart, j - currentMsgStart);

        this.index = j;

        try {
            this.onPushReceived(newMsg);
        } catch (err) {
            alert(err);
        }
    }
};

PushClient.prototype.onPartial = function (xmlHttpRequest, responseHeader) {
    this.doProcess(xmlHttpRequest);
};

PushClient.prototype.onComplete = function (xmlHttpRequest, responseHeader) {
    if (xmlHttpRequest.status >= 200 && xmlHttpRequest.status < 400) {
        if (xmlHttpRequest.responseText.indexOf("ERR_USER_NOT_LOGGED") > -1) {
            window.location = location;
            return;
        }
        this.doProcess(xmlHttpRequest);
        this.index = 0;
        window.setTimeout(this.poll.bind(this), 0);
    } else {
        window.setTimeout(this.poll.bind(this), this.ON_FAILED_RETRY_TIME);
    }
    this.channelOpen = false;
};

PushClient.prototype.onPushReceived = function (pushMessage) {
    this.listeners.forEach(function (listener) {
        listener.onMessageReceived(pushMessage);
    });
};

PushClient.prototype.onFrameLoad = function (event) {
    this.channelRefreshStarted();
};

PushClient.prototype.checkChannel = function () {
    if (this.channelOpen) return;
    window.setTimeout(this.checkChannel.bind(this), PushClient.ON_FAILED_RETRY_TIME);
    this.frame.src = this.pushServerUrl + "?c=1&i=" + this.instanceId + "&t=" + Math.random();
};

PushClient.prototype.channelRefreshStarted = function () {
    this.channelOpen = false;
    this.checkChannel();
};

PushClient.prototype.isChannelOpen = function () {
    return this.channelOpen;
};