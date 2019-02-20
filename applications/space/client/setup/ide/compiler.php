<?php

//-----------------------------------------------------------------------------
$sInitialScript = "var AppTemplate=new Array;\nvar Lang=new Array;\nvar ToolbarDefinition=new Array;\nvar MenuDefinition=new Array;\nvar Path=new Array;\n\n";
$sGPLLicense = "/*\n    Monet Manager Application\n    (c) 2009 Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria\n\n    Manager is free software under the terms of the GNU Affero General Public License.\n    For details, see web site: http://www.gnu.org/licenses/\n*/\n\n";
$ListLibrary = array();
$ListTemplates = array();
$ListStyles = array();
$ListScripts = array();
$ListProtect = array();
$StackModules = array();
$Files = array();

//-----------------------------------------------------------------------------
function AddLibrary($Library) {
  global $ListLibrary;
  array_push($ListLibrary, $Library);
}

//-----------------------------------------------------------------------------
function UseModule($sModule) {
  global $ListLanguages;
  global $ListLibrary;
  global $ListTemplates;
  global $ListStyles;
  global $ListScripts;
  global $ListProtect;
  global $StackModules;

  if (array_search($sModule, $StackModules) !== false) {
    echo "<b>Error</b>: Referencia circular <br>";
    for ($i=0;$i<count($StackModules);$i++) echo $StackModules[$i] . " ";
    echo "<br>\n";
    return false;
  }

  $bFound = false;
  array_push($StackModules, $sModule);

  for ($i=0;$i<count($ListLibrary);$i++) {
    $Library = $ListLibrary[$i];
    $LibraryFiles = $Library[FILES];

    if (!array_key_exists($sModule, $LibraryFiles)) continue;

    $Data = $LibraryFiles[$sModule];
    if (is_array($Data))
      for ($j=0;$j<count($Data);$j++) 
        if (UseModule($Data[$j]) == false) 
          return false;

    $bFound = true;
    $sFilename = $Library[PATH] . $sModule;
    $bProtected = $Library[_PROTECTED];

    if ($bProtected) $ListProtect[$sModule] = true;

    if (strpos($sFilename, TAG_LANGUAGE) === false) {
      if (file_exists($sFilename) == false) {
        echo "<b>Error</b>: No existe el fichero <b>$sFilename</b><br>\n";
        return false;
      }
    }
    else {
      foreach ($ListLanguages as $key => $sLanguage) {
        $sAux = str_replace(TAG_LANGUAGE, $sLanguage, $sFilename);
        if (file_exists($sAux) == false) {
          echo "<b>Error</b>: No existe el fichero <b>$sAux</b><br>\n";
          return false;
        }
      }
    }
    break;
  }

  if ($bFound == false) {
    echo "<b>Error</b>: El m�dulo <b>$sModule</b> no ha sido declarado en ninguna librer�a<br>\n";
    return false;
  }
  
  $File = pathinfo($sFilename);
  $sExtension = strtoupper($File["extension"]);
  switch ($sExtension) {
    case "HTML":
      if (array_search($sModule, $ListTemplates)) return;
      $ListTemplates[$sModule] = $sFilename;
      break;
    case "CSS":
      if (array_search($sModule, $ListStyles)) return;
      $ListStyles[$sModule] = $sFilename;
      break;
    case "JS":
      if (array_search($sModule, $ListScripts)) return;
      $ListScripts[$sModule] = $sFilename;
      if ($bProtected) echo $sProtection;

      break;
  }

  array_pop($StackModules);
  return true;
}

//-----------------------------------------------------------------------------
function CompileModules($sBasename, $sName) {
  CompileScript($sBasename . "/javascript", $sName);
  CompileStyle($sBasename . "/styles", $sName);
}

//-----------------------------------------------------------------------------
function IsProtected($sFilename) {
  global $ListProtect;

  $File = pathinfo($sFilename);
  return $ListProtect[$File['basename']];
}

//-----------------------------------------------------------------------------
function CompileScript($sBasename, $sName) {
  global $ListLanguages;
  global $ListScripts;
  global $ListTemplates;
  global $sInitialScript;
  global $sGPLLicense;

  $Data[0] = $sGPLLicense;
  foreach ($ListLanguages as $key => $sLanguage) $Data[$key] = Obfuscate($sInitialScript, false);

  foreach ($ListTemplates as $key => $sFilename) {
    if (strpos($sFilename, TAG_LANGUAGE) === false)
      $Data[0] .= ReadTemplate($sFilename);
    else 
      foreach ($ListLanguages as $key => $sLanguage) {
        $sAux = str_replace(TAG_LANGUAGE, $sLanguage, $sFilename);
        $Data[$key] .= ReadTemplate($sAux);
      }
  }
  foreach ($ListScripts as $key => $sFilename) {
    if (strpos($sFilename, TAG_LANGUAGE) === false) 
      $Data[0] .= ReadScript($sFilename);
    else {
      foreach ($ListLanguages as $key => $sLanguage) {
        $sAux = str_replace(TAG_LANGUAGE, $sLanguage, $sFilename);
        $Data[$key] .= ReadScript($sAux);
      }
    }
  }
  WriteScript($sBasename, $sName, $Data);
}

