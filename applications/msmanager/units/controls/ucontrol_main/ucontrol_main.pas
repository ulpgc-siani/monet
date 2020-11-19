unit ucontrol_main;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, fmain, uagent_main, ucontrol_wait, uagent_commandthread, uagent_vm, ucontrol_server,ucontrol_commandthread, uagent_preferences, ucontrol_preferences, uagent_remoteserver;

type

  TControlMain = class
    private
      FAgentMain: TAgentMain;
      FAgentPreferences: TAgentPreferences;
      FAgentCommandThread: TAgentCommandThread;
      FAgentVM: TAgentVM;
      FAgentRemoteServer: TAgentRemoteServer;

      ControlWaitAtOperation: TControlWait;
      ControlWaitCheckMSG: TControlWait;
      ControlCommandThreadRefresh: TControlCommandThread;
      ControlCommandThreadConfigure: TControlCommandThread;
      ControlPreferences: TControlPreferences;

      procedure CheckMSG(ShowMessageUpdate: boolean);
      procedure CheckExistsMSG;
      function CheckUpdateMSG(shownotification: boolean): boolean;
      procedure ExecuteUpdateMSG;
      procedure ProcessUpdateMSG(ShowMessageUpdate: boolean);

      procedure FinishDownload;

      procedure AtOperation(Sender: TObject; Operation: TOperationMain);
    public
      constructor Create;
  end;

var
  ControlMain: TControlMain;

implementation

uses forms, lclintf, utools, controls, LCLType, fabout, utypes, uconstants, fproperties,clipbrd, fmessage, fnewremoteserver, usocommand, TypInfo;

constructor TControlMain.Create;
begin
  FAgentPreferences := TAgentPreferences.Create;
  FAgentPreferences.Log.Info('TControlMain.Create: Start application.');

  FAgentMain := TAgentMain.Create(FAgentPreferences);
  FAgentCommandThread := TAgentCommandThread.Create(FAgentPreferences);
  FAgentVM := TAgentVM.Create(FAgentPreferences, FAgentCommandThread);
  FAgentRemoteServer := TAgentRemoteServer.Create(FAgentPreferences);

  Application.CreateForm(TFrmMain, FrmMain);
  FrmMain.OnOperation := @AtOperation;
  FrmMain.AgentPreferences:=FAgentPreferences;
  Application.CreateForm(TFrmAbout, FrmAbout);

  ControlWaitAtOperation := TControlWait.Create(FrmMain);
  ControlWaitCheckMSG := TControlWait.Create(FrmMain);
  ControlPreferences := TControlPreferences.Create(FrmMain, FAgentPreferences);

{  if not FAgentVM.ExistsVirtualBox then
  begin
    if ShowConfirmation('VirtualBox must be installed to run this application. Do you want to download?') = mrYes then
      OpenURL(UrlDownloadVirtualbox);
  end;}
  if OSType = 'windows' then
    if FAgentVM.VirtualBoxVersion = '4.3.8' then
      ShowWarning('VirtualBox version 4.3.8 not compatible with this version in Windows.', 'Warning');

  if not FAgentVM.VirtualBoxExists then
  begin
    FrmMain.ServersControl.ActivePage := FrmMain.TabRemoteServers;
//    FrmMain.ServersControl.ShowTabs:= false;
    FrmMain.LblLoading.Caption:= 'VirtualBox is not installed.';
  end;

  FAgentPreferences.Log.Info('TControlMain.Create: Check is inactive network.');
  if FAgentPreferences.IsInactiveNetwork then
  begin
    ShowInformation('The network has changed. It will select a default IP.');
    FAgentPreferences.IP := FAgentPreferences.MainIp;
    FAgentPreferences.SaveConfig;
  end;

  FAgentPreferences.Log.Info('TControlMain.Create: Refresh remote machines.');
  AtOperation(Self, opRefreshListRemote);

  FrmMain.TabIndex:= FAgentPreferences.TabIndex;

  if FAgentVM.VirtualBoxExists then
  begin
    FAgentPreferences.Log.Info('TControlMain.Create: Refresh local machines.');
    AtOperation(Self, opRefreshMachines);
    AtOperation(Self, opConfigureAllMachines);
  end;
  AtOperation(Self, opDisableAllButtons);

  FAgentPreferences.Log.Info('TControlMain.Create: End constructor.');
  FAgentPreferences.Log.Info('TControlMain.Create. OS: ' + OsType);
