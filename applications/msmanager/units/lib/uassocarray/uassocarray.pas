unit uassocarray;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils;

type
  TAssocArray = class
    FElementsList: TStrings;
    FIndexList: TStrings;

    function GetValue(index: string): string;
    function GetIndex(index: integer): string;
    procedure SetValue(index,value: string);
    function GetCount: integer;
  public
    constructor Create;
    destructor Destroy; override;
    procedure Clear;
    property Items[index: string]: string read GetValue write SetValue; default;
    property IndexName[index: integer]: string read GetIndex;
    property Count: integer read GetCount;
  end;



implementation

constructor TAssocArray.Create;
begin
  FIndexList := TStringList.Create;
  FElementsList := TStringList.Create;
end;

destructor TAssocArray.Destroy;
begin
  inherited;
  FIndexList.Free;
  FElementsList.Free;
end;

function TAssocArray.GetValue(index: string): string;
var
  i: integer;
  value: string;
begin
  value := '';
  i := FIndexList.IndexOf(index);
  if i > -1 then
    value := FElementsList.Strings[i];
  Result := value;
end;

function TAssocArray.GetIndex(index: integer): string;
var
  value: string;
begin
  value := '';
  if index > -1 then
    value := FIndexList.Strings[index];
  Result := value;
end;

procedure TAssocArray.SetValue(index,value: string);
var
  i: integer;
begin
  i := FIndexList.IndexOf(index);
  if i = -1 then
  begin
    FIndexList.Add(index);
    FElementsList.Add(value);
  end
  else
  begin
    FElementsList.Strings[i] := value;
  end;
end;

procedure TAssocArray.Clear;
begin
  FIndexList.Clear;
  FElementsList.Clear;
end;

function TAssocArray.GetCount: integer;
begin
  Result := FIndexList.Count;
end;

end.

