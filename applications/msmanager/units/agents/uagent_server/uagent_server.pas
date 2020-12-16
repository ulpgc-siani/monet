unit uagent_server;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, uagent_preferences;

type

  TAgentServer = class
    private
      FAgentPreferences: TAgentPreferences;
      FHost: string;
      FPort: string;
      FDeployType: string;
      FDeployServicePort: string;
      FDeployServiceUser: string;
      FDeployServicePassword: string;
      FSSHPort: string;
      FDeployServiceDomain: string;
      FLoggingHubHost: string;
      FLoggingHubPort: string;
      FIsLocal: boolean;

      fxmlcontent: string;

      FSpacesList: string;
      FFederationsList: string;
      FUsersList: string;
      FDeployServiceVersion: string;
      FDeployServiceTime: string;
      FFederationDomain: string;
      FServerID: string;
      FContainerID: string;
      FMetaModelVersion: string;

      procedure DeployServerInfoInit;
      procedure SpacesListInit(FirstTime: boolean = false);
      procedure FederationsListInit(FirstTime: boolean = false);
      procedure DeployServiceVersionInit(FirstTime: boolean = false);
      procedure FederationDomainInit(FirstTime: boolean = false);
      procedure DeployServiceTimeInit;
      procedure GenerateResultError(commandid,info,result: string);
    public
      constructor Create(AgentPreferences: TAgentPreferences);
      procedure Refresh(FirstTime: boolean = false);

      function GetUrlDownloadApps: string;
      function GetUrlDownloadDeployService: string;

      function GetDeployServiceVersion: string;
      function GetMetamodelVersion: string;
      function GetDeployServiceTime: string;
      function GetFederationDomain: string;
      function GetSpacesList: string;
      function GetSpaceUrl(Name: string): string;
      function GetSpaceFederationUrl(Name: string): string;
      function GetFederationsList: string;
      function GetUsersList: string;

      function ExistsSpace(Name: string): boolean;
      procedure CreateSpace(Name, Federation, Url: string);
      procedure DeleteSpace(Name: string);
      function UpdateDeployService(UrlDeployService: string): boolean;
      procedure UpdateServer(UrlApps: string);
      procedure CleanUpServers;
      procedure RestartServer(type_restart: string = 'automatic');
      procedure RenewCertificate(days: integer = 365);

      function GetSSHFile: string;
      function GetLoggingHubFile(SpaceName: string): string;
      function GetJavaWindowsPath: string;

      function GetServers: string;

      property IsLocal: boolean read FIsLocal write FIsLocal;
      property Host: string read FHost write FHost;
      property Port: string read FPort write FPort;
      property DeployType: string read FDeployType write FDeployType;
      property DeployServicePort: string read FDeployServicePort write FDeployServicePort;
      property DeployServiceUser: string read FDeployServiceUser write FDeployServiceUser;
      property DeployServicePassword: string read FDeployServicePassword write FDeployServicePassword;
      property SSHPort: string read FSSHPort write FSSHPort;
      property DeployServiceDomain: string read FDeployServiceDomain write FDeployServiceDomain;
      property LoggingHubHost: string read FLoggingHubHost write FLoggingHubHost;
      property LoggingHubPort: string read FLoggingHubPort write FLoggingHubPort;
  end;

implementation

uses utools, registry, dialogs, regexpr, strutils, uconstants, forms;

constructor TAgentServer.Create(AgentPreferences: TAgentPreferences);
begin
  FAgentPreferences := AgentPreferences;
  fxmlcontent := '';
  FServerID :='';
  FContainerID := '';
  FMetaModelVersion := '';
  FSpacesList := '';
  FFederationsList := '';
end;

procedure TAgentServer.DeployServerInfoInit;
var
  commandid, command: string;
  xml: string;
  RegexObj: TRegExpr;

