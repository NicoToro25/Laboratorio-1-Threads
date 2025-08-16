/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author nect25
 */
public class CountThread extends Thread{
    private String nombre;
    private int A;
    private int B;

    public CountThread(String nombre, int A, int B) {
        this.nombre = nombre;
        this.A = A;
        this.B = B;
    }

    @Override
    public void run() {
        for (int i = A; i <= B; i++) {
            System.out.println(nombre + " va contando en " + i);
            try {
                CountThread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
