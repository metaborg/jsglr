GRAMMARS =							\
	Booleans.tbl						\
	Stratego.tbl						\
	StrategoStratego.tbl						\
	Java-15.tbl						\
	WebDSL.tbl						\
	SmallAmbLang.tbl                \
	SpoofaxConfiguration.tbl                \
	$(patsubst basic/%.sdf,%.tbl,$(wildcard basic/*.sdf))

#	Java-SQL.tbl						

all : $(GRAMMARS)

%.tbl : gen-%.def
	@echo "Building table $@"
	sdf2table -i $< -o $@ -t -m basic/$(echo $(basename $<) | sed "s/gen-//")

%.tbl : %.def
	@echo "Building table $@"
	echo sdf2table -i $< -o $@ -t -m basic/$(basename $<)
	sdf2table -i $< -o $@ -t -m basic/$(basename $<)

StrategoStratego.tbl : StrategoStratego.def
	sdf2table -i $< -o $@ -m StrategoStratego

SpoofaxConfiguration.tbl : SpoofaxConfiguration.def
	sdf2table -i $< -o $@ -m SpoofaxConfiguration

%.def : basic/%.sdf Makefile
	@echo "Packing $< to $@"
	pack-sdf -I . -i $< -o $@

#	(echo "definition";	\
#	 for i in basic/*.sdf; do	\
#	   cat $$i;		\
#	   echo;		\
#	 done) >$@

%.def: %.def.in
	ln -sf $< $@

#Java-SQL.def: Java-SQL.def.in
#	ln -sf $< $@

Stratego.tbl:
	curl http://lennart.cl/misc/$@ | baffle -wt -o $@

Java-15.tbl:
	curl http://lennart.cl/misc/$@ | baffle -wt -o $@

WebDSL.tbl:
	curl http://lennart.cl/misc/$@ | baffle -wt -o $@

clean:
	rm -f *.def *.tbl
