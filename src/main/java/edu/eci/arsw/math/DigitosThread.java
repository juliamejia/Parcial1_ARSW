package edu.eci.arsw.math;

import java.util.LinkedList;
import java.util.List;

public class DigitosThread extends Thread {
    private int Ini;
    private int contador;
    private byte[] digitos;
    private static int digitosPer = 8;
    private static double Epsilon = 1e-17;
    boolean suspender = false;
    int a, b;

    private static List<Integer> primes = new LinkedList<Integer>();

    public DigitosThread(int a, int b) {
        this.Ini = Ini;
        this.contador = contador;
        digitos = new byte[contador];
    }

    @Override
    public void run(){
        double sum = 0;
        for (int i = 0; i < contador; i++) {
            if (i % digitosPer == 0) {
                sum = 4 * sum(1, Ini)
                        - 2 * sum(4, Ini)
                        - sum(5, Ini)
                        - sum(6, Ini);

                Ini += digitosPer;
            }
            sum = 16 * (sum - Math.floor(sum));
            digitos[i] = (byte) sum;
            enSuspencion();
        }
    }
    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }
    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }

    public byte[] getDigitos() {
        return digitos;
    }

    //PARA EL PUNTO 3------------------------------------------------

    boolean isPrime(int n){
        if(n%2 == 0) {
            return false;
        }
        for (int i=3; i*i <= n; i+=2){
            if(n%i ==0){
                return false;
            }
        }
        return true;
    }
    public synchronized void suspender(){
        suspender = true;
    }
    public synchronized void renaudar(){
        suspender=false;
        notifyAll();
    }
    public void enSuspencion(){
        while (suspender){
            try {
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    public static List<Integer>getPrimes(){
        return primes;
    }
}

