package juego.pieza;

import juego.jugador.Player;
import juego.jugador.WhitesPlayer;
import juego.jugador.BlacksPlayer;
import juego.tablero.BoardUtils;

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
        public Player choosePlayer(final WhitesPlayer jugadorBlancas, final BlacksPlayer jugadorNegras) {
            return jugadorBlancas;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHT_RANK[position];
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
        public Player choosePlayer(final WhitesPlayer jugadorBlancas, final BlacksPlayer jugadorNegras) {
            return jugadorNegras;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }
    };

    public abstract int getDirection();
    public abstract int getOppositeDirection();
    public abstract boolean isBlack();
    public abstract boolean isWhite();
    public abstract boolean isPawnPromotionSquare(int position);
    public abstract Player choosePlayer(WhitesPlayer jugadorBlancas, BlacksPlayer jugadorNegras);
}
