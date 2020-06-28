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

/**
 *
 * @author emers
 */
public class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVES_VECTOR = { -9, -7, 7, 9 };

    public Bishop(PieceColor pieceColor, int piecePosition) {
        super(PieceType.BISHOP, pieceColor, piecePosition, true);
    }

    public Bishop(PieceColor pieceColor, int piecePosition, boolean isFirstMove) {
        super(PieceType.BISHOP, pieceColor, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMovements(final Board board) {

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
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece destinationPiece = destinationCandidateTile.getPiece();
                        final PieceColor pieceColor = destinationPiece.getPieceColor();

                        if (this.pieceColor != pieceColor) {
                            legalMoves
                                    .add(new AtackMove(board, this, candidateDestinationCoordinate, destinationPiece));
                        }
                    }
                    break;
                }

            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    public String toString() {
        return PieceType.BISHOP.toString(this.pieceColor);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffSet == -9) || (candidateOffSet == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffSet == -7) || (candidateOffSet == 9);
    }

    @Override
    public Bishop movePiece(final Move move) {
        return new Bishop(move.getMovedPiece().getPieceColor(), move.getDestinationCoordinate());
    }
}
