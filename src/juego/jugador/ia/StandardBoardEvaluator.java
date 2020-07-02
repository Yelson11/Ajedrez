/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.jugador.ia;

import juego.jugador.Player;
import juego.pieza.Piece;
import juego.tablero.Board;

/**
 *
 * @author Yelson
 */
public class StandardBoardEvaluator implements BoardEvaluator {

    public StandardBoardEvaluator() {
    }

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.whitesPlayer(), depth) - scorePlayer(board, board.blacksPlayer(), depth) ;
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return pieceValue(player);
        // + checkmate, check, castled, mobility...
    }
    
    private static int pieceValue(final Player player){
        int piecesValueScore = 0;
        for(final Piece piece : player.getActivePieces()){
            piecesValueScore += piece.getPieceValue();
        }
        return piecesValueScore;
    }

    
}