end;

procedure TControlMain.AtOperation(Sender: TObject; Operation: TOperationMain);
var
  CommandInfo: TCommandInfo;
  ControlCommandThread: TControlCommandThread;
  ControlCommandThreads: TControlCommandThreads;
  IsRunningMachine: boolean;
  MachinesCount: integer;
  MachineFileName: string;
  MachineList: TStringList;

  ServerInfo: TRemoteServer;

  SOCommand: TSOCommand;
  RemoteServers: TRemoteServers;

  ControlServer: TControlServer;
  ServerForm: TForm;

begin
  CommandInfo.Name := FrmMain.MachineNameLocal;
  CommandInfo.FileName:= FrmMain.MachineFileName;
  CommandInfo.Domain:= FAgentPreferences.IP;
  CommandInfo.ShowModalWait:= true;

  if (Operation <> opChangeMachine) then
    FAgentPreferences.Log.Info('Operation: ' + GetEnumName(TypeInfo(TOperationMain), integer(Operation)));

  case Operation of
    opAddMachine:
    begin
      AtOperation(Sender, opStopThreads);
      CheckMSG(false);
      MachineFileName := GetConfigDir + '/' + MonetServerTemplateFileName;

      if FileExists(MachineFileName) then
      begin
        FrmMain.ForceUpdateMachineFileName(MachineFileName);
        FrmMain.ForceUpdateMachineName(CommandInfo.Name);
        AtOperation(Sender, opImportMachine)
      end;
      AtOperation(Sender, opStartThreads);
    end;
    opRefreshMachines:
    begin
      FAgentPreferences.Log.Info('TControlMain.Create: Refresh machines.');
      CommandInfo.Command:= opRefreshMainListCmd;
      ControlCommandThreadRefresh := TControlCommandThread.Create(CommandInfo, FAgentPreferences, FAgentCommandThread, FAgentVM, nil);
      ControlCommandThreadRefresh.OnMainRefreshList:= @FrmMain.RefreshList;
