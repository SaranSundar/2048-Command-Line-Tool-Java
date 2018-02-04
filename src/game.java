import java.awt.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class game {

    private static int[][] board;
    private static Random gen;
    private static boolean gameOver = false;

    public static void main(String[] args) {
        board = new int[4][4];
        gen = new Random(new Date().getTime());
        fillBoard(2);
        drawBoard();
        runGame();
    }

    public static boolean addRight(int[][] board) {
        boolean madeChange = shiftRight(board);
        for (int r = 0; r < board.length; r++) {
            for (int c = board[r].length - 1; c >= 0; c--) {
                if (board[r][c] == 0) {
                    break;
                }
                if (c - 1 >= 0) {
                    if (board[r][c] == board[r][c - 1]) {
                        board[r][c] = board[r][c] * 2;
                        board[r][c - 1] = 0;
                        madeChange = true;
                        c--;
                    }
                }
            }
        }
        shiftRight(board);
        return madeChange;
    }

    public static boolean addLeft(int[][] board) {
        boolean madeChange = shiftLeft(board);
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == 0) {
                    break;
                }
                if (c + 1 < board[r].length) {
                    if (board[r][c] == board[r][c + 1]) {
                        board[r][c] = board[r][c] * 2;
                        board[r][c + 1] = 0;
                        madeChange = true;
                        c++;
                    }
                }
            }
        }
        shiftLeft(board);
        return madeChange;
    }

    static boolean shiftLeft(int[][] board) {
        boolean madeChange = false;
        for (int r = 0; r < board.length; r++) {
            int[] arr = board[r];
            int[] orig = Arrays.copyOf(board[r], board[r].length);
            int n = arr.length;
            int count = 0;
            for (int i = 0; i < n; i++)
                if (arr[i] != 0)
                    arr[count++] = arr[i];
            while (count < n) {
                arr[count++] = 0;
            }
            if (!madeChange) {
                madeChange = !Arrays.equals(orig, arr);
            }
            board[r] = arr;
        }
        return madeChange;
    }

    static boolean shiftRight(int[][] board) {
        boolean madeChange = false;
        for (int r = 0; r < board.length; r++) {
            int[] arr = board[r];
            int[] orig = Arrays.copyOf(board[r], board[r].length);
            int n = arr.length;
            int count = n - 1;
            for (int i = n - 1; i >= 0; i--)
                if (arr[i] != 0)
                    arr[count--] = arr[i];
            while (count >= 0)
                arr[count--] = 0;
            if (!madeChange) {
                madeChange = !Arrays.equals(orig, arr);
            }
            board[r] = arr;
        }
        return madeChange;
    }

    public static boolean addUpDown(boolean direction, int[][] board) {
        int[][] rotated = new int[board[0].length][board.length];
        for (int c = 0; c < board[0].length; c++) {
            for (int r = 0; r < board.length; r++) {
                rotated[c][r] = board[r][c];
            }
        }
        boolean madeChange = false;
        if (direction) { // True for going up, false for going down
            madeChange = addLeft(rotated);
        } else {
            madeChange = addRight(rotated);
        }

        for (int c = 0; c < rotated[0].length; c++) {
            for (int r = 0; r < rotated.length; r++) {
                board[c][r] = rotated[r][c];
            }
        }
        return madeChange;
    }

    public static void runGame() {
        Scanner kb = new Scanner(System.in);
        while (!gameOver) {
            System.out.print("Enter Direction Here, (W,A,S,D): ");
            String com = kb.nextLine().toLowerCase();
            boolean madeChange = false;
            if (com.equals("w")) {
                madeChange = addUpDown(true, board);
            } else if (com.equals("a")) {
                madeChange = addLeft(board);
            } else if (com.equals("s")) {
                madeChange = addUpDown(false, board);
            } else if (com.equals("d")) {
                madeChange = addRight(board);
            }
            if (madeChange) {
                fillBoard(1);
            }
            if (checkGameOver()) {
                System.out.println("GAME OVER!!!");
                gameOver = true;
            }
            System.out.print("\033[H\033[2J");
            System.out.flush();
            drawBoard();


        }
        kb.close();
    }

    public static boolean checkGameOver() {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == 0) {
                    return false;
                }
                if (c + 1 < board[r].length) {
                    if (board[r][c + 1] == 0 || board[r][c] == board[r][c + 1]) {
                        return false;
                    }
                }
                if (r + 1 < board.length) {
                    if (board[r + 1][c] == 0 || board[r][c] == board[r + 1][c]) {
                        return false;
                    }
                }
                if (r + 1 < board.length && c + 1 < board[r].length) {
                    if (board[r + 1][c + 1] == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void fillBoard(int randomNumsToMake) {
        Point[] emptySpots = new Point[board.length * board[0].length];
        int i = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                if (board[r][c] == 0) {
                    emptySpots[i] = new Point(c, r);
                    i++;
                }
            }
        }
        if (i == 0) {
            return;
        }
        for (int ii = 0; ii < randomNumsToMake; ii++) {
            int index = gen.nextInt(100);
            if (index < 90) {
                index = 2;
            } else {
                index = 4;
            }
            while (true) {
                int attemptIndex = gen.nextInt(i);
                if (attemptIndex < emptySpots.length) {
                    Point p = emptySpots[attemptIndex];
                    //System.out.println("COL " + p.getX() + " ROW " + p.getY());
                    if (board[p.y][p.x] == 0) {
                        board[p.y][p.x] = index;
                        return;
                    }
                }
            }
        }
    }

    public static void drawBoard() {
        System.out.println("+-----------------------------------------------------------+");
        for (int r = 0; r < board.length; r++) {
            System.out.println(" |                                                      |");
            System.out.print("|||");
            for (int c = 0; c < board[r].length; c++) {
                String item = " ";
                if (board[r][c] != 0) {
                    item = "" + board[r][c];
                }
                System.out.printf("%6s      |", item);
                //System.out.print("        " + item + "       |");
            }
            System.out.print(" |");
            System.out.println(" |");
            System.out.println("  |                                                       |");
        }
        System.out.println("+-----------------------------------------------------------+");
        System.out.println();
    }


}
