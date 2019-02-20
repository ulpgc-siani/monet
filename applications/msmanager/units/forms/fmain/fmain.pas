unit fmain;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls,
  Buttons, ExtCtrls, Menus, Grids, ComCtrls, PopupNotifier, utypes, uagent_preferences;

const
  ServerListTop = 7;
  ServerListLeft = 13;
  ServerListWidth = 777;
  ServerListHeight = 447;

type

  TOperationMain = (opAddMachine, opRefreshMachines, opStartMachine, opStopMachine, opDeleteMachine, opChangeMachine, opShowInfoMachineLocal, opShowInfoMachineRemote, opImportMachine, opPauseMachine, opExportMachine, opShowConsole, opShowAbout, opClose, opDownloadMSG, opRestartServer, opFormShow, opShowSpacesLocal, opShowSpacesRemote, opConfigureMachine, opConfigureAllMachines, opPreferences, opChangeStatusRefresh, opStopThreads, opStartThreads, opDisableAllButtons, opAddRemoteServer, opDelRemoteServer, opChangeTabIndex, opRefreshListRemote, opModifyRemoteServer);
  TOperationMainEvent = procedure(Sender: TObject; Operation: TOperationMain) of object;

  { TFrmMain }
  TFrmMain = class(TForm)
    BtnCreate: TBitBtn;
    BtnAddRemoteServer: TBitBtn;
    BtnCreatePanNoServers: TButton;
    BtnAddPanNoServersRemote: TButton;
    BtnDeleteLocalServer: TBitBtn;
    BtnDeleteRemoteServer: TBitBtn;
    BtnModifyRemoteServer: TBitBtn;
    BtnExit: TButton;
    BtnStart: TBitBtn;
    BtnStop: TBitBtn;
    TxtServersRemoteFilter: TEdit;
    LblLoading: TLabel;
    LblNoServers: TLabel;
    LblNoServersRemote: TLabel;
    MainMenu: TMainMenu;
    MenuCreate: TMenuItem;
    MenuExport: TMenuItem;
    MenuOpenPopRemote: TMenuItem;
    MenuPreferences: TMenuItem;
    MenuPropertiesRemote: TMenuItem;
    MenuSeparator6: TMenuItem;
    MenuSeparator8: TMenuItem;
    MenuOpen: TMenuItem;
    MenuSeparator7: TMenuItem;
    MenuSeparator5: TMenuItem;
    MenuOpenPop: TMenuItem;
    MenuProperties: TMenuItem;
    MenuSeparator4: TMenuItem;
    MenuRestartPop: TMenuItem;
    MenuStopPop: TMenuItem;
    MenuPausePop: TMenuItem;
    MenuStartPop: TMenuItem;
    MenuStart: TMenuItem;
    MenuStop: TMenuItem;
    MenuRestart: TMenuItem;
    MenuSeparator3: TMenuItem;
    MenuDelete: TMenuItem;
    MenuSeparator2: TMenuItem;
    MenuExit: TMenuItem;
    MenuCheckForUpdates: TMenuItem;
    MenuSeparator1: TMenuItem;
    MenuHelp: TMenuItem;
    MenuAbout: TMenuItem;
    MenuTools: TMenuItem;
    MenuImport: TMenuItem;
    MenuServer: TMenuItem;
    OpDMachine: TOpenDialog;
    PanLoading: TPanel;
    PanNoServers: TPanel;
    PanNoServersRemote: TPanel;
    PopServerRemoteOptions: TPopupMenu;
    PopupNotifier: TPopupNotifier;
    ServersControl: TPageControl;
    PopServerLocalOptions: TPopupMenu;
    StrGridServersLocal: TStringGrid;
    StrGridServersRemote: TStringGrid;
    SvDMachine: TSaveDialog;
    TabLocalServers: TTabSheet;
    TabRemoteServers: TTabSheet;
    procedure BtnAddRemoteServerClick(Sender: TObject);
    procedure BtnCheckForUpdatesClick(Sender: TObject);
    procedure BtnCreateClick(Sender: TObject);
    procedure BtnDeleteLocalServerClick(Sender: TObject);
    procedure BtnDeleteRemoteServerClick(Sender: TObject);
    procedure BtnExitClick(Sender: TObject);
    procedure BtnAboutClick(Sender: TObject);
    procedure BtnExportClick(Sender: TObject);
    procedure BtnImportClick(Sender: TObject);
    procedure BtnModifyRemoteServerClick(Sender: TObject);
    procedure BtnPropertiesClick(Sender: TObject);
    procedure BtnPauseClick(Sender: TObject);
    procedure BtnRefreshClick(Sender: TObject);
    procedure BtnRestartClick(Sender: TObject);
    procedure BtnStartClick(Sender: TObject);
    procedure BtnStopClick(Sender: TObject);
    procedure FormClose(Sender: TObject; var CloseAction: TCloseAction);
    procedure FormCreate(Sender: TObject);
    procedure FormKeyDown(Sender: TObject; var Key: Word; Shift: TShiftState);
    procedure FormShow(Sender: TObject);
    procedure FormWindowStateChange(Sender: TObject);
    procedure MenuDeleteClick(Sender: TObject);
    procedure MenuOpenClick(Sender: TObject);
    procedure MenuPreferencesClick(Sender: TObject);
    procedure MenuPropertiesRemoteClick(Sender: TObject);
    procedure PopServerLocalOptionsPopup(Sender: TObject);
    procedure PopServerRemoteOptionsPopup(Sender: TObject);
    procedure ServersControlChange(Sender: TObject);
    procedure StrGridServersLocalDblClick(Sender: TObject);
    procedure StrGridServersLocalSelection(Sender: TObject; aCol, aRow: Integer);
    procedure StrGridServersRemoteDblClick(Sender: TObject);
    procedure TxtServersRemoteFilterChange(Sender: TObject);
    procedure TxtServersRemoteFilterEnter(Sender: TObject);
    procedure TxtServersRemoteFilterExit(Sender: TObject);
  private
    { private declarations }
    FAgentPreferences: TAgentPreferences;

    FOnOperation: TOperationMainEvent;
    FMachineNameLocal: string;
    FMachineNameRemote: string;
    FMachineNameRemoteIndex: integer;
    FMachineFileName: string;
    FSelectedMachines: TStringList;

    FMachineRemoteList: TStringList;

    function GetMachineNameLocal: string;
    function GetMachineNameRemote: string;

    function GetTabIndex: integer;
    procedure SetTabIndex(value: integer);

    procedure GridSetFocus;
  public
    { public declarations }
    constructor Create(TheOwner: TComponent); override;

    procedure ForceUpdateMachineName(MachineName: string = '');
    procedure ForceUpdateMachineFileName(FileName: string);

    procedure RefreshList(Info: TMainRefreshInfo);
    procedure RefreshListRemote(Info: TStringList);
    procedure SetMachineStatus(MachineName, value: string);
    procedure AppDeactivate(Sender: TObject);
    procedure AppActivate(Sender: TObject);

    function FindForm(TextCaption: string): TForm;
    procedure ShowPopup(message: string; title: string = 'Information');

    property MachineNameLocal: string read FMachineNameLocal;
    property MachineNameRemote: string read FMachineNameRemote;
    property MachineNameRemoteIndex: integer read FMachineNameRemoteIndex;
    property MachineFileName: string read FMachineFileName;
    property TabIndex: integer read GetTabIndex write SetTabIndex;

    property OnOperation: TOperationMainEvent read FOnOperation write FOnOperation;
    property AgentPreferences: TAgentPreferences write FAgentPreferences;

  end;

