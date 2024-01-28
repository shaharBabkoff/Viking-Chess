import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLogic implements PlayableLogic {
    private ConcretePlayer _blue;
    private ConcretePlayer _red;
    private boolean _turn;//which player's turn
    private int _size = 11;//size of the board
    private ConcretePiece[][] _board = new ConcretePiece[this._size][this._size];//matrix of the pieces
    private ArrayList<ConcretePiece> _pieceList = new ArrayList<ConcretePiece>();//array of the pieces
    private ArrayList<Pawn> _pawnsList = new ArrayList<Pawn>();//array of the pawns
    private boolean _gameFinished;
    public Player _winnerOfGame;
    private ArrayList<Position> _posList = new ArrayList<Position>();//list of positions that pieces have been on during the game
    private ArrayList<ArrayList<PositionPiece>> _back = new ArrayList<ArrayList<PositionPiece>>();

    public GameLogic() {
        this._board = new ConcretePiece[_size][_size];
        this._blue = new ConcretePlayer(true, 0);//set the defender player
        this._red = new ConcretePlayer(false, 0);//set the attacker player
        this.reset();
    }

    public void reset() {
        this._board = new ConcretePiece[_size][_size];//set the size of the board
        this._turn = false;//set who start (_red start)
        _winnerOfGame = null; // at the start of the game no player is a winner
        //we now want to do 2 things: first thing is to place every piece in his position.
        //second thing is to add every piece to _pieceList with his name number. both things are done by using the helper function "createPawn"
        int pieceIndex = 0;
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; i <= 10; i++) {
                this._board[i][j] = null;
            }
        }
        for (int i = 3; i < 8; i++) {//up walls
            this.createRedPawn(this._red, i, 0, pieceIndex);//1-5 pawns
            pieceIndex++;
        }
        this.createRedPawn(this._red, 5, 1, pieceIndex);//pawn 6
        pieceIndex++;
        for (int i = 3; i < 8; i++) {//7-18 pawns
            this.createRedPawn(this._red, 0, i, pieceIndex);
            pieceIndex++;
            if (i == 5) {//12 and 13 pawns
                this.createRedPawn(this._red, 1, i, pieceIndex);
                pieceIndex++;
                this.createRedPawn(this._red, 9, i, pieceIndex);
                pieceIndex++;
            }
            this.createRedPawn(this._red, 10, i, pieceIndex);
            pieceIndex++;
        }
        this.createRedPawn(this._red, 5, 9, pieceIndex);// pawn 19
        pieceIndex++;
        for (int i = 3; i < 8; i++) {//20-24 pawns
            this.createRedPawn(this._red, i, 10, pieceIndex);
            pieceIndex++;
        }
        //blue pawns:
        int blueIndex = 1;
        this.createBluePawn(this._blue, 5, 3, blueIndex);//pawn 1
        blueIndex++;
        for (int i = 4; i < 7; i++) {//pawn 2-4
            this.createBluePawn(this._blue, i, 4, blueIndex);
            blueIndex++;
        }
        for (int i = 3; i < 8; i++) { //5-9 pawns
            if (i == 5) {
                this._board[5][5] = new King(_blue, new Position(5, 5));//create the king
                this._pieceList.add(blueIndex, this._board[5][5]);
                this._board[5][5].set_numberOfPiece(blueIndex);
                blueIndex++;
            } else {
                this.createBluePawn(this._blue, i, 5, blueIndex);
                blueIndex++;
            }
        }
        for (int i = 4; i < 7; i++) {// 10-12 pawns
            this.createBluePawn(this._blue, i, 6, blueIndex);
            blueIndex++;
        }
        this.createBluePawn(this._blue, 5, 7, blueIndex);//pawn 13

        for (int i = 0; i < _size; i++) {//go over all the board and add to _posList the positions that have piece on
            for (int j = 0; j < _size; j++) {
                if (this._board[i][j] != null) {
                    Position p = this._board[i][j].getPos();
                    p.addPieceToPos(this._board[i][j]);
                    _posList.add(p);
                }
            }
        }
    }

    private void createRedPawn(ConcretePlayer player, int x, int y, int pawnIndex) { //helper function that set a red piece in position and also set the number of the piece
        this._board[x][y] = new Pawn(player, new Position(x, y));
        this._pieceList.add(pawnIndex, this._board[x][y]);
        this._board[x][y].set_numberOfPiece(pawnIndex + 1);
    }

    private void createBluePawn(ConcretePlayer player, int x, int y, int pawnIndex) {//helper function that set a blue piece in position and also set the number of the piece
        this._board[x][y] = new Pawn(player, new Position(x, y));
        this._pieceList.add(pawnIndex + 23, this._board[x][y]);
        this._board[x][y].set_numberOfPiece(pawnIndex);
    }

    public boolean move(Position a, Position b) {
        ConcretePiece aPiece = this._board[a.getX()][a.getY()];//the piece in position a
        ConcretePiece bPiece = this._board[b.getX()][b.getY()];// the piece in position b
        if (bPiece != null || aPiece.getOwner().isPlayerOne() != _turn) {
            return false; // if there is a piece in b or it is not aPiece turn -> invalid move
        }
        if (!isValidMove(a, b)) {
            return false; //the function 'isValidMove' check if the move is valid
        }
        aPiece.addStep(b);//add the step to the step array of the piece
        updatePosList(b, aPiece);//update the list of the positions
        _back.add(new ArrayList<>());//create new arrayList in _back list
        PositionPiece positionPiece = new PositionPiece(a, b, this._board[a.getX()][a.getY()]);//a-old position. b- new position, and the piece is the piece im a position
        _back.getLast().add(positionPiece);//add positionPiece to the arrayList in the last cell



        this._board[b.getX()][b.getY()] = aPiece;//move aPiece to bPiece
        this._board[a.getX()][a.getY()] = null;//change aPiece to be null


        if (!"♔".equals(getPieceAtPosition(b).getType())) {//check if the piece is not king(pawn) because the king can't eat
            isKingNeighbor(b);//check if one of your neighbors is a king
            isCaptureMove(b);// in 'isCaptured' function, there are all the captures rules and update the number of captured of this pieces
        }
        aPiece.calculateDist(a, b);
        if (isGameFinished() == true) {//if the game is finished -> print all the statistics
            printStatistic();
        }
        _turn = !_turn;//if a piece moved -> change the turn to the next player
        return true;
    }

    //function that update _posList:if a piece step on a position, and no one have stepped there before
    // -> add the position to _posList and add the piece to the _pieceAtPos that every position has
    private void updatePosList(Position pos, ConcretePiece piece) {
        if (posIsInPosList(pos) != -1) {//means that the position is in _posList
            if (_posList.get(posIsInPosList(pos)).isPieceInPosList(piece) == false) {//means that the piece is not in the list of the position list
                _posList.get(posIsInPosList(pos)).addPieceToPos(piece);//add the piece to the list
            }
        } else {
            _posList.add(pos);//means that the position is not in _posList ->add the position to _posList
            _posList.getLast().addPieceToPos(piece);//add the piece to the position list
        }
    }

    private int posIsInPosList(Position pos) {//helper function that check if a position is in _posList
        for (int i = 0; i < _posList.size(); i++) {
            if (_posList.get(i).getX() == pos.getX() && _posList.get(i).getY() == pos.getY()) {
                return i;//return the index of the position in _posList
            }
        }
        return -1;
    }

    private boolean isValidMove(Position a, Position b) {
        if (a.getX() != b.getX() && a.getY() != b.getY()) {// check if b is diagonal
            return false;
        }
        if (a.getX() == b.getX()) {// Check for pieces in the path (for horizontal movement)
            int startCol = Math.min(a.getY(), b.getY()) + 1;
            int endCol = Math.max(a.getY(), b.getY());
            for (int col = startCol; col < endCol; col++) {
                if (this._board[a.getX()][col] != null) {
                    return false; // There is a piece in the path
                }
            }
        }
        if (a.getY() == b.getY()) {// Check for pieces in the path (for vertical movement)
            int startRow = Math.min(a.getX(), b.getX()) + 1;
            int endRow = Math.max(a.getX(), b.getX());
            for (int row = startRow; row < endRow; row++) {
                if (this._board[row][a.getY()] != null) {
                    return false; // There is a piece in the path
                }
            }
        }
        if (b.isCorner() && !"♔".equals(getPieceAtPosition(a).getType()))//allow only to the king move to the corner
        {
            return false;
        }
        if (b.isCorner() && "♔".equals(getPieceAtPosition(a).getType())) {
            this._gameFinished = true;//_blue win
            _blue.updateWins();
            _winnerOfGame = _blue;
        }
        return true;
    }

    public void isCaptureMove(Position b) {
        int numOfCaptured = 0;
        Player ownerB = getPieceOwnerAtPosition(b.getX(), b.getY());
        //check if there is a capture from right:
        if (b.getX() + 1 < _size) {//if there is a valid position from his right
            Piece rightPiece = this._board[b.getX() + 1][b.getY()];
            if (b.getX() + 2 < _size && b.getY() < _size) {//if there is valid position 2 position from his right
                Piece rightRightPiece = this._board[b.getX() + 2][b.getY()];
                if ((rightPiece != null) && (rightRightPiece != null)) {//check if there is a piece on the right and the rightRight position
                    if ((ownerB != rightPiece.getOwner()) && (ownerB == rightRightPiece.getOwner())) {
                        if (!"♔".equals(this._board[b.getX() + 1][b.getY()].getType())) {//check if the captured piece is not king//check the owners of both pieces
                            Position rightPos = new Position(b.getX()+1,b.getY());
                            PositionPiece positionPiece = new PositionPiece(rightPos, null, this._board[b.getX()+1][b.getY()]);//if the piece have captured->the new pos is null
                            _back.getLast().add(positionPiece);//add to the last cell of _back the new "move"
                            this._board[b.getX() + 1][b.getY()] = null;//there is a capture
                            numOfCaptured++;
                        }
                    }
                }
            } else if ((rightPiece != null) && (ownerB != rightPiece.getOwner())) {// b position is in the second square from the outside
                if (!"♔".equals(this._board[b.getX() + 1][b.getY()].getType())) {//check if the captured piece is not king
                    Position rightPos = new Position(b.getX()+1,b.getY());
                    PositionPiece positionPiece = new PositionPiece(rightPos, null, this._board[b.getX()+1][b.getY()]);//if the piece have captured->the new pos is null
                    _back.getLast().add(positionPiece);//add to the last cell of _back the new "move"
                    this._board[b.getX() + 1][b.getY()] = null;
                    numOfCaptured++;
                }
            }
        }
//check if there is a capture from left:
        if (b.getX() - 1 >= 0) {//if there is a valid position from his left
            Piece leftPiece = this._board[b.getX() - 1][b.getY()];
            if (b.getX() - 2 >= 0 && b.getY() < _size) {//if there is valid position 2 position from his left
                Piece leftLeftPiece = this._board[b.getX() - 2][b.getY()];
                if ((leftPiece != null) && (leftLeftPiece != null)) {//check if there is a piece on the left and the leftLeft position
                    if ((ownerB != leftPiece.getOwner()) && (ownerB == leftLeftPiece.getOwner())) {//check the owners of both pieces
                        if (!"♔".equals(this._board[b.getX() - 1][b.getY()].getType())) {//check if the captured piece is not king
                            Position leftPos = new Position(b.getX()-1,b.getY());
                            PositionPiece positionPiece = new PositionPiece(leftPos, null, this._board[b.getX()-1][b.getY()]);//if the piece have captured->the new pos is null
                            _back.getLast().add(positionPiece);//add to the last cell of _back the new "move"
                            this._board[b.getX() - 1][b.getY()] = null;//there is a capture
                            numOfCaptured++;
                        }
                    }
                }
            } else if ((leftPiece != null) && (ownerB != leftPiece.getOwner())) {// b position is in the second square from the outside
                if (!"♔".equals(this._board[b.getX() - 1][b.getY()].getType())) {//check if the captured piece is not king
                    Position leftPos = new Position(b.getX()-1,b.getY());
                    PositionPiece positionPiece = new PositionPiece(leftPos, null, this._board[b.getX()-1][b.getY()]);//if the piece have captured->the new pos is null
                    _back.getLast().add(positionPiece);//add to the last cell of _back the new "move"
                    this._board[b.getX() - 1][b.getY()] = null;
                    numOfCaptured++;
                }
            }
        }
//check if there is a capture from up:
        if (b.getY() - 1 >= 0) {//if there is a valid position from  up
            Piece upPiece = this._board[b.getX()][b.getY() - 1];
            if (b.getX() < _size && b.getY() - 2 >= 0) {//if there is valid position 2 position from up
                Piece upUpPiece = this._board[b.getX()][b.getY() - 2];
                if ((upPiece != null) && (upUpPiece != null)) {//check if there is a piece on the up and the upUp position
                    if ((ownerB != upPiece.getOwner()) && (ownerB == upUpPiece.getOwner())) {//check the owners of both pieces
                        if (!"♔".equals(this._board[b.getX()][b.getY() - 1].getType())) {//check if the captured piece is not king
                            Position upPos = new Position(b.getX(),b.getY()-1);
                            PositionPiece positionPiece = new PositionPiece(upPos, null, this._board[b.getX()][b.getY()-1]);//if the piece have captured->the new pos is null
                            _back.getLast().add(positionPiece);//add to the last cell of _back the new "move"
                            this._board[b.getX()][b.getY() - 1] = null;//there is a capture
                            numOfCaptured++;
                        }
                    }
                }
            } else if ((upPiece != null) && (ownerB != upPiece.getOwner())) {// b position is in the second square from the outside
                if (!"♔".equals(this._board[b.getX()][b.getY() - 1].getType())) {//check if the captured piece is not king
                    Position upPos = new Position(b.getX(),b.getY()-1);
                    PositionPiece positionPiece = new PositionPiece(upPos, null, this._board[b.getX()][b.getY()-1]);//if the piece have captured->the new pos is null
                    _back.getLast().add(positionPiece);//add to the last cell of _back the new "move"
                    this._board[b.getX()][b.getY() - 1] = null;
                    numOfCaptured++;
                }
            }
        }

//check if there is a capture from down:
        if (b.getY() + 1 < _size) {//if there is valid position from down
            Piece downPiece = this._board[b.getX()][b.getY() + 1];
            if (b.getX() < _size && b.getY() + 2 < _size) {//if there is valid position 2 position from down
                Piece downDownPiece = this._board[b.getX()][b.getY() + 2];
                if ((downPiece != null) && (downDownPiece != null)) {//check if there is a piece on the down and the downDown position
                    if ((ownerB != downPiece.getOwner()) && (ownerB == downDownPiece.getOwner())) {//check the owners of both pieces
                        if (!"♔".equals(this._board[b.getX()][b.getY() + 1].getType())) {//check if the captured piece is not king
                            Position downPos = new Position(b.getX(),b.getY()+1);
                            PositionPiece positionPiece = new PositionPiece(downPos, null, this._board[b.getX()][b.getY()+1]);//if the piece have captured->the new pos is null
                            _back.getLast().add(positionPiece);////add to the last cell of _back the new "move"
                            this._board[b.getX()][b.getY() + 1] = null;//there is a capture
                            numOfCaptured++;
                        }
                    }
                }
            } else if ((downPiece != null) && (ownerB != downPiece.getOwner())) {// b position is in the second square from the outside
                if (!"♔".equals(this._board[b.getX()][b.getY() + 1].getType())) {//check if the captured piece is not king
                    Position downPos = new Position(b.getX(),b.getY()+1);
                    PositionPiece positionPiece = new PositionPiece(downPos, null, this._board[b.getX()][b.getY()+1]);//if the piece have captured->the new pos is null
                    _back.getLast().add(positionPiece);//add to the last cell of _back the new "move"
                    this._board[b.getX()][b.getY() + 1] = null;
                    numOfCaptured++;
                }
            }
        }
        if (numOfCaptured > 0) {//if a capture have done ->
            Pawn p = (Pawn) getPieceAtPosition(b);//we know that the piece is a pawn
            if (isInPawnList(p) != -1) {//if the pawn is in the list(he killed before) ->add numOfCaptured to his number of captured counter
                int i = isInPawnList(p);
                _pawnsList.get(i).set_capturedPieces(numOfCaptured);
            } else {
                p.set_capturedPieces(numOfCaptured);//else(it his first time of eating) -> add the pawn to the _pawnList ad set his number of captured counter
                _pawnsList.add(p);
            }
        }
    }

    private int isInPawnList(Pawn piece) { //helper function that checks if a pawn is in _pawnList (in order to know if the piece capture before)
        for (int i = 0; i < _pawnsList.size(); i++) {
            if (_pawnsList.get(i) == piece) {
                return i;//return the index of the piece in _pawnList
            }
        }
        return -1;
    }

    //helper function that check if the position have a king neighbor.
    // if he does -> check if the king is captured in the "isKingCaptured"
    private void isKingNeighbor(Position b) {
        if (b.getX() + 1 < _size && this._board[b.getX() + 1][b.getY()] != null) {
            if ("♔".equals(this._board[b.getX() + 1][b.getY()].getType())) {//if right neighbor is king
                isKingCaptured(b.getX() + 1, b.getY());
            }
        }
        if (b.getX() - 1 >= 0 && _board[b.getX() - 1][b.getY()] != null) {
            if ("♔".equals(this._board[b.getX() - 1][b.getY()].getType())) {//if left neighbor is king
                isKingCaptured(b.getX() - 1, b.getY());
            }
        }
        if (b.getY() + 1 < _size && _board[b.getX()][b.getY() + 1] != null) {
            if ("♔".equals(this._board[b.getX()][b.getY() + 1].getType())) {//if down neighbor is king
                isKingCaptured(b.getX(), b.getY() + 1);
            }
        }
        if (b.getY() - 1 >= 0 && _board[b.getX()][b.getY() - 1] != null) {
            if ("♔".equals(this._board[b.getX()][b.getY() - 1].getType())) {//if up neighbor is king
                isKingCaptured(b.getX(), b.getY() - 1);
            }
        }
    }

    private boolean isKingCaptured(int x, int y) {  //helper function that check if the king is captured
        int counter = 0;//count the number of red neighbors of king
        if (x + 1 < _size && getPieceOwnerAtPosition(x + 1, y) == _red) {//right neighbor
            counter++;
        }
        if (x - 1 >= 0 && getPieceOwnerAtPosition(x - 1, y) == _red) {//left neighbor
            counter++;
        }
        if (y + 1 < _size && getPieceOwnerAtPosition(x, y + 1) == _red) {//down neighbor
            counter++;
        }
        if (y - 1 >= 0 && getPieceOwnerAtPosition(x, y - 1) == _red) {//up neighbor
            counter++;
        }
        if ((counter == 4) || (counter == 3 && isPosOnFrame(x, y))) {//if count ==3 -> check if the king on the frame
            this._board[x][y] = null;
            this._gameFinished = true;//_red win
            _red.updateWins();
            _winnerOfGame = _red;
            return true;
        }
        return false;
    }

    private boolean isPosOnFrame(int x, int y) {//helper function who check if a position is on frame
        if (x == 0 || x == 10 || y == 0 || y == 10) {
            return true;
        }
        return false;
    }

    private void printStatistic() {
        // List<ConcretePiece> pieceList = this._pieceList;
        Collections.sort(_pieceList, new CompareBySteps(_winnerOfGame));//sort _pieceList by "CompareBySteps" comparator
        printListOfSteps(_pieceList);//print the list of steps for every piece in _pieceList
        for (int i = 0; i < 75; i++) {//print 75 "*"
            System.out.print("*");
        }
        System.out.println("");
        // List<Pawn> pawnList = this._pawnsList;
        Collections.sort(_pawnsList, new compareByKills(_winnerOfGame));//sort _pieceList by "CompareBySteps" comparator
        printListOfKills(_pawnsList);//print the list of steps for every piece in _pieceList
        for (int i = 0; i < 75; i++) {//print 75 "*"
            System.out.print("*");
        }
        System.out.println("");
        Collections.sort(_pieceList, new CompareByDist(_winnerOfGame));//sort _pieceList by "CompareByDist" comparator
        printListOfDist(_pieceList);//print the list of steps for every piece in _pieceList
        for (int i = 0; i < 75; i++) {//print 75 "*"
            System.out.print("*");
        }
        System.out.println("");
        Collections.sort(_posList, new CompareByPieces());//sort _pieceList by "CompareByPosition" comparator
        printListOfPosition(_posList);
        for (int i = 0; i < 75; i++) {//print 75 "*"
            System.out.print("*");
        }
        System.out.println("");
    }

    private void printListOfPosition(List<Position> positionList) {//function that print the positions who have been stepped more than 1 time
        for (int i = 0; i < _posList.size(); i++) {
            if (_posList.get(i).getNumOfPiecesInPos() >= 2) {//we want to print positions only if 2 different pieces been on them
                Position p = _posList.get(i);
                System.out.println(p.posToString() + p.getNumOfPiecesInPos() + " pieces");
            }
        }
    }

    private void printListOfSteps(List<ConcretePiece> pieceList) {//function that print the steps of every piece
        for (int i = 0; i < pieceList.size(); i++) {
            if (pieceList.get(i)._steps.size() >= 2) {//we want to print the steps of only pieces that have moved
                System.out.print(ownerToString(pieceList.get(i)) + pieceList.get(i).get_numberOfPiece() + ": [");
                pieceList.get(i).printStepsOfPiece();//"printStepsOfPiece" is a method in "ConcretePiece" class that print the steps of a piece
                System.out.println("]");
            }
        }
    }

    private void printListOfKills(List<Pawn> pawnList) {//function that print for every pawn in the list the number of kills
        for (int i = 0; i < pawnList.size(); i++) {
            System.out.print(ownerToString(pawnList.get(i)) + pawnList.get(i).get_numberOfPiece() + ": ");
            System.out.println(pawnList.get(i).get_capturedPieces() + " kills");
        }
    }

    private void printListOfDist(List<ConcretePiece> pieceList) {
        for (int i = 0; i < pieceList.size(); i++) {
            if (pieceList.get(i)._dist > 0) {
                System.out.print(ownerToString(pieceList.get(i)) + pieceList.get(i).get_numberOfPiece() + ": ");
                System.out.println(pieceList.get(i)._dist + " squares");
            }
        }
    }

    public Piece getPieceAtPosition(Position pos) {
        return this._board[pos.getX()][pos.getY()];
    }

    private Player getPieceOwnerAtPosition(int x, int y) {// Helper method to get the owner of the piece at a specific position
        if (x >= 0 && x < _size && y >= 0 && y < _size && this._board[x][y] != null) {
            return this._board[x][y].getOwner();
        }
        return null;
    }

    public String ownerToString(ConcretePiece piece) {
        //helper function that give "D" if you _blue , "A" if you _red and "K" for the king
        if ("♔".equals(piece.getType())) {
            return "K";
        }
        if (piece.getOwner() == _red) {
            return "A";
        }
        return "D";
    }

    public void undoLastMove() {
        if (_back.size() > 0) {//means no move have done
            for (int i = 0; i < _back.getLast().size(); i++) {//going over the last move
                ConcretePiece p = _back.getLast().get(i).get_piece();//get the piece
                Position oldPos = _back.getLast().get(i).get_posOld();//get the old position of the piece
                Position newPos = _back.getLast().get(i).get_posNew();//get the new  position of the piece
                _board[oldPos.getX()][oldPos.getY()] = p;// bring the piece to be in the old position of the piece
                if (newPos!=null) {//if the new position is not null -> make it null
                    _board[newPos.getX()][newPos.getY()] = null;//
                }
            }
            _back.removeLast();//remove the move from the list
            _turn=!_turn;//change the turn
        }
    }


    public int getBoardSize() {
        return _size;
    }

    public Player getFirstPlayer() {
        return _blue;
    }

    public Player getSecondPlayer() {
        return _red;
    }

    public boolean isGameFinished() {
        return this._gameFinished;
    }

    public boolean isSecondPlayerTurn() {
        return !this._turn;
    }
}

