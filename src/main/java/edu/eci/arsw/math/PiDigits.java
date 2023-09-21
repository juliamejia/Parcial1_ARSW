package edu.eci.arsw.math;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {
        /**
         * Returns a range of hexadecimal digits of pi.
         *
         * @param inicio The starting location of the range.
         * @param count  The number of digits to return
         * @return An array containing the hexadecimal digits.
         */
        public static byte[] getDigits(int inicio, int count, int n) throws InterruptedException {
                int hilos = n;
                byte[] digits = new byte[count];

                if (inicio < 0) {
                        throw new RuntimeException("Intervalo Invalido");
                }

                if (count < 0) {
                        throw new RuntimeException("Intervalo Invalido");
                }

                if (count < n) {
                        throw new RuntimeException("Demasiados hilos para la operacion");
                }

                DigitosThread[] threads = new DigitosThread[n];

                int bandera = 0;
                int numT = n;
                int a = inicio;

                while (numT > 0) {
                        int b = count;
                        while (b % n != 0) {
                                b--;
                        }
                        threads[bandera] = new DigitosThread(a, count / n);
                        bandera++;
                        count = count - (b / n);
                        a = a + (b / n);
                        n--;
                        numT--;
                }

                for (int i = 0; i < hilos; i++) {
                        threads[i].start();
                }

                try {
                        for (int i = 0; i < hilos; i++) {
                                threads[i].join();
                        }
                } catch (InterruptedException ex) {
                        Logger.getLogger(PiDigits.class.getName()).log(Level.SEVERE, null, ex);
                }

                for (int i = 0; i < hilos; i++) {
                        System.out.println(threads[i].getDigitos());

                }
                //INTENTO PUNTO 3----------------------------------------------------------
                Long starTime = System.nanoTime();
                TimeUnit.SECONDS.sleep(5);
                Long endTime = System.nanoTime();
                Long timeLapso = endTime -starTime;

                for(DigitosThread thread: threads){
                        thread.suspender();
                }

                Scanner scanner = new Scanner(System.in);
                System.out.println("presione enter para continuar");
                String input = scanner.nextLine();

                if (input == ""){
                        for (DigitosThread thread: threads){
                                thread.renaudar();
                        }
                }

                return digits;
        }

}



