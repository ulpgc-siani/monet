unit uagent_vm;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, utypes, uagent_commandthread, uassocarray, uagent_preferences, usocommand;

type

  TAgentVM = class
    private
      FAgentPreferences: TAgentPreferences;
      FAgentCommandThread: TAgentCommandThread;
      FMachineList, FMachineListRunning, FMachineListPaused: string;
      SOCommand: TSOCommand;

      FTomcatPorts, FSSHPorts, FDeployServicePorts, FTomcatDebugPorts, FMysqlPorts, FLoggingHubPorts: TAssocArray;
      FNewTomcatPorts, FNewSSHPorts, FNewDeployServicePorts, FNewTomcatDebugPorts, FNewMysqlPorts, FNewLoggingHubPorts: TAssocArray;
      FExistsVirtualBox: integer;

      function GetPortMachine(search, name: string): string;
      function GetMachineList: string;
//      function IsPortOccuped(Port: string): boolean;

    public
      constructor Create(AgentPreferences: TAgentPreferences; AgentCommandThread: TAgentCommandThread);

      function GetTomcatPortMachine(name: string): string;
      function GetTomcatPortMachineFree: string;
      function GetSSHPortMachine(name: string): string;
      function GetSSHPortMachineFree: string;
      function GetDeployServicePortMachine(name: string): string;
      function GetDeployServicePortMachineFree: string;
      function GetTomcatDebugPortMachine(name: string): string;
      function GetTomcatDebugPortMachineFree: string;
      function GetMysqlPortMachine(name: string): string;
      function GetMysqlPortMachineFree: string;
      function GetLoggingHubPortMachine(name: string): string;
      function GetLoggingHubPortMachineFree: string;
      function ExistsMachine(name: string): boolean;

      function GetMachineListMvmInit: string;
      function GetRunMachineListInit: string;
      function GetPauseMachineListInit: string;

      function GetMachineListMvm: string;
      function GetRunMachineList: string;
      function GetPauseMachineList: string;

      function IsRunning(name: string): boolean;
      function IsPausedMachine(name: string): boolean;
      function VirtualBoxVersion: string;
      function VirtualBoxExists: boolean;

      procedure ConfigureFederationConfigMachine(TomcatPort, DeployServicePort: string);
      procedure ConfigureSSHConfigMachine(SSHPort, DeployServicePort: string);
      procedure ConfigureDeployServiceConfigMachine(DeployServicePort: string);
      procedure ConfigureTimeMachine(Time: string; DeployServicePort: string);
//      procedure RestartServer(DeployServicePort: string; debug: boolean = false);
//      procedure UpdateServers(DeployServicePort, Url: string);
//      procedure CleanUpServers(DeployServicePort: string);

      procedure Refresh;
  end;


implementation

uses blcksock, strutils, uconstants, utools,registry;


constructor TAgentVM.Create(AgentPreferences: TAgentPreferences; AgentCommandThread: TAgentCommandThread);
begin
  FAgentPreferences := AgentPreferences;
  FAgentCommandThread:= AgentCommandThread;
  SOCommand := TSOCommand.Create(FAgentPreferences);

  FTomcatPorts := TAssocArray.Create();
  FSSHPorts := TAssocArray.Create();
  FDeployServicePorts := TAssocArray.Create();
  FTomcatDebugPorts := TAssocArray.Create();
  FMysqlPorts := TAssocArray.Create();
  FLoggingHubPorts := TAssocArray.Create();

  FNewTomcatPorts := TAssocArray.Create();
  FNewSSHPorts := TAssocArray.Create();
  FNewDeployServicePorts := TAssocArray.Create();
  FNewTomcatDebugPorts := TAssocArray.Create();
  FNewMysqlPorts := TAssocArray.Create();
  FNewLoggingHubPorts := TAssocArray.Create();
  FExistsVirtualBox := -1;
end;

function TAgentVM.GetPortMachine(search, name: string): string;
var
  Info: TStringList;
  CommandInfo: TCommandInfo;
  x,i: integer;
  aux: string;

begin
  Result := '';
  Info := TStringList.Create;
  try
    CommandInfo.Command := opGetInfoMachineCmd;
    CommandInfo.Name := name;
