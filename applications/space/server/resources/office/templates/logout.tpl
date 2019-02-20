<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8;">
  <link href='https://fonts.googleapis.com/css?family=Roboto:300' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" type="text/css" title="base" href="${STYLES_URL}/dialog.css">
  <script type="text/javascript" src="${JAVASCRIPT_URL}/loading.js"></script>
  <script>
    var sLocation = "${ENTERPRISE_LOGIN_URL}";
    function MyLoad() {
      if (sLocation == "") Load();
      else parent.window.location = sLocation;
    }
  </script>
</head>

<body onload="MyLoad()">

<div class="layout">
  <div>
    <img class="displayed" src="${FEDERATION_LOGO_ORIGINAL}" alt="${FEDERATION_LABEL}" title="${FEDERATION_LABEL}"/>
  </div>

  <form method="post" id="formLogout" class="box" action="${ACTION_LOGOUT}">
    <table class="dialogs">
      <tr>
        <td class="logout dialog" style="width:70%;">
          <h1>${LBL_USER_NOT_GRANTED}</h1>

          <div class="tab_container">
            <div class="tab_content">
              <table border="0" width="100%">
                <tr>
                  <td width="100%">
                    <div class="HELP">${MSG_USER_NOT_GRANTED}</div>
                  </td>
                </tr>
              </table>
              <div class="buttons">
                <button class="button login" type="submit">${LBL_LOGOUT}</button>
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
  <span class="copyright">© ${CURRENT_YEAR} ${LBL_RIGHTS_RESERVED}</span>
</div>

</body>

</html>