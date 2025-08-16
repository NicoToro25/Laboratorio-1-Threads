/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {
    
    public static void main(String[] args){

        CountThread a = new CountThread("Hilo A", 0, 99);
        CountThread b = new CountThread("Hilo B", 100, 199);
        CountThread c = new CountThread("Hilo C", 200, 299);

        a.start();
        b.start();
        c.start();

    }
    
}
