package juego.pieza;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import juego.tablero.Move;
import juego.tablero.Move.MajorMove;
import juego.tablero.Board;
import juego.tablero.BoardUtils;
import juego.tablero.Move.PawnAttackMove;
import juego.tablero.Move.PawnJump;

/**
 *
 * @author emers
 */
public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVES_VECTOR = { 8, 16, 7, 9 };

    public Pawn(PieceColor pieceColor, int piecePosition) {
        super(PieceType.PAWN, pieceColor, piecePosition, true);
    }

    public Pawn(PieceColor pieceColor, int piecePosition, boolean isFirstMove) {
        super(PieceType.PAWN, pieceColor, piecePosition, isFirstMove);
    }
    
    /*
    @Override
    public int locationBonus() {
        return this.pieceColor.pawnBonus(this.piecePosition);
    }*/

    @Override
    public Collection<Move> calculateLegalMovements(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidate : CANDIDATE_MOVES_VECTOR) {
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceColor().getDirection() * currentCandidate);

            if (!BoardUtils.tileIsValid(candidateDestinationCoordinate)) {
                continue;
            }
            
            if (currentCandidate == 8 && board.getTile(candidateDestinationCoordinate).tileIsOccupied()) {
                //ToDo Promotions
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidate == 16 && this.isFirstMove() && 
                    ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceColor().isBlack()) || 
                     (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceColor().isWhite()))) {
                final int behindCandidateDestination = this.piecePosition + (this.pieceColor.getDirection() * 8);
                if (!board.getTile(behindCandidateDestination).tileIsOccupied()
                        && !board.getTile(candidateDestinationCoordinate).tileIsOccupied()) {
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidate == 7 && 
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceColor.isWhite() || 
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isBlack())))) {
                if (board.getTile(candidateDestinationCoordinate).tileIsOccupied()) {
                    final Piece candidatePiece = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceColor != candidatePiece.getPieceColor()) {
                        legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, candidatePiece));
                    }
                }

            } else if (currentCandidate == 9
                    && !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isWhite() || 
                         (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceColor.isBlack())))) {
                if (board.getTile(candidateDestinationCoordinate).tileIsOccupied()) {
                    final Piece candidatePiece = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceColor != candidatePiece.getPieceColor()) {
                        legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, candidatePiece));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    public String toString() {
        return PieceType.PAWN.toString();
    }
    
    public Pawn movePiece(final Move movimiento) {
        return new Pawn(movimiento.getMovedPiece().getPieceColor(), movimiento.getDestinationCoordinate());
    }

}
