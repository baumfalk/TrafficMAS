#!/bin/bash

#if dir does not exist
if hash sumo 2>/dev/null; then
    echo "Sumo exists!"
    exit 0;
else
    echo "Sumo doesn't yet exist. Start build"
    apt-get update -qq 
    apt-get install -y libgdal1h libgdal-dev g++ libxerces-c3.1 libxerces-c-dev libicu-dev libproj-dev libfox-1.6-dev libgl1-mesa-dev libglu1-mesa-dev python 
    wget http://www.dlr.de/ts/en/Portaldata/16/Resources/sumo/sumo-src-0.22.0.zip
    unzip ./sumo-src-0.22.0.zip
    cd ./sumo-0.22.0
    ./configure --prefix=/home/user/opt/
    make
    make install
    cd ../
    export PATH=/home/user/opt/bin:$PATH
    exit 0;
fi
