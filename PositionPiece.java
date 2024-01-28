public class PositionPiece {
    //class that we creat for 'back' method. in this class the object has 3 fields: the old and new position of the piece and the piece itself.
    private Position _posNew;
    private Position _posOld;
    private  ConcretePiece _piece;
    public PositionPiece(Position posOld,Position posNew ,ConcretePiece piece){
        this._piece=piece;
        this._posNew=posNew;
        this._posOld=posOld;

    }

    public Position get_posNew() {
        return _posNew;
    }

    public Position get_posOld() {
        return _posOld;
    }

    public ConcretePiece get_piece() {
        return _piece;
    }
}
