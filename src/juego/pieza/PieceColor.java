package juego.pieza;

import juego.jugador.Player;
import juego.jugador.WhitesPlayer;
import juego.jugador.BlacksPlayer;

/**
 *
 * @author emers
 */
public enum PieceColor {

    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public Player escogerJugador(final WhitesPlayer jugadorBlancas, final BlacksPlayer jugadorNegras) {
            return jugadorBlancas;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public Player escogerJugador(final WhitesPlayer jugadorBlancas, final BlacksPlayer jugadorNegras) {
            return jugadorNegras;
        }
    };

    public abstract int getDirection();

    public abstract boolean isBlack();

    public abstract boolean isWhite();

    public abstract Player escogerJugador(WhitesPlayer jugadorBlancas, BlacksPlayer jugadorNegras);
}
