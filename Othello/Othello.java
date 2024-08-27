import java.io.*;
import java.util.*;

public class Othello {
    int turn;
    int winner;
    int board[][];
    //add required class variables here

    public Othello(String filename) throws Exception {
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        turn = sc.nextInt();
        board = new int[8][8];
        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j){
                board[i][j] = sc.nextInt();
            }
        }
        winner = -1;
        //Student can choose to add preprocessing here
    }

    //add required helper functions here


    public int boardScore() {
        /* Complete this function to return num_black_tiles - num_white_tiles if turn = 0, 
         * and num_white_tiles-num_black_tiles otherwise. 
        */
        int num_black_tiles = 0;
        int num_white_tiles = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 0) {
                    num_black_tiles++;
                }
                else if (board[i][j] == 1) {
                    num_white_tiles++;
                }
            }
        }
        if (turn == 0) {
            return (num_black_tiles-num_white_tiles);
        }
        else {
            return (num_white_tiles-num_black_tiles);
        }
    }

    public int bestMove(int k) {
        /* Complete this function to build a Minimax tree of depth k (current board being at depth 0),
         * for the current player (siginified by the variable turn), and propagate scores upward to find
         * the best move. If the best move (move with max score at depth 0) is i,j; return i*8+j
         * In case of ties, return the smallest integer value representing the tile with best score.
         * 
         * Note: Do not alter the turn variable in this function, so that the boardScore() is the score
         * for the same player throughout the Minimax tree.
        */
        int[] tilescore = bestMoveHelper(turn, board, 0, k, 0);
        // System.out.println("i"+tilescore[0]);
        // System.out.println("j"+tilescore[1]);
        return tilescore[0]*8+tilescore[1];
    }

    public ArrayList<Integer> fullGame(int k) {
        /* Complete this function to compute and execute the best move for each player starting from
         * the current turn using k-step look-ahead. Accordingly modify the board and the turn
         * at each step. In the end, modify the winner variable as required.
         */
        // System.out.println("turn, k="+turn+","+k);
        // System.out.println("board printing");
        // for (int p = 0; p < board.length; p++) {
        //     for (int q = 0; q < board[p].length; q++) {
        //         System.out.printf("%4d", board[p][q]);
        //     }
        //     System.out.println();
        // }
        // System.out.println("board printed");
        // System.out.println("fullgamecalled");
        ArrayList<Integer> bestMoves = new ArrayList<>();
        while (true) {
            int finalMove = bestMove(k);
            // System.out.println("board printing");
            // for (int p = 0; p < board.length; p++) {
            //     for (int q = 0; q < board[p].length; q++) {
            //         System.out.printf("%4d", board[p][q]);
            //     }
            //     System.out.println();
            // }
            // System.out.println("board printed");
            // System.out.println("finalmove,turn"+finalMove+" "+turn);
            if (finalMove != 72) {
                bestMoves.add(finalMove);
                boardChanger(finalMove);
            }
            else {
                if (turn == 0) {
                    turn = 1;
                }
                else {
                    turn = 0;
                }
                finalMove = bestMove(k);
                if (finalMove == 72) {
                    if (turn == 0) {
                        turn = 1;
                    }
                    else {
                        turn = 0;
                    }
                    break;
                }
                else {
                    bestMoves.add(finalMove);
                    boardChanger(finalMove);
                }
            }
        }
        
        int boardScore = boardScore();
        // System.out.println(boardScore);
        // System.out.println(turn);
        if (boardScore > 0) {
            winner = turn;
        }
        else if (boardScore < 0) {
            if (turn == 0) {
                winner = 1;
            }
            else {
                winner = 0;
            }
        }
        // System.out.println("winner"+winner);
        // System.out.println("final board");
        // for (int p = 0; p < board.length; p++) {
        //     for (int q = 0; q < board[p].length; q++) {
        //         System.out.printf("%4d", board[p][q]);
        //     }
        //     System.out.println();
        // }
        // System.out.println("board printed");
        // System.out.println("fullgameended");
        return bestMoves;
    }

    public int[][] getBoardCopy() {
        // System.out.println("callinggetboardcopy");
        int copy[][] = new int[8][8];
        for(int i = 0; i < 8; ++i)
            System.arraycopy(board[i], 0, copy[i], 0, 8);
        return copy;
    }

    public int getWinner() {
        // System.out.println("callinggetwinner");
        return winner;
    }

    public int getTurn() {
        // System.out.println("callinggetturn");
        return turn;
    }

    private int[] bestMoveHelper(int currTurn, int[][]currBoard, int level, int k, int delScore) {
        // System.out.println("bestmovehelper:->currturn,level="+currTurn+","+level);
        int[] tilescore = new int[3];
        boolean upperSwitch = false;
        // tilescore[2] = -1;
        tilescore[0] = 8;
        tilescore[1] = 8;
        tilescore[2] = delScore;
        int check;
        if (currTurn == 0) {
            check = 1;
        }
        else {
            check = 0;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (currBoard[i][j] == -1) {
                    // if (currBoard[i+1][j] == check) {
                    //     for (int k = i+2; k <=8; k++) {
                    //         if (currBoard[k][j] == turn) {
                    //             for (int l = i+1; l <= k-1; l++) {
                    //                 currBoard[l][j] = turn;
                    //             }
                    //         }
                    //         else if (currBoard[k][j] == -1) {
                    //             break;
                    //         }
                    //         else {}
                    //     }
                    // }
                    int[][] copyofcurrBoard = getBoardCopy(currBoard);
                    int[] temp = new int[3];
                    temp[0] = i;
                    temp[1] = j;
                    temp[2] = delScore;
                    boolean Switch = false;
                    for (int dirX = -1; dirX <= 1; dirX++) {
                        for (int dirY = -1; dirY <= 1; dirY++) {
                            if (!(dirX == 0 && dirY == 0)) {
                                if (i+dirX<=7 && i+dirX>=0 && j+dirY<=7 && j+dirY>=0 && (currBoard[i+dirX][j+dirY] == check)) {
                                    // System.out.println("vacantij "+i+","+j+" direction"+dirX+","+dirY);
                                    for (int m = i+2*dirX, n = j+2*dirY; m<=7 && m>=0 && n<=7 && n>=0; m+=dirX, n+=dirY) {
                                        if (copyofcurrBoard[m][n] == currTurn) {
                                            // System.out.println("finding same as turn"+m+","+n);
                                            Switch = true;
                                            //upperSwitch = true;
                                            for (int K = i, L = j; ((dirX >= 0 && K<=m-dirX) || (dirX < 0 && K>=m-dirX)) && ((dirY >= 0 && L<=n-dirY) || (dirY < 0 && L>=n-dirY)); K+=dirX, L+=dirY) {
                                                // System.out.println("turning"+K+","+L);
                                                copyofcurrBoard[K][L] = currTurn;
                                                temp[2]+=2;
                                                // System.out.println("board printing");
                                                // for (int p = 0; p < copyofcurrBoard.length; p++) {
                                                //     for (int q = 0; q < copyofcurrBoard[p].length; q++) {
                                                //         System.out.printf("%4d", copyofcurrBoard[p][q]);
                                                //     }
                                                //     System.out.println();
                                                // }
                                                // System.out.println("board printed");
                                                // tilescore[2]++;                                  
                                            }
                                            temp[2]-=2;
                                            break;
                                        }
                                        else if (copyofcurrBoard[m][n] == -1) {
                                            // System.out.println("not valid");
                                            break;
                                        }
                                        else {}
                                    }
                                }
                            }
                        }
                    }                    
                    if (Switch == true) {
                        temp[2]++;
                        if (level == k-1) {
                            if (upperSwitch == false) {
                                tilescore = temp;
                                upperSwitch = true;
                            }
                            else if (temp[2] > tilescore[2]) {
                                tilescore = temp;
                                // System.out.println("tilescorechange"+Arrays.toString(tilescore));
                            }
                            else if (temp[2] == tilescore[2]) {
                                if ((tilescore[0]*8+tilescore[1]) > (temp[0]*8+temp[1])) {
                                    tilescore = temp;
                                    // System.out.println("tile change"+Arrays.toString(tilescore));
                                }
                            }
                        }
                        else if (level < k-1) {
                            temp = bestMoveHelper(check, copyofcurrBoard, level+1, k, -temp[2]);
                            if (upperSwitch == false) {
                                tilescore[2] = temp[2];
                                tilescore[0] = i;
                                tilescore[1] = j;
                                upperSwitch = true;
                            }
                            else if (temp[2] > tilescore[2]) {
                                tilescore[2] = temp[2];
                                tilescore[0] = i;
                                tilescore[1] = j;
                                // System.out.println("tilescorechange levelless"+Arrays.toString(tilescore));
                            }
                            else if (temp[2] == tilescore[2]) {
                                if ((tilescore[0]*8+tilescore[1]) > (i*8+j)) {
                                    tilescore[0] = i;
                                    tilescore[1] = j;
                                    // System.out.println("tile change levelless"+Arrays.toString(tilescore));
                                }
                            }
                        }
                        // else {
                        //     System.out.println("scoreless or ijmore");
                        // }
                        // else if (level == k-1) {
                        // }
                        // if (temp[2] > tilescore[2]) {
                        //     tilescore[2] = temp[2];
                        //     System.out.println("tile2=temp2"+tilescore[2]);
                        // }
                        // else if (level%2 != 0 && (tilescore[2] == 0 || tilescore[2] > temp[2])) {
                        //     tilescore[2] = temp[2];
                        // }
                        // else if (level == 0 && temp[2] == tilescore[2]) {
                        //     if ((tilescore[0]*8+tilescore[1]) > (temp[0]*8+temp[1])) {
                        //         tilescore = temp;
                        //         System.out.println("tile=temp");
                        //         // System.out.println(Arrays.toString(tilescore));
                        //     }
                        //     else {
                        //         System.out.println("previousij");
                        //     }
                        // }
                        // else {
                        //     System.out.println("scoreless");
                        // }
                    }
                }
            }
        }
        if (upperSwitch == false && level < k-1) {
            // System.out.println("uSFALSE");
            tilescore[2] = bestMoveHelper(check, currBoard, level+1, k, -delScore)[2];
        }
        // System.out.println(Arrays.toString(tilescore));
        tilescore[2] = -tilescore[2];
        // System.out.println("helperfinish level="+level);
        return tilescore;
    }

    private void boardChanger(int finalMove) {
        int i = finalMove/8;
        int j = finalMove - 8*i;
        int check;
        if (turn == 0) {
            check = 1;
        }
        else {
            check = 0;
        }
        //System.out.println("ij="+i+j);
        for (int dirX = -1; dirX <= 1; dirX++) {
            for (int dirY = -1; dirY <= 1; dirY++) {
                //System.out.println("dirxy="+dirX+dirY);
                if (!(dirX == 0 && dirY == 0)) {
                    if (i+dirX<=7 && i+dirX>=0 && j+dirY<=7 && j+dirY>=0 && (board[i+dirX][j+dirY] == check)) {
                        for (int m = i+2*dirX, n = j+2*dirY; m<=7 && m>=0 && n<=7 && n>=0; m+=dirX, n+=dirY) {
                            if (board[m][n] == turn) {
                                for (int K = i, L = j; ((dirX >= 0 && K<=m-dirX) || (dirX < 0 && K>=m-dirX)) && ((dirY >= 0 && L<=n-dirY) || (dirY < 0 && L>=n-dirY)); K+=dirX, L+=dirY) {
                                    //System.out.println("turningmn="+m+n);
                                    board[K][L] = turn;                                              
                                }
                                break;
                            }
                            else if (board[m][n] == -1) {
                                // System.out.println("not valid");
                                break;
                            }
                            else {}
                        }
                    }
                }
            }
        }
        turn = check;

        // System.out.println("board printing");
        // for (int p = 0; p < board.length; p++) {
        //     for (int q = 0; q < board[p].length; q++) {
        //         System.out.printf("%4d", board[p][q]);
        //     }
        //     System.out.println();
        // }
        // System.out.println("board printed");
    }

    private int[][] getBoardCopy(int[][] currBoard) {
        int copy[][] = new int[8][8];
        for(int i = 0; i < 8; ++i)
            System.arraycopy(currBoard[i], 0, copy[i], 0, 8);
        return copy;
    }
    
    // private Object[] validMoves(int currTurn, int[][]currBoard, int i, int j, int dirX, int dirY) {
    //     int tilescore[2] = 0;
    //     int check;
    //     if (currTurn == 0) {
    //         check = 1;
    //     }
    //     else {
    //         check = 0;
    //     }

    //     if (currBoard[i+dirX][j+dirY] == check) {
    //         for (int m = i+2*dirX, n = j+2*dirY; m<=7 && m>=0 && n<=7 && n>=0; m+=dirX, n+=dirY) {
    //             if (currBoard[m][n] == turn) {
    //                 for (int K = i, L = j; K<=m-1 && K>=0 && L<=n-1 && L>=0; K+=dirX, L+=dirY) {
    //                     currBoard[K][L] = turn;
    //                     tilescore[2]++;
    //                 }
    //                 Object[] collection = {currBoard, tilescore[2]};
    //                 return collection;
    //             }
    //             else if (currBoard[m][n] == -1) {
    //                 return null;
    //             }
    //             else {}
    //         }
    //     }
    //     return null;
    // }

    public static void main(String[] args) throws Exception {
        Othello oth = new Othello("C:\\Users\\Vayun\\Downloads\\4th sem\\COL106\\COL106 assignments\\submissions\\A6_starter_code\\input4.txt");
        System.out.println(oth.boardScore());
        System.out.println(oth.bestMove(2));
        // System.out.println(oth.fullGame(4).toString());
        // System.out.println(oth.getTurn());
        // System.out.println(oth.getWinner());
        // System.out.println(oth.getBoardCopy().toString());
    }
}