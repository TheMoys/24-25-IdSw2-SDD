package src;

import java.util.ArrayList;
import java.util.List;

public class PagoEfectivo {
    
    private double monto;
    private List<Efectivo> listaEfectivo;

    public PagoEfectivo(double monto) {
        this.monto = monto;
        this.listaEfectivo = new ArrayList<Efectivo>();
    }

    public double getMonto() {
        return monto;
    }
    
    public List<Efectivo> getListaEfectivo() {
        return listaEfectivo;
    }

    public void ingresarEfectivo(Efectivo efectivo) {
        listaEfectivo.add(efectivo);
    }

    public void validarEfectivo() {
        double total = 0;
        for (Efectivo efectivo : listaEfectivo) {
            total += efectivo.getDenominacion();
        }
        if (total == monto) {
            System.out.println("Pago realizado con éxito.");
        } 
        if (total > monto) {
            System.out.println("El monto ingresado es mayor al monto a pagar.");
        }
    }

    // TODO: Crea un metodo que calcule la cantidad de efectivo a devolver si el monto ingresado es mayor al monto a pagar
    // Se debe calcular cuantas monedas de cada denominacion se deben devolver, de manera equitativa
}
