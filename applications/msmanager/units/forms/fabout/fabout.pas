unit fabout;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls,
  ExtCtrls;

type

  { TFrmAbout }

  TFrmAbout = class(TForm)
    BtnClose: TButton;
    ImgLogo: TImage;
    LblName: TLabel;
    LblVersion: TLabel;
    LblCopyright: TLabel;
    procedure BtnCloseClick(Sender: TObject);
    procedure FormCreate(Sender: TObject);
  private
    { private declarations }
  public
    { public declarations }
  end; 

var
  FrmAbout: TFrmAbout;

implementation

{$R *.lfm}
uses utools;

{ TFrmAbout }

procedure TFrmAbout.FormCreate(Sender: TObject);
begin
    LblName.Caption := GetAppProductName;
    LblVersion.Caption:= ' v' + GetAppVersionTotal;
end;

procedure TFrmAbout.BtnCloseClick(Sender: TObject);
begin
  Close;
end;

end.

