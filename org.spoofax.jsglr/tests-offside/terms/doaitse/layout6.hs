-- layout can extend over multiple lines
test = let x = 1;  
           y = x
           f x y = z; z = f x
            y in y
