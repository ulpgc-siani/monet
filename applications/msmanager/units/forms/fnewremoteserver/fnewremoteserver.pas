unit fnewremoteserver;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls;

type

  { TFrmNewRemoteServer }

  TFrmNewRemoteServer = class(TForm)
    BtnOk: TButton;
    BtnCancel: TButton;
    CmbFileConfig: TComboBox;
    CmbDeployType: TComboBox;
    LblFieldRequired: TLabel;
    LblFileConfig: TLabel;
    LblDeployType: TLabel;
    LblUpdateAppsFullUrl: TLabel;
    LblName: TLabel;
    LblLoggingHost: TLabel;
    LblPort: TLabel;
    LblDeployServiceDomain: TLabel;
    LblLoggingPort: TLabel;
    TxtHost: TEdit;
    TxtUpdateAppsFullUrl: TEdit;
    TxtName: TEdit;
    TxtDeployServicePort: TEdit;
    TxtDeployServiceUser: TEdit;
    TxtDeployServicePassword: TEdit;
    LblHost: TLabel;
    LblDeployServicePort: TLabel;
    LblDeployServiceUser: TLabel;
    LblDeployServicePassword: TLabel;
    TxtLoggingHost: TEdit;
    TxtPort: TEdit;
    TxtDeployServiceDomain: TEdit;
    TxtLoggingPort: TEdit;
    procedure BtnCancelClick(Sender: TObject);
    procedure BtnOkClick(Sender: TObject);
    procedure FormShow(Sender: TObject);
  private
    { private declarations }
    FResult: boolean;

    function GetName: string;
    function GetHost: string;
    function GetPort: string;
    function GetDeployServicePort: string;
    function GetDeployServiceDomain: string;
    function GetDeployServiceUser: string;
    function GetDeployServicePassword: string;
    function GetLoggingHost: string;
    function GetLoggingPort: string;
    function GetUpdateAppsFullUrl: string;
    function GetFileConfig: string;
    function GetDeployType: string;

    procedure SetNameServer(value: string);
    procedure SetHost(value: string);
    procedure SetPort(value: string);
    procedure SetDeployServicePort(value: string);
    procedure SetDeployServiceDomain(value: string);
    procedure SetDeployServiceUser(value: string);
    procedure SetDeployServicePassword(value: string);
    procedure SetLoggingHost(value: string);
    procedure SetLoggingPort(value: string);
    procedure SetUpdateAppsFullUrl(value: string);
    procedure SetFileConfig(value: string);
    procedure SetDeployType(value: string);


  public
    { public declarations }
    procedure Clear;
    procedure SetConfigFiles(value: TStringList);

    property Result: boolean read FResult;

    property Name: string read GetName write SetNameServer;
    property Host: string read GetHost write SetHost;
    property Port: string read GetPort write SetPort;
    property DeployType: string read GetDeployType write SetDeployType;
    property DeployServicePort: string read GetDeployServicePort write SetDeployServicePort;
    property DeployServiceDomain: string read GetDeployServiceDomain write SetDeployServiceDomain;
    property DeployServiceUser: string read GetDeployServiceUser write SetDeployServiceUser;
    property DeployServicePassword: string read GetDeployServicePassword write SetDeployServicePassword;
    property LoggingHost: string read GetLoggingHost write SetLoggingHost;
    property LoggingPort: string read GetLoggingPort write SetLoggingPort;
    property UpdateAppsFullUrl: string read GetUpdateAppsFullUrl write SetUpdateAppsFullUrl;
    property FileConfig: string read GetFileConfig write SetFileConfig;
  end;

var
  FrmNewRemoteServer: TFrmNewRemoteServer;

implementation

{$R *.lfm}
uses fmessage;
{ TFrmNewRemoteServer }

