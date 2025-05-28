package src;

import java.util.ArrayList;
import java.util.List;

public class PagoEfectivo {
    
    private double monto; // Monto requerido para el pago
    private List<Efectivo> listaEfectivo; // Lista de denominaciones ingresadas
    private double cambio; // Cambio calculado

    public PagoEfectivo(double monto) {
        this.monto = monto;
        this.listaEfectivo = new ArrayList<>();
        this.cambio = 0.0;
    }

    public double getMonto() {
        return monto;
    }

    // Nuevo método para obtener el monto requerido
    public double getMontoRequerido() {
        return monto;
    }
    
    public List<Efectivo> getListaEfectivo() {
        return listaEfectivo;
    }

    public double getCambio() {
        return cambio;
    }

    public void ingresarEfectivo(Efectivo efectivo) {
        listaEfectivo.add(efectivo);
    }

    public double getMontoIngresado() {
        double total = 0;
        for (Efectivo efectivo : listaEfectivo) {
            total += efectivo.getDenominacion();
        }
        return total;
    }

    public boolean validarEfectivo() {
        double totalIngresado = getMontoIngresado();
        if (totalIngresado >= monto) {
            cambio = totalIngresado - monto;
            return true;
        }
        cambio = 0.0;
        return false;
    }
}