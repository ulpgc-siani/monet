<?php
  include "compiler.php";
  include "protectedwords.php";
  include "modules.php";
  include "tokens.php";

  AddLibrary($Main);
  
  AddLibrary($Control);
  AddLibrary($ControlCommands);
  AddLibrary($ControlNotifications);  
  AddLibrary($ControlActivities);
  AddLibrary($ControlPlaces);
  
  AddLibrary($Core);  
  AddLibrary($CoreModel);
  AddLibrary($CoreEvents);  
  AddLibrary($CoreSerializers);
  AddLibrary($CoreExceptions);  
  AddLibrary($CoreKernel);
  
  AddLibrary($UI);  
  AddLibrary($UIComponent);
  AddLibrary($UIDialog);
  AddLibrary($UIEditor);
  AddLibrary($UILang);
  AddLibrary($UILayout);    
  AddLibrary($UIView);
  AddLibrary($Library);
  
  AddLibrary($ResourcesStyles);
  AddLibrary($ResourcesStylesExt);
  AddLibrary($ResourcesStylesLang);
  AddLibrary($ResourcesTemplates);

  if (!UseModule("application.js")) exit;
  CompileModules("../../server/WebContent", "grided");
?>