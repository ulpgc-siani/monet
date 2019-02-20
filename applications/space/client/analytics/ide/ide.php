<?php
  include "compiler.php";
  include "protectedwords.php";
  include "modules.php";
  include "tokens.php";

  AddLibrary($Main);
  AddLibrary($Control);
  AddLibrary($CoreModel);
  AddLibrary($CoreKernel);
  AddLibrary($Interface);
  AddLibrary($Library);
  AddLibrary($ResourcesLang);
  AddLibrary($ResourcesStyles);
  AddLibrary($ResourcesTemplates);

  if (!UseModule("application.js")) exit;
  CompileModules("../../../server/WebContent/analytics", "analytics");
?>
