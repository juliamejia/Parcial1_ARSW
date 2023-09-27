package edu.eci.arsw.math;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

///
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {
        private int DigitsPerSum = 8;
        private double Epsilon = 1e-17;
        private List<DigitosThread> threads = new ArrayList<DigitosThread>();
        private byte[] digits;
        /**
         * Returns a range of hexadecimal digits of pi.
         * @param start The starting location of the range.
         * @param count The number of digits to return
         * @return An array containing the hexadecimal digits.
         */
        public byte[] getDigits(int start, int count, int N) throws InterruptedException {
                int numberPerThread = count / N;
                digits = new byte[count];
                int aux = 0;
                if (count % N != 0) aux =count % N ;
                threads.add(new DigitosThread(start,numberPerThread + aux));
                N--;
                start += aux;
                // System.out.println("VALOR start: " + start);
                for (int i = 0; i < N; i++) {
                        threads.add(new DigitosThread(start + numberPerThread,numberPerThread));
                        start +=numberPerThread;
                        // System.out.println("VALOR start: " + start + ", VALOR PER THREAD: " + (numberPerThread + aux) );
                }
                for (Thread t : threads) {
                        t.start();
                }
                try {
                        int i = 0;
                        for (DigitosThread t : threads) {
                                t.join();
                                for (byte b : t.getMyListDigits()) {
                                        digits[i]= b;
                                        i++;
                                }
                        }
                } catch (Exception e) {
                        System.out.println("Error in thread");
                }
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
                byte[] digits = this.digits;
                return digits;
        }
}