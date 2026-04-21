package co.edu.poli.agro.vista;

import co.edu.poli.agro.modelo.Abono;
import co.edu.poli.agro.modelo.Fertilizante;
import co.edu.poli.agro.modelo.Pesticida;
import co.edu.poli.agro.modelo.Producto;
import co.edu.poli.agro.servicios.ImplementacionOperacionCRUD;

import java.util.Scanner;

public class Principal {

    static Scanner sc = new Scanner(System.in);
    static ImplementacionOperacionCRUD impl = new ImplementacionOperacionCRUD();

    public static void main(String[] args) {

        // Cargar datos del archivo al iniciar si existe
        java.io.File archivo = new java.io.File("productos.txt");
        if (archivo.exists()) {
            impl.desereralizar();
            System.out.println("[Sistema] Datos cargados desde productos.txt (" +
                               impl.getContador() + " productos).");
        }

        int opcion;

        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║   REGISTRO DE PRODUCTOS AGRICOLAS   ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Registrar producto               ║");
            System.out.println("║  2. Ver producto por indice          ║");
            System.out.println("║  3. Ver todos los productos          ║");
            System.out.println("║  4. Modificar producto               ║");
            System.out.println("║  5. Eliminar producto                ║");
            System.out.println("║  6. Serializar datos                 ║");
            System.out.println("║  7. Deserializar datos               ║");
            System.out.println("║  0. Salir                            ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Seleccione una opcion: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1: registrar();     break;
                case 2: verUno();        break;
                case 3: verTodos();      break;
                case 4: modificar();     break;
                case 5: eliminar();      break;
                case 6: serializar();    break;
                case 7: deserializar();  break;
                case 0: System.out.println("Hasta luego."); break;
                default: System.out.println("Opcion invalida.");
            }

        } while (opcion != 0);
    }

    // ─── 1. Registrar ────────────────────────────────────────────────────────
    static void registrar() {
        System.out.println("\n-- Tipo de producto --");
        System.out.println("1. Fertilizante");
        System.out.println("2. Pesticida");
        System.out.println("3. Abono");
        System.out.print("Seleccione: ");
        int tipo = sc.nextInt();
        sc.nextLine();

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Cantidad disponible: ");
        double cantidad = sc.nextDouble();
        sc.nextLine();
        System.out.print("Unidad de medida (kg, L, etc.): ");
        String unidad = sc.nextLine();
        System.out.print("Fecha de aplicacion (dd/mm/aaaa): ");
        String fecha = sc.nextLine();

        Producto p = null;

        switch (tipo) {
            case 1:
                System.out.print("Tipo de formula: ");
                String formula = sc.nextLine();
                System.out.print("Observaciones: ");
                String obsF = sc.nextLine();
                p = new Fertilizante(0, nombre, cantidad, unidad, fecha, formula, obsF);
                break;
            case 2:
                System.out.print("Plaga objetivo: ");
                String plaga = sc.nextLine();
                System.out.print("Toxicidad: ");
                String tox = sc.nextLine();
                System.out.print("Observaciones: ");
                String obsP = sc.nextLine();
                p = new Pesticida(0, nombre, cantidad, unidad, fecha, plaga, tox, obsP);
                break;
            case 3:
                System.out.print("Origen: ");
                String origen = sc.nextLine();
                System.out.print("Tiempo de descomposicion (dias): ");
                int dias = sc.nextInt();
                sc.nextLine();
                System.out.print("Observaciones: ");
                String obsA = sc.nextLine();
                p = new Abono(0, nombre, cantidad, unidad, fecha, origen, dias, obsA);
                break;
            default:
                System.out.println("Tipo invalido.");
                return;
        }

        System.out.println(impl.crear(p));
        System.out.println("Capacidad actual del arreglo: " + impl.getCapacidad());
    }

    // ─── 2. Ver uno ──────────────────────────────────────────────────────────
    static void verUno() {
        if (impl.getContador() == 0) {
            System.out.println("No hay productos registrados.");
            return;
        }
        System.out.print("Ingrese el indice (0 - " + (impl.getContador() - 1) + "): ");
        int idx = sc.nextInt();
        sc.nextLine();
        Producto p = impl.leer(idx);
        if (p != null) {
            System.out.println("\n" + p.resumen());
        }
    }

    // ─── 3. Ver todos ────────────────────────────────────────────────────────
    static void verTodos() {
        Producto[] lista = impl.leertodo();
        if (lista.length == 0) {
            System.out.println("No hay productos registrados.");
            return;
        }
        System.out.println("\n--- Lista de productos (" + lista.length + ") ---");
        for (int i = 0; i < lista.length; i++) {
            System.out.println("[" + i + "] " + lista[i].resumen());
        }
    }

    // ─── 4. Modificar ────────────────────────────────────────────────────────
    static void modificar() {
        if (impl.getContador() == 0) {
            System.out.println("No hay productos registrados.");
            return;
        }
        verTodos();
        System.out.print("Ingrese el indice a modificar: ");
        int idx = sc.nextInt();
        sc.nextLine();

        Producto actual = impl.leer(idx);
        if (actual == null) return;

        System.out.println("Modificando: " + actual.resumen());
        System.out.println("(Deje en blanco para conservar el valor actual)");

        System.out.print("Nuevo nombre [" + actual.getNombre() + "]: ");
        String nombre = sc.nextLine();
        if (nombre.isEmpty()) nombre = actual.getNombre();

        System.out.print("Nueva cantidad [" + actual.getCantidadDisponible() + "]: ");
        String cantStr = sc.nextLine();
        double cantidad = cantStr.isEmpty() ? actual.getCantidadDisponible() : Double.parseDouble(cantStr);

        System.out.print("Nueva unidad [" + actual.getUnidadmedida() + "]: ");
        String unidad = sc.nextLine();
        if (unidad.isEmpty()) unidad = actual.getUnidadmedida();

        System.out.print("Nueva fecha [" + actual.getFechaAplicacion() + "]: ");
        String fecha = sc.nextLine();
        if (fecha.isEmpty()) fecha = actual.getFechaAplicacion();

        Producto nuevo = null;

        if (actual instanceof Fertilizante) {
            Fertilizante f = (Fertilizante) actual;
            System.out.print("Nueva formula [" + f.getTipoFormula() + "]: ");
            String formula = sc.nextLine();
            if (formula.isEmpty()) formula = f.getTipoFormula();
            System.out.print("Nuevas observaciones [" + f.getObservaciones() + "]: ");
            String obs = sc.nextLine();
            if (obs.isEmpty()) obs = f.getObservaciones();
            nuevo = new Fertilizante(0, nombre, cantidad, unidad, fecha, formula, obs);

        } else if (actual instanceof Pesticida) {
            Pesticida p = (Pesticida) actual;
            System.out.print("Nueva plaga [" + p.getPlagaObjetivo() + "]: ");
            String plaga = sc.nextLine();
            if (plaga.isEmpty()) plaga = p.getPlagaObjetivo();
            System.out.print("Nueva toxicidad [" + p.getToxicidad() + "]: ");
            String tox = sc.nextLine();
            if (tox.isEmpty()) tox = p.getToxicidad();
            System.out.print("Nuevas observaciones [" + p.getObservaciones() + "]: ");
            String obs = sc.nextLine();
            if (obs.isEmpty()) obs = p.getObservaciones();
            nuevo = new Pesticida(0, nombre, cantidad, unidad, fecha, plaga, tox, obs);

        } else if (actual instanceof Abono) {
            Abono a = (Abono) actual;
            System.out.print("Nuevo origen [" + a.getOrigen() + "]: ");
            String origen = sc.nextLine();
            if (origen.isEmpty()) origen = a.getOrigen();
            System.out.print("Nuevo tiempo descomp. [" + a.getTiempoDescomposicion() + "]: ");
            String diasStr = sc.nextLine();
            int dias = diasStr.isEmpty() ? a.getTiempoDescomposicion() : Integer.parseInt(diasStr);
            System.out.print("Nuevas observaciones [" + a.getObservaciones() + "]: ");
            String obs = sc.nextLine();
            if (obs.isEmpty()) obs = a.getObservaciones();
            nuevo = new Abono(0, nombre, cantidad, unidad, fecha, origen, dias, obs);
        }

        System.out.println(impl.modificar(idx, nuevo));
    }

    // ─── 5. Eliminar ─────────────────────────────────────────────────────────
    static void eliminar() {
        if (impl.getContador() == 0) {
            System.out.println("No hay productos registrados.");
            return;
        }
        verTodos();
        System.out.print("Ingrese el indice a eliminar: ");
        int idx = sc.nextInt();
        sc.nextLine();
        System.out.println(impl.eliminar(idx));
    }

    // ─── 6. Serializar ───────────────────────────────────────────────────────
    static void serializar() {
        System.out.println(impl.serializar());
    }

    // ─── 7. Deserializar ─────────────────────────────────────────────────────
    static void deserializar() {
        Producto[] lista = impl.desereralizar();
        if (lista.length == 0) {
            System.out.println("No se encontro el archivo productos.txt o esta vacio.");
            return;
        }
        System.out.println("\n=== DATOS CARGADOS DESDE ARCHIVO (" + lista.length + " productos) ===");
        for (int i = 0; i < lista.length; i++) {
            System.out.println("[" + i + "] " + lista[i].resumen());
        }
    }
}