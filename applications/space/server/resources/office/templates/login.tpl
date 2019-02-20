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
<table align="center" cellpadding="0" cellspacing="0" width="387px" class="DIALOG">
  <tr>
    <td width="100%" class="TITLE">${LBL_LOGIN}</td>
  </tr>
  <tr>
    <td width="100%" class="BODY">
      <div class="HELP">${LBL_WELCOME}</div>
      <form action="" method="post" name="login" id="login">
        <input name="action" type="hidden" id="action" value="${ACTION_DOLOGIN}">
        <table cellpadding="0" cellspacing="0" width="100%">
          <tr>
            <td><label for="name">${LBL_USERNAME}</label></td>
            <td width="100%"><input name="name" type="text" id="name" class="TEXT"></td>
          </tr>
          <tr>
            <td><label for="password">${LBL_PASSWORD}</label></td>
            <td width="100%"><input name="password" type="password" id="password" class="TEXT"></td>
          </tr>
          <input type="hidden" name="businessunit" id="businessunit" value="${BUSINESSUNIT.getCode()}"/>
        </table>
        <div class="BUTTONS"><input name="login" onClick="ShowAni()" type="submit" id="login" class="BUTTON"
                                    value="${LBL_ACCEPT}"></div>
      </form>
      #if (${ERROR_MESSAGE} != "")
      <div class="ERROR">
        <h1>${MSG_UNKOWN_USERNAME_OR_PASSWORD}</h1>
        <span>${MSG_UNKOWN_USERNAME_OR_PASSWORD_RECOMMENDATIONS}</span>
      </div>
      #end
    </td>
  </tr>
  <tr height="21px">
    <td class="BOTTOM" width="100%">&nbsp;</td>
  </tr>
</table>
</body>

</html>