DEFS=$(wildcard *.def)
INS=$(wildcard *.in) $(wildcard *.app)
OUTS=$(INS:.in=.out)
TBLS=$(DEFS:.def=.tbl)
CLASSES=$(wildcard ../bin/org/spoofax/jsglr/*.class)

WDINS=$(wildcard *.app) $(wildcard $(WD)/test/*/*.app)
WDOUTS=$(WDINS:.app=.aapp)

PARSE=java -Xss8m -Xverify:none -client -cp ../bin:`strj-jar -p` org.spoofax.jsglr.Main


all : $(TBLS)	

check : $(TBLS) $(OUTS) check-webdsl
	echo done

check-webdsl : $(WDOUTS)
	@if [ "$(WD)" = "" ]; then \
	  echo 'check-webdsl: This test requires a WD environment variable to be set.'; \
	  exit 1; \
	fi

clean :
	rm -f $(TBLS) $(OUTS) $(WDOUTS)

%.tbl : %.def
	sdf2table -i $< -o $@

%-1.out : %-1.in %.tbl $(CLASSES)
	@echo Parsing $<
	@sglri    -p $*.tbl -i $< > /dev/null  || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p $*.tbl -i $< -o $@ || (rm $@; exit 1)
	@if `cat $@ | grep 'amb(' >/dev/null`; then \
	   implode-asfix -i $@ | pp-aterm; exit 1; \
	 fi

%-2.out : %-1.in %.tbl $(CLASSES)
	@echo Parsing $<
	@sglri    -p $*.tbl -i $< > /dev/null  || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p $*.tbl -i $< -o $@ || (rm $@; exit 1)
	@if `cat $@ | grep 'amb(' >/dev/null`; then \
	   implode-asfix -i $@ | pp-aterm; exit 1; \
	 fi

%-3.out : %-1.in %.tbl $(CLASSES)
	@echo Parsing $<
	@sglri    -p $*.tbl -i $< > /dev/null  || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p $*.tbl -i $< -o $@ || (rm $@; exit 1)
	@if `cat $@ | grep 'amb(' >/dev/null`; then \
	   implode-asfix -i $@ | pp-aterm; exit 1; \
	 fi

%-4.out : %-1.in %.tbl $(CLASSES)
	@echo Parsing $<
	@sglri    -p $*.tbl -i $< > /dev/null  || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p $*.tbl -i $< -o $@ || (rm $@; exit 1)
	@if `cat $@ | grep 'amb(' >/dev/null`; then \
	   implode-asfix -i $@ | pp-aterm; exit 1; \
	 fi

%-5.out : %-1.in %.tbl $(CLASSES)
	@echo Parsing $<
	@sglri    -p $*.tbl -i $< > /dev/null || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p $*.tbl -i $< -o $@ || (rm $@; exit 1)
	@if `cat $@ | grep 'amb(' >/dev/null`; then \
	   implode-asfix -i $@ | pp-aterm; exit 1; \
	 fi

WebDSL.tbl : WebDSL.def
	sdf2table -i $< -o $@ -m WebDSL

WebDSL.def : $(WD)/src/org/webdsl/dsl/syntax/WebDSL.def
	cp $< $@

%.aapp : %.app WebDSL.tbl $(CLASSES)
	@echo Parsing $<
	@sglri    -p WebDSL.tbl -s Unit -i $< > /dev/null || (echo "ERROR: Standard sglr parser failed"; exit 1)
	@$(PARSE) -p WebDSL.tbl -s Unit -i $< -o $@ || (rm $@; exit 1)
	@if `cat $@ | grep 'amb(' >/dev/null`; then \
	   implode-asfix -i $@ | pp-aterm; exit 1; \
	 fi

