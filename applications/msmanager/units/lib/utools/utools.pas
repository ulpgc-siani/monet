unit utools;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils;


type
  THttpResult = record
    content: string;
    code: integer;
  end;

function DirectoryTemp: string;
function OSType: string;
function ApplicationPath: string;
function GetAppVersionTotal:string;
function GetAppProductName:string;
function GetShortFileName(FileName: string): string;
function GetConfigDir: string;
function GetAppProductShortName: string;
function GetFileNameMD5(FileName: string): string;
function FormatByteSize(const bytes: Int64): string;
function SecToTime(Sec: Integer): string;
function DeployServiceSendCommand(Ip, DeployServicePort, Command: string; Wait: boolean = true): string;
function DeployServiceSendCommandStatus(Ip, DeployServicePort, Command: string; Wait: boolean): integer;
function DeployServiceSendCommandUrl(Url: string; Wait: boolean): string;
function IsPortConnected(Ip, Port: string): boolean;
function IsPortOccuped(Host: string; Port: string): boolean;
function VerifyName(name: string): boolean;
function HTMLDecode(const AStr: String): String;
function ResolveHost(HostName: string): string;
function StripHTML(S: string): string;
function etxt(s: string; code: longint; encrypt :boolean): string;
function ConvertDateTime(datetime: string): TDateTime;


implementation

uses forms, LCLType, uinfo, versiontypes, LSHTTPSend {$ifdef windows},windows{$else},netdb{$endif}, md5,DateUtils,blcksock, httpsend, ssl_openssl, SynaCode;

function DirectoryTemp: string;
begin
  Result := GetTempDir;
end;

function OSType: string;
begin
  {$IFDEF LCLcarbon}
    Result := 'macos';
  {$ELSE}
    {$IFDEF Linux}
      Result := 'linux';
    {$ELSE}
      {$IFDEF UNIX}
        Result := 'unix';
      {$ELSE}
        {$IFDEF WINDOWS}
          Result := 'windows';
        {$ENDIF}
      {$ENDIF}
    {$ENDIF}
  {$ENDIF}
end;

function ApplicationPath: string;
begin
  Result := ExtractFilePath(Application.ExeName);
end;

function ProductVersionToString(PV: TFileProductVersion): String;
 begin
   Result := Format('%d.%d.%d.%d', [PV[0],PV[1],PV[2],PV[3]])
 end;

function GetAppVersionTotal:string;
var Info: TVersionInfo;
begin
  Info := TVersionInfo.Create;
  Info.Load(HINSTANCE);
  Result := ProductVersionToString(Info.FixedInfo.FileVersion);
  Info.Free;
end;

function GetAppProductName:string;
var Info: TVersionInfo;
begin
  Info := TVersionInfo.Create;
  Info.Load(HINSTANCE);
  Result :=  Info.StringFileInfo[0].Values['ProductName'];
end;

function GetAppProductShortName: string;
begin
  Result := ApplicationName;
end;

function GetShortFileName(FileName: string): string;
{$ifdef windows}
var
  corto:array [0..MAX_PATH] of char;
  longitud:Cardinal;
{$endif}
begin
  Result := FileName;
  {$ifdef windows}
  Longitud:= Sizeof(Corto) -1;
  GetShortPathName(PChar(FileName),@corto,Longitud);
  Result:=String(Pchar(@corto));
  {$endif}
end;

function GetConfigDir: string;
begin
  Result := GetUserDir + '.' + GetAppProductShortName;
end;

function GetFileNameMD5(FileName:string): string;
begin
  Result := MD5Print(MD5File(FileName));
end;

function FormatByteSize(const bytes: Int64): string;
const
   B = 1; //byte
   KB = 1024 * B; //kilobyte
   MB = 1024 * KB; //megabyte
   GB = 1024 * MB; //gigabyte
begin
   if bytes > GB then
     result := FormatFloat('#.## GB', bytes / GB)
   else
     if bytes > MB then
       result := FormatFloat('#.## MB', bytes / MB)
     else
       if bytes > KB then
         result := FormatFloat('#.## KB', bytes / KB)
       else
         result := FormatFloat('#.## bytes', bytes) ;
end;

function SecToTime(Sec: Integer): string;
var
   H, M, S: string;
   ZH, ZM, ZS: Integer;
begin
   ZH := Sec div 3600;
   ZM := Sec div 60 - ZH * 60;
   ZS := Sec - (ZH * 3600 + ZM * 60) ;
   H := IntToStr(ZH);// if Length(H) = 1 then H := '0' + H;
   M := IntToStr(ZM);// if Length(M) = 1 then M := '0' + M;
   S := IntToStr(ZS);// if Length(S) = 1 then S := '0' + S;

  if ZH > 0 then
    Result := H + ' hour, ';

  if (ZM > 0) or (ZM > 0) then
    Result += M +' minutes, ';

  if (ZM > 0) or (ZM > 0) or (ZS >= 0) then
    Result += S +' seconds';

  if Sec < 0 then
    Result := 'Unknown';

