#!/bin/bash

if [ ! -d "bin" ]; then
    echo "Please compile first using: ./compile.sh"
    exit 1
fi

echo "Starting Chess Game..."
java -cp bin chess.ChessGame
