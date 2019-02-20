<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8;">
  <link href='https://fonts.googleapis.com/css?family=Roboto:300' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" type="text/css" title="base" href="::stylesUrl::/dialog.css">
  <script type="text/javascript" src="::javascriptUrl::/loading.js"></script>
  <script>
    var sLocation = "::enterpriseLoginUrl::";
    function MyLoad() {
      if (sLocation == "") Load();
      else parent.window.location = sLocation;
    }
  </script>
</head>

<body onload="MyLoad()">
<div class="layout">
  <div>
    <img class="displayed" src="::federationLogoOriginalUrl::" alt="::federationLabel::" title="::federationLabel::"/>
  </div>

  <form method="post" id="formLogout" class="box" action="::logoutAction::">
    <table class="dialogs">
      <tr>
        <td class="logout dialog" style="width:70%;">
          <h1>You have not access</h1>

          <div class="tab_container">
            <div class="tab_content">
              <table border="0" width="100%">
                <tr>
                  <td width="100%">
                    <div class="HELP">You have not access to this application. Close session and access with another
                      account.
                    </div>
                  </td>
                </tr>
              </table>
              <div class="buttons">
                <button class="button login" type="submit">Logout</button>
              </div>
            </div>
          </div>
        </td>
      </tr>
      <tr height="21px">
        <td>
          <div class="powered">powered by
            <italic>Monet</italic>
          </div>
        </td>
      </tr>
    </table>
  </form>
  <span class="copyright">© ::currentYear:: All rights reserved</span>
</div>

</body>

</html>