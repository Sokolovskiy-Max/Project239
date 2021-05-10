package com.company;

import javax.swing.JFrame;
public class firstForm {
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame=new JFrame("Выбор системы");


        secondForm demo=new secondForm();

        frame.setContentPane(demo.createContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize( 400,400);
        frame.setVisible(true);
    }
}

