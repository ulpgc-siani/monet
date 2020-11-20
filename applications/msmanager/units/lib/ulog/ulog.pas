unit ulog;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils;

type
  TLog = class
  private
    FFileName: string;
//    TextFile: TStringList;
    FEnabled: boolean;
    procedure WriteMessage(mtype, message: string);
  public
    constructor Create(FileName: string);

    procedure Info(message: string);
    procedure Error(message: string);

    property Enabled: boolean read FEnabled write FEnabled;
end;

implementation

constructor TLog.Create(FileName: string);
begin
  FFileName := FileName;
//  TextFile := TStringList.Create;
  FEnabled := true;
end;

procedure TLog.WriteMessage(mtype, message: string);
var
  formattedDateTime: string;
  TextFile: TStringList;
begin
  if FEnabled then
  begin
    DateTimeToString(formattedDateTime, 'dd:mm:yyyy hh:nn:ss', Now);
    try
      TextFile := TStringList.Create;
      if FileExists(FFileName) then TextFile.LoadFromFile(FFileName);
      TextFile.Add('['+formattedDateTime +'] '+mtype+': ' + message);
      TextFile.SaveToFile(FFileName);
      TextFile.Free;
    except end;
  end;
end;

procedure TLog.Info(message: string);
begin
  WriteMessage('Info', message);
end;

procedure TLog.Error(message: string);
begin
  WriteMessage('Error', message);
end;

end.

