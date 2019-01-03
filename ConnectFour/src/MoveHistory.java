import java.util.LinkedList;
import java.util.List;

public class MoveHistory {
	private List<Move> history;

	public MoveHistory() {
		history = new LinkedList<Move>();
	}
	
	public void addMove(Move m) {
		history.add(m);
	}
	
	public void removeMove(Move m) {
		history.remove(m);
	}
	
	public Move getLastMove() {
		return history.get(history.size() - 1);
	}
	
	public Move getMoveNumber(int x) {
		return history.get(x);
	}
	
	public int getHistorySize() {
		return history.size();
	}
}