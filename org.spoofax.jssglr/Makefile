worker_exists := $(wildcard ./war/jssglrWorker/*.cache.js)

all: tbltojs jssglr-ace

jssglr-ace:
ifeq ($(strip $(worker_exists)),) 
	echo "Compile the project first using GWT. (make sure a *.cache.js is generated)"
else
	rm -rf bin
	mkdir -p bin
	echo Making the GWT compiled file ready for use in ACE...
	cp ./war/jssglrWorker/*.cache.js ./bin/tmp.js
	cat \
	./src/ace/requirejsprepend.js \
	./bin/tmp.js \
	./src/ace/requirejsappend.js > ./bin/jssglr.js
	rm ./bin/tmp.js
endif

	


tbltojs:
	rm tbltojs* || strc \
	-m main-to-js \
	-i ./trans/to-js.str \
	-o tbltojs \
	-la stratego-lib \
	-la stratego-xtc \
	-la stratego-sglr

clean:
	rm -f tbltojs*
	rm -rf bin
