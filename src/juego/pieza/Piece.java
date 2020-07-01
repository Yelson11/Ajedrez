package juego.pieza;

import java.util.Collection;
import juego.tablero.Move;
import juego.tablero.Board;

/**
 *
 * @author emers
 */
public abstract class Piece {
    final PieceType pieceType;
    protected final PieceColor pieceColor;
    protected final int piecePosition;
    protected final boolean isFirstMove;
    private final int hashCode;

    Piece(final PieceType pieceType, final PieceColor pieceColor, final int piecePosition, final boolean isFirstMove) {
        this.piecePosition = piecePosition;
        this.pieceColor = pieceColor;
        this.isFirstMove = isFirstMove;
        this.pieceType = pieceType;
        this.hashCode = computeHashCode();
    }
    
    private int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceColor.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }
    
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
               pieceColor == otherPiece.getPieceColor() && isFirstMove == otherPiece.isFirstMove();
    }
    
    @Override
    public int hashCode(){
     return this.hashCode;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public PieceColor getPieceColor() {
        return this.pieceColor;
    } 

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public abstract Collection<Move> calculateLegalMovements(final Board tablero);
    
    public abstract Piece movePiece(Move movimiento);

    public enum PieceType {
        BISHOP("B"){ //BISOHP
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
            
        }, 
        KING("K"){ //KING
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        }, 
        KNIGHT("N"){  //kNight
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        }, 
        QUEEN("Q"){ //QUEEN
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        }, 
        ROOK("R"){ //ROOK
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        }, 
        PAWN("P"){ //PAWN
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;

        public String toString(PieceColor color) {
            return this.pieceName;
        }

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }
        
        public abstract boolean isKing();
        public abstract boolean isRook();
        
    }

}
