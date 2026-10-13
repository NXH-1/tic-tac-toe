import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Game {
    int windowWidth = 620;
    int WindowHeight= 860;
    
    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel topContainer = new JPanel();
    JPanel scorePanel = new JPanel();
    JPanel boardPanel;
    JPanel controlPanel = new JPanel();

    JButton[][] board = new JButton[3][3];
    JButton restartButton = new JButton("Restart");
    JButton resetScoreButton = new JButton("Reset Score");

    JLabel xScoreLabel = new JLabel("X: 0", JLabel.LEFT);
    JLabel tieScoreLabel = new JLabel("Ties: 0", JLabel.CENTER);
    JLabel oScoreLabel = new JLabel("O: 0", JLabel.RIGHT);
    Font scoreFont = new Font("Arial", Font.BOLD, 25);
    
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    int scoreX = 0;
    int scoreO = 0;
    int ties = 0;

    boolean gameOver = false;
    int turns = 0;
    int winStartRow, winStartCol, winEndRow, winEndCol;

    Color customDarkGray = new Color(30, 30, 46);
    Color customGray = new Color(69, 71, 90);
    Color customRed = new Color(243, 160, 168);
    Color customGreen = new Color(166, 227, 161);
    Color customOrange = new Color(250, 179, 135);
    Color textColor = new Color(205, 214, 244);
    Color subTextColor = new Color(166, 173, 200);

    Game() {
        frame.setVisible(true);
        frame.setSize(windowWidth, WindowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        topContainer.setLayout(new BorderLayout());
        topContainer.setBackground(customDarkGray);

        textLabel.setBackground(customDarkGray);
        textLabel.setForeground(textColor);
        textLabel.setFont(new Font("Arial", Font.BOLD,60));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);
        textLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        topContainer.add(textLabel, BorderLayout.NORTH);

        scorePanel.setLayout(new GridLayout(1, 3));
        scorePanel.setBackground(customDarkGray);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 12, 25));
        
        xScoreLabel.setForeground(subTextColor);
        xScoreLabel.setFont(scoreFont);

        tieScoreLabel.setForeground(subTextColor);
        tieScoreLabel.setFont(scoreFont);
        
        oScoreLabel.setForeground(subTextColor);
        oScoreLabel.setFont(scoreFont);

        scorePanel.add(xScoreLabel);
        scorePanel.add(tieScoreLabel);
        scorePanel.add(oScoreLabel);

        topContainer.add(scorePanel, BorderLayout.SOUTH);
        frame.add(topContainer, BorderLayout.NORTH);

        boardPanel = new JPanel() {
            @Override
            protected void paintChildren(Graphics g) {
                super.paintChildren(g);
                
                if (gameOver) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(customRed);
                    g2d.setStroke(new BasicStroke(12, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                    int width = getWidth();
                    int height = getHeight();
                    int cellW = width / 3;
                    int cellH = height / 3;

                    int x1 = winStartCol * cellW + cellW / 2;
                    int y1 = winStartRow * cellH + cellH / 2;
                    int x2 = winEndCol * cellW + cellW / 2;
                    int y2 = winEndRow * cellH + cellH / 2;

                    double extension = 60.0; 

                    double dx = x2 - x1;
                    double dy = y2 - y1;
                    double length = Math.sqrt(dx * dx + dy * dy);
                    double ux = dx / length;
                    double uy = dy / length;

                    x1 -= ux * extension;
                    y1 -= uy * extension;
                    x2 += ux * extension;
                    y2 += uy * extension;

                    g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                }
            }
        };

        boardPanel.setLayout(new GridLayout(3,3, 2, 2));
        boardPanel.setBackground(customDarkGray);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(boardPanel, BorderLayout.CENTER);


        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                JButton tile = new JButton (){
                    @Override
                    protected void processMouseEvent(MouseEvent e) {
                        if (!gameOver) super.processMouseEvent(e);
                    }
                    @Override
                    protected void processMouseMotionEvent(MouseEvent e) {
                        if (!gameOver) super.processMouseMotionEvent(e);
                    }
                };
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

        
        controlPanel.setBackground(customDarkGray);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        restartButton.setFont(new Font("Arial", Font.BOLD, 30));
        restartButton.setFocusable(false);
        restartButton.setBackground(customGray);
        restartButton.setForeground(textColor);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> resetGame());

        resetScoreButton.setFont(new Font("Arial", Font.BOLD, 30));
        resetScoreButton.setFocusable(false);
        resetScoreButton.setBackground(customRed);
        resetScoreButton.setForeground(customDarkGray);
        resetScoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetScoreButton.addActionListener(e -> resetScores());

        controlPanel.add(restartButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        controlPanel.add(resetScoreButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

    }

    void resetScores() {
        scoreX = 0;
        scoreO = 0;
        ties = 0;
        updateScoreText();
    }

    void updateScoreText() {
        xScoreLabel.setText("X: " + scoreX);
        tieScoreLabel.setText("Ties: " + ties);
        oScoreLabel.setText("O: " + scoreO);
    }

    void resetGame() {
        gameOver = false;
        turns = 0;
        currentPlayer = playerX;
        textLabel.setText("Tic-Tac-Toe");

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                board[row][column].setText("");
                board[row][column].setForeground(textColor);
            }
        }
        boardPanel.repaint();
    }

    void checkWinner() {
        int[][] winConditions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
        };

        for (int[] condition : winConditions) {

            JButton firstSpace = board[condition[0] / 3][condition[0] % 3];
            JButton secondSpace = board[condition[1] / 3][condition[1] % 3];
            JButton thirdSpace = board[condition[2] / 3][condition[2] % 3];
            
            if (firstSpace.getText().equals("")) continue;
            
            if (firstSpace.getText().equals(secondSpace.getText())
                && secondSpace.getText().equals(thirdSpace.getText())) {
                    
                setWinningLine(condition[0] / 3, condition[0] % 3, condition[2] / 3, condition[2] % 3);
                setWinner(firstSpace, secondSpace, thirdSpace);
                gameOver = true;
                boardPanel.repaint();
                return;
            }
        }

        if (turns == 9) {
            setTie();
            gameOver = true;
        }

    }

    void setWinningLine(int r1, int c1, int r2, int c2) {
        winStartRow = r1;
        winStartCol = c1;
        winEndRow = r2;
        winEndCol = c2;
    }

    void setWinner(JButton space1, JButton space2, JButton space3) {
        space1.setForeground(customGreen);
        space2.setForeground(customGreen);
        space3.setForeground(customGreen);
        
        textLabel.setText(currentPlayer + " is the winner!");
        if (currentPlayer.equals(playerX)) {
            scoreX++;
        } else {
            scoreO++;
        }
        updateScoreText();
    }

    void setTie() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                board[row][column].setForeground(customOrange);
            }
        }
        textLabel.setText("It's a Tie!");

        ties++;
        updateScoreText();
    }
}

// Todo: add a main menu that you can pick between cpu or a another player
