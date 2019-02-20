unit fwait;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls,
  ComCtrls, ExtCtrls;

type
  TOperationWait = (opFormShow, opCancelDownload);
  TOperationWaitEvent = procedure(Sender: TObject; Operation: TOperationWait) of object;

  { TFrmWait }
  TFrmWait = class(TForm)
    BtnCancel: TButton;
    LblInfo: TLabel;
    PrbWait: TProgressBar;
    LblSubInfo: TLabel;
    procedure BtnCancelClick(Sender: TObject);
    procedure FormShow(Sender: TObject);
  private
    { private declarations }
    FOnOperation: TOperationWaitEvent;

  public
    { public declarations }
    procedure SetTitle(title: string);
    procedure SetContent(info: string; subinfo: string = '');
    procedure SetBarStyle(value: TProgressBarStyle);
    procedure SetBarMax(value: integer);
    procedure SetBarPosition(value: integer);
    procedure ShowError(message: string);
    procedure ShowInfo(message: string);

    property OnOperation: TOperationWaitEvent read FOnOperation write FOnOperation;
  end;

implementation

{$R *.lfm}
uses LCLType, fmessage;

procedure TFrmWait.FormShow(Sender: TObject);
begin
  OnOperation(Sender, opFormShow);
end;

procedure TFrmWait.BtnCancelClick(Sender: TObject);
begin
  OnOperation(Sender, opCancelDownload);
end;

procedure TFrmWait.SetTitle(title: string);
begin
  Caption := title;
end;

procedure TFrmWait.SetContent(info: string; subinfo: string = '');
begin
  LblInfo.Caption := info;
  LblSubInfo.Caption := subinfo;
end;

procedure TFrmWait.SetBarStyle(value: TProgressBarStyle);
begin
  PrbWait.Style:= value;;
end;

procedure TFrmWait.SetBarMax(value: integer);
begin
  PrbWait.Max:= value;
end;

procedure TFrmWait.SetBarPosition(value: integer);
begin
  PrbWait.Position := value;
end;

procedure TFrmWait.ShowError(message: string);
begin
  MessageDlg(Message, mtError, [mbOk],0);
end;

procedure TFrmWait.ShowInfo(message: string);
begin
  MessageDlg(Message, mtInformation, [mbOk],0);
end;

end.