//      ControlCommandThreadRefresh.OnMainUpdateForm:= @FrmMain.UpdateForm;
      ControlCommandThreadRefresh.Resume;
    end;
    opRefreshListRemote:
    begin
      FAgentRemoteServer.Refresh;
      RemoteServers := FAgentRemoteServer.GetServers;
      FrmMain.RefreshListRemote(RemoteServers.List);
      FrmMain.StrGridServersRemote.visible := (RemoteServers.List.Count > 0);
      FrmMain.PanNoServersRemote.visible := (RemoteServers.List.Count = 0);
    end;
    opStartMachine:
    begin
      AtOperation(Sender, opStopThreads);
      FrmMain.SetMachineStatus(CommandInfo.Name, 'Starting');
      AtOperation(Sender, opDisableAllButtons);

      CommandInfo.Command:= opStartMachineCmd;
      CommandInfo.Title:= 'Starting server';
      CommandInfo.Content:= 'Please, wait...';
      ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, FAgentCommandThread, FAgentVM, nil);

      ControlCommandThreads.ControlCommandThread := ControlCommandThread;
      ControlCommandThreads.ControlCommandThreadRefresh := ControlCommandThreadRefresh;
      ControlCommandThreads.ControlCommandThreadConfigure := ControlCommandThreadConfigure;
      ControlWaitAtOperation.Execute(ControlCommandThreads);
    end;
    opStopMachine:
    begin
      AtOperation(Sender, opStopThreads);
      FrmMain.SetMachineStatus(CommandInfo.Name, 'Stopping');
      AtOperation(Sender, opDisableAllButtons);

      CommandInfo.Command:=opStopMachineCmd;

      SOCommand := TSOCommand.Create(FAgentPreferences);
      try
        SOCommand.Execute(FAgentCommandThread.GetCommand(CommandInfo), FAgentCommandThread.GetPath);
      finally
        SOCommand.Free;
      end;

      AtOperation(Sender, opStartThreads);
    end;
    opPauseMachine:
    begin
      AtOperation(Sender, opStopThreads);
      FrmMain.SetMachineStatus(CommandInfo.Name, 'Pausing');
      AtOperation(Sender, opDisableAllButtons);

      CommandInfo.Command := opSaveStateMachineCmd;
      CommandInfo.Title:= 'Pausing server';
      CommandInfo.Content:= 'Please, wait...';
      ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, FAgentCommandThread, FAgentVM, nil);

      ControlCommandThreads.ControlCommandThread := ControlCommandThread;
      ControlCommandThreads.ControlCommandThreadRefresh := ControlCommandThreadRefresh;
      ControlCommandThreads.ControlCommandThreadConfigure := ControlCommandThreadConfigure;
      ControlWaitAtOperation.Execute(ControlCommandThreads);
    end;
    opDeleteMachine:
    begin
      CommandInfo.Command:=opDeleteMachineCmd;
      AtOperation(Sender, opStopThreads);

      SOCommand := TSOCommand.Create(FAgentPreferences);
      try
        SOCommand.Execute(FAgentCommandThread.GetCommand(CommandInfo), FAgentCommandThread.GetPath);
      finally
        SOCommand.Free;
      end;

      AtOperation(Sender, opStartThreads);
    end;
    opChangeMachine:
    begin
      MachineList := TStringList.Create;
      MachineList.Text := FAgentVM.GetMachineListMvm;
      MachinesCount := MachineList.Count;
      MachineList.Free;

      IsRunningMachine := FAgentVM.IsRunning(FrmMain.MachineNameLocal);

      with FrmMain do
      begin
        if not BtnCreate.Enabled then BtnCreate.Enabled := true; MenuCreate.Enabled:= BtnCreate.Enabled;
        MenuOpen.Enabled := true and (MachinesCount > 0);
        MenuOpenPop.Enabled := MenuOpen.Enabled;
        BtnStart.Enabled := (not IsRunningMachine) and (MachinesCount > 0);
        MenuStart.Enabled := BtnStart.Enabled; MenuStartPop.Enabled := BtnStart.Enabled;
        BtnStop.Enabled := IsRunningMachine and (MachinesCount > 0);
        MenuStop.Enabled := BtnStop.Enabled; MenuStopPop.Enabled := BtnStop.Enabled;
//        BtnPause.Enabled := IsRunningMachine and (MachinesCount > 0);
//        MenuPause.Enabled := BtnPause.Enabled; MenuPausePop.Enabled := BtnPause.Enabled;
//        BtnRestart.Enabled := IsRunningMachine and (MachinesCount > 0);
//        MenuRestart.Enabled := BtnRestart.Enabled; MenuRestartPop.Enabled := BtnRestart.Enabled;
        BtnDeleteLocalServer.Enabled := not IsRunningMachine and (MachinesCount > 0);
        BtnDeleteRemoteServer.Enabled := StrGridServersRemote.RowCount > 1;
        BtnModifyRemoteServer.Enabled := StrGridServersRemote.RowCount > 1;
        MenuDelete.Enabled := (BtnDeleteLocalServer.Enabled and (ServersControl.ActivePage.Name = 'TabLocalServers')) or (BtnDeleteRemoteServer.Enabled and (ServersControl.ActivePage.Name = 'TabRemoteServers'));

        MenuImport.Enabled := FAgentVM.VirtualBoxExists;
        MenuExport.Enabled := FAgentVM.VirtualBoxExists;
//        MenuCheckForUpdates.Enabled:= FAgentVM.VirtualBoxExists;

        if PanLoading.Visible then PanLoading.Visible := false;
//        if (MachinesCount = 0) <> PanNoServers.Visible then
        PanNoServers.Visible:= (MachinesCount = 0);

        if (MachinesCount > 0) <> StrGridServersLocal.Visible then
        begin
          StrGridServersLocal.Visible := (MachinesCount > 0);
          if StrGridServersLocal.Focused then
            StrGridServersLocal.SetFocus;
        end;
      end;
    end;
    opDisableAllButtons:
    begin
      with FrmMain do
      begin
        BtnCreate.Enabled := false; MenuCreate.Enabled := BtnCreate.Enabled;
        BtnStart.Enabled := false;
        MenuStart.Enabled := BtnStart.Enabled; MenuStartPop.Enabled := BtnStart.Enabled;
        BtnStop.Enabled := false;
        MenuStop.Enabled := BtnStop.Enabled; MenuStopPop.Enabled := BtnStop.Enabled;
