/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.jugador.ia;

import juego.jugador.TransitionMove;
import juego.tablero.Board;
import juego.tablero.Move;

/**
 *
 * @author Yelson
 */
public class MiniMax implements MoveStrategy{
    
    private final BoardEvaluator boardEvaluator;
    
    public MiniMax(){
        this.boardEvaluator = null;
    }
    
    @Override
    public String toString(){
        return "MiniMax";
    }

    @Override
    public Move execute(Board board, int depth) {
        
        return null;
    }
    
    public int min(final Board board, final int depth){
        if(depth == 0/*|| game over*/){
            return this.boardEvaluator.evaluate(board, depth);
        }
        
        int lowestSeenValue = Integer.MAX_VALUE;
        for(final Move move : board.currentPlayer().getLegalMovements()){
            final TransitionMove moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = max(moveTransition.getTransitionBooard(), depth - 1);
                if(currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }
    
    public int max(final Board board, final int depth){
        if(depth == 0/*|| game over*/){
            return this.boardEvaluator.evaluate(board, depth);
        }
        
        int highestSeenValue = Integer.MIN_VALUE;
        for(final Move move : board.currentPlayer().getLegalMovements()){
            final TransitionMove moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = max(moveTransition.getTransitionBooard(), depth - 1);
                if(currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }
    
}
