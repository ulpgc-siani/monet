unit fconsole;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls;

type

  { TFrmConsole }

  TFrmConsole = class(TForm)
    BtnClose: TButton;
    MemConsole: TMemo;
    procedure BtnCloseClick(Sender: TObject);
  private
    { private declarations }
  public
    { public declarations }
  end; 

var
  FrmConsole: TFrmConsole;

implementation

{$R *.lfm}

{ TFrmConsole }

procedure TFrmConsole.BtnCloseClick(Sender: TObject);
begin
  Close;
end;

end.

