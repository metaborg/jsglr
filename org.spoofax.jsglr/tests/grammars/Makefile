GRAMMARS =							\
	Booleans.tbl						\
	Stratego.tbl						\
	$(patsubst basic/%.sdf,%.tbl,$(wildcard basic/*.sdf))

all : $(GRAMMARS)

%.tbl : gen-%.def
	@echo "Building table $@"
	echo sdf2table -i $< -o $@ -t -m basic/$(basename $<)
	sdf2table -i $< -o $@ -t -m basic/$(basename $<)

%.tbl : %.def
	@echo "Building table $@"
	echo sdf2table -i $< -o $@ -t -m $(basename $<)
	sdf2table -i $< -o $@ -t -m $(basename $<)

gen-%.def : basic/%.sdf Makefile
	@echo "Packing $< to $@"
	(echo "definition";	\
	 for i in basic/*.sdf; do	\
	   cat $$i;		\
	   echo;		\
	 done) >$@

clean:
	rm -f *.def *.tbl
