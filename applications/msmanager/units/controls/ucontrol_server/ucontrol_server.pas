unit ucontrol_server;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, fserver, forms, uagent_server, ucontrol_wait, ucontrol_commandthread, uagent_vm, uagent_commandthread, uagent_preferences,uagent_remoteserver;

type
  TControlServer = class
    private
      FrmServer: TFrmServer;
      AFrmMain: TForm;
      FAgentServer: TAgentServer;
      FAgentRemoteServer: TAgentRemoteServer;
      FAgentVM: TAgentVM;
      FAgentCommandThread: TAgentCommandThread;
      FAgentPreferences: TAgentPreferences;
      ControlWait: TControlWait;
      ControlCommandThreadRefresh: TControlCommandThread;


      FServerName: string;
      FServerRemoteIndex: integer;
      FOldSelectedSpace: string;
      FRemoteUpdateAppsFullUrl: string;

      procedure AtOperation(Sender: TObject; Operation: TOperationSpaces);
      function Init: boolean;
    public
      constructor Create(ServerId: string; IsLocal: boolean; FrmMain: TForm; AgentPreferences: TAgentPreferences);
      destructor Destroy; override;

      procedure Execute(AgentVM: TAgentVM; AgentCommandThread: TAgentCommandThread);
end;

implementation

uses clipbrd, lclintf, utypes, dialogs, uconstants, utools, fmessage, controls, usocommand, fnewspace, strutils, finformation, flogin;

constructor TControlServer.Create(ServerId: string; IsLocal: boolean; FrmMain: TForm; AgentPreferences: TAgentPreferences);
begin
  FAgentPreferences := AgentPreferences;
  AFrmMain := FrmMain;
  Application.CreateForm(TFrmServer, FrmServer);
  FrmServer.OnOperation:= @AtOperation;
  FAgentServer := TAgentServer.Create(FAgentPreferences);
  FAgentRemoteServer := TAgentRemoteServer.Create(FAgentPreferences);
  ControlWait := TControlWait.Create(FrmMain);

  FServerName := ServerId;
  FServerRemoteIndex := -1;
  if not IsLocal then
  begin
    FServerRemoteIndex := StrToInt(ServerId);
    FServerName := FAgentRemoteServer.Get(FServerRemoteIndex).Id;
  end;

  FAgentServer.IsLocal := IsLocal;
  FRemoteUpdateAppsFullUrl := '';

  FrmServer.Caption := ServerLabelLocal;
  if not FAgentServer.IsLocal then FrmServer.Caption := ServerLabelRemote;
  FrmServer.Caption:= FrmServer.Caption +FServerName;
  FrmServer.LblServerName.Caption := FServerName;
end;

destructor TControlServer.Destroy;
begin
  FrmServer.Free;
  inherited;
end;

procedure TControlServer.Execute(AgentVM: TAgentVM; AgentCommandThread: TAgentCommandThread);
begin
  FAgentVM := AgentVM;
  FAgentCommandThread := AgentCommandThread;

  FOldSelectedSpace := '';
  ControlCommandThreadRefresh := nil;

  FrmServer.Show;
end;

procedure TControlServer.AtOperation(Sender: TObject; Operation: TOperationSpaces);
var
  SpacesCount: integer;
  CommandInfo: TCommandInfo;
  TextCopy: string;
  SpacesList: TStringList;
  FileName: string;
  ControlCommandThread: TControlCommandThread;
  CommandStr, ParametersStr: string;
  SOCommand: TSOCommand;
  TextInformation: string;

