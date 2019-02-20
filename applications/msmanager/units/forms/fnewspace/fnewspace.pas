unit fnewspace;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, StdCtrls;

type

  { TFrmNewSpace }

  TFrmNewSpace = class(TForm)
    BtnCancel: TButton;
    BtnOk: TButton;
    CmbFederation: TComboBox;
    LblFederation: TLabel;
    LblName: TLabel;
    LblUrl: TLabel;
    TxtName: TEdit;
    TxtUrl: TEdit;
    procedure BtnCancelClick(Sender: TObject);
    procedure BtnOkClick(Sender: TObject);
    procedure FormShow(Sender: TObject);
    procedure TxtNameChange(Sender: TObject);
    procedure TxtNameKeyUp(Sender: TObject; var Key: Word; Shift: TShiftState);
  private
    { private declarations }
    FResult: boolean;
    FUrl: string;

    function GetName: string;
    function GetFederation: string;
    function GetUrl: string;

    procedure SetFederations(FederationsList: string);
    procedure SetUrl(Url: string);
  public
    { public declarations }

    property Result: boolean read FResult;

    property Name: string read GetName;
    property Federation: string read GetFederation;
    property Federations: string write SetFederations;
    property Url: string read GetUrl write SetUrl;

  end;

var
  FrmNewSpace: TFrmNewSpace;

implementation

{$R *.lfm}

{ TFrmNewSpace }

function TFrmNewSpace.GetName: string; begin Result := TxtName.Text; end;
function TFrmNewSpace.GetFederation: string; begin Result := CmbFederation.Text; end;
function TFrmNewSpace.GetUrl: string; begin Result := TxtUrl.Text; end;

procedure TFrmNewSpace.SetFederations(FederationsList: string);
begin
  CmbFederation.Clear;
  CmbFederation.Items.Text:=FederationsList;

  if CmbFederation.Items.Count > 0 then
    CmbFederation.ItemIndex := 0;
end;

procedure TFrmNewSpace.SetUrl(Url: string); begin FUrl := Url; TxtUrl.Text := Url; end;

procedure TFrmNewSpace.FormShow(Sender: TObject);
begin
  FResult := false;
  TxtName.Text := '';
  TxtName.SetFocus;
end;

procedure TFrmNewSpace.TxtNameChange(Sender: TObject);
begin
  TxtUrl.Text := FUrl + TxtName.Text;
end;

procedure TFrmNewSpace.TxtNameKeyUp(Sender: TObject; var Key: Word;
  Shift: TShiftState);
begin
  TxtUrl.Text := FUrl + TxtName.Text;
end;

procedure TFrmNewSpace.BtnOkClick(Sender: TObject);
begin
  if (TxtName.Text <> '') and
     (CmbFederation.Text <> '') then
  begin
    FResult := true;
    Close;
  end;

end;

procedure TFrmNewSpace.BtnCancelClick(Sender: TObject);
begin
  Close;
end;

end.

