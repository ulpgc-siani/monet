unit fserver;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, ComCtrls,
  StdCtrls, Buttons, Menus, ExtCtrls, Grids, utypes;

type
  TOperationSpaces = (opRefresh, opCreateBusinessSpace, opDelete, opChange, opCopyCodeToClipboard, opOpenSpace, opClose, opShow, opOpenFederation, opUpdateServer, opCleanUpServer, opSSHConsole, opUploadDistribution, opCopyPropertyValueToClipboard, opChangeStatusRefresh, opStopThreads, opStartThreads, opRestartServer, opRestartServerDebug, opOpenLog, opShowInformation, opRenewCertificate1y, opRenewCertificate5y, opAllowDelete);
  TOperationSpacesEvent = procedure(Sender: TObject; Operation: TOperationSpaces) of object;

  { TFrmServer }

  TFrmServer = class(TForm)
    BtnCleanUp: TBitBtn;
    BtnClose: TButton;
    BtnCreateBusinessSpace: TBitBtn;
    BtnDelete: TBitBtn;
    BtnFederation: TBitBtn;
    BtnRestart: TBitBtn;
    BtnOpenLog: TBitBtn;
    BtnRestartDebug: TBitBtn;
    BtnUpdate: TBitBtn;
    BtnUploadDistribution: TBitBtn;
    LblTotalConnections: TLabel;
    LblInfoTotalConnections: TLabel;
    LblServerName: TLabel;
    LblNoSpaces: TLabel;
    LblLoading: TLabel;
    MenuCopyPropertyValue: TMenuItem;
    MenuDeletePop: TMenuItem;
    MenuISeparator3: TMenuItem;
    MenuItem1: TMenuItem;
    MenuRenewCertificate5y: TMenuItem;
    MenuRenewCertificate1y: TMenuItem;
    MenuShowInformationPop: TMenuItem;
    MenuOpenPop: TMenuItem;
    MenuSeparator2: TMenuItem;
    MenuSeparator1: TMenuItem;
    MenuOpenSSHConsolePop: TMenuItem;
    MenuCopyDeployUri: TMenuItem;
    OpenDlgDistributionFIle: TOpenDialog;
    PageControl: TPageControl;
    PanNoSpaces: TPanel;
    PanLoading: TPanel;
    PopSpacesOptions: TPopupMenu;
    PopPropertiesOptions: TPopupMenu;
    StrGridProperties: TStringGrid;
    StrGridUsers: TStringGrid;
    StrGridSpaces: TStringGrid;
    TabProperties: TTabSheet;
    TabUsers: TTabSheet;
    TabSpaces: TTabSheet;
    procedure BtnCleanUpClick(Sender: TObject);
    procedure BtnCloseClick(Sender: TObject);
    procedure BtnCopyCodeToClipboardClick(Sender: TObject);
    procedure BtnCreateBusinessSpaceClick(Sender: TObject);
    procedure BtnOpenLogClick(Sender: TObject);
    procedure BtnRestartClick(Sender: TObject);
    procedure BtnRestartDebugClick(Sender: TObject);
    procedure BtnUploadDistributionClick(Sender: TObject);
    procedure BtnDeleteClick(Sender: TObject);
    procedure BtnFederationClick(Sender: TObject);
    procedure BtnRefreshClick(Sender: TObject);
    procedure BtnUpdateClick(Sender: TObject);
    procedure FormClose(Sender: TObject; var CloseAction: TCloseAction);
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure FormKeyDown(Sender: TObject; var Key: Word; Shift: TShiftState);
    procedure FormShow(Sender: TObject);
    procedure LstSpacesDblClick(Sender: TObject);
    procedure LstSpacesSelectItem(Sender: TObject; Item: TListItem;
      Selected: Boolean);
    procedure MenuCopyDeployUriClick(Sender: TObject);
    procedure MenuCopyPropertyValueClick(Sender: TObject);
    procedure MenuOpenSSHConsolePopClick(Sender: TObject);
    procedure MenuRenewCertificate1yClick(Sender: TObject);
    procedure MenuRenewCertificate5yClick(Sender: TObject);
    procedure MenuShowInformationPopClick(Sender: TObject);
    procedure PopPropertiesOptionsPopup(Sender: TObject);
    procedure PopSpacesOptionsPopup(Sender: TObject);
    procedure StrGridSpacesDblClick(Sender: TObject);
    procedure StrGridSpacesPrepareCanvas(sender: TObject; aCol, aRow: Integer;
      aState: TGridDrawState);
    procedure StrGridSpacesSelection(Sender: TObject; aCol, aRow: Integer);
  private
    { private declarations }
    FOnOperation: TOperationSpacesEvent;
    FSpaceName: string;
    FDistributionFile: string;

    function GetSpaceName: string;
    function GetLstItem(Lst: TListView; ItemCaption: string): TListItem;
    procedure SetSpaces(Spaces: string);
    procedure SetUsers(Users: string);
    procedure SetDeployServiceVersion(version: string);
    procedure SetMetaModelVersion(version: string);

