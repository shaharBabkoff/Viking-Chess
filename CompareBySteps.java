import java.util.Comparator;

public class CompareBySteps implements Comparator<ConcretePiece> {

    //compare 2 pieces by their steps:
    // 1. by the winner of the game
    // 2. by number of steps from high to low
    // 3. by the number name of the piece
    private Player _winner;

    public CompareBySteps(Player winner) {
        this._winner = winner;
    }

    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        if (o1.getOwner() == _winner && o2.getOwner() != _winner) {
            return -1;
        } else if (o1.getOwner() != _winner && o2.getOwner() == _winner) {
            return 1;
        }
        if (o1._steps.size() > o2._steps.size()) {
            return 1;
        }
        if (o1._steps.size() < o2._steps.size()) {
            return -1;
        }
        if (o1._numberOfPiece > o2._numberOfPiece) {
            return 1;
        }
        if (o1._numberOfPiece < o2._numberOfPiece) {
            return -1;
        }
        return 0;

    }

}
