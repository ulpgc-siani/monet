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
    <td width="100%" class="TITLE">${LBL_UPDATING_SPACE}</td>
  </tr>
  <tr>
    <td width="100%" class="BODY">
      <div class="HELP">${MSG_UPDATING_SPACE}</div>
      <a href="${ACTION_RELOAD}">${LBL_RELOAD}</a>
    </td>
  </tr>
  <tr height="21px">
    <td class="BOTTOM" width="100%">&nbsp;</td>
  </tr>
</table>
</body>

</html>