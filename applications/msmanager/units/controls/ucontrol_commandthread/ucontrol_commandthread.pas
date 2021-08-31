unit ucontrol_commandthread;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, uagent_commandthread, utypes, uagent_vm, httpsend, ExtCtrls, ComCtrls, uagent_server, uagent_preferences, usocommand;

type
    TMainRefreshListEvent = procedure(Info: TMainRefreshInfo) of Object;
    TMainUpdateFormEvent = procedure(Info: TMainUpdateFormInfo) of Object;
    TServerRefreshListEvent = procedure(Info: TServerRefreshInfo) of Object;
    TWaitHideEvent = procedure of Object;
    TWaitUpdateContentEvent = procedure(content: String; subcontent: String) of Object;
    TWaitBarStyleEvent = procedure(value: TProgressBarStyle) of Object;
    TWaitBarMaxEvent = procedure(value: integer) of Object;
    TWaitBarPositionEvent = procedure(value: integer) of Object;
    TWaitShowErrorEvent = procedure(value: string) of Object;
    TWaitShowInfoEvent = procedure(value: string) of Object;
    TMainFinishDownloadEvent = procedure of Object;

    TControlCommandThread = class(TThread)
    private
      ACommandInfo: TCommandInfo;
      MessageStatus: string;
      FAgentCommandThread: TAgentCommandThread;
      FAgentVM: TAgentVM;
      FAgentServer: TAgentServer;
      FAgentPreferences: TAgentPreferences;
      MainRefreshInfo: TMainRefreshInfo;
      MainUpdateFormInfo: TMainUpdateFormInfo;
      ServerRefreshInfo: TServerRefreshInfo;
      SOCommand: TSOCommand;

      FOnMainRefreshList: TMainRefreshListEvent;
      FOnMainUpdateForm: TMainUpdateFormEvent;
      FOnServerRefreshList: TServerRefreshListEvent;
      FOnWaitHide: TWaitHideEvent;
      FOnWaitUpdateContent: TWaitUpdateContentEvent;
      FOnWaitBarStyle: TWaitBarStyleEvent;
      FOnWaitBarMax: TWaitBarMaxEvent;
      FOnWaitBarPosition: TWaitBarPositionEvent;
      FOnWaitShowError: TWaitShowErrorEvent;
      FOnWaitShowInfo: TWaitShowInfoEvent;
      FOnMainFinishDownload: TMainFinishDownloadEvent;

      FUrl: string;
      FDownloadActive: boolean;
      FFirstRequest: Boolean;
      FCanDownload: Boolean;
      FHTTPSend: THTTPSend;
      FFileStream: TFileStream;
      FFileName: string;
      FTotalDownloadSize: Int64;
      FWaitUpdateContent: string;
      FWaitUpdateSubContent: string;
      FWaitBarStyle: TProgressBarStyle;
      FWaitBarMax: integer;
      FWaitBarPosition: integer;
      FWaitShowError: string;
      FWaitShowInfo: string;
      FTimeInit: Int64;
      FLastSecondsRemaining: Int64;
