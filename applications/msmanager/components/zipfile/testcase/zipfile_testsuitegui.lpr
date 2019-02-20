program ZipFile_testsuiteGUI;

{$mode objfpc}{$H+}

uses
  Interfaces, Forms, GuiTestRunner, ZipFile_tc;

begin
  Application.Initialize;
  Application.CreateForm(TGuiTestRunner, TestRunner);
  Application.Run;
end.

