STRATEGOXT ?= $(HOME)/.nix-profile
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
  -la $(STRATEGOXT)/lib/libstratego-sdf.la \
  -O 3
  
all : make-permissive # jar

make-permissive : make-permissive.str $(STRS)
	strc -i $< -m main-make-permissive $(STRCFLAGS)

java bin/org/strategoxt/permissivegrammars/make_permissive.java : make-permissive.str $(STRS)
	[ -e bin/org/strategoxt/permissivegrammars ] || mkdir -p bin/org/strategoxt/permissivegrammars
	strj -clean -i $< -o bin/org/strategoxt/permissivegrammars/make_permissive.java -m main-make-permissive -p org.strategoxt.permissivegrammars -la stratego-sdf $(STRCFLAGS)

jar bin/make_permissive.jar : bin/org/strategoxt/permissivegrammars/make_permissive.java
	strj-jar -i $< -o bin/make_permissive.jar -cp bin
	     
make-permissive.str : $(ASTRS) syntax/Stratego-Attributes-Sdf2.tbl syntax/Comments.pp.af
	aster --verbose 2 -i $(ASTRS) -I syntax

%.tbl : %.def
	sdf2table -i $< -o $@ -m `basename $< .def`

%.def : %.sdf
	pack-sdf -i $< -o $@ \
	         -Idef $(ASTER)/share/aster/MixStratego-Attributes.def \
	         -Idef $(ASTER)/share/aster/Stratego-Attributes.def \
	         -Idef $(STRATEGOXT)/share/sdf/sdf-front/Stratego-Sdf2.def

clean :
	rm -f $(STRS) syntax/*.tbl

%.pp.af : %.pp
	parse-pp-table -i $< -o $@
