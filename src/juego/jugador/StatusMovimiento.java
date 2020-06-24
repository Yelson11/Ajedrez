package juego.jugador;

public enum StatusMovimiento {
    
    LISTO {
        @Override
        boolean estaListo() {
            return true;
        }
    },
    ILEGAL {

        @Override
        boolean estaListo() {
            return false;
        }

    },
    DEJA_JUGADOR_EN_JAQUE {
        @Override
        boolean estaListo() {
            return false;
        }
    };

    abstract boolean estaListo();
}
