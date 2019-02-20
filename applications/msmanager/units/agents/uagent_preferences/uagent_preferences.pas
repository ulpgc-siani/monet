unit uagent_preferences;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, ulog;

type
  TAgentPreferences = class
    private
      FConfigFile: string;
      FIP: string;
      FVersionBeta: boolean;
      FMirrorUlpgc: boolean;
      FTabIndex: integer;
      FSSHKeyFile: string;
      FDebug: boolean;
      FRemoteServersConfigFile: string;
      FUpdatesStart: boolean;

      function GetMainIP: string;
      function GetIPs: string;

      function GetIP: string;
      function GetVersionBeta: boolean;
      function GetMirrorUlpgc: boolean;
      function GetTabIndex: integer;
      function GetSSHKeyFile: string;
      function GetDebug: boolean;
      function GetRemoteServersConfigFile: string;
      function GetUpdatesStart: boolean;

      procedure SetIP(value: string);
      procedure SetVersionBeta(value: boolean);
      procedure SetMirrorUlpgc(value: boolean);
      procedure SetTabIndex(value: integer);
      procedure SetSSHKeyFile(value: string);
      procedure SetDebug(value: boolean);
      procedure SetRemoteServersConfigFile(value: string);
      procedure SetUpdatesStart(value: boolean);

      function GetUrlMSTemplate: string;

      function GetRemoteServersFile: string;
      function GetLogFile: string;
      function GetIsInactiveNetwork: boolean;
    public
      Log: TLog;

      constructor Create;

      procedure LoadConfig;
      procedure SaveConfig;

      function GetUrlApps(MetaModelVersion: string): string;
      function GetUrlDeployService: string;

      property IP: string read GetIP write SetIP;
      property VersionBeta: boolean read GetVersionBeta write SetVersionBeta;
      property MirrorUlpgc: boolean read GetMirrorUlpgc write SetMirrorUlpgc;
      property TabIndex: integer read GetTabIndex write SetTabIndex;
      property SSHKeyFile: string read GetSSHKeyFile write SetSSHKeyFile;
      property Debug: boolean read GetDebug write SetDebug;
      property RemoteServersConfigFile: string read GetRemoteServersConfigFile write SetRemoteServersConfigFile;
      property UpdatesStart: boolean read GetUpdatesStart write SetUpdatesStart;

      property LocalIPs: string read GetIPs;
      property UrlMSTemplate: string read GetUrlMSTemplate;
      property MainIp: string read GetMainIP;
      property RemoteServersFile: string read GetRemoteServersFile;
      property IsInactiveNetwork: boolean read GetIsInactiveNetwork;
  end;


implementation

uses LSHTTPSend{$ifdef windows}, winsock{$endif}, utools, INIFiles, uconstants;

constructor TAgentPreferences.Create;
begin
  FConfigFile := GetConfigDir + '/' + GetAppProductShortName + '.config';
  LoadConfig;
  Log := TLog.Create(GetLogFile);
  Log.Enabled:= FDebug;
end;

procedure TAgentPreferences.SetIP(value: string); begin FIP := value; end;
function TAgentPreferences.GetIP: string; begin Result := FIP; end;

procedure TAgentPreferences.SetVersionBeta(value: boolean); begin FVersionBeta := value; end;
function TAgentPreferences.GetVersionBeta: boolean; begin Result := FVersionBeta; end;

procedure TAgentPreferences.SetMirrorUlpgc(value: boolean); begin FMirrorUlpgc := value; end;
function TAgentPreferences.GetMirrorUlpgc: boolean; begin Result := FMirrorUlpgc; end;

procedure TAgentPreferences.SetTabIndex(value: integer); begin FTabIndex := value; end;
function TAgentPreferences.GetTabIndex: integer; begin Result := FTabIndex; end;

procedure TAgentPreferences.SetSSHKeyFile(value: string); begin FSSHKeyFile := value; end;
function TAgentPreferences.GetSSHKeyFile: string; begin Result := FSSHKeyFile; end;

procedure TAgentPreferences.SetDebug(value: boolean); begin FDebug := value; Log.Enabled:= FDebug; end;
function TAgentPreferences.GetDebug: boolean; begin Result := FDebug; end;

procedure TAgentPreferences.SetRemoteServersConfigFile(value: string); begin FRemoteServersConfigFile := value; end;
function TAgentPreferences.GetRemoteServersConfigFile: string; begin Result := FRemoteServersConfigFile; end;

procedure TAgentPreferences.SetUpdatesStart(value: boolean); begin FUpdatesStart := value; end;
function TAgentPreferences.GetUpdatesStart: boolean; begin Result := FUpdatesStart; end;

function TAgentPreferences.GetMainIP: string;
const
  CLSIPType: array[0..1] of TLSIPType = (iptLocal, iptExternal);
