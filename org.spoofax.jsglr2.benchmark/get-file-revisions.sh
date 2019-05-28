#!/usr/bin/env bash

set -e

shortname=$(basename "$1")
mkdir "$shortname"
i=0

for revision in $(git log --format=oneline -- "$1" | cut -d' ' -f1 | tac) ;
do
    git show ${revision}:"$1" > "$shortname/$i.in"
    ((i += 1))
done
