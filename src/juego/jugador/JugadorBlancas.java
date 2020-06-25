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
public class JugadorBlancas extends Jugador {

    public JugadorBlancas(final Tablero tablero, final Collection<Movimiento> movimientosPermitidosBlancas, final Collection<Movimiento> movimientosPermitidosNegras) {
        super(tablero, movimientosPermitidosBlancas, movimientosPermitidosNegras);
    }

    @Override
    public Collection<Pieza> getPiezasActivas() {
        return this.tablero.getPiezasBlancas();
    }

    @Override
    public ColorPieza getColor() {
        return ColorPieza.BLANCO;
    }

    @Override
    public Jugador getOponente() {
        return this.tablero.jugadorNegras();
    }

    //@Override
    protected Collection<Movimiento> calcularReyCastles(final Collection<Movimiento> jugadorLegales, final Collection<Movimiento> oponentesLegales){
        
        final List<Movimiento> reyCastles = new ArrayList();
        
        if(this.reyJugador.isFirstMove() && !this.estaEnJacque()){
            //Rey Castle
            if(!this.tablero.getCasilla(61).casillaEstaOcupada() && !this.tablero.getCasilla(62).casillaEstaOcupada()){
                final Casilla torreCasilla = this.tablero.getCasilla(63);
                
                if(torreCasilla.casillaEstaOcupada() && torreCasilla.getPieza().isFirstMove()){
                    if(Jugador.calcularMovimientosDeAtaqueEnCasilla(61, oponentesLegales).isEmpty() &&
                       Jugador.calcularMovimientosDeAtaqueEnCasilla(62, oponentesLegales).isEmpty() &&
                       torreCasilla.getPieza().getTipoPieza().isTorre()){
                        reyCastles.add(new Movimiento.MovimientoCastilloLadoRey(this.tablero, this.reyJugador, 62, (Torre)torreCasilla.getPieza(), torreCasilla.getCoordenadaCasilla(),61));
                    }
                }
            }
            if(!this.tablero.getCasilla(59).casillaEstaOcupada() && 
               !this.tablero.getCasilla(58).casillaEstaOcupada() && 
               !this.tablero.getCasilla(57).casillaEstaOcupada()){
                
                final Casilla torreCasilla = this.tablero.getCasilla(56);
                if(torreCasilla.casillaEstaOcupada() && torreCasilla.getPieza().isFirstMove() &&
                    Jugador.calcularMovimientosDeAtaqueEnCasilla(58, oponentesLegales).isEmpty() && 
                    Jugador.calcularMovimientosDeAtaqueEnCasilla(59, oponentesLegales).isEmpty()  &&
                    torreCasilla.getPieza().getTipoPieza().isTorre()){
                    reyCastles.add(new Movimiento.MovimientoCastilloLadoReina(this.tablero, this.reyJugador, 58, (Torre)torreCasilla.getPieza(), torreCasilla.getCoordenadaCasilla(), 59));
                }
            }
        }
        
        return ImmutableList.copyOf(reyCastles);
    }

}
