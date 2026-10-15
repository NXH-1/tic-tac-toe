import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Game extends JPanel {
    boolean vsCpuMode;
    private Runnable BackToMenu;
    
    JLabel textLabel = new JLabel();
    JPanel topContainer = new JPanel();
    JPanel scorePanel = new JPanel();
    JPanel boardPanel;
    JPanel controlPanel = new JPanel();

    JButton[][] board = new JButton[3][3];
    JButton restartButton = new JButton("Restart");
    JButton resetScoreButton = new JButton("Reset Score");
    JButton backButton = new JButton("← Back");
    JComboBox<String> difficultyDropdown;
    JButton switchSideButton;

    JLabel xScoreLabel = new JLabel("X: 0", JLabel.LEFT);
    JLabel tieScoreLabel = new JLabel("Ties: 0", JLabel.CENTER);
    JLabel oScoreLabel = new JLabel("O: 0", JLabel.RIGHT);
    
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    // For CPU Mode
    String cpuPlayer = playerO;

    int scoreX = 0;
    int scoreO = 0;
    int ties = 0;

    boolean gameOver = false;
    int turns = 0;
    int winStartRow, winStartCol, winEndRow, winEndCol;

    Game(boolean vsCpu, Runnable BackToMenu) {
        this.vsCpuMode = vsCpu;
        this.BackToMenu = BackToMenu;

        setLayout(new BorderLayout());
        setBackground(Theme.CUSTOM_DARK_GRAY);

        topContainer.setLayout(new BorderLayout());
        topContainer.setBackground(Theme.CUSTOM_DARK_GRAY);

        textLabel.setBackground(Theme.CUSTOM_DARK_GRAY);
        textLabel.setForeground(Theme.TEXT_COLOR);
        textLabel.setFont(new Font("Arial", Font.BOLD,50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Vs a Friend"); 
        textLabel.setOpaque(true);
        textLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        topContainer.add(textLabel, BorderLayout.NORTH);

        scorePanel.setLayout(new GridLayout(1, 3));
        scorePanel.setBackground(Theme.CUSTOM_DARK_GRAY);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 12, 25));
        
        xScoreLabel.setForeground(Theme.SUB_TEXT_COLOR);
        xScoreLabel.setFont(Theme.SCORE_FONT);

        tieScoreLabel.setForeground(Theme.SUB_TEXT_COLOR);
        tieScoreLabel.setFont(Theme.SCORE_FONT);
        
        oScoreLabel.setForeground(Theme.SUB_TEXT_COLOR);
        oScoreLabel.setFont(Theme.SCORE_FONT);

        scorePanel.add(xScoreLabel);
        scorePanel.add(tieScoreLabel);
        scorePanel.add(oScoreLabel);

        topContainer.add(scorePanel, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        boardPanel = new JPanel() {
            @Override
            protected void paintChildren(Graphics g) {
                super.paintChildren(g);
                
                if (gameOver) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(Theme.CUSTOM_RED);
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
        boardPanel.setBackground(Theme.CUSTOM_DARK_GRAY);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(boardPanel, BorderLayout.CENTER);


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

                tile.setBackground(Theme.CUSTOM_DARK_GRAY);
                tile.setForeground(Theme.TEXT_COLOR);
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

                            if (vsCpuMode && currentPlayer.equals(cpuPlayer)) {
                                Timer cpuTimer = new Timer(300, event -> {
                                    makeCpuMove();
                                    ((Timer)event.getSource()).stop();
                                });
                                cpuTimer.setRepeats(false);
                                cpuTimer.start();
                            }
                        }
                    }
                });
            }
        }

        
        controlPanel.setBackground(Theme.CUSTOM_DARK_GRAY);
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JPanel leftActions = new JPanel();
        leftActions.setLayout(new BoxLayout(leftActions, BoxLayout.Y_AXIS));
        leftActions.setBackground(Theme.CUSTOM_DARK_GRAY);

        restartButton.setFont(Theme.BUTTON_FONT);
        restartButton.setFocusable(false);
        restartButton.setBackground(Theme.CUSTOM_GRAY);
        restartButton.setForeground(Theme.TEXT_COLOR);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> resetGame());

        resetScoreButton.setFont(Theme.BUTTON_FONT);
        resetScoreButton.setFocusable(false);
        resetScoreButton.setBackground(Theme.CUSTOM_RED);
        resetScoreButton.setForeground(Theme.CUSTOM_DARK_GRAY);
        resetScoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetScoreButton.addActionListener(e -> resetScores());

        backButton.setFont(Theme.BUTTON_FONT);
        backButton.setFocusable(false);
        backButton.setBackground(Theme.CUSTOM_RED);
        backButton.setForeground(Color.WHITE);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1), 
                                                                BorderFactory.createEmptyBorder(2, 20, 2, 20)));
        backButton.addActionListener(e -> BackToMenu.run());

        leftActions.add(restartButton);
        leftActions.add(Box.createRigidArea(new Dimension(10, 8)));
        leftActions.add(resetScoreButton);
        leftActions.add(Box.createRigidArea(new Dimension(10, 8)));
        leftActions.add(backButton);
        controlPanel.add(leftActions);

        if (vsCpuMode) {
            restartButton.setAlignmentX(LEFT_ALIGNMENT);
            resetScoreButton.setAlignmentX(LEFT_ALIGNMENT);
            backButton.setAlignmentX(LEFT_ALIGNMENT);

            JPanel rightActions = new JPanel();
            rightActions.setLayout(new BoxLayout(rightActions, BoxLayout.Y_AXIS));
            rightActions.setBackground(Theme.CUSTOM_DARK_GRAY);
            

            textLabel.setText("Vs a CPU");

            String[] difficulties = {"Normal", "Hard", "Unbeatable"};
            difficultyDropdown = new JComboBox<>(difficulties);
            difficultyDropdown.setFont(Theme.BUTTON_FONT);
            difficultyDropdown.setBackground(Theme.CUSTOM_DARK_GRAY);
            difficultyDropdown.setForeground(Theme.TEXT_COLOR);
            difficultyDropdown.setFocusable(false);
            difficultyDropdown.setMaximumSize(new Dimension(250, 60));

            switchSideButton = new JButton("Switch to O");
            switchSideButton.setFont(Theme.BUTTON_FONT);
            switchSideButton.setFocusable(false);
            switchSideButton.setBackground(Theme.CUSTOM_GRAY);
            switchSideButton.setForeground(Theme.TEXT_COLOR);
            switchSideButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            switchSideButton.addActionListener(e -> toggleSide());

            rightActions.add(difficultyDropdown);
            rightActions.add(Box.createRigidArea(new Dimension(0, 8)));
            rightActions.add(switchSideButton);
            rightActions.add(Box.createRigidArea(new Dimension(0, 50)));
            controlPanel.add(rightActions);

            updateScoreText();
        }

        add(controlPanel, BorderLayout.SOUTH);


    }

    void resetScores() {
        scoreX = 0;
        scoreO = 0;
        ties = 0;
        updateScoreText();
    }

    void updateScoreText() {
        String humanIsX = "";
        String humanIsO = "";
        if (vsCpuMode) {
            if (cpuPlayer.equals("X")) {
                humanIsO = " (Player)";
            } else {
                humanIsX = " (Player)";
            }
        }
        xScoreLabel.setText("X: " + scoreX + humanIsX);
        tieScoreLabel.setText("Ties: " + ties);
        oScoreLabel.setText("O: " + scoreO + humanIsO);
    }

    void resetGame() {
        gameOver = false;
        turns = 0;
        currentPlayer = playerX;
        textLabel.setText("Tic-Tac-Toe");

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                board[row][column].setText("");
                board[row][column].setForeground(Theme.TEXT_COLOR);
            }
        }
        boardPanel.repaint();

        if (vsCpuMode && currentPlayer.equals(cpuPlayer)) {
            Timer cpuTimer = new Timer(300, event -> {
                makeCpuMove();
                ((Timer)event.getSource()).stop();
            });
            cpuTimer.setRepeats(false);
            cpuTimer.start();
        }
    }

    void toggleSide() {
        if (cpuPlayer.equals("X")) {
            cpuPlayer = "O";
            switchSideButton.setText("Switch to X");
        } else {
            cpuPlayer = "X";
            switchSideButton.setText("Switch to O");
        }
        resetGame();
        updateScoreText();
    }

    void checkWinner() {

        for (int[] condition : getWinConditions()) {

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
        space1.setForeground(Theme.CUSTOM_GREEN);
        space2.setForeground(Theme.CUSTOM_GREEN);
        space3.setForeground(Theme.CUSTOM_GREEN);
        
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
                board[row][column].setForeground(Theme.CUSTOM_ORANGE);
            }
        }
        textLabel.setText("It's a Tie!");

        ties++;
        updateScoreText();
    }

    public static int[][] getWinConditions() {
        int[][] winConditions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
        };
        return winConditions;
    }

    void makeCpuMove() {
        String difficulty = (String) difficultyDropdown.getSelectedItem();
        double chanceForBestMove = 0.6;

        if (difficulty.equals("Hard")) {
            chanceForBestMove = 0.8;
        } else if (difficulty.equals("Unbeatable")) {
            chanceForBestMove = 1;
        }

        boolean playBestMove = Math.random() < chanceForBestMove;
        int bestRow = -1;
        int bestColumn = -1;

        String[][] currentBoardState = new String[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                currentBoardState[r][c] = board[r][c].getText();
            }
        }

        if (playBestMove) {
            int[] optimalMove = Minimax.getBestMove(currentBoardState, cpuPlayer);
            bestRow = optimalMove[0];
            bestColumn = optimalMove[1];
        } else {
            java.util.List<int[]> emptyTiles = new java.util.ArrayList<>();
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c].getText().equals("")) {
                        emptyTiles.add(new int[]{r, c});
                    }
                }
            }
            if (!emptyTiles.isEmpty()) {
                int randomIndex = (int) (Math.random() * emptyTiles.size());
                bestRow = emptyTiles.get(randomIndex)[0];
                bestColumn = emptyTiles.get(randomIndex)[1];
            }
        }

        if (bestRow != -1 && bestColumn != -1) {
            board[bestRow][bestColumn].setText(cpuPlayer);
            turns++;
            checkWinner();

        if (!gameOver) {
                currentPlayer = cpuPlayer.equals("X") ? "O" : "X";
                textLabel.setText(currentPlayer + "'s turn.");
            }
        }

    }
}