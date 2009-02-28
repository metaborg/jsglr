all : Stratego-Attributes-Sdf2.tbl

%.tbl : %.def
	sdf2table -i $< -o $@ -m `basename $< .def`

%.def : %.sdf
	pack-sdf -i $< -o $@ \
	         -Idef $(HOME)/.nix-profile/share/aster/MixStratego-Attributes.def \
	         -Idef $(HOME)/.nix-profile/share/aster/Stratego-Attributes.def \
	         -Idef $(HOME)/.nix-profile/share/sdf/sdf-front/Stratego-Sdf2.def
