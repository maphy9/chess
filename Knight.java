public final class Knight extends Piece {
    public char character;
    public int row;
    public int col;

    public Knight(boolean isWhite, int row, int col) {
        this.row = row;
        this.col = col;

        if(isWhite) {
            this.character = '♞';
        } else {
            this.character = '♘';
        }
    }

    @Override
    public char getChar() {
        return this.character;
    }
    @Override
    public int[] getPos() {
        return new int[] {this.col, this.row};
    }

    @Override
    public boolean checkMove(char[][] board, Player enemy, Player player, int newCol, int newRow, boolean isMoving, boolean isChecking) {
        if(isMoving || isChecking) {
            if(!validateMove(board, enemy, player, newCol, newRow)) {
                return false;
            }
        }

        if(newCol > 0 && newCol < 9 && newRow > 0 && newRow < 9 && !player.asciiPieces.contains(board[newRow][newCol])) {
          boolean isValidMove = (newCol == this.col + 1 && newRow == this.row + 2) ||
                    (newCol == this.col - 1 && newRow == this.row + 2) ||
                    (newCol == this.col + 2 && newRow == this.row + 1) ||
                    (newCol == this.col + 2 && newRow == this.row - 1) ||
                    (newCol == this.col - 2 && newRow == this.row + 1) ||
                    (newCol == this.col - 2 && newRow == this.row - 1) ||
                    (newCol == this.col + 1 && newRow == this.row - 2) ||
                    (newCol == this.col - 1 && newRow == this.row - 2);
					if(enemy.asciiPieces.contains(board[newRow][newCol]) && isMoving && isValidMove) {
                for(Piece el : enemy.pieces) {
                    if(el.getPos()[0] == newCol && el.getPos()[1] == newRow) {
												Piece.erase(board, newCol, newRow);
                        enemy.pieces.remove(el);
                        break;
                    }
                }
            }
            return isValidMove;
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
