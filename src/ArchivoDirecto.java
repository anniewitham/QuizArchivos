
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class ArchivoDirecto {

    private int clave;
    private String nombre;
    private int edad;
    private long tamreg;
    private long canreg;
    private File fl;
    RandomAccessFile archivo;

    public ArchivoDirecto() throws IOException {
        this.clave = 1;
        this.nombre = "";
        this.edad = 0;
        this.tamreg = 58; // Tamaño del registro
        this.canreg = 0;
        try {
            fl = new File("src//prueba.dat");
            archivo = new RandomAccessFile(fl, "rw");
            // Recuperar la cantidad de registros existentes
            archivo.seek(0);
            canreg = archivo.length() / tamreg;
            if (canreg > 0) {
                archivo.seek((canreg - 1) * tamreg); // Ir al último registro
                clave = archivo.readInt() + 1; // Establecer la clave como la siguiente después del último registro
            }
        } catch (FileNotFoundException fnfe) {
            // Archivo no encontrado
            System.err.println("Archivo no encontrado.");
        }
    }

    public void escribir() {
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("\nEscribiendo Registros:");
            String r = "S";
            while (r.equalsIgnoreCase("S")) {

                System.out.println("Ingrese nombre: ");
                nombre = teclado.readLine();
                if (nombre.length() < 25) {
                    nombre = String.format("%-25s", nombre); // Ajustar el nombre a 25 caracteres
                } else {
                    nombre = nombre.substring(0, 25);
                }
                System.out.println("Ingrese edad: ");
                edad = Integer.parseInt(teclado.readLine());

                archivo.seek(archivo.length()); // Posicionar al final del archivo
                archivo.writeInt(clave++);
                archivo.writeChars(nombre);
                archivo.writeInt(edad);

                canreg++;
                System.out.println("Ingresar otra linea (S or N)?: \t");
                r = teclado.readLine();
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("Archivo no encontrado.");
        } catch (IOException ioe) {
            System.err.println("Error al escribir en el archivo.");
            ioe.printStackTrace();
        }
    }

    public void leerTodo() {
        System.out.println("\nMostrando todos los Registros");
        try {
            archivo.seek(0);
            canreg = archivo.length() / tamreg;
            for (int r = 0; r < canreg; r++) {
                clave = archivo.readInt();
                StringBuilder nombreBuilder = new StringBuilder();
                for (int i = 0; i < 25; i++) {
                    nombreBuilder.append(archivo.readChar());
                }
                nombre = nombreBuilder.toString().trim(); // Eliminar espacios en blanco
                edad = archivo.readInt();
                System.out.println(" Registro No: " + clave + " Nombre: " + nombre + " Edad: " + edad);
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("Archivo no encontrado.");
        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo.");
            ioe.printStackTrace();
        }
    }

    public void leerUnRegistro() {
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nBuscando un Registro:");
        System.out.println("Ingrese el numero de registro a buscar: ");
        try {
            int numRegistro = Integer.parseInt(teclado.readLine());
            archivo.seek((numRegistro - 1) * tamreg); // Argumento es tipo long

            clave = archivo.readInt();
            StringBuilder nombreBuilder = new StringBuilder();
            for (int i = 0; i < 25; i++) {
                nombreBuilder.append(archivo.readChar());
            }
            nombre = nombreBuilder.toString().trim(); // Eliminar espacios en blanco
            edad = archivo.readInt();
            System.out.println("Registro No. " + clave + " Nombre: " + nombre + " Edad: " + edad + "\n\n");
        } catch (FileNotFoundException fnfe) {
            System.err.println("Archivo no encontrado.");
        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo.");
            ioe.printStackTrace();
        }
    }

    public void contarRegistros() {
        try {
            archivo.seek(0); // Ubicarse al inicio
            canreg = archivo.length() / tamreg;
            System.out.println("\nCantidad de registros: " + canreg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualizarRegistro() throws IOException {
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nActualizando un Registro:");
        System.out.println("Desea actualizar un registro? (S/N)");
        String respuesta = teclado.readLine();

        if (respuesta.equalsIgnoreCase("S")) {
            System.out.println("Ingrese el numero de registro a actualizar: ");
            try {
                int numRegistro = Integer.parseInt(teclado.readLine());
                archivo.seek((numRegistro-1) * tamreg); // Se calcula la posición en el archivo

                // Leer la clave, nombre y edad actuales
                int claveLeida = archivo.readInt();
                StringBuilder nombreActual = new StringBuilder();
                for (int i = 0; i < 25; i++) {
                    nombreActual.append(archivo.readChar()); // Leer el nombre (25 caracteres)
                }
                int edadActual = archivo.readInt();

                System.out.println("Registro actual: ");
                System.out.println("Clave: " + claveLeida + " Nombre: " + nombreActual.toString().trim() + " Edad: " + edadActual);

                System.out.println("Que desea modificar? (1 = Nombre, 2 = Edad): ");
                int opcion = Integer.parseInt(teclado.readLine());

                if (opcion == 1) {
                    // Actualizar el nombre
                    System.out.println("Ingrese el nuevo nombre: ");
                    String nuevoNombre = teclado.readLine();

                    // Ajustar el nombre para que tenga 25 caracteres
                    if (nuevoNombre.length() < 25) {
                        nuevoNombre = String.format("%-25s", nuevoNombre); // Rellenar con espacios
                    } else {
                        nuevoNombre = nuevoNombre.substring(0, 25);
                    }

                    archivo.seek((numRegistro) * tamreg + 4); // La clave ocupa los primeros 4 bytes
                    archivo.writeChars(nuevoNombre); // Escribir el nuevo nombre

                } else if (opcion == 2) {
                    // Actualizar la edad
                    System.out.println("Ingrese la nueva edad: ");
                    int nuevaEdad = Integer.parseInt(teclado.readLine());

                    archivo.seek((numRegistro - 1) * tamreg + 54); // 54 bytes es donde empieza la edad
                    archivo.writeInt(nuevaEdad); // Escribir la nueva edad
                }

                System.out.println("Registro actualizado con exito.");
            } catch (IOException e) {
                e.printStackTrace(); // Manejo de errores de entrada/salida
            }
        } else {
            System.out.println("No se actualizara ningun registro.");
        }
    }
    
    public void buscarClavePrimerRegistro() {
    try {
        archivo.seek(0); // Ir al inicio del archivo
        int primeraClave = archivo.readInt(); // Leer la clave del primer registro
        System.out.println("La clave del primer registro es: " + primeraClave);
    } catch (FileNotFoundException fnfe) {
        System.err.println("Archivo no encontrado.");
    } catch (IOException ioe) {
        System.err.println("Error al leer el archivo.");
        ioe.printStackTrace();
    }
}

    public void buscarClavePenultimoRegistro() {
        try {
            if (canreg > 1) { // Verificar que haya al menos dos registros
                archivo.seek((canreg - 2) * tamreg); // Ir al penúltimo registro
                int penultimaClave = archivo.readInt(); // Leer la clave del penúltimo registro
                System.out.println("La clave del penultimo registro es: " + penultimaClave);
            } else {
                System.out.println("No hay suficientes registros para mostrar el penúltimo.");
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("Archivo no encontrado.");
        } catch (IOException ioe) {
            System.err.println("Error al leer el archivo.");
            ioe.printStackTrace();
        }
    }

    public void cerrar() {
        try {
            archivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ArchivoDirecto arch = new ArchivoDirecto();
        arch.escribir();
        arch.leerTodo();
        arch.contarRegistros();
        arch.buscarClavePrimerRegistro(); // Llamada para buscar la clave del primer registro
        arch.buscarClavePenultimoRegistro(); // Llamada para buscar la clave del penúltimo registro
        arch.actualizarRegistro(); // Llamada para actualizar un registro
        arch.cerrar();
    } // fin del main
} // fin de la clase
