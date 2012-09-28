JSQL_FILES = $(wildcard data/jsql*.txt)
AMB_FILES = $(wildcard data/amb*.txt)
SPOO_FILES = $(wildcard data/*.config)
B_FILES = $(wildcard data/b*.txt)
STR_FILES = $(wildcard data/*.str)
STRSTR_FILES = $(wildcard data/*.strstr)
JAVA_FILES = $(wildcard data/*.java)
WEBDSL_FILES = $(wildcard data/*.app)
TRM_FILES= \
	$(patsubst %txt,%trm, $(JSQL_FILES)) \
	$(patsubst %txt,%trm, $(AMB_FILES)) \
	$(patsubst %config,%trm, $(SPOO_FILES)) \
	$(patsubst %txt,%trm, $(B_FILES)) \
	$(patsubst %str,%trm, $(STR_FILES)) \
	$(patsubst %strstr,%trm, $(STRSTR_FILES)) \
	$(patsubst %java,%trm, $(JAVA_FILES)) \
	$(patsubst %app,%trm, $(WEBDSL_FILES)) \
	data/g-ambi-1.trm \
	data/g-ambi-2.trm \
	data/g-avoid-1_1.trm \
	data/g-avoid-2_1.trm \
	data/g-left-assoc-1.trm \
	data/g-prefer-1_1.trm \
	data/g-prefer-2_1.trm \
	data/g-prio-1_1.trm \
	data/g-prio-2_1.trm \
	data/g-reject-1_1.trm \
	data/g-reject-2_1.trm \
	data/g-right-assoc-1.trm \
	data/g1_1.trm \
	data/g2_1.trm

ITRM_FILES = $(subst .trm,.itrm,$(TRM_FILES))
SGLR_TRACES = $(patsubst data/%.str,sglr-trace-%.txt,$(STR_FILES))
SGLR_TRACES = $(patsubst data/%.str,sglr-trace-%.txt,$(STR_FILES))
JSGLR_TRACES = $(patsubst data/%.str,jsglr-trace-%.txt,$(STR_FILES))
TESTS = $(patsubst data/%.str,trace-diff-%.txt,$(STR_FILES))

SGLR=$(HOME)/.nix-profile/bin/sglr
IMPLODE=$(HOME)/.nix-profile/bin/implode-asfix
SGLROPTS=-2 -t #-fd -fe -fi -fp -fr -t -lvd
#../../../sglr/sglr-2.1M1/sglr/sglr/sglr

all: grammars $(TRM_FILES) $(ITRM_FILES)
	cd grammars && make 
	echo .$(TRM_FILES).

grammars: .PHONY
	$(MAKE) -C grammars

.PHONY:

_all: $(TESTS) $(SGLR_TRACES) $(JSGLR_TRACES)

%.itrm : %.trm
	$(IMPLODE) -i $< -o $@

data/g-ambi-1.trm : data/g-ambi-1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-ambi.tbl -i $< -o $@

data/g-ambi-2.trm : data/g-ambi-2.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-ambi.tbl -i $< -o $@

data/g-avoid-1_1.trm : data/g-avoid-1_1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-avoid-1.tbl -i $< -o $@

data/g-avoid-2_1.trm : data/g-avoid-2_1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-avoid-2.tbl -i $< -o $@

data/g-left-assoc-1.trm : data/g-left-assoc-1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-left-assoc.tbl -i $< -o $@

data/g-right-assoc-1.trm : data/g-right-assoc-1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-right-assoc.tbl -i $< -o $@

data/g-prefer-1_1.trm : data/g-prefer-1_1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-prefer-1.tbl -i $< -o $@

data/g-prefer-2_1.trm : data/g-prefer-2_1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-prefer-2.tbl -i $< -o $@

data/g-prio-1_1.trm : data/g-prio-1_1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-prio-1.tbl -i $< -o $@

data/g-prio-2_1.trm : data/g-prio-2_1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-prio-2.tbl -i $< -o $@

data/g-reject-1_1.trm : data/g-reject-1_1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-reject-1.tbl -i $< -o $@

data/g-reject-2_1.trm : data/g-reject-2_1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G-reject-2.tbl -i $< -o $@

data/g1_1.trm : data/g1_1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G1.tbl -i $< -o $@

data/g2_1.trm : data/g2_1.txt
	$(SGLR) $(SGLROPTS) -p grammars/G2.tbl -i $< -o $@

data/jsql%.trm : data/jsql%.txt
	$(SGLR) $(SGLROPTS) -p grammars/Java-SQL.tbl -i $< -o $@

data/amb%.trm : data/amb%.txt
	$(SGLR) $(SGLROPTS) -p grammars/SmallAmbLang.tbl -i $< -o $@

data/%.trm : data/%.config
	$(SGLR) $(SGLROPTS) -p grammars/SpoofaxConfiguration.tbl -i $< -o $@

data/b%.trm : data/b%.txt
	$(SGLR) $(SGLROPTS) -p grammars/Booleans.tbl -i $< -o $@

data/%.trm : data/%.java
	$(SGLR) $(SGLROPTS) -p grammars/Java-15.tbl -i $< -o $@

data/%.trm : data/%.str
	$(SGLR) $(SGLROPTS) -p grammars/Stratego.tbl -i $< -o $@

data/%.trm : data/%.strstr
	$(SGLR) $(SGLROPTS) -p grammars/StrategoStratego.tbl -i $< -o $@

data/%.trm : data/%.app
	$(SGLR) $(SGLROPTS) -p grammars/WebDSL.tbl -i $< -o $@

sglr-only: $(SGLR_TRACES)

sglr-trace-%.txt : data/%.str grammars/Stratego.tbl
	$(SGLR) $(SGLROPTS) -p grammars/Stratego.tbl -i $< -o sglr.trm
	cat .sglr-log | egrep "^(Goto|Reducing|\#|Current|nl|Amb)" > $@

jsglr-trace-%.txt : data/%.str grammars/Stratego.tbl
	../jsglr -v -p grammars/Stratego.tbl -i $< -o jsglr.trm
	cat .jsglr-log | egrep "^(Goto|Reducing|\#|Current|nl|Amb)" > $@

trace-diff-%.txt : sglr-trace-%.txt jsglr-trace-%.txt
	-diff $^ > $@

grammars/Stratego.tbl:
	cp $(HOME)/.nix-profile/share/sdf/stratego-front/Stratego.tbl grammars/Stratego.tbl

grammars/StrategoStratego.tbl:
	cp $(HOME)/.nix-profile/share/sdf/stratego-front/StrategoStratego.tbl grammars/StrategoStratego.tbl

clean:
	rm -f sglr-trace-* jsglr-trace-* trace-diff-*

fullclean : clean
	rm -f sglr*trace* jsglr*trace*
