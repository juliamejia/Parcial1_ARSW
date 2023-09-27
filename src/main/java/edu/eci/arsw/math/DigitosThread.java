package edu.eci.arsw.math;

public class DigitosThread extends Thread {
    /*
    En estas líneas, se define la clase DigitosThread como una subclase de Thread.
    Se declaran algunas variables estáticas y miembros de instancia, como DigitsPerSum y Epsilon,
    que se utilizan para cálculos posteriores. También se declaran variables de instancia ini, contador y digitos,
    así como un booleano suspend para controlar la suspensión y reanudación de hilos.
    */
    private static int DigitsPerSum = 8; // Cantidad de dígitos procesados antes de actualizar la suma.
    private static double Epsilon = 1e-17; // Valor de epsilon utilizado en cálculos.
    private int ini; // Inicio del rango de cálculo.
    private int contador; // Cantidad de dígitos a calcular.
    private byte[] digitos; // Almacenará los dígitos calculados.
    boolean suspend = false; // Variable que controla la suspensión del hilo.

    public DigitosThread() {
    }

    /*
    Este es un constructor personalizado que toma dos argumentos a y b y
    los asigna a las variables de instancia ini y contador.
    */
    public DigitosThread(int a, int b) {
        this.ini = a;
        this.contador = b;
    }

    /*
    Este método getDigits toma dos argumentos start y count que representan el inicio
    y la cantidad de dígitos a calcular. Verifica si estos valores son válidos (no negativos)
    y lanza una excepción si no lo son.
     */
    public byte[] getDigits(int start, int count) {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval"); // Lanza una excepción si el inicio es negativo.
        }
        if (count < 0) {
            throw new RuntimeException("Invalid Interval"); // Lanza una excepción si la cantidad es negativa.
        }
        /*
        Se crea un arreglo de bytes digits para almacenar los dígitos calculados.
        Se inicializa una variable sum para realizar los cálculos.
         */
        byte[] digits = new byte[count];
        double sum = 0;

        for (int i = 0; i < count; i++) {
            /*
            En este bloque, se verifica si i es un múltiplo de DigitsPerSum.
            Si es así, se realiza un cálculo específico y se actualiza el valor de start.
             */
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);
                start += DigitsPerSum;
            }
            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum; // Almacena el byte calculado en el arreglo de dígitos.
        }
        /*
        Se realiza el cálculo principal utilizando la fórmula del algoritmo
        para calcular los dígitos de pi. El resultado se almacena en digits.
         */
        digitos = digits; // Almacena el resultado en la variable de instancia digitos.
        return digits;
    }

    /*
    El resultado calculado se asigna a la variable de instancia digitos
    y luego se devuelve como resultado del método.
     */
    public byte[] getMyListDigits() {
        return digitos; // Devuelve el arreglo de dígitos calculados.
    }

    /*
    Este método devuelve el arreglo de bytes digitos que contiene los dígitos calculados
     */
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
        return sum; // Retorna la suma de términos calculada.
    }

    /*
    Se agrega el término actual al valor de sum, se actualizan las variables power
     y d y se continúa calculando la serie hasta que se cumpla la condición de salida.
     Luego, se devuelve el valor calculado de sum.
     */
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
        return result; // Retorna el resultado de la operación 16^p mod m.
    }

    /*
   Este método se ejecuta cuando se inicia un hilo. Llama a getDigits para calcular los dígitos y
   luego llama a enSuspencion para suspender el hilo.
    */
    public void run() {
        getDigits(this.ini, this.contador);
        enSuspencion(); // Llama al método enSuspencion() para gestionar la suspensión del hilo.
    }

    /*
    Este método establece la variable suspend en true,
    lo que indica que el hilo debe ser suspendido.
     */
    public synchronized void suspender() {
        suspend = true; // Establece la variable de suspensión en true.
    }

    /*
    Establece la variable suspend en false y notifica a todos
    los hilos en espera que pueden reanudar su ejecución
     */
    public synchronized void renaudar() {
        suspend = false; // Establece la variable de suspensión en false.
        notifyAll(); // Notifica a los hilos en espera que pueden continuar.
    }

    /*
    Este metodo se encarga de mantener el hilo en suspensión mientras la variable suspend sea true.
    Utiliza wait() para esperar hasta que se le notifique que puede reanudar su ejecución.
     */
    public synchronized void enSuspencion() {
        while (suspend) {
            try {
                wait(); // Espera hasta que suspend sea false y reciba una notificación para continuar.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