public
    { public declarations }
    procedure RefreshList(Info: TServerRefreshInfo);

    procedure CleanProperties;
    procedure AddProperty(title, value: string);

    property OnOperation: TOperationSpacesEvent read FOnOperation write FOnOperation;
    property SpaceName: string read FSpaceName;
    property DistributionFile: string read FDistributionFile;

end;

implementation

{$R *.lfm}
uses LCLType, fmessage, utools, LCLIntf;

{ TFrmServer }

type
  TCellData = class(TObject)
  private
    FBackground: TColor;
  public
    property Background: TColor read FBackground write FBackground;
  end;

procedure TFrmServer.FormCreate(Sender: TObject);
begin
  PanLoading.Top := StrGridSpaces.Top;
  PanLoading.Left := StrGridSpaces.Left;
  PanLoading.Height:= StrGridSpaces.Height;
  PanLoading.Width:= StrGridSpaces.Width;

  PanNoSpaces.Top := StrGridSpaces.Top;
  PanNoSpaces.Left := StrGridSpaces.Left;
  PanNoSpaces.Height:= StrGridSpaces.Height;
  PanNoSpaces.Width:= StrGridSpaces.Width;

  if (OSType = 'windows') or (OSType = 'macos') then
  begin
    BtnFederation.AutoSize := false;
    BtnUpdate.AutoSize := false;

    BtnCreateBusinessSpace.AutoSize := false;
    BtnDelete.AutoSize := false;
    BtnCleanUp.AutoSize := false;
    BtnUploadDistribution.Autosize := false;
    BtnRestart.Autosize := false;
    BtnRestartDebug.Autosize := false;
    BtnOpenLog.AutoSize := false;
  end;
  StrGridSpaces.AllowOutboundEvents := False;

end;

procedure TFrmServer.FormDestroy(Sender: TObject);
var x:integer;
begin
  for x := 0 to StrGridSpaces.RowCount - 1 do
    StrGridSpaces.Objects[1, x].Free;
end;

procedure TFrmServer.FormKeyDown(Sender: TObject; var Key: Word;
  Shift: TShiftState);
begin
  if (Shift = [ssctrl, ssshift]) and (Key = VK_F12) then
    OnOperation(Sender, opChangeStatusRefresh);
  if (Shift = [ssalt]) then
    OnOperation(Sender, opAllowDelete);
  writeln('1');
end;

procedure TFrmServer.BtnCloseClick(Sender: TObject);
begin
  OnOperation(Sender, opClose);
  Close;
end;

procedure TFrmServer.BtnCleanUpClick(Sender: TObject);
begin
  if ShowConfirmation('All spaces will be deleted. Are you sure you want to continue?') = mrYes then
    OnOperation(Sender, opCleanUpServer);
  OnOperation(Sender, opChange);
  if (PageControl.ActivePageIndex = 0) and StrGridSpaces.Visible then StrGridSpaces.SetFocus;
end;

procedure TFrmServer.BtnCopyCodeToClipboardClick(Sender: TObject);
begin
  OnOperation(Sender, opCopyCodeToClipboard);
  OnOperation(Sender, opChange);
end;

procedure TFrmServer.BtnCreateBusinessSpaceClick(Sender: TObject);
begin
  OnOperation(Sender, opCreateBusinessSpace);
  OnOperation(Sender, opChange);

  if (PageControl.ActivePageIndex = 0) and StrGridSpaces.Visible then StrGridSpaces.SetFocus;