//      FTimeLast: Int64;
      FIsError: boolean;

      FStopRefresh: boolean;
      FCritialSecction: boolean;

      FRefreshSpaces: boolean;

      procedure NewMessageStatus(message: string);
      procedure ExecuteGeneric;
      procedure ExecuteImport;
      procedure ExecuteExport;
      procedure ExecuteRefreshMainList;
      procedure ExecuteCreateSpace;
      procedure ExecuteDestroySpace;
      procedure ExecuteCleanUpServers;
      procedure ExecuteUpdateServers;
      procedure ExecuteRefreshSpacesList;
      procedure ExecuteConfigureMachineTime;
      procedure ExecuteConfigureMachine;
      procedure ExecuteConfigureAllMachinesRunning;
      procedure ExecuteRestartServers;
      procedure ExecuteRestartServersDebug;
      procedure ExecuteRenewCertificate;

      procedure DownloadMSTemplate;

      function Download: boolean;

      procedure MainRefreshList;
      procedure MainUpdateForm;
      procedure MainFinishDownload;
      procedure ServerRefreshList;

      procedure WaitHide;
      procedure WaitUpdateText;
      procedure WaitBarStyle;
      procedure WaitBarMax;
      procedure WaitBarPosition;
      procedure WaitShowError;
      procedure WaitShowInfo;

    protected
      procedure Execute; override;
    public
      constructor Create(CommandInfo: TCommandInfo; AgentPreferences: TAgentPreferences; AgentCommandThread: TAgentCommandThread; AgentVM: TAgentVM; AgentServer: TAgentServer);

      procedure CancelDownload;
      procedure StopRefresh;
      procedure StartRefresh;
      function StatusRefresh: boolean;
      procedure CancelRefreshSpaces;

      property CommandInfo: TCommandInfo read ACommandInfo;

      property OnMainRefreshList: TMainRefreshListEvent read FOnMainRefreshList write FOnMainRefreshList;
      property OnMainUpdateForm: TMainUpdateFormEvent read FOnMainUpdateForm write FOnMainUpdateForm;
      property OnServerRefreshList: TServerRefreshListEvent read FOnServerRefreshList write FOnServerRefreshList;
      property OnWaitHide: TWaitHideEvent read FOnWaitHide write FOnWaitHide;
      property OnWaitUpdateText: TWaitUpdateContentEvent read FOnWaitUpdateContent write FOnWaitUpdateContent;
      property OnWaitBarStyle: TWaitBarStyleEvent read FOnWaitBarStyle write FOnWaitBarStyle;
      property OnWaitBarMax: TWaitBarMaxEvent read FOnWaitBarMax write FOnWaitBarMax;
      property OnWaitBarPosition: TWaitBarPositionEvent read FOnWaitBarPosition write FOnWaitBarPosition;
      property OnWaitShowError: TWaitShowErrorEvent read FOnWaitShowError write FOnWaitShowError;
      property OnWaitShowInfo: TWaitShowInfoEvent read FOnWaitShowInfo write FOnWaitShowInfo;
      property OnMainFinishDownload: TMainFinishDownloadEvent read FOnMainFinishDownload write FOnMainFinishDownload;
    end;

    TControlCommandThreads = record
      ControlCommandThread: TControlCommandThread;
      ControlCommandThreadRefresh: TControlCommandThread;
      ControlCommandThreadConfigure: TControlCommandThread;
    end;

implementation

uses utools,LCLType, LCLProc, LSMessages, LSHTTPSend, LSConsts, fileutil, strutils, uconstants, forms, fmessage, dialogs;

constructor TControlCommandThread.Create(CommandInfo: TCommandInfo; AgentPreferences: TAgentPreferences; AgentCommandThread: TAgentCommandThread; AgentVM: TAgentVM; AgentServer: TAgentServer);
begin
  inherited Create(true);
  Self.FreeOnTerminate := True;

  FAgentCommandThread := AgentCommandThread.Create(AgentPreferences);
  FAgentVM := AgentVM;
  FAgentServer := AgentServer;
  FAgentPreferences := AgentPreferences;
  SOCommand := TSOCommand.Create(FAgentPreferences);

  ACommandInfo := CommandInfo;
  MessageStatus := '';
  FreeOnTerminate := True;

  FHTTPSend := THTTPSend.Create;

  FIsError := false;
  FStopRefresh := false;
  FCritialSecction := false;
end;

procedure TControlCommandThread.Execute;
begin
  case ACommandInfo.Command of
    opImportMachineCmd: ExecuteImport;
    opExportMachineCmd: ExecuteExport;
    opRefreshMainListCmd: ExecuteRefreshMainList;
    opRefreshSpacesListCmd: ExecuteRefreshSpacesList;
    opDownloadUpdatesCmd: DownloadMSTemplate;
    opCreateSpaceCmd: ExecuteCreateSpace;
    opDestroySpaceCmd: ExecuteDestroySpace;
    opCleanUpServersCmd: ExecuteCleanUpServers;
    opUpdateServersCmd: ExecuteUpdateServers;
    opConfigureMachineCmd: ExecuteConfigureMachine;
    opConfigureAllMachinesRunningCmd: ExecuteConfigureAllMachinesRunning;
    opRestartServersCmd: ExecuteRestartServers;
    opRestartServersDebugCmd: ExecuteRestartServersDebug;
    opRenewCertificateCmd: ExecuteRenewCertificate;
  else
    ExecuteGeneric;
  end;
  Synchronize(@WaitHide);
