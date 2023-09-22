import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

public final class Player {
    public boolean isWhite;
    public List<Piece> pieces = new ArrayList<>();
    public List<Character> asciiPieces;
    private int[] friendlyRows;
    boolean isChecked = false;
    King king;

		// creating Player
    public Player(boolean isWhite, char[][] board) {
        this.isWhite = isWhite;
        if(this.isWhite) {
            this.friendlyRows = new int[] {8, 7};
        } else {
            this.friendlyRows = new int[]{1, 2};
        }

        if(isWhite) {
            this.asciiPieces = Arrays.asList('♟', '♞', '♝', '♜', '♛', '♚');
        } else {
            this.asciiPieces = Arrays.asList('♙', '♘', '♗', '♖', '♕', '♔');
        }

        // adding pieces
        for(int i = 0; i < 8; i++) {
            this.pieces.add(new Paw(isWhite, friendlyRows[1], i + 1));
        }
        this.pieces.add(new Rook(isWhite, friendlyRows[0], 1));
        this.pieces.add(new Knight(isWhite, friendlyRows[0], 2));
        this.pieces.add(new Bishop(isWhite, friendlyRows[0], 3));
        this.pieces.add(new Queen(isWhite, friendlyRows[0], 4));
        this.pieces.add(new King(isWhite, friendlyRows[0], 5));
        king = (King) pieces.get(pieces.size() - 1);
        this.pieces.add(new Bishop(isWhite, friendlyRows[0], 6));
        this.pieces.add(new Knight(isWhite, friendlyRows[0], 7));
        this.pieces.add(new Rook(isWhite, friendlyRows[0], 8));

        this.drawPlayerPieces(board);
    }


		// update playerPieces positions
    public void drawPlayerPieces(char[][] board) {
        for (Piece piece : this.pieces) {
            int[] pos = piece.getPos();
            char character = piece.getChar();

            board[pos[1]][pos[0]] = character;
        }
    }


		// 
    public boolean moveAPiece(char[][] board, Player enemy) {
        for(Piece el : this.pieces) {
            if(el.getClass() == Paw.class) {
                ((Paw) el).justDoubled = false;
            }
        }

        System.out.println("It's a " + (this.isWhite ? "WHITE" : "BLACK") + " player's move!");
        Scanner scanner = new Scanner(System.in);
        String selectedSquare;
        int selectedRow;
        int selectedCol;
        Piece selectedPiece = null;

        do {
            selectedSquare = "";
            while(selectedSquare.length() != 2) {
                System.out.print("Choose a piece to move (eg B2): ");
                selectedSquare = scanner.nextLine().toLowerCase();
            }
            selectedCol = ((int) selectedSquare.charAt(0)) - 96;
            selectedRow = Character.getNumericValue(selectedSquare.charAt(1));
        } while(!(selectedCol > 0 && selectedCol < 9) || !(selectedRow > 0 && selectedRow < 9) || !this.asciiPieces.contains(board[selectedRow][selectedCol]));

        // getting the piece
        for(Piece el : this.pieces) {
            if(el.getPos()[0] == selectedCol && el.getPos()[1] == selectedRow) {
                selectedPiece = el;
                break;
            }
        }

        String newSquare;
        int newRow;
        int newCol;
        boolean canGo;

        do {
            do {
                newSquare = "";
                while(newSquare.length() != 2) {
                    System.out.println("If you are stuck type 'R'");
                    System.out.print("Choose a square where you want to move the piece (eg D5): ");
                    newSquare = scanner.nextLine().toLowerCase();
                    if(newSquare.equals("r")) {
											return false;
                    }
                }
                newCol = ((int) newSquare.charAt(0)) - 96;
                newRow = Character.getNumericValue(newSquare.charAt(1));
            } while(!(newCol > 0 && newCol < 9) || !(newRow > 0 && newRow < 9));
            assert selectedPiece != null;
            canGo = selectedPiece.checkMove(board, enemy, this, newCol, newRow, true, false);
        } while(!canGo);

        selectedPiece.move(newRow, newCol);

        if(selectedPiece.getClass() == Paw.class) {
            ((Paw) selectedPiece).checkEnd(this.pieces, isWhite ? 1 : 8);
        }

        this.drawPlayerPieces(board);

        if(selectedRow % 2 == 0) {
            if(selectedCol % 2 == 0) {
                board[selectedRow][selectedCol] = '◼';
            } else {
                board[selectedRow][selectedCol] = '◻';
            }
        } else {
            if(selectedCol % 2 == 0) {
                board[selectedRow][selectedCol] = '◻';
            } else {
                board[selectedRow][selectedCol] = '◼';
            }
        }

        // is player checking
        for(Piece el : this.pieces) {
            if(el.checkMove(board, enemy, this, enemy.king.getPos()[0], enemy.king.getPos()[1], false, false)) {
                enemy.isChecked = true;
                break;
            } else {
                enemy.isChecked = false;
            }
        }

        // is player checked
        for(Piece el : enemy.pieces) {
            if(el.checkMove(board, this, enemy, this.king.getPos()[0], this.king.getPos()[1], false, false)) {
                this.isChecked = true;
                break;
            } else {
                this.isChecked = false;
            }
        }
				
        return true;
    }
}
