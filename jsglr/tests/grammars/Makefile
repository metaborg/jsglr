all : Booleans.tbl

Booleans.tbl : Booleans.def
	sdf2table -i Booleans.def -o Booleans.tbl -m Booleans

Booleans.def : basic/*.sdf
	pack-sdf -I . -i basic/Booleans.sdf -o Booleans.def


clean:
	rm -f *.def *.tbl
