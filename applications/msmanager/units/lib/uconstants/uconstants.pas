unit uconstants;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils; 

const
  UriSSHConsole = 'deployservice_manager/term.jnlp';
  UriFederation = 'federation';
  MonetServerTemplateFileName = 'mse.ova';
  MonetServerTemplateFileNameMD5 = 'mse.ova.md5';
  MonetServerTemplateName = 'Monet Server Emulator';
  MonetServerTemplateShortName = 'MSE';

  ServerID = '98c012a8b263929cf4cf4271bc105df6';
  ContainerID = 'dfd3138392337507568d85aba91379ab';

  UrlApps = 'http://files.ces.siani.es/monet/app';
  UrlAppsUlpgc = 'http://files.ces.siani.es/monet/app';

  UrlMSTemplateBeta = 'http://files.ces.siani.es/monet/mstemplate/?op=beta';
  UrlMSTemplateStable = 'http://files.ces.siani.es/monet/mstemplate/?op=stable';

  UrlMSTemplateBetaUlpgc = 'http://files.ces.siani.es/monet/mstemplate/?op=beta';
  UrlMSTemplateStableUlpgc = 'http://files.ces.siani.es/monet/mstemplate/?op=stable';

  UrlDownloadVirtualbox = 'https://www.virtualbox.org/wiki/Downloads';
  EMPTY='';

  ListSelectIndex = 0;
  ListNameIndex = 1;
  ListStatus = 2;

  ServerLabelLocal = 'Server local: ';
  ServerLabelRemote = 'Server remote: ';

  CodeCrypt = 5929348;

  function LineBreak: string;

implementation

uses utools;

function LineBreak: string;
begin
  Result := #10;
  if OSType = 'windows' then
    Result := #13#10;
end;

end.

