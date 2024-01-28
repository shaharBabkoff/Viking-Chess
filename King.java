public class King extends ConcretePiece {
    private String _type = "♔";

    public King(Player player, Position pos) {//constructor of king (from ConcretePiece class)
        super(player, pos);
    }


    public Player getOwner() {//get the owner of the king
        return super.getOwner();
    }

    @Override
    public String getType() {
        return this._type;
    }

}