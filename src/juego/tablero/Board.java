package juego.tablero;

import juego.jugador.BlacksPlayer;
import juego.jugador.Player;
import juego.jugador.WhitesPlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.HashMap;

import juego.pieza.Bishop;
import juego.pieza.PieceColor;
import juego.pieza.King;
import juego.pieza.Knight;
import juego.pieza.Pawn;
import juego.pieza.Piece;
import juego.pieza.Queen;
import juego.pieza.Rook;

/**
 *
 * @author emers
 */
public class Board {
    private final List<Tile> chessBoard;
    protected final Collection<Piece> whitePieces;
    protected final Collection<Piece> blackPieces;

    private final WhitesPlayer whitesPlayer;
    private final BlacksPlayer blacksPlayer;
    private final Player currentPlayer;

    private Board(BoardBuilder builder) {

        this.chessBoard = createChessBoard(builder);
        this.whitePieces = calculateActivePieces(this.chessBoard, PieceColor.WHITE);
        this.blackPieces = calculateActivePieces(this.chessBoard, PieceColor.BLACK);

        final Collection<Move> movimientosPermitidosBlancas = calculateLegalMoves(this.whitePieces);
        final Collection<Move> movimientosPermitidosNegras = calculateLegalMoves(this.blackPieces);

        this.whitesPlayer = new WhitesPlayer(this, movimientosPermitidosBlancas, movimientosPermitidosNegras);
        this.blacksPlayer = new BlacksPlayer(this, movimientosPermitidosBlancas, movimientosPermitidosNegras);

        this.currentPlayer = builder.nextMoveMaker.escogerJugador (this.whitesPlayer, this.blacksPlayer);

    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Player whitesPlayer() {
        return this.whitesPlayer;
    }

    public Player blacksPlayer() {
        return this.blacksPlayer;
    }

    @Override
    public String toString() {
        final StringBuilder constructorString = new StringBuilder();
        for (int i = 0; i < BoardUtils.TILE_NUMBER; i++) {
            final String textoCasilla = this.chessBoard.get(i).toString();
            constructorString.append(String.format("%3s", textoCasilla));
            if ((i + 1) % BoardUtils.TILE_NUMBER_PER_ROW == 0) {
                constructorString.append("\n");
            }
        }
        return constructorString.toString();
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> piezas) {

        final List<Move> movimientosPermitidos = new ArrayList<>();

        for (final Piece pieza : piezas) {
            movimientosPermitidos.addAll(pieza.calculateLegalMovements(this));
        }

        return ImmutableList.copyOf(movimientosPermitidos);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> chessBoard, final PieceColor color) {
        final List<Piece> piezasActivas = new ArrayList<>();

        for(final Tile tile : chessBoard) {
            if (tile.tileIsOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getPieceColor() == color) {
                    piezasActivas.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(piezasActivas);
    }

    public Tile getTile(final int coordenadaCasilla) {
        return chessBoard.get(coordenadaCasilla);
    }

    private static List<Tile> createChessBoard(final BoardBuilder builder) {
        final Tile[] tiles = new Tile[BoardUtils.TILE_NUMBER];
        for (int i = 0; i < BoardUtils.TILE_NUMBER; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfiguration.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new Rook(PieceColor.BLACK, 0));
        builder.setPiece(new Knight(PieceColor.BLACK, 1));
        builder.setPiece(new Bishop(PieceColor.BLACK, 2));
        builder.setPiece(new Queen(PieceColor.BLACK, 3));
        builder.setPiece(new King(PieceColor.BLACK, 4));
        builder.setPiece(new Bishop(PieceColor.BLACK, 5));
        builder.setPiece(new Knight(PieceColor.BLACK, 6));
        builder.setPiece(new Rook(PieceColor.BLACK, 7));
        builder.setPiece(new Pawn(PieceColor.BLACK, 8));
        builder.setPiece(new Pawn(PieceColor.BLACK, 9));
        builder.setPiece(new Pawn(PieceColor.BLACK, 10));
        builder.setPiece(new Pawn(PieceColor.BLACK, 11));
        builder.setPiece(new Pawn(PieceColor.BLACK, 12));
        builder.setPiece(new Pawn(PieceColor.BLACK, 13));
        builder.setPiece(new Pawn(PieceColor.BLACK, 14));
        builder.setPiece(new Pawn(PieceColor.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(PieceColor.WHITE, 48));
        builder.setPiece(new Pawn(PieceColor.WHITE, 49));
        builder.setPiece(new Pawn(PieceColor.WHITE, 50));
        builder.setPiece(new Pawn(PieceColor.WHITE, 51));
        builder.setPiece(new Pawn(PieceColor.WHITE, 52));
        builder.setPiece(new Pawn(PieceColor.WHITE, 53));
        builder.setPiece(new Pawn(PieceColor.WHITE, 54));
        builder.setPiece(new Pawn(PieceColor.WHITE, 55));
        builder.setPiece(new Rook(PieceColor.WHITE, 56));
        builder.setPiece(new Knight(PieceColor.WHITE, 57));
        builder.setPiece(new Bishop(PieceColor.WHITE, 58));
        builder.setPiece(new Queen(PieceColor.WHITE, 59));
        builder.setPiece(new King(PieceColor.WHITE, 60));
        builder.setPiece(new Bishop(PieceColor.WHITE, 61));
        builder.setPiece(new Knight(PieceColor.WHITE, 62));
        builder.setPiece(new Rook(PieceColor.WHITE, 63));
        // white to move
        builder.setMoveMaker(PieceColor.WHITE);
        // build the board
        return builder.buildBoard();
    }

    Iterable<Move> getLegalMovements() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitesPlayer.getLegalMovements(), this.blacksPlayer.getLegalMovements()));
    }

    public static class BoardBuilder {

        Map<Integer, Piece> boardConfiguration;
        PieceColor nextMoveMaker;
        Pawn peonPasajero;

        public BoardBuilder() {
            this.boardConfiguration = new HashMap<Integer, Piece>();
        }

        public BoardBuilder setPiece(final Piece pieza) {
            this.boardConfiguration.put(pieza.getPiecePosition(), pieza);
            return this;
        }

        public BoardBuilder setMoveMaker(final PieceColor nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board buildBoard() {
            return new Board(this);
        }

        void setPeonPasajero(Pawn peonMovido) {
            this.peonPasajero = peonMovido;
        }

    }

}
