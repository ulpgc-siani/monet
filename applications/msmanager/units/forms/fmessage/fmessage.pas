unit fmessage;

interface

uses
  Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, ExtCtrls, uconstants;

  function ShowWarning(const MainMessage: String; const AltMessage: String = EMPTY): TModalResult;
  function ShowError(const MainMessage: String; const AltMessage: String = EMPTY): TModalResult;
  function ShowInformation(const MainMessage: String; const AltMessage: String = EMPTY): TModalResult;
  function ShowConfirmation(const MainMessage: String; const AltMessage: String = EMPTY; Buttons: TMsgDlgButtons = [mbYes, mbNo]; DefaultButton: TMsgDlgBtn = mbYes): TModalResult;
  function ShowMessage(const Caption, MainMessage, AltMessage: String; Image: TPicture; Buttons: TMsgDlgButtons): TModalResult; overload;
  function ShowMessage(const Caption, MainMessage, AltMessage: String; Image: TPicture; Buttons: TMsgDlgButtons; Default: TMsgDlgBtn): TModalResult; overload;

type
  TWinDgMessage = class(TForm)
    PnlMessage: TPanel;
    LblMessage: TLabel;
    ImageList: TImageList;
    Image: TImage;
  end;

implementation

{$R *.lfm}

type
  TGDgMessage = class
  private
    FWinMessage: TWinDgMessage;

    FMessageType: TMsgDlgType;
    FsCaption: String;
    FsMainMessage: String;
    FsAltMessage: String;
    FModalResult: TModalResult;

    FButtons: TMsgDlgButtons;
    FDefaultButton: TMsgDlgBtn;
    FCancelButton: TMsgDlgBtn;
    FPicture: TPicture;

    FiButtonsCount: Integer;
    FButton: TButton;

    procedure CreateButtons(Buttons: TMsgDlgButtons);
    procedure SetImage(Idx: Integer);
    procedure UpdateDialog;

    procedure AtButtonClick(Sender: TObject);
    procedure AtImageMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure AtFormShow(Sender:TObject);

  public
    constructor Create;
    destructor Destroy; override;

    class function Execute(MessageType: TMsgDlgType; const MainMessage, AltMessage: String; Buttons: TMsgDlgButtons = []; DefaultButton: TMsgDlgBtn = mbYes): TModalResult;
    class function ExecuteCustom(const Caption, MainMessage, AltMessage: String; Buttons: TMsgDlgButtons = []; DefaultButton: TMsgDlgBtn = mbYes; Image: TPicture = nil): TModalResult;
  end;



const
  kCaptions: array[TMsgDlgType] of String = ('Warning', 'Error', 'Information', 'Confirmation', '');
  kImages: array[TMsgDlgType] of Integer = (0, 1, 2 , 3, -1);
  kButtonCaptions: array[TMsgDlgBtn] of String = ('&Yes', '&No', 'Aceptar', 'Cancelar', '&Abort', '&Retry', '&Ignore', 'All', 'Not to all', 'Yes to all', 'H&elp', 'Close');

  kModalResults: array[TMsgDlgBtn] of Integer = (mrYes, mrNo, mrOk, mrCancel, mrAbort, mrRetry, mrIgnore, mrAll, mrNoToAll, mrYesToAll, 0, 0);

  kButtonsSort: Array[0..10] of TMsgDlgBtn =
     (mbHelp, mbAbort, mbRetry, mbIgnore,
      mbCancel, mbNoToAll, mbNo, mbYes, mbOK, mbAll, mbYesToAll);

  kDefaultButtons: Array[0..10] of TMsgDlgBtn =
     (mbYes, mbOK, mbNo, mbCancel, mbAbort, mbRetry, mbIgnore,
      mbAll, mbNoToAll, mbYesToAll, mbHelp);

  kiButtonSeparation = 5;
  kiButtonWidth = 75;
  kiButtonHeight = 25;

//------------------------------------------------------------------------------

function GetDefaultButton(Buttons: TMsgDlgButtons): TMsgDlgBtn;
var
  i: Integer;
begin
  for i := Low(kDefaultButtons) to High(kDefaultButtons) do
    if kDefaultButtons[i] in Buttons then
    begin
      result := kDefaultButtons[i];
      exit;
    end;
  result := mbYes;
end;

//------------------------------------------------------------------------------

function ShowWarning(const MainMessage: String; const AltMessage: String = EMPTY): TModalResult;
begin
  result := TGDgMessage.Execute(mtWarning, MainMessage, AltMessage);
end;

//------------------------------------------------------------------------------