begin
  case Operation of
    opRefresh:
    begin
      CommandInfo.Command:= opRefreshSpacesListCmd;
      ControlCommandThreadRefresh := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, nil, FAgentServer);
      ControlCommandThreadRefresh.OnServerRefreshList:= @FrmServer.RefreshList;
      ControlCommandThreadRefresh.Resume;
    end;
    opCreateBusinessSpace:
    begin
      FrmNewSpace.Federations := FAgentServer.GetFederationsList;
      if FAgentServer.Port = '80' then
        FrmNewSpace.Url := 'http://'+FAgentServer.Host+'/'
      else
        if FAgentServer.Port = '443' then
          FrmNewSpace.Url := 'https://'+FAgentServer.Host+'/'
        else
          FrmNewSpace.Url := 'http://'+FAgentServer.Host+':'+FAgentServer.Port+'/';
      FrmNewSpace.ShowModal;
      if FrmNewSpace.Result then
      begin
        if VerifyName(FrmNewSpace.Name) then
        begin
          if (not FAgentServer.ExistsSpace(FrmNewSpace.Name)) or (FAgentServer.ExistsSpace(FrmNewSpace.Name) and (ShowConfirmation('The space "'+FrmNewSpace.Name+'" exists. Are you sure you want to overwrite it?', '', [mbYes, mbNo], mbNo) = mrYes)) then
          begin
            CommandInfo.Command:= opCreateSpaceCmd;
            CommandInfo.Title:= 'Create business space';
            CommandInfo.Content:= 'Please, wait...';
            CommandInfo.Name:= FrmNewSpace.Name;
            CommandInfo.Federation := FrmNewSpace.Federation;
            CommandInfo.Url:= FrmNewSpace.Url;
            AtOperation(Sender, opStopThreads);
            ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, nil, FAgentServer);
            ControlCommandThread.OnServerRefreshList:= @FrmServer.RefreshList;
            ControlWait.Execute(ControlCommandThread);
            AtOperation(Sender, opStartThreads);
          end;
        end
        else
          ShowError('You must put a correct name.');
      end;
    end;
    opDelete:
    begin
      AtOperation(Sender, opStopThreads);
      CommandInfo.Command:= opDestroySpaceCmd;
      CommandInfo.Title:= 'Delete business space';
      CommandInfo.Content:= 'Please, wait...';
      CommandInfo.Name:= FrmServer.SpaceName;
      ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, nil, FAgentServer);
      ControlCommandThread.OnServerRefreshList:= @FrmServer.RefreshList;
      ControlWait.Execute(ControlCommandThread);

      AtOperation(Sender, opStartThreads);
    end;
    opChange:
    begin
      SpacesList := TStringList.Create;
      try
        SpacesList.Text := FAgentServer.GetSpacesList;
        SpacesCount := SpacesList.Count;
      finally
        SpacesList.Free;
      end;

      with FrmServer do
      begin
        if PanLoading.Visible then PanLoading.Visible := false;
        if (not BtnRestart.Enabled) and (FAgentServer.LoggingHubHost <> '') and (FAgentServer.LoggingHubPort <> '')  then BtnOpenLog.Enabled := true;
        if not BtnRestart.Enabled then BtnRestart.Enabled := true;
        if not BtnRestartDebug.Enabled then BtnRestartDebug.Enabled := true;
        if not BtnFederation.Enabled then BtnFederation.Enabled := true;
        if not BtnUpdate.Enabled then BtnUpdate.Enabled := true;
        if not BtnCreateBusinessSpace.Enabled then BtnCreateBusinessSpace.Enabled :=true;
        if not BtnUploadDistribution.Enabled then BtnUploadDistribution.Enabled :=true;
        if not MenuOpenSSHConsolePop.Enabled then MenuOpenSSHConsolePop.Enabled := true;

        if (FAgentServer.DeployType <> 'production') then
          BtnDelete.Enabled := (SpacesCount > 0);
        MenuCopyDeployUri.Enabled := (SpacesCount > 0);
        BtnCleanUp.Enabled := (SpacesCount > 0);

        PanNoSpaces.Visible:= (SpacesCount = 0);
        StrGridSpaces.Visible:= (SpacesCount > 0);
      end;
    end;
    opCopyCodeToClipboard:
    begin
      TextCopy := #9 +'space '+FrmServer.SpaceName+' {' + LineBreak + #9#9 +'deploy-uri = "'+FAgentServer.GetSpaceUrl(FrmServer.SpaceName)+'";' + LineBreak + #9 +'}' + LineBreak;
      TextCopy := TextCopy +LineBreak+ #9 +'federation {' +LineBreak+ #9#9 + 'uri = "'+FAgentServer.GetSpaceFederationUrl(FrmServer.SpaceName)+'";' + LineBreak + #9#9 + 'secret = "1234";' + LineBreak + #9 +'}' + LineBreak;
      ClipBoard.AsText:= TextCopy;
    end;
    opOpenSpace:
    begin
      OpenURL(FAgentServer.GetSpaceUrl(FrmServer.SpaceName));
    end;
    opClose:
    begin
      if (ControlCommandThreadRefresh <> nil) then
      begin
        AtOperation(Sender, opStopThreads);
        ControlCommandThreadRefresh.CancelRefreshSpaces;
      end;
    end;
    opShow:
    begin
      if Init then
      begin
        with FrmServer do
        begin
          StrGridSpaces.Visible:= false;
          PanLoading.Visible := true;
          PanNoSpaces.Visible := false;
          BtnRestart.Enabled := false;
          BtnRestartDebug.Enabled := false;
          BtnFederation.Enabled := false;
          BtnUpdate.Enabled := false;
          BtnCreateBusinessSpace.Enabled:=false;
          BtnUploadDistribution.Enabled:=false;
          BtnOpenLog.Enabled:=false;
          BtnCleanUp.Visible := FAgentServer.IsLocal;
          BtnDelete.Enabled := FAgentServer.DeployType <> 'production';

          CleanProperties;
          AddProperty('Host', FAgentServer.Host);
          AddProperty('Port', FAgentServer.Port);
          if FAgentServer.IsLocal then AddProperty('SSH Port', FAgentServer.SSHPort);
          AddProperty('Deploy Service Domain', FAgentServer.DeployServiceDomain);
          AddProperty('Deploy Service User', FAgentServer.DeployServiceUser);
          AddProperty('Deploy Service Port', FAgentServer.DeployServicePort);

          if FAgentServer.IsLocal then
          begin
            AddProperty('Tomcat debug port', FAgentVM.GetTomcatDebugPortMachine(FServerName));
            AddProperty('Mysql port', FAgentVM.GetMysqlPortMachine(FServerName));
          end;
        end;
      end;
    end;
    opOpenFederation:
    begin
      if FAgentServer.Port = '80' then
        OpenURL('http://'+FAgentServer.Host+'/'+ UriFederation)
      else
        OpenURL('http://'+FAgentServer.Host+':'+FAgentServer.Port+'/'+ UriFederation);
    end;
    opUpdateServer:
    begin
      AtOperation(Sender, opStopThreads);

      if FAgentServer.UpdateDeployService(FAgentServer.GetUrlDownloadDeployService) then
      begin
        CommandInfo.Command:=opUpdateServersCmd;
        CommandInfo.Title:= 'Updating server';
        CommandInfo.Content:= 'Please, wait...';
        CommandInfo.DeployServicePort:= FAgentServer.DeployServicePort;
        CommandInfo.Url:= FAgentServer.GetUrlDownloadApps;

        if (not FAgentServer.IsLocal) and (FRemoteUpdateAppsFullUrl <> '') then
          CommandInfo.Url := FRemoteUpdateAppsFullUrl;

        ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, nil, FAgentServer);
        ControlWait.Execute(ControlCommandThread);
      end
      else
        ShowError('Service deployservice is restart. Please wait a few minutes.');

      AtOperation(Sender, opStartThreads);
    end;
    opCleanUpServer: begin
      AtOperation(Sender, opStopThreads);
      CommandInfo.Command:= opCleanUpServersCmd;
      CommandInfo.Title:= 'Delete all spaces';
      CommandInfo.Content:= 'Please, wait...';
      ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, nil, FAgentServer);
      ControlCommandThread.OnServerRefreshList:= @FrmServer.RefreshList;
      ControlWait.Execute(ControlCommandThread);
      AtOperation(Sender, opStartThreads);
    end;
    opSSHConsole:
    begin
      if FAgentVM.IsRunning(FServerName) or (not FAgentServer.IsLocal) then
      begin
        FileName := FAgentServer.GetSSHFile;
        SOCommand := TSOCommand.Create(FAgentPreferences);

        if OsType = 'windows' then
        begin
          CommandStr := '"'+ FAgentServer.GetJavaWindowsPath + '\bin\javaws.exe"';
          ParametersStr := '"' + ExtractFileName(FileName) + '"';
        end
        else
        begin
          CommandStr := 'javaws';
          ParametersStr := '"' + FileName + '"';
        end;

        FAgentPreferences.Log.Info('Operation: SSHConsole, Command: ' +CommandStr + ' ' + ParametersStr);
        try
          SOCommand.ExecuteSimple(CommandStr, ParametersStr, ExtractFilePath(FileName));
        finally
          SOCommand.Free;
        end;
      end;
    end;
    opRestartServer:
    begin
      AtOperation(Sender, opStopThreads);
      CommandInfo.Command := opRestartServersCmd;
      CommandInfo.Title:= 'Restarting server';
      CommandInfo.Content:= 'Please, wait...';
      CommandInfo.Name:=FServerName;
      CommandInfo.DeployServicePort:= FAgentServer.DeployServicePort;
      ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, nil, FAgentServer);
      ControlWait.Execute(ControlCommandThread);
      AtOperation(Sender, opStartThreads);
    end;
    opRestartServerDebug:
    begin
      AtOperation(Sender, opStopThreads);
      CommandInfo.Command := opRestartServersDebugCmd;
      CommandInfo.Title:= 'Restarting server in debug mode';
      CommandInfo.Content:= 'Please, wait...';
      CommandInfo.Name:=FServerName;
      CommandInfo.DeployServicePort:= FAgentServer.DeployServicePort;
      ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, nil, FAgentServer);
      ControlWait.Execute(ControlCommandThread);
      AtOperation(Sender, opStartThreads);
    end;
    opUploadDistribution:
    begin
    end;
    opCopyPropertyValueToClipboard: ClipBoard.AsText:= FrmServer.StrGridProperties.Cells[1, FrmServer.StrGridProperties.Row];
    opChangeStatusRefresh: if ControlCommandThreadRefresh.StatusRefresh then AtOperation(Sender, opStopThreads) else AtOperation(Sender, opStartThreads);
    opStopThreads: ControlCommandThreadRefresh.StopRefresh;
    opStartThreads: ControlCommandThreadRefresh.StartRefresh;
    opOpenLog:
    begin
      FileName := FAgentServer.GetLoggingHubFile(FServerName);
      if OsType = 'windows' then
      begin
        CommandStr := '"'+ FAgentServer.GetJavaWindowsPath + '\bin\javaw.exe"';
        ParametersStr := '-cp "' + ApplicationPath + 'vl-logging-frontend-1.1.45.jar" com.vertexlabs.logging.frontend.SwingFrontEnd';
      end
      else
      begin
        CommandStr := 'java';
        if OsType = 'macos' then
        begin
          ParametersStr := '-cp "' + ApplicationPath + '../../../vl-logging-frontend-1.1.45.jar" ' + ' com.vertexlabs.logging.frontend.SwingFrontEnd';
        end
        else
        begin
          ParametersStr := '-cp "' + ApplicationPath + 'vl-logging-frontend-1.1.45.jar" ' + ' com.vertexlabs.logging.frontend.SwingFrontEnd';
        end;
      end;
      SOCommand := TSOCommand.Create(FAgentPreferences);

      FAgentPreferences.Log.Info('Operation: OpenLog, Command: ' +CommandStr + ' ' + ParametersStr);
      try
        SOCommand.ExecuteSimple(CommandStr, ParametersStr, ExtractFilePath(FileName));
      finally
        SOCommand.Free;
      end;
    end;
    opShowInformation:
    begin
      TextInformation := StripHTML(DeployServiceSendCommandUrl(FAgentServer.GetSpaceUrl(FrmServer.SpaceName) + '/monet-info.jsp', true));
      TextInformation := Trim(StringReplace(TextInformation, '   Info'+#10, '', [rfReplaceAll]));

      FrmInformation.SetInfomation(TextInformation);

      FrmInformation.ShowModal;
    end;
    opRenewCertificate1y:
    begin
      AtOperation(Sender, opStopThreads);
      CommandInfo.Command := opRenewCertificateCmd;
      CommandInfo.Title:= 'Renew certificate';
      CommandInfo.Content:= 'Please, wait...';
      CommandInfo.Name:=FServerName;
      CommandInfo.DeployServicePort:= FAgentServer.DeployServicePort;
      CommandInfo.Days:=365;
      ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, nil, FAgentServer);
      ControlWait.Execute(ControlCommandThread);
      AtOperation(Sender, opStartThreads);
      AtOperation(Sender, opRestartServer);
    end;
    opRenewCertificate5y:
    begin
      AtOperation(Sender, opStopThreads);
      CommandInfo.Command := opRenewCertificateCmd;
      CommandInfo.Title:= 'Renew certificate';
      CommandInfo.Content:= 'Please, wait...';
      CommandInfo.Name:=FServerName;
      CommandInfo.DeployServicePort:= FAgentServer.DeployServicePort;
      CommandInfo.Days:=1825;
      ControlCommandThread := TControlCommandThread.Create(CommandInfo, FAgentPreferences, nil, nil, FAgentServer);
      ControlWait.Execute(ControlCommandThread);
      AtOperation(Sender, opStartThreads);
    end;
    opAllowDelete:
    begin;
      if (SpacesList.Count > 0) then
        FrmServer.BtnDelete.Enabled:=true;
    end;

  end;
end;

function TControlServer.Init:boolean;
var
  GetServers: string;
  LoginCancel: boolean;
begin
  Result := false;

  if FAgentServer.IsLocal then
  begin
    FAgentServer.Host:= FAgentPreferences.IP;
    FAgentServer.Port:=FAgentVM.GetTomcatPortMachine(FServerName);
    FAgentServer.DeployType := 'development';
    FAgentServer.DeployServiceDomain := 'localhost';
    FAgentServer.SSHPort:= FAgentVM.GetSSHPortMachine(FServerName);
    FAgentServer.DeployServicePort:= FAgentVM.GetDeployServicePortMachine(FServerName);
    FAgentServer.DeployServiceUser:= 'admin';
    FAgentServer.DeployServicePassword:= '1234';
    FAgentServer.LoggingHubHost:= 'localhost';
    FAgentServer.LoggingHubPort:= FAgentVM.GetLoggingHubPortMachine(FServerName);
    Result := true;
    AtOperation(Self, opRefresh);
  end
  else
  begin
    FAgentServer.Host := FAgentRemoteServer.Get(FServerRemoteIndex).Host;
    FAgentServer.Port := FAgentRemoteServer.Get(FServerRemoteIndex).Port;
    FAgentServer.DeployType := FAgentRemoteServer.Get(FServerRemoteIndex).DeployType;
    FAgentServer.DeployServicePort := FAgentRemoteServer.Get(FServerRemoteIndex).DeployServicePort;
    FAgentServer.DeployServiceDomain := FAgentRemoteServer.Get(FServerRemoteIndex).DeployServiceDomain;

    FAgentServer.DeployServiceUser:= FAgentRemoteServer.Get(FServerRemoteIndex).DeployServiceUser;
    FAgentServer.DeployServicePassword:= FAgentRemoteServer.Get(FServerRemoteIndex).DeployServicePassword;
    FAgentServer.LoggingHubHost:= FAgentRemoteServer.Get(FServerRemoteIndex).LoggingHost;
    FAgentServer.LoggingHubPort:= FAgentRemoteServer.Get(FServerRemoteIndex).LoggingPort;
    FRemoteUpdateAppsFullUrl := FAgentRemoteServer.Get(FServerRemoteIndex).UpdateAppsFullUrl;

    if not IsPortConnected(FAgentServer.DeployServiceDomain, FAgentServer.DeployServicePort) then
    begin
      AtOperation(Self, opClose);
      ShowError('I can''t connect to server. Please, check the configuration.');
      FrmServer.Close;
    end
    else
    begin

      GetServers := FAgentServer.GetServers;
      FAgentPreferences.Log.Info('TControlServer.Init: GetServers.' +LineBreak+ GetServers);

      FrmLogin.User := FAgentServer.DeployServiceUser;
      FrmLogin.Password := FAgentServer.DeployServicePassword;
      LoginCancel := false;

      while (AnsiContainsStr(GetServers, '<result id="invalid_user_or_password"><status></status><caption></caption><content></content></result>')) and (not LoginCancel) do
      begin
        LoginCancel := FrmLogin.ShowModal = mrCancel;

        FAgentServer.DeployServiceUser := FrmLogin.User;
        FAgentServer.DeployServicePassword := FrmLogin.Password;

        GetServers := FAgentServer.GetServers;
      end;

      if not (AnsiContainsStr(GetServers, '<result id="get_servers"><status>ok</status><caption></caption><content>')) then
      begin
        AtOperation(Self, opClose);
        ShowError('I can''t connect to server. Is server active?');
        FrmServer.Close;
      end
      else
      begin
        Result := true;
        AtOperation(Self, opRefresh);
      end;
    end;
  end;
end;

end.

