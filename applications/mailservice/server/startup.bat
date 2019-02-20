@echo off
set CPATH=.
set CPATH=%CPATH%;etc\


for %%f in (lib\*.jar) do (call addcp.bat %%f)
for %%f in (plugins\*.jar) do (call addcp.bat %%f)

rem clamAV setup
for %%f in (plugins\filters\ClamAV\*.jar) do (call addcp.bat %%f)

rem SA setup
for %%f in (plugins\filters\SA\*.jar) do (call addcp.bat %%f)

rem Jasen setup
rem for %%f in (plugins\filters\Jasen\*.jar) do (call addcp.bat %%f)
rem set CPATH=%CPATH%;plugins\filters\Jasen

rem inputIPFilters
for %%f in (plugins\inputIPFilters\*.jar) do (call addcp.bat %%f)


echo %CPATH%

java -Xmx512m -cp %CPATH% org.jsmtpd.Launcher