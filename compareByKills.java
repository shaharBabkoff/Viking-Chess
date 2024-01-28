import java.util.Comparator;

public class compareByKills implements Comparator<Pawn> {
    //compare 2 pieces by their number of kills:
    // 1. by number of kills from low to high.
    // 2. by the number name of the piece
    // 3. by the winner of the game
    private Player _winner;

    public compareByKills(Player winner) {
        this._winner = winner;
    }

    @Override
    public int compare(Pawn o1, Pawn o2) {

        if (o1.get_capturedPieces() < o2.get_capturedPieces()) {
            return 1;
        }
        if (o1.get_capturedPieces() > o2.get_capturedPieces()) {
            return -1;
        }
        if (o1._numberOfPiece < o2._numberOfPiece) {
            return 1;
        }
        if (o1._numberOfPiece > o2._numberOfPiece) {
            return -1;
        }
        if (o1.getOwner() == _winner && o2.getOwner() != _winner) {
            return 1;
        } else if (o1.getOwner() != _winner && o2.getOwner() == _winner) {
            return -1;
        }
        return 0;
    }
}