//        BtnPause.Enabled := false;
//        MenuPause.Enabled := BtnPause.Enabled; MenuPausePop.Enabled := BtnPause.Enabled;
//        BtnRestart.Enabled := false;
//        MenuRestart.Enabled := BtnRestart.Enabled; MenuRestartPop.Enabled := BtnRestart.Enabled;
        BtnDeleteLocalServer.Enabled := false;
//        BtnDeleteRemoteServer.Enabled := false;
//        BtnModifyRemoteServer.Enabled := false;
        MenuDelete.Enabled := (BtnDeleteLocalServer.Enabled and (ServersControl.ActivePage.Name = 'TabLocalServers')) or (BtnDeleteRemoteServer.Enabled and (ServersControl.ActivePage.Name = 'TabRemoteServers'));
        MenuImport.Enabled := false;
        MenuExport.Enabled := false;
//        MenuCheckForUpdates.Enabled:= false;
      end;
    end;
    opShowInfoMachineLocal:
    begin
      with FrmProperties do
      begin
        Clean;
        Add('Server name', CommandInfo.Name);
        Add('IP', CommandInfo.Domain);
        Add('Tomcat port', FAgentVM.GetTomcatPortMachine(FrmMain.MachineNameLocal));
        Add('Tomcat debug port', FAgentVM.GetTomcatDebugPortMachine(FrmMain.MachineNameLocal));
        Add('Mysql port', FAgentVM.GetMysqlPortMachine(FrmMain.MachineNameLocal));
        Add('SSH port', FAgentVM.GetSSHPortMachine(FrmMain.MachineNameLocal));
        Add('Deploy Service port', FAgentVM.GetDeployServicePortMachine(FrmMain.MachineNameLocal));
        Add('Logging Hub port', FAgentVM.GetLoggingHubPortMachine(FrmMain.MachineNameLocal));
        ShowModal;
      end;
    end;
    opShowInfoMachineRemote:
    begin
      with FrmProperties do
      begin
        Clean;
        Add('Host', FAgentRemoteServer.Get(FrmMain.MachineNameRemoteIndex).Host);
        Add('Port', FAgentRemoteServer.Get(FrmMain.MachineNameRemoteIndex).Port);
        Add('Deploy Service Port', FAgentRemoteServer.Get(FrmMain.MachineNameRemoteIndex).DeployServicePort);
        Add('Deploy Service Domain', FAgentRemoteServer.Get(FrmMain.MachineNameRemoteIndex).DeployServiceDomain);
        Add('DeployService User', FAgentRemoteServer.Get(FrmMain.MachineNameRemoteIndex).DeployServiceUser);
        Add('DeployService Password', '****');
        Add('Logging Host', FAgentRemoteServer.Get(FrmMain.MachineNameRemoteIndex).LoggingHost);
        Add('Logging Port', FAgentRemoteServer.Get(FrmMain.MachineNameRemoteIndex).LoggingPort);
        Add('Update Applications Full Url', FAgentRemoteServer.Get(FrmMain.MachineNameRemoteIndex).UpdateAppsFullUrl);
        ShowModal;
      end;
    end;
    opImportMachine:
    begin
      CommandInfo.Command:= opImportMachineCmd;

      if not FAgentVM.ExistsMachine(CommandInfo.Name) then
      begin
        if FAgentMain.IsValidFileName(CommandInfo.FileName) then
        begin
          CommandInfo.Title:= 'Create server';
          CommandInfo.Content := 'Please, wait...';
          ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, FAgentCommandThread, FAgentVM, nil);
          ControlCommandThreads.ControlCommandThread := ControlCommandThread;
          ControlCommandThreads.ControlCommandThreadRefresh := ControlCommandThreadRefresh;
          ControlCommandThreads.ControlCommandThreadConfigure := ControlCommandThreadConfigure;
          ControlWaitAtOperation.Execute(ControlCommandThreads);
        end
        else
          ShowError('Error: The directory where '+MonetServerTemplateName+' should not have special characters (such as accents).');
      end
      else
        ShowError('Error: The "'+FrmMain.MachineNameLocal+'" server already exists.');
    end;
    opExportMachine:
    begin
      CommandInfo.Command:= opExportMachineCmd;
      if not FAgentVM.IsRunning(FrmMain.MachineNameLocal) then
      begin
        if FAgentMain.IsValidFileName(CommandInfo.FileName) then
        begin
          if FileExists(FrmMain.MachineFileName) then
            DeleteFile(FrmMain.MachineFileName);

          CommandInfo.Title:= 'Export server';
          CommandInfo.Content:= 'Please, wait...';
          ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, FAgentCommandThread, FAgentVM, nil);
          ControlCommandThreads.ControlCommandThread := ControlCommandThread;
          ControlCommandThreads.ControlCommandThreadRefresh := ControlCommandThreadRefresh;
          ControlCommandThreads.ControlCommandThreadConfigure := ControlCommandThreadConfigure;
          ControlWaitAtOperation.Execute(ControlCommandThreads);
        end
        else
          ShowError('Error: The directory where '+MonetServerTemplateName+' should not have special characters (such as accents).');
      end
      else
        ShowError('Error: The "'+FrmMain.MachineNameLocal+'" server must be off.');
    end;
    opShowAbout:
    begin
      AtOperation(Sender, opStopThreads);
      FrmAbout.ShowModal;
      AtOperation(Sender, opStartThreads);
    end;
    opDownloadMSG: CheckMSG(true);
    opRestartServer:
    begin
      CommandInfo.Command := opRestartServersCmd;

      CommandInfo.Title:= 'Restarting server';
      CommandInfo.Content:= 'Please, wait...';
      ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, FAgentCommandThread, FAgentVM, nil);

      ControlCommandThreads.ControlCommandThread := ControlCommandThread;
      ControlCommandThreads.ControlCommandThreadRefresh := ControlCommandThreadRefresh;
      ControlCommandThreads.ControlCommandThreadConfigure := ControlCommandThreadConfigure;
      ControlWaitAtOperation.Execute(ControlCommandThreads);
    end;
    opFormShow: CheckUpdateMSG(true);
    opShowSpacesLocal:
    begin