end;

procedure TFrmServer.BtnOpenLogClick(Sender: TObject);
begin
  OnOperation(Sender, opOpenLog);
  OnOperation(Sender, opChange);
  if (PageControl.ActivePageIndex = 0) and StrGridSpaces.Visible then StrGridSpaces.SetFocus;
end;

procedure TFrmServer.BtnRestartClick(Sender: TObject);
begin
  OnOperation(Sender, opRestartServer);
  OnOperation(Sender, opChange);
  if (PageControl.ActivePageIndex = 0) and StrGridSpaces.Visible then StrGridSpaces.SetFocus;
end;

procedure TFrmServer.BtnRestartDebugClick(Sender: TObject);
begin
  OnOperation(Sender, opRestartServerDebug);
  OnOperation(Sender, opChange);
  if (PageControl.ActivePageIndex = 0) and StrGridSpaces.Visible then StrGridSpaces.SetFocus;
end;

procedure TFrmServer.BtnUploadDistributionClick(Sender: TObject);
begin
  FSpaceName := GetSpaceName;

  if OpenDlgDistributionFile.Execute then
  begin
    FDistributionFile := OpenDlgDistributionFile.FileName;
    OnOperation(Sender, opUploadDistribution);
  end;
  OnOperation(Sender, opChange);
end;

procedure TFrmServer.BtnDeleteClick(Sender: TObject);
begin
  FSpaceName := GetSpaceName;
  if ShowConfirmation('This operation eliminate the space "'+ FSpaceName + '" permanently. Are you sure?', '', [mbYes, mbNo], mbNo) = mrYes then
    OnOperation(Sender, opDelete);
  OnOperation(Sender, opChange);
  if (PageControl.ActivePageIndex = 0) and StrGridSpaces.Visible then StrGridSpaces.SetFocus;
end;

procedure TFrmServer.BtnFederationClick(Sender: TObject);
begin
  OnOperation(Self, opOpenFederation);
  OnOperation(Sender, opChange);
  if (PageControl.ActivePageIndex = 0) and StrGridSpaces.Visible then StrGridSpaces.SetFocus;
end;

procedure TFrmServer.BtnRefreshClick(Sender: TObject);
begin
  OnOperation(Sender, opRefresh);
  if (PageControl.ActivePageIndex = 0) and StrGridSpaces.Visible then StrGridSpaces.SetFocus;
end;

procedure TFrmServer.BtnUpdateClick(Sender: TObject);
begin
  OnOperation(Sender, opUpdateServer);
  OnOperation(Sender, opChange);
  if (PageControl.ActivePageIndex = 0) and StrGridSpaces.Visible then StrGridSpaces.SetFocus;
end;

procedure TFrmServer.FormClose(Sender: TObject; var CloseAction: TCloseAction);
begin
  FSpaceName := '';
  OnOperation(Sender, opClose);
end;

procedure TFrmServer.FormShow(Sender: TObject);
begin
  PageControl.ActivePageIndex:=0;
  OnOperation(Sender, opShow);
end;

procedure TFrmServer.LstSpacesDblClick(Sender: TObject);
begin
  FSpaceName := GetSpaceName;
  OnOperation(Sender, opOpenSpace);
end;

procedure TFrmServer.LstSpacesSelectItem(Sender: TObject; Item: TListItem;
  Selected: Boolean);
begin
  FSpaceName := GetSpaceName;
  OnOperation(Sender, opChange);
end;

procedure TFrmServer.MenuCopyDeployUriClick(Sender: TObject);
begin
  FSpaceName := GetSpaceName;
  OnOperation(Sender, opCopyCodeToClipboard);
end;

procedure TFrmServer.MenuCopyPropertyValueClick(Sender: TObject);
begin
  FSpaceName := GetSpaceName;
  OnOperation(Sender, opCopyPropertyValueToClipboard);
end;

procedure TFrmServer.MenuOpenSSHConsolePopClick(Sender: TObject);
begin
  OnOperation(Sender, opSSHConsole);
end;

