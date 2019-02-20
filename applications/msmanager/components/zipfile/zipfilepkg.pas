{ This file was automatically created by Lazarus. Do not edit!
This source is only used to compile and install the package.
 }

unit ZipFilePkg; 

interface

uses
  ZipFile, LazarusPackageIntf; 

implementation

procedure Register; 
begin
  RegisterUnit('ZipFile', @ZipFile.Register); 
end; 

initialization
  RegisterPackage('ZipFilePkg', @Register); 
end.
