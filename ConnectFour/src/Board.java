import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class Board extends JPanel {
	int[][] board;
	MoveHistory history = new MoveHistory();
	private boolean playerTurn = true;
	private boolean playable = true;

	public Board() {
		board = new int[7][6];
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				board[col][row] = 0;
			}
		}
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		for (int row = 0; row < 6; row ++) {
			for (int col = 0; col < 7; col++) {
				g.drawRect(70 + col * 80, row * 80, 80, 80);
				int cellState = board[col][row];
				if (cellState == 0) {
					g.clearRect(70 + col * 80, row * 80, 80, 80);
				} else if (cellState == 1) {
					g.setColor(Color.RED);
					g.drawOval(70 + col * 80, row * 80, 80, 80);
					g.fillOval(70 + col * 80, row * 80, 80, 80);
					g.setColor(Color.BLACK);
				} else {
					g.setColor(Color.YELLOW);
					g.drawOval(70 + col * 80, row * 80, 80, 80);
					g.fillOval(70 + col * 80, row * 80, 80, 80);
					g.setColor(Color.BLACK);
				}
			}
		}
	}
	
	public void setValue(int row, int col, int x) {
		board[col][row] = x;
	}
	
	public int getValue(int row, int col) {
		int value = board[col][row];
		return value;
	}
	
	public void togglePlayable() {
		playable = !playable;
	}
	
	public boolean getPlayerTurn() {
		boolean turn = playerTurn;
		return turn;
	}
	
	public void resetBoard() {
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				setValue(row, col, 0);
			}
		}
		playerTurn = true;
		playable = true;
		history = new MoveHistory();
		repaint();
	}
	
	public Move makeMove(int c) {
		if (playerTurn) {
			return new Move(1, c);
		} else {
			return new Move(2, c);
		}
	}
	
	public int availableRow(int col) {
		for (int row = 5; row >= 0; row--) {
			if (getValue(row, col) == 0) {
				return row;
			}
		}
		return -1;
	}
	
	public int playMove(Move m) {
		if (playable) {
			for (int row = 5; row >= 0; row--) {
				if (getValue(row, m.getColumn()) == 0) {
					setValue(row, m.getColumn(), m.getPlayer());
					history.addMove(m);
					playerTurn = !playerTurn;
					repaint();
					return row;
				}
			}
		}
		return -1;
	}
	
	public void undoMove() {
		if (history.getHistorySize() != 0 && playable) {
			Move m = history.getLastMove();
			history.removeMove(m);
			for (int row = 0; row < 6; row++) {
				if (getValue(row, m.getColumn()) != 0) {
					setValue(row, m.getColumn(), 0);
					break;
				}
			}
		}
		repaint();
	}
	
	public boolean isWinningMove(int row, Move m) {
		int col = m.getColumn();
		int player = m.getPlayer();
		int currentValue = getValue(row, col);
		setValue(row, col, player);
		int sum;
		
		//test horizontally
		for (int i = 0; i < 4; i++) {
			int testCol = i;
			sum = 0;
			while (testCol < 7 && getValue(row, testCol) == player) {
				sum++;
				testCol++;
			}
			if (sum == 4) {
				setValue(row, col, currentValue);
				return true;
			}
		}
		//test vertically
		for (int j = 0; j < 5; j++) {
			int testRow = j;
			sum = 0;
			while (testRow < 6 && getValue(testRow, col) == player) {
				sum++;
				testRow++;
			}
			if (sum == 4) {
				setValue(row, col, currentValue);
				return true;
			}
		}
		//test LR diagonal
		int RowLR = Math.max(0, row - col);
		int ColLR = Math.max(0, col - row);
		int testRowLR;
		int testColLR;
		
		for (int k = 0; k < 4; k++) {
			testRowLR = RowLR + k;
			testColLR = ColLR + k;
			sum = 0;
			while (testRowLR < 6 && testColLR < 7 && getValue(testRowLR, testColLR) == player) {
				sum++;
				testRowLR++;
				testColLR++;
			}
			if (sum == 4) {
				setValue(row, col, currentValue);
				return true;
			}
		}
		//test RL diagonal
		int RowRL = Math.max(0, row - 6 + col);
		int ColRL = Math.min(6, col + row);
		int testRowRL;
		int testColRL;
		
		for (int l = 0; l < 4; l++) {
			testRowRL = RowRL + l;
			testColRL = ColRL - l;
			sum = 0;
			while (testRowRL < 6 && testColRL >= 0 && getValue(testRowRL, testColRL) == player) {
				sum++;
				testRowRL++;
				testColRL--;
			}
			if (sum == 4) {
				setValue(row, col, currentValue);
				return true;
			}
		}
		
		setValue(row, col, currentValue);
		return false;
	}
	
	public int computerColumn() {
		int computerChoice = (int)(7 * Math.random());
		ArrayList<Integer> blacklist = new ArrayList<Integer>();
		
		if (history.getHistorySize() == 1) {
			computerChoice = Math.min(5, history.getLastMove().getColumn() + 1);
			return computerChoice;
		}
			
		for (int col = 0; col < 7; col++) {
			Move m = new Move(2, col);
			int row = availableRow(col);
			if (row >= 0 && row < 6) {
				if (isWinningMove(row, m)) {
					computerChoice = col;
					return computerChoice;
				}
			}
		}
		for (int col = 0; col < 7; col++) {
			Move m = new Move(1, col);
			int row = availableRow(col);
			if (row >= 0 && row < 6) {
				if (isWinningMove(row, m)) {
					computerChoice = col;
					return computerChoice;
				}
			}
		}
		for (int col1 = 0; col1 < 7; col1++) {
			int row1 = availableRow(col1);
			if (row1 >= 0 && row1 < 6) {
				setValue(row1, col1, 2);
				for (int col2 = 0; col2 < 7; col2++) {
					Move m2 = new Move(1, col2);
					int row2 = availableRow(col2);
					if (row2 >= 0 && row2 < 6) {
						if (!isWinningMove(row2, m2)) {
							setValue(row2, col2, 1);
							for (int col3 = 0; col3 < 7; col3++) {
								Move m3 = new Move(2, col3);
								int row3 = availableRow(col3);
								if (row3 >= 0 && row3 < 6) {
									if (isWinningMove(row3, m3) && !blacklist.contains(col3)) {
										computerChoice = col1;
										setValue(row1, col1, 0);
										setValue(row2, col2, 0);
										return computerChoice;
									}
								}
							}
						} else {
							blacklist.add(col2);
						}
						setValue(row2, col2, 0);
					}
				}
				setValue(row1, col1, 0);
			}
		}
		
		if (blacklist.contains(computerChoice)) {
			for (int col = 0; col < 7; col++) {
				if (availableRow(col) != -1 && col != computerChoice) {
					computerChoice = col;
				}
			}
		}
		return computerChoice;
	}
}