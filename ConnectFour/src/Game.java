import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Game implements Runnable {
	private final static JFrame frame = new JFrame("Connect Four");
	private final JButton reset = new JButton("New Game");
	private final JButton undo = new JButton("Undo");
	private Board board = new Board();
	Container grid = new Container();

	public void run() {
		frame.setSize(700, 600);
		frame.setMinimumSize(new Dimension(700, 600));
        frame.setMaximumSize(new Dimension(700, 600));
      
        frame.add(flowColumnButtons(), BorderLayout.NORTH);
        frame.add(flowLayoutPanel(), BorderLayout.SOUTH);
        
        undo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		board.undoMove();
        		board.undoMove();
        	}
        });
        
        reset.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		board.resetBoard();
        	}
        });
        
        frame.add(board, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JPanel flowLayoutPanel() {
		JPanel panel = new JPanel();
		panel.add(reset);
		panel.add(undo);
		return panel;
	}
	
	private JPanel flowColumnButtons() {
		JPanel panel = new JPanel();
		for (int i = 0; i < 7; i++) {
			int column = i;
			JButton columnButton = new JButton(Integer.toString(i + 1));
			columnButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Move mp = board.makeMove(column);
					int rowp = board.playMove(mp);
					if (rowp >= 0 && board.isWinningMove(rowp, mp)) {
						board.togglePlayable();
						JOptionPane.showMessageDialog(frame, "Player Wins!");
					}
					if (board.getPlayerTurn() == false) {
						Move mc = board.makeMove(board.computerColumn());
						int rowc = board.playMove(mc);
						if (rowc >= 0 && board.isWinningMove(rowc, mc)) {
							board.togglePlayable();
							JOptionPane.showMessageDialog(frame, "Computer Wins...");
						}
					}
				}
			});
			panel.add(columnButton);
		}
		return panel;
	}
	
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(frame, 
				"<html><center><strong>Instructions</strong><center><br>"
				+ "<p>Welcome to ConnectFour! Use the COLUMN BUTTONS at<br>"
				+ "the top of the window to select which column you'd like<br>"
				+ "to drop your pieces in. Your pieces are RED, and the<br>"
				+ "computer's pieces are YELLOW. Get FOUR of your pieces<br>"
				+ "in a row horizontally, vertically, or diagonally<br>"
				+ "before the opponent does to win.</p></html>");
		SwingUtilities.invokeLater(new Game());
	}
}