//    Info.Text := FAgentCommandThread.ExecuteCommand(FAgentCommandThread.GetCommand(CommandInfo));
    Info.Text := SOCommand.Execute(FAgentCommandThread.GetCommand(CommandInfo), FAgentCommandThread.GetPath);

    x := 0;
    while (x < Info.Count) do
    begin
      aux := Info.Strings[x];
      i := Pos(search, aux);
      if (i > 0) then
      begin
        aux := copy(aux, i+length(search), length(search));
        Result := copy(aux, 1, pos(',',aux)-1);
        x := Info.Count;
      end;
      x := x + 1;
    end;

  finally
     Info.Free;
  end;
end;

function TAgentVM.IsPausedMachine(name: string): boolean;
var
  Info: TStringList;
  CommandInfo: TCommandInfo;
  x,i: integer;
  aux: string;
  search: string;
begin
  Result := false;
  Info := TStringList.Create;
  try
    CommandInfo.Command := opGetInfoMachineCmd;
    CommandInfo.Name := name;
    Info.Text := SOCommand.Execute(FAgentCommandThread.GetCommand(CommandInfo), FAgentCommandThread.GetPath);

    search := 'saved (since';
    x := 0;
    while (not Result) and (x < Info.Count) do
    begin
      aux := Info.Strings[x];
      i := Pos(search, aux);
      if (i > 0) then
        Result := true;
      x := x + 1;
    end;
  finally
     Info.Free;
  end;
end;


function TAgentVM.GetTomcatPortMachine(name: string): string;
begin
  Result := FTomcatPorts.Items[name];
end;

function TAgentVM.GetSSHPortMachine(name: string): string;
begin
  Result := FSSHPorts.Items[name];
end;

function TAgentVM.GetDeployServicePortMachine(name: string): string;
begin
  Result := FDeployServicePorts.Items[name];
end;

function TAgentVM.GetTomcatDebugPortMachine(name: string): string;
begin
  Result := FTomcatDebugPorts.Items[name];
end;

function TAgentVM.GetMysqlPortMachine(name: string): string;
begin
  Result := FMysqlPorts.Items[name];
end;

function TAgentVM.GetLoggingHubPortMachine(name: string): string;
begin
  Result := FLoggingHubPorts.Items[name];
end;

function TAgentVM.ExistsMachine(name: string): boolean;
var
  StringList: TStringList;
begin
  StringList := TStringList.Create;
  try
    StringList.Text := GetMachineListMvm;
    Result := (StringList.IndexOf(name) >=0);
  finally
     StringList.Free;
  end;
end;

function TAgentVM.GetTomcatPortMachineFree: string;
var
  StringList: TStringList;
  value, x: integer;
  name: string;
begin
  StringList := TStringList.Create;
  try
    StringList.Text := GetMachineListMvm;

    value := 8089;
    for x := 0 to StringList.Count-1 do
    begin
      Name := StringList.Strings[x];
      if StrToInt(GetTomcatPortMachine(name)) > value then
        value := StrToIntDef(GetTomcatPortMachine(name),0);
    end;

    repeat
      value := value+1;
    until not IsPortOccuped(FAgentPreferences.IP, IntToStr(value));
    Result := IntToStr(value);
  finally
     StringList.Free;
  end;
end;

function TAgentVM.GetSSHPortMachineFree: string;
var
  StringList: TStringList;
  value, x: integer;
  name: string;
begin
  StringList := TStringList.Create;
  try
    StringList.Text := GetMachineListMvm;

    value := 5019;
    for x := 0 to StringList.Count-1 do
    begin
      Name := StringList.Strings[x];
      if StrToIntDef(GetSSHPortMachine(name),0) > value then
        value := StrToInt(GetSSHPortMachine(name));
    end;

    repeat
      value := value+1;
    until not IsPortOccuped(FAgentPreferences.IP,IntToStr(value));
    Result := IntToStr(value);
  finally
     StringList.Free;
  end;
end;

function TAgentVM.GetDeployServicePortMachineFree: string;
var
  StringList: TStringList;
  value, x: integer;
  name: string;
begin
  StringList := TStringList.Create;
  try
    StringList.Text := GetMachineListMvm;

    value := 4319;
    for x := 0 to StringList.Count-1 do
    begin
      Name := StringList.Strings[x];
      if StrToIntDef(GetDeployServicePortMachine(name),0) > value then
        value := StrToInt(GetDeployServicePortMachine(name));
    end;

    repeat
      value := value+1;
    until not IsPortOccuped(FAgentPreferences.IP,IntToStr(value));
    Result := IntToStr(value);
  finally
     StringList.Free;
  end;
