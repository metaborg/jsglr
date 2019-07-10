#!/usr/bin/env bash

# Takes a single argument: the path to a file.
# The output of the script is a directory containing all recorded versions of this file.
# The name of the directory is equal to the file name.

set -e

shortname=$(basename "$1")
mkdir "$shortname"
i=0

# Take all revisions in which this file can be found, use cut to get the commit hash, reverse order to get oldest first
for revision in $(git log --format=oneline -- "$1" | cut -d' ' -f1 | tac) ;
do
    # For every revision $i, show the file at this revision and save it in file $i.in
    git show ${revision}:"$1" > "$shortname/$i.in"
    ((i += 1))
done