//-----------------------------------------------------------------------------
function CompileStyle($sBasename, $sName) {
  global $ListLanguages;
  global $ListStyles;

  $Data[0] = "";
  foreach ($ListLanguages as $key => $sLanguage) $Data[$key] = "";

  foreach ($ListStyles as $key => $sFilename) {
    if (strpos($sFilename, TAG_LANGUAGE) === false) {
      $Data[0] .= ReadStyle($sFilename);
    }
    else
      foreach ($ListLanguages as $key => $sLanguage) {
        $sAux = str_replace(TAG_LANGUAGE, $sLanguage, $sFilename);
        $Data[$key] .= ReadStyle($sAux);
      }
  }
  WriteStyle($sBasename, $sName, $Data);
}

//-----------------------------------------------------------------------------

function AddTitle($sInput, $sFilename)  {
  global $bObfuscate;

  $sFilename = preg_replace("/^.*source\//","", $sFilename); 

  //if (!$bObfuscate) $sInput = "/*� $sFilename �*/\n$sInput";
  return "$sInput\n";
}

//-----------------------------------------------------------------------------

function Obfuscate($sInput, $bSkipStrings, $sSep = null) {
  global $ListProtectedPrefix;
  global $bObfuscate;
  global $Tokens;

  //if (!$bObfuscate) return $sInput;
  return $sInput;

  if (!$sSep) $sSep = "\n\t\r =.();:{}$&!|[]+-*/<>,?";


  if ($bSkipStrings) {
    $sInput = preg_replace('/"([^"\n]*)"/',"\n@@@\n\\1\n@@@\n", $sInput);  //Normalizar las "
    $sInput = preg_replace("/'([^'\n]*)'/","\n%%%\n\\1\n%%%\n", $sInput);  //Normalizar las '
  }

  foreach ($ListProtectedPrefix as $key => $sPrefix) {
    $sInput = preg_replace("/$sPrefix([^\(\)=;]*)/", "\n###\n$sPrefix\\1\n###\n", $sInput);
  }

  $sOutput = "";
  $iPos = 0;
  $bCode = true;
  $sToken = strtok ($sInput, $sSep);

  while ($sToken !== false) {
    $iTokenPos = strpos($sInput, $sToken, $iPos);
    $sAux = substr($sInput, $iPos, $iTokenPos - $iPos);

    $sOutput .= $sAux;
    if (($sToken == '@@@') || ($sToken == '%%%') || ($sToken == '###')) $bCode = !$bCode; 

    if (($bCode) && (isset($Tokens[$sToken]))) {
      $sOutput .= $Tokens[$sToken]; 
    }
    else 
      $sOutput .= $sToken;
    $iPos = $iTokenPos + strlen($sToken);
    $sToken = strtok($sSep);
  }
  $sOutput .= substr($sInput, $iPos, strlen($sInput));

  if ($bSkipStrings) {
    $sOutput = preg_replace("/\n###\n([^\n]*)\n###\n/", "\\1", $sOutput);
    $sOutput = preg_replace("/\n%%%\n([^\n]*)\n%%%\n/","'\\1'", $sOutput);  //Restaurar las '
    $sOutput = preg_replace("/\n@@@\n([^\n]*)\n@@@\n/",'"\\1"', $sOutput);  //Restaurar las "
  }

  return $sOutput;
}

//-----------------------------------------------------------------------------

function RemoveComments($sInput) {
  $sInput = preg_replace("/\r/", "", $sInput);   // Quitar los LF
  $sInput = preg_replace("/(http|ftp|mailto):\/\//i", "###\$1:###", $sInput);  //Normalizar las URL
  $sInput = preg_replace("/\(\/(.*)\/(.*)\)/U","###\\1,\\2###", $sInput); // Normalizar expresiones regulares: (/.../...) -> %%%...,...%%%
  $sInput = preg_replace("/\/\/(.*)\n/", "\n", $sInput);  // Eliminar comentarios del tipo //...
  $sInput = preg_replace("/\/\*(.*)?\*\//Usi", "", $sInput);  // Eliminar comentarios del tipo /* ... */
  $sInput = preg_replace("/###(http|ftp|mailto):###/i", "\$1://", $sInput);  //Restaurar las URL
  $sInput = preg_replace("/###(.*),(.*)###/U","(/\\1/\\2)", $sInput);  //Restaurar las expresiones regulares
  return $sInput;
}

