unit fproperties;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls,
  Grids, Menus;

type

  { TFrmProperties }

  TFrmProperties = class(TForm)
    BtnClose: TButton;
    MenuCopyPropertyValue: TMenuItem;
    PopProperties: TPopupMenu;
    StrGridProperties: TStringGrid;
    procedure BtnCloseClick(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure MenuCopyPropertyValueClick(Sender: TObject);
  private
    { private declarations }
    FIndex: integer;
  public
    { public declarations }
    procedure Clean;
    procedure Add(title, value: string);
  end;

var
  FrmProperties: TFrmProperties;

implementation

{$R *.lfm}
uses clipbrd;

procedure TFrmProperties.FormCreate(Sender: TObject);
begin
  Clean;
end;

procedure TFrmProperties.BtnCloseClick(Sender: TObject);
begin
  Close;
end;

procedure TFrmProperties.MenuCopyPropertyValueClick(Sender: TObject);
begin
  ClipBoard.AsText:= StrGridProperties.Cells[1, StrGridProperties.Row];
end;

procedure TFrmProperties.Clean;
begin
  FIndex := 1;
end;

procedure TFrmProperties.Add(title, value: string);
begin
  StrGridProperties.RowCount:= FIndex+1;
  StrGridProperties.Cells[0, FIndex] := title;
  StrGridProperties.Cells[1, FIndex] := value;
  inc(FIndex);
end;

end.

