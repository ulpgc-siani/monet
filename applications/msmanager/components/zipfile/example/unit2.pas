unit Unit2; 

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, LResources, Forms, Controls, Graphics, Dialogs, StdCtrls;

type

  { TForm1 }

  TForm1 = class(TForm)
    FileDataMemo: TMemo;
    procedure FileDataMemoChange(Sender: TObject);
    procedure FormShow(Sender: TObject);
  private
    { private declarations }
  public
    { public declarations }
    DataChanged: boolean;
  end;

var
  Form1: TForm1; 

implementation

{ TForm1 }

procedure TForm1.FileDataMemoChange(Sender: TObject);
begin
  DataChanged := True;
end;

procedure TForm1.FormShow(Sender: TObject);
begin
  DataChanged := False;
end;

initialization
  {$I unit2.lrs}

end.

