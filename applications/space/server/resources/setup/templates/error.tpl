<html>
<head>
  <title>Monet</title>

  <link href='https://fonts.googleapis.com/css?family=Roboto:300' rel='stylesheet' type='text/css'>

  <script type="text/javascript" src="${JAVASCRIPT_URL}/loading.js"></script>
  <script>
    window.onload = function () {
      Load();
    }
  </script>

  <style type="text/css">
    BODY {
      margin: 0;
      padding: 0;
      font-family: Roboto, "Trebuchet MS", Tahoma, Arial, Serif;
    }

    INPUT {
      font-size: 25pt;
      height: 80px;
      width: 300px;
      cursor: pointer;
    }

    TD.TOP {
      height: 46px;
      background: white url('${URL}/images/header-bg.gif') repeat-x right 0;
      color: white;
      font-family: Roboto, Tahoma;
      font-size: 8pt;
      padding: 3px;
      border-bottom: 1px solid Black;
    }

    TD.TOP .LOGO {
      float: left;
      margin: 0px 15px;
    }

    TD.BOTTOM {
      background-color: #DFEBFF;
    }

    TD.BOTTOM .MESSAGE {
      font-size: 18pt;
      margin: 20px 20px 0px;
      color: #1D4191;
    }

    TD.BOTTOM .INFO {
      color: #666;
      font-size: 10pt;
      margin-bottom: 20px;
    }

    TD.BOTTOM .RESOURCES {
      border: 1px solid #1D4191;
      margin-top: 20px;
      text-align: left;
      width: 400px;
      font-size: 11pt;
    }

    TD.BOTTOM .RESOURCES .LABEL {
      background-color: #1D4191;
      color: white;
      padding: 5px;
    }

    TD.BOTTOM .RESOURCES SPAN {
      font-size: 11pt;
      font-weight: bold;
    }

    TD.BOTTOM .RESOURCES TR {
      height: 30px;
    }

    TD.BOTTOM .RESOURCES SELECT {
      width: 265px;
      cursor: pointer;
    }
  </style>

</head>

<body>

<table cellpadding="0" cellspacing="0" width="100%" height="95%">
  <tr>
    <td class="TOP">
      <img class="LOGO" src="${URL}/images/logo.gif">
    </td>
  </tr>
  <tr>
    <td width="100%" height="100%" align="center" class="BOTTOM">
      #if ($ERROR_CODE == "ERR_BUSINESS_UNIT_STOPPED")
      <div class="MESSAGE">${ERR_BUSINESS_UNIT_STOPPED}</div>
      <div class="INFO">${MSG_BUSINESS_UNIT_STOPPED_INFO}</div>
      #else ${ERR_UNKNOWN}
      <div class="MESSAGE">${ERR_UNKNOWN}</div>
      <div class="INFO">${MSG_UNKOWN}</div>
      #end
    </td>
  </tr>
</table>

</body>
</html>