end;

function TAgentVM.GetTomcatDebugPortMachineFree: string;
var
  StringList: TStringList;
  value, x: integer;
  name: string;
begin
  StringList := TStringList.Create;
  try
    StringList.Text := GetMachineListMvm;

    value := 9009;
    for x := 0 to StringList.Count-1 do
    begin
      Name := StringList.Strings[x];
      if StrToIntDef(GetTomcatDebugPortMachine(name),0) > value then
        value := StrToInt(GetTomcatDebugPortMachine(name));
    end;

    repeat
      value := value+1;
    until not IsPortOccuped(FAgentPreferences.IP,IntToStr(value));
    Result := IntToStr(value);
  finally
     StringList.Free;
  end;
end;

function TAgentVM.GetMysqlPortMachineFree: string;
var
  StringList: TStringList;
  value, x: integer;
  name: string;
begin
  StringList := TStringList.Create;
  try
    StringList.Text:= GetMachineListMvm;

    value := 13299;
    for x := 0 to StringList.Count-1 do
    begin
      Name := StringList.Strings[x];
      if StrToIntDef(GetMysqlPortMachine(name),0) > value then
        value := StrToInt(GetMysqlPortMachine(name));
    end;

    repeat
      value := value+1;
    until not IsPortOccuped(FAgentPreferences.IP,IntToStr(value));
    Result := IntToStr(value);
  finally
     StringList.Free;
  end;
end;

function TAgentVM.GetLoggingHubPortMachineFree: string;
var
  StringList: TStringList;
  value, x: integer;
  name: string;
begin
  StringList := TStringList.Create;
  try
    StringList.Text:= GetMachineListMvm;

    value := 58770;
    for x := 0 to StringList.Count-1 do
    begin
      Name := StringList.Strings[x];
      if StrToIntDef(GetLoggingHubPortMachine(name),0) > value then
        value := StrToInt(GetLoggingHubPortMachine(name));
    end;

    repeat
      value := value+1;
    until not IsPortOccuped(FAgentPreferences.IP,IntToStr(value));
    Result := IntToStr(value);
  finally
     StringList.Free;
  end;
end;


function TAgentVM.GetMachineList: String;
var
  CommandInfo: TCommandInfo;
  x: integer;
  Name: string;
  StringList: TStringList;
  MachineList: TStringList;
begin
  Result := '';
  StringList := TStringList.Create;
  MachineList := TStringList.Create;
  try
    CommandInfo.Command:= opGetMachineListCmd;
    CommandInfo.Name := '';
    StringList.Text:= SOCommand.Execute(FAgentCommandThread.GetCommand(CommandInfo), FAgentCommandThread.GetPath);

    for x := 0 to StringList.Count-1 do
    begin
      Name := copy(StringList.Strings[x],1, Pos(' ',StringList.Strings[x])-1);
      Name := AnsiReplaceStr(Name, '"', '');
      MachineList.Add(Name);
      Result := Result + Name + LineBreak;
    end;
    MachineList.Sort;
    Result := MachineList.Text;
  finally
     StringList.Free;
     MachineList.Free;
  end;
end;

function TAgentVM.GetMachineListMvmInit: string;
var
  StringList: TStringList;
  Name: string;
  ListCount, x: integer;
  TomcatPort: string;
begin
  Result := '';
  TomcatPort := '';
  StringList := TStringList.Create;
  try
    FNewTomcatPorts.Clear;
    FNewSSHPorts.Clear;
    FNewDeployServicePorts.Clear;
    FNewTomcatDebugPorts.Clear;
    FNewMysqlPorts.Clear;
    FNewLoggingHubPorts.Clear;

    StringList.Text := GetMachineList;
    ListCount := StringList.Count;
    for x := 0 to ListCount-1 do
    begin
      Name := StringList.Strings[x];
      TomcatPort := GetPortMachine('tomcat, protocol = tcp, host ip = , host port = ', name);

      if TomcatPort <> '' then
      begin
        FNewTomcatPorts.Items[name] := TomcatPort;
        FNewSSHPorts.Items[name] := GetPortMachine('ssh, protocol = tcp, host ip = , host port = ', name);
        FNewDeployServicePorts.Items[name] := GetPortMachine('deployservice, protocol = tcp, host ip = , host port = ', name);
        FNewTomcatDebugPorts.Items[name] := GetPortMachine('tomcatdebug, protocol = tcp, host ip = , host port = ', name);
        FNewMysqlPorts.Items[name] := GetPortMachine('mysql, protocol = tcp, host ip = , host port = ', name);
        FNewLoggingHubPorts.Items[name] := GetPortMachine('logginghub, protocol = tcp, host ip = , host port = ', name);

        Result := Result + Name + LineBreak;
      end;
    end;
  finally
     StringList.Free;
  end;
