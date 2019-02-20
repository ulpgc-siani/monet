unit fpreferences;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls,
  ComCtrls;

type

  { TFrmPreferences }
  TOperationPreferences = (opSave);
  TOperationPreferencesEvent = procedure(Sender: TObject; Operation: TOperationPreferences) of object;

  TFrmPreferences = class(TForm)
    BtnOk: TButton;
    BtnCancel: TButton;
    BtnSSHKeyFileBrowse: TButton;
    BtnRemoteServersConfigFile: TButton;
    BtnRemoteServersAdd: TButton;
    BtnRemoteServersDelete: TButton;
    ChkDebug: TCheckBox;
    ChkMirrorUlpgc: TCheckBox;
    ChkVersionBeta: TCheckBox;
    ChkUpdatesStart: TCheckBox;
    CmbIPs: TComboBox;
    LblRemoteServersFiles: TLabel;
    LstRemoteServers: TListBox;
    SvRemoteServersConfigFile: TSaveDialog;
    TabRemoteServers: TTabSheet;
    TxtRemoteServersConfigFile: TEdit;
    LblRemoteServersConfigFile: TLabel;
    OpDSSHKeyFile: TOpenDialog;
    TxtSSHKeyFile: TEdit;
    LblSSHKeyFile: TLabel;
    LblIPs: TLabel;
    ControlPreferences: TPageControl;
    TabGeneral: TTabSheet;
    TabSSH: TTabSheet;
    procedure BtnCancelClick(Sender: TObject);
    procedure BtnOkClick(Sender: TObject);
    procedure BtnRemoteServersAddClick(Sender: TObject);
    procedure BtnRemoteServersConfigFileClick(Sender: TObject);
    procedure BtnRemoteServersDeleteClick(Sender: TObject);
    procedure BtnSSHKeyFileBrowseClick(Sender: TObject);
    procedure FormShow(Sender: TObject);
  private
    { private declarations }
    FOnOperation: TOperationPreferencesEvent;

    procedure SetVersionBeta(value: boolean);
    function GetVersionBeta: boolean;

    procedure SetMirrorUlpgc(value: boolean);
    function GetMirrorUlpgc: boolean;

    procedure SetLocalIPs(value: string);
    function GetLocalIPs: string;

    procedure SetDefaultIP(value: string);
    function GetDefaultIP: string;

    procedure SetSSHKeyFile(value: string);
    function GetSSHKeyFile: string;

    procedure SetDebug(value: boolean);
    function GetDebug: boolean;

    procedure SetUpdatesStart(value: boolean);
    function GetUpdatesStart: boolean;

    procedure SetRemoteServersConfigFile(value: string);
    function GetRemoteServersConfigFile: string;

  public
    { public declarations }
    property VersionBeta: boolean read GetVersionBeta write SetVersionBeta;
    property MirrorUlpgc: boolean read GetMirrorUlpgc write SetMirrorUlpgc;
    property LocalIPs: string read GetLocalIPs write SetLocalIPs;
    property DefaultIP: string read GetDefaultIP write SetDefaultIP;
    property SSHKeyFile: string read GetSSHKeyFile write SetSSHKeyFile;
    property Debug: boolean read GetDebug write SetDebug;
    property UpdatesStart: boolean read GetUpdatesStart write SetUpdatesStart;
    property RemoteServersConfigFile: string read GetRemoteServersConfigFile write SetRemoteServersConfigFile;

    property OnOperation: TOperationPreferencesEvent read FOnOperation write FOnOperation;
  end;

var
  FrmPreferences: TFrmPreferences;

implementation

{$R *.lfm}

{ TFrmPreferences }

procedure TFrmPreferences.BtnCancelClick(Sender: TObject);
begin
  Close;
end;

procedure TFrmPreferences.BtnOkClick(Sender: TObject);
begin
  OnOperation(Sender, opSave);
  Close;
end;

procedure TFrmPreferences.BtnRemoteServersAddClick(Sender: TObject);
begin
  if SvRemoteServersConfigFile.Execute then
    LstRemoteServers.Items.Add(SvRemoteServersConfigFile.FileName);
end;

procedure TFrmPreferences.BtnRemoteServersConfigFileClick(Sender: TObject);
begin
  if SvRemoteServersConfigFile.Execute then
    TxtRemoteServersConfigFile.Text:= SvRemoteServersConfigFile.FileName;
end;

procedure TFrmPreferences.BtnRemoteServersDeleteClick(Sender: TObject);
begin
//  writeln(LstRemoteServers.ItemIndex);
  if LstRemoteServers.ItemIndex >= 0 then
    LstRemoteServers.Items.Delete(LstRemoteServers.ItemIndex);
end;

procedure TFrmPreferences.BtnSSHKeyFileBrowseClick(Sender: TObject);
begin
  if OpDSSHKeyFile.Execute then
    TxtSSHKeyFile.Text:= OpDSSHKeyFile.FileName;
end;

procedure TFrmPreferences.FormShow(Sender: TObject);
begin
  ControlPreferences.TabIndex:= 0;
end;

procedure TFrmPreferences.SetVersionBeta(value: boolean); begin ChkVersionBeta.Checked:= value; end;
function TFrmPreferences.GetVersionBeta: boolean; begin Result := ChkVersionBeta.Checked; end;

procedure TFrmPreferences.SetMirrorUlpgc(value: boolean); begin ChkMirrorUlpgc.Checked:= value; end;
function TFrmPreferences.GetMirrorUlpgc: boolean; begin Result := ChkMirrorUlpgc.Checked; end;

procedure TFrmPreferences.SetLocalIPs(value: string); begin CmbIPs.Items.Text:=value;end;
function TFrmPreferences.GetLocalIPs: string; begin Result := CmbIPs.Items.Text; end;

procedure TFrmPreferences.SetDefaultIP(value: string); begin CmbIPs.Text:=value; end;
function TFrmPreferences.GetDefaultIP: string; begin Result := CmbIPs.Text; end;

procedure TFrmPreferences.SetSSHKeyFile(value: string); begin TxtSSHKeyFile.Text := value; end;
function TFrmPreferences.GetSSHKeyFile: string; begin Result := TxtSSHKeyFile.Text; end;

procedure TFrmPreferences.SetDebug(value: boolean); begin ChkDebug.Checked:= value; end;
function TFrmPreferences.GetDebug: boolean; begin Result := ChkDebug.Checked; end;

procedure TFrmPreferences.SetUpdatesStart(value: boolean); begin ChkUpdatesStart.Checked:= value; end;
function TFrmPreferences.GetUpdatesStart: boolean; begin Result := ChkUpdatesStart.Checked; end;

procedure TFrmPreferences.SetRemoteServersConfigFile(value: string);
begin
  LstRemoteServers.Items.Clear;
  LstRemoteServers.Items.Text:= value;
end;
function TFrmPreferences.GetRemoteServersConfigFile: string;
begin
  Result := LstRemoteServers.Items.Text;
end;

end.
