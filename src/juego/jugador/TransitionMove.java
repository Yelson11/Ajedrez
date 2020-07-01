package juego.jugador;

import juego.tablero.Move;
import juego.tablero.Board;

public class TransitionMove {

    private final Board transitionBoard;
    private final Move move;
    private final Move.MoveStatus statusMovimiento;

    public TransitionMove(final Board tableroDeTransacion, final Move movimiento,
            final Move.MoveStatus statusMovimiento) {

        this.transitionBoard = tableroDeTransacion;
        this.move = movimiento;
        this.statusMovimiento = statusMovimiento;

    }

    public Move.MoveStatus getMoveStatus() {
        return this.statusMovimiento;
    }
    
    public Board getTransitionBooard(){
        return transitionBoard;
    }

}
