package juego.pieza;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import juego.tablero.Tile;
import juego.tablero.Move;
import juego.tablero.Board;
import juego.tablero.BoardUtils;

/**
 *
 * @author emers
 */
public class Rook extends Piece {

    private final static int[] CANDIDATE_MOVES_VECTOR = { -8, -1, 1, 8 };

    public Rook(PieceColor pieceColor, int piecePosition) {
        super(PieceType.ROOK, pieceColor, piecePosition, true);
    }

    public Rook(PieceColor pieceColor, int piecePosition, boolean isFirstMove) {
        super(PieceType.ROOK, pieceColor, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMovements(final Board tablero) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffSet : CANDIDATE_MOVES_VECTOR) {

            int candidateDestinationCoordinate = this.piecePosition;

            while (BoardUtils.tileIsValid(candidateDestinationCoordinate)) {

                if (isFirstColumnExclusion(this.piecePosition, candidateDestinationCoordinate)
                        || isEighthColumnExclusion(this.piecePosition, candidateDestinationCoordinate)) {
                    break;
                }

                candidateDestinationCoordinate += candidateCoordinateOffSet;

                if (BoardUtils.tileIsValid(candidateDestinationCoordinate)) {
                    final Tile destinationCandidateTile = tablero.getTile(candidateDestinationCoordinate);
                    if (!destinationCandidateTile.tileIsOccupied()) {
                        legalMoves.add(new Move.MajorMove(tablero, this, candidateDestinationCoordinate));
                    } else {
                        final Piece destinationPiece = destinationCandidateTile.getPiece();
                        final PieceColor pieceColor = destinationPiece.getPieceColor();

                        if (this.pieceColor != pieceColor) {
                            legalMoves.add(new Move.AtackMove(tablero, this,
                                    candidateDestinationCoordinate, destinationPiece));
                        }
                    }
                    break;
                }

            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    public String toString() {
        return PieceType.ROOK.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffSet == -1);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffSet == 1);
    }

    public Rook movePiece(final Move movimiento) {
        return new Rook(movimiento.getMovedPiece().getPieceColor(), movimiento.getDestinationCoordinate());
    }
    
}
