# Create simple parse tables

From within a Spoofax language project, you need the file `src-gen/syntax/<filename>/def`.

Convert it to a parse table using the following command where you substitute the path and the module name:

```
sdf2table -i /path/to/your/project/src-gen/syntax/<filename>.def -o /path/to/your/project/src-gen/syntax/<filename>.aterm -m <module> -t
```

Pretty print with the following command:

```
pp-aterm -i /path/to/your/project/src-gen/syntax/<filename>.aterm -o /path/to/your/project/src-gen/syntax/<filename>_pp.aterm
```