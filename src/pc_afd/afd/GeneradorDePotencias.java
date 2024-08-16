package pc_afd.afd;

/**
 * Descripción:
 * Este programa genera potencias de un alfabeto dado, recibe un alfabeto y un número entero que es la potencia
 *
 * @author Jose Enrique Gonzalez Sanchez
 * @version 16/08/2024
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GeneradorDePotencias {

	// Método que genera potencias y guarda en un archivo
	public static void generarPotencias(String[] lenguaje, int potenciaMaxima) {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
			if (potenciaMaxima == 0) {
				writer.write("\u03BB");
			}
			for (int potencia = 1; potencia <= potenciaMaxima; potencia++) {
				calcularPotencia(lenguaje, potencia, "", writer);
				writer.newLine(); // Agrega un salto de línea después de cada potencia
			}

			// JOptionPane.showMessageDialog(null, "Archivo output.txt creado exitosamente
			// en " + System.getProperty("user.dir"));
			System.out.println("Archivo output.txt creado exitosamente en " + System.getProperty("user.dir"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Método para generar las potencias del lenguaje
	private static void calcularPotencia(String[] lenguaje, int potencia, String resultado, BufferedWriter writer)
			throws IOException {
		if (potencia == 0) {
			writer.write(resultado + ", ");
		} else {
			for (String simbolo : lenguaje) {
				calcularPotencia(lenguaje, potencia - 1, resultado + simbolo, writer);
			}
		}
	}
}
