public abstract class Piece {
    public abstract char getChar();
    public abstract int[] getPos();

		// check if move is possible
    public abstract boolean checkMove(char[][] board, Player player, Player enemy, int newCol, int newRow, boolean isMoving, boolean isChecking);
    
		public abstract void move(int newRow, int newCol);
    
		// check if move is safe
		public abstract boolean validateMove(char[][] board, Player enemy, Player player, int newCol, int newRow);

		// delete piece from board
    public static void erase(char[][] board, int newCol, int newRow) {
        if(newRow % 2 == 0) {
            if(newCol % 2 == 0) {
                board[newRow][newCol] = '◼';
            } else {
                board[newRow][newCol] = '◻';
            }
        } else {
            if(newCol % 2 == 0) {
                board[newRow][newCol] = '◻';
            } else {
                board[newRow][newCol] = '◼';
            }
        }
    }
}
