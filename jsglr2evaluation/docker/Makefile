#!/bin/bash

all: spoofax

spoofax: pull-spoofax-releng build-spoofax

pull-spoofax-releng:
	if [ ! -d /jsglr2evaluation/data/spoofax/.git ]; then git clone --recursive https://github.com/metaborg/spoofax-releng.git /jsglr2evaluation/data/spoofax; fi;
	# TODO: checkout specific commit

build-spoofax:
	cd /jsglr2evaluation/data/spoofax && ./b build java
