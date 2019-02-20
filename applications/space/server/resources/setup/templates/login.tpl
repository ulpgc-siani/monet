<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>Monet - Setup</title>
  <meta charset="utf-8">

  <script type="text/javascript" src="${WEB_COMPONENTS_URL}/webcomponentsjs/webcomponents-lite.js"></script>
  <link rel="import" href="${WEB_COMPONENTS_URL}/paper-input/paper-input.html">
  <link rel="import" href="${WEB_COMPONENTS_URL}/paper-button/paper-button.html">
  <link rel="import" href="${WEB_COMPONENTS_URL}/cotton-signatory/cotton-signatory-login.html">
  <link rel="import" href="${WEB_COMPONENTS_URL}/iron-ajax/iron-ajax.html">
</head>

<body>
    <style is="custom-style">
        body {
            @apply(--layout-vertical);
            font-family: RobotoDraft, 'Helvetica Neue', Helvetica, Arial;
            background: #efefef;
            margin: 0 20px 10px;
        }

        .dialog {
            align-items:center;
            -webkit-align-items: center;
            justify-content:center;
            -webkit-justify-content:center;
            width: 500px;
            min-width: 300px;
            padding: 20px 10px;
            background: white;
            border: 1px solid #efefef;
            -webkit-border-radius: 10px;
            -moz-border-radius: 10px;
            border-radius: 10px;
            box-shadow: 2px 2px 5px #808080;
            -webkit-box-shadow: 2px 2px 5px #808080;
            -moz-box-shadow: 2px 2px 5px #808080;
            margin: 10% auto;
        }

        h1 {
            margin-left: 10px;
            margin-bottom: 0;
        }

        p {
            margin: 15px 10px;
        }

        #responseFailure {
            color: red;
            font-size: 18px;
            margin: 70px 0 0;
            display: none;
            text-align: center;
        }

        paper-button {
            margin: 10px 0;
            float: right;
            background: #4285f4;
            color: #fff;
        }

        .home {
            right: 0;
            text-align: right;
            margin-top: 10px;
            position: absolute;
            margin-right: 20px;
        }

    </style>

    <div class="dialog centered">
        <div>
            <iron-ajax method="post" url="${API_URL}?op=login&mode=signature" content-type="application/text"></iron-ajax>
            <cotton-signatory-login language="${LANGUAGE}" download-url="${APPLICATION_SIGNATORY_URL}/app"
                                    storage-url="${APPLICATION_SIGNATORY_URL}/store"
                                    retrieve-url="${APPLICATION_SIGNATORY_URL}/retrieve"></cotton-signatory-login>
        </div>
        #if (!$COLONIZED)
        <div class="message notcolonized">${MSG_NOT_COLONIZED}</div>
        #end
        <div id="responseFailure" class="flex">login failure!!</div>
    </div>

    <script>
        document.querySelector("cotton-signatory-login").addEventListener("beforeLogin", function(event) {
            document.querySelector("#responseFailure").style.display = "none";
        });

        document.querySelector("cotton-signatory-login").addEventListener("login", function(event) {
            var ajax = document.querySelector("iron-ajax");
            ajax.body = "signature=" + event.detail.signature;
            ajax.generateRequest();
        });

        document.querySelector("cotton-signatory-login").addEventListener("loginFailure", function(event) {
            var responseFailure = document.querySelector("#responseFailure");
            responseFailure.style.display = "block";
            if (event.detail.message !== "")
                responseFailure.innerHTML = event.detail.message;
        });

        document.querySelector("iron-ajax").addEventListener("response", function(event) {
            var response = event.detail.response;

            if (response.code === 1) {
                window.location.href = "${URL}";
                return;
            }

            var responseElement = document.querySelector("#responseFailure");
            responseElement.style.display = "block";
            responseElement.innerHTML = response.message;
        });

    </script>

</body>

</html>