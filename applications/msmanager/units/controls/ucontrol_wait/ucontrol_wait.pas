unit ucontrol_wait;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, fwait, forms, ucontrol_commandthread;

type
  TControlWait = class
    private
      FrmWait: TFrmWait;
      AFrmMain: TForm;
      AControlCommandThread: TControlCommandThread;

      procedure AtOperation(Sender: TObject; Operation: TOperationWait);
    public
      constructor Create(FrmMain: TForm);
      destructor Destroy; override;

      procedure Execute(ControlCommandThreads: TControlCommandThreads); overload;
      procedure Execute(ControlCommandThread: TControlCommandThread); overload;
  end;

implementation

constructor TControlWait.Create(FrmMain: TForm);
begin
  AFrmMain := FrmMain;
end;

destructor TControlWait.Destroy;
begin
  inherited;
end;

procedure TControlWait.Execute(ControlCommandThreads: TControlCommandThreads);
begin
  FrmWait.Free;
  Application.CreateForm(TFrmWait, FrmWait);
  FrmWait.OnOperation:= @AtOperation;

  AControlCommandThread := ControlCommandThreads.ControlCommandThread;
  FrmWait.SetTitle(AControlCommandThread.CommandInfo.Title);
  FrmWait.SetContent(AControlCommandThread.CommandInfo.Content, AControlCommandThread.CommandInfo.SubContent);
  if ControlCommandThreads.ControlCommandThreadRefresh <> nil then ControlCommandThreads.ControlCommandThreadRefresh.StopRefresh;
  if ControlCommandThreads.ControlCommandThreadConfigure <> nil then ControlCommandThreads.ControlCommandThreadConfigure.StopRefresh;
  FrmWait.ShowModal;
  if ControlCommandThreads.ControlCommandThreadRefresh <> nil then ControlCommandThreads.ControlCommandThreadRefresh.StartRefresh;
  if ControlCommandThreads.ControlCommandThreadConfigure <> nil then ControlCommandThreads.ControlCommandThreadConfigure.StartRefresh;

end;

procedure TControlWait.Execute(ControlCommandThread: TControlCommandThread);
begin
  FrmWait.Free;
  Application.CreateForm(TFrmWait, FrmWait);
  FrmWait.OnOperation:= @AtOperation;

  AControlCommandThread := ControlCommandThread;
  FrmWait.SetTitle(AControlCommandThread.CommandInfo.Title);
  FrmWait.SetContent(AControlCommandThread.CommandInfo.Content, AControlCommandThread.CommandInfo.SubContent);
  FrmWait.ShowModal;

end;

procedure TControlWait.AtOperation(Sender: TObject; Operation: TOperationWait);
begin
  case Operation of
    opFormShow:
    begin
      AControlCommandThread.OnWaitHide:= @FrmWait.Close;
      AControlCommandThread.OnWaitUpdateText:= @FrmWait.SetContent;
      AControlCommandThread.OnWaitBarStyle:= @FrmWait.SetBarStyle;
      AControlCommandThread.OnWaitBarMax:= @FrmWait.SetBarMax;
      AControlCommandThread.OnWaitBarPosition:= @FrmWait.SetBarPosition;
      AControlCommandThread.OnWaitShowError:= @FrmWait.ShowError;
      AControlCommandThread.OnWaitShowInfo := @FrmWait.ShowInfo;
      AControlCommandThread.Resume;
    end;
    opCancelDownload:
    begin
      AControlCommandThread.CancelDownload;
      FrmWait.Close;
    end;

  end;
end;

end.