var
  FrmMain: TFrmMain;

implementation

{$R *.lfm}
uses LCLType, utools, fmessage, finput, strutils;

{ TFrmMain }

procedure TFrmMain.FormCreate(Sender: TObject);
var
  MarginServersRemote: integer;
begin
  Caption := GetAppProductName;

  StrGridServersLocal.Top := ServerListTop;
  StrGridServersLocal.Left := ServerListLeft;
  StrGridServersLocal.Width := ServerListWidth;
  StrGridServersLocal.Height := ServerListHeight;

  MarginServersRemote := 0;
  StrGridServersRemote.Top := ServerListTop + MarginServersRemote;
  StrGridServersRemote.Left := ServerListLeft;
  StrGridServersRemote.Width:= ServerListWidth;
  StrGridServersRemote.Height := ServerListHeight - MarginServersRemote;

  FSelectedMachines := TStringList.Create;

  if (OSType = 'windows') or (OSType = 'macos') then
  begin
    BtnCreate.AutoSize:= false;
    BtnDeleteLocalServer.AutoSize := false;
    BtnStart.AutoSize := false;
    BtnStop.AutoSize := false;

    BtnAddRemoteServer.AutoSize := false;
    BtnDeleteRemoteServer.AutoSize := false;
    BtnModifyRemoteServer.AutoSize := false;
  end;
