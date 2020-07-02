package juego.jugador.ia;

import juego.tablero.Board;

/**
 *
 * @author Yelson
 */
public interface BoardEvaluator {
    
    int evaluate(Board board, int depth);
    
}