//      AtOperation(Sender, opStopThreads);
      if FAgentVM.IsRunning(CommandInfo.Name) then
      begin
        ServerForm := FrmMain.FindForm(ServerLabelLocal + CommandInfo.Name);
        if ServerForm <> nil then
          ServerForm.Show
        else
        begin
          ControlServer := TControlServer.Create(CommandInfo.Name, true, FrmMain, FAgentPreferences);
          ControlServer.Execute(FAgentVM, FAgentCommandThread)
        end;
      end
      else
        ShowInformation('The server is not running. Start the server first.');
//      AtOperation(Sender, opStartThreads);
    end;
    opConfigureMachine:
    begin
      CommandInfo.Command:= opConfigureMachineCmd;
      ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, FAgentVM, nil);
      ControlCommandThread.Resume;
    end;
    opConfigureAllMachines:
    begin
      CommandInfo.Command:= opConfigureAllMachinesRunningCmd;
      ControlCommandThreadConfigure := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, FAgentVM, nil);
      ControlCommandThreadConfigure.Resume;
    end;
    opPreferences:
    begin
      AtOperation(Sender, opStopThreads);
      ControlPreferences.Execute;
      AtOperation(Self, opRefreshListRemote);
      AtOperation(Sender, opStartThreads);
    end;
    opChangeStatusRefresh: if ControlCommandThreadRefresh.StatusRefresh then AtOperation(Sender, opStopThreads) else AtOperation(Sender, opStartThreads);
    opStopThreads:
    begin
      if FAgentVM.VirtualBoxExists then ControlCommandThreadRefresh.StopRefresh;
    end;
    opStartThreads:
    begin
      if FAgentVM.VirtualBoxExists then ControlCommandThreadRefresh.StartRefresh;
    end;
    opAddRemoteServer:
    begin
      if FrmNewRemoteServer.FileConfig = '' then
        FrmNewRemoteServer.SetConfigFiles(FAgentRemoteServer.GetConfigFiles);

      FrmNewRemoteServer.ShowModal;
      if FrmNewRemoteServer.Result then
      begin
        ServerInfo.Id := FrmNewRemoteServer.Name;
        ServerInfo.Host:= FrmNewRemoteServer.Host;
        ServerInfo.Port:= FrmNewRemoteServer.Port;
        ServerInfo.DeployServicePort:= FrmNewRemoteServer.DeployServicePort;
        ServerInfo.DeployServiceDomain:= FrmNewRemoteServer.DeployServiceDomain;
        ServerInfo.DeployServiceUser:= FrmNewRemoteServer.DeployServiceUser;
        ServerInfo.DeployServicePassword:= FrmNewRemoteServer.DeployServicePassword;
        ServerInfo.LoggingHost := FrmNewRemoteServer.LoggingHost;
        ServerInfo.LoggingPort:= FrmNewRemoteServer.LoggingPort;
        ServerInfo.UpdateAppsFullUrl := FrmNewRemoteServer.UpdateAppsFullUrl;
        ServerInfo.FileConfig:= FrmNewRemoteServer.FileConfig;


        FAgentRemoteServer.Add(ServerInfo);
      end;
      FAgentRemoteServer.Refresh;
      RemoteServers := FAgentRemoteServer.GetServers;
      FrmMain.RefreshListRemote(RemoteServers.List);
      FrmMain.StrGridServersRemote.visible := (RemoteServers.List.Count > 0);
      FrmMain.PanNoServersRemote.visible := (RemoteServers.List.Count = 0);
      FrmNewRemoteServer.Clear;
    end;
    opDelRemoteServer:
    begin
      FAgentRemoteServer.Remove(FrmMain.MachineNameRemoteIndex);
      FrmMain.RefreshListRemote(FAgentRemoteServer.GetServers.List);
    end;
    opModifyRemoteServer:
    begin
      FrmNewRemoteServer.Clear;

      ServerInfo := FAgentRemoteServer.Get(FrmMain.MachineNameRemoteIndex);
      FrmNewRemoteServer.Name := ServerInfo.Id;
      FrmNewRemoteServer.Host := ServerInfo.Host;
      FrmNewRemoteServer.Port := ServerInfo.Port;
      FrmNewRemoteServer.DeployServicePort := ServerInfo.DeployServicePort;
      FrmNewRemoteServer.DeployServiceDomain := ServerInfo.DeployServiceDomain;
      FrmNewRemoteServer.DeployServiceUser := ServerInfo.DeployServiceUser;
      FrmNewRemoteServer.DeployServicePassword := ServerInfo.DeployServicePassword;
      FrmNewRemoteServer.LoggingHost := ServerInfo.LoggingHost;
      FrmNewRemoteServer.LoggingPort := ServerInfo.LoggingPort;
      FrmNewRemoteServer.UpdateAppsFullUrl := ServerInfo.UpdateAppsFullUrl;

      FrmNewRemoteServer.SetConfigFiles(FAgentRemoteServer.GetConfigFiles);
      FrmNewRemoteServer.FileConfig := ServerInfo.FileConfig;

      AtOperation(Sender, opAddRemoteServer);
    end;
    opChangeTabIndex: ControlPreferences.ChangeTabIndex(FrmMain.TabIndex);
    opShowSpacesRemote:
    begin;
      ServerForm := FrmMain.FindForm(ServerLabelRemote + FrmMain.MachineNameRemote);
      if ServerForm <> nil then
        ServerForm.Show
      else
      begin
        ControlServer := TControlServer.Create(IntToStr(FrmMain.MachineNameRemoteIndex), false, FrmMain, FAgentPreferences);
        ControlServer.Execute(FAgentVM, FAgentCommandThread);
      end;
    end;
  end;
