(function (window) {

    function _registerEvent(target, eventType, cb) {
        if (target.addEventListener) {
            target.addEventListener(eventType, cb);
            return {
                remove: function () {
                    target.removeEventListener(eventType, cb);
                }
            };
        } else {
            target.attachEvent(eventType, cb);
            return {
                remove: function () {
                    target.detachEvent(eventType, cb);
                }
            };
        }
    }

    function _createHiddenIframe(target, uri) {
        var iframe = document.createElement("iframe");
        iframe.src = uri;
        iframe.id = "hiddenIframe";
        iframe.style.display = "none";
        target.appendChild(iframe);
        return iframe;
    }

    function openUriWithHiddenFrame(uri, failCb) {

        var timeout = setTimeout(function () {
            failCb();
            handler.remove();
        }, 1000);

        var iframe = document.querySelector("#hiddenIframe");
        if (!iframe) {
            iframe = _createHiddenIframe(document.body, "about:blank");
        }

        var handler = _registerEvent(window, "blur", onBlur);

        function onBlur() {
            clearTimeout(timeout);
            handler.remove();
        }

        iframe.contentWindow.location.href = uri;
    }

    function openUriWithTimeoutHack(uri, failCb) {
        var windowInstance = window.parent != null ? window.parent : window;

        var timeout = setTimeout(function () {
            failCb();
            handler.remove();
        }, isLinux() ? 5000 : 1000);

        var handler = _registerEvent(windowInstance, "blur", onBlur);

        function onBlur() {
            clearTimeout(timeout);
            handler.remove();
        }

        function isLinux() {
            return navigator.appVersion.indexOf("Linux") != -1;
        }

        windowInstance.location = uri;
    }

    function openUriUsingFirefox(uri, failCb) {
        var iframe = document.querySelector("#hiddenIframe");
        if (!iframe) {
            iframe = _createHiddenIframe(document.body, "about:blank");
        }
        try {
            iframe.contentWindow.location.href = uri;
        } catch (e) {
            if (e.name == "NS_ERROR_UNKNOWN_PROTOCOL") {
                failCb();
            }
        }
    }

    function openUriUsingIE(uri, failCb) {
        //check if OS is Win 8 or 8.1
        var ua = navigator.userAgent.toLowerCase();
        var isWin8 = /windows nt 6.2/.test(ua) || /windows nt 6.3/.test(ua);

        if (isWin8) {
            openUriUsingIEInWindows8(uri, failCb);
        } else {
            if (getInternetExplorerVersion() === 10) {
                openUriUsingIE10InWindows7(uri, failCb);
            } else if (getInternetExplorerVersion() === 9 || getInternetExplorerVersion() === 11) {
                openUriWithHiddenFrame(uri, failCb);
            } else {
                openUriInNewWindowHack(uri, failCb);
            }
        }
    }

    function openUriUsingIE10InWindows7(uri, failCb) {
        var timeout = setTimeout(failCb, 1000);
        window.addEventListener("blur", function () {
            clearTimeout(timeout);
        });

        var iframe = document.querySelector("#hiddenIframe");
        if (!iframe) {
            iframe = _createHiddenIframe(document.body, "about:blank");
        }
        try {
            iframe.contentWindow.location.href = uri;
        } catch (e) {
            failCb();
            clearTimeout(timeout);
        }
    }

    function openUriInNewWindowHack(uri, failCb) {
        var myWindow = window.open('', '', 'width=0,height=0');

        myWindow.document.write("<iframe src='" + uri + "'></iframe>");
        setTimeout(function () {
            try {
                myWindow.location.href;
                myWindow.setTimeout("window.close()", 1000);
            } catch (e) {
                myWindow.close();
                failCb();
            }
        }, 1000);
    }

    function openUriUsingIEInWindows8(uri, failCb) {
        if (navigator.msLaunchUri) {
            navigator.msLaunchUri(uri,
                function () {
                    window.location = uri;
                },
                failCb
            );
        }
    }

    function checkBrowser() {
        var isOpera = !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
        return {
            isOpera: isOpera,
            isFirefox: typeof InstallTrigger !== 'undefined',
            isSafari: Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0,
            isChrome: !!window.chrome && !isOpera,
            isIE: /*@cc_on!@*/false || !!document.documentMode   // At least IE6
        }
    }

    function getInternetExplorerVersion() {
        var rv = -1;
        if (navigator.appName === "Microsoft Internet Explorer") {
            var ua = navigator.userAgent;
            var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
            if (re.exec(ua) != null)
                rv = parseFloat(RegExp.$1);
        }
        else if (navigator.appName === "Netscape") {
            var ua = navigator.userAgent;
            var re = new RegExp("Trident/.*rv:([0-9]{1,}[\.0-9]{0,})");
            if (re.exec(ua) != null) {
                rv = parseFloat(RegExp.$1);
            }
        }
        return rv;
    }

    window.protocolCheck = function (uri, failCb) {
        var browser = checkBrowser();

        function failCallback() {
            failCb && failCb();
        }

        if (browser.isFirefox) {
            openUriUsingFirefox(uri, failCallback);
        } else if (browser.isChrome) {
            openUriWithTimeoutHack(uri, failCallback);
        } else if (browser.isIE) {
            openUriUsingIE(uri, failCallback);
        } else {
            //not supported, implement please
        }
    }
}(window));
