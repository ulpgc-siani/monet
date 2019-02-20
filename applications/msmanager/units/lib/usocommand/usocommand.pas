unit usocommand;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, process, uagent_preferences, strutils;

type
  TSOCommand = class
  private
    FAgentPreferences: TAgentPreferences;

    function ExecuteCommandUnix(Command: string; CurrentDirectory: string = ''): string;
    function ExecuteCommandWindows(Command: string; CurrentDirectory: string = ''): string;

    function ExecuteCommandUnixProcess(Command: string; Wait: boolean = true; CurrentDirectory: string = ''): TProcess;
    function ExecuteCommandWindowsProcess(Command: string; Wait: boolean = true; CurrentDirectory: string = ''): TProcess;

    procedure ExecuteSimpleWindows(Command, Parameters: string; CurrentDirectory: string = '');
    procedure ExecuteSimpleUnix(Command, Parameters: string; CurrentDirectory: string = '');
  public
    constructor Create(AgentPreferences: TAgentPreferences);

    function Execute(Command: string; CurrentDirectory: string): string;
    function ExecuteProcess(Command: string; CurrentDirectory: string = ''): TProcess;
    procedure ExecuteSimple(Command, Parameters: string; CurrentDirectory: string = '');
end;


implementation

uses utools{$IFDEF Windows}, ShellApi{$ENDIF};

constructor TSOCommand.Create(AgentPreferences: TAgentPreferences);
begin
  FAgentPreferences := AgentPreferences;
end;

function TSOCommand.Execute(command: string; CurrentDirectory: string): string;
begin
  if OsType = 'windows' then
    Result := ExecuteCommandWindows(command, CurrentDirectory)
  else
    Result := ExecuteCommandUnix(command, CurrentDirectory);
end;

function TSOCommand.ExecuteProcess(command: string; CurrentDirectory: string = ''): TProcess;
begin
  if OsType = 'windows' then
    Result := ExecuteCommandWindowsProcess(command, false, CurrentDirectory)
  else
    Result := ExecuteCommandUnixProcess(command, false, CurrentDirectory);
end;

procedure TSOCommand.ExecuteSimple(Command, Parameters: string; CurrentDirectory: string = '');
begin
  {$IFDEF Windows}
    ExecuteSimpleWindows(command, Parameters, CurrentDirectory);
  {$ENDIF}

  {$IFDEF UNIX}
    ExecuteSimpleUnix(command, Parameters, CurrentDirectory);
  {$ENDIF}
end;

function TSOCommand.ExecuteCommandUnix(command: string; CurrentDirectory: string = ''): string;
var
  Process: TProcess;
  StringList: TStringList;
begin
  Result := '';
  StringList := TStringList.Create;

  try
    Process := ExecuteCommandUnixProcess(command, true, CurrentDirectory);
    StringList.LoadFromStream(Process.Output);
    Result := StringList.Text;
    Result := AnsiReplaceStr(Result, #13, '');
  finally
    Process.Free;
    StringList.Free;
  end;
end;

function TSOCommand.ExecuteCommandWindows(Command: string; CurrentDirectory: string = ''): string;
const READ_BYTES = 2048;
var
  Process: TProcess;
  MemStream: TMemoryStream;
  NumBytes: LongInt;
  BytesRead: LongInt;
  StringList: TStringList;

begin
  Result := '';
  BytesRead := 0;
  MemStream := TMemoryStream.Create;

  try
    Process := ExecuteCommandWindowsProcess(command, true, CurrentDirectory);

    while Process.Running do
    begin
      MemStream.SetSize(BytesRead + READ_BYTES);

      NumBytes := Process.Output.Read((MemStream.Memory + BytesRead)^, READ_BYTES);
      if NumBytes > 0
      then begin
        Inc(BytesRead, NumBytes);
      end
      else begin  Sleep(100);
      end;
    end;

    repeat
      MemStream.SetSize(BytesRead + READ_BYTES);
      NumBytes := Process.Output.Read((MemStream.Memory + BytesRead)^, READ_BYTES);
      if NumBytes > 0
      then begin
        Inc(BytesRead, NumBytes);
      end;
    until NumBytes <= 0;
    MemStream.SetSize(BytesRead);

    StringList := TStringList.Create;
    try
      StringList.LoadFromStream(MemStream);
      result := StringList.Text;
    finally
      StringList.Free;
    end;

  finally
    Process.Free;
    MemStream.Free;
  end;
end;

function TSOCommand.ExecuteCommandUnixProcess(command: string; Wait: boolean = true; CurrentDirectory: string = ''): TProcess;
var
  Process: TProcess;

begin
  Result := nil;
  Process := TProcess.Create(nil);
  Process.CommandLine := command;

  Process.Options := Process.Options + [poWaitOnExit, poUsePipes];
  if not Wait then
    Process.Options := [poUsePipes];
  Process.ShowWindow:= swoHIDE;
  if CurrentDirectory <> '' then Process.CurrentDirectory := CurrentDirectory;
  Process.Execute;

  Result := Process;
end;

function TSOCommand.ExecuteCommandWindowsProcess(Command: string; Wait: boolean = true; CurrentDirectory: string = ''): TProcess;
var
  Process: TProcess;
begin
  Result := nil;
  Process := TProcess.Create(nil);

  Process.CommandLine := Command;
  Process.Options := Process.Options + [poUsePipes];
  Process.ShowWindow:= swoHIDE;
  if CurrentDirectory <> '' then Process.CurrentDirectory := CurrentDirectory;
  Process.Execute;

  Result := Process;
end;

procedure TSOCommand.ExecuteSimpleUnix(Command, Parameters: string; CurrentDirectory: string = '');
begin
  ExecuteCommandUnixProcess(command +' '+parameters, false, CurrentDirectory);
end;

procedure TSOCommand.ExecuteSimpleWindows(Command, Parameters: string; CurrentDirectory: string = '');
begin
  ExecuteCommandWindowsProcess(command +' '+parameters, false, CurrentDirectory);
end;


end.