end;

procedure TFrmMain.BtnStartClick(Sender: TObject);
begin
  FMachineNameLocal := GetMachineNameLocal;
  OnOperation(Sender, opStartMachine);
  GridSetFocus;
end;

procedure TFrmMain.BtnStopClick(Sender: TObject);
begin
  FMachineNameLocal := GetMachineNameLocal;
  OnOperation(Sender, opStopMachine);
  GridSetFocus;
end;

procedure TFrmMain.FormClose(Sender: TObject; var CloseAction: TCloseAction);
begin
  OnOperation(Sender, opClose);
  CloseAction := caFree;
end;

procedure TFrmMain.FormKeyDown(Sender: TObject; var Key: Word;
  Shift: TShiftState);
begin
  if (Shift = [ssctrl, ssshift]) and (Key = VK_F12) then
    OnOperation(Sender, opChangeStatusRefresh);
end;

procedure TFrmMain.FormShow(Sender: TObject);
begin
  PanLoading.Top := ServerListTop;
  PanLoading.Left:= ServerListLeft;
  PanLoading.Height:= ServerListHeight;
  PanLoading.Width:= StrGridServersLocal.Width;

  PanNoServers.Top := PanLoading.Top;
  PanNoServers.Left:= PanLoading.Left;
  PanNoServers.Height:= PanLoading.Height;
  PanNoServers.Width:= PanLoading.Width;

  PanNoServersRemote.Top := PanLoading.Top;
  PanNoServersRemote.Left:= PanLoading.Left;
  PanNoServersRemote.Height:= PanLoading.Height;
  PanNoServersRemote.Width:= PanLoading.Width;

  OnOperation(Sender, opChangeMachine);
  OnOperation(Sender, opFormShow);
end;

procedure TFrmMain.FormWindowStateChange(Sender: TObject);
begin
end;

procedure TFrmMain.MenuDeleteClick(Sender: TObject);
begin
  if ServersControl.ActivePage.Name = 'TabLocalServers' then
    BtnDeleteLocalServerClick(Sender)
  else
    if ServersControl.ActivePage.Name = 'TabRemoteServers' then
      BtnDeleteRemoteServerClick(Sender);
end;

procedure TFrmMain.MenuOpenClick(Sender: TObject);
begin
  if ServersControl.ActivePage.Name = 'TabLocalServers' then
    StrGridServersLocalDblClick(Sender)
  else
    if ServersControl.ActivePage.Name = 'TabRemoteServers' then
      StrGridServersRemoteDblClick(Sender);
end;

procedure TFrmMain.MenuPreferencesClick(Sender: TObject);
begin
  OnOperation(Sender, opPreferences);
end;

procedure TFrmMain.MenuPropertiesRemoteClick(Sender: TObject);
begin
  FMachineNameRemote := GetMachineNameRemote;
  OnOperation(Sender, opShowInfoMachineRemote);
  GridSetFocus;
end;

procedure TFrmMain.PopServerLocalOptionsPopup(Sender: TObject);
var
  ControlCoord, NewCell: TPoint;
