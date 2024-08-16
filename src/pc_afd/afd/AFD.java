package pc_afd.afd;

/**
 * Descripción:
 * Programa que simula el comportamiento de un AFD.
 * Captura estados, transiciones, evalua cadenas y las busca en un diccionario de potencias que genera el mismo programa
 *
 * @author Jose Enrique Gonzalez Sanchez
 * @version 16/08/2024
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AFD {

	public Map<String, Map<String, String>> funcionDeTransicion;
	public String[] estados;
	public String[] alfabeto;
	public String estadoInicial;
	public String[] estadosFinales;
	public StringBuilder resultados;
	
    public static void main(String[] args) {
        AFD afd = new AFD();
        afd.capturarElementosAutomata();
    }

	public AFD() {
		resultados = new StringBuilder();
	}

	public void capturarElementosAutomata() {
		Scanner scanner = new Scanner(System.in);

		capturarEstados(scanner);

		System.out.println("Ingrese la potencia:");
		int potencia = Integer.parseInt(scanner.nextLine());

		generarLenguaje(potencia, alfabeto);

		capturarTransiciones(scanner);

		leerYValidarCadenas(scanner);

		guardarResultadosEnArchivo("output_2.txt");

		System.out.println("Lectura completada y lenguaje generado.");
		scanner.close();
	}

	public void capturarEstados(Scanner scanner) {
		System.out.println("Ingrese los estados (Q), separados por comas:");
		String estadosInput = scanner.nextLine();
		estados = estadosInput.split(",");

		System.out.println("Ingrese el alfabeto (∑), separado por comas:");
		String alfabetoInput = scanner.nextLine();
		alfabeto = alfabetoInput.split(",");

		System.out.println("Ingrese el estado inicial (q0):");
		estadoInicial = scanner.nextLine();

		System.out.println("Ingrese los estados finales (F), separados por comas:");
		String estadosFinalesInput = scanner.nextLine();
		estadosFinales = estadosFinalesInput.split(",");

		funcionDeTransicion = new HashMap<>();
	}

	public void capturarTransiciones(Scanner scanner) {
		for (String estado : estados) {
			Map<String, String> transiciones = new HashMap<>();
			for (String simbolo : alfabeto) {
				System.out.println("Ingrese el estado de transición desde " + estado + " con símbolo " + simbolo + ":");
				String estadoDestino = scanner.nextLine();
				transiciones.put(simbolo, estadoDestino);
			}
			funcionDeTransicion.put(estado, transiciones);
		}
	}

	public void leerYValidarCadenas(Scanner scanner) {
		System.out.println(
				"Ingrese las cadenas a validar (una por línea, presione Enter en una línea vacía para terminar):");

		while (true) {
			String cadena = scanner.nextLine();
			if (cadena.isEmpty()) {
				System.out.println("Finalizando la validación de cadenas.");
				break;
			}
			validarCadenaYBuscarEnArchivo(cadena, "output.txt");
		}
	}

	public void validarCadenaYBuscarEnArchivo(String cadena, String archivo) {
		StringBuilder resultado = new StringBuilder("Cadena: " + cadena + " - ");

		if (validarMultiplesCadenas(cadena)) {
			if (buscarEnArchivo(cadena, archivo)) {
				System.out.println("Cadena aceptada por el lenguaje.");
				resultado.append("Aceptada por el AFD, Aceptada por el lenguaje.");
			} else {
				System.out.println("Cadena no aceptada por el lenguaje.");
				resultado.append("Aceptada por el AFD, No aceptada por el lenguaje.");
			}
		} else {
			resultado.append("No aceptada por el AFD.");
		}

		resultados.append(resultado.toString()).append("\n");
	}

	public boolean validarMultiplesCadenas(String cadena) {
		String estadoActual = estadoInicial;
		boolean cadenaValida = true;

		for (char c : cadena.toCharArray()) {
			String simbolo = String.valueOf(c);
			if (funcionDeTransicion.containsKey(estadoActual)
					&& funcionDeTransicion.get(estadoActual).containsKey(simbolo)) {
				estadoActual = funcionDeTransicion.get(estadoActual).get(simbolo);
			} else {
				System.out.println("Cadena no válida para el AFD.");
				cadenaValida = false;
				break;
			}
		}

		if (cadenaValida && esEstadoFinal(estadoActual)) {
			System.out.println("Cadena válida para el AFD.");
			return true;
		} else if (cadenaValida) {
			System.out.println("Cadena no válida para el AFD.");
		}
		return false;
	}

	private boolean esEstadoFinal(String estado) {
		for (String estadoFinal : estadosFinales) {
			if (estadoFinal.trim().equals(estado.trim())) {
				return true;
			}
		}
		return false;
	}

	public boolean buscarEnArchivo(String cadena, String archivo) {
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] cadenasEnLinea = linea.split(",\\s*");
				for (String cadenaEnLinea : cadenasEnLinea) {
					if (cadenaEnLinea.trim().equals(cadena.trim())) {
						return true;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error al leer el archivo: " + e.getMessage());
		}
		return false;
	}

	public void guardarResultadosEnArchivo(String archivo) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
			writer.write(resultados.toString());
			System.out.println("Resultados guardados en " + archivo);
		} catch (IOException e) {
			System.out.println("Error al escribir en el archivo: " + e.getMessage());
		}
	}

	public void generarLenguaje(int potencia, String[] alfabeto) {
		GeneradorDePotencias.generarPotencias(alfabeto, potencia);
	}
}
