package juego.pieza;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import juego.tablero.Tile;
import juego.tablero.Move;
import juego.tablero.Move.AtackMove;
import juego.tablero.Move.MajorMove;
import juego.tablero.Board;
import juego.tablero.BoardUtils;
import juego.tablero.Move.MajorAttackMove;

/**
 *
 * @author emers
 */
public class King extends Piece {

    private final static int[] CANDIDATE_MOVES_VECTOR = { -9, -8, -7, -1, 1, 7, 8, 9 };

    public King(PieceColor pieceColor, int piecePosition) {
        super(PieceType.KING, pieceColor, piecePosition, true);
    }

    public King(PieceColor pieceColor, int piecePosition, boolean isFirstMove) {
        super(PieceType.KING, pieceColor, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMovements(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidate : CANDIDATE_MOVES_VECTOR) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidate;

            if (isFirstColumnExclusion(this.piecePosition, currentCandidate)
                    || isEighthColumnExclusion(this.piecePosition, currentCandidate)) {
                continue;
            }

            if (BoardUtils.tileIsValid(candidateDestinationCoordinate)) {
                final Tile destinationCandidateTile = board.getTile(candidateDestinationCoordinate);
                if (!destinationCandidateTile.tileIsOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece destinationPiece = destinationCandidateTile.getPiece();
                    final PieceColor pieceColor = destinationPiece.getPieceColor();

                    if (this.pieceColor != pieceColor) {
                        legalMoves
                                .add(new MajorAttackMove(board, this, candidateDestinationCoordinate, destinationPiece));
                    }
                }

            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    public String toString() {
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffSet == -9) || (candidateOffSet == -1)
                || (candidateOffSet == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffSet == -7) || (candidateOffSet == 1)
                || (candidateOffSet == 9);
    }
    
    public King movePiece(final Move movimiento) {
        return new King(movimiento.getMovedPiece().getPieceColor(), movimiento.getDestinationCoordinate());
    }

}
