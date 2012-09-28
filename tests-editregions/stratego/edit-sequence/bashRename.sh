#bash
counter=1;
for f in *str.scn; do 
	echo $f
	echo edit_$counter.str.scn; 
	mv $f edit_$counter.str.scn; 
	counter=`expr $counter + 1` 
done

