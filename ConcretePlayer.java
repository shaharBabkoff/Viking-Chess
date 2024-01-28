public class ConcretePlayer implements Player {
    private boolean _player;
    private int _wins;

    public ConcretePlayer(boolean player, int wins) {
        this._player = player;
        this._wins = wins;
    }

    @Override
    public boolean isPlayerOne() {
        return _player;
    }

    @Override
    public int getWins() {
        return this._wins;
    }

    public void updateWins() {
        this._wins++;
    }
}