begin
  ControlCoord := StrGridServersLocal.ScreenToControl(PopServerLocalOptions.PopupPoint);
  NewCell:=StrGridServersLocal.MouseToCell(ControlCoord);
  StrGridServersLocal.Col:=NewCell.X;
  StrGridServersLocal.Row:=NewCell.Y;
end;

procedure TFrmMain.PopServerRemoteOptionsPopup(Sender: TObject);
var
  ControlCoord, NewCell: TPoint;
begin
  ControlCoord := StrGridServersRemote.ScreenToControl(PopServerRemoteOptions.PopupPoint);
  NewCell:=StrGridServersRemote.MouseToCell(ControlCoord);
  StrGridServersRemote.Col:=NewCell.X;
  StrGridServersRemote.Row:=NewCell.Y;
end;

procedure TFrmMain.ServersControlChange(Sender: TObject);
begin
  OnOperation(Sender, opChangeTabIndex);
end;

procedure TFrmMain.StrGridServersLocalDblClick(Sender: TObject);
begin
  FMachineNameLocal := GetMachineNameLocal;
  OnOperation(Sender, opShowSpacesLocal);
end;

procedure TFrmMain.StrGridServersLocalSelection(Sender: TObject; aCol, aRow: Integer);
begin
  FMachineNameLocal := GetMachineNameLocal;
  OnOperation(Sender, opChangeMachine);
end;

procedure TFrmMain.StrGridServersRemoteDblClick(Sender: TObject);
begin
  FMachineNameRemote := GetMachineNameRemote;
  OnOperation(Sender, opShowSpacesRemote);
end;

procedure TFrmMain.TxtServersRemoteFilterChange(Sender: TObject);
begin
  RefreshListRemote(FMachineRemoteList);
end;

procedure TFrmMain.TxtServersRemoteFilterEnter(Sender: TObject);
begin
  if TxtServersRemoteFilter.Text = 'Search' then
    TxtServersRemoteFilter.Text := '';
end;

procedure TFrmMain.TxtServersRemoteFilterExit(Sender: TObject);
begin
  if TxtServersRemoteFilter.Text = '' then
    TxtServersRemoteFilter.Text := 'Search';
end;

procedure TFrmMain.BtnExitClick(Sender: TObject);
begin
  Close;
end;

procedure TFrmMain.BtnAboutClick(Sender: TObject);
begin
  OnOperation(Sender, opShowAbout);
  GridSetFocus;
end;

procedure TFrmMain.BtnExportClick(Sender: TObject);
begin
  FMachineNameLocal := GetMachineNameLocal;
  if (FMachineNameLocal <> '') then
  begin
    if SvDMachine.Execute then
    begin
      FMachineFileName := SvDMachine.FileName;
      Application.ProcessMessages;
      OnOperation(Sender, opExportMachine);
    end
    else
      ShowError('You must select the image file server');
  end;
  GridSetFocus;
end;

procedure TFrmMain.BtnImportClick(Sender: TObject);
var NewMachineName: string;
begin
  NewMachineName := '';
  if WinDgInput.Execute('Import server', 'Name', NewMachineName) then
  begin
    if (NewMachineName <> '') and VerifyName(NewMachineName) then
    begin
      if OpDMachine.Execute then
      begin
        FMachineNameLocal := NewMachineName;
        FMachineFileName := OpDMachine.FileName;
        Application.ProcessMessages;
        OnOperation(Sender, opImportMachine);
      end
      else
        ShowError('You must select the image file server');
    end
    else
        ShowError('You must put a correct server name.');
  end;
  GridSetFocus;
end;

procedure TFrmMain.BtnModifyRemoteServerClick(Sender: TObject);
begin
  FMachineNameRemote := GetMachineNameRemote;
  OnOperation(Sender, opModifyRemoteServer);
end;

procedure TFrmMain.BtnPropertiesClick(Sender: TObject);
begin
  FMachineNameLocal := GetMachineNameLocal;
  OnOperation(Sender, opShowInfoMachineLocal);
  GridSetFocus;
end;

procedure TFrmMain.BtnPauseClick(Sender: TObject);
begin
  FMachineNameLocal := GetMachineNameLocal;
  OnOperation(Sender, opPauseMachine);
  GridSetFocus;
