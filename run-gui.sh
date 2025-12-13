#!/bin/bash

if [ ! -d "bin" ]; then
    echo "Please compile first using: javac -d bin src/main/java/chess/*.java"
    exit 1
fi

echo "Starting Chess Game GUI..."
java -cp bin chess.ChessGUI