begin
  Result := LSGetIP(CLSIPType[0]);
end;

function TAgentPreferences.GetIPs: string;
{$ifdef windows}
type
	TaPInAddr = array [0..10] of PInAddr;
	PaPInAddr = ^TaPInAddr;
{$endif}
var
{$ifdef windows}
    TempBuf : array [0..63] of char;
    phe: PHostEnt;
    pptr: PaPInAddr;
    i : integer;
    IPstr : string;
    GInitData: TWSADATA;
{$endif}
    List: TStringList;
begin
  List := TStringList.Create;
  List.Add('127.0.0.1');
  Result := '';
  try
{$ifdef windows}
    if WSAStartup($0101, GInitData) = 0 then
    begin
      GetHostName(TempBuf, SizeOf(TempBuf));
      phe :=GetHostByName(TempBuf);
      if phe = nil then begin end
      else begin
        i := 0;
        pptr := PaPInAddr(Phe^.h_addr_list);
        IPstr := '';
        while pptr^[i] <> nil do begin
          IPstr := StrPas(inet_ntoa(pptr^[i]^));
          List.Add(IPstr);
          Inc(i);
        end;
        WSACleanup;
      end;
    end;
{$else}
    List.Add(GetMainIP);
{$endif}
    Result := List.Text;
  finally
    List.Free;
  end;
end;

procedure TAgentPreferences.LoadConfig;
var
  Config:TINIFile;
begin
  Config := TINIFile.Create(FConfigFile);
  FIP := Config.ReadString('General', 'IP', GetMainIP);
  FVersionBeta := Config.ReadBool('General', 'VersionBeta', true);
  FMirrorUlpgc := Config.ReadBool('General', 'MirrorUlpgc', false);
  FTabIndex := Config.ReadInteger('General', 'TabIndex', 0);
  FSSHKeyFile := Config.ReadString('General', 'SSHKeyFile', '');
  FDebug := Config.ReadBool('General', 'Debug', false);
  FUpdatesStart := Config.ReadBool('General', 'UpdatesStart', true);
  FRemoteServersConfigFile := StringReplace(Config.ReadString('General', 'RemoteServersConfigFile', ''), ';', LineBreak, [rfReplaceAll]);
  Config.Free;
end;

procedure TAgentPreferences.SaveConfig;
var
  Config:TINIFile;
  ConfigPath: string;
begin
  ConfigPath := ExtractFilePath(FConfigFile);
  if not DirectoryExists(ConfigPath) then
    ForceDirectories(ConfigPath);

  Config := TINIFile.Create(FConfigFile);
  Config.WriteString('General', 'IP', FIP);
  Config.WriteBool('General', 'VersionBeta', FVersionBeta);
  Config.WriteBool('General', 'MirrorUlpgc', FMirrorUlpgc);
  Config.WriteInteger('General', 'TabIndex', FTabIndex);
  Config.WriteString('General', 'SSHKeyFile', FSSHKeyFile);
  Config.WriteBool('General', 'Debug', FDebug);
  Config.WriteString('General', 'RemoteServersConfigFile', StringReplace(FRemoteServersConfigFile, LineBreak, ';', [rfReplaceAll]));
  Config.WriteBool('General', 'UpdatesStart', FUpdatesStart);
  Config.Free;
end;

function TAgentPreferences.GetIsInactiveNetwork: boolean;
var
  IPs: TStringList;
begin
  IPs := TStringList.Create;
  IPs.Text := GetIPs;

  Result := (IPs.IndexOf(GetIP) = -1);
end;

function TAgentPreferences.GetUrlApps(MetaModelVersion: string): string;
begin
  Result :=  UrlApps + '/'+ MetaModelVersion;

  if FMirrorUlpgc then
    Result := UrlAppsUlpgc + '/'+ MetaModelVersion
end;

function TAgentPreferences.GetUrlDeployService: string;
begin
  Result :=  UrlApps;
  if FMirrorUlpgc then
    Result := UrlAppsUlpgc
end;

function TAgentPreferences.GetUrlMSTemplate: string;
begin
  Result := UrlMSTemplateStable;
  if VersionBeta then
  begin
    if FMirrorUlpgc then
      Result := UrlMSTemplateBetaUlpgc
    else
      Result := UrlMSTemplateBeta;
  end
  else
  begin
    if FMirrorUlpgc then
      Result := UrlMSTemplateStableUlpgc
    else
      Result := UrlMSTemplateStable;
  end;
end;

function TAgentPreferences.GetRemoteServersFile: string;
begin
  Result := GetConfigDir + '/remoteservers.ini';
  if FRemoteServersConfigFile <> '' then
    Result := FRemoteServersConfigFile;
end;

function TAgentPreferences.GetLogFile: string;
begin
  Result := GetConfigDir + '/msmanager.log';
end;

end.

