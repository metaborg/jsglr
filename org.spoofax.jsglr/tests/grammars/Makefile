GRAMMARS =							\
	Booleans.tbl						\
#	Java-SQL.tbl						\
	Stratego.tbl						\
	Java-15.tbl						\
	WebDSL.tbl						\
	$(patsubst basic/%.sdf,%.tbl,$(wildcard basic/*.sdf))

all : $(GRAMMARS)

%.tbl : gen-%.def
	@echo "Building table $@"
	echo sdf2table -i $< -o $@ -t -m basic/$(basename $<)
	sdf2table -i $< -o $@ -t -m basic/$(echo $(basename $<) | sed "s/gen-//")

%.tbl : %.def
	@echo "Building table $@"
	echo sdf2table -i $< -o $@ -t -m basic/$(basename $<)
	sdf2table -i $< -o $@ -t -m basic/$(basename $<)

%.def : basic/%.sdf Makefile
	@echo "Packing $< to $@"
	pack-sdf -I . -i $< -o $@

#	(echo "definition";	\
#	 for i in basic/*.sdf; do	\
#	   cat $$i;		\
#	   echo;		\
#	 done) >$@

Stratego.def: Stratego.def.in
	ln -sf $< $@

#Java-SQL.def: Java-SQL.def.in
#	ln -sf $< $@

Stratego.tbl:
	curl http://boblycat.org/~karltk/stuff/$@ | baffle -wt -o $@

Java-15.tbl:
	curl http://boblycat.org/~karltk/stuff/$@ | baffle -wt -o $@

WebDSL.tbl:
	curl http://boblycat.org/~karltk/stuff/$@ | baffle -wt -o $@

clean:
	rm -f *.def *.tbl
