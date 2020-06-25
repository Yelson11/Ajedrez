package juego.pieza;

import java.util.Collection;
import juego.tablero.Movimiento;
import juego.tablero.Tablero;

/**
 *
 * @author emers
 */
public abstract class Pieza {
    final TipoPieza tipoPieza;
    protected final ColorPieza piezaColor;
    protected final int posicionPieza;
    protected final boolean isFirstMove;
    private final int hashCodeAlmacenado;

    Pieza(final TipoPieza tipoPieza, final ColorPieza piezaColor, final int posicionPieza,
            final boolean isFirstMove) {
        this.posicionPieza = posicionPieza;
        this.piezaColor = piezaColor;
        this.isFirstMove = isFirstMove;
        this.tipoPieza = tipoPieza;
        this.hashCodeAlmacenado = computeHashCode();
    }
    
    private int computeHashCode(){
        int result = tipoPieza.hashCode();
        result = 31 * result + piezaColor.hashCode();
        result = 31 * result + posicionPieza;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }
    
    @Override
    public boolean equals(final Object otro){
        if(this == otro){
            return true;
        }
        if(!(otro instanceof Pieza)){
            return false;
        }
        final Pieza otrPieza = (Pieza) otro;
        return posicionPieza == otrPieza.getPosicionPieza() && tipoPieza == otrPieza.getTipoPieza() &&
               piezaColor == otrPieza.getPiezaColor() && isFirstMove == otrPieza.isFirstMove();
    }
    
    @Override
    public int hashCode(){
     return this.hashCodeAlmacenado;
    }

    public int getPosicionPieza() {
        return this.posicionPieza;
    }

    public ColorPieza getPiezaColor() {
        return this.piezaColor;
    } 

    public TipoPieza getTipoPieza() {
        return this.tipoPieza;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public abstract Collection<Movimiento> calculaMovimientosLegales(final Tablero tablero);
    
    public abstract Pieza moverPieza(Movimiento movimiento);

    public enum TipoPieza {
        ALFIL("B"){ //BISOHP
            @Override
            public boolean isRey() {
                return false;
            }

            @Override
            public boolean isTorre() {
                return false;
            }
            
        }, 
        REY("K"){ //KING
            @Override
            public boolean isRey() {
                return true;
            }

            @Override
            public boolean isTorre() {
                return false;
            }
        }, 
        CABALLO("N"){  //kNight
            @Override
            public boolean isRey() {
                return false;
            }

            @Override
            public boolean isTorre() {
                return false;
            }
        }, 
        REINA("Q"){ //QUEEN
            @Override
            public boolean isRey() {
                return false;
            }

            @Override
            public boolean isTorre() {
                return false;
            }
        }, 
        TORRE("R"){ //ROOK
            @Override
            public boolean isRey() {
                return false;
            }

            @Override
            public boolean isTorre() {
                return true;
            }
        }, 
        PEON("P"){ //PAWN
            @Override
            public boolean isRey() {
                return false;
            }

            @Override
            public boolean isTorre() {
                return false;
            }
        };

        private String nombrePieza;

        public String toString(ColorPieza color) {
            return this.nombrePieza;
        }

        TipoPieza(final String nombrePieza) {
            this.nombrePieza = nombrePieza;
        }
        
        public abstract boolean isRey();
        public abstract boolean isTorre();
        
    }

}
