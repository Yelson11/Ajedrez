package juego.jugador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import juego.pieza.PieceColor;
import juego.pieza.Piece;
import juego.pieza.King;
import juego.pieza.Piece.PieceType;
import juego.tablero.Move;
import juego.tablero.Board;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board, final Collection<Move> legalMoves,
            final Collection<Move> oponentMoves) {

        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, oponentMoves)));
        this.isInCheck = !Player.calculateAttackMovesInTile(this.playerKing.getPiecePosition(), oponentMoves).isEmpty();
    }

    protected static Collection<Move> calculateAttackMovesInTile(int piecePosition,
            Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : moves) {
            if (piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    public King getPleyerKing(){
        return this.playerKing;
    }

    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType() == PieceType.KING) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Tablero inválido, no se ha encontrado la piece Rey");
    }

    public boolean isMoveLegal(final Move movimiento) {
        return this.legalMoves.contains(movimiento);
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMovements();
    }

    // https://es.wikipedia.org/wiki/Ahogado_(ajedrez)
    public boolean isInStaleMate() {
        return !this.isInCheck() && !hasEscapeMovements();
    }

    // https://es.wikipedia.org/wiki/Enroque
    public boolean isCastled() {
        return false;
    }


    private boolean hasEscapeMovements() {
        for (final Move move : this.legalMoves) {
            final TransitionMove transition = makeMove((move));
            if (transition.getMoveStatus() == Move.MoveStatus.DONE) {
                return true;
            }
        }
        return false;
    }

    public Collection<Move> getLegalMovements() {
        return this.legalMoves;
    }


    public TransitionMove makeMove(final Move move) {

        if (!isMoveLegal((move))){
            return new TransitionMove(this.board, move, Move.MoveStatus.ILLEGAL);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move> attacksToKing = Player.calculateAttackMovesInTile((transitionBoard.currentPlayer().getOponent().getPleyerKing().getPiecePosition()),
             transitionBoard.currentPlayer().getLegalMovements());

        if ( !attacksToKing.isEmpty()  ){
            return new TransitionMove(this.board, move, Move.MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new TransitionMove(transitionBoard, move, Move.MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract PieceColor getPiecesColor();
    public abstract Player getOponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> jugadorLegales, Collection<Move> oponentesLegales);

}