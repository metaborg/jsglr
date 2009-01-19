ASTRS=$(wildcard *.astr) \
      $(wildcard */*.astr)

STRS=$(ASTRS:.astr=.str)

make-permissive : make-permissive.str $(STRS)
	strc -i $< -m main-make-permissive \
	     -I . \
	     -la stratego-aterm \
	     -la stratego-lib \
	     -la stratego-xtc \
	     -la stratego-sglr \
	     -la ~/.nix-profile/lib/libstratego-sdf.la
	     
make-permissive.str : $(ASTRS)
	aster --verbose 2 -i $(ASTRS)

clean :
	rm -f $(STRS)
