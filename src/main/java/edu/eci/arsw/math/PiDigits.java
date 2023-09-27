package edu.eci.arsw.math;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

// Implementación de la fórmula Bailey-Borwein-Plouffe para calcular dígitos hexadecimales de π.
// Referencia: https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
// Este código se tradujo desde C#: https://github.com/mmoroney/DigitsOfPi
public class PiDigits {
        private int DigitsPerSum = 8; // Número de dígitos a sumar en cada iteración.
        private double Epsilon = 1e-17; // Valor de epsilon para comparaciones numéricas.
        private List<DigitosThread> threads = new ArrayList<DigitosThread>(); // Lista de hilos.
        private byte[] digits; // Arreglo para almacenar los dígitos de π.

        /**
         * Obtiene un rango de dígitos hexadecimales de π.
         * @param start La ubicación de inicio del rango.
         * @param count El número de dígitos a devolver.
         * @return Un arreglo que contiene los dígitos hexadecimales.
         */
        public byte[] getDigits(int start, int count, int N) throws InterruptedException {
                int numberPerThread = count / N; // Cantidad de dígitos a calcular por hilo.
                digits = new byte[count]; // Inicializa el arreglo para almacenar los dígitos.
                int aux = 0;
                if (count % N != 0) aux = count % N; // Calcula la cantidad de dígitos adicionales.

                // Crea y configura los hilos para calcular los dígitos de π.
                threads.add(new DigitosThread(start, numberPerThread + aux));
                N--;
                start += aux;

                for (int i = 0; i < N; i++) {
                        threads.add(new DigitosThread(start + numberPerThread, numberPerThread));
                        start += numberPerThread;
                }

                // Inicia los hilos.
                for (Thread t : threads) {
                        t.start();
                }

                try {
                        int i = 0;
                        // Espera a que todos los hilos terminen y recopila los dígitos calculados.
                        for (DigitosThread t : threads) {
                                t.join();
                                for (byte b : t.getMyListDigits()) {
                                        digits[i] = b;
                                        i++;
                                }
                        }
                } catch (Exception e) {
                        System.out.println("Error en el hilo");
                }

                // Mide el tiempo de espera de 5 segundos.
                Long startTime = System.nanoTime();
                TimeUnit.SECONDS.sleep(5);
                Long endTime = System.nanoTime();
                Long timeElapsed = endTime - startTime;

                // Pausa los hilos y espera la entrada del usuario para reanudar.
                for(DigitosThread thread: threads){
                        thread.suspender();
                }
                Scanner scanner = new Scanner(System.in);
                System.out.println("Presione Enter para continuar");
                String input = scanner.nextLine();

                if (input.equals("")) {
                        // Reanuda los hilos si el usuario presiona Enter.
                        for (DigitosThread thread: threads) {
                                thread.renaudar();
                        }
                }

                // Devuelve los dígitos calculados.
                byte[] digits = this.digits;
                return digits;
        }
}