procedure TFrmServer.MenuRenewCertificate1yClick(Sender: TObject);
begin
  FSpaceName := GetSpaceName;
  OnOperation(Sender, opRenewCertificate1y);
end;

procedure TFrmServer.MenuRenewCertificate5yClick(Sender: TObject);
begin
  FSpaceName := GetSpaceName;
  OnOperation(Sender, opRenewCertificate5y);
end;

procedure TFrmServer.MenuShowInformationPopClick(Sender: TObject);
begin
  FSpaceName := GetSpaceName;
  OnOperation(Sender, opShowInformation);
end;

procedure TFrmServer.PopPropertiesOptionsPopup(Sender: TObject);
var
  ControlCoord, NewCell: TPoint;
begin
  ControlCoord := StrGridProperties.ScreenToControl(PopPropertiesOptions.PopupPoint);
  NewCell:=StrGridProperties.MouseToCell(ControlCoord);
  StrGridProperties.Col:=NewCell.X;
  StrGridProperties.Row:=NewCell.Y;
end;

procedure TFrmServer.PopSpacesOptionsPopup(Sender: TObject);
var
  ControlCoord, NewCell: TPoint;
begin
  ControlCoord := StrGridSpaces.ScreenToControl(PopSpacesOptions.PopupPoint);
  NewCell:=StrGridSpaces.MouseToCell(ControlCoord);
  StrGridSpaces.Col:=NewCell.X;
  StrGridSpaces.Row:=NewCell.Y;
end;

procedure TFrmServer.StrGridSpacesDblClick(Sender: TObject);
begin
  FSpaceName := GetSpaceName;
  OnOperation(Sender, opOpenSpace);
end;

procedure TFrmServer.StrGridSpacesPrepareCanvas(sender: TObject; aCol,
  aRow: Integer; aState: TGridDrawState);
var
  CellData: TCellData;
begin
  if StrGridSpaces.Objects[ACol, ARow] is TCellData then
  begin
      CellData := TCellData(StrGridSpaces.Objects[ACol, ARow]);
      StrGridSpaces.Canvas.Brush.Color := CellData.Background;
  end;
end;

procedure TFrmServer.StrGridSpacesSelection(Sender: TObject; aCol, aRow: Integer);
begin
  FSpaceName := GetSpaceName;
  OnOperation(Sender, opChange);
end;

procedure TFrmServer.SetSpaces(Spaces: string);
var
  x: integer;
  Space, Model: string;
  SpacesList: TStringList;
  line: string;
  CertificateExpiration: string;
  CertificateExpirationDate: TDateTime;
  CellData: TCellData;

begin
  SpacesList := TStringList.Create;
  try
    SpacesList.Text := Spaces;
    if (SpacesList.Count <> StrGridSpaces.RowCount-1) then
    begin
      StrGridSpaces.RowCount:= SpacesList.Count+1;

      for x := 0 to StrGridSpaces.RowCount - 1 do
      begin
        if StrGridSpaces.Objects[1, x] = nil then
        begin
          CellData := TCellData.Create;
          CellData.Background := clWhite;
          StrGridSpaces.Objects[1, x] := CellData;
        end;
      end;

    end;

    for x := 0 to SpacesList.Count-1 do
    begin
      line := SpacesList.Strings[x];
      Space := Copy(line, 1, Pos('|',line)-1);

      line := Copy(line, Pos('|',line)+1, Length(line));
      Model := Copy(line, 1, Pos('|',line)-1);

      line := Copy(line, Pos('|',line)+1, Length(line)); //url
      line := Copy(line, Pos('|',line)+1, Length(line)); //federation_url
      line := Copy(line, Pos('|',line)+1, Length(line));
      CertificateExpiration := Copy(line, 1, Pos('|',line)-1);
      if (CertificateExpiration <> '') then
      begin
        StrGridSpaces.Columns[1].Visible:=true;

        CertificateExpirationDate := ConvertDateTime(CertificateExpiration);
        CertificateExpiration := FormatDateTime('DD-MM-YYYY hh:nn', CertificateExpirationDate);

        StrGridSpaces.Cells[1, x+1] := CertificateExpiration;
        if StrGridSpaces.Objects[1, x+1] is TCellData then
        begin
          CellData := TCellData(StrGridSpaces.Objects[1, x+1]);
          CellData.Background := clGreen;

          if CertificateExpirationDate <= Now then
            CellData.Background := clRed
          else
            if IncMonth(CertificateExpirationDate,-3) <= Now then
              CellData.Background := RGB(255, 165, 0); //orange
        end;
      end
      else
      begin
        StrGridSpaces.Columns[1].Visible:=false;
        StrGridSpaces.Columns[0].Width := StrGridSpaces.Width;
      end;


      StrGridSpaces.Cells[0,x+1] := Space;


      StrGridSpaces.Cells[2, x+1] := Model;
    end;
  finally
    SpacesList.Free;
  end;
