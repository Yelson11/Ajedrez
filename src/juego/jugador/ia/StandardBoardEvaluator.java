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
    
    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;

    public StandardBoardEvaluator() {
    }

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.whitesPlayer(), depth) - scorePlayer(board, board.blacksPlayer(), depth) ;
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return pieceValue(player) + mobility(player) + check(player) + checkmate(player, depth) + castled(player);
        // + checkmate, check, castled, mobility...
    }
    
    private int mobility(final Player player){
        return player.getLegalMovements().size();
    }
    
    private static int castled(final Player player){
        return player.isCastled() ? CASTLE_BONUS : 0;
    }
    
    private static int check(Player player) {
        return player.getOponent().isInCheck() ? CHECK_BONUS : 0;
    }
    
    private int checkmate(Player player, int depth) {
        return player.getOponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }
    
    private static int depthBonus(int depth){
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    
    private static int pieceValue(final Player player){
        int piecesValueScore = 0;
        for(final Piece piece : player.getActivePieces()){
            piecesValueScore += piece.getPieceValue();
        }
        return piecesValueScore;
    }
    
}
