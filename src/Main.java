package src;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Crear máquinas expendedoras con sus cajas
        Map<String, Caja> maquinas = new LinkedHashMap<>();
        maquinas.put("Maquina-1", new Caja("Maquina-1"));
        maquinas.put("Maquina-2", new Caja("Maquina-2"));

        // Inicializar las cajas con efectivo
        ControladorPago controladorPago = new ControladorPago();
        maquinas.values().forEach(caja -> inicializarCaja(controladorPago, caja));

        // Crear productos en las máquinas
        Map<String, Double> productos = new LinkedHashMap<>();
        productos.put("Coca-Cola", 1.5);
        productos.put("Pepsi", 1.4);
        productos.put("Agua", 1.0);
        productos.put("Chocolate", 2.0);

        Usuario usuario = new Usuario();

        System.out.println("========================================");
        System.out.println("       Bienvenido a las máquinas expendedoras");
        System.out.println("========================================");
        usuario.mostrarSaldos();

        // Mostrar contenido inicial de la caja
        System.out.println("\n========================================");
        System.out.println("       Contenido inicial de la caja");
        System.out.println("========================================");
        maquinas.forEach((nombre, caja) -> {
            System.out.println("Caja de " + nombre + ":");
            caja.mostrarContenidoCaja();
            System.out.println("----------------------------------------");
        });

        // Mostrar menú de máquinas
        System.out.println("\n========================================");
        System.out.println("       Seleccione una máquina");
        System.out.println("========================================");
        List<String> maquinasList = new ArrayList<>(maquinas.keySet());
        for (int i = 0; i < maquinasList.size(); i++) {
            System.out.println((i + 1) + ". " + maquinasList.get(i));
        }
        System.out.print("Ingrese el número de la máquina: ");
        int maquinaSeleccionadaIndex = scanner.nextInt() - 1;

        if (maquinaSeleccionadaIndex < 0 || maquinaSeleccionadaIndex >= maquinasList.size()) {
            System.out.println("Máquina no válida. Saliendo...");
            return;
        }

        Caja caja = maquinas.get(maquinasList.get(maquinaSeleccionadaIndex));

        // Mostrar menú de productos
        System.out.println("\n========================================");
        System.out.println("       Productos disponibles");
        System.out.println("========================================");
        List<Map.Entry<String, Double>> productosList = new ArrayList<>(productos.entrySet());
        for (int i = 0; i < productosList.size(); i++) {
            System.out.println((i + 1) + ". " + productosList.get(i).getKey() + " - €" + productosList.get(i).getValue());
        }
        System.out.print("Ingrese el número del producto que desea comprar: ");
        int productoSeleccionadoIndex = scanner.nextInt() - 1;

        if (productoSeleccionadoIndex < 0 || productoSeleccionadoIndex >= productosList.size()) {
            System.out.println("Producto no válido. Saliendo...");
            return;
        }

        Map.Entry<String, Double> productoSeleccionado = productosList.get(productoSeleccionadoIndex);
        double precioProducto = productoSeleccionado.getValue();
        System.out.println("El precio del producto es: €" + precioProducto);

        // Elegir método de pago
        System.out.println("\n========================================");
        System.out.println("       Métodos de pago");
        System.out.println("========================================");
        System.out.println("1. EFECTIVO");
        System.out.println("2. MONEDERO");
        System.out.println("3. BANCARIA");
        System.out.print("Seleccione el método de pago: ");
        int metodoPagoSeleccionado = scanner.nextInt();

        switch (metodoPagoSeleccionado) {
            case 1:
                procesarPagoEfectivo(scanner, usuario, controladorPago, caja, precioProducto);
                break;
            case 2:
                procesarPagoTarjeta(usuario, "MONEDERO", precioProducto);
                break;
            case 3:
                procesarPagoTarjeta(usuario, "BANCARIA", precioProducto);
                break;
            default:
                System.out.println("Método de pago inválido. Saliendo...");
        }

        System.out.println("\n========================================");
        System.out.println("       Gracias por su compra");
        System.out.println("========================================");
        usuario.mostrarSaldos();

        // Mostrar contenido final de la caja
        System.out.println("\n========================================");
        System.out.println("       Contenido final de la caja");
        System.out.println("========================================");
        caja.mostrarContenidoCaja();
    }

    private static void inicializarCaja(ControladorPago controladorPago, Caja caja) {
        double[] denominaciones = {0.05, 0.10, 0.20, 0.50, 1.0, 2.0, 5.0, 10.0, 20.0};
        for (double denom : denominaciones) {
            for (int i = 0; i < 10; i++) {
                Efectivo efectivo = new Efectivo(denom, denom < 5.0 ? Efectivo.Tipo.MONEDA : Efectivo.Tipo.BILLETE);
                controladorPago.agregarEfectivo(efectivo);
                caja.actualizarCaja(efectivo);
            }
        }
    }

    private static void procesarPagoEfectivo(Scanner scanner, Usuario usuario, ControladorPago controladorPago, Caja caja, double precioProducto) {
        double montoIngresado = 0.0;
        Map<Double, Integer> efectivoUsado = new HashMap<>();

        while (montoIngresado < precioProducto) {
            System.out.print("Ingrese una denominación de efectivo: ");
            double denominacion = scanner.nextDouble();

            if (usuario.ingresarEfectivo(denominacion)) {
                Efectivo efectivo = new Efectivo(denominacion, denominacion < 5.0 ? Efectivo.Tipo.MONEDA : Efectivo.Tipo.BILLETE);
                controladorPago.agregarEfectivo(efectivo);
                caja.actualizarCaja(efectivo);

                efectivoUsado.put(denominacion, efectivoUsado.getOrDefault(denominacion, 0) + 1);

                montoIngresado += denominacion;
                System.out.println("Monto ingresado: €" + montoIngresado);
            } else {
                System.out.println("Denominación no válida o insuficiente.");
            }
        }

        double cambio = montoIngresado - precioProducto;
        Map<Double, Integer> cambioEntregado = new HashMap<>();

        if (cambio > 0) {
            try {
                cambioEntregado = caja.devolverCambio(cambio);
                System.out.println("Cambio entregado: " + cambioEntregado);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        usuario.actualizarEfectivoConCambio(efectivoUsado, cambioEntregado);
    }

    private static void procesarPagoTarjeta(Usuario usuario, String tipoTarjeta, double precioProducto) {
        double saldo = tipoTarjeta.equals("MONEDERO") ? usuario.getTarjetaMonedero() : usuario.getTarjetaBancaria();

        if (saldo >= precioProducto) {
            System.out.println("Pago realizado con " + tipoTarjeta + ". Monto: €" + precioProducto);
            if (tipoTarjeta.equals("MONEDERO")) {
                usuario.descontarSaldoMonedero(precioProducto);
            } else {
                usuario.descontarSaldoBancario(precioProducto);
            }
        } else {
            System.out.println("Saldo insuficiente en la tarjeta " + tipoTarjeta + ".");
        }
    }
}