end;

procedure TControlMain.CheckMSG(ShowMessageUpdate: boolean);
begin
  CheckExistsMSG;
  ProcessUpdateMSG(ShowMessageUpdate);
end;

procedure TControlMain.CheckExistsMSG;
begin
  if not FileExists(GetConfigDir + '/' + MonetServerTemplateFileName) then
  begin
    if ShowConfirmation(MonetServerTemplateName + ' ('+MonetServerTemplateShortName+') does not exists, '+MonetServerTemplateShortName+' it is necessary in order to create servers. Do you want to download?') = mrYes then
    begin
      ExecuteUpdateMSG;
    end;
  end;
end;

function TControlMain.CheckUpdateMSG(shownotification: boolean): boolean;
var
  RemoteMD5, LocalMD5: string;
begin
  FAgentPreferences.Log.Info('TControlMain.CheckUpdateMSG. Start procedure.');
  Result := false;
  RemoteMD5 := '';

  if FileExists(GetConfigDir + '/' + MonetServerTemplateFileName) then
  begin
    LocalMD5 := FAgentMain.GetLocalMSTemplateMD5(GetConfigDir + '/' + MonetServerTemplateFileNameMD5);
    if FAgentPreferences.UpdatesStart then
      RemoteMD5 := FAgentMain.GetRemoteMSTemplateMD5(FAgentMain.GetUrlDownloadMSTemplate +'/' + MonetServerTemplateFileNameMD5);

    FAgentPreferences.Log.Info('TControlMain.CheckUpdateMSG. Local md5: ' + LocalMD5);
    FAgentPreferences.Log.Info('TControlMain.CheckUpdateMSG. Remote md5: ' + RemoteMD5);

    if ((RemoteMD5 <> '') and (LocalMD5 <> RemoteMD5)) then
    begin
      Result := true;
      if shownotification then FrmMain.ShowPopup('There is a new update ' +MonetServerTemplateName + LineBreak + 'Please, update in Menu->Help->Check for updates...');
    end;
  end;

  FAgentPreferences.Log.Info('TControlMain.CheckUpdateMSG. Finish procedure.');