begin
  RegexObj := TRegExpr.Create;
  try
    commandid := 'get_servers';
    command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter></command>';

    repeat
      xml := DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command, true);

      RegexObj.Expression := '<content>(.*)</content>';
      if RegexObj.Exec(xml) then
        fxmlcontent := AnsiReplaceStr(HTMLDecode(RegexObj.Match[1]), '<br/>', LineBreak)
      else
        fxmlcontent := '';

      if fxmlcontent <> '' then
      begin
        RegexObj.Expression := '<server id="([^"]+)"';
        if RegexObj.Exec(fxmlcontent) then
        begin
          FServerID := RegexObj.Match[1];
          if Pos(' ', FServerID) > 0 then
            FServerID := Copy(FServerID, 1, Pos(' ', FServerID)-2);
        end;

        RegexObj.Expression := '<container id="([^"]+)"';
        if RegexObj.Exec(fxmlcontent) then
          FContainerID := RegexObj.Match[1];

        RegexObj.Expression := '<servers .* monet-version="([^"]+)"';
        if RegexObj.Exec(fxmlcontent) then
          FMetaModelVersion := RegexObj.Match[1];
        if FMetaModelVersion = '' then FMetaModelVersion := '3.1';
      end;
    until (xml <> '') or not IsPortConnected(FDeployServiceDomain, FDeployServicePort);
  finally
    RegexObj.Free;
  end;
end;

function TAgentServer.GetSpacesList: string;
begin
  Result := FSpacesList;
end;

function TAgentServer.GetFederationsList: string;
begin
  Result := FFederationsList;
end;

function TAgentServer.GetUsersList: string;
begin
  Result := FUsersList;
end;

function TAgentServer.ExistsSpace(Name: string): boolean;
var
  SpacesList: TStringList;
  x: integer;
  line, space: string;
begin
  Result := false;
  SpacesList := TStringList.Create;
  try
    SpacesList.Text := FSpacesList;

    x := 0;
    while (x < SpacesList.Count) and (Result = false) do
    begin
      line := SpacesList.Strings[x];
      space := Copy(line, 1, Pos('|',line)-1);
      if (space = name) then
        Result := true;
      inc(x);
    end;
  finally
     SpacesList.Free;
  end;
end;

procedure TAgentServer.SpacesListInit(FirstTime: boolean = false);
var
  ResultList: TStringList;
  Content: TStringList;
//  RegexObj: TRegExpr;
  x: integer;
  line: string;
  name, model, url, federation_url: string;
  certificate_expiration: string;
  expression: string;
begin
  if FirstTime then FSpacesList := '';
  if fxmlcontent <> '' then
  begin
//    RegexObj := TRegExpr.Create;
    ResultList := TStringList.Create;
    Content := TStringList.Create;
    try
      Content.AddText(fxmlcontent);
      for x := 0 to Content.Count-1 do
      begin
        line := Content.Strings[x];

        expression := '<space id="([^"]+)" url="([^"]+)" model="([^"]*)" federation-url="([^"]+)"';
        name := GetMatchRegularExpression(line, expression, 1);
        url := GetMatchRegularExpression(line, expression, 2);
        model := GetMatchRegularExpression(line, expression, 3);
        federation_url := GetMatchRegularExpression(line, expression, 4);

        expression := '<space .* certificate-expiration="([^"]+)"';
        certificate_expiration := GetMatchRegularExpression(line, expression, 1);


{        RegexObj.Expression := '<space id="([^"]+)" url="([^"]+)" model="([^"]*)" federation-url="([^"]+)"';

        if (line <> '') and RegexObj.Exec(line) then
        begin
          name := RegexObj.Match[1];
          url :=  RegexObj.Match[2];
          model:= RegexObj.Match[3];
          federation_url:= RegexObj.Match[4];

          RegexObj.Expression := '<space .* certificate-expiration="([^"]+)"';
          if (line <> '') and RegexObj.Exec(line) then
          begin
            certificate_expiration := RegexObj.Match[1];
          end;}

        if (name <> '') then
        begin
          ResultList.Add(name +'|'+ model + '|' + url +'|'+ federation_url + '|' + certificate_expiration + '|');
        end;
      end;
    finally
      ResultList.Sort;
      FSpacesList := ResultList.Text;
