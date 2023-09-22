public final class Rook extends Piece {
    public char character;
    public int row;
    public int col;
    public boolean hasMoved = false;

    public Rook(boolean isWhite, int row, int col) {
        this.row = row;
        this.col = col;

        if(isWhite) {
            this.character = '♜';
        } else {
            this.character = '♖';
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

        if(this.row == newRow) {
            int k = this.col > newCol ? -1 : 1;

            for(int i = 1; i < Math.abs(this.col - newCol); i++) {
                if(enemy.asciiPieces.contains(board[this.row][this.col + k * i]) || player.asciiPieces.contains(board[this.row][this.col + k * i])) {
                    return false;
                }
            }
        } else if (this.col == newCol) {
            int k = this.row > newRow ? -1 : 1;

            for(int i = 1; i < Math.abs(this.row - newRow); i++) {
                if(enemy.asciiPieces.contains(board[this.row + k * i][this.col]) || player.asciiPieces.contains(board[this.row + k * i][this.col])) {
                    return false;
                }
            }
        } else {
            return false;
        }

        if(player.asciiPieces.contains(board[newRow][newCol])) {
            return false;
        }

        if(enemy.asciiPieces.contains(board[newRow][newCol])) {
            for (Piece el : enemy.pieces) {
                if (el.getPos()[0] == newCol && el.getPos()[1] == newRow && isMoving) {
                    Piece.erase(board, newCol, newRow);
                    enemy.pieces.remove(el);
                    break;
                }
            }
        }

        this.hasMoved = true;
        return true;
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