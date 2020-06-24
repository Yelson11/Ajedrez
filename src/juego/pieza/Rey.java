package juego.pieza;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import juego.tablero.Casilla;
import juego.tablero.Movimiento;
import juego.tablero.Movimiento.MovimientoAtaque;
import juego.tablero.Movimiento.MovimientoMayor;
import juego.tablero.Tablero;
import juego.tablero.tableroUtilitarios;

/**
 *
 * @author emers
 */
public class Rey extends Pieza {

    private final static int[] CANDIDATOS_MOVIMIENTOS_VECTOR = { -9, -8, -7, -1, 1, 7, 8, 9 };

    public Rey(ColorPieza piezaColor, int posicionPieza) {
        super(TipoPieza.REY, piezaColor, posicionPieza, true);
    }

    public Rey(ColorPieza piezaColor, int posicionPieza, boolean esPrimerMovimiento) {
        super(TipoPieza.REY, piezaColor, posicionPieza, esPrimerMovimiento);
    }

    @Override
    public Collection<Movimiento> calculaMovimientosLegales(Tablero tablero) {

        final List<Movimiento> movimientosLegales = new ArrayList<>();

        for (final int candidatoActual : CANDIDATOS_MOVIMIENTOS_VECTOR) {
            final int candidateDestinationCoordinate = this.posicionPieza + candidatoActual;

            if (isFirstColumnExclusion(this.posicionPieza, candidatoActual)
                    || isEighthColumnExclusion(this.posicionPieza, candidatoActual)) {
                continue;
            }

            if (tableroUtilitarios.casillaEsValida(candidateDestinationCoordinate)) {
                final Casilla casillaDestinoCandidato = tablero.getCasilla(candidateDestinationCoordinate);
                if (!casillaDestinoCandidato.casillaEstaOcupada()) {
                    movimientosLegales.add(new MovimientoMayor(tablero, this, candidateDestinationCoordinate));
                } else {
                    final Pieza piezaDestino = casillaDestinoCandidato.getPieza();
                    final ColorPieza piezaColor = piezaDestino.getPiezaColor();

                    if (this.piezaColor != piezaColor) {
                        movimientosLegales
                                .add(new MovimientoAtaque(tablero, this, candidateDestinationCoordinate, piezaDestino));
                    }
                }

            }

        }

        return ImmutableList.copyOf(movimientosLegales);
    }

    public String toString() {
        return TipoPieza.REY.toString(this.piezaColor);
    }

    private static boolean isFirstColumnExclusion(final int posicionActual, final int candidateOffSet) {
        return tableroUtilitarios.PRIMERA_COLUMNA[posicionActual] && (candidateOffSet == -9) || (candidateOffSet == -1)
                || (candidateOffSet == 7);
    }

    private static boolean isEighthColumnExclusion(final int posicionActual, final int candidateOffSet) {
        return tableroUtilitarios.EIGHTH_COLUMNA[posicionActual] && (candidateOffSet == -7) || (candidateOffSet == 1)
                || (candidateOffSet == 9);
    }
    
    public Rey moverPieza(final Movimiento movimiento) {
        return new Rey(movimiento.getPiezaMovida().getPiezaColor(), movimiento.getCoordenadaDestino());
    }

}
