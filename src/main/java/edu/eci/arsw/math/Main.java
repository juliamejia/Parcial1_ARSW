/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.math;

/**
 *
 * @author hcadavid // Comentario indicando el autor del código
 */
public class Main { // Declaración de la clase "Main"

    public static void main(String a[]) throws InterruptedException { // Método principal "main" que se ejecuta al iniciar el programa
        // Líneas que imprimen en la consola valores convertidos a hexadecimal
        System.out.println(bytesToHex(new PiDigits().getDigits(0, 10, 8)));
        System.out.println(bytesToHex(new PiDigits().getDigits(0, 100, 8)));
        System.out.println(bytesToHex(new PiDigits().getDigits(1, 1000, 8)));
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray(); // Declaración de un arreglo de caracteres llamado "hexArray"

    public static String bytesToHex(byte[] bytes) { // Método que convierte un arreglo de bytes en una cadena hexadecimal
        char[] hexChars = new char[bytes.length * 2]; // Declara un arreglo de caracteres para almacenar los caracteres hexadecimales
        for (int j = 0; j < bytes.length; j++) { // ciclo que recorre el arreglo de bytes
            int v = bytes[j] & 0xFF; // Obtiene el valor de un byte y lo convierte a un valor entre 0 y 255
            hexChars[j * 2] = hexArray[v >>> 4]; // Obtiene el primer dígito hexadecimal
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Obtiene el segundo dígito hexadecimal
        }
        StringBuilder sb = new StringBuilder(); // Creación de un objeto StringBuilder para construir la cadena hexadecimal
        for (int i = 0; i < hexChars.length; i = i + 2) { // Ciclo que recorre el arreglo de caracteres
            sb.append(hexChars[i + 1]); // Agrega el segundo dígito hexadecimal al StringBuilder
        }
        return sb.toString(); // Devuelve la cadena hexadecimal como una cadena de texto
    }
}
