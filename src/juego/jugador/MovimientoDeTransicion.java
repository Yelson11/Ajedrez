package juego.jugador;

import juego.tablero.Movimiento;
import juego.tablero.Tablero;

public class MovimientoDeTransicion {

    private final Tablero tableroDeTransacion;
    private final Movimiento movimiento;
    private final StatusMovimiento statusMovimiento;

    public MovimientoDeTransicion(final Tablero tableroDeTransacion, final Movimiento movimiento,
            final StatusMovimiento statusMovimiento) {

        this.tableroDeTransacion = tableroDeTransacion;
        this.movimiento = movimiento;
        this.statusMovimiento = statusMovimiento;

    }

    public StatusMovimiento getStatusMovimiento() {
        return this.statusMovimiento;
    }

}
