# Viking Chess 

## 📜 About the Game
Viking Chess, is an ancient Nordic strategy game played on an 11x11 board. The game features two asymmetric sides: the **defenders** and the **attackers**. The **defender's goal** is to help the King escape to one of the corner squares, while the **attackers aim to capture the King** before he escapes.

## 🎮 Game Rules
- The **King** starts in the center of the board, surrounded by his defending pieces.
- The **attackers** are placed at the middle of each board edge in a specific formation.
- All pieces move **like the rook in chess** – in straight lines over empty squares.
- **Capturing** occurs when a piece is trapped between two enemy pieces.
- The **King is captured** when surrounded on **all four sides** (or three if at the board's edge).
- **Only the King can move to the corner squares**, and doing so wins the game.

## 🔧 Project Structure
This project follows **Object-Oriented Programming (OOP) principles** and includes several core classes:

### **Main Components**
- `Main.java` – The main entry point for the game.
- `GUI_for_chess_like_games.java` – Handles graphical user interface.
- `PlayableLogic.java` – Interface defining the logic for playable games.
- `Player.java` – Represents a player in the game.
- `Piece.java` – Represents individual game pieces.

### **OOP Design**
- `ConcretePiece` – Implements `Piece`, serving as a base class for different piece types.
- `Pawn` – Extends `ConcretePiece`, representing a basic piece.
- `King` – Extends `ConcretePiece`, representing the King.
- `Position` – Represents a piece's position on the board.
- `ConcretePlayer` – Implements `Player`, representing a game player.
- `GameLogic` – Implements `PlayableLogic`, managing game rules and logic.

## 🛠 Features
- **GUI-based gameplay** 🎨
- **Undo last move** ↩️
- **Reset game** 🔄
- **Turn-based logic** 🔁
- **Game validation rules** ✅


