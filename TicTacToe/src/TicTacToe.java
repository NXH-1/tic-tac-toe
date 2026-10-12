import java.awt.*;
import javax.swing.*;

public class TicTacToe {
    int windowWidth = 600;
    int WindowHeight= 700;
    
    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    JButton[][] board = new JButton[3][3];
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    boolean gameOver = false;
    int turns = 0;

    Color customDarkGray = new Color(30, 30, 46);
    Color customRed = new Color(243, 160, 168);
    Color customGreen = new Color(166, 227, 161);
    Color customOrange = new Color(250, 179, 135);
    Color textColor = new Color(205, 214, 244);

    TicTacToe() {
        frame.setVisible(true);
        frame.setSize(windowWidth, WindowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setBackground(customDarkGray);
        textLabel.setForeground(textColor);
        textLabel.setFont(new Font("Arial", Font.BOLD,50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3,3));
        boardPanel.setBackground(customDarkGray);
        frame.add(boardPanel);


        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                JButton tile = new JButton ();
                board[row][column] = tile;
                boardPanel.add(tile);

                tile.setBackground(customDarkGray);
                tile.setForeground(textColor);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);

                tile.addActionListener(e -> {
                    
                    JButton sourceTile = (JButton) e.getSource();
                    if (sourceTile.getText().equals("")) {
                        if (gameOver) return;
                        sourceTile.setText(currentPlayer);
                        turns++;
                        checkWinner();
                        

                        if (!gameOver) {
                            currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                            textLabel.setText(currentPlayer + "'s turn.");
                        }
                    }
                });
            }
        }
    }

    void checkWinner() {
        int[][] winConditions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
        };

        for (int[] condition : winConditions) {
            JButton firstSpace = board[condition[0] % 3][condition[0] / 3];
            JButton secondSpace = board[condition[1] % 3][condition[1] / 3];
            JButton thirdSpace = board[condition[2] % 3][condition[2] / 3];
            
            if (firstSpace.getText().equals("")) continue;
            
            if (firstSpace.getText().equals(secondSpace.getText())
                && secondSpace.getText().equals(thirdSpace.getText())) {
                
                setWinner(firstSpace, secondSpace, thirdSpace);
                gameOver = true;
                return;
            }
        }

        if (turns == 9) {
            setTie();
            gameOver = true;
        }

    }

    void setWinner(JButton space1, JButton space2, JButton space3) {
        space1.setForeground(customGreen);
        space2.setForeground(customGreen);
        space3.setForeground(customGreen);
        
        textLabel.setText(currentPlayer + " is the winner!");
    }

    void setTie() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                board[row][column].setForeground(customOrange);
            }
        }
        textLabel.setText("It's a Tie!");
    }
}

// Todo: add restart function
// Todo: add a main menu that you can pick between cpu or a another player
// Todo: add a scorring system between players