end;

procedure TFrmServer.SetUsers(Users: string);
var
  x: integer;
  Username, Federation, IsMobile, LastUse, Space, Node: string;
  UsersList: TStringList;
  line: string;
begin
  UsersList := TStringList.Create;
  try
    UsersList.Text := Users;
    if (UsersList.Count <> StrGridUsers.RowCount-1) then
      StrGridUsers.RowCount:= UsersList.Count+1;

    for x := 0 to UsersList.Count-1 do
    begin
      line := UsersList.Strings[x];
      LastUse := Copy(line, 1, Pos('|',line)-1);

      line := Copy(line, Pos('|',line)+1, Length(line));
      Username := Copy(line, 1, Pos('|',line)-1);

      line := Copy(line, Pos('|',line)+1, Length(line));
      Federation := Copy(line, 1, Pos('|',line)-1);

      line := Copy(line, Pos('|',line)+1, Length(line));
      IsMobile := Copy(line, 1, Pos('|',line)-1);
      if IsMobile = '1' then IsMobile := 'Yes' else IsMobile := 'No';

      line := Copy(line, Pos('|',line)+1, Length(line));
      space := Copy(line, 1, Pos('|',line)-1);

      line := Copy(line, Pos('|',line)+1, Length(line));
      node := Copy(line, 1, Pos('|',line)-1);

      StrGridUsers.Cells[0, x+1] := Username;
      StrGridUsers.Cells[1, x+1] := Federation;
      StrGridUsers.Cells[2, x+1] := Space;
      StrGridUsers.Cells[3, x+1] := IsMobile;
      StrGridUsers.Cells[4, x+1] := Node;
      StrGridUsers.Cells[5, x+1] := LastUse;
    end;
    LblTotalConnections.Caption:= IntToStr(UsersList.Count);
  finally
    UsersList.Free;
  end;
end;

procedure TFrmServer.SetDeployServiceVersion(version: string);
begin
  AddProperty('Deploy Service version', version);
end;

procedure TFrmServer.SetMetaModelVersion(version: string);
begin
  AddProperty('Metamodel version', version);
end;

function TFrmServer.GetSpaceName: string;
begin
  Result := FSpaceName;
  if StrGridSpaces.Row > 0 then
    Result := StrGridSpaces.Cells[0,StrGridSpaces.Row];
end;

function TFrmServer.GetLstItem(Lst: TListView; ItemCaption: string): TListItem;
var
  x: integer;
begin
  Result := nil;
  x := 0;
  while (x < Lst.Items.Count) and (Result = nil) do
  begin
    if Lst.Items[x].Caption = ItemCaption then
      Result := Lst.Items[x];
    inc(x);
  end;
end;

procedure TFrmServer.RefreshList(Info: TServerRefreshInfo);
begin
  SetSpaces(Info.SpacesList);
  SetUsers(Info.UsersList);
  SetDeployServiceVersion(Info.DeployServiceVersion);
  SetMetaModelVersion(Info.MetaModelVersion);
  OnOperation(Self, opChange);
end;

procedure TFrmServer.CleanProperties;
begin
  StrGridProperties.RowCount:= 1;
end;

procedure TFrmServer.AddProperty(title, value: string);
var
  x: integer;
begin
  x := 1;
  while (StrGridProperties.RowCount > x) and
        (StrGridProperties.Cells[0,x] <> title) do
    inc(x);

  StrGridProperties.RowCount:= x+1;
  StrGridProperties.Cells[0, x] := title;
  StrGridProperties.Cells[1, x] := value;
end;

end.

