DEFS=$(wildcard *.def)
INS=$(wildcard *.in)
OUTS=$(INS:.in=.out)
TBLS=$(DEFS:.def=.tbl)

WDINS=$(wildcard $(WD)/test/*/*.app)
WDOUTS=$(WDINS:.app=.aapp)

PARSE=java -client -cp ../bin:`strj-jar -p` org.spoofax.jsglr.Main

all : $(TBLS)

check : $(TBLS) $(OUTS) check-webdsl

check-webdsl : $(WDOUTS)
	if [ "$(WD)" = "" ]; then \
	  echo 'check-webdsl: This test requires a $(WD) environment variable to be set.'; \
	  exit 1; \
	fi

clean :
	rm -f $(TBLS) $(OUTS) $(WDOUTS)

%.tbl : %.def
	sdf2table -i $< -o $@

%-1.out : %-1.in %.tbl
	@echo Parsing $<
	@sglri    -p $*.tbl -i $< > /dev/null  || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p $*.tbl -i $< -o $@ || (rm $@; exit 1)
	@cat $< | grep 'amb(' && exit 1 || exit 0

%-2.out : %-1.in %.tbl
	@echo Parsing $<
	@sglri    -p $*.tbl -i $< > /dev/null  || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p $*.tbl -i $< -o $@ || (rm $@; exit 1)
	@cat $< | grep 'amb(' && exit 1 || exit 0

%-3.out : %-1.in %.tbl
	@echo Parsing $<
	@sglri    -p $*.tbl -i $< > /dev/null  || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p $*.tbl -i $< -o $@ || (rm $@; exit 1)
	@cat $< | grep 'amb(' && exit 1 || exit 0

%-4.out : %-1.in %.tbl
	@echo Parsing $<
	@sglri    -p $*.tbl -i $< > /dev/null  || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p $*.tbl -i $< -o $@ || (rm $@; exit 1)
	@cat $< | grep 'amb(' && exit 1 || exit 0

%-5.out : %-1.in %.tbl
	@echo Parsing $<
	@sglri    -p $*.tbl -i $< > /dev/null || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p $*.tbl -i $< -o $@ || (rm $@; exit 1)
	@cat $< | grep 'amb(' && exit 1 || exit 0

WebDSL.tbl : $(WD)/src/org/webdsl/dsl/syntax/WebDSL.def
	sdf2table -i $< -o $@ -m WebDSL

%.aapp : %.app WebDSL.tbl
	@echo Parsing $<
	@sglri    -p WebDSL.tbl -s Unit -i $< > /dev/null || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p WebDSL.tbl -s Unit -i $< -o $@ || (rm $@; exit 1)
	@cat $< | grep 'amb(' && exit 1 || exit 0

