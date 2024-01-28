import java.util.Comparator;

public class CompareByPieces implements Comparator<Position> {
    //compare 2 positions by their number of different pieces that stepped on them:
    // 1. by number from high to low
    // 2. by the X of the position
    // 3. by the Y of the position
    @Override
    public int compare(Position o1, Position o2) {
        if (o1.getNumOfPiecesInPos()< o2.getNumOfPiecesInPos()) {
            return 1;
        }
        if ((o1.getNumOfPiecesInPos()> o2.getNumOfPiecesInPos())) {
            return -1;
        }
        if(o1.getX()>o2.getX()) {
            return 1;
        }
        if(o1.getX()<o2.getX()) {
            return -1;
        }
        if(o1.getY()>o2.getY()) {
            return 1;
        }
        if(o1.getY()<o2.getY()) {
            return -1;
        }
        return 0;
    }

}