end;

procedure TFrmMain.BtnRefreshClick(Sender: TObject);
begin
  OnOperation(Sender, opRefreshMachines);
  GridSetFocus;
end;

procedure TFrmMain.BtnRestartClick(Sender: TObject);
begin
  FMachineNameLocal := GetMachineNameLocal;
  OnOperation(Sender, opRestartServer);
  GridSetFocus;
end;

procedure TFrmMain.BtnCreateClick(Sender: TObject);
var NewMachineName: string;
begin
  NewMachineName := '';
  if WinDgInput.Execute('Create server', 'Name', NewMachineName) then
  begin
    if (NewMachineName <> '') and VerifyName(NewMachineName) then
    begin
      FMachineNameLocal := NewMachineName;
      Application.ProcessMessages;
      OnOperation(Sender, opAddMachine);
    end
    else
        ShowError('You must put a correct name.');
  end;
  GridSetFocus;
end;

procedure TFrmMain.BtnCheckForUpdatesClick(Sender: TObject);
begin
  OnOperation(Sender, opDownloadMSG);
  GridSetFocus;
end;

procedure TFrmMain.BtnAddRemoteServerClick(Sender: TObject);
begin
  OnOperation(Sender, opAddRemoteServer);
end;

procedure TFrmMain.BtnDeleteLocalServerClick(Sender: TObject);
begin
  FMachineNameLocal := GetMachineNameLocal;
  if ShowConfirmation('This operation eliminate the server "'+ FMachineNameLocal + '" permanently. Are you sure?', '', [mbYes, mbNo], mbNo) = mrYes then
    OnOperation(Sender, opDeleteMachine);
  GridSetFocus;
end;

procedure TFrmMain.BtnDeleteRemoteServerClick(Sender: TObject);
begin
  FMachineNameRemote := GetMachineNameRemote;
  if ShowConfirmation('This operation eliminate the server "'+ FMachineNameRemote + '" config. Are you sure?', '', [mbYes, mbNo], mbNo) = mrYes then
    OnOperation(Sender, opDelRemoteServer);
end;

function TFrmMain.GetMachineNameLocal: string;
begin
  Result := FMachineNameLocal;
  if StrGridServersLocal.Row > 0 then
    Result := StrGridServersLocal.Cells[0,StrGridServersLocal.Row];
end;

function TFrmMain.GetMachineNameRemote: string;
begin
  Result := FMachineNameRemote;
  if StrGridServersRemote.Row > 0 then
  begin
    Result := StrGridServersRemote.Cells[0,StrGridServersRemote.Row];
    FAgentPreferences.Log.Info('TFrmMain.GetMachineNameRemote. Server Name: ' + Result +', Server id: ' + FMachineRemoteList.Values[Result] + ', Map: ' + FMachineRemoteList.Strings[StrToInt(FMachineRemoteList.Values[Result])-1]);
    FMachineNameRemoteIndex := StrToInt(FMachineRemoteList.Values[Result]);
  end;
end;

procedure TFrmMain.ForceUpdateMachineName(MachineName: string = '');
begin
  if (MachineName <> '') then
    FMachineNameLocal := MachineName
  else
    FMachineNameLocal := GetMachineNameLocal;
end;

procedure TFrmMain.ForceUpdateMachineFileName(FileName: string);
begin
  FMachineFileName := FileName;
end;

procedure TFrmMain.RefreshList(Info: TMainRefreshInfo);
var
  x: integer;
  MachineList, MachineListRunning, MachineListPaused: TStringList;

