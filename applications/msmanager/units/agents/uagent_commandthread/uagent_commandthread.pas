unit uagent_commandthread;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, utypes, uagent_preferences;

type
  TAgentCommandThread = class
    private
      FAgentPreferences: TAgentPreferences;

      function ProcessFileNameExe(FileName: string): string;
      function GetVBoxWindowsPath: string;
    public
      constructor Create(AgentPreferences: TAgentPreferences);

      function GetCommand(CommandInfo: TCommandInfo): string;
      function GetPath:string;
  end;

implementation

uses utools,registry,blcksock;

constructor TAgentCommandThread.Create(AgentPreferences: TAgentPreferences);
begin
  FAgentPreferences := AgentPreferences;
  FAgentPreferences.Log.Debug('TAgentCommandThread.GetVBoxWindowsPath. Path: ' + GetVBoxWindowsPath);
end;

function TAgentCommandThread.GetCommand(CommandInfo: TCommandInfo): string;
begin

  Result := 'VBoxManage ';
  if OsType = 'windows' then
    Result := 'VBoxManage.exe '
//    Result := '"'+ GetVBoxWindowsPath + 'VBoxManage.exe" '
  else
    if OsType = 'macos' then
      Result := '/usr/local/bin/VBoxManage ';
//    Result := ProcessFileNameExe(GetVBoxWindowsPath + 'VBoxManage.exe') + ' ';

  case CommandInfo.Command of
    opImportMachineCmd: begin
      Result := Result + 'import ' + '--vsys 0 --vmname ' + CommandInfo.Name + ' "' + CommandInfo.FileName + '"';
      if (OsType = 'linux') then writeln(Result);
    end;
    opExportMachineCmd: Result := Result + 'export ' + CommandInfo.Name + ' --output "' + CommandInfo.FileName + '"';
    opGetMachineListCmd: Result := Result + 'list vms';
    opGetRunMachineListCmd: Result := Result +'list runningvms';
    opStartMachineCmd: Result := Result +'startvm ' + CommandInfo.Name;
    opStopMachineCmd: Result := Result +'controlvm '+CommandInfo.Name+' poweroff';
    opSaveStateMachineCmd: Result := Result +'controlvm '+CommandInfo.Name+' savestate';
    opGetInfoMachineCmd: Result := Result +'showvminfo ' + CommandInfo.Name + '';
    opDeleteMachineCmd: Result := Result +'unregistervm '+CommandInfo.Name+' --delete';

    opInsertSSHRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 ssh,tcp,,'+CommandInfo.Port+',10.0.2.15,22';
    opInsertTomcatRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 tomcat,tcp,,'+CommandInfo.Port+',10.0.2.15,8080';
    opInsertDeployServiceRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 deployservice,tcp,,'+CommandInfo.Port+',10.0.2.15,4323';
    opInsertTomcatDebugRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 tomcatdebug,tcp,,'+CommandInfo.Port+',10.0.2.15,9010';
    opInsertMysqlRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 mysql,tcp,,'+CommandInfo.Port+',10.0.2.15,3306';
    opInsertLoggingHubRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 logginghub,tcp,,'+CommandInfo.Port+',10.0.2.15,58770';

    opDeleteSSHRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 delete ssh';
    opDeleteTomcatRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 delete tomcat';
    opDeleteDeployServiceRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 delete deployservice';
    opDeleteTomcatDebugRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 delete tomcatdebug';
    opDeleteMysqlRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 delete mysql';
    opDeleteLoggingHubRuleNet: Result := Result +'modifyvm '+CommandInfo.Name+' --natpf1 delete logginghub';

    opGetVersionVirtualBox: Result := Result +'--version';
  end;

  if OsType = 'windows' then
  begin
    Result := 'cmd /S /C ' + Result;
  end;
  FAgentPreferences.Log.Debug('TAgentCommandThread.GetCommand. Command: ' + Result);
end;

function TAgentCommandThread.ProcessFileNameExe(filename: string): string;
begin
  if OsType = 'windows' then
  begin
    Result := StringReplace(filename, '\', '"\"', [rfReplaceAll]);
    Result := StringReplace(Result, ':"\', ':\', [rfReplaceAll]);
    Result := Result + '"';
    Result := StringReplace(Result, '\""', '\', [rfReplaceAll]);
  end
  else Result := '"' + filename + '"';
end;

function TAgentCommandThread.GetVBoxWindowsPath: string;
var
  Reg: TRegistry;
  value: string;
begin
  Result := '';
  reg := TRegistry.Create(KEY_READ OR $0100);
  try
    Reg.RootKey := HKEY_LOCAL_MACHINE;

    if Reg.OpenKey('\SOFTWARE\Oracle\VirtualBox', false) then
    begin
      value := 'InstallDir';
      if Reg.ValueExists(value) then
        Result := Reg.ReadString(value);
      Reg.CloseKey;
    end;
  finally
    Reg.Free;
  end;
end;

function TAgentCommandThread.GetPath: string;
begin
  Result := '';
  if OsType = 'windows' then
    Result := GetVBoxWindowsPath;
end;

end.

