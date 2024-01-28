import java.util.Comparator;

public class CompareByDist implements Comparator<ConcretePiece> {
    //compare 2 pieces by their distance:
    // 1. by distance from high to low
    // 2. by the number name of the piece
    // 3. by the winner of the game
    private Player _winner;

    public CompareByDist(Player winner) {
        this._winner = winner;
    }
    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        if (o1._dist < o2._dist) {
            return 1;
        }
        if (o1._dist> o2._dist) {
            return -1;
        }
        if (o1._numberOfPiece >o2._numberOfPiece) {
            return 1;
        }
        if (o1._numberOfPiece < o2._numberOfPiece) {
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
