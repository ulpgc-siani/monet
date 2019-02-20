unit finput;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls;

type

  { TWinDgInput }

  TWinDgInput = class(TForm)
    BtnOk: TButton;
    BtnCancel: TButton;
    TxtInput: TEdit;
    LblMessage: TLabel;
    procedure BtnCancelClick(Sender: TObject);
    procedure BtnOkClick(Sender: TObject);
    procedure FormShow(Sender: TObject);
  private
    { private declarations }
    FSelectedOk: boolean;
  public
    { public declarations }
    function Execute(Title, Message: string; var Value: string): boolean;
  end; 

var
  WinDgInput: TWinDgInput;

implementation

{$R *.lfm}

{ TWinDgInput }

function TWinDgInput.Execute(Title, Message: string; var Value: string): boolean;
begin
  FSelectedOk := false;
  Caption := Title;
  LblMessage.Caption := Message;
  TxtInput.Text:= Value;
  ShowModal;
  Result := FSelectedOk;
  Value := TxtInput.Text;
end;

procedure TWinDgInput.BtnOkClick(Sender: TObject);
begin
  FSelectedOk := true;
  Close;
end;

procedure TWinDgInput.FormShow(Sender: TObject);
begin
  TxtInput.SetFocus;
end;

procedure TWinDgInput.BtnCancelClick(Sender: TObject);
begin
  Close;
end;

end.

