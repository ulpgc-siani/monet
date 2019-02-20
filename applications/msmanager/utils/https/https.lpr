program https;

{$mode objfpc}{$H+}

uses
  {$IFDEF UNIX}{$IFDEF UseCThreads}
  cthreads,
  {$ENDIF}{$ENDIF}
  Classes
  ,httpsend,ssl_openssl,synautil;

var HTTP:THTTPSend;
  Result:boolean;
begin
  // En linux instalar el paquete libssl-dev: sudo apt-get install libssl-dev

  HTTP := THTTPSend.Create;
  try
    Result := HTTP.HTTPMethod('GET', 'https://files.gisc.siani.es/monet/mstemplate/?op=beta');
    if Result then
      writeln(ReadStrFromStream(HTTP.Document,HTTP.Document.Size))
    else
      begin
        writeln('HTTP.Sock.LastError :',HTTP.Sock.LastError, ' ; ' ,HTTP.Sock.LastErrorDesc);
        writeln('HTTP.Sock.SSL.LastError :',HTTP.Sock.SSL.LastError, ' ; ' ,HTTP.Sock.SSL.LastErrorDesc);
      end;
  finally
    HTTP.Free;
  end;
end.