//      RegexObj.Free;
      ResultList.Free;
      Content.Free;
      FAgentPreferences.Log.Info('TAgentServer.SpacesListInit. Spaces list: ' + FSpacesList);
    end;
  end;
end;

function StringListSortCompare(List: TStringList; Index1, Index2: Integer): LongInt;
begin
  Result := AnsiCompareStr(List[Index2], List[Index1]); // Will sort in reverse order
end;

procedure TAgentServer.FederationsListInit(FirstTime: boolean = false);
var
  FederationsList: TStringList;
  UsersList: TStringList;
  Content: TStringList;
//  RegexObj: TRegExpr;
  x: integer;
  line: string;
  name, username, is_mobile, last_use, space, node: string;
  expression: string;
begin
  if FirstTime then FFederationsList := '';
  if fxmlcontent <> '' then
  begin
//    RegexObj := TRegExpr.Create;
    FederationsList := TStringList.Create;
    UsersList := TStringList.Create;
    Content := TStringList.Create;
    try
      Content.Text := fxmlcontent;
      for x := 0 to Content.Count-1 do
      begin
        line := Content.Strings[x];

        expression := '<federation id="([^"]+)"';
        name := GetMatchRegularExpression(line, expression, 1);
        if (name <> '') then
          FederationsList.Add(name);



{        RegexObj.Expression := '<federation id="([^"]+)"';
        if RegexObj.Exec(line) then
        begin
          name := RegexObj.Match[1];
          FederationsList.Add(name);
        end;}

        expression := '<user name="([^"]+)" is_mobile="([^"]+)" last_use="([^"]+)"';
        username := GetMatchRegularExpression(line, expression, 1);
        is_mobile := GetMatchRegularExpression(line, expression, 2);
        last_use := GetMatchRegularExpression(line, expression, 3);

        expression := '<user name="([^"]+)" is_mobile="([^"]+)" last_use="([^"]+)"';
        space := GetMatchRegularExpression(line, expression, 1);
        node := GetMatchRegularExpression(line, expression, 1);

        if last_use <> '' then
          UsersList.Add(last_use + '|' + username + '|' + name + '|' + is_mobile + '|' + space + '|' + node + '|');


{        username := '';
        is_mobile := '';
        last_use := '';
        RegexObj.Expression := '<user name="([^"]+)" is_mobile="([^"]+)" last_use="([^"]+)"';
        if RegexObj.Exec(line) then
        begin
          username := RegexObj.Match[1];
          is_mobile := RegexObj.Match[2];
          last_use := RegexObj.Match[3];

          space := '';
          node := '';
          RegexObj.Expression := '<user .* space="([^"]*)" node="([^"]*)"';
          if (line <> '') and RegexObj.Exec(line) then
          begin
            space := RegexObj.Match[1];
            node := RegexObj.Match[2];
          end;

          UsersList.Add(last_use + '|' + username + '|' + name + '|' + is_mobile + '|' + space + '|' + node + '|');
        end;}
      end;
    finally
      FederationsList.Sort;
      FFederationsList := FederationsList.Text;
      UsersList.CustomSort(@StringListSortCompare);
      FUsersList := UsersList.Text;

//      RegexObj.Free;
      FederationsList.Free;
      UsersList.Free;
      Content.Free;
      FAgentPreferences.Log.Info('TAgentServer.FederationListInit. Users list: ' + FUsersList + ', Federation list: ' + FFederationsList);
    end;
  end;
end;

function TAgentServer.GetDeployServiceVersion: string;
begin
  Result := FDeployServiceVersion;
end;

function TAgentServer.GetMetamodelVersion: string;
begin
  Result := FMetaModelVersion;
