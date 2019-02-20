cd ~/proyectos/monet/applications/msmanager
/usr/bin/git clean -f -d
/usr/bin/git reset --hard
/usr/bin/git pull
/usr/bin/git checkout $1
/usr/bin/git pull
/usr/local/bin/lazbuild --build-all msmanager.lpi

#rm -f ~/proyectos/monet/applications/msmanager/build/msmanager.app/Contents/MacOS/msmanager
#mv -f ~/proyectos/monet/applications/msmanager/build/msmanager ~/proyectos/monet/applications/msmanager/build/msmanager.app/Contents/MacOS
