<html>
<head>
  <title>SiteShell Obfuscator</title>
</head>
<body>

<?php
  include("reservedwords.php");
  include("protectedwords.php");

  $ListTokens = array();

  function initTokens() {
    global $sOptions;
    global $iLenOptions;
    global $index;

    $sOptions = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    $iLenOptions = strlen($sOptions);
    $index = 0;
  }

  function generateToken() {
    global $sOptions;
    global $iLenOptions;
    global $index;

    $i = $index++;
    $s = "";
    while ($i >= $iLenOptions) {
      $j = $i % $iLenOptions;
      $s = $sOptions{$i % $iLenOptions} . $s;
      $i = floor ($i / $iLenOptions) - 1;
    }
    $s = $sOptions{$i % $iLenOptions} . $s;
    return $s;
  }

  function getNextToken() {
    global $ListReservedWords;

    do
      $sToken = generateToken();
    while (in_array($sToken, $ListReservedWords));
    return $sToken;

  }

  function addToken($sToken) {
    global $ListTokens;
    $sToken = str_replace("ReplacementFor_", "", $sToken);
    if (isset($ListTokens[$sToken]))
      $ListTokens[$sToken]++;
    else 
      $ListTokens[$sToken] = 1;
  }

  function loadFile($sFilename) {
    $sSep = "\n\t\r =.();:&!|[]+-*/<>,?";
    $sCode = trim(file_get_contents($sFilename));
    $sToken = strtok ($sCode, $sSep);
    while ($sToken !== false) {
      if (strpos($sToken, "ReplacementFor_") !== false) addToken($sToken);
      $sToken = strtok($sSep);
    }
  }

  function getNewTokens() {
    global $ListTokens;

    arsort($ListTokens, SORT_NUMERIC);
    foreach ($ListTokens as $Token => $value)
      $ListTokens[$Token] = getNextToken();
  }

  function saveTokens($sFilename) {
    global $ListTokens, $ListProtectedWords;

    $sOutput = "<?php\n\n\$Tokens = array();\n";
    foreach ($ListTokens as $Token => $value) {
      if (in_array($Token, $ListProtectedWords)) continue;
      $sOutput .= "\$Tokens[\"$Token\"] = \"$value\";\n";
    }
    $sOutput .= "?>";

    $File = fopen($sFilename, "w");
    fwrite($File, $sOutput);
    fclose($File);
  }


  initTokens();
  $sPath = dirname(__FILE__);
  loadFile("$sPath/temp/editor.es.js");
  loadFile("$sPath/temp/editor.js");
  getNewTokens();
  saveTokens("$sPath/tokens.php");
  echo "Tokens generados";
?>

</body>
</html>
