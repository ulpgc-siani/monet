#!/bin/bash
cd $HOME/proyectos/monet/applications/msmanager
git clean -f -d
git reset --hard
git pull
git checkout $1
git pull
lazbuild --build-all msmanager.lpi
mkdir -p $HOME/proyectos/monet/applications/modeleditor/org.monet.editor/resources/msmanager/linux/64
cp -f $HOME/proyectos/monet/applications/msmanager/build/msmanager $HOME/proyectos/monet/applications/modeleditor/org.monet.editor/resources/msmanager/linux/64/msmanager
