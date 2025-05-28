package src;

public class ControladorPago {

    PagoEfectivo pagoEfectivo;
    PagoTarjeta pagoTarjeta;
    public boolean pagoRealizado = false;

    public ControladorPago() {
        this.pagoEfectivo = null;
        this.pagoTarjeta = null;
    }

    public PagoEfectivo getPagoEfectivo() {
        return pagoEfectivo;
    }
    
    public PagoTarjeta getPagoTarjeta() {
        return pagoTarjeta;
    }

    public boolean isPagoRealizado() {
        return pagoRealizado;
    }

    public void iniciarPagoEfectivo(double monto) {
        this.pagoEfectivo = new PagoEfectivo(monto);
    }

    public void agregarEfectivo(Efectivo efectivo) {
        if (pagoEfectivo == null) {
            System.out.println("Iniciando el pago en efectivo automáticamente.");
            iniciarPagoEfectivo(0); // Inicializa con un monto de 0
        }
        pagoEfectivo.ingresarEfectivo(efectivo);
    
        // Validar solo si el monto total ingresado es suficiente
        if (pagoEfectivo.getMontoIngresado() >= pagoEfectivo.getMontoRequerido()) {
            if (pagoEfectivo.validarEfectivo()) {
                pagoRealizado = true;
                System.out.println("Pago en efectivo validado.");
            } else {
                pagoRealizado = false;
                System.out.println("Monto insuficiente.");
            }
        }
    }

    public void realizarPagoTarjeta(Tarjeta tarjeta, double monto) {
        this.pagoTarjeta = new PagoTarjeta(tarjeta, monto);
        if (pagoTarjeta.realizarPago()) {
            System.out.println("Pago con tarjeta realizado exitosamente.");
        } else {
            System.out.println("Fondos insuficientes en la tarjeta.");
        }
    }

}
