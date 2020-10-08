#!/bin/bash

EVALUATION_CONFIG=config.yml
EVALUATION_TARGET=all

all: spoofax evaluation

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

evaluation:
	cd scripts && \
	make \
		DIR=/jsglr2evaluation/data \
		SPOOFAX_DIR=/jsglr2evaluation/data/spoofax \
		CONFIG=$(EVALUATION_CONFIG) \
		$(EVALUATION_TARGET)