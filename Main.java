public class Main {
    public static void main(String[] args) {
        Board gameBoard = new Board();
        Player white = new Player(true, gameBoard.board);
        Player black = new Player(false, gameBoard.board);

        while(true) {
            boolean whiteMove;
            boolean blackMove;

            do {
                if(white.isChecked) {
                    System.out.println("Check");
                }
                gameBoard.drawBoard();
                whiteMove = white.moveAPiece(gameBoard.board, black);
            } while(!whiteMove || white.isChecked);
            if(checkDraw1(white, black) || checkDraw2(gameBoard.board, black, white)) {
                gameBoard.drawBoard();
                System.out.println("DRAW!");
                break;
            }
            if(checkWin(gameBoard.board, white, black)) {
                gameBoard.drawBoard();
                System.out.println("White won!");
                break;
            }
            do {
                if(black.isChecked) {
                    System.out.println("Check");
                }
                gameBoard.drawBoard();
                blackMove = black.moveAPiece(gameBoard.board, white);
            } while(!blackMove || black.isChecked);
            if(checkDraw1(white, black) || checkDraw2(gameBoard.board, white, black)) {
                gameBoard.drawBoard();
                System.out.println("DRAW!");
                break;
            }
            if(checkWin(gameBoard.board, black, white)) {
                gameBoard.drawBoard();
                System.out.println("Black won!");
                break;
            }
        }
    }

    public static boolean checkDraw1(Player white, Player black) {
        return white.pieces.size() == 1 && black.pieces.size() == 1;
    }

    public static boolean checkDraw2(char[][] board, Player player, Player enemy) {
        if(!player.isChecked && player.pieces.size() == 1) {
            return !player.king.checkMove(board, enemy, player, player.king.col + 1, player.king.row, false, false) &&
                !player.king.checkMove(board, enemy, player, player.king.col - 1, player.king.row, false, false) &&
                !player.king.checkMove(board, enemy, player, player.king.col, player.king.row + 1, false, false) &&
                !player.king.checkMove(board, enemy, player, player.king.col, player.king.row - 1, false, false) &&
                !player.king.checkMove(board, enemy, player, player.king.col + 1, player.king.row + 1, false, false) &&
                !player.king.checkMove(board, enemy, player, player.king.col - 1, player.king.row + 1, false, false) &&
                !player.king.checkMove(board, enemy, player, player.king.col + 1, player.king.row - 1, false, false) &&
                !player.king.checkMove(board, enemy, player, player.king.col - 1, player.king.row - 1, false, false);
        }

        return false;
    }

    public static boolean checkWin(char[][] board, Player player, Player enemy) {
        if(enemy.isChecked) {
            for(Piece el : enemy.pieces) {
                for(int i = 1; i < 9; i++) {
                    for(int k = 1; k < 9; k++) {
                        if(el.checkMove(board, player, enemy, i, k, false, true)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        return false;
    }
}
