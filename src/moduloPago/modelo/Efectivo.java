package src.moduloPago.modelo;

import java.util.Map;
import java.util.HashMap;


public class Efectivo implements Pago {
    private Double cantidad;
    private double montoDisponible;
    private Map<Double, Integer> denominacionesUsuario; 
    public static final double[] denominaciones_aceptadas = {20, 10, 5, 2.00, 1.00, 0.50, 0.20, 0.10, 0.05};

    public Efectivo(double montoInicial) {
        this.denominacionesUsuario = new HashMap<>();
        this.montoDisponible = montoInicial;
    }

    @Override
    public boolean pagar(double monto) {
        if (montoDisponible >= monto) {
            montoDisponible -= monto;
            return true;
        }
        return false;
    }

    public double getMontoDisponible() {
        return montoDisponible;
    }

    public void agregarEfectivo(double monto) {
        this.montoDisponible += monto;
    }

    public Map<Double, Integer> getDenominacionesUsuario() {
        return denominacionesUsuario;
    }

    public void agregarDenominacionUsuario(double denominacion, int cantidad) {
        if (!esDenominacionAceptada(denominacion)) {
            throw new IllegalArgumentException("Denominación no válida: " + denominacion);
        }
        denominacionesUsuario.put(denominacion, denominacionesUsuario.getOrDefault(denominacion, 0) + cantidad);

    }

    public void retirarDenominacionUsuario(double denominacion, int cantidad) {
        Map<Double, Integer> denominaciones = getDenominacionesUsuario();
        int actual = denominaciones.getOrDefault(denominacion, 0);
        if (cantidad > actual) {
            throw new IllegalArgumentException("No tienes suficientes monedas/billetes de " + denominacion + "€.");
        }
        denominaciones.put(denominacion, actual - cantidad);
        montoDisponible -= denominacion * cantidad; 
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public void retirarMonto(double monto) {
        if (monto > 0 && monto <= montoDisponible) {
            montoDisponible -= monto;
        } else {
            throw new IllegalArgumentException("Monto inválido para retirar.");
        }
    }

    public void agregarMonto(double monto) {
        if (monto > 0) {
            montoDisponible += monto;
        } else {
            throw new IllegalArgumentException("Monto inválido para agregar.");
        }
    }
    public boolean esDenominacionAceptada(double denominacion) {
        for (double aceptada : denominaciones_aceptadas) {
            if (aceptada == denominacion) {
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        return "Efectivo disponible: " + montoDisponible + "€";
    }
}
