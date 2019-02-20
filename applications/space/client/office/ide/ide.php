<?php
  include "compiler.php";
  include "protectedwords.php";
  include "modules.php";
  include "tokens.php";

  AddLibrary($Main);
  AddLibrary($Control);
  AddLibrary($CoreModel);
  AddLibrary($CoreIterators);
  AddLibrary($CoreProducer);
  AddLibrary($CoreKernel);
  AddLibrary($Interface);
  AddLibrary($InterfaceConstructor);
  AddLibrary($InterfaceDecorator);
  AddLibrary($InterfaceBehaviour);
  AddLibrary($InterfaceDialog);
  AddLibrary($InterfaceLang);
  AddLibrary($InterfaceLayout);
  AddLibrary($InterfaceToolbar);
  AddLibrary($InterfaceEditor);
  AddLibrary($InterfaceView);
  AddLibrary($InterfaceViewer);
  AddLibrary($InterfaceWidget);
  AddLibrary($Library);
  AddLibrary($LibraryEdi);
  AddLibrary($LibraryExt);
  AddLibrary($LibraryIsi);
  AddLibrary($LibraryView);
  AddLibrary($ResourcesStyles);
  AddLibrary($ResourcesStylesExt);
  AddLibrary($ResourcesStylesLang);
  AddLibrary($ResourcesTemplates);

  if (!UseModule("application.js")) exit;
  CompileModules("../../../server/WebContent/office", "office");
?>