end;

procedure TControlCommandThread.CancelDownload;
begin
  FIsError := true;
  FDownloadActive := false;
end;

procedure TControlCommandThread.WaitHide;
begin
  if Assigned(FOnWaitHide) then
    FOnWaitHide;
end;

procedure TControlCommandThread.MainFinishDownload;
begin
  if Assigned(FOnMainFinishDownload) then
    FOnMainFinishDownload;
end;

procedure TControlCommandThread.MainRefreshList;
begin
  if Assigned(FOnMainRefreshList) then
    FOnMainRefreshList(MainRefreshInfo);
end;

procedure TControlCommandThread.MainUpdateForm;
begin
  if Assigned(FOnMainUpdateForm) then
    FOnMainUpdateForm(MainUpdateFormInfo);
end;

procedure TControlCommandThread.ServerRefreshList;
begin
  if Assigned(FOnServerRefreshList) then
    FOnServerRefreshList(ServerRefreshInfo);
end;

procedure TControlCommandThread.WaitUpdateText;
begin
  if Assigned(FOnWaitUpdateContent) then
    FOnWaitUpdateContent(FWaitUpdateContent, FWaitUpdateSubContent);
end;

procedure TControlCommandThread.WaitBarStyle;
begin
  if Assigned(FOnWaitBarStyle) then
    FOnWaitBarStyle(FWaitBarStyle);
end;

procedure TControlCommandThread.WaitBarMax;
begin
  if Assigned(FOnWaitBarMax) then
    FOnWaitBarMax(FWaitBarMax);
end;

procedure TControlCommandThread.WaitBarPosition;
begin
  if Assigned(FOnWaitBarPosition) then
    FOnWaitBarPosition(FWaitBarPosition);
end;

procedure TControlCommandThread.WaitShowError;
begin
  if Assigned(FOnWaitShowError) then
    FOnWaitShowError(FWaitShowError);
end;

procedure TControlCommandThread.WaitShowInfo;
begin
  if Assigned(FOnWaitShowInfo) then
    FOnWaitShowInfo(FWaitShowInfo);
end;

procedure TControlCommandThread.NewMessageStatus(message: string);
begin
  MessageStatus := message;
end;

procedure TControlCommandThread.ExecuteGeneric;
begin
  SOCommand.Execute(FAgentCommandThread.GetCommand(ACommandInfo), FAgentCommandThread.GetPath);
end;

procedure TControlCommandThread.ExecuteImport;
var
  TomcatPort, SSHPort, DeployServicePort, TomcatDebugPort, MysqlPort, LoggingHubPort: string;
  x: integer;
begin
  try
    with FAgentCommandThread do
    begin
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);

      // Delete old ports
      ACommandInfo.Command:= opDeleteTomcatRuleNet;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
      ACommandInfo.Command:= opDeleteSSHRuleNet;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
      ACommandInfo.Command:= opDeleteDeployServiceRuleNet;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
      ACommandInfo.Command:=opDeleteTomcatDebugRuleNet;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
      ACommandInfo.Command := opDeleteMysqlRuleNet;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
      ACommandInfo.Command := opDeleteLoggingHubRuleNet;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);

      TomcatPort := FAgentVM.GetTomcatPortMachineFree;
      SSHPort := FAgentVM.GetSSHPortMachineFree;
      DeployServicePort := FAgentVM.GetDeployServicePortMachineFree;
      TomcatDebugPort := FAgentVM.GetTomcatDebugPortMachineFree;
      MysqlPort := FAgentVM.GetMysqlPortMachineFree;
      LoggingHubPort := FAgentVM.GetLoggingHubPortMachineFree;

      ACommandInfo.Command:= opInsertTomcatRuleNet;
      ACommandInfo.Port:= TomcatPort;
      x := 0;
      repeat
        SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
        inc(x);
        sleep(500);
        FAgentVM.Refresh;
      until (FAgentVM.GetTomcatPortMachine(ACommandInfo.Name) <> '') or (x > 120);

      ACommandInfo.Command:= opInsertSSHRuleNet;
      ACommandInfo.Port:= SSHPort;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
      ACommandInfo.Command:= opInsertDeployServiceRuleNet;
      ACommandInfo.Port:= DeployServicePort;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
      ACommandInfo.Command:= opInsertTomcatDebugRuleNet;
      ACommandInfo.Port:= TomcatDebugPort;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
      ACommandInfo.Command:=opInsertMysqlRuleNet;
      ACommandInfo.Port:= MysqlPort;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
      ACommandInfo.Command:=opInsertLoggingHubRuleNet;
      ACommandInfo.Port:= LoggingHubPort;
      SOCommand.Execute(GetCommand(ACommandInfo), GetPath);
    end;
  except
    on E: Exception do
      begin
        FWaitShowError := 'Error to import server.'+#13#10#13#10+'Status: ' + MessageStatus + #13#10 + E.Message;
        Synchronize(@WaitShowError);
      end;
  end;
