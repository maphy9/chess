public final class King extends Piece {
    public char character;
    public int row;
    public int col;
    public boolean hasMoved = false;

    public King(boolean isWhite, int row, int col) {
        this.row = row;
        this.col = col;

        if(isWhite) {
            this.character = '♚';
        } else {
            this.character = '♔';
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

            for(Piece el : enemy.pieces) {
                if(el.checkMove(board, player, enemy, newCol, newRow, false, false)) {
                    return false;
                }
            }
        }

        if (
                (newCol == this.col && newRow == this.row + 1) ||
                (newCol == this.col && newRow == this.row - 1) ||
                (newCol == this.col + 1 && newRow == this.row) ||
                (newCol == this.col - 1 && newRow == this.row) ||
                (newCol == this.col + 1 && newRow == this.row + 1) ||
                (newCol == this.col - 1 && newRow == this.row + 1) ||
                (newCol == this.col + 1 && newRow == this.row - 1) ||
                (newCol == this.col - 1 && newRow == this.row - 1)
        ) {
            if(player.asciiPieces.contains(board[newRow][newCol])) {
                return false;
            }

            if(enemy.asciiPieces.contains(board[newRow][newCol]) && isMoving) {
                Piece.erase(board, newCol, newRow);
                for(Piece el : enemy.pieces) {
                    if(el.getPos()[0] == newCol && el.getPos()[1] == newRow) {
                        enemy.pieces.remove(el);
                        break;
                    }
                }
            }

            this.hasMoved = true;
            return true;
        } else if(!player.isChecked && !this.hasMoved && (newCol == 3 || newCol == 7) && newRow == this.row) {
            int rookCol = this.col > newCol ? 1 : 8;
            if(board[newRow][rookCol] == player.asciiPieces.get(3)) {
                int k = this.col > newCol ? -1 : 1;
                Rook rook = null;
                for(Piece el : player.pieces) {
                    if(el.getPos()[0] == rookCol && el.getPos()[1] == newRow) {
                        rook = (Rook) el;
                    }
                }
                assert rook != null;
                if(rook.hasMoved) {return false;}

                for(int i = 0; i < Math.abs(this.col - rookCol) - 1; i++) {
                    if(player.asciiPieces.contains(board[this.row][this.col + k * (i + 1)]) || enemy.asciiPieces.contains(board[this.row][this.col + k * (i + 1)])) {
                        return false;
                    }
                }

                for(Piece el : enemy.pieces) {
                    if(el.checkMove(board, player, enemy, this.col + 2 * k, this.row, false, false)) {
                        return false;
                    }
                }

                this.hasMoved = true;
                rook.hasMoved = true;
                this.col = newCol;
                rook.col = this.col - k;
                return true;
            }
        }

        return  false;
    }

    @Override
    public void move(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    @Override
    public boolean validateMove(char[][] board, Player enemy, Player player, int newCol, int newRow) {
        int oldRow = this.row;
        int oldCol = this.col;
        if(enemy.asciiPieces.contains(board[newRow][newCol])) {
            Piece element = null;
            for(Piece el : enemy.pieces) {
                if(el.getPos()[0] == newCol && el.getPos()[1] == newRow) {
                    element = el;
                }
            }

            char old = board[newRow][newCol];
            Piece.erase(board, this.col, this.row);
            board[newRow][newCol] = this.character;
            this.row = newRow;
            this.col = newCol;
            for(Piece el : enemy.pieces) {
                if(el != element && el.checkMove(board, player, enemy, player.king.col, player.king.row, false, false)) {
                    board[newRow][newCol] = old;
                    board[oldRow][oldCol] = this.character;
                    this.row = oldRow;
                    this.col = oldCol;
                    return false;
                }
            }
            board[newRow][newCol] = old;
            board[oldRow][oldCol] = this.character;
            this.row = oldRow;
            this.col = oldCol;
            return true;
        } else {
            return this.checkMove(board, enemy, player, newCol, newRow, false, false);
        }
    }
}

