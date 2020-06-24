package juego.pieza;

import juego.jugador.Jugador;
import juego.jugador.JugadorBlancas;
import juego.jugador.JugadorNegras;

/**
 *
 * @author emers
 */
public enum ColorPieza {

    BLANCO {
        @Override
        public int getDireccion() {
            return -1;
        }

        @Override
        public boolean esNegra() {
            return false;
        }

        @Override
        public boolean esBlanca() {
            return true;
        }

        @Override
        public Jugador escogerJugador(final JugadorBlancas jugadorBlancas, final JugadorNegras jugadorNegras) {
            return jugadorBlancas;
        }
    },
    NEGRO {
        @Override
        public int getDireccion() {
            return 1;
        }

        @Override
        public boolean esNegra() {
            return true;
        }

        @Override
        public boolean esBlanca() {
            return false;
        }

        @Override
        public Jugador escogerJugador(final JugadorBlancas jugadorBlancas, final JugadorNegras jugadorNegras) {
            return jugadorNegras;
        }
    };

    public abstract int getDireccion();

    public abstract boolean esNegra();

    public abstract boolean esBlanca();

    public abstract Jugador escogerJugador(JugadorBlancas jugadorBlancas, JugadorNegras jugadorNegras);
}