end;

procedure TControlCommandThread.ExecuteExport;
var
  MD5: string;
  FileMD5: TSTringList;
begin
  SOCommand.Execute(FAgentCommandThread.GetCommand(ACommandInfo), FAgentCommandThread.GetPath);

  MD5 := GetFileNameMD5(ACommandInfo.FileName);
  FileMD5 := TStringList.Create;
  FileMD5.Add(MD5);
  FileMD5.SaveToFile(ACommandInfo.FileName + '.md5');
  FileMD5.Free;

  if OsType <> 'windows' then
    SOCommand.Execute('chmod a+rw "'+ ACommandInfo.FileName + '"', ExtractFilePath(ACommandInfo.FileName));

end;

procedure TControlCommandThread.ExecuteRefreshMainList;
begin
  try
    repeat
      MainUpdateFormInfo.Active:= true;
      if not FStopRefresh and (Application.Active) then
      begin
        FCritialSecction := true;
        FAgentVM.Refresh;

        MainRefreshInfo.MachineList := FAgentVM.GetMachineListMvm;
        MainRefreshInfo.MachineListRunning := FAgentVM.GetRunMachineList;
        MainRefreshInfo.MachineListPaused := FAgentVM.GetPauseMachineList;
        Synchronize(@MainRefreshList);

        MainUpdateFormInfo.Active:= true;
        FCritialSecction := false;
      end
      else MainUpdateFormInfo.Active:= false;

      Synchronize(@MainUpdateForm);
      Sleep(500);
    until 0<>0;
  except
  end;
end;

procedure TControlCommandThread.DownloadMSTemplate;
var
  isDownload: boolean;
begin
  try
    isDownload := false;
    if not FileExists(GetConfigDir) then
      ForceDirectories(GetConfigDir);

    FUrl := ACommandInfo.UrlMD5;
    FFileName := ACommandInfo.FileNameMD5 + '.temp';
    FFirstRequest := True;
    isDownload := Download;

    if isDownload then
    begin
        FUrl := ACommandInfo.Url;
        FFileName := ACommandInfo.FileName + '.temp';
        FFirstRequest := True;
        isDownload := Download;

        if isDownload then
        begin
          DeleteFile(ACommandInfo.FileNameMD5);
          RenameFile(ACommandInfo.FileNameMD5 + '.temp', ACommandInfo.FileNameMD5);

          DeleteFile(ACommandInfo.FileName);
          RenameFile(ACommandInfo.FileName + '.temp', ACommandInfo.FileName);
        end;
    end;
  finally
    Synchronize(@MainFinishDownload);
  end;
end;

function TControlCommandThread.Download: boolean;

  function _FormatHTTPError(const S: string): string;
  begin
    Result := Format(SLSHTTPSendUnableToGetError, [S]);
  end;

  function _DownloadCompleteMsg(const AURL, AFileName: string): string;
  begin
     Result := Format(SLSHTTPSendDownloadCompleteMsg, [AURL, AFileName]);
  end;

  function _TotalDownloadSizeMsg(const ASize: Int64): string;
  begin
    Result := Format(SLSHTTPSendTotalDownloadSizeMsg, [ASize]);
  end;