end;

function TAgentVM.GetRunMachineListInit: string;
var
  StringList, MvmList: TStringList;
  CommandInfo: TCommandInfo;
  x: integer;
  Name: string;
begin
  Result := '';
  StringList := TStringList.Create;

  MvmList := TStringList.Create;
  MvmList.Text:= FMachineList;

  try
    CommandInfo.Command:= opGetRunMachineListCmd;
    CommandInfo.Name := '';
    StringList.Text := SOCommand.Execute(FAgentCommandThread.GetCommand(CommandInfo), FAgentCommandThread.GetPath);

    for x := 0 to StringList.Count-1 do
    begin
      Name := copy(StringList.Strings[x],1, Pos(' ',StringList.Strings[x])-1);
      Name := AnsiReplaceStr(Name, '"', '');

      if (MvmList.IndexOf(Name) >= 0) then
        Result := Result + Name + LineBreak;
    end;
  finally
     StringList.Free;
     MvmList.Free;
  end;
end;

function TAgentVM.GetPauseMachineListInit: string;
var
  StringList: TStringList;
  x: integer;
begin
  StringList := TStringList.Create;
  try
    StringList.Text :=  GetMachineListMvm;
    Result := '';

    for x := 0 to StringList.Count-1 do
    begin
      if IsPausedMachine(StringList.Strings[x]) then
        Result := Result + StringList.Strings[x] + LineBreak;
    end;
  finally
     StringList.Free;
  end;
end;

function TAgentVM.GetMachineListMvm: string;
begin
  Result := FMachineList;
end;

function TAgentVM.GetRunMachineList: string;
begin
  Result := FMachineListRunning;
end;

function TAgentVM.GetPauseMachineList: string;
begin
  Result := FMachineListPaused;
end;

function TAgentVM.IsRunning(name: string): boolean;
var
  StringList: TStringList;
begin
  StringList := TStringList.Create;
  try
    StringList.Text := GetRunMachineList;
    Result := (StringList <> nil) and (StringList.IndexOf(name) >= 0);
  finally
     StringList.Free;
  end;
end;

function TAgentVM.VirtualBoxVersion: string;
var
  CommandInfo: TCommandInfo;
  info: string;
  Reg: TRegistry;
  value: string;
begin
  Result := '';

  if OSType = 'windows' then
  begin
    reg := TRegistry.Create(KEY_READ OR $0100);
    try
      Reg.RootKey := HKEY_LOCAL_MACHINE;
      if Reg.OpenKey('\SOFTWARE\Oracle\VirtualBox', false) then
      begin
        value := 'VersionExt';
        if Reg.ValueExists(value) then
          Result := Reg.ReadString(value);
        Reg.CloseKey;
      end;
    finally
      Reg.Free;
    end;
  end
  else
  begin
    try
      CommandInfo.Command:= opGetVersionVirtualBox;
      CommandInfo.Name:= '';
      info := SOCommand.Execute(FAgentCommandThread.GetCommand(CommandInfo), FAgentCommandThread.GetPath);
      Result := info;
    except end;
  end;
end;

function TAgentVM.VirtualBoxExists: boolean;
//var
//  CommandInfo: TCommandInfo;
//  info: string;
//  Reg: TRegistry;
//  value: string;
begin
  Result := false;
  if FExistsVirtualBox = -1 then
  begin
    if OSType = 'windows' then
      Result := ((VirtualBoxVersion <> '') and (VirtualBoxVersion <> '4.3.8'))
    else
      Result := VirtualBoxVersion <> '';

    if Result then
      FExistsVirtualbox := 1
    else
      FExistsVirtualbox := 0;
  end
  else
  begin
    if FExistsVirtualbox = 1 then
      Result := true;
  end;
