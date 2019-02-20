unit uagent_main;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, uagent_preferences;

type

  TAgentMain = class
    private
      FAgentPreferences: TAgentPreferences;
      function GetVBoxWindowsPath: string;
      function IsPortOccuped(Port: string): boolean;

    public
      constructor Create(AgentPreferences: TAgentPreferences);

      function IsValidFileName(FileName: string): boolean;

      function GetRemoteMSTemplateMD5(Url:string): string;
      function GetLocalMSTemplateMD5(FileName:string): string;

      function GetUrlDownloadMSTemplate: string;
  end;


implementation

uses forms, registry, utools, httpsend,blcksock, ssl_openssl;

constructor TAgentMain.Create(AgentPreferences: TAgentPreferences);
begin
  FAgentPreferences := AgentPreferences;
end;

function TAgentMain.GetVBoxWindowsPath: string;
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

function TAgentMain.IsPortOccuped(Port: string): boolean;
var sock: TTCPBlockSocket;
begin
  Result := true;
  sock := TTCPBlockSocket.create;
  try
    sock.Connect(FAgentPreferences.IP, Port);
    if sock.lasterror <> 0 then Result := false;
    sock.CloseSocket;
  finally
     sock.free;
  end;
end;

function TAgentMain.IsValidFileName(FileName: string): boolean;
const
   alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_1234567890:\.';
var
  x: integer;
  character: string;

begin
  Result := true;
  if OsType = 'windows' then
  begin
    x := 1;
    while Result and (x <= Length(FileName)) do
    begin
      character := FileName[x];
      Result := (Pos(alphabet, character) = 0);
      inc(x);
    end;
  end;
end;

function TAgentMain.GetRemoteMSTemplateMD5(Url:string): string;
var
  http: THTTPSend;
  ResultMD5: TStringList;
begin
  Result := '';
  http:= THTTPSend.Create;
  http.Timeout := 5000;

  FAgentPreferences.Log.Info('GetRemoteMSTemplateMD5. Url: ' + Url);
  if http.HTTPMethod('GET', Url) then
  begin
    ResultMD5 := TStringList.Create;
    ResultMD5.LoadFromStream(http.Document);
    Result := Trim(ResultMD5.Text);
  end;
  http.Free;
end;

function TAgentMain.GetLocalMSTemplateMD5(FileName: string): string;
var
  ResultMD5: TStringList;
begin
  ResultMD5 := TStringList.Create;
  ResultMD5.LoadFromFile(FileName);
  Result := Trim(ResultMD5.Text);
end;

function TAgentMain.GetUrlDownloadMSTemplate: string;
var
  http : THTTPSend;
  page : TStringList;
begin
  Result := '';
  http:=THTTPSend.Create;
  http.Timeout := 5000;
  page:=TStringList.Create;
  try
    FAgentPreferences.Log.Info('GetUrlDownloadMSTemplate. Url: ' + FAgentPreferences.UrlMSTemplate);
    if http.HTTPMethod('GET',FAgentPreferences.UrlMSTemplate) then
    begin
      page.LoadFromStream(http.Document);
      Result := page.Text;
      Result := Trim(Copy(Result, 1, Length(Result)-1));
    end;
  finally
     http.Free;
     page.Free;
  end;
end;


end.

