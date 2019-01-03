import static org.junit.Assert.*;
import org.junit.*;

public class ConnectFourTests extends Board {
	private Board board;
	
	@Before
	public void setUp() {
		board = new Board();
	}
	
	@Test
	public void testEmptyOnInit() {
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col ++) {
				assertEquals("All cells empty", 0, board.getValue(row, col));
			}
		}
	}
	
	@Test
	public void testSetGetValue() {
		assertEquals("cell is empty", 0, board.getValue(0, 5));
		board.setValue(0, 5, 1);
		assertEquals("cell has 1", 1, board.getValue(0, 5));
	}
	
	@Test
	public void testTogglePlayablePlay() {
		Move m = new Move(1, 2);
		board.togglePlayable();
		board.playMove(m);
		assertEquals("cell is empty", 0, board.getValue(5, 2));
	}
	
	@Test
	public void testTogglePlayableUndo() {
		Move m = new Move(1, 0);
		board.playMove(m);
		board.togglePlayable();
		board.undoMove();
		assertEquals("cell is still 1", 1, board.getValue(5, 0));
	}
	
	@Test
	public void testTogglePlayableTwice() {
		Move m = new Move(1, 2);
		board.togglePlayable();
		board.playMove(m);
		board.togglePlayable();
		board.playMove(m);
		assertEquals("cell has 1", 1, board.getValue(5, 2));
	}
	
	@Test
	public void testGetPlayerTurn() {
		Move m= new Move(1, 0);
		assertTrue("starts on player's turn", board.getPlayerTurn());
		board.playMove(m);
		assertFalse("player's move toggles turn", board.getPlayerTurn());
	}
	
	@Test
	public void testResetBoard() {
		Move m1 = new Move(1, 6);
		Move m2 = new Move(2, 5);
		Move m3 = new Move(1, 5);
		Move m4 = new Move(1, 4);
		board.playMove(m1);
		board.playMove(m2);
		board.playMove(m3);
		board.playMove(m4);
		assertEquals("not empty", 1, board.getValue(5, 6));
		board.resetBoard();
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col ++) {
				assertEquals("All cells empty", 0, board.getValue(row, col));
			}
		}
	}
	
	@Test
	public void testMakeMove() {
		Move m1 = new Move(1, 0);
		Move m2 = new Move(2, 0);
		Move m3 = new Move(1, 0);
		assertTrue("first move is player's", board.makeMove(0).equals(m1));
		board.playMove(m1);
		assertTrue("second move is computer's", board.makeMove(0).equals(m2));
		board.playMove(m2);
		assertTrue("third move is player's", board.makeMove(0).equals(m3));
	}
	
	@Test
	public void testAvailableRow() {
		Move m1 = new Move(1, 0);
		Move m2 = new Move(2, 0);
		Move m3 = new Move(1, 0);
		assertEquals("row 5 is open", 5, board.availableRow(m1.getColumn()));
		board.playMove(m1);
		assertEquals("row 4 is open", 4, board.availableRow(m2.getColumn()));		
		board.playMove(m2);
		assertEquals("row 3 is open", 3, board.availableRow(m3.getColumn()));
		board.undoMove();
		assertEquals("row 4 is reopened", 4, board.availableRow(m3.getColumn()));
	}
	
	@Test
	public void testAvailableRowFullCol() {
		Move m1 = new Move(1, 0);
		Move m2 = new Move(2, 0);
		Move m3 = new Move(1, 0);
		Move m4 = new Move(2, 0);
		Move m5 = new Move(1, 0);
		Move m6 = new Move(2, 0);
		Move m7 = new Move(1, 0);
		board.playMove(m1);
		board.playMove(m2);
		board.playMove(m3);
		board.playMove(m4);
		board.playMove(m5);
		board.playMove(m6);
		assertEquals("no rows available", -1, board.availableRow(m7.getColumn()));
	}
	
	@Test
	public void testPlayMove() {
		Move m = new Move(1, 2);
		assertEquals("Places in row 5", 5, board.playMove(m));
		assertEquals("Places 1 in cell (5, 2)", 1, board.getValue(5, 2));
	}
	
	@Test
	public void testPlayMoveFullColumn() {
		Move m1 = new Move(1, 0);
		Move m2 = new Move(2, 0);
		Move m3 = new Move(1, 0);
		Move m4 = new Move(2, 0);
		Move m5 = new Move(1, 0);
		Move m6 = new Move(2, 0);
		Move m7 = new Move(1, 0);
		board.playMove(m1);
		board.playMove(m2);
		board.playMove(m3);
		board.playMove(m4);
		board.playMove(m5);
		board.playMove(m6);
		board.playMove(m7);
		assertTrue("Should still be player's turn", board.getPlayerTurn());
		assertEquals("Playmove returns -1", -1, board.playMove(m7));
	}
	
	@Test
	public void testUndoMoveSingleMove() {
		Move m = new Move(1, 0);
		board.playMove(m);
		assertEquals("Places 1 in cell (5, 0)", 1, board.getValue(5, 0));
		board.undoMove();
		assertEquals("Removes 1 in cell (5, 0)", 0, board.getValue(5, 0));
	}
	
	@Test
	public void testUndoMoveMulitple() {
		Move m1 = new Move(1, 0);
		Move m2 = new Move(2, 0);
		Move m3 = new Move(1, 3);
		Move m4 = new Move(2, 5);
		board.playMove(m1);
		board.playMove(m2);
		board.playMove(m3);
		assertEquals("1 in cell (5, 3)", 1, board.getValue(5, 3));
		board.undoMove();
		assertEquals("Removes 1 in cell (5, 3)", 0, board.getValue(5, 3));
		board.playMove(m4);
		assertEquals("2 in cell (5, 5)", 2, board.getValue(5, 5));
		board.undoMove();
		assertEquals("Removes 2 in cell (5, 5)", 0, board.getValue(5, 5));
		assertEquals("Still 2 in cell (4, 0)", 2, board.getValue(4, 0));
	}
	
	@Test
	public void testIsWinningMoveHorizontal() {
		Move m1 = new Move(1, 3);
		Move m2 = new Move(1, 4);
		Move m3 = new Move(1, 5);
		Move m4 = new Move(1, 6);
		assertEquals("Places in row 5", 5, board.playMove(m1));
		assertEquals("Places in row 5", 5, board.playMove(m2));
		assertEquals("Places in row 5", 5, board.playMove(m3));
		assertEquals("Places in row 5", 5, board.playMove(m4));
		assertTrue("m4 is a winningMove", board.isWinningMove(5, m4));
	}
	
	@Test
	public void testIsWinningMoveHorizontalBeforePlay() {
		Move m1 = new Move(1, 3);
		Move m2 = new Move(1, 4);
		Move m3 = new Move(1, 5);
		Move m4 = new Move(1, 6);
		assertEquals("Places in row 5", 5, board.playMove(m1));
		assertEquals("Places in row 5", 5, board.playMove(m2));
		assertEquals("Places in row 5", 5, board.playMove(m3));
		assertTrue("m4 is a winningMove", board.isWinningMove(5, m4));
	}
	
	@Test
	public void testIsWinningMoveVertical() {
		Move m1 = new Move(1, 0);
		Move m2 = new Move(1, 0);
		Move m3 = new Move(1, 0);
		Move m4 = new Move(1, 0);
		assertEquals("Places in row 5", 5, board.playMove(m1));
		assertEquals("Places in row 5", 4, board.playMove(m2));
		assertEquals("Places in row 5", 3, board.playMove(m3));
		assertTrue("m4 is a winningMove", board.isWinningMove(2, m4));
	}
	
	@Test
	public void testIsWinningMoveDiagonalLR() {
		Move m1 = new Move(1, 6);
		Move m2 = new Move(2, 5);
		Move m3 = new Move(1, 5);
		Move m4 = new Move(1, 4);
		Move m5 = new Move(2, 4);
		Move m6 = new Move(1, 4);
		Move m7 = new Move(2, 3);
		Move m8 = new Move(1, 3);
		Move m9 = new Move(2, 3);
		Move m10 = new Move(1, 3);
		board.playMove(m1);
		board.playMove(m2);
		board.playMove(m3);
		board.playMove(m4);
		board.playMove(m5);
		board.playMove(m6);
		board.playMove(m7);
		board.playMove(m8);
		board.playMove(m9);
		board.playMove(m10);
		assertTrue("m10 is a winningMove", board.isWinningMove(2, m10));
	}
	
	@Test
	public void testIsWinningMoveDiagonalRL() {
		Move m1 = new Move(1, 0);
		Move m2 = new Move(2, 1);
		Move m3 = new Move(1, 1);
		Move m4 = new Move(1, 2);
		Move m5 = new Move(2, 2);
		Move m6 = new Move(1, 2);
		Move m7 = new Move(2, 3);
		Move m8 = new Move(1, 3);
		Move m9 = new Move(2, 3);
		Move m10 = new Move(1, 3);
		board.playMove(m1);
		board.playMove(m2);
		board.playMove(m3);
		board.playMove(m4);
		board.playMove(m5);
		board.playMove(m6);
		board.playMove(m7);
		board.playMove(m8);
		board.playMove(m9);
		assertTrue("m10 is a winningMove", board.isWinningMove(2, m10));
	}
}