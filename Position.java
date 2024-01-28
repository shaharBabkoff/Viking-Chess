import java.util.ArrayList;

public class Position {
    private int x;
    private int y;
    private ArrayList<ConcretePiece> _pieceAtPos;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this._pieceAtPos = new ArrayList<>();
    }

    public void addPieceToPos(ConcretePiece p) {
        _pieceAtPos.add(p);
    }

public boolean isPieceInPosList(ConcretePiece piece){
    for (int i = 0; i <_pieceAtPos.size() ; i++) {
        if (_pieceAtPos.get(i)==piece){
            return true;
        }
    }
    return false;
}
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setPosition(Position pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    public boolean isCorner() {
        if ((this.x == 0 && this.y == 0) || (this.x == 10 && this.y == 10)
                || (this.x == 10 && this.y == 0) || (this.x == 0 && this.y == 10)) {
            return true;

        }
        return false;
    }
    public int getNumOfPiecesInPos(){
        return _pieceAtPos.size();
    }

    public String posToString() {
        return "(" + x + ","+" " + y + ")";
    }


}
