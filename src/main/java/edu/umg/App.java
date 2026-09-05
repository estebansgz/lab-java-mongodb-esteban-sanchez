package edu.umg;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        try (ProductoDAO dao = new ProductoDAO();
             Scanner sc = new Scanner(System.in)) {

            int opcion = -1;

            while (opcion != 0) {
                mostrarMenu();
                System.out.print("Seleccione una opción: ");
                
                if (!sc.hasNextInt()) {
                    System.out.println("Opción inválida. Ingrese un número.");
                    sc.nextLine();
                    continue;
                }
                
                opcion = sc.nextInt();
                sc.nextLine(); // Limpiar el salto de línea

                switch (opcion) {
                    case 1:
                        agregarProducto(dao, sc);
                        break;
                    case 2:
                        dao.listar();
                        break;
                    case 3:
                        buscarProducto(dao, sc);
                        break;
                    case 4:
                        actualizarPrecio(dao, sc);
                        break;
                    case 5:
                        actualizarExistencia(dao, sc);
                        break;
                    case 6:
                        eliminarProducto(dao, sc);
                        break;
                    case 7:
                        listarPocoInventario(dao, sc);
                        break;
                    case 0:
                        System.out.println("¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error en la aplicación: " + e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("                 TIENDA                   ");
        System.out.println("========================================");
        System.out.println("1. Agregar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar producto");
        System.out.println("4. Actualizar precio");
        System.out.println("5. Actualizar existencia");
        System.out.println("6. Eliminar producto");
        System.out.println("7. Productos con poco inventario");
        System.out.println("0. Salir");
        System.out.println();
    }

    private static void agregarProducto(ProductoDAO dao, Scanner sc) {
    System.out.print("Ingrese código: ");
    String codigo = sc.nextLine().trim();

    // Si el usuario presiona Enter vacío por error, vuelve a solicitarlo
    while (codigo.isEmpty()) {
        System.out.print("El código no puede estar vacío. Ingrese código: ");
        codigo = sc.nextLine().trim();
    }

    if (dao.buscarPorCodigo(codigo) != null) {
        System.out.println("El código ya existe en la base de datos.");
        return;
    }

    System.out.print("Ingrese nombre: ");
    String nombre = sc.nextLine().trim();

    System.out.print("Ingrese categoría: ");
    String categoria = sc.nextLine().trim();

    double precio = -1;
    while (precio < 0) {
        System.out.print("Ingrese precio: ");
        if (sc.hasNextDouble()) {
            precio = sc.nextDouble();
            if (precio < 0) System.out.println("El precio no puede ser negativo.");
        } else {
            System.out.println("Ingrese un número válido para el precio.");
        }
        sc.nextLine(); // Limpia la línea tras leer el número
    }

    int existencia = -1;
    while (existencia < 0) {
        System.out.print("Ingrese existencia: ");
        if (sc.hasNextInt()) {
            existencia = sc.nextInt();
            if (existencia < 0) System.out.println("La existencia no puede ser negativa.");
        } else {
            System.out.println("Ingrese un número entero válido.");
        }
        sc.nextLine(); // Limpia la línea tras leer el número
    }

    Producto p = new Producto(codigo, nombre, categoria, precio, existencia);
    dao.insertar(p);
}

    private static void buscarProducto(ProductoDAO dao, Scanner sc) {
        System.out.print("Ingrese el código del producto: ");
        String codigo = sc.nextLine();
        dao.buscarPorCodigo(codigo);
    }

    private static void actualizarPrecio(ProductoDAO dao, Scanner sc) {
        System.out.print("Ingrese el código del producto: ");
        String codigo = sc.nextLine();

        if (dao.buscarPorCodigo(codigo) == null) {
            return;
        }

        double nuevoPrecio = -1;
        while (nuevoPrecio < 0) {
            System.out.print("Ingrese el nuevo precio: ");
            if (sc.hasNextDouble()) {
                nuevoPrecio = sc.nextDouble();
                if (nuevoPrecio < 0) System.out.println("El precio no puede ser negativo.");
            } else {
                System.out.println("Ingrese un número válido.");
            }
            sc.nextLine();
        }

        dao.actualizarPrecio(codigo, nuevoPrecio);
    }

    private static void actualizarExistencia(ProductoDAO dao, Scanner sc) {
        System.out.print("Ingrese el código del producto: ");
        String codigo = sc.nextLine();

        if (dao.buscarPorCodigo(codigo) == null) {
            return;
        }

        int nuevaExistencia = -1;
        while (nuevaExistencia < 0) {
            System.out.print("Ingrese la nueva existencia: ");
            if (sc.hasNextInt()) {
                nuevaExistencia = sc.nextInt();
                if (nuevaExistencia < 0) System.out.println("La existencia no puede ser negativa.");
            } else {
                System.out.println("Ingrese un número válido.");
            }
            sc.nextLine();
        }

        dao.actualizarExistencia(codigo, nuevaExistencia);
    }

    private static void eliminarProducto(ProductoDAO dao, Scanner sc) {
        System.out.print("Ingrese el código del producto a eliminar: ");
        String codigo = sc.nextLine();

        if (dao.buscarPorCodigo(codigo) == null) {
            return;
        }

        dao.eliminar(codigo);
    }

    private static void listarPocoInventario(ProductoDAO dao, Scanner sc) {
        int limite = -1;
        while (limite < 0) {
            System.out.print("Ingrese el límite de existencia: ");
            if (sc.hasNextInt()) {
                limite = sc.nextInt();
                if (limite < 0) System.out.println("El límite no puede ser negativo.");
            } else {
                System.out.println("Ingrese un número válido.");
            }
            sc.nextLine();
        }

        dao.listarPocoInventario(limite);
    }
}