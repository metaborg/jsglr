all: spoofax build-jsglr2 evaluation

spoofax: pull-spoofax-releng build-spoofax

pull-spoofax-releng:
	if [ ! -d data/spoofax/.git ]; then git clone --recursive https://github.com/metaborg/spoofax-releng.git data/spoofax; fi;
	cd data/spoofax && \
		git checkout $(SPOOFAX_VERSION) && \
		git pull --rebase && \
		git submodule update --init --remote --recursive && \
		./b checkout -y && \
		./b update

build-spoofax:
	cd /jsglr2evaluation/data/spoofax && ./b build java

build-jsglr2:
	cd /jsglr2evaluation/data/spoofax/jsglr && git checkout $(JSGLR_VERSION)
	cd /jsglr2evaluation/data/spoofax/jsglr/org.spoofax.jsglr2 && mvn install

evaluation:
	cd scripts && \
	make \
		DIR=/jsglr2evaluation/data \
		SPOOFAX_DIR=/jsglr2evaluation/data/spoofax \
		$(EVALUATION_TARGET)