end;

function TAgentServer.GetDeployServiceTime: string;
begin
  Result := FDeployServiceTime;
end;

function TAgentServer.GetFederationDomain: string;
begin
  Result := FFederationDomain;
end;

procedure TAgentServer.DeployServiceVersionInit(FirstTime: boolean = false);
//var
//  RegexObj: TRegExpr;
begin
  if FirstTime then FDeployServiceVersion := '';

  FDeployServiceVersion := GetMatchRegularExpression(fxmlcontent, '<servers .* deploy-server-version="([^"]+)"', 1);

{  if fxmlcontent <> '' then
  begin
    RegexObj := TRegExpr.Create;

    try
      RegexObj.Expression := '<servers .* deploy-server-version="([^"]+)"';
      if RegexObj.Exec(fxmlcontent) then
        FDeployServiceVersion := RegexObj.Match[1];
    finally
      RegexObj.Free;
    end;
  end;}
end;

procedure TAgentServer.DeployServiceTimeInit;
{var
  RegexObj: TRegExpr;}
begin
  FDeployServiceTime := GetMatchRegularExpression(fxmlcontent, '<servers .* time="([^"]+)"', 1);
{  FDeployServiceTime := '';

  if fxmlcontent <> '' then
  begin
    RegexObj := TRegExpr.Create;

    try
      RegexObj.Expression := '<servers .* time="([^"]+)"';
      if RegexObj.Exec(fxmlcontent) then
        FDeployServiceTime := RegexObj.Match[1];
    finally
      RegexObj.Free;
    end;
  end;}
end;

procedure TAgentServer.FederationDomainInit(FirstTime: boolean = false);
//var
  //RegexObj: TRegExpr;
begin
  if FirstTime then FFederationDomain := '';

  FFederationDomain := GetMatchRegularExpression(fxmlcontent, '<federation .* domain="([^"]+)"', 1);
  if (FFederationDomain = '') then
    FFederationDomain := GetMatchRegularExpression(fxmlcontent, '<servers .* federation-domain="([^"]+)"', 1);

{  if fxmlcontent <> '' then
  begin
    RegexObj := TRegExpr.Create;
    try
      RegexObj.Expression := '<federation .* domain="([^"]+)"';
      RegexObj.ModifierG := true;
      if RegexObj.Exec(fxmlcontent) then
        FFederationDomain := RegexObj.Match[1];

      if FFederationDomain = '' then
      begin
        RegexObj.Expression := '<servers .* federation-domain="([^"]+)"';
        RegexObj.ModifierG := true;
        if RegexObj.Exec(fxmlcontent) then
          FFederationDomain := RegexObj.Match[1];
      end;

    finally
      RegexObj.Free;
    end;
  end;}
end;

procedure TAgentServer.Refresh(FirstTime: boolean = false);
begin
  DeployServerInfoInit;
  SpacesListInit(FirstTime);
  FederationsListInit(FirstTime);
  DeployServiceVersionInit(FirstTime);
  FederationDomainInit(FirstTime);
  DeployServiceTimeInit;
end;

function TAgentServer.GetUrlDownloadApps: string;
begin
  Result := FAgentPreferences.GetUrlApps(FMetaModelVersion);
end;

function TAgentServer.GetUrlDownloadDeployService: string;
begin
  Result := FAgentPreferences.GetUrlDeployService;
end;

function TAgentServer.GetSSHFile: string;
var
  TextFile: TStringList;
  FileName: string;
