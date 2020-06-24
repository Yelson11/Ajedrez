package juego.jugador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import juego.pieza.ColorPieza;
import juego.pieza.Pieza;
import juego.pieza.Rey;
import juego.pieza.Pieza.TipoPieza;
import juego.tablero.Movimiento;
import juego.tablero.Tablero;

public abstract class Jugador {

    protected final Tablero tablero;
    protected final Rey reyJugador;
    protected final Collection<Movimiento> movimientosPermitidos;
    private final boolean estaEnJacque;

    Jugador(final Tablero tablero, final Collection<Movimiento> movimientosPermitidos,
            final Collection<Movimiento> movimientosOponente) {

        this.tablero = tablero;
        this.reyJugador = establecerRey();
        this.movimientosPermitidos = ImmutableList.copyOf(Iterables.concat(movimientosPermitidos, calcularReyCastles(movimientosPermitidos, movimientosOponente)));
        this.estaEnJacque = !Jugador.calcularMovimientosDeAtaqueEnCasilla(this.reyJugador.getPosicionPieza(), movimientosOponente).isEmpty();
    }

    protected static Collection<Movimiento> calcularMovimientosDeAtaqueEnCasilla(int posicionPieza,
            Collection<Movimiento> movimientos) {
        final List<Movimiento> movimientosDeAtaque = new ArrayList<>();
        for (final Movimiento movimiento : movimientos) {
            if (posicionPieza == movimiento.getCoordenadaDestino()) {
                movimientosDeAtaque.add(movimiento);
            }
        }
        return ImmutableList.copyOf(movimientosDeAtaque);
    }

    public Rey getReyJugador(){
        return this.reyJugador;
    }

    private Rey establecerRey() {
        for (final Pieza pieza : getPiezasActivas()) {
            if (pieza.getTipoPieza() == TipoPieza.REY) {
                return (Rey) pieza;
            }
        }
        throw new RuntimeException("Tablero inválido, no se ha encontrado la pieza Rey");
    }

    public boolean movimientoEsPermitido(final Movimiento movimiento) {
        return this.movimientosPermitidos.contains(movimiento);
    }

    public boolean estaEnJacque() {
        return this.estaEnJacque;
    }

    public boolean estaEnJaqueMate() {
        return this.estaEnJacque && !tieneMovimientosDeEscape();
    }

    // https://es.wikipedia.org/wiki/Ahogado_(ajedrez)
    public boolean estaAhogado() {
        return !this.estaEnJacque() && !tieneMovimientosDeEscape();
    }

    // https://es.wikipedia.org/wiki/Enroque
    public boolean estaEnrocado() {
        return false;
    }


    private boolean tieneMovimientosDeEscape() {
        for (final Movimiento movimiento : this.movimientosPermitidos) {
            final MovimientoDeTransicion transicion = hacerMovimiento((movimiento));
            if (transicion.getStatusMovimiento().estaListo()) {
                return true;
            }
        }
        return false;
    }

    public Collection<Movimiento> getMovimientosPermitidos() {
        return this.movimientosPermitidos;
    }


    public MovimientoDeTransicion hacerMovimiento(final Movimiento movimiento) {

        if (!movimientoEsPermitido((movimiento))){
            return new MovimientoDeTransicion(this.tablero, movimiento, StatusMovimiento.ILEGAL);
        }

        final Tablero tableroDeTransicion = movimiento.ejecutar();

        final Collection<Movimiento> ataquesAlRey = Jugador.calcularMovimientosDeAtaqueEnCasilla((tableroDeTransicion.jugadorActual().getOponente().getReyJugador().getPosicionPieza()),
             tableroDeTransicion.jugadorActual().getMovimientosPermitidos());

        if ( !ataquesAlRey.isEmpty()  ){
            return new MovimientoDeTransicion(this.tablero, movimiento, StatusMovimiento.DEJA_JUGADOR_EN_JAQUE);
        }

        return new MovimientoDeTransicion(tableroDeTransicion, movimiento, StatusMovimiento.LISTO);
    }

    public abstract Collection<Pieza> getPiezasActivas();
    public abstract ColorPieza getColor();
    public abstract Jugador getOponente();
    protected abstract Collection<Movimiento> calcularReyCastles(Collection<Movimiento> jugadorLegales, Collection<Movimiento> oponentesLegales);

}