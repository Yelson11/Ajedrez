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
import static juego.tablero.BoardUtils.tileIsValid;

/**
 *
 * @author emers
 */
public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVES_VECTOR = { -17, -15, -10, -6, 6, 10, 15, 17 };

    public Knight(final PieceColor pieceColor, final int piecePosition) {
        super(PieceType.KNIGHT, pieceColor, piecePosition, true);
    }

    public Knight(final PieceColor pieceColor, final int piecePosition, final boolean isFirstMove){
        super(PieceType.KNIGHT, pieceColor, piecePosition, isFirstMove);
    }


    @Override
    public List<Move> calculateLegalMovements(final Board board) {

        Collection<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidate : CANDIDATE_MOVES_VECTOR) {

            final int destinationCandidate = this.piecePosition + currentCandidate;

            if (tileIsValid(destinationCandidate)) {
                if (isFirstColumnExclusion(this.piecePosition, currentCandidate)
                        || isSecondColumnExclusion(this.piecePosition, currentCandidate)
                        || isSeventhColumnExclusion(this.piecePosition, currentCandidate)
                        || isEighthColumnExclusion(this.piecePosition, currentCandidate)) {
                    continue;
                }

                final Tile destinationCandidateTile = board.getTile(destinationCandidate);
                if (!destinationCandidateTile.tileIsOccupied()) {
                    legalMoves.add(new MajorMove(board, this, destinationCandidate));
                } else {
                    final Piece destianationPiece = destinationCandidateTile.getPiece();
                    final PieceColor pieceColor = destianationPiece.getPieceColor();

                    if (this.pieceColor != pieceColor) {
                        legalMoves.add(new AtackMove(board, this, destinationCandidate, destianationPiece));
                    }
                }
            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffSet == -17)
                || (candidateOffSet == -10) || (candidateOffSet == 6) || (candidateOffSet == -15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffSet == -10) || (candidateOffSet == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffSet == -6) || (candidateOffSet == 10);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffSet == -15) || (candidateOffSet == -6)
                || (candidateOffSet == 10) || (candidateOffSet == 17);
    }
    
    public Knight movePiece(final Move movimiento) {
        return new Knight(movimiento.getMovedPiece().getPieceColor(), movimiento.getDestinationCoordinate());
    }

}
