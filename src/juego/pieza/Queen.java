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
public class Queen extends Piece {

    private final static int[] CANDIDATE_MOVES_VECTOR = { -9, -8, -7, -1, 1, 7, 8, 9 };

    public Queen(PieceColor pieceColor, int piecePosition) {
        super(PieceType.QUEEN, pieceColor, piecePosition, true);
    }

    public Queen(PieceColor pieceColor, int piecePosition, boolean isFirstMove) {
        super(PieceType.QUEEN, pieceColor, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMovements(final Board board ){

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
                    final Tile destinationCandidateTile = board.getTile(candidateDestinationCoordinate);
                    if (!destinationCandidateTile.tileIsOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece destinationPiece = destinationCandidateTile.getPiece();
                        final PieceColor pieceColor = destinationPiece.getPieceColor();

                        if (this.pieceColor != pieceColor) {
                            legalMoves.add(new Move.AtackMove(board, this,
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
        return PieceType.QUEEN.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffSet == -1) || (candidateOffSet == -9)
                || (candidateOffSet == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffSet == -7) || (candidateOffSet == 1)
                || (candidateOffSet == 9);
    }
    
    public Queen movePiece(final Move move) {
        return new Queen(move.getMovedPiece().getPieceColor(), move.getDestinationCoordinate());
    }
}
