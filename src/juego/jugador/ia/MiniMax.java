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
    private final int searchDepth;
    
    public MiniMax(final int depth){
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = depth;
    }
    
    @Override
    public String toString(){
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();
        
        Move bestMove = null;
        
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        
        System.out.println(board.currentPlayer() + "Analizando con profundidad de " + this.searchDepth);
        
        int numMoves = board.currentPlayer().getLegalMovements().size();
        
        for(final Move move : board.currentPlayer().getLegalMovements()){
            final TransitionMove moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                currentValue = board.currentPlayer().getPiecesColor().isWhite()?
                    min(moveTransition.getTransitionBooard(), this.searchDepth - 1) :
                    max(moveTransition.getTransitionBooard(), this.searchDepth - 1);
                if(board.currentPlayer().getPiecesColor().isWhite() && currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                    bestMove = move;
                }else if(board.currentPlayer().getPiecesColor().isBlack() && currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long executionTime = System.currentTimeMillis() - startTime;
        return bestMove;
    }
    
    public static boolean isEndGame(final Board board){
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStaleMate();
    }
    
    public int min(final Board board, final int depth){
        if(depth == 0 || isEndGame(board)){
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
        if(depth == 0 || isEndGame(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }
        
        int highestSeenValue = Integer.MIN_VALUE;
        for(final Move move : board.currentPlayer().getLegalMovements()){
            final TransitionMove moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = min(moveTransition.getTransitionBooard(), depth - 1);
                if(currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }
    
}
