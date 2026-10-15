
public class Minimax {

    public static int[] getBestMove(String[][] board, String cpu) {
        String humanPlayer = cpu.equals("X") ? "O" : "X";
        int bestScore = -1000;
        int bestRow = -1;
        int bestCol = -1;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c].equals("")) {
                    board[r][c] = cpu;
                    int score = minimax(board, 0, false, cpu, humanPlayer);
                    board[r][c] = "";

                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = r;
                        bestCol = c;
                    }
                }
            }
        }
        return new int[]{bestRow, bestCol};
    }

    private static int minimax(String[][] board, int depth, boolean isMaximizing, String cpu, String humanPlayer) {
        String result = evaluateBoard(board);
        if (result != null) {
            if (result.equals(cpu)) return 10 - depth;
            if (result.equals(humanPlayer)) return depth - 10;
            if (result.equals("tie")) return 0;
        }

        if (isMaximizing) {
            int bestScore = -1000;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c].equals("")) {
                        board[r][c] = cpu;
                        int score = minimax(board, depth + 1, false, cpu, humanPlayer);
                        board[r][c] = "";
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = 1000;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c].equals("")) {
                        board[r][c] = humanPlayer;
                        int score = minimax(board, depth + 1, true, cpu, humanPlayer);
                        board[r][c] = "";
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    public static String evaluateBoard(String[][] board) {
        for (int[] condition : Game.getWinConditions()) {
            String firstSpace = board[condition[0] / 3][condition[0] % 3];
            String secondSpace = board[condition[1] / 3][condition[1] % 3];
            String thirdSpace = board[condition[2] / 3][condition[2] % 3];

            if (firstSpace.equals(secondSpace) && secondSpace.equals(thirdSpace)) {
                return firstSpace;
            }
        }

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c].equals("")) {
                    return null; // Game is still ongoing
                }
            }
        }

        return "tie";
    }
}