begin
  MachineList := TStringList.Create;
  MachineListRunning := TStringList.Create;
  MachineListPaused := TStringList.Create;

  try
    MachineList.Text := Info.MachineList;
    MachineListRunning.Text := Info.MachineListRunning;
    MachineListPaused.Text := Info.MachineListPaused;

    if (MachineList.Count <> StrGridServersLocal.RowCount-1) then
      StrGridServersLocal.RowCount:= MachineList.Count+1;

    for x := 0 to MachineList.Count-1 do
    begin
      StrGridServersLocal.Cells[0,x+1] := MachineList.Strings[x];

      if (MachineListRunning.IndexOf(MachineList.Strings[x]) >=0) then
      begin
        StrGridServersLocal.Cells[1,x+1] := 'On';
      end
      else
      begin
        if (MachineListPaused.IndexOf(MachineList.Strings[x]) >=0) then
          StrGridServersLocal.Cells[1,x+1] := 'Paused'
        else
          StrGridServersLocal.Cells[1,x+1] := 'Off';
      end;
    end;

    ForceUpdateMachineName;

    OnOperation(Self, opChangeMachine);
  finally
    MachineList.Free;
    MachineListRunning.Free;
    MachineListPaused.Free;
  end;
end;

procedure TFrmMain.RefreshListRemote(Info: TStringList);
var
  x: integer;
  count: integer;
  start_text: string;
  contains_filter: boolean;
  filter: string;

begin
  FMachineRemoteList := Info;
  filter := TxtServersRemoteFilter.Text;
  if (filter = 'Search') then filter := '';

  if (Info.Count <> StrGridServersRemote.RowCount-1) then
    StrGridServersRemote.RowCount:= Info.Count+1;

  contains_filter := copy(filter,1,1) = '*';
  if contains_filter then
    filter := copy(filter, 2, length(filter));

  count := 0;
  for x := 0 to Info.Count-1 do
  begin
    if contains_filter then
    begin
      if AnsiContainsText(Info.Names[x], filter) then
      begin
        StrGridServersRemote.Cells[0,count+1] := Info.Names[x];
        inc(count);
      end;
    end
    else
    begin
      start_text := copy(Info.Names[x], 1, length(filter));
      if start_text = filter then
      begin
        StrGridServersRemote.Cells[0,count+1] := Info.Names[x];
        inc(count);
      end;
    end;
  end;

  StrGridServersRemote.RowCount:= count+1;
end;

procedure TFrmMain.SetMachineStatus(MachineName, value: string);
var x: integer;
begin
  x := 1;
  while (x < StrGridServersLocal.RowCount) and (StrGridServersLocal.Cells[0, x] <> MachineName) do
    inc(x);

  if (x < StrGridServersLocal.RowCount) then
    StrGridServersLocal.Cells[1,x] := value;
end;

procedure TFrmMain.GridSetFocus;
begin
  if FrmMain.Enabled and StrGridServersLocal.Enabled and StrGridServersLocal.Visible and StrGridServersLocal.Focused then
  try
    StrGridServersLocal.SetFocus;
  except end;
end;

procedure TFrmMain.AppDeactivate(Sender: TObject);
begin
  StrGridServersLocal.Color := clGrayText;
end;

procedure TFrmMain.AppActivate(Sender: TObject);
begin
  StrGridServersLocal.Color := clWindow;
  StrGridServersLocal.Visible := false;
  PanLoading.Visible := true;
  PanNoServers.Visible := false;
end;

constructor TFrmMain.Create(TheOwner: TComponent);
begin
  inherited Create(TheOwner);
  Application.OnDeactivate:=@AppDeactivate;
  Application.OnActivate:=@AppActivate;
end;

function TFrmMain.GetTabIndex: integer;
begin
  Result := ServersControl.TabIndex;
end;

procedure TFrmMain.SetTabIndex(value: integer);
begin
  ServersControl.TabIndex := value;
end;

function TFrmMain.FindForm(TextCaption: string): TForm;
var
  x: integer;
begin
  Result := nil;
  x := 1;
  while (Result = nil) and (x < Application.ComponentCount) do
  begin
    if (Application.Components[x] is TForm) and (TForm(Application.Components[x]).Caption = TextCaption) then
      Result := TForm(Application.Components[x]);
    inc(x);
  end;
end;

procedure TFrmMain.ShowPopup(message: string; title: string = 'Information');
begin
  PopupNotifier.Text := message;
  PopupNotifier.Title:= title;
  PopupNotifier.Show;
end;

end.

