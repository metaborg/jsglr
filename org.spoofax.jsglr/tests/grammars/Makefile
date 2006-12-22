GRAMMARS = \
	Booleans.tbl \
	$(patsubst basic/%.sdf,%.tbl,$(wildcard basic/*.sdf))

all : $(GRAMMARS)

%.tbl : %.def
	@echo "Building table $@"
	echo sdf2table -i $< -o $@ -t -m basic/$(basename $<)
	sdf2table -i $< -o $@ -t -m basic/$(basename $<)

%.def : basic/%.sdf Makefile
	@echo "Packing $< to $@"
	pack-sdf -I . -i $< -o $@


clean:
	rm -f *.def *.tbl
