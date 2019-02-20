@Echo off

@set git_install_root="E:\SmartGit\git"
@set lazarus_install_root="e:\lazarus"
@set PATH=%git_install_root%\bin;%git_install_root%\mingw\bin;%git_install_root%\cmd;%lazarus_install_root%;%PATH%
@set HOME=%USERPROFILE%

c:
cd C:\Users\application_jenkins\Documents\proyectos\monet\applications\msmanager\utils\generate-msm
git clean -f -d
git reset --hard
git pull
git checkout %1
git pull
lazbuild --build-all ..\..\msmanager.lpi