function TFrmNewRemoteServer.GetName: string; begin Result := TxtName.Text; end;
function TFrmNewRemoteServer.GetHost: string; begin Result := TxtHost.Text; end;
function TFrmNewRemoteServer.GetPort: string; begin Result := TxtPort.Text; end;
function TFrmNewRemoteServer.GetDeployServicePort: string; begin Result := TxtDeployServicePort.Text; end;
function TFrmNewRemoteServer.GetDeployServiceDomain: string; begin Result := TxtDeployServiceDomain.Text; end;
function TFrmNewRemoteServer.GetDeployServiceUser: string; begin Result := TxtDeployServiceUser.Text; end;
function TFrmNewRemoteServer.GetDeployServicePassword: string; begin Result := TxtDeployServicePassword.Text; end;
function TFrmNewRemoteServer.GetLoggingHost: string; begin Result := TxtLoggingHost.Text; end;
function TFrmNewRemoteServer.GetLoggingPort: string; begin Result := TxtLoggingPort.Text; end;
function TFrmNewRemoteServer.GetUpdateAppsFullUrl: string; begin Result := TxtUpdateAppsFullUrl.Text; end;
function TFrmNewRemoteServer.GetFileConfig: string; begin Result := CmbFileConfig.Text; end;
function TFrmNewRemoteServer.GetDeployType: string; begin Result := CmbDeployType.Text; end;

procedure TFrmNewRemoteServer.SetNameServer(value: string); begin TxtName.Text := value; end;
procedure TFrmNewRemoteServer.SetHost(value: string); begin TxtHost.Text := value; end;
procedure TFrmNewRemoteServer.SetPort(value: string); begin TxtPort.Text := value; end;
procedure TFrmNewRemoteServer.SetDeployServicePort(value: string); begin TxtDeployServicePort.Text := value; end;
procedure TFrmNewRemoteServer.SetDeployServiceDomain(value: string); begin TxtDeployServiceDomain.Text := value; end;
procedure TFrmNewRemoteServer.SetDeployServiceUser(value: string); begin TxtDeployServiceUser.Text := value; end;
procedure TFrmNewRemoteServer.SetDeployServicePassword(value: string); begin TxtDeployServicePassword.Text := value; end;
procedure TFrmNewRemoteServer.SetLoggingHost(value: string); begin TxtLoggingHost.Text := value; end;
procedure TFrmNewRemoteServer.SetLoggingPort(value: string); begin TxtLoggingPort.Text := value; end;
procedure TFrmNewRemoteServer.SetUpdateAppsFullUrl(value: string); begin TxtUpdateAppsFullUrl.Text := value; end;
procedure TFrmNewRemoteServer.SetFileConfig(value: string); begin CmbFileConfig.ItemIndex := CmbFileConfig.Items.IndexOf(value); end;
procedure TFrmNewRemoteServer.SetDeployType(value: string); begin CmbDeployType.ItemIndex := CmbDeployType.Items.IndexOf(value); end;

procedure TFrmNewRemoteServer.SetConfigFiles(value: TStringList);
var
  x: integer;
begin
  for x := 0 to value.Count-1 do
    CmbFileConfig.Items.Add(value.Strings[x]);

  if CmbFileConfig.Items.Count > 0 then
    CmbFileConfig.ItemIndex := 0;
end;

procedure TFrmNewRemoteServer.BtnOkClick(Sender: TObject);
begin
  if (TxtName.Text <> '') and
     (TxtHost.Text <> '') and
     (TxtPort.Text <> '') and
     (CmbDeployType.Text <> '') and
     (TxtDeployServicePort.Text <> '') and
     (TxtDeployServiceDomain.Text <> '') and
     (TxtDeployServiceUser.Text <> '') and
     (CmbFileConfig.Text <> '') then
  begin
    FResult := true;
    Close;
  end
  else
    ShowWarning('You must fill in all required fields.');
end;

procedure TFrmNewRemoteServer.Clear;
begin
  TxtName.Text := '';
  TxtHost.Text := '';
  TxtPort.Text := '80';
  CmbDeployType.ItemIndex:=0;
  TxtDeployServicePort.Text := '';
  TxtDeployServiceDomain.Text := '';
  TxtDeployServiceUser.Text := '';
  TxtDeployServicePassword.Text := '';
  TxtLoggingHost.Text := '';
  TxtLoggingPort.Text := '';
  TxtUpdateAppsFullUrl.Text := '';
  CmbFileConfig.Clear;
end;

procedure TFrmNewRemoteServer.FormShow(Sender: TObject);
begin
  FResult := false;
  TxtName.SetFocus;
end;

procedure TFrmNewRemoteServer.BtnCancelClick(Sender: TObject);
begin
  Close;
end;

end.

