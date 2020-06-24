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
public class Alfil extends Pieza {

    private final static int[] CANDIDATOS_MOVIMIENTOS_VECTOR = { -9, -7, 7, 9 };

    public Alfil(ColorPieza piezaColor, int posicionPieza) {
        super(TipoPieza.ALFIL, piezaColor, posicionPieza, true);
    }

    public Alfil(ColorPieza piezaColor, int posicionPieza, boolean esPrimerMovimiento) {
        super(TipoPieza.ALFIL, piezaColor, posicionPieza, esPrimerMovimiento);
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
                        movimientosLegales.add(new MovimientoMayor(tablero, this, candidateDestinationCoordinate));
                    } else {
                        final Pieza piezaDestino = destinoCandidatoCasilla.getPieza();
                        final ColorPieza piezaColor = piezaDestino.getPiezaColor();

                        if (this.piezaColor != piezaColor) {
                            movimientosLegales
                                    .add(new MovimientoAtaque(tablero, this, candidateDestinationCoordinate, piezaDestino));
                        }
                    }
                    break;
                }

            }

        }
        return ImmutableList.copyOf(movimientosLegales);
    }

    public String toString() {
        return TipoPieza.ALFIL.toString(this.piezaColor);
    }

    private static boolean isFirstColumnExclusion(final int posicionActual, final int candidateOffSet) {
        return tableroUtilitarios.PRIMERA_COLUMNA[posicionActual] && (candidateOffSet == -9) || (candidateOffSet == 7);
    }

    private static boolean isEighthColumnExclusion(final int posicionActual, final int candidateOffSet) {
        return tableroUtilitarios.EIGHTH_COLUMNA[posicionActual] && (candidateOffSet == -7) || (candidateOffSet == 9);
    }

    @Override
    public Alfil moverPieza(final Movimiento movimiento) {
        return new Alfil(movimiento.getPiezaMovida().getPiezaColor(), movimiento.getCoordenadaDestino());
    }
}
