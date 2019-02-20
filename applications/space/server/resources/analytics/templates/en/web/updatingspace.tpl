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
<table align="center" cellpadding="0" cellspacing="0" width="387px" class="DIALOG">
  <tr>
    <td width="100%" class="TITLE">Updating...</td>
  </tr>
  <tr>
    <td width="100%" class="BODY">
      <div class="HELP">The business space is being updated. Try access again later.</div>
      <a href="::reloadAction::">Reload</a>
    </td>
  </tr>
  <tr height="21px">
    <td class="BOTTOM" width="100%">&nbsp;</td>
  </tr>
</table>
</body>

</html>