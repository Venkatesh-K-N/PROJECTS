#!/bin/bash

echo "Compiling Chess Game..."
mkdir -p bin
javac -d bin src/main/java/chess/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Run the game with: ./run.sh"
else
    echo "Compilation failed!"
    exit 1
fi
