CHESS GAME - Java OOP Implementation
=====================================

This is a fully functional chess game built with Java using Object-Oriented Programming principles.

OBJECT-ORIENTED CONCEPTS USED:
-------------------------------
1. Inheritance: Abstract Piece class with concrete piece implementations (King, Queen, Rook, Bishop, Knight, Pawn)
2. Polymorphism: Each piece overrides getPossibleMoves() with their unique movement logic
3. Encapsulation: Private fields with public getters/setters
4. Abstraction: Abstract methods in Piece class
5. Composition: Board contains Pieces, Game contains Board
6. Enumerations: Color, PieceType, GameStatus

PROJECT STRUCTURE:
------------------
chess/
├── Position.java       - Represents board position (row, col)
├── Color.java          - Enum for WHITE/BLACK
├── Piece.java          - Abstract base class for all pieces
├── King.java           - King piece implementation
├── Queen.java          - Queen piece implementation
├── Rook.java           - Rook piece implementation
├── Bishop.java         - Bishop piece implementation
├── Knight.java         - Knight piece implementation
├── Pawn.java           - Pawn piece implementation
├── Board.java          - Chess board management
├── Move.java           - Represents a chess move
├── Game.java           - Game logic (turn management, validation, check/checkmate)
├── GameStatus.java     - Enum for game status
├── ChessGame.java      - Command-line interface
├── ChessGUI.java       - Main GUI frame using Java Swing
├── BoardPanel.java     - Chess board visual component with mouse interaction
└── GameInfoPanel.java  - Right panel with player info and move history

KEY FEATURES:
-------------
✓ All standard chess piece movements
✓ Move validation
✓ Check detection
✓ Checkmate detection
✓ Stalemate detection
✓ Move history
✓ Turn-based gameplay
✓ Unicode chess piece symbols

HOW TO RUN:
-----------

COMMAND LINE VERSION:

### Ensure your terminal supports Unicode
In Windows CMD/PowerShell run:
 chcp 65001
This enables UTF-8 so chess pieces display correctly.

1. Compile the game:
   javac -d bin src/main/java/chess/*.java

2. Run the command-line game:
   java -cp bin chess.ChessGame

GUI VERSION (Java Swing):
1. Compile the game:
   javac -d bin src/main/java/chess/*.java

2. Run the GUI:
   java -cp bin chess.gui.ChessGUI

HOW TO PLAY (Command Line):
---------------------------
- Enter moves in format: e2 e4 (from position, to position)
- Columns are labeled a-h
- Rows are labeled 1-8
- White always starts first
- Type 'quit' to exit

HOW TO PLAY (GUI):
-----------------
- Click on a piece to select it (shows green highlight)
- Valid moves appear as small green dots
- Click on a valid move destination to move the piece
- Click on the selected piece again to deselect
- Game info panel on the right shows:
  * Current player (White/Black)
  * Game status
  * Complete move history
- New Game button restarts the game

EXAMPLE MOVES:
--------------
e2 e4    - Move pawn from e2 to e4
g1 f3    - Move knight from g1 to f3
b8 c6    - Move knight from b8 to c6

BOARD DISPLAY (Command Line):
-----------------------------
♔ ♕ ♖ ♗ ♘ ♙ = White pieces
♚ ♛ ♜ ♝ ♞ ♟ = Black pieces
·         = Empty square

GUI BOARD:
----------
Light squares: Tan color (#F0D9B5)
Dark squares: Brown color (#B58863)
Selected piece: Green highlight
Valid moves: Small green circles

GAME FEATURES:
--------------
✓ Complete chess rules enforcement
✓ Check detection and highlighting
✓ Checkmate detection with winner announcement
✓ Stalemate detection (draw)
✓ Move validation and piece movement rules
✓ Turn-based gameplay with player indication
✓ Move history tracking
✓ GUI with intuitive click-to-move interface





