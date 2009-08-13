ASTRS=$(wildcard *.astr) \
      $(wildcard */*.astr)

STRS=$(ASTRS:.astr=.str)

STRCFLAGS=\
  -I . \
  -I syntax \
  -la stratego-aterm \
  -la stratego-lib \
  -la stratego-xtc \
  -la stratego-sglr \
  -la stratego-gpp \
  -la ~/.nix-profile/lib/libstratego-sdf.la

make-permissive : make-permissive.str $(STRS)
	strc -i $< -m main-make-permissive $(STRCFLAGS)

java bin/make_permissive.java : make-permissive.str $(STRS)
	[ -e bin ] || mkdir bin
	strj -i $< -o $@ -m main-make-permissive -p org.strategoxt.permissivegrammars -la stratego-sdf $(STRCFLAGS)

jar bin/make_permissive.jar : bin/make_permissive.java
	strj-jar -i $<
	     
make-permissive.str : $(ASTRS) syntax/Stratego-Attributes-Sdf2.tbl syntax/Comments.pp.af
	aster --verbose 2 -i $(ASTRS) -I syntax

%.tbl : %.def
	sdf2table -i $< -o $@ -m `basename $< .def`

%.def : %.sdf
	pack-sdf -i $< -o $@ \
	         -Idef $(HOME)/.nix-profile/share/aster/MixStratego-Attributes.def \
	         -Idef $(HOME)/.nix-profile/share/aster/Stratego-Attributes.def \
	         -Idef $(HOME)/.nix-profile/share/sdf/sdf-front/Stratego-Sdf2.def

clean :
	rm -f $(STRS) syntax/*.tbl

%.pp.af : %.pp
	parse-pp-table -i $< -o $@
