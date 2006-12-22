
STR_FILES = $(wildcard data/s*.str)
SGLR_TRACES = $(patsubst data/%.str,sglr-trace-%.txt,$(STR_FILES))
JSGLR_TRACES = $(patsubst data/%.str,jsglr-trace-%.txt,$(STR_FILES))
TESTS = $(patsubst data/%.str,trace-diff-%.txt,$(STR_FILES))

SGLR=../../../sglr/sglr-2.1M1/sglr/sglr/sglr

all: $(TESTS) $(SGLR_TRACES) $(JSGLR_TRACES)

sglr-only: $(SGLR_TRACES)

sglr-trace-%.txt : data/%.str
	$(SGLR) -2 -fa -fd -fe -fi -fp -fr -t -lvd -p grammars/Stratego.tbl -i $<
	cat .sglr-log | egrep "^(Goto|Reducing|\#|Current|nl|Amb)" > $@

jsglr-trace-%.txt : data/%.str
	../jsglr -v -p grammars/Stratego.tbl -i $<
	cat .jsglr-log | egrep "^(Goto|Reducing|\#|Current|nl|Amb)" > $@

trace-diff-%.txt : sglr-trace-%.txt jsglr-trace-%.txt
	-diff $^ > $@

clean:
	rm -f sglr-trace-* jsglr-trace-* trace-diff-*

fullclean : clean
	rm -f sglr*trace* jsglr*trace*
