/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.jugador.ia;

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
    
}