begin
  Result := '';
  TextFile := TStringList.Create;

  try
    TextFile.Add('<?xml version="1.0" encoding="utf-8"?>');
    TextFile.Add('<jnlp spec="1.0+" codebase="http://www.weavervsworld.com/ssh" href="mindterm.jnlp">');
    TextFile.Add('  <information>');
    TextFile.Add('    <title>MindTerm 3.1.2 SSH</title>');
    TextFile.Add('    <vendor>Appgate</vendor>');
    TextFile.Add('    <homepage href="http://www.appgate.com/mindterm"/>');
    TextFile.Add('    <description>MindTerm Java SSH Client</description>');
    TextFile.Add('    <offline-allowed/>');
    TextFile.Add('  </information>');
    TextFile.Add('  <security>');
    TextFile.Add('    <all-permissions/>');
    TextFile.Add('  </security>');
    TextFile.Add('  <resources>');
    TextFile.Add('    <j2se version="1.4+"/>');
    TextFile.Add('    <jar href="mindterm312.weavselfsign.jar"/>');
    TextFile.Add('  </resources>');
    TextFile.Add('  <application-desc main-class="com.mindbright.application.MindTerm">');
    TextFile.Add('    <argument>-protocol</argument>');
    TextFile.Add('    <argument>ssh2</argument>');
    TextFile.Add('    <argument>-alive</argument>');
    TextFile.Add('    <argument>60</argument>');
    TextFile.Add('    <argument>-80x132-enable</argument>');
    TextFile.Add('    <argument>true</argument>');
    TextFile.Add('    <argument>-80x132-toggle</argument>');
    TextFile.Add('    <argument>true</argument>');
    TextFile.Add('    <argument>-bg-color</argument>');
    TextFile.Add('    <argument>black</argument>');
    TextFile.Add('    <argument>-fg-color</argument>');
    TextFile.Add('    <argument>white</argument>');
    TextFile.Add('    <argument>-cursor-color</argument>');
    TextFile.Add('    <argument>i_green</argument>');
    TextFile.Add('    <argument>-geometry</argument>');
    TextFile.Add('    <argument>132x35</argument>');
    TextFile.Add('    <argument>-encoding</argument>');
    TextFile.Add('    <argument>utf-8</argument>');
    TextFile.Add('    <argument>-server</argument>');
    TextFile.Add('    <argument>'+FHost+'</argument>');
    TextFile.Add('    <argument>-port</argument>');
    TextFile.Add('    <argument>'+FSSHPort+'</argument>');
    TextFile.Add('    <argument>-username</argument>');
    TextFile.Add('    <argument>root</argument>');
    TextFile.Add('  </application-desc>');
    TextFile.Add('</jnlp>');

    FileName := DirectoryTemp + 'msmanager.term.jnlp';
    TextFile.SaveToFile(FileName);
    Result := FileName;
  finally
     TextFile.Free;
  end;
end;

function TAgentServer.GetLoggingHubFile(SpaceName: string): string;
var
  TextFile: TStringList;
  FileName: string;
begin
  Result := '';
  TextFile := TStringList.Create;

  try
    TextFile.Add('<loggingFrontendConfiguration title="'+SpaceName+' configuration">');
    TextFile.Add('  <environment name="'+SpaceName+'">');
    TextFile.Add('    <hub name="'+SpaceName+'Hub" host="'+FLoggingHubHost+'" port="'+FLoggingHubPort+'" />');
    TextFile.Add('  </environment>');
    TextFile.Add('</loggingFrontendConfiguration>');

    FileName := ApplicationPath + 'logging.frontend.xml';
    TextFile.SaveToFile(FileName);
    Result := FileName;
  finally
     TextFile.Free;
  end;
end;

function TAgentServer.GetJavaWindowsPath: string;
var
  Reg: TRegistry;
  value: string;
  CurrentVersion: string;
