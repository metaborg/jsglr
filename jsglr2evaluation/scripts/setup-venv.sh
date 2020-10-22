#!/usr/bin/env bash

set -eu

PWD="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
VENV_DIR="$PWD/.venv"

if [[ "${1-}" == "clean" || ! -d "$VENV_DIR" ]]
then
    rm -rf "$VENV_DIR"
    python3 -m venv "$VENV_DIR"
    "$VENV_DIR/bin/python3" -m pip install matplotlib pdftools
fi