function ShowError(const MainMessage: String; const AltMessage: String = EMPTY): TModalResult;
begin
  result := TGDgMessage.Execute(mtError, MainMessage, AltMessage);
end;

//------------------------------------------------------------------------------

function ShowInformation(const MainMessage: String; const AltMessage: String = EMPTY): TModalResult;
begin
  result := TGDgMessage.Execute(mtInformation, MainMessage, AltMessage);
end;

//------------------------------------------------------------------------------

function ShowConfirmation(const MainMessage: String; const AltMessage: String = EMPTY; Buttons: TMsgDlgButtons = [mbYes, mbNo]; DefaultButton: TMsgDlgBtn = mbYes): TModalResult;
begin
  result := TGDgMessage.Execute(mtConfirmation, MainMessage, AltMessage, Buttons, DefaultButton);
end;

//------------------------------------------------------------------------------

function ShowMessage(const Caption, MainMessage, AltMessage: String; Image: TPicture; Buttons: TMsgDlgButtons): TModalResult;
begin
  result := TGDgMessage.ExecuteCustom(Caption, MainMessage, AltMessage, Buttons, GetDefaultButton(Buttons), Image);
end;

//------------------------------------------------------------------------------

function ShowMessage(const Caption, MainMessage, AltMessage: String; Image: TPicture; Buttons: TMsgDlgButtons; Default: TMsgDlgBtn): TModalResult;
begin
  result := TGDgMessage.ExecuteCustom(Caption, MainMessage, AltMessage, Buttons, Default, Image);
end;

//------------------------------------------------------------------------------

constructor TGDgMessage.Create;
begin
  inherited;
  FPicture := TPicture.Create;
  FWinMessage := TWinDgMessage.Create(nil);
end;

//------------------------------------------------------------------------------

destructor TGDgMessage.Destroy;
begin
  FPicture.Free;
  FWinMessage.Free;
  inherited;
end;

//------------------------------------------------------------------------------

procedure TGDgMessage.UpdateDialog;
begin
  with FWinMessage do
  begin
    LblMessage.Caption := FsMainMessage;
    case FMessageType of
      mtWarning:
      begin
        Caption := kCaptions[FMessageType];
        FButtons := [mbOK];
        FDefaultButton := mbOk;
        FCancelButton := mbOk;
      end;
      mtError:
      begin
        Caption := kCaptions[FMessageType];
        FButtons := [mbOK];
        FDefaultButton := mbOk;
        FCancelButton := mbOk;
      end;
      mtInformation:
      begin
        Caption := kCaptions[FMessageType];
        FButtons := [mbOK];
        FDefaultButton := mbOk;
        FCancelButton := mbOk;
      end;
      mtConfirmation:
      begin
        Caption := kCaptions[FMessageType];
        if mbOk in FButtons then
          FDefaultButton := mbOk;

        if mbCancel in FButtons then
          FCancelButton := mbCancel
        else if mbNo in FButtons then
          FCancelButton := mbNo;
      end;
      mtCustom:
      begin
        Caption := FsCaption;
        FCancelButton := mbCancel;
      end;
    end;
    Image.OnMouseDown := @AtImageMouseDown;
  end;

  CreateButtons(FButtons);
  SetImage(kImages[FMessageType]);
  FWinMessage.ActiveControl := FWinMessage.PnlMessage;
end;

//------------------------------------------------------------------------------

procedure TGDgMessage.CreateButtons(Buttons: TMsgDlgButtons);
var
  iLeft, iTop: Integer;

  //-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -

  procedure DestroyButtons(Parent: TWinControl);
  var
    i: Integer;
    Control: TControl;
  begin
    if Parent = nil then exit;

    for i := Parent.ControlCount - 1 downto 0 do
    begin
      Control := Parent.Controls[i];
      if not (Control is TWinControl) then continue;

      if Control is TButton then
        Control.Free
      else
        DestroyButtons(Control as TWinControl);
    end;

    FiButtonsCount := 0;
    FButton := nil;
  end;

  //-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -

  function CreateButton(MsgBtn: TMsgDlgBtn): TButton;
  begin
    Inc(FiButtonsCount);

    FButton := TButton.Create(FWinMessage);
    with FButton do
    begin
      Parent := FWinMessage;
      Caption := kButtonCaptions[MsgBtn];
      ModalResult := kModalResults[MsgBtn];
      Width := kiButtonWidth;
      Height := kiButtonHeight;
      Top := iTop;
      Left := iLeft;
      TabOrder := 0;
      Anchors := [akRight, akBottom];
      OnClick := @AtButtonClick;
      if MsgBtn = FDefaultButton then Default := True;
      if MsgBtn = FCancelButton then Cancel := True;
    end;

    Dec(iLeft, kiButtonWidth + kiButtonSeparation);
    result := FButton;
  end;

  //-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -

var
  i: Integer;
  Button: TButton;
  MsgBtn: TMsgDlgBtn;
begin
  DestroyButtons(FWinMessage);

  with FWinMessage do
  begin
    Width := 0;

    iLeft := ClientWidth - kiButtonWidth - 5;
    iTop := ClientHeight - kiButtonHeight - 5;

    Button := nil;
    for i := Low(kButtonsSort) to High(kButtonsSort) do
    begin
      MsgBtn := kButtonsSort[i];
      if MsgBtn in Buttons then Button := CreateButton(MsgBtn);
    end;

    if (Button <> nil) and (Button.Left < 0) then
      Width := Width - Button.Left;
  end;
end;

//------------------------------------------------------------------------------

procedure TGDgMessage.SetImage(Idx: Integer);
var
  Icon: TBitMap;

begin
  if Idx < 0 then
    FWinMessage.Image.Picture.Assign(FPicture)
  else
  begin
    Icon := TBitMap.Create;
    try
      FWinMessage.ImageList.GetBitMap(Idx, Icon);
      FWinMessage.Image.Picture.Bitmap := Icon;
    finally
      Icon.Free;
    end;
  end;
end;

//------------------------------------------------------------------------------

procedure TGDgMessage.AtButtonClick(Sender: TObject);
begin
  FModalResult := (Sender as TButton).ModalResult;
  FWinMessage.Close;
end;

//------------------------------------------------------------------------------

procedure TGDgMessage.AtImageMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
begin
  with FWinMessage do
  begin
    if (ssCtrl in Shift) and (ssAlt in Shift) and (ssLeft in Shift) then
    begin
      if LblMessage.Caption = FsMainMessage then
        LblMessage.Caption := FsAltMessage
      else
        LblMessage.Caption := FsMainMessage
    end;
  end;
end;

//------------------------------------------------------------------------------

procedure TGDgMessage.AtFormShow(Sender: TObject);
var
  x: integer;
  finddefaultcomponent: boolean;
  Button: TButton;
begin
  with FWinMessage do
  begin
    if not Application.MainForm.Active then
      Position := poScreenCenter;

    if FWinMessage.ComponentCount > 0 then
    begin
      finddefaultcomponent := false;
      x :=0;
      while not finddefaultcomponent and (x < FWinMessage.ComponentCount) do
      begin
        if (FWinMessage.Components[x] is TButton) then
        begin
          Button := TButton(FWinMessage.Components[x]);
          if Button.Default then Button.SetFocus;
        end;
        inc(x);
      end;
    end;
  end;
end;

//------------------------------------------------------------------------------

class function TGDgMessage.Execute(MessageType: TMsgDlgType; const MainMessage, AltMessage: String; Buttons: TMsgDlgButtons; DefaultButton: TMsgDlgBtn): TModalResult;
var
  DgMessage: TGDgMessage;
begin
  DgMessage := TGDgMessage.Create;
  try
    with DgMessage do
    begin
      if Buttons <> [] then
        FButtons := Buttons;
      FDefaultButton := DefaultButton;
      FMessageType := MessageType;
      FsMainMessage := MainMessage;
      FsAltMessage := AltMessage;
      if FsAltMessage = EMPTY then FsAltMessage := FsMainMessage;
      UpdateDialog;
      FWinMessage.OnShow := @AtFormShow;
      FWinMessage.FormStyle := fsStayOnTop;
      FWinMessage.BringToFront;
      FWinMessage.ShowModal;

      result := FModalResult;
    end;
  finally
    DgMessage.Free;
  end;
end;

//------------------------------------------------------------------------------

class function TGDgMessage.ExecuteCustom(const Caption, MainMessage, AltMessage: String; Buttons: TMsgDlgButtons;
                                 DefaultButton: TMsgDlgBtn; Image: TPicture): TModalResult;
var
  DgMessage: TGDgMessage;
begin
  DgMessage := TGDgMessage.Create;
  try
    with DgMessage do
    begin
      FsCaption := Caption;

      if Buttons <> [] then
        FButtons := Buttons;
      FDefaultButton := DefaultButton;
      if Image <> nil then
        FPicture.Assign(Image);

      FMessageType := mtCustom;
      FsMainMessage := MainMessage;
      FsAltMessage := AltMessage;
      if FsAltMessage = EMPTY then FsAltMessage := FsMainMessage;
      UpdateDialog;
      FWinMessage.ShowModal;
      result := FModalResult;
    end;
  finally
    DgMessage.Free;
  end;
end;

//------------------------------------------------------------------------------

end.
