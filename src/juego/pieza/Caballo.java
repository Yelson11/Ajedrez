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
import static juego.tablero.tableroUtilitarios.casillaEsValida;

/**
 *
 * @author emers
 */
public class Caballo extends Pieza {

    private final static int[] movimientos_coordinados_candidatos = { -17, -15, -10, -6, 6, 10, 15, 17 };

    public Caballo(ColorPieza piezaColor, int posicionPieza) {
        super(TipoPieza.CABALLO, piezaColor, posicionPieza, true);
    }

    public Caballo(ColorPieza piezaColor, int posicionPieza, boolean esPrimerMovimiento) {
        super(TipoPieza.CABALLO, piezaColor, posicionPieza, esPrimerMovimiento);
    }

    @Override
    public List<Movimiento> calculaMovimientosLegales(final Tablero tablero) {

        Collection<Movimiento> movimientosLegales = new ArrayList<>();

        for (final int candidatoActual : movimientos_coordinados_candidatos) {

            final int destinoCandidato = this.posicionPieza + candidatoActual;

            if (casillaEsValida(destinoCandidato)) {
                if (isFirstColumnExclusion(this.posicionPieza, candidatoActual)
                        || isSecondColumnExclusion(this.posicionPieza, candidatoActual)
                        || isSeventhColumnExclusion(this.posicionPieza, candidatoActual)
                        || isEighthColumnExclusion(this.posicionPieza, candidatoActual)) {
                    continue;
                }

                final Casilla destinoCandidatoCasilla = tablero.getCasilla(destinoCandidato);
                if (!destinoCandidatoCasilla.casillaEstaOcupada()) {
                    movimientosLegales.add(new MovimientoMayor(tablero, this, destinoCandidato));
                } else {
                    final Pieza piezaDestino = destinoCandidatoCasilla.getPieza();
                    final ColorPieza piezaColor = piezaDestino.getPiezaColor();

                    if (this.piezaColor != piezaColor) {
                        movimientosLegales.add(new MovimientoAtaque(tablero, this, destinoCandidato, piezaDestino));
                    }
                }
            }

        }

        return ImmutableList.copyOf(movimientosLegales);
    }

    public String toString() {
        return TipoPieza.CABALLO.toString(this.piezaColor);
    }

    private static boolean isFirstColumnExclusion(final int posicionActual, final int candidateOffSet) {
        return tableroUtilitarios.FIRST_COLUMN[posicionActual] && (candidateOffSet == -17)
                || (candidateOffSet == -10) || (candidateOffSet == 6) || (candidateOffSet == -15);
    }

    private static boolean isSecondColumnExclusion(final int posicionActual, final int candidateOffSet) {
        return tableroUtilitarios.SECOND_COLUMN[posicionActual] && (candidateOffSet == -10) || (candidateOffSet == 6);
    }

    private static boolean isSeventhColumnExclusion(final int posicionActual, final int candidateOffSet) {
        return tableroUtilitarios.SEVENTH_COLUMN[posicionActual] && (candidateOffSet == -6) || (candidateOffSet == 10);
    }

    private static boolean isEighthColumnExclusion(final int posicionActual, final int candidateOffSet) {
        return tableroUtilitarios.EIGHT_COLUMN[posicionActual] && (candidateOffSet == -15) || (candidateOffSet == -6)
                || (candidateOffSet == 10) || (candidateOffSet == 17);
    }
    
    public Caballo moverPieza(final Movimiento movimiento) {
        return new Caballo(movimiento.getPiezaMovida().getPiezaColor(), movimiento.getCoordenadaDestino());
    }

}
