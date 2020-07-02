package juego.jugador.ia;

import juego.tablero.Board;
import juego.tablero.Move;

/**
 *
 * @author Yelson
 */
public interface MoveStrategy {
    
    Move execute(Board board);
}
