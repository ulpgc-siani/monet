<html>
<head>
  <title>Monet setup</title>

  <link href='https://fonts.googleapis.com/css?family=Roboto:300' rel='stylesheet' type='text/css'>

  <script type="text/javascript" src="${JAVASCRIPT_URL}/jquery-1.7.2.min.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/jquery-ui-1.8.20.custom.min.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/jquery.tmpl.min.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/jquery.form.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/signer.lib.js"></script>
  <script type="text/javascript" src="${JAVASCRIPT_URL}/setup.js"></script>
  <script type="text/javascript" src="${WEB_COMPONENTS_URL}/webcomponentsjs/webcomponents-lite.js"></script>

  <link rel="import" href="${WEB_COMPONENTS_URL}/cotton-signatory/cotton-signatory-signer.html">

  <script>
    var selectedTabName = "${TAB}";

    window.onload = function () {
      var selectedIndex = 0;

      if (selectedTabName == "masters")
        selectedIndex = 1;
      else if (selectedTabName == "distribution")
        selectedIndex = 2;

      $(".tabs").tabs({selected: selectedIndex});
    }

    //window.setTimeout("window.location = location;", 100000);
  </script>

  <link rel="stylesheet" type="text/css" title="base" href="${STYLES_URL}/jquery-ui-1.8.20.custom.css"/>
  <link rel="stylesheet" type="text/css" title="base" href="${STYLES_URL}/jquery-ui-setup.css"/>
  <link rel="stylesheet" type="text/css" title="base" href="${STYLES_URL}/setup.css"/>
</head>

<body>

<cotton-signatory-signer language="${LANGUAGE}" download-url="${APPLICATION_SIGNATORY_URL}/app"
                         storage-url="${APPLICATION_SIGNATORY_URL}/store"
                         retrieve-url="${APPLICATION_SIGNATORY_URL}/retrieve"></cotton-signatory-signer>

