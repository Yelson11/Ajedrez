/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.jugador;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import juego.pieza.ColorPieza;
import juego.pieza.Pieza;
import juego.pieza.Torre;
import juego.tablero.Casilla;
import juego.tablero.Movimiento;
import juego.tablero.Tablero;

/**
 *
 * @author edamazzio
 */
public class JugadorNegras extends Jugador {

    public JugadorNegras(final Tablero tablero, final Collection<Movimiento> movimientosPermitidosBlancas,
            final Collection<Movimiento> movimientosPermitidosNegras) {
        super(tablero, movimientosPermitidosNegras, movimientosPermitidosBlancas);
    }

    @Override
    public Collection<Pieza> getPiezasActivas() {
        return this.tablero.getPiezasNegras();
    }

    @Override
    public ColorPieza getColor() {
        return ColorPieza.NEGRO;
    }

    @Override
    public Jugador getOponente() {
        return this.tablero.jugadorBlancas();
    }

    //@Override
    protected Collection<Movimiento> calcularReyCastles(final Collection<Movimiento> jugadorLegales, final Collection<Movimiento> oponentesLegales) {
        final List<Movimiento> reyCastles = new ArrayList<>();
        if(this.reyJugador.isFirstMove() && !this.estaEnJacque()){
            if(!this.tablero.getCasilla(5).casillaEstaOcupada() && !this.tablero.getCasilla(6).casillaEstaOcupada()){
                final Casilla casilla = this.tablero.getCasilla(7);
                if(casilla.casillaEstaOcupada() && casilla.getPieza().isFirstMove()){
                    if(Jugador.calcularMovimientosDeAtaqueEnCasilla(5, oponentesLegales).isEmpty() &&
                       Jugador.calcularMovimientosDeAtaqueEnCasilla(6, oponentesLegales).isEmpty() &&
                       casilla.getPieza().getTipoPieza().isTorre()){
                       reyCastles.add(new Movimiento.MovimientoCastilloLadoRey(this.tablero, this.reyJugador, 6, (Torre)casilla.getPieza(), casilla.getCoordenadaCasilla(), 5));
                    }
                }
            }
            if (!this.tablero.getCasilla(1).casillaEstaOcupada() &&
                !this.tablero.getCasilla(2).casillaEstaOcupada() &&
                !this.tablero.getCasilla(3).casillaEstaOcupada()){
                
                final Casilla casilla = this.tablero.getCasilla(0);
                if(casilla.casillaEstaOcupada() && casilla.getPieza().isFirstMove()){
                    reyCastles.add(new Movimiento.MovimientoCastilloLadoReina(this.tablero, this.reyJugador, 2, (Torre)casilla.getPieza(), casilla.getCoordenadaCasilla(), 3));
                }
            }
        }
        return ImmutableList.copyOf(reyCastles);
    }

}