begin
  Result := '';
  CurrentVersion := '';
  reg := TRegistry.Create(KEY_READ OR $0100);
  try
    Reg.RootKey := HKEY_LOCAL_MACHINE;

    if Reg.OpenKey('\SOFTWARE\JavaSoft\Java Runtime Environment', false) then
    begin
      value := 'CurrentVersion';
      if Reg.ValueExists(value) then
        CurrentVersion := Reg.ReadString(value);
      Reg.CloseKey;
    end;

    if (CurrentVersion <> '') and Reg.OpenKey('\SOFTWARE\JavaSoft\Java Runtime Environment\' + CurrentVersion, false) then
    begin
      value := 'JavaHome';
      if Reg.ValueExists(value) then
        Result := Reg.ReadString(value);
      Reg.CloseKey;
    end;
  finally
    Reg.Free;
  end;
end;

procedure TAgentServer.CreateSpace(Name, Federation, Url: string);
var
  commandid, command, result: string;
begin
  DeleteSpace(Name);

  commandid := 'add_space';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter><parameter id="space">'+Name+'</parameter><parameter id="base-url">'+Url+'</parameter><parameter id="space-label-es">'+Name+'</parameter><parameter id="federation">'+Federation+'</parameter></command>';

  result := DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command);
  GenerateResultError(commandid, 'Failed to add space.', result);
end;

procedure TAgentServer.DeleteSpace(Name: string);
var
  commandid, command: string;
begin
  commandid := 'delete_space';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter><parameter id="space">'+Name+'</parameter></command>';

  DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command);
end;

function TAgentServer.UpdateDeployService(UrlDeployService: string): boolean;
const max_time_wait_500 = 10000;
var
  commandid, command: string;
  code: integer;
  time_wait_500: integer;
begin
  Result := false;
  time_wait_500 := 0;
  commandid := 'update_application';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="url">'+UrlDeployService+'</parameter></command>';
  DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command, false);

  commandid := 'get_servers';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter></command>';
  repeat
    code := DeployServiceSendCommandStatus(FDeployServiceDomain, FDeployServicePort, command, false);

    if ((code = 0) or (code >= 500)) and (time_wait_500 <= max_time_wait_500) then
    begin
      sleep(1000);
      time_wait_500 += 1000;
    end;
  until ((code > 0) and (code < 500)) or (time_wait_500 > max_time_wait_500);

  if (code = 200) then Result := true;
end;

procedure TAgentServer.UpdateServer(UrlApps: string);
var
  commandid, command, result: string;
begin
  commandid := 'update_federations';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter><parameter id="url">'+UrlApps+'</parameter></command>';

  result := DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command);
  GenerateResultError(commandid, 'Failed to update the federations (download apps files).', result);

  commandid := 'update_wars';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter><parameter id="url">'+UrlApps+'</parameter></command>';

  result := DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command);
  GenerateResultError(commandid, 'Failed to update the server (download apps files).', result);

  commandid := 'reset_monet_container';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter><parameter id="replace-theme">true</parameter></command>';

  result := DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command);
  GenerateResultError(commandid, 'Failed to update the server.', result);

  commandid := 'reset_docservice_container';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter><parameter id="replace-theme">true</parameter></command>';

  result := DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command);
  GenerateResultError(commandid, 'Failed to update the server.', result);

  RestartServer;
end;

procedure TAgentServer.CleanUpServers;
var
  commandid, command, result: string;
