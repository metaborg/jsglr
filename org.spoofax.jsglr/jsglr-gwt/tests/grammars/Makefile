GRAMMARS =							\
	Booleans.tbl						\
	Stratego.tbl						\
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

Stratego.tbl:
	cp $(HOME)/.nix-profile/share/sdf/stratego-front/Stratego.tbl Stratego.tbl
clean:
	rm -f *.def *.tbl
