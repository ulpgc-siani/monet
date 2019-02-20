unit uagent_remoteserver;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, utypes, uagent_preferences, INIFiles;

type

  TAgentRemoteServer = class
    private
      FAgentPreferences: TAgentPreferences;
      RemoteServers: TRemoteServers;
      ConfigFiles: TStringList;

    public
      constructor Create(AgentPreferences: TAgentPreferences);

      procedure Refresh;

      procedure Add(ServerInfo: TRemoteServer);
      procedure Remove(Index: integer);

      function GetServers: TRemoteServers;
      function Get(Index: integer): TRemoteServer;
      function GetConfigFiles: TStringList;

      function GetDeployServicePortTemp(Host: string): string;
  end;


implementation

uses utools, uconstants;

constructor TAgentRemoteServer.Create(AgentPreferences: TAgentPreferences);
begin
  FAgentPreferences := AgentPreferences;
  ConfigFiles := TStringList.Create;
  ConfigFiles.Text:= FAgentPreferences.RemoteServersFile;
  FAgentPreferences.RemoteServersFile;

  Refresh;
end;

procedure TAgentRemoteServer.Add(ServerInfo: TRemoteServer);
var
  FConfig:TINIFile;
begin
  FConfig := TINIFile.Create(ServerInfo.FileConfig);
  FConfig.WriteString(ServerInfo.Id, 'Host', ServerInfo.Host);
  FConfig.WriteString(ServerInfo.Id, 'Port', ServerInfo.Port);
  FConfig.WriteString(ServerInfo.Id, 'DeployType', ServerInfo.DeployType);
  FConfig.WriteString(ServerInfo.Id, 'DeployServiceDomain', ServerInfo.DeployServiceDomain);
  FConfig.WriteString(ServerInfo.Id, 'DeployServicePort', ServerInfo.DeployServicePort);
  FConfig.WriteString(ServerInfo.Id, 'DeployServiceUser', ServerInfo.DeployServiceUser);
  FConfig.WriteString(ServerInfo.Id, 'DeployServicePassword', etxt(ServerInfo.DeployServicePassword, CodeCrypt, true));
  FConfig.WriteString(ServerInfo.Id, 'LoggingHost', ServerInfo.LoggingHost);
  FConfig.WriteString(ServerInfo.Id, 'LoggingPort', ServerInfo.LoggingPort);
  FConfig.WriteString(ServerInfo.Id, 'UpdateAppsFullUrl', ServerInfo.UpdateAppsFullUrl);
  Refresh;
end;

procedure TAgentRemoteServer.Remove(index: integer);
var
  FileConfig: string;
  Id: string;
  FConfig:TINIFile;
begin
  Id := RemoteServers.List.Names[index-1];
  FileConfig := RemoteServers.Locations[index-1];

  FConfig := TINIFile.Create(FileConfig);
  FConfig.EraseSection(Id);

  Refresh;
end;

function TAgentRemoteServer.GetServers: TRemoteServers;
begin
  Result := RemoteServers;
end;

procedure TAgentRemoteServer.Refresh;
var
  ResultList: TStringList;
  x,y: integer;
  FConfig:TINIFile;
  FileConfig: string;
  count: integer;

begin
  RemoteServers.List := TStringList.Create;
  RemoteServers.Locations := TStringList.Create;

  ConfigFiles.Text:= FAgentPreferences.RemoteServersFile;

  count := 1;
  for x := 0 to ConfigFiles.Count-1 do
  begin
    FileConfig := ConfigFiles.Strings[x];
    FConfig := TINIFile.Create(FileConfig);

    ResultList := TStringList.Create;
    try
      FConfig.ReadSections(ResultList);

      for y := 0 to ResultList.Count-1 do
      begin
        RemoteServers.List.Add(ResultList.Strings[y]+'='+IntToStr(count));
        RemoteServers.Locations.Add(FileConfig);
        inc(count);
      end;
    finally
      ResultList.Free;
    end;
    FConfig.Free;
  end;
end;

function TAgentRemoteServer.Get(Index: integer): TRemoteServer;
var
  FConfig:TINIFile;
  Id: string;
  FileConfig: string;
begin
  Id := RemoteServers.List.Names[index-1];
  FileConfig := RemoteServers.Locations[index-1];

  FConfig := TINIFile.Create(FileConfig);
  Result.Id:= Id;
  Result.Host:= FConfig.ReadString(Id, 'Host', '');
  Result.Port:= FConfig.ReadString(Id, 'Port', '');
  Result.DeployType:=FConfig.ReadString(Id, 'DeployType', 'production');
  Result.DeployServicePort:= FConfig.ReadString(Id, 'DeployServicePort', '');
  Result.DeployServiceDomain := FConfig.ReadString(Id, 'DeployServiceDomain', '');
  Result.DeployServiceUser:= FConfig.ReadString(Id, 'DeployServiceUser', '');
  Result.DeployServicePassword:= etxt(FConfig.ReadString(Id, 'DeployServicePassword', ''), CodeCrypt, false);
  Result.LoggingHost:= FConfig.ReadString(Id, 'LoggingHost', '');
  Result.LoggingPort:= FConfig.ReadString(Id, 'LoggingPort', '');
  Result.UpdateAppsFullUrl := FConfig.ReadString(Id, 'UpdateAppsFullUrl', '');
  Result.FileConfig:=FileConfig;
end;

function TAgentRemoteServer.GetConfigFiles: TStringList;
begin
  Result := ConfigFiles;
end;

function TAgentRemoteServer.GetDeployServicePortTemp(Host: string): string;
var
  value: integer;
begin
  value := 30000;
  repeat
    value := value+1;
  until not IsPortOccuped(Host, IntToStr(value));
  Result := IntToStr(value);
end;

end.

