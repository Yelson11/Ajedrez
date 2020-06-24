package juego.pieza;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import juego.tablero.Casilla;
import juego.tablero.Movimiento;
import juego.tablero.Tablero;
import juego.tablero.tableroUtilitarios;

/**
 *
 * @author emers
 */
public class Torre extends Pieza {

    private final static int[] CANDIDATOS_MOVIMIENTOS_VECTOR = { -8, -1, 1, 8 };

    public Torre(ColorPieza piezaColor, int posicionPieza) {
        super(TipoPieza.TORRE, piezaColor, posicionPieza, true);
    }

    public Torre(ColorPieza piezaColor, int posicionPieza, boolean esPrimerMovimiento) {
        super(TipoPieza.TORRE, piezaColor, posicionPieza, esPrimerMovimiento);
    }

    @Override
    public Collection<Movimiento> calculaMovimientosLegales(final Tablero tablero) {

        final List<Movimiento> movimientosLegales = new ArrayList<>();

        for (final int candidateCoordinateOffSet : CANDIDATOS_MOVIMIENTOS_VECTOR) {

            int candidateDestinationCoordinate = this.posicionPieza;

            while (tableroUtilitarios.casillaEsValida(candidateDestinationCoordinate)) {

                if (isFirstColumnExclusion(this.posicionPieza, candidateDestinationCoordinate)
                        || isEighthColumnExclusion(this.posicionPieza, candidateDestinationCoordinate)) {
                    break;
                }

                candidateDestinationCoordinate += candidateCoordinateOffSet;

                if (tableroUtilitarios.casillaEsValida(candidateDestinationCoordinate)) {
                    final Casilla destinoCandidatoCasilla = tablero.getCasilla(candidateDestinationCoordinate);
                    if (!destinoCandidatoCasilla.casillaEstaOcupada()) {
                        movimientosLegales.add(new Movimiento.MovimientoMayor(tablero, this, candidateDestinationCoordinate));
                    } else {
                        final Pieza piezaDestino = destinoCandidatoCasilla.getPieza();
                        final ColorPieza piezaColor = piezaDestino.getPiezaColor();

                        if (this.piezaColor != piezaColor) {
                            movimientosLegales.add(new Movimiento.MovimientoAtaque(tablero, this,
                                    candidateDestinationCoordinate, piezaDestino));
                        }
                    }
                    break;
                }

            }

        }
        return ImmutableList.copyOf(movimientosLegales);
    }

    public String toString() {
        return TipoPieza.TORRE.toString(this.piezaColor);
    }

    private static boolean isFirstColumnExclusion(final int posicionActual, final int candidateOffSet) {
        return tableroUtilitarios.PRIMERA_COLUMNA[posicionActual] && (candidateOffSet == -1);
    }

    private static boolean isEighthColumnExclusion(final int posicionActual, final int candidateOffSet) {
        return tableroUtilitarios.EIGHTH_COLUMNA[posicionActual] && (candidateOffSet == 1);
    }

    public Torre moverPieza(final Movimiento movimiento) {
        return new Torre(movimiento.getPiezaMovida().getPiezaColor(), movimiento.getCoordenadaDestino());
    }
    
}