end;

procedure TAgentVM.Refresh;
var
  x: integer;
  IndexName: string;
begin
  FMachineList := GetMachineListMvmInit;

  for x := 0 to FNewTomcatPorts.Count-1 do
  begin
    IndexName := FNewTomcatPorts.IndexName[x];
    FTomcatPorts.Items[IndexName] := FNewTomcatPorts.Items[IndexName];
    FSSHPorts.Items[IndexName] := FNewSSHPorts.Items[IndexName];
    FDeployServicePorts.Items[IndexName] := FNewDeployServicePorts.Items[IndexName];
    FTomcatDebugPorts.Items[IndexName] := FNewTomcatDebugPorts.Items[IndexName];
    FMysqlPorts.Items[IndexName] := FNewMysqlPorts.Items[IndexName];
    FLoggingHubPorts.Items[IndexName] := FNewLoggingHubPorts.Items[IndexName];
  end;

  FMachineListRunning := GetRunMachineListInit;
  FMachineListPaused := GetPauseMachineListInit;
end;

procedure TAgentVM.ConfigureTimeMachine(time: string; DeployServicePort: string);
var
  commandid, command, result: string;
begin
  commandid := 'update_time';
  command := '<command id="'+commandid+'"><parameter id="user">admin</parameter><parameter id="password">1234</parameter><parameter id="time">'+time+'</parameter></command>';

  result := DeployServiceSendCommand(FAgentPreferences.IP, DeployServicePort, command);
  if not AnsiContainsStr(result, '<result id="'+commandid+'"><status>ok</status><caption /><content></content></result>') then
   raise Exception.Create('Error to update time in machine.'+#13#10+'Result: ' + result);
end;

procedure TAgentVM.ConfigureFederationConfigMachine(TomcatPort, DeployServicePort: string);
var
  commandid, command{, result}: string;
begin
  commandid := 'update_federation_config';
  command := '<command id="'+commandid+'"><parameter id="user">admin</parameter><parameter id="password">1234</parameter><parameter id="server">'+ ServerID +'</parameter><parameter id="container">'+ ContainerID +'</parameter><parameter id="federation-name">'+UriFederation+'</parameter><parameter id="federation-domain">'+FAgentPreferences.IP+'</parameter><parameter id="federation-port">'+TomcatPort+'</parameter></command>';

{  result := }DeployServiceSendCommand(FAgentPreferences.IP, DeployServicePort, command);
//  if not AnsiContainsStr(result, '<result id="'+commandid+'"><status>ok</status><caption /><content></content></result>') then
//   raise Exception.Create('Error to modify Federation config in machine.'+#13#10+'Result: ' + result);
end;

procedure TAgentVM.ConfigureSSHConfigMachine(SSHPort, DeployServicePort: string);
var
  commandid, command{, result}: string;
begin
  commandid := 'update_ssh_config';
  command := '<command id="'+commandid+'"><parameter id="user">admin</parameter><parameter id="password">1234</parameter><parameter id="server">'+ ServerID +'</parameter><parameter id="container">'+ ContainerID +'</parameter><parameter id="ssh-port">'+SSHPort+'</parameter></command>';

{  result := }DeployServiceSendCommand(FAgentPreferences.IP, DeployServicePort, command);
//  if not AnsiContainsStr(result, '<result id="'+commandid+'"><status>ok</status><caption /><content></content></result>') then
//     raise Exception.Create('Error to modify SSH config in machine.'+#13#10+'Result: ' + result);
end;

procedure TAgentVM.ConfigureDeployServiceConfigMachine(DeployServicePort: string);
var
  commandid, command{, result}: string;
begin
  commandid := 'update_deployservice_config';
  command := '<command id="'+commandid+'"><parameter id="user">admin</parameter><parameter id="password">1234</parameter><parameter id="server">'+ ServerID +'</parameter><parameter id="container">'+ ContainerID +'</parameter><parameter id="deployservice-port">'+DeployServicePort+'</parameter></command>';

{  result := }DeployServiceSendCommand(FAgentPreferences.IP, DeployServicePort, command);
//  if not AnsiContainsStr(result, '<result id="'+commandid+'"><status>ok</status><caption /><content></content></result>') then
//     raise Exception.Create('Error to modify DeployService config in machine.'+#13#10+'Result: ' + result);
end;

end.