end;

procedure TControlMain.ExecuteUpdateMSG;
var
  CommandInfo: TCommandInfo;
  ControlCommandThread: TControlCommandThread;
  ControlCommandThreads: TControlCommandThreads;
  UrlDownloadTemplates: string;

begin
  UrlDownloadTemplates := FAgentMain.GetUrlDownloadMSTemplate;
  CommandInfo.FileName:= GetConfigDir + '/' + MonetServerTemplateFileName;
  CommandInfo.Url:= UrlDownloadTemplates +'/' + MonetServerTemplateFileName;
  CommandInfo.FileNameMD5:= GetConfigDir + '/' + MonetServerTemplateFileNameMD5;
  CommandInfo.UrlMD5:= UrlDownloadTemplates +'/' + MonetServerTemplateFileNameMD5;
  CommandInfo.Command:= opDownloadUpdatesCmd;
  CommandInfo.ShowModalWait:= false;

  FrmMain.MenuCheckForUpdates.Enabled:=false;
  CommandInfo.Title:= 'Download';
  CommandInfo.Content:= 'Downloading ' + MonetServerTemplateName +' (%Length/%Total)';
  CommandInfo.SubContent := 'Remaining time %Time...';
  ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, FAgentCommandThread, FAgentVM, nil);
  ControlCommandThread.OnMainFinishDownload:= @FinishDownload;

  ControlCommandThreads.ControlCommandThread := ControlCommandThread;
  ControlCommandThreads.ControlCommandThreadRefresh := ControlCommandThreadRefresh;
  ControlCommandThreads.ControlCommandThreadConfigure := ControlCommandThreadConfigure;

  ControlWaitCheckMSG.Execute(ControlCommandThreads);
end;

procedure TControlMain.ProcessUpdateMSG(ShowMessageUpdate: boolean);
var
  FileName: String;
begin
  if CheckUpdateMSG(false) then
  begin
    if ShowConfirmation('There is a new update ' +MonetServerTemplateName + '. Download?') = mrYes then
    begin
      ExecuteUpdateMSG;
    end;
  end
  else
  begin
    FileName := GetConfigDir + '/' + MonetServerTemplateFileName;
    if ShowMessageUpdate and FileExists(FileName) then ShowInformation(MonetServerTemplateName + ' is updated.');
  end
end;

procedure TControlMain.FinishDownload;
begin
  FrmMain.MenuCheckForUpdates.Enabled := true;
//  ControlCommandThreadRefresh.StartRefresh;
end;

end.