end;

function VerifyName(name: string): boolean;
const
   alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_1234567890';
var
  x: integer;
  character: string;
begin
  Result := true;

  x := 1;
  while Result and (x <= Length(name)) do
  begin
    character := name[x];
    Result := (Pos(character, alphabet) <> 0);
    inc(x);
  end;
end;

function IsPortConnected(Ip, Port: string): boolean;
var
  sock: TTCPBlockSocket;
begin
  sock := TTCPBlockSocket.create;
  try
    sock.Connect(IP, Port);
    Result := (sock.lasterror = 0);
  finally
    sock.free;
  end;
end;

function DeployServiceSendCommandSocket(Ip, DeployServicePort, Command: string; Wait: boolean): string;
var
  sock: TTCPBlockSocket;
  buffer: string;
  x: integer;
begin
  Result := '';
  sock := TTCPBlockSocket.create;
  try
    x := 0;
    repeat
      sock.Connect(Ip, DeployServicePort);
      if sock.lasterror = 111 then sleep(100);
      inc(x);
    until (sock.lasterror <> 111) or (x > 5); // Connection refused

    if sock.lasterror = 0 then
    begin
      sock.SendString(command+#13#10);
      result := '';
      buffer := '';
      x := 0;
      if Wait then
      begin
        repeat
          buffer := sock.RecvPacket(2000);
          Result := Result + buffer;
          if (Result = '') then sleep(100);
          inc(x);
         until (Result <> '') or (x > 12000);
      end
      else
        Result := sock.RecvPacket(2000);
    end;
  finally
    try
      sock.free;
    except
      Result := '';
    end;
  end;
end;

function DeployServiceSendCommandHTTPS(Url: string; Wait: boolean): THttpResult;
const max_time_wait_500 = 5000;
var
  http : THTTPSend;
  page : TStringList;
  time_wait_500: integer;
begin
  Result.content := '';
  Result.code := 0;
  page:=TStringList.Create;
  try
    time_wait_500 := 0;
    repeat
      try
        Result.content := '';
        Result.code := 0;

        http:=THTTPSend.Create;
        http.Timeout := High(Integer);
        if not Wait then http.Timeout := 5000;
        if http.HTTPMethod('GET', Url) then
        begin
          page.LoadFromStream(http.Document);
          Result.content := page.Text;
          Result.content := Trim(Copy(Result.content, 1, Length(Result.content)-1));
        end;
        Result.code:=http.ResultCode;
      finally
        http.Free;
      end;

      if (Wait) and (Result.code <> 200) and (time_wait_500 <= max_time_wait_500) then
      begin
        sleep(1000);
        time_wait_500 += 1000;
      end;
    until (not Wait) or (Result.code = 200) or (time_wait_500 > max_time_wait_500);
    Result.code:= http.ResultCode;
  finally
    page.Free;
  end;
end;

function DeployServiceSendCommand(Ip, DeployServicePort, Command: string; Wait: boolean): string;
begin
  Result := DeployServiceSendCommandHTTPS('https://' + Ip +':'+ DeployServicePort + '/' + EncodeUrl(Command), Wait).content;
end;

function DeployServiceSendCommandStatus(Ip, DeployServicePort, Command: string; Wait: boolean): integer;
begin
  Result := DeployServiceSendCommandHTTPS('https://' + Ip +':'+ DeployServicePort + '/' + EncodeUrl(Command), Wait).code;
end;

function DeployServiceSendCommandUrl(Url: string; Wait: boolean): string;
begin
  Result := DeployServiceSendCommandHTTPS(Url, Wait).content;
end;

function HTMLDecode(const AStr: String): String;
var
  Sp, Rp, Cp, Tp: PChar;
  S: String;
  I, Code: Integer;
begin
  SetLength(Result, Length(AStr));
  Sp := PChar(AStr);
  Rp := PChar(Result);
  Cp := Sp;
  try
    while Sp^ <> #0 do
    begin
      case Sp^ of
        '&': begin
               Cp := Sp;
               Inc(Sp);
               case Sp^ of
                 'a': if AnsiStrPos(Sp, 'amp;') = Sp then  { do not localize }
                      begin
                        Inc(Sp, 3);
                        Rp^ := '&';
                      end;
                 'l',
                 'g': if (AnsiStrPos(Sp, 'lt;') = Sp) or (AnsiStrPos(Sp, 'gt;') = Sp) then { do not localize }
                      begin
                        Cp := Sp;
                        Inc(Sp, 2);
                        while (Sp^ <> ';') and (Sp^ <> #0) do
                          Inc(Sp);
                        if Cp^ = 'l' then
                          Rp^ := '<'
                        else
                          Rp^ := '>';
                      end;
                 'n': if AnsiStrPos(Sp, 'nbsp;') = Sp then  { do not localize }
                      begin
                        Inc(Sp, 4);
                        Rp^ := ' ';
                      end;
                 'q': if AnsiStrPos(Sp, 'quot;') = Sp then  { do not localize }
                      begin
                        Inc(Sp,4);
                        Rp^ := '"';
                      end;
                 '#': begin
                        Tp := Sp;
                        Inc(Tp);
                        while (Sp^ <> ';') and (Sp^ <> #0) do
                          Inc(Sp);
                        SetString(S, Tp, Sp - Tp);
                        Val(S, I, Code);
                        Rp^ := Chr((I));
                      end;
                 else
                   Exit;
               end;
           end
      else
        Rp^ := Sp^;
      end;
      Inc(Rp);
      Inc(Sp);
    end;
  except
  end;
  SetLength(Result, Rp - PChar(Result));
end;

function IsPortOccuped(Host: string; Port: string): boolean;
var sock: TTCPBlockSocket;
begin
  Result := true;
  sock := TTCPBlockSocket.create;
  try
    sock.Connect(Host, Port);
    if sock.lasterror <> 0 then Result := false;
    sock.CloseSocket;
  finally
     sock.free;
  end;
end;

function ResolveHost(HostName: string): string;
{$ifndef windows}
var
  temp: THostEntry;
{$endif}
begin
  Result := '';

  {$ifndef windows}
  temp.Name:='';
  temp.Addr.s_bytes[1] := 0;
  temp.Addr.s_bytes[2] := 0;
  temp.Addr.s_bytes[3] := 0;
  temp.Addr.s_bytes[4] := 0;
  ResolveHostByName(HostName, temp);
  Result := IntToStr(temp.Addr.s_bytes[1]) +'.'+ IntToStr(temp.Addr.s_bytes[2]) +'.'+ IntToStr(temp.Addr.s_bytes[3]) +'.'+ IntToStr(temp.Addr.s_bytes[4]);
  {$endif}
end;

function StripHTML(S: string): string;
var
  TagBegin, TagEnd, TagLength: integer;
begin
  TagBegin := Pos( '<', S);      // search position of first <

  while (TagBegin > 0) do begin  // while there is a < in S
    TagEnd := Pos('>', S);              // find the matching >
    TagLength := TagEnd - TagBegin + 1;
    Delete(S, TagBegin, TagLength);     // delete the tag
    TagBegin:= Pos( '<', S);            // search for next <
  end;

  s := StringReplace(s, #10+#10+#10, '', [rfReplaceAll]);
  s := StringReplace(s, #10+' ', '', [rfReplaceAll]);

  Result := S;                   // give the result
end;

function etxt(s: string; code: longint; encrypt :boolean): string;
var
  KeyLen      :Integer;
  KeyPos      :Integer;
  offset      :Integer;
  dest        :string;
  SrcPos      :Integer;
  SrcAsc      :Integer;
  TmpSrcAsc   :Integer;
  Range       :Integer;

  Key,Src: string;
begin
  Src := s;
  dest := '';

  Key := IntToStr(code);

  KeyLen:=Length(Key);
  KeyPos:=0;
  Range:=256;

  if src <> '' then
     if Encrypt then
     begin
       Randomize;
       offset:=Random(Range);
       dest:=format('%1.2x',[offset]);
       for SrcPos := 1 to Length(Src) do
       begin
         SrcAsc:=(Ord(Src[SrcPos]) + offset) MOD 255;
         if KeyPos < KeyLen then KeyPos:= KeyPos + 1 else KeyPos:=1;
         SrcAsc:= SrcAsc xor Ord(Key[KeyPos]);
         dest:=dest + format('%1.2x',[SrcAsc]);
         offset:=SrcAsc;
       end;
     end
     else
     begin
       try
         offset:=StrToInt('$'+ copy(src,1,2));
         SrcPos:=3;
         if Length(Src) <=2 then
           dest := ''
         else
           repeat
             SrcAsc:=StrToInt('$'+ copy(src,SrcPos,2));
             if KeyPos < KeyLen Then KeyPos := KeyPos + 1 else KeyPos := 1;
             TmpSrcAsc := SrcAsc xor Ord(Key[KeyPos]);
             if TmpSrcAsc <= offset then
               TmpSrcAsc := 255 + TmpSrcAsc - offset
             else
               TmpSrcAsc := TmpSrcAsc - offset;
             dest := dest + chr(TmpSrcAsc);
             offset:=srcAsc;
             SrcPos:=SrcPos + 2;
           until SrcPos >= Length(Src);
       except Dest := ''; end;
     end;
  Result:=Dest;
end;

function ConvertDateTime(datetime: string): TDateTime;
var
  day, month, year, hour, minute: string;
  date: string;
begin
  month := copy(datetime,1,2);
  day := copy(datetime,3,2);
  hour := copy(datetime,5,2);
  minute := copy(datetime,7,2);
  year := copy(datetime,9,4);

  if OSType = 'windows' then
    date := day + '/' + month + '/' + year + ' ' + hour + ':' + minute
  else
    date := day + '-' + month + '-' + year + ' ' + hour + ':' + minute;

  result := StrToDateTime(date);
end;

end.