begin
  commandid := 'delete_container';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter></command>';

  result := DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command);
  if not AnsiContainsStr(result, '<result id="'+commandid+'"><status>ok</status><caption /><content></content></result>') then
     raise Exception.Create('Failed to clean up server.'+#13#10+'Result: ' + result);
end;

procedure TAgentServer.RestartServer(type_restart: string = 'automatic');
var
  commandid, command, result: string;
  TextTypeRestart: string;
begin
  commandid := 'restart_container';
  TextTypeRestart := '<parameter id="type-restart">'+type_restart+'</parameter>';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter>'+TextTypeRestart+'</command>';

  result := DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command);
  if not AnsiContainsStr(result, '<result id="'+commandid+'"><status>ok</status><caption /><content></content></result>') then
     raise Exception.Create('Failed to restart the server.'+#13#10+'Result: ' + result);
end;

procedure TAgentServer.RenewCertificate(days: integer = 365);
var
  commandid, command, result: string;
  parameters: string;
begin
  commandid := 'renew_certificates';
  parameters := '<parameter id="days">'+IntToStr(days)+'</parameter>';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter>'+parameters+'</command>';

  result := DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command);
  if not AnsiContainsStr(result, '<result id="'+commandid+'"><status>ok</status><caption /><content></content></result>') then
     raise Exception.Create('Failed to renew certificate.'+#13#10+'Result: ' + result);
end;

function TAgentServer.GetServers: string;
var
  commandid, command: string;
begin
  Result := '';
  commandid := 'get_servers';
  command := '<command id="'+commandid+'"><parameter id="user">'+FDeployServiceUser+'</parameter><parameter id="password">'+FDeployServicePassword+'</parameter><parameter id="server">'+ FServerID +'</parameter><parameter id="container">'+ FContainerID +'</parameter></command>';
  FAgentPreferences.Log.Info('TAgentServer.GetServers. Domain: "' + FDeployServiceDomain + '", Port: "' + FDeployServicePort + '", command: "' + command + '"');

  Result := DeployServiceSendCommand(FDeployServiceDomain, FDeployServicePort, command, true);
end;

function TAgentServer.GetSpaceUrl(Name: string): String;
var
  x: integer;
  Space: string;
  SpacesList: TStringList;
  line: string;
begin
  SpacesList := TStringList.Create;
  try
    SpacesList.Text := FSpacesList;

    Result := '';
    x := 0;
    while (x < SpacesList.Count) and (Result = '') do
    begin
      line := SpacesList.Strings[x];
      space := Copy(line, 1, Pos('|',line)-1);
      if (Space = Name) then
      begin
        line := Copy(line, Pos('|',line)+1, Length(line));
        line := Copy(line, Pos('|',line)+1, Length(line));
        Result := Copy(line, 1, Pos('|',line)-1);
      end;
      inc(x);
    end;
  finally
    SpacesList.Free;
  end;
end;

function TAgentServer.GetSpaceFederationUrl(Name: string): String;
var
  x: integer;
  Space: string;
  SpacesList: TStringList;
  line: string;
begin
  SpacesList := TStringList.Create;
  try
    SpacesList.Text := FSpacesList;

    Result := '';
    x := 0;
    while (x < SpacesList.Count) and (Result = '') do
    begin
      line := SpacesList.Strings[x];
      Space := Copy(line, 1, Pos('|',line)-1);
      if (Space = Name) then
      begin
        line := Copy(line, Pos('|',line)+1, Length(line));
        line := Copy(line, Pos('|',line)+1, Length(line));
        line := Copy(line, Pos('|',line)+1, Length(line));
        Result := Copy(line, 1, Pos('|',line)-1);
      end;
      inc(x);
    end;
  finally
    SpacesList.Free;
  end;
end;

procedure TAgentServer.GenerateResultError(commandid,info,result: string);
var
//  RegexObj: TRegExpr;
  expression: string;
  messageA, messageB: string;
begin
  if not AnsiContainsStr(result, '<result id="'+commandid+'"><status>ok</status><caption /><content></content></result>') then
  begin

    expression := '<status>(.*)</status>.*<caption>(.*)</caption>';
    messageA := GetMatchRegularExpression(result, expression, 1);

    if (messageA <> '') then
    begin
      messageB := GetMatchRegularExpression(result, expression, 2);
      result := 'Message: ' + messageA +LineBreak+ messageB;
    end;

{    RegexObj := TRegExpr.Create;
    RegexObj.Expression := '<status>(.*)</status>.*<caption>(.*)</caption>';
    if RegexObj.Exec(result) then
    begin
      result := 'Message: ' + RegexObj.Match[1] +LineBreak+ RegexObj.Match[2];
    end;}

    raise Exception.Create(info+LineBreak+LineBreak+ result);
  end;
end;

end.

