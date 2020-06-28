package juego.jugador;

import juego.tablero.Move;
import juego.tablero.Board;

public class TransitionMove {

    private final Board tableroDeTransacion;
    private final Move movimiento;
    private final MoveStatus statusMovimiento;

    public TransitionMove(final Board tableroDeTransacion, final Move movimiento,
            final MoveStatus statusMovimiento) {

        this.tableroDeTransacion = tableroDeTransacion;
        this.movimiento = movimiento;
        this.statusMovimiento = statusMovimiento;

    }

    public MoveStatus getMoveStatus() {
        return this.statusMovimiento;
    }
    
    public Board getTablero(){
        return tableroDeTransacion;
    }

}
