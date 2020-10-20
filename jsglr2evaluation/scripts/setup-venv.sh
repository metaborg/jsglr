#!/usr/bin/env bash

set -eu

PWD="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
VENV_DIR="$PWD/.venv"

if [[ "${1-}" == "clean" || ! -d "$VENV_DIR" ]]
then
    rm -rf "$VENV_DIR"
    # Use --without-pip and manually install Pip via curl, because else it's broken on Ubuntu/Debian:
    # https://bugs.launchpad.net/ubuntu/+source/python3.4/+bug/1290847/+index?comments=all (comment #58)
    python3 -m venv --without-pip "$VENV_DIR"
    source "$VENV_DIR/bin/activate"
    curl https://bootstrap.pypa.io/get-pip.py | python
    pip install matplotlib pdftools
    deactivate
fi
