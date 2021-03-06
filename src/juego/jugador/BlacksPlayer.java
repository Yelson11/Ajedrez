/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.jugador;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import juego.pieza.PieceColor;
import juego.pieza.Piece;
import juego.pieza.Rook;
import juego.pieza.Piece.PieceType;
import juego.tablero.Tile;
import juego.tablero.Move;
import juego.tablero.Board;

/**
 *
 * @author edamazzio
 */
public class BlacksPlayer extends Player {

    public BlacksPlayer(final Board board, final Collection<Move> whiteStandardLegals,
            final Collection<Move> blackStandardLegals) {
        super(board, blackStandardLegals, whiteStandardLegals);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public PieceColor getPiecesColor() {
        return PieceColor.BLACK;
    }

    @Override
    public Player getOponent() {
        return this.board.whitesPlayer();
    }

    //@Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> reyCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            if(!this.board.getTile(5).tileIsOccupied() && !this.board.getTile(6).tileIsOccupied()){
                final Tile torreCasilla = this.board.getTile(7);
                if(torreCasilla.tileIsOccupied() && torreCasilla.getPiece().isFirstMove()){
                    if(Player.calculateAttackMovesInTile(5, opponentLegals).isEmpty() &&
                       Player.calculateAttackMovesInTile(6, opponentLegals).isEmpty() &&
                       torreCasilla.getPiece().getPieceType().isRook()){
                       reyCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6, (Rook)torreCasilla.getPiece(), torreCasilla.getTileCoordinate(), 5));
                    }
                }
            }
            if (!this.board.getTile(1).tileIsOccupied() &&
                !this.board.getTile(2).tileIsOccupied() &&
                !this.board.getTile(3).tileIsOccupied()){
                
                final Tile torreCasilla = this.board.getTile(0);
                if(torreCasilla.tileIsOccupied() && torreCasilla.getPiece().isFirstMove() && 
                   Player.calculateAttackMovesInTile(2, opponentLegals).isEmpty() && 
                   Player.calculateAttackMovesInTile(3, opponentLegals).isEmpty()  &&
                   torreCasilla.getPiece().getPieceType().isRook()){
                    reyCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2, (Rook)torreCasilla.getPiece(), torreCasilla.getTileCoordinate(), 3));
                }
            }
        }
        return ImmutableList.copyOf(reyCastles);
    }

}
