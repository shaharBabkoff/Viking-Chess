import java.util.ArrayList;

public abstract class ConcretePiece implements Piece {

    protected Player owner;//player 1 or player 2
    protected Position pos;// which position the piece on
    protected int _numberOfPiece;
    protected int _dist;
    protected ArrayList<Position> _steps;


    public ConcretePiece(Player player, Position pos) {//constructor with the owner and the position of the piece
        this.owner = player;
        this.pos = pos;
        this._steps = new ArrayList<>();
        _steps.add(pos);//add the started position of the piece
        this._dist = 0;
    }


    public int get_numberOfPiece() {
        return _numberOfPiece;
    }

    public void addStep(Position pos) {
        this._steps.add(pos);
    }

    public void calculateDist(Position a, Position b) {
        if (a.getX() == b.getX()) {
            int distY = Math.abs(a.getY() - b.getY());
            this._dist = this._dist + distY;
        }
        if (a.getY() == b.getY()) {
            int distX = Math.abs(a.getX() - b.getX());
            this._dist = this._dist + distX;
        }
    }

    public void set_numberOfPiece(int _numberOfPiece) {
        this._numberOfPiece = _numberOfPiece;
    }



    public void printStepsOfPiece() {//function that print all the steps of a piece
        for (int j = 0; j < _steps.size(); j++) {
            Position pos = _steps.get(j);
            System.out.print("(" + pos.getX() + ","+" " + pos.getY() + ")");
            if (j < _steps.size() - 1) {
                System.out.print(","+" ");
            }
        }
    }

    public Player getOwner() {
        return this.owner;
    }

    public String getType() {
        return null;
    }

    public Position getPos() {
        return this.pos;
    }

}
