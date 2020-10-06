#!/bin/bash

all: spoofax

spoofax: pull-spoofax-releng build-spoofax

pull-spoofax-releng:
	if [ ! -d data/spoofax/.git ]; then git clone --recursive https://github.com/metaborg/spoofax-releng.git data/spoofax; fi;
	cd data/spoofax && \
		git checkout master && \
		git pull --rebase && \
		git submodule update --init --remote --recursive && \
		./b checkout -y && \
		./b update
	# TODO: checkout specific commit

build-spoofax:
	cd /jsglr2evaluation/data/spoofax && ./b build java