//-----------------------------------------------------------------------------

function RemoveSpaces($sInput)  {
  global $bObfuscate;
  //if (!$bObfuscate) return $sInput;
  return $sInput;

  $sInput = preg_replace("/\n/","@@@", $sInput);   // Normalizar CR
  $sInput = preg_replace("/\s+([!\}\{;,&<=>\|\-\+\*\/\)\(:])/","\\1", $sInput); // Eliminar espacios antes de: [!}{;,&=|-+*/)(:
  $sInput = preg_replace("/([!\}\{;,&<=>\|\-\+\*\/\)\(:])\s+/","\\1", $sInput); // Eliminar espacios despues de: [!}{;,&=|-+*/)(:
  $sInput = preg_replace("/\s+@@@+/","@@@", $sInput);  //Eliminar espacios antes de CR
  $sInput = preg_replace("/@@@+\s+/","@@@", $sInput);  //Eliminar espacios despues de CR
  $sInput = preg_replace("/@@@+/","\n", $sInput);  // Restaurar CR
  $sInput = trim($sInput);

  $sInput = str_replace("\n}","}", $sInput);  //
  $sInput = str_replace("{\n","{", $sInput);  //
  $sInput = str_replace("(\n","(", $sInput);  //
  $sInput = str_replace(",\n",",", $sInput);  //
  $sInput = str_replace("=\n","=", $sInput);  //
  $sInput = str_replace(";\n",";", $sInput);  //
  $sInput = str_replace(";}","}", $sInput);   //
  return $sInput;
}

//-----------------------------------------------------------------------------

function ReadScript($sFilename) {
  $sOutput = trim(file_get_contents_utf8($sFilename)) . "\n";
  $sOutput = RemoveComments($sOutput);
  $sOutput = AddTitle($sOutput, $sFilename);
  if (!IsProtected($sFilename)) $sOutput = Obfuscate($sOutput, true);

  return $sOutput;
}

//-----------------------------------------------------------------------------

function file_get_contents_utf8($fn) { 
     $content = file_get_contents($fn); 
      return mb_convert_encoding($content, 'UTF-8', 
          mb_detect_encoding($content, 'UTF-8, ISO-8859-1', true)); 
} 

//-----------------------------------------------------------------------------
function ReadTemplate($sFilename) {
  $sTemplate = basename($sFilename, ".html");
  $sData = trim(file_get_contents($sFilename));
  $sData = preg_replace("/\r/", "", $sData);
  $sData = preg_replace("/\s*([<>])\s*/","\\1", $sData);
  $sData = str_replace("'", "\'", $sData);
  $sData = utf8_encode($sData);

  $sOutput = "AppTemplate.$sTemplate='+++';\n";
  $sOutput = Obfuscate($sOutput, false);
  $sOutput = str_replace("+++", $sData, $sOutput);
  return $sOutput;
}

//-----------------------------------------------------------------------------
function ReadStyle($sFilename) {
  $sOutput = trim(file_get_contents($sFilename)) . "\n";
  $sOutput = preg_replace("/\r/", "", $sOutput);
  $sOutput = preg_replace("/\/\/(.*)?\n/", "\n", $sOutput);
  $sOutput = preg_replace("/\/\*(.*)?\*\//Usi", "", $sOutput);
  $sOutput = preg_replace("/(\n)+/","\n", $sOutput);
  $sOutput = AddTitle($sOutput, $sFilename);
  return $sOutput;
}

//-----------------------------------------------------------------------------
function WriteScript($sBasename, $sName, $Data) {
  global $bObfuscate;

  foreach ($Data as $key => $sOutput) {
    $sOutput = RemoveSpaces($sOutput);
    if ($sOutput == "") continue;

    $sFilename = $sBasename . "/$sName" . (($key == "0") ? "" : ".$key") . ".js";
	file_put_contents($sFilename, $sOutput);
    
    if ($bObfuscate) {
  	  $sOutput = str_replace("í", "#####i#####", $sOutput);
	  file_put_contents($sFilename, $sOutput);
	  
	  system("java -jar " . dirname(__FILE__) . "/obfuscator.jar " . $sFilename . " -o " . $sFilename);

	  $sContent = file_get_contents($sFilename);
	  $sContent = str_replace("#####i#####", "í", $sContent);
	  file_put_contents($sFilename, $sContent);
	}
  }
}

//-----------------------------------------------------------------------------
function WriteStyle($sBasename, $sName, $Data) {
  foreach ($Data as $key => $sOutput) {
    $sOutput = RemoveSpaces($sOutput);
    if ($sOutput == "") continue;

    $sFilename = $sBasename . "/$sName" . (($key == "0") ? "" : ".$key") . ".css";
    $File = fopen($sFilename, "w");
    fwrite($File, $sOutput);
    fclose($File);
  }
}

?>