<table cellpadding="0" cellspacing="0" width="100%" height="95%">
  <tr>
    <td class="top">
      <img class="logo" src="${URL}/images/logo.gif">
      <ul class="toolbar">
        <li><a href="${API_URL}?op=logout">${LBL_LOGOUT}</a></li>
      </ul>
    </td>
  </tr>
  <tr>
    <td width="100%" height="100%" align="center" class="bottom">

      <div class="tabs">

        <ul>
          <li><a href="#tab-state">${LBL_STATE}</a></li>
          <li><a href="#tab-masters">${LBL_MASTERS}</a></li>
          <li><a href="#tab-distribution">${LBL_DISTRIBUTION}</a></li>
        </ul>

        <div id="tab-state" class="tab">
          #set ($ACTION = "${API_URL}?op=start")
          #if ($IS_BUSINESS_UNIT_STARTED)
          #set ($ACTION = "${API_URL}?op=stop")
          #end

          <form action="${ACTION}" method="POST">
            #if ($IS_BUSINESS_UNIT_STARTED)
            <div class="message">${MSG_BUSINESS_UNIT_STARTED}</div>
            <div class="info">${MSG_BUSINESS_UNIT_STARTED_INFO}</div>
            <input type="submit" class="button stop" value="${LBL_STOP_BUSINESS_UNIT}">
            #else
            <div class="message">${MSG_BUSINESS_UNIT_STOPPED}</div>
            <div class="info">${MSG_BUSINESS_UNIT_STOPPED_INFO}</div>
            <input type="submit" class="button start" value="${LBL_START_BUSINESS_UNIT}">
            #if ($ERROR_CODE)
            <div class="error">$ERR_BUSINESS_UNIT_COULD_NOT_START</div>
            #end
            <div class="dialog components">
              <div class="label">${LBL_COMPONENTS}</div>
              <table style="margin: 10px;">
                #foreach($COMPONENTTYPE in $COMPONENT_TYPES)
                <tr>
                  <td width="400px">
                    <span>${COMPONENTTYPE.getLabel("es")}</span>
                  </td>
                  <td>
                    <select id="${COMPONENT_PREFIX}${COMPONENTTYPE.getCode()}" name="${COMPONENT_PREFIX}${COMPONENTTYPE.getCode()}">
                      #foreach($COMPONENT in $COMPONENTS.get($COMPONENTTYPE.getCode()))
                      #set ($SELECTED = "")
                      #if ($BUSINESSUNIT.getResources().containsKey($COMPONENT.getCode()))
                      #set ($SELECTED = "selected")
                      #end
                      <option value="${COMPONENT.getCode()}" ${SELECTED}>${COMPONENT.getLabel("es")}</option>
                      #end
                    </select>
                  </td>
                </tr>
                #end
              </table>
            </div>
            #end
          </form>
        </div>

        <div id="tab-masters" class="tab">
          <ul class="masters">
            #foreach($MASTER in $MASTERS)
            <li>
              <table>
                <tr>
                  <td>
                    #set ($info = $MASTER.getInfo())
                    #if (!$info.getFullname().isEmpty())
                    #set ($name = "${info.getFullname()}")
                    #else
                    #set ($name = "${MASTER.getUsername()}")
                    #end

                    #set ($colonizer = "")
                    #if ($MASTER.isColonizer())
                    #set ($colonizer = " <span>(${LBL_COLONIZER})</span>")
                    #end

                    #set ($email = "${LBL_NO_MAIL}")
                    #if ($MASTER.getInfo().getEmail())
                    #set ($email = "${MASTER.getInfo().getEmail()}")
                    #end

                    <div class="username">${name}${colonizer}</div>
                    <div class="email">${email}</div>
                  </td>
                  <td style="vertical-align:top;">
                    #if (!$MASTER.isColonizer())
                    <a class="delete" href="${API_URL}?op=deletemaster&id=$MASTER.getId()">${LBL_DELETE}</a>
                    #end
                  </td>
                </tr>
              </table>
            </li>
            #end
          </ul>
          <div><a class="toggle" href="javascript:void(null)" onclick="javascript:toggleVisibility('addmasterfromsignature')">${LBL_TOGGLE_MASTER_FROM_SIGNATURE_DIALOG}</a>
          </div>
          <form id="addmasterfromsignature" action="${API_URL}?op=addmasterfromsignature" method="POST" style="display:none;">
            <div class="dialog">
              <div class="label">${LBL_ADD_MASTER_FROM_SIGNATURE}</div>
              <div class="message">${MSG_ADD_MASTER_FROM_SIGNATURE}</div>
              <div id="addmasterfromsignatureresponse" class="message failure"></div>
              <input type="hidden" name="signature" id="signature"/>
            </div>
            <input type="button" class="button" onclick="addMasterFromSignature()" value="${LBL_ADD}">
          </form>
          <div><a class="toggle" href="javascript:void(null)" onclick="javascript:toggleVisibility('addmasterfromcertificate')">${LBL_TOGGLE_MASTER_FROM_CERTIFICATE_DIALOG}</a>
          </div>
          <form id="addmasterfromcertificate" action="${API_URL}?op=addmasterfromcertificate" method="POST" enctype="multipart/form-data" style="display:none;">
            <div class="dialog">
              <div class="label">${LBL_ADD_MASTER_FROM_CERTIFICATE}</div>
              <div class="message">${MSG_ADD_MASTER_FROM_CERTIFICATE}</div>
              <input class="file" type="file" name="certificate" id="certificate"/>
            </div>
            <input type="submit" class="button" value="${LBL_ADD}">
          </form>
        </div>

        <div id="tab-distribution" class="tab">
          <div class="distribution_info">
            #if ($DISTRIBUTION)
            <ul>
              <li>${LBL_DISTRIBUTION_NAME}: <span>${DISTRIBUTION_NAME}</span></li>
              <li>${LBL_DISTRIBUTION_TITLE}: <span>${DISTRIBUTION_TITLE}</span></li>
              <li>${LBL_DISTRIBUTION_SUBTITLE}: <span>${DISTRIBUTION_SUBTITLE}</span></li>
              <li>${LBL_DISTRIBUTION_AUTHOR}: <span>${DISTRIBUTION_AUTHOR}</span></li>
              <li>${LBL_DISTRIBUTION_FEDERATION}: <span>${DISTRIBUTION_FEDERATION}</span></li>
              <li>${LBL_DISTRIBUTION_METAMODEL_VERSION}: <span>${DISTRIBUTION_METAMODEL_VERSION}</span></li>
              <li>${LBL_DISTRIBUTION_VERSION}: <span>${DISTRIBUTION_VERSION}</span></li>
              <li>${LBL_DISTRIBUTION_RELEASE}: <span>${DISTRIBUTION_RELEASE}</span></li>
            </ul>
            #if ($RUNNING)
            <a class="toggle" href="javascript:void(null)" onclick="javascript:toggleVisibility('uploaddistribution')">${LBL_TOGGLE_DISTRIBUTION_DIALOG}</a>

            <form id="uploaddistribution" action="${API_URL}?op=uploaddistribution" method="POST" enctype="multipart/form-data" style="display:none;">
              <div class="dialog">
                <div class="label">${LBL_UPLOAD_DISTRIBUTION}</div>
                <div class="message">${MSG_UPLOAD_DISTRIBUTION}</div>
                <div class="message">${MSG_DOWNLOAD_DISTRIBUTION}. <a class="download" href="${API_URL}?op=downloaddistribution">${LBL_DOWNLOAD}</a>
                </div>
                <div id="uploaddistributionresponse" class="uploaddistributionresponse">${MSG_WAITING_RESPONSE}</div>
                <input class="file" type="file" name="distribution" id="distribution"/>
              </div>
              <input type="button" class="button" value="${LBL_UPDATE}" onclick="javascript:uploadDistribution('${MSG_WAITING_RESPONSE}')">
            </form>
            #end
            #else
            <div class="nodistribution">${LBL_NO_DISTRIBUTION}</div>
            <form id="uploaddistribution" action="${API_URL}?op=uploaddistribution" method="POST" enctype="multipart/form-data">
              <div class="dialog">
                <div class="label">${LBL_INSTALL_DISTRIBUTION}</div>
                <div class="message">${MSG_INSTALL_DISTRIBUTION}</div>
                <div id="uploaddistributionresponse" class="uploaddistributionresponse">${MSG_WAITING_RESPONSE}</div>
                <input class="file" type="file" name="distribution" id="distribution"/>
              </div>
              <input type="button" class="button" value="${LBL_INSTALL}" onclick="javascript:uploadDistribution('${MSG_WAITING_RESPONSE}')">
            </form>
            #end
          </div>
          <div class="context_info">
            <h3>${LBL_CONTEXT_INFO}</h3>
            <ul>
              <li>${LBL_APPLICATION_DIR}: <span>${APPLICATION_DIR}</span></li>
              <li>${LBL_HOME_DIR}: <span>${HOME_DIR}</span></li>
              <li>${LBL_CONTEXT_NAME}: <span>${CONTEXT_NAME}</span></li>
              <li>${LBL_CHARSET}: <span>${CHARSET}</span></li>
              <li>${LBL_CURRENT_DATE}: <span>${CURRENT_DATE}</span></li>
            </ul>
          </div>
          #if (${VERSION_INFO})
          <div class="version_info">
            <h3>${LBL_VERSION_INFO}</h3>

            <div>${VERSION_INFO}</div>
          </div>
          #end
        </div>

      </div>
    </td>
  </tr>
</table>

</body>
</html>