const
  {$IFDEF Windows}
    CDownloadKBBySec = 500 * 1024; // Use High(Int64) to download direct.
//    CDownloadKBBySec = 1000 * 1024; // Use High(Int64) to download direct.
//    CDownloadKBBySec = 100000 * 1024;
  {$ELSE}
//      CDownloadKBBySec = High(Int64); // Use High(Int64) to download direct.
     CDownloadKBBySec = 100000 * 1024;
  {$ENDIF}

var
  S: string;
  VFileSize, VContentLength: Int64;
  VOldNameValueSeparator: Char;
begin
  try
    Result := false;
    FCanDownload := true;
    FDownloadActive := true;

    FWaitBarStyle := pbstNormal;
    Synchronize(@WaitBarStyle);

    FWaitUpdateContent := ACommandInfo.Content;
    FWaitUpdateContent := AnsiReplaceText(FWaitUpdateContent, '%Length', '0');
    FWaitUpdateContent := AnsiReplaceText(FWaitUpdateContent, '%Total', '?');
    Synchronize(@WaitUpdateText);

    FAgentPreferences.Log.Info('TControlCommandThread.Download. Start download "'+FUrl+'"');


    while FDownloadActive do
    begin
      try
        FCanDownload := False;
        FHTTPSend.Clear;
        VOldNameValueSeparator := FHTTPSend.Headers.NameValueSeparator;
        FHTTPSend.Headers.NameValueSeparator := ':';
        if FFirstRequest then
        begin
          DeleteFile(FFileName);
          FHTTPSend.RangeEnd := 1;
          FTimeInit := Trunc((Now - EncodeDate(1970, 1 ,1)) * 24 * 60 * 60);
          FLastSecondsRemaining := -1;
          if LSShotMethod(FHTTPSend, CLSHTTPSendGETMethod, FUrl, False) then
          begin
            if FHTTPSend.ResultCode = 200 then
            begin
              FDownloadActive := False;
              FFileStream := TFileStream.Create(FFileName, fmCreate);
              try
                FFileStream.CopyFrom(FHTTPSend.Document, FHTTPSend.Document.Size);
                FWaitBarMax := 100;
                FWaitBarPosition := 100;
                Synchronize(@WaitBarMax);
                Synchronize(@WaitBarPosition);
              finally
                FFileStream.Free;
              end;
              Result := true;
            end
            else
            begin
              if FHTTPSend.ResultCode = 206 then
              begin
                FFirstRequest := False;
                S := FHTTPSend.Headers.Values['content-range'];
                FTotalDownloadSize := StrToInt64Def(GetPart('/', '', S, False, False), 0);
                FCanDownload := True;

                FWaitUpdateContent := ACommandInfo.Content;
                FWaitUpdateContent := AnsiReplaceText(FWaitUpdateContent, '%Length', FormatByteSize(0));
                FWaitUpdateContent := AnsiReplaceText(FWaitUpdateContent, '%Total', FormatByteSize(FTotalDownloadSize));
                Synchronize(@WaitUpdateText);

                FWaitBarMax := FTotalDownloadSize;
                Synchronize(@WaitBarMax);
              end
              else
              begin
                FDownloadActive := False;
                FWaitShowError := _FormatHTTPError(FURL);
                Synchronize(@WaitShowError);
                FIsError := true;
              end;
            end;
          end
          else
          begin
            FDownloadActive := False;
            FWaitShowError := _FormatHTTPError(FURL);
            Synchronize(@WaitShowError);
            FIsError := true;
          end;
        end
        else
        begin
          if FileExists(FFileName) then
            FFileStream := TFileStream.Create(FFileName, fmOpenReadWrite)
          else
            FFileStream := TFileStream.Create(FFileName, fmCreate);
          try
            VFileSize := FFileStream.Size;
            if VFileSize >= FTotalDownloadSize then
            begin
              FDownloadActive := False;
              Result := true;
            end
            else
            begin
              FFileStream.Position := VFileSize;
              FHTTPSend.RangeStart := VFileSize;

              if VFileSize + CDownloadKBBySec < FTotalDownloadSize then
                FHTTPSend.RangeEnd := VFileSize + CDownloadKBBySec;
              if LSShotMethod(FHTTPSend, CLSHTTPSendGETMethod, FURL, False) then
              begin
                if FHTTPSend.ResultCode = 200 then
                begin
                  FDownloadActive := False;
                  FFileStream.CopyFrom(FHTTPSend.Document, FHTTPSend.Document.Size);
                  FWaitBarMax := 100;
                  FWaitBarPosition := 100;
                  Synchronize(@WaitBarMax);
                  Synchronize(@WaitBarPosition);
                  Result := true;
                end
                else
                begin
                  if FHTTPSend.ResultCode = 206 then
                  begin
                    VContentLength := VFileSize;
                    VContentLength += StrToInt64Def(FHTTPSend.Headers.Values['content-length'], 0);

                    FFileStream.CopyFrom(FHTTPSend.Document, FHTTPSend.RangeEnd);
                    FWaitUpdateContent := ACommandInfo.Content;
                    FWaitUpdateContent := AnsiReplaceText(FWaitUpdateContent, '%Length', FormatByteSize(VContentLength));
                    FWaitUpdateContent := AnsiReplaceText(FWaitUpdateContent, '%Total', FormatByteSize(FTotalDownloadSize));

                    Synchronize(@WaitUpdateText);
                    FWaitBarPosition := VContentLength;
                    Synchronize(@WaitBarPosition);
                    FCanDownload := True;
                  end
                  else
                  begin
                    FDownloadActive := False;
                    FWaitShowError := _FormatHTTPError(FURL +LineBreak+ 'ResultCode: ' + IntToStr(FHTTPSend.ResultCode) +LineBreak+ 'Head:'+LineBreak+ FHTTPSend.Headers.Text);
                    Synchronize(@WaitShowError);
                    FIsError := true;
                  end;
                end;
              end
              else
              begin
                FDownloadActive := False;
                FWaitShowError := _FormatHTTPError(FURL);
                Synchronize(@WaitShowError);
                FIsError := true;
              end;
            end;
          finally
            FFileStream.Free;
          end;
        end;
      finally
        FHTTPSend.Headers.NameValueSeparator := VOldNameValueSeparator;
      end;
    end;

    FWaitBarStyle := pbstMarquee;
    Synchronize(@WaitBarStyle);

    if FIsError then DeleteFile(FFileName);
    FAgentPreferences.Log.Info('TControlCommandThread.Download. End download "'+FUrl+'"');

  except
    on E: Exception do
      begin
        FWaitShowError := E.Message;
        Synchronize(@WaitShowError);
      end;
  end;
