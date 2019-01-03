
public class Move {
	private int player;
	private int column;

	public Move(int p, int c) {
		player = p;
		column = c;
	}
	
	public int getPlayer() {
		int p = player;
		return p;
	}
	
	public int getColumn() {
		int c = column;
		return c;
	}
	
	public boolean equals(Move m) {
		if (this.getPlayer() == m.getPlayer()
				&& this.getColumn() == m.getColumn()) {
			return true;
		}
		return false;
	}
}
