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
}

unit ZipFile_tc;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, fpcunit, testutils, testregistry, ZipFile;

type
  THackZipFile = class(TZipFile);
  
  { TZipFileTest }
  TZipFileTest = class(TTestCase)
  private
    zf: TZipFile;
  protected
    procedure SetUp; override;
    procedure TearDown; override;
  published
    procedure AppendFileFromDisk;
    procedure AppendStream;
    procedure DateTimeToDosDateTime;
    procedure DeleteFile;
    procedure DosDateTimeToDateTime;
    procedure EndOfCDRecordPosition;
    procedure FileNameIndex;
    procedure GetFileStream;
    procedure GetStreamCrc32;
    procedure UpdateFile;
    procedure FileCount;
    procedure FileExists;
    procedure FindFirst;
    procedure FindNext;
  end;

implementation

procedure TZipFileTest.SetUp;
var
  s: TStringStream;
  fdt: TDateTime;
begin
  zf := TZipFile.Create(nil);
  //zf.FileName := GetTempFileName;
  zf.FileName := 'tmp.zip';
  zf.Activate;

  //only setup the temp fiole once
  if zf.FileCount < 3 then
  begin
    //make file date time
    fdt := EncodeDate(2006, 9, 17) + EncodeTime(11, 51, 44, 00);

    //add some data
    s := TStringStream.Create('test1');
    zf.AppendStream(s, 'first.txt', fdt);
    s.Free;

    s := TStringStream.Create('test2');
    fdt := fdt + 1;
    zf.AppendStream(s, 'second.txt', fdt);
    s.Free;

    s := TStringStream.Create('test3');
    fdt := fdt + 1;
    zf.AppendStream(s, 'folder\third.txt', fdt);
    s.Free;
  end;
end;

procedure TZipFileTest.TearDown;
begin
  zf.Free;
  //SysUtils.DeleteFile(zf.FileName);
end;

procedure TZipFileTest.AppendFileFromDisk;
begin
  Fail('No testcase defined.');
end;

procedure TZipFileTest.AppendStream;
begin
  Fail('No testcase defined.');
end;

procedure TZipFileTest.DateTimeToDosDateTime;
var
  dosdate: word;
  dostime: word;
  StartDateTime: TDateTime;
  ResultDateTime: TDateTime;
begin
  StartDateTime := EncodeDate(2006, 9, 17) + EncodeTime(11, 51, 44, 00);
  THackZipFile(zf).DateTimeToDosDateTime(StartDateTime, dosdate, dostime);

  AssertEquals('Invalid conversion to dostime', 24182, dostime);
  AssertEquals('Invalid conversion to dosdate', 13617, dosdate);
end;

procedure TZipFileTest.DeleteFile;
begin
  Fail('No testcase defined.');
end;

procedure TZipFileTest.DosDateTimeToDateTime;
var
  dosdate: word;
  dostime: word;
  StartDateTime: TDateTime;
  ResultDateTime: TDateTime;
begin
  StartDateTime := EncodeDate(2006, 9, 17) + EncodeTime(11, 51, 44, 00);
  THackZipFile(zf).DateTimeToDosDateTime(StartDateTime, dosdate, dostime);
  THackZipFile(zf).DosDateTimeToDateTime(dosdate, dostime, ResultDateTime);

  AssertEquals('Invalid conversion to datetime from dosdatetime', StartDateTime, ResultDateTime);
end;

procedure TZipFileTest.EndOfCDRecordPosition;
begin
  Fail('No testcase defined.');
end;

procedure TZipFileTest.FileNameIndex;
begin
  AssertEquals('Invalid filename index', 1, THackZipFile(zf).FileNameIndex('second.txt'));
end;

procedure TZipFileTest.GetFileStream;
begin
  Fail('No testcase defined.');
end;

procedure TZipFileTest.GetStreamCrc32;
var
  stream: TStringStream;
begin
  stream := TStringStream.Create('testing');

  AssertEquals('Invalid CRC32 value', $E8F35A06, THackZipFile(zf).GetStreamCrc32(stream));

  stream.Free;
end;

procedure TZipFileTest.UpdateFile;
begin
  Fail('No testcase defined.');
end;

procedure TZipFileTest.FileExists;
begin
  Fail('No testcase defined.');
end;

procedure TZipFileTest.FindFirst;
var
  sr: TZipSearchRec;
begin
  zf.FindFirst(sr);
  AssertEquals('Invalid first file found', 'first.txt', sr.Name);
end;

procedure TZipFileTest.FindNext;
var
  sr: TZipSearchRec;
begin
  zf.FindNext(sr);
  AssertEquals('Invalid second file found', 'second.txt', sr.Name);

  zf.FindNext(sr);
  AssertEquals('Invalid third file found', 'folder\third.txt', sr.Name);
end;

procedure TZipFileTest.FileCount;
begin
  AssertEquals('Invalid number of files', 3, zf.FileCount);
end;

initialization
  RegisterTest(TZipFileTest);

end.
