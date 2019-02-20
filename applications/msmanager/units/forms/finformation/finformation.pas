unit finformation;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls;

type

  { TFrmInformation }

  TFrmInformation = class(TForm)
    BtnClose: TButton;
    MemInformation: TMemo;
    procedure BtnCloseClick(Sender: TObject);
  private
    { private declarations }
  public
    { public declarations }
    procedure SetInfomation(value: string);
  end;

var
  FrmInformation: TFrmInformation;

implementation

{$R *.lfm}

procedure TFrmInformation.BtnCloseClick(Sender: TObject);
begin
  Close;
end;

procedure TFrmInformation.SetInfomation(value: string);
begin
  MemInformation.Clear;
  MemInformation.Append(value);

  MemInformation.Selstart := 0;
  MemInformation.SelLength := 0;
end;

end.

