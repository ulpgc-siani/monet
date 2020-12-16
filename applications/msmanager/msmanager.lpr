program msmanager;

{$mode objfpc}{$H+}
{$DEFINE UseCThreads}

uses
  {$IFDEF UNIX}
    {$IFDEF UseCThreads}
      cthreads, cmem,
    {$ENDIF}
  {$ENDIF}
  Interfaces, // this includes the LCL widgetset
  Forms, ucontrol_main, utools,
  fabout, fproperties, fmessage,
  finput, uniqueinstanceraw, fnewremoteserver,
fnewspace, finformation, flogin;

{$R *.res}

begin
  if (OSType <> 'macos') then
    if InstanceRunning then
    begin
      ShowInformation('Is allowed only one instance of the application.');
      Halt;
    end;

  Application.Initialize;
  ControlMain := TControlMain.Create;
  Application.CreateForm(TFrmAbout, FrmAbout);
  Application.CreateForm(TFrmProperties, FrmProperties);
  Application.CreateForm(TFrmInformation, FrmInformation);
  Application.CreateForm(TWinDgInput, WinDgInput);
  Application.CreateForm(TFrmNewRemoteServer, FrmNewRemoteServer);
  Application.CreateForm(TFrmNewSpace, FrmNewSpace);
  Application.CreateForm(TFrmLogin, FrmLogin);
  Application.Run;
end.

