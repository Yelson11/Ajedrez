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
import juego.tablero.Tile;
import juego.tablero.Move;
import juego.tablero.Board;

/**
 *
 * @author edamazzio
 */
public class WhitesPlayer extends Player {

    public WhitesPlayer(final Board board, final Collection<Move> whitesAllowedMoves, final Collection<Move> blackAllowedMoves) {
        super(board, whitesAllowedMoves, blackAllowedMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public PieceColor getPieceColor() {
        return PieceColor.WHITE;
    }

    @Override
    public Player getOponent() {
        return this.board.blacksPlayer();
    }

    //@Override
    protected Collection<Move> calcularReyCastles(final Collection<Move> jugadorLegales, final Collection<Move> oponentesLegales){
        
        final List<Move> reyCastles = new ArrayList();
        
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            //Rey Castle
            if(!this.board.getTile(61).tileIsOccupied() && !this.board.getTile(62).tileIsOccupied()){
                final Tile torreCasilla = this.board.getTile(63);
                
                if(torreCasilla.tileIsOccupied() && torreCasilla.getPiece().isFirstMove()){
                    if(Player.calculateAttackMovesInTile(61, oponentesLegales).isEmpty() &&
                       Player.calculateAttackMovesInTile(62, oponentesLegales).isEmpty() &&
                       torreCasilla.getPiece().getPieceType().isRook()){
                        reyCastles.add(new Move.MovimientoCastilloLadoRey(this.board, this.playerKing, 62, (Rook)torreCasilla.getPiece(), torreCasilla.getTileCoordinate(),61));
                    }
                }
            }
            if(!this.board.getTile(59).tileIsOccupied() && 
               !this.board.getTile(58).tileIsOccupied() && 
               !this.board.getTile(57).tileIsOccupied()){
                
                final Tile torreCasilla = this.board.getTile(56);
                if(torreCasilla.tileIsOccupied() && torreCasilla.getPiece().isFirstMove() &&
                    Player.calculateAttackMovesInTile(58, oponentesLegales).isEmpty() && 
                    Player.calculateAttackMovesInTile(59, oponentesLegales).isEmpty()  &&
                    torreCasilla.getPiece().getPieceType().isRook()){
                    reyCastles.add(new Move.MovimientoCastilloLadoReina(this.board, this.playerKing, 58, (Rook)torreCasilla.getPiece(), torreCasilla.getTileCoordinate(), 59));
                }
            }
        }
        
        return ImmutableList.copyOf(reyCastles);
    }

}