end;

procedure TControlCommandThread.ExecuteCreateSpace;
begin
  try
    FAgentServer.CreateSpace(ACommandInfo.Name, ACommandInfo.Federation, ACommandInfo.Url);
  except
    on E: Exception do
      begin
        FWaitShowError := E.Message;
        Synchronize(@WaitShowError);
      end;
  end;


end;

procedure TControlCommandThread.ExecuteDestroySpace;
begin
  FAgentServer.DeleteSpace(ACommandInfo.Name);
end;

procedure TControlCommandThread.ExecuteCleanUpServers;
begin
  FAgentServer.CleanUpServers;
end;

procedure TControlCommandThread.ExecuteRefreshSpacesList;
begin
  FRefreshSpaces := true;
  FAgentServer.Refresh(true);
  repeat
    if not FStopRefresh and (Application.Active) then
    begin
      FAgentServer.Refresh;
      ServerRefreshInfo.SpacesList := FAgentServer.GetSpacesList;
      ServerRefreshInfo.UsersList:= FAgentServer.GetUsersList;
      ServerRefreshInfo.DeployServiceVersion:= FAgentServer.GetDeployServiceVersion;
      ServerRefreshInfo.MetaModelVersion:= FAgentServer.GetMetaModelVersion;
      Synchronize(@ServerRefreshList);
    end;
    Sleep(500);
  until (0<>0) or not FRefreshSpaces;
end;

procedure TControlCommandThread.ExecuteUpdateServers;
begin
  try
    FAgentServer.UpdateServer(ACommandInfo.Url);
  except
    on E: Exception do
      begin
        FWaitShowError := E.Message;
        Synchronize(@WaitShowError);
      end;
  end;
