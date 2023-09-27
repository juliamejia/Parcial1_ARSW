package edu.eci.arsw.math;
public class DigitosThread extends Thread {
    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    private int ini;
    private int contador;
    private byte[] digitos;
    public DigitosThread() {
    }
    public DigitosThread(int a, int b) {
        this.ini = a;
        this.contador = b;
    }
    public byte[] getDigits(int start, int count) {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        byte[] digits = new byte[count];
        double sum = 0;
        for (int i = 0; i < count; i++) {
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);
                start += DigitsPerSum;
            }
            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
        }
        digitos = digits;
        return digits;
    }
    public byte[] getMyListDigits() {
        return digitos;
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
    public void run() {
        getDigits(this.ini,this.contador);
    }

}