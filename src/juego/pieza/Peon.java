package juego.pieza;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import juego.tablero.Movimiento;
import juego.tablero.Movimiento.MovimientoMayor;
import juego.tablero.Tablero;
import juego.tablero.tableroUtilitarios;

/**
 *
 * @author emers
 */
public class Peon extends Pieza {

    private final static int[] CANDIDATOS_MOVIMIENTOS_VECTOR = { 8, 16, 7, 9 };

    public Peon(ColorPieza piezaColor, int posicionPieza) {
        super(TipoPieza.PEON, piezaColor, posicionPieza, true);
    }

    public Peon(ColorPieza piezaColor, int posicionPieza, boolean esPrimerMovimiento) {
        super(TipoPieza.PEON, piezaColor, posicionPieza, esPrimerMovimiento);
    }

    @Override
    public Collection<Movimiento> calculaMovimientosLegales(final Tablero tablero) {

        final List<Movimiento> movimientosLegales = new ArrayList<>();

        for (final int candidatoActual : CANDIDATOS_MOVIMIENTOS_VECTOR) {
            final int candidateDestinationCoordinate = this.posicionPieza
                    + (this.getPiezaColor().getDireccion() * candidatoActual);

            if (!tableroUtilitarios.casillaEsValida(candidateDestinationCoordinate)) {
                continue;
            }

            if (candidatoActual == 8 && tablero.getCasilla(candidateDestinationCoordinate).casillaEstaOcupada()) {
                movimientosLegales.add(new MovimientoMayor(tablero, this, candidateDestinationCoordinate));
            } else if (candidatoActual == 16 && this.isFirstMove()
                    && (tableroUtilitarios.SECOND_ROW[this.posicionPieza]) && this.getPiezaColor().esNegra()
                    || (tableroUtilitarios.SEVENTH_ROW[this.posicionPieza]) && this.getPiezaColor().esBlanca()) {
                final int behindCandidateDestination = this.posicionPieza + (this.piezaColor.getDireccion() * 8);
                if (!tablero.getCasilla(behindCandidateDestination).casillaEstaOcupada()
                        && !tablero.getCasilla(candidateDestinationCoordinate).casillaEstaOcupada()) {
                    movimientosLegales.add(new MovimientoMayor(tablero, this, candidateDestinationCoordinate));
                }
            } else if (candidatoActual == 7 && !(tableroUtilitarios.EIGHTH_COLUMNA[this.posicionPieza]
                    && this.piezaColor.esBlanca()
                    || tableroUtilitarios.PRIMERA_COLUMNA[this.posicionPieza] && this.piezaColor.esNegra())) {
                if (tablero.getCasilla(candidateDestinationCoordinate).casillaEstaOcupada()) {
                    final Pieza piezaCandidata = tablero.getCasilla(candidateDestinationCoordinate).getPieza();
                    if (this.piezaColor != piezaCandidata.getPiezaColor()) {
                        movimientosLegales.add(new MovimientoMayor(tablero, this, candidateDestinationCoordinate));
                    }
                }

            } else if (candidatoActual == 9
                    && !(tableroUtilitarios.PRIMERA_COLUMNA[this.posicionPieza] && this.piezaColor.esBlanca()
                            || tableroUtilitarios.EIGHTH_COLUMNA[this.posicionPieza] && this.piezaColor.esNegra())) {
                if (tablero.getCasilla(candidateDestinationCoordinate).casillaEstaOcupada()) {
                    final Pieza piezaCandidata = tablero.getCasilla(candidateDestinationCoordinate).getPieza();
                    if (this.piezaColor != piezaCandidata.getPiezaColor()) {
                        movimientosLegales.add(new MovimientoMayor(tablero, this, candidateDestinationCoordinate));
                    }
                }
            }

        }

        return ImmutableList.copyOf(movimientosLegales);
    }

    public String toString() {
        return TipoPieza.PEON.toString(this.piezaColor);
    }
    
    public Peon moverPieza(final Movimiento movimiento) {
        return new Peon(movimiento.getPiezaMovida().getPiezaColor(), movimiento.getCoordenadaDestino());
    }

}