end;

procedure TControlCommandThread.StopRefresh;
begin
  FStopRefresh := true;
  while FCritialSecction do Application.ProcessMessages;
end;

procedure TControlCommandThread.StartRefresh;
begin
  FStopRefresh := false;
end;

function TControlCommandThread.StatusRefresh: boolean;
begin
  Result := not FStopRefresh;
end;

procedure TControlCommandThread.ExecuteConfigureMachineTime;
begin
  try
    FAgentVM.ConfigureTimeMachine(ACommandInfo.Time, FAgentVM.GetDeployServicePortMachine(ACommandInfo.Name));
  except end;
end;

procedure TControlCommandThread.ExecuteConfigureMachine;
begin
  try
    FAgentVM.ConfigureFederationConfigMachine(FAgentVM.GetTomcatPortMachine(ACommandInfo.Name), FAgentVM.GetDeployServicePortMachine(ACommandInfo.Name));
    FAgentVM.ConfigureSSHConfigMachine(FAgentVM.GetSSHPortMachine(ACommandInfo.Name), FAgentVM.GetDeployServicePortMachine(ACommandInfo.Name));
    FAgentVM.ConfigureDeployServiceConfigMachine(FAgentVM.GetDeployServicePortMachine(ACommandInfo.Name));
  except end;
end;

procedure TControlCommandThread.ExecuteConfigureAllMachinesRunning;
var
  MachineList: TStringList;
  MachineName: string;
  x: integer;
  AgentServer: TAgentServer;
  FederationDomain: string;
  timelocal, timeserver: string;
begin
  try
    repeat
      if not FStopRefresh then
      begin
        if FAgentPreferences.IsInactiveNetwork then
          FAgentPreferences.IP := FAgentPreferences.MainIp;

        MachineList := TStringList.Create;
        try
          MachineList.Text := FAgentVM.GetRunMachineList;

          for x := 0 to MachineList.Count -1 do
          begin
            MachineName := MachineList.Strings[x];

            AgentServer := TAgentServer.Create(FAgentPreferences);
            AgentServer.DeployServiceUser:= 'admin';
            AgentServer.DeployServicePassword:= '1234';
            AgentServer.DeployServiceDomain:='localhost';
            AgentServer.DeployServicePort:= FAgentVM.GetDeployServicePortMachine(MachineName);
            AgentServer.Refresh;
            FederationDomain := AgentServer.GetFederationDomain;
            TimeServer := AgentServer.GetDeployServiceTime;
            AgentServer.Free;

            if (FederationDomain <> FAgentPreferences.IP) then
            begin
              ACommandInfo.Name := MachineName;
              ExecuteConfigureMachine;
            end;

            timelocal := FormatDateTime('MMDDhhnnYYYY', Now);
            if (timeserver <> '') and (timeserver <> timelocal) then
            begin
              ACommandInfo.Name := MachineName;
              ACommandInfo.Time := FormatDateTime('MMDDhhnnYYYY', Now);
              ExecuteConfigureMachineTime;
            end;
          end;
        finally
          MachineList.Free;
        end;
      end;
      Sleep(1000);
    until 0<>0;
  except
  end;

end;

procedure TControlCommandThread.CancelRefreshSpaces;
begin
  FRefreshSpaces := false;
end;

procedure TControlCommandThread.ExecuteRestartServers;
begin
  try
    FAgentServer.RestartServer('normal');
  except
    on E: Exception do
      begin
        FWaitShowError := E.Message;
        Synchronize(@WaitShowError);
      end;
  end;
end;

procedure TControlCommandThread.ExecuteRestartServersDebug;
begin
  try
    FAgentServer.RestartServer('debug');
  except
    on E: Exception do
      begin
        FWaitShowError := E.Message;
        Synchronize(@WaitShowError);
      end;
  end;
end;

procedure TControlCommandThread.ExecuteRenewCertificate;
begin
  try
    FAgentServer.RenewCertificate(ACommandInfo.Days);
  except
    on E: Exception do
      begin
        FWaitShowError := E.Message;
        Synchronize(@WaitShowError);
      end;
  end;
end;

end.

