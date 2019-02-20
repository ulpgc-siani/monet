<?php
  include "compiler.php";
  include "protectedwords.php";
  include "modules.php";
  include "tokens.php";

  AddLibrary($Main);
  AddLibrary($Control);
  AddLibrary($CoreModel);
  AddLibrary($CoreIterators);
  AddLibrary($CoreSerializers);
  AddLibrary($CoreExceptions);
  AddLibrary($CoreProducer);
  AddLibrary($CoreKernel);
  AddLibrary($Interface);
  AddLibrary($InterfaceAdapter);  
  AddLibrary($InterfaceComponent);
  AddLibrary($InterfaceDialog);
  AddLibrary($InterfaceLang);
  AddLibrary($InterfaceLayout);
  AddLibrary($InterfaceToolbar);
  
  AddLibrary($InterfaceView);
  AddLibrary($Library);
  AddLibrary($LibraryEdi);
  AddLibrary($ResourcesStyles);
  AddLibrary($ResourcesStylesExt);
  AddLibrary($ResourcesStylesLang);
  AddLibrary($ResourcesTemplates);

  if (!UseModule("application.js")) exit;
  CompileModules("../../../server/WebContent/setup", "setup");
?>