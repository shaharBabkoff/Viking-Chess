public class Pawn extends ConcretePiece {
    private String _empty = "♙";//there are 2 kinds of pawns: one is filled and one is empty
    private String _full = "♟";//empty is player 1 and full is player 2
    private int _capturedPieces;

    public Pawn(Player player,  Position pos) {//constructor of the pawn (from ConcretePiece class)
        super(player,  pos);
    }

    @Override
    public int get_numberOfPiece() {
        return super.get_numberOfPiece();
    }

    @Override
    public Player getOwner() {
        return super.getOwner();
    }

    @Override
    public String getType() {
        if (getOwner().isPlayerOne() == true) {//true>for player 1, false> for player 2
            return _empty;
        }
        return _full;
    }

    public int get_capturedPieces() {
        return _capturedPieces;
    }

    public void set_capturedPieces(int number) {
        this._capturedPieces =this._capturedPieces+number;
    }
}
