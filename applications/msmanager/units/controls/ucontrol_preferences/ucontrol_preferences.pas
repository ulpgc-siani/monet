unit ucontrol_preferences;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, fpreferences, forms, uagent_preferences;

type
  TControlPreferences = class
    private
      FrmPreferences: TFrmPreferences;
      AFrmMain: TForm;
      FAgentPreferences: TAgentPreferences;
      FIsChangeIP: boolean;

      procedure AtOperation(Sender: TObject; Operation: TOperationPreferences);
    public
      constructor Create(FrmMain: TForm; AgentPreferences: TAgentPreferences);
      destructor Destroy; override;

      procedure Execute;
      procedure ChangeTabIndex(Index: integer);

      property IsChangeIP: boolean read FIsChangeIP;
  end;

implementation

constructor TControlPreferences.Create(FrmMain: TForm; AgentPreferences: TAgentPreferences);
begin
  AFrmMain := FrmMain;
  Application.CreateForm(TFrmPreferences, FrmPreferences);
  FrmPreferences.OnOperation:= @AtOperation;
  FAgentPreferences := AgentPreferences;
end;

destructor TControlPreferences.Destroy;
begin
  FrmPreferences.Free;
  inherited;
end;

procedure TControlPreferences.Execute;
begin
  FIsChangeIP := false;
  FrmPreferences.LocalIPs:= FAgentPreferences.LocalIPs;
  FrmPreferences.DefaultIP:= FAgentPreferences.IP;
  FrmPreferences.VersionBeta:= FAgentPreferences.VersionBeta;
  FrmPreferences.MirrorUlpgc:= FAgentPreferences.MirrorUlpgc;
  FrmPreferences.SSHKeyFile:= FAgentPreferences.SSHKeyFile;
  FrmPreferences.Debug:= FAgentPreferences.Debug;
  FrmPreferences.UpdatesStart := FAgentPreferences.UpdatesStart;
  FrmPreferences.RemoteServersConfigFile:= FAgentPreferences.RemoteServersConfigFile;

  FrmPreferences.ShowModal;
end;

procedure TControlPreferences.ChangeTabIndex(Index: integer);
begin
  FAgentPreferences.LoadConfig;
  FAgentPreferences.TabIndex := Index;
  FAgentPreferences.SaveConfig;
end;

procedure TControlPreferences.AtOperation(Sender: TObject; Operation: TOperationPreferences);
begin
  case Operation of
    opSave:
    begin
      if FrmPreferences.DefaultIP <> FAgentPreferences.IP then FIsChangeIP := true;
      FAgentPreferences.IP:= FrmPreferences.DefaultIP;
      FAgentPreferences.VersionBeta := FrmPreferences.VersionBeta;
      FAgentPreferences.MirrorUlpgc := FrmPreferences.MirrorUlpgc;
      FAgentPreferences.SSHKeyFile := FrmPreferences.SSHKeyFile;
      FAgentPreferences.Debug := FrmPreferences.Debug;
      FAgentPreferences.RemoteServersConfigFile := FrmPreferences.RemoteServersConfigFile;
      FAgentPreferences.UpdatesStart := FrmPreferences.UpdatesStart;
      FAgentPreferences.SaveConfig;
    end;
  end;

end;

end.

