{ Copyright (C) 2006 Darius Blaszijk

  This library is free software; you can redistribute it and/or modify it
  under the terms of the GNU Library General Public License as published by
  the Free Software Foundation; either version 2 of the License, or (at your
  option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT
  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Library General Public License
  for more details.

  You should have received a copy of the GNU Library General Public License
  along with this library; if not, write to the Free Software Foundation,
  Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.


  (De)compression objects kindly donated by Graeme Geldenhuys  (19 october 2006)

}

unit ZipFile;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, LResources, Math, Dialogs, StrUtils,
  {$if defined(VER2_0_0) or defined(VER2_0_1) or defined(VER2_0_2)}
    gzCrc
  {$ELSE}
    crc
  {$ENDIF},
  tiCompress, tiCompressNone, tiCompressZLib, tiConstants;

resourcestring
  rsFilenameSDoesNotExistInS = 'Filename %s does not exist in %s';
  rsZipfileSDoesNotExist = 'Zipfile %s does not exist';

type
  TZipSearchRec = record
    DateTime : TDateTime;
    USize : Int64;
    CSize: Int64;
    Name : TFileName;
  end;
  
  TLocalFileHeaderStart = packed record
    signature:         longword;      {local file header signature     4 bytes  (0x04034b50)}
    extractversion:    word;          {version needed to extract       2 bytes}
    generalpurposebit: word;          {general purpose bit flag        2 bytes}
    compressmethod:    word;          {compression method              2 bytes}
    lastmodtime:       word;          {last mod file time              2 bytes}
    lastmoddate:       word;          {last mod file date              2 bytes}
    crc32:             longword;      {crc-32                          4 bytes}
    compressedsize:    longword;      {compressed size                 4 bytes}
    uncompressedsize:  longword;      {uncompressed size               4 bytes}
    filenamelength:    word;          {file name length                2 bytes}
    extrafieldlength:  word;          {extra field length              2 bytes}
  end;
  
  TLocalFileHeaderAdditional = packed record
    filename: string;
    extrafield: string;
  end;
  
  TLocalFileHeader = packed record
    start: TLocalFileHeaderStart;
    add: TLocalFileHeaderAdditional;
  end;

  TCDFileHeaderStart = packed record
    signature:              longword; {central file header signature   4 bytes  (0x02014b50)}
    versionmadeby:          word;     {version made by                 2 bytes}
    versiontoextract:       word;     {version needed to extract       2 bytes}
    generalpurposebit:      word;     {general purpose bit flag        2 bytes}
    compressionmethod:      word;     {compression method              2 bytes}
    lastmodfiletime:        word;     {last mod file time              2 bytes}
    lastmodfiledate:        word;     {last mod file date              2 bytes}
    crc32:                  longword; {crc-32                          4 bytes}
    compressedsize:         longword; {compressed size                 4 bytes}
    uncompressedsize:       longword; {uncompressed size               4 bytes}
    filenamelength:         word;     {file name length                2 bytes}
    extrafieldlength:       word;     {extra field length              2 bytes}
    filecommentlength:      word;     {file comment length             2 bytes}
    disknumberstart:        word;     {disk number start               2 bytes}
    internalfileattributes: word;     {internal file attributes        2 bytes}
    externalfileattributed: longword; {external file attributes        4 bytes}
    reloffsetlocalheader:   longword; {relative offset of local header 4 bytes}
  end;

  TCDFileHeaderAdditional = packed record
    filename:    string;              {file name (variable size)}
    extrafield:  string;              {extra field (variable size)}
    filecomment: string;              {file comment (variable size)}
  end;
  
  TCDFileHeader = packed record
    start: TCDFileHeaderStart;
    add: TCDFileHeaderAdditional;
  end;
  
  TEndOfCentralDirectoryRecordStart = packed record
    endofcentraldirsignature:  longword; {end of central dir signature    4 bytes  (0x06054b50)}
    numberofthisdisk:          word;     {number of this disk             2 bytes}
    numberofthisdiskwithcd:    word;     {number of the disk with the}
                                         {start of the central directory  2 bytes}
    numberofcdentries:         word;     {total number of entries in the}
                                         {central directory on this disk  2 bytes}
    totalnumberofcdentries:    word;     {total number of entries in}
                                         {the central directory           2 bytes}
    sizeofthecentraldirectory: longword; {size of the central directory   4 bytes}
    cdoffset:                  longword; {offset of start of central}
                                         {directory with respect to}
                                         {the starting disk number        4 bytes}
    ZIPfilecommentlength:      word;     {.ZIP file comment length        2 bytes}
  end;
  
  TEndOfCentralDirectoryRecordAdditional = packed record
    ZIPfilecomment: string;           {.ZIP file comment       (variable size)}
  end;

  TEndOfCentralDirectoryRecord = packed record
    start: TEndOfCentralDirectoryRecordStart;
    add: TEndOfCentralDirectoryRecordAdditional;
  end;
  
  TZipFileItem = record
    lfh: TLocalFileHeader;
    filedata: Pbyte;
  end;

  TFileChangedEvent = procedure(Sender: TObject) of object;

  TZipFile = class(TComponent)
  protected
    endofcdrecord: TEndOfCentralDirectoryRecord;
    endofcdrecordstartpos: longword;
    FActive: boolean;
    FFileName: string;
    fileheaderlist: array of TCDFileHeader;
    fileindex: longword;
    FOnFileChanged: TFileChangedEvent;
    fs: TFileStream;

    function EndOfCDRecordPosition: longword;
    function FileNameIndex(AFileName: string): longint;
    function GetSearchResult(var SearchResult : TZipSearchRec): integer;
    function GetStreamCrc32(Stream: TStream): longword;
    function MakeLocalFileHeader(FileName: string; FSize: Int64; crc32: cardinal; FileDateTime: TDateTime): TLocalFileHeader;
    function ReadLocalFileHeader(AOffSet: longword): TLocalFileHeader;
    function ShowCDFileHeaderReport(index: longword): TStrings;
    function ShowEndOfCDRecordReport: TStrings;
    function ShowLocalFileHeaderReport(index: longword): TStrings;
    procedure AddCDFileHeader(FSize: int64; AFileName: string; LocalOffset: longword; LFHSize: Int64; crc32: cardinal; FileDateTime: TDateTime);
    procedure DateTimeToDosDateTime(DateTime: TDateTime; var dosdate, dostime: word);
    procedure DosDateTimeToDateTime(dosdate, dostime: word; var DateTime: TDateTime);
    procedure GetCDFileHeaders;
    procedure GetEndOfCDRecord;
    procedure Reset;
    procedure SetActive(Value: boolean);
    procedure SetFileName(Value: string);
  public
    fileheadercount: longword;
    
    constructor Create(AOwner: TComponent);
    destructor Destroy; override;

    procedure Activate;

    //file functions
    function GetFileStream(AFileName: string; var BufLength: longword): TMemoryStream;
    function FileCount: longword;
    function FileExists(AFileName: string): boolean;
    function FindFirst(var SearchResult : TZipSearchRec): integer;
    function FindNext(var SearchResult : TZipSearchRec): integer;
    procedure AppendFileFromDisk(AFileName, ZIPFileName: string);
    procedure AppendStream(Stream: TStream; ZIPFileName: string; FileDateTime: TDateTime);
    procedure DeleteFile(AFileName: string);
    procedure UpdateFile(Stream: TStream; ZIPFileName: string);

    //reporting
    function Report: TStrings;
  published
    property FileName: string read FFileName write SetFileName;
    property Active: boolean read FActive write SetActive;
    property OnFileChanged: TFileChangedEvent read FOnFileChanged write FOnFileChanged;
  end;
  
procedure Register;

implementation

procedure Register;
begin
  RegisterComponents('MB', [TZipFile]);
end;

constructor TZipFile.Create(AOwner: TComponent);
begin
  inherited Create(AOwner);

  endofcdrecord.start.endofcentraldirsignature := $06054b50;

  Reset;
end;

destructor TZipFile.Destroy;
begin
  inherited Destroy;

  Reset;
end;

procedure TZipFile.Reset;
begin
  FActive := False;
  FFileName := '';
  endofcdrecordstartpos := 0;
  fileheadercount:= 0;
  fileindex := 0;
  SetLength(fileheaderlist, fileheadercount);

  if Assigned(fs) then
    FreeAndNil(fs);
end;

procedure TZipFile.SetFileName(Value: string);
begin
  if Value <> FFileName then
  begin
    Reset;
    FFileName := Value;
  end;
end;

procedure TZipFile.Activate;
begin
  Active := True;
end;

procedure TZipFile.SetActive(Value: boolean);
var
  i: integer;
begin
  if Value <> FActive then
  begin
    FActive := Value;

    if Value = True then
    begin
      if not SysUtils.FileExists(FileName) then
      begin
        fs := TFileStream.Create(FileName, fmCreate);

        //write EOCDH
        fs.WriteBuffer(endofcdrecord.start, SizeOf(endofcdrecord.start));
        fs.WriteBuffer(endofcdrecord.add.ZIPfilecomment[1], Length(endofcdrecord.add.ZIPfilecomment));

        fs.Free;
      end;

      //open the filestream to the zipfile
      fs := TFileStream.Create(FileName, fmOpenReadWrite);

      //get position of end of CD header
      endofcdrecordstartpos := EndOfCDRecordPosition;

      //get end of CD record from disk
      GetEndOfCDRecord;

      //read all file headers from zipfile
      fs.Seek(endofcdrecord.start.cdoffset, soFromBeginning);
      for i := 1 to endofcdrecord.start.numberofcdentries do
        GetCDFileHeaders;

      if Assigned(FOnFileChanged) then FOnFileChanged(Self);
    end
    else
      Reset;
  end;
end;

procedure TZipFile.GetCDFileHeaders;
begin
  Inc(fileheadercount);
  SetLength(fileheaderlist, fileheadercount);
  
  with fileheaderlist[Pred(fileheadercount)] do
  begin
    //read fixed record
    fs.ReadBuffer(start, SizeOf(start));

    //do not change the order of the remaining statements unless you know what you are doing
    SetLength(add.filename, start.filenamelength);
    fs.ReadBuffer(add.filename[1], start.filenamelength);
    SetLength(add.extrafield, start.extrafieldlength);
    fs.ReadBuffer(add.extrafield[1], start.extrafieldlength);
    SetLength(add.filecomment, start.filecommentlength);
    fs.ReadBuffer(add.filecomment[1], start.filecommentlength);
  end;
end;

procedure TZipFile.GetEndOfCDRecord;
begin
  fs.Seek(endofcdrecordstartpos, soFromBeginning);
  
  //read fixed part
  fs.ReadBuffer(endofcdrecord.start, SizeOf(endofcdrecord.start));
  
  //read variable part
  SetLength(endofcdrecord.add.ZIPfilecomment, endofcdrecord.start.ZIPfilecommentlength);
  fs.ReadBuffer(endofcdrecord.add.ZIPfilecomment[1], endofcdrecord.start.ZIPfilecommentlength);
end;

function TZipFile.EndOfCDRecordPosition: longword;
var
  len: LongWord;
  buf: string;
  i: integer;
begin
  //check if the position was already determined before
  if endofcdrecordstartpos <> 0 then
  begin
    Result := endofcdrecordstartpos;
    exit;
  end;
  
  //calculate the size of the buffer for the endofcdheader record
  //65557bytes is the maximum size of the datastructure
  fs.Seek(0,soFromEnd);
  len := Min(65557, fs.Position);

  //reset the filestream to start looking from the end
  fs.Seek(-len,soFromEnd);

  //read data segment
  SetLength(buf, len);
  fs.ReadBuffer(buf[1], len);

  //search for the start of the end of header record signature
  for i:= Length(buf) - 21 downto 1 do
    if(buf[i]='P') and (buf[i+1]='K') and (buf[i+2]=#5) and (buf[i+3]=#6) then
      break;

  Result := Pred(i);
end;

function GetBit(const Value: DWord; const Bit: Byte): Boolean;
begin
  Result := (Value and (1 shl Bit)) <> 0;
end;

function TZipFile.ReadLocalFileHeader(AOffSet: longword): TLocalFileHeader;
begin
  fs.Seek(AOffset, soFromBeginning);

  //read local header start
  fs.ReadBuffer(Result.start, SizeOf(Result.start));

  //read filename
  SetLength(Result.add.filename, Result.start.filenamelength);
  fs.ReadBuffer(Result.add.filename[1], Result.start.filenamelength);

  //read extra field
  SetLength(Result.add.extrafield, Result.start.ExtraFieldLength);
  fs.ReadBuffer(Result.add.extrafield[1], Result.start.extrafieldlength);
end;

function TZipFile.GetFileStream(AFileName: string; var BufLength: longword): TMemoryStream;
var
  index: longword;
  lfh: TLocalFileHeader;
  filedataoffset: longword;
  lCompressed : TMemoryStream ;
  lCompress : TtiCompressAbs ;
begin
  index := FileNameIndex(AFileName);
  
  if (index < 0) or (index > Pred(fileheadercount)) then
    raise Exception.CreateFmt(rsFilenameSDoesNotExistInS, [AFileName, FileName]);
    
  lfh := ReadLocalFileHeader(FileHeaderList[index].start.reloffsetlocalheader);
  BufLength := FileHeaderList[index].start.compressedsize;

  filedataoffset := FileHeaderList[index].start.reloffsetlocalheader + 30 +
                    lfh.start.filenamelength + lfh.start.extrafieldlength;

  fs.Seek(filedataoffset, soFromBeginning);

  lCompressed := TMemoryStream.Create;
  lCompressed.CopyFrom(fs, BufLength);
  lCompressed.Position := 0;

  Result := TMemoryStream.Create;
  try
    //select the appropriate compression method
    lCompress := gCompressFactory.CreateInstance(cgsCompressNone);
    try
      lCompress.DecompressStream(lCompressed, Result);
    finally
      lCompress.Free ;
    end ;
  finally
    lCompressed.Free ;
  end ;
end;

function TZipFile.FileNameIndex(AFileName: string): longint;
var
  i: longint;
begin
  Result := -1;

  for i:= 0 to Pred(fileheadercount) do
    if FileHeaderList[i].add.filename = AFileName then
    begin
      Result := i;
      Break;
    end;
end;

function TZipFile.FileCount: longword;
begin
  Result := fileheadercount;
end;

function TZipFile.FileExists(AFileName: string): boolean;
begin
  Result := FileNameIndex(AFileName) <> -1;
end;

function TZipFile.GetSearchResult(var SearchResult: TZipSearchRec): integer;
var
  FileDateTime: TDateTime;
begin
  Result := 0;
  if fileindex >= fileheadercount then
  begin
    Result := 1;
    exit;
  end;

  with SearchResult do
  begin
    DosDateTimeToDateTime(FileHeaderList[fileindex].start.lastmodfiledate,
                          FileHeaderList[fileindex].start.lastmodfiletime,
                          FileDateTime);
    DateTime := FileDateTime;
    USize := FileHeaderList[fileindex].start.uncompressedsize;
    CSize := FileHeaderList[fileindex].start.compressedsize;
    Name := FileHeaderList[fileindex].add.filename
  end;
end;

function TZipFile.FindFirst(var SearchResult: TZipSearchRec): integer;
begin
  fileindex := 0;

  Result := GetSearchResult(SearchResult);
end;

function TZipFile.FindNext(var SearchResult: TZipSearchRec): integer;
begin
  Inc(fileindex);

  Result := GetSearchResult(SearchResult);
end;

procedure TZipFile.DateTimeToDosDateTime(DateTime: TDateTime; var dosdate, dostime: word);
var
  y, m, d, h, n, s, ms: word;
begin
  DecodeDate(DateTime, y, m, d);
  DecodeTime(DateTime, h, n, s, ms);

  y := y - 1980;

  dosdate := d + (32 * m) + (512 * y);
  dostime := (s div 2) + (32 * n) + (2048 * h);
end;

procedure TZipFile.DosDateTimeToDateTime(dosdate, dostime: word; var DateTime: TDateTime);
var
  y, m, d, h, n, s: word;
begin
  y := dosdate div 512 + 1980;
  dosdate := dosdate mod 512;
  m := dosdate div 32;
  dosdate := dosdate mod 32;
  d := dosdate;

  h := dostime div 2048;
  dostime := dostime mod 2048;
  n := dostime div 32;
  dostime := dostime mod 32;
  s := dostime * 2;

  DateTime := EncodeDate(y, m, d) + EncodeTime(h, n, s, 0);
end;

procedure TZipFile.AddCDFileHeader(FSize: int64; AFileName: string; LocalOffset: longword; LFHSize: Int64; crc32: cardinal; FileDateTime: TDateTime);
begin
  Inc(fileheadercount);
  SetLength(fileheaderlist, fileheadercount);

  with fileheaderlist[Pred(fileheadercount)] do
  begin
    start.signature := $02014b50;
    start.versionmadeby := $0B14;
    start.versiontoextract := $000A;
    start.generalpurposebit := $0000;
    start.compressionmethod := $0000;
    DateTimeToDosDateTime(FileDateTime , start.lastmodfiledate, start.lastmodfiletime);
    start.crc32 := crc32;
    start.compressedsize := FSize;
    start.uncompressedsize := FSize;
    start.filenamelength := Length(AFileName);
    start.extrafieldlength := 0;
    start.filecommentlength := 0;
    start.disknumberstart := 0;
    start.internalfileattributes := 1;
    start.externalfileattributed := 32;
    start.reloffsetlocalheader := LocalOffset;

    add.filename := AFileName;
    add.extrafield := '';
    add.filecomment := '';
  end;
  
  //update EOCDH
  Inc(endofcdrecord.start.numberofcdentries);
  Inc(endofcdrecord.start.totalnumberofcdentries);
  Inc(endofcdrecord.start.sizeofthecentraldirectory, SizeOf(fileheaderlist[Pred(fileheadercount)].start) + Length(AFileName));
  Inc(endofcdrecord.start.cdoffset, LFHSize + Length(AFileName) + FSize);
end;

function TZipFile.MakeLocalFileHeader(FileName: string; FSize: Int64; crc32: cardinal; FileDateTime: TDateTime): TLocalFileHeader;
begin
  with Result do
  begin
    start.signature := $04034b50;
    start.extractversion := $000A;
    start.generalpurposebit := $0000;
    start.compressmethod := $0000;
    DateTimeToDosDateTime(FileDateTime , start.lastmoddate, start.lastmodtime);
    start.crc32 := crc32;
    start.compressedsize := FSize;
    start.uncompressedsize := FSize;
    start.filenamelength := Length(FileName);
    start.extrafieldlength := 0;

    add.filename := FileName;
    add.extrafield := '';
  end;
end;

procedure TZipFile.AppendStream(Stream: TStream; ZIPFileName: string; FileDateTime: TDateTime);
var
  localoffset: longword;
  count: longint;
  i: longword;
  lfh: TLocalFileHeader;
  crc32: cardinal;

  lCompressed : TMemoryStream ;
  lCompress : TtiCompressAbs ;
begin
  crc32 := GetStreamCrc32(Stream);
  lfh := MakeLocalFileHeader(ZIPFileName, Stream.Size, crc32, FileDateTime);
  localoffset := endofcdrecord.start.cdoffset;
  AddCDFileHeader(Stream.Size, ZIPFileName, localoffset, SizeOf(lfh.start), crc32, FileDateTime);

  //compress data
  lCompressed := TMemoryStream.Create;
  try
    lCompress := gCompressFactory.CreateInstance(cgsCompressNone);
    try
      lCompress.CompressStream(Stream, lCompressed);
    finally
      lCompress.Free;
    end;
  finally
  end;

  //update lfh
  lfh.start.compressedsize := lCompressed.Size;

  //write local file header
  fs.Seek(localoffset, soFromBeginning);
  fs.WriteBuffer(lfh.start, SizeOf(lfh.start));

  //write local file header additional parameters
  fs.WriteBuffer(lfh.add.filename[1], Length(lfh.add.filename));
  fs.WriteBuffer(lfh.add.extrafield[1], Length(lfh.add.extrafield));

  //write local file data
  fs.CopyFrom(lCompressed, lCompressed.Size);

  lCompressed.Free;

  //write CD
  for i := 0 to Pred(fileheadercount) do
  begin
    count := SizeOf(fileheaderlist[i].start);
    fs.WriteBuffer(fileheaderlist[i].start, count);
    fs.WriteBuffer(fileheaderlist[i].add.filename[1], Length(fileheaderlist[i].add.filename));
    fs.WriteBuffer(fileheaderlist[i].add.extrafield[1], Length(fileheaderlist[i].add.extrafield));
    fs.WriteBuffer(fileheaderlist[i].add.filecomment[1], Length(fileheaderlist[i].add.filecomment));
  end;

  //write EOCDH
  count := SizeOf(endofcdrecord.start);
  fs.WriteBuffer(endofcdrecord.start, count);
  fs.WriteBuffer(endofcdrecord.add.ZIPfilecomment[1], Length(endofcdrecord.add.ZIPfilecomment));

  if Assigned(FOnFileChanged) then FOnFileChanged(Self);
end;

procedure TZipFile.AppendFileFromDisk(AFileName, ZIPFileName: string);
var
  newfile: TFileStream;
  FileDateTime: TDateTime;
begin
  if not SysUtils.FileExists(AFileName) then
    raise Exception.CreateFmt(rsZipfileSDoesNotExist, [AFileName]);

  newfile := TFileStream.Create(AFileName,fmOpenRead);
  FileDateTime := FileDateToDateTime(FileAge(AFileName));

  AppendStream(newfile, ZIPFileName, FileDateTime);
  
  newfile.Free;
end;

function TZipFile.GetStreamCrc32(Stream: TStream): longword;
var
  pos: Int64;
  buf: Pbyte;
  buflen: longword;
begin
  pos := Stream.Position;
  Stream.Position := 0;

  buf := GetMem(1024);

  Result := crc32(0,nil,0);
  repeat
    buflen := Stream.Read(buf^, 1024);
    Result := crc32(Result, buf, buflen);
  until buflen <> SizeOf(buf);

  FreeMem(buf);
  
  Stream.Position := pos;
end;

procedure TZipFile.DeleteFile(AFileName: string);
var
  index: longword;
  i: longword;
  startbuf: longword;
  endbuf: longword;
  frompos: longword;
  topos: longword;
  buflen: longword;
  buf: Pbyte;
  cditemsize: longword;
begin
  index := FileNameIndex(AFileName);

  if (index < 0) or (index > Pred(fileheadercount)) then
    raise Exception.CreateFmt(rsFilenameSDoesNotExistInS, [AFileName, FileName]);

  startbuf := fileheaderlist[index].start.reloffsetlocalheader;
  if index = Pred(fileheadercount) then
    endbuf := endofcdrecord.start.cdoffset
  else
    endbuf := fileheaderlist[Succ(index)].start.reloffsetlocalheader;
    
  //move local file headers on disk
  if index <> Pred(fileheadercount) then
  begin
    buf := GetMem(1024);
    frompos := endbuf;
    topos := startbuf;
    repeat
      fs.Seek(frompos, soFromBeginning);
      buflen := fs.Read(buf^, Min(fs.Size - frompos, 1024));

      fs.Seek(topos, soFromBeginning);
      fs.Write(buf^, buflen);
      
      Inc(frompos, buflen);
      Inc(topos, buflen);
    until buflen < 1024;
    FreeMem(buf);
  end;

  //calculate new offsets for all CD
  if index <> Pred(fileheadercount) then
    for i:= Succ(index) to Pred(fileheadercount) do
      Dec(fileheaderlist[i].start.reloffsetlocalheader, endbuf - startbuf);

  //get size of deleted CD item
  cditemsize := SizeOf(fileheaderlist[index].start) +
                fileheaderlist[index].start.filenamelength +
                fileheaderlist[index].start.extrafieldlength +
                fileheaderlist[index].start.filecommentlength;

  //delete CD
  for i:= Succ(index) to Pred(fileheadercount) do
    fileheaderlist[Pred(i)] := fileheaderlist[i];
  Dec(fileheadercount);
  SetLength(fileheaderlist, fileheadercount);

  //calculate new values for EOCDH
  Dec(endofcdrecord.start.cdoffset, endbuf - startbuf);
  Dec(endofcdrecord.start.numberofcdentries);
  Dec(endofcdrecord.start.totalnumberofcdentries);
  Dec(endofcdrecord.start.sizeofthecentraldirectory, cditemsize);

  //write CD to disk
  fs.Seek(endofcdrecord.start.cdoffset, soFromBeginning);
  if fileheadercount <> 0 then
    for i:= 0 to Pred(fileheadercount) do
    begin
      fs.WriteBuffer(fileheaderlist[i].start, SizeOf(fileheaderlist[i].start));
      fs.WriteBuffer(fileheaderlist[i].add.filename[1], fileheaderlist[i].start.filenamelength);
      fs.WriteBuffer(fileheaderlist[i].add.extrafield[1], fileheaderlist[i].start.extrafieldlength);
      fs.WriteBuffer(fileheaderlist[i].add.filecomment[1], fileheaderlist[i].start.filecommentlength);
    end;

  //write EOCDH
  fs.WriteBuffer(endofcdrecord.start, SizeOf(endofcdrecord.start));

  //overwrite the zipfile comment
  fs.WriteBuffer(endofcdrecord.add.ZIPfilecomment[1], endofcdrecord.start.ZIPfilecommentlength);

  //truncate file
  fs.Size := fs.Size - cditemsize - endbuf + startbuf;

  if Assigned(FOnFileChanged) then FOnFileChanged(Self);
end;

procedure TZipFile.UpdateFile(Stream: TStream; ZIPFileName: string);
begin
  if FileExists(ZIPFileName) then
    DeleteFile(ZIPFileName);
  AppendStream(Stream, ZIPFileName, Now);
end;

function TZipFile.Report: TStrings;
var
  i: integer;
begin
  Result := TStringList.Create;

  Result.Add(Format('Archive: %s',[FileName]));
  Result.Add('');
  Result.Add('');
  Result.Add('Local file headers  (note: current implementation only finds LFH''s through CDFH)');
  Result.Add('------------------');
  Result.Add('');
  Result.Add(Format('Number of entries: %d',[fileheadercount]));
  Result.Add('');

  for i := 0 to Pred(fileheadercount) do
    Result.AddStrings(ShowLocalFileHeaderReport(i));

  Result.Add('');
  Result.Add('File headers');
  Result.Add('------------');
  Result.Add('');
  Result.Add(Format('Number of entries: %d',[fileheadercount]));
  Result.Add('');

  for i := 0 to Pred(fileheadercount) do
    Result.AddStrings(ShowCDFileHeaderReport(i));

  Result.Add('');
  Result.Add('');
  Result.Add('End of central directory record:');
  Result.Add('--------------------------------');
  Result.Add('');
  Result.AddStrings(ShowEndOfCDRecordReport);
end;

function TZipFile.ShowLocalFileHeaderReport(index: longword): TStrings;
var
  lfh: TLocalFileHeader;
  s: string;
  o: string;
begin
  Result := TStringList.Create;

  lfh := ReadLocalFileHeader(FileHeaderList[index].start.reloffsetlocalheader);

  s := IntToStr(Succ(index));
  o := DupeString(' ', Length(s));
  with Result do
  begin
    Add(Format('%s. signature        : $%.8x', [s, lfh.start.signature]));
    Add(Format('%s  extractversion   : %d', [o, lfh.start.extractversion]));
    Add(Format('%s  bitflag          : $%.4x', [o, lfh.start.generalpurposebit]));
    Add(Format('%s  compressmethod   : %d', [o, lfh.start.compressmethod]));
    Add(Format('%s  lastmodtime      : %d', [o, lfh.start.lastmodtime]));
    Add(Format('%s  lastmoddate      : %d', [o, lfh.start.lastmoddate]));
    Add(Format('%s  crc32            : $%.8x', [o, lfh.start.crc32]));
    Add(Format('%s  compressedsize   : %d', [o, lfh.start.compressedsize]));
    Add(Format('%s  uncompressedsize : %d', [o, lfh.start.uncompressedsize]));
    Add(Format('%s  filenamelength   : %d', [o, lfh.start.filenamelength]));
    Add(Format('%s  extrafieldlength : %d', [o, lfh.start.extrafieldlength]));
    Add(Format('%s  filename         : %s', [o, lfh.add.filename]));
    Add(Format('%s  extra field      : %s', [o, lfh.add.extrafield]));
  end;
end;

function TZipFile.ShowEndOfCDRecordReport: TStrings;
begin
  Result := TStringList.Create;

  with Result do
  begin
    Add(Format('endofcentraldirsignature  : $%.8x', [endofcdrecord.start.endofcentraldirsignature]));
    Add(Format('numberofthisdisk          : %d', [endofcdrecord.start.numberofthisdisk]));
    Add(Format('numberofthisdiskwithcd    : %d', [endofcdrecord.start.numberofthisdiskwithcd]));
    Add(Format('numberofcdentries         : %d', [endofcdrecord.start.numberofcdentries]));
    Add(Format('totalnumberofcdentries    : %d', [endofcdrecord.start.totalnumberofcdentries]));
    Add(Format('sizeofthecentraldirectory : %d', [endofcdrecord.start.sizeofthecentraldirectory]));
    Add(Format('cdoffset                  : %d', [endofcdrecord.start.cdoffset]));
    Add(Format('ZIPfilecommentlength      : %d', [endofcdrecord.start.ZIPfilecommentlength]));
    Add(Format('ZIPfilecomment            : %s', [endofcdrecord.add.ZIPfilecomment]))
  end;
end;

function TZipFile.ShowCDFileHeaderReport(index: longword): TStrings;
var
  s: string;
  o: string;
begin
  Result := TStringList.Create;

  if (index < 0) or (index >= fileheadercount) then
    Raise Exception.Create('fileheader index out of bounds!');

  s := IntToStr(Succ(index));
  o := DupeString(' ', Length(s));
  with FileHeaderList[index] do
  begin
    Result.Add(Format('%s. signature              : $%.8x', [s, start.signature]));
    Result.Add(Format('%s  versionmadeby          : $%.4x', [o, start.versionmadeby]));
    Result.Add(Format('%s  versiontoextract       : $%.4x', [o, start.versiontoextract]));
    Result.Add(Format('%s  generalpurposebit      : $%.4x', [o, start.generalpurposebit]));
    Result.Add(Format('%s  compressionmethod      : $%.4x', [o, start.compressionmethod]));
    Result.Add(Format('%s  lastmodfiletime        : $%.4x', [o, start.lastmodfiletime]));
    Result.Add(Format('%s  lastmodfiledate        : $%.4x', [o, start.lastmodfiledate]));
    Result.Add(Format('%s  crc32                  : $%.8x', [o, start.crc32]));
    Result.Add(Format('%s  compressedsize         : %d', [o, start.compressedsize]));
    Result.Add(Format('%s  uncompressedsize       : %d', [o, start.uncompressedsize]));
    Result.Add(Format('%s  filenamelength         : %d', [o, start.filenamelength]));
    Result.Add(Format('%s  extrafieldlength       : %d', [o, start.extrafieldlength]));
    Result.Add(Format('%s  filecommentlength      : %d', [o, start.filecommentlength]));
    Result.Add(Format('%s  disknumberstart        : %d', [o, start.disknumberstart]));
    Result.Add(Format('%s  internalfileattributes : %d', [o, start.internalfileattributes]));
    Result.Add(Format('%s  externalfileattributed : %d', [o, start.externalfileattributed]));
    Result.Add(Format('%s  reloffsetlocalheader   : %d', [o, start.reloffsetlocalheader]));
    Result.Add(Format('%s  filename               : %s', [o, add.filename]));
    Result.Add(Format('%s  extrafield             : %s', [o, add.extrafield]));
    Result.Add(Format('%s  filecomment            : %s', [o, add.filecomment]));
  end;
end;

initialization
  {$I tzipfile.lrs}

end.
