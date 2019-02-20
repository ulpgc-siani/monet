unit flogin;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls;

type

  { TFrmLogin }

  TFrmLogin = class(TForm)
    BtnOk: TButton;
    BtnCancel: TButton;
    TxtDeployServicePassword: TEdit;
    TxtDeployServiceUser: TEdit;
    LblDeployServiceUser: TLabel;
    LblDeployServicePassword: TLabel;
    procedure BtnCancelClick(Sender: TObject);
    procedure BtnOkClick(Sender: TObject);
    procedure FormShow(Sender: TObject);
  private
    { private declarations }
    function GetUser: string;
    function GetPassword: string;

    procedure SetUser(value: string);
    procedure SetPassword(value: string);
  public
    { public declarations }
    Cancel: boolean;

    property User: string read GetUser write SetUser;
    property Password: string read GetPassword write SetPassword;
  end;

var
  FrmLogin: TFrmLogin;

implementation

{$R *.lfm}

{ TFrmLogin }

procedure TFrmLogin.FormShow(Sender: TObject);
begin
  TxtDeployServiceUser.SetFocus;
//  ModalResult := mrOk;
end;

procedure TFrmLogin.BtnOkClick(Sender: TObject);
begin
  ModalResult := mrOk;
//  Close;
end;

procedure TFrmLogin.BtnCancelClick(Sender: TObject);
begin
  ModalResult := mrCancel;
//  Close;
end;

procedure TFrmLogin.SetUser(value: string);
begin
  TxtDeployServiceUser.Text:= value;
end;

function TFrmLogin.GetUser: string;
begin
  Result := TxtDeployServiceUser.Text;
end;

procedure TFrmLogin.SetPassword(value: string);
begin
  TxtDeployServicePassword.Text:= value;
end;

function TFrmLogin.GetPassword: string;
begin
  Result := TxtDeployServicePassword.Text;
end;

end.

