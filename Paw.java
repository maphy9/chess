import java.util.List;
import java.util.Scanner;

public final class Paw extends Piece {
    public boolean isWhite;
    public char character;
    public int row;
    public int col;
    public int[] startPos;
    public boolean justDoubled = false;

    public Paw(boolean isWhite, int row, int col) {
        this.isWhite = isWhite;
        this.row = row;
        this.col = col;

        this.startPos = new int[]{col, row};

        if (isWhite) {
            this.character = '♟';
        } else {
            this.character = '♙';
        }
    }

    public void checkEnd(List<Piece> playerPieces, int endRow) {
        if (this.row == endRow) {
            System.out.println("Q - Queen");
            System.out.println("K - Knight");
            System.out.println("B - Bishop");
            System.out.println("R - Rook");
            Scanner scanner = new Scanner(System.in);
            char choice;

            do {
                System.out.print("Chose a piece you want to transform your paw: ");
                choice = scanner.nextLine().toLowerCase().charAt(0);
            } while (choice != 'q' && choice != 'k' && choice != 'b' && choice != 'r');

						scanner.close();

            playerPieces.remove(this);
            switch (choice) {
                case 'q' -> playerPieces.add(new Queen(this.isWhite, this.row, this.col));
                case 'k' -> playerPieces.add(new Knight(this.isWhite, this.row, this.col));
                case 'b' -> playerPieces.add(new Bishop(this.isWhite, this.row, this.col));
                case 'r' -> playerPieces.add(new Rook(this.isWhite, this.row, this.col));
            }
        }
    }

    @Override
    public char getChar() {
        return this.character;
    }

    @Override
    public int[] getPos() {
        return new int[]{this.col, this.row};
    }

    @Override
    public boolean checkMove(char[][] board, Player enemy, Player player, int newCol, int newRow, boolean isMoving, boolean isChecking) {
        if(isChecking || isMoving) {
            if(!validateMove(board, enemy, player, newCol, newRow)) {
                return false;
            }
        }

        if(isMoving) {
            if(player.isChecked) {
                Piece element = null;
                if(enemy.asciiPieces.contains(board[newRow][newCol])) {
                    for(Piece el : enemy.pieces) {
                        if(el.getPos()[0] == newCol && el.getPos()[1] == newRow) {
                            element = el;
                        }
                    }
                }
                char old = board[newRow][newCol];
                Piece.erase(board, this.col, this.row);
                board[newRow][newCol] = this.character;
                for(Piece el : enemy.pieces) {
                    if(el != element && el.checkMove(board, player, enemy, player.king.col, player.king.row, false, false)) {
                        board[newRow][newCol] = old;
                        board[this.row][this.col] = this.character;
                        return false;
                    }
                }
                board[newRow][newCol] = old;
                board[this.row][this.col] = this.character;
            }
        }

        int k = this.isWhite ? -1 : 1;
        int killDoubleRow = this.isWhite ? 4 : 5;


        if (newCol > 0 && newCol < 9 && newRow > 0 && newRow < 9) {
            // going forth
            if (this.col == newCol && !player.asciiPieces.contains(board[this.row + k][this.col]) && !enemy.asciiPieces.contains(board[this.row + k][this.col])) {
                if (newRow == this.row + k) {
                    return true;
                } else if (startPos[0] == this.col && startPos[1] == this.row && newRow == this.row + k * 2 && !player.asciiPieces.contains(board[this.row + 2 * k][this.col]) && !enemy.asciiPieces.contains(board[this.row + 2 * k][this.col])) {
                    this.justDoubled = true;
                    return true;
                }
            } else if ((newCol == this.col + 1 || newCol == this.col - 1) && newRow == this.row + k && enemy.asciiPieces.contains(board[newRow][newCol]) && isMoving) {
                for (Piece el : enemy.pieces) {
                    if (el.getPos()[0] == newCol && el.getPos()[1] == newRow) {
                        Piece.erase(board, newCol, newRow);
                        enemy.pieces.remove(el);
                        break;
                    }
                }
                return true;
            } else if (this.row == killDoubleRow && newRow == this.row + k && newCol == this.col + 1) {
                if (board[killDoubleRow][this.col + 1] == enemy.asciiPieces.get(0)) {
                    for (Piece el : enemy.pieces) {
                        Paw paw = (Paw) el;
                        if (el.getPos()[0] == this.col + 1 && el.getPos()[1] == killDoubleRow && paw.justDoubled && isMoving) {
                            Piece.erase(board, newCol, newRow - k);
                            enemy.pieces.remove(el);
                            return true;
                        }
                    }
                }
            } else if (this.row == killDoubleRow && newRow == this.row + k && newCol == this.col - 1) {
                if (board[killDoubleRow][this.col - 1] == enemy.asciiPieces.get(0)) {
                    for (Piece el : enemy.pieces) {
                        try {
                            Paw paw = (Paw) el;
                            if (el.getPos()[0] == this.col - 1 && el.getPos()[1] == killDoubleRow && paw.justDoubled && isMoving) {
                                Piece.erase(board, newCol, newRow - k);
                                enemy.pieces.remove(el);
                                return true;
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void move(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    @Override
    public boolean validateMove(char[][] board, Player enemy, Player player, int newCol, int newRow) {
        Piece element = null;
        if(enemy.asciiPieces.contains(board[newRow][newCol])) {
            for(Piece el : enemy.pieces) {
                if(el.getPos()[0] == newCol && el.getPos()[1] == newRow) {
                    element = el;
                }
            }
        }
        char old = board[newRow][newCol];
        Piece.erase(board, this.col, this.row);
        board[newRow][newCol] = this.character;
        for(Piece el : enemy.pieces) {
            if(el != element && el.checkMove(board, player, enemy, player.king.col, player.king.row, false, false)) {
                board[newRow][newCol] = old;
                board[this.row][this.col] = this.character;
                return false;
            }
        }
        board[newRow][newCol] = old;
        board[this.row][this.col] = this.character;

        return true;
    }
}
