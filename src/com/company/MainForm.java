package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import static com.company.MovingCircle.showMovingCircle;

class MainForm {
    JPanel createContentPane(int k, int radb) {
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);
        totalGUI.setBounds(1000, 200, 1000, 250);
        if (radb == 0){


        JTextField[] mt = new JTextField[k];
        JTextField[] rxt = new JTextField[k];
        JTextField[] ryt = new JTextField[k];
        JTextField[] vxt = new JTextField[k];
        JTextField[] vyt = new JTextField[k];

        JLabel[] m = new JLabel[k];
        JLabel[] rx = new JLabel[k];
        JLabel[] ry = new JLabel[k];
        JLabel[] vx = new JLabel[k];
        JLabel[] vy = new JLabel[k];
        JRadioButton[] r = new JRadioButton[k + 2];
        r[0] = new JRadioButton("Система центра масс");
        r[1] = new JRadioButton("ЛСО");
        for (int i = 0; i < k; i++) {
            m[i] = new JLabel("Введите Массу тела " + (i + 1) + "\n" + "\n в кг*10^30:");
            vx[i] = new JLabel("Введите скорость тела " + (i + 1) + "\n" + " по x в м/с:");
            vy[i] = new JLabel("Введите скорость тела " + (i + 1) + "\n" + " по y в м/с:");
            rx[i] = new JLabel("Введите координату тела " + (i + 1) + "\n" + " по x в а.е.:");
            ry[i] = new JLabel("Введите координату тела " + (i + 1) + "\n" + " по y в а.е.:");

            mt[i] = new JTextField(10);
            vxt[i] = new JTextField(10);
            vyt[i] = new JTextField(10);
            rxt[i] = new JTextField(10);
            ryt[i] = new JTextField(10);

            r[i + 2] = new JRadioButton("Система тела " + (i + 1));

        }
        for (int i = 0; i < k; i++) {
            m[i].setLocation(10, 10 + i * 90);
            m[i].setSize(230, 40);
            totalGUI.add(m[i]);
            mt[i].setLocation(10, 60 + i * 90);
            mt[i].setSize(230, 40);
            totalGUI.add(mt[i]);

            vx[i].setLocation(250, 10 + i * 90);
            vx[i].setSize(230, 40);
            totalGUI.add(vx[i]);
            vxt[i].setLocation(250, 60 + i * 90);
            vxt[i].setSize(230, 40);
            totalGUI.add(vxt[i]);

            vy[i].setLocation(490, 10 + i * 90);
            vy[i].setSize(230, 40);
            totalGUI.add(vy[i]);
            vyt[i].setLocation(490, 60 + i * 90);
            vyt[i].setSize(230, 40);
            totalGUI.add(vyt[i]);

            rx[i].setLocation(730, 10 + i * 90);
            rx[i].setSize(230, 40);
            totalGUI.add(rx[i]);
            rxt[i].setLocation(730, 60 + i * 90);
            rxt[i].setSize(230, 40);
            totalGUI.add(rxt[i]);

            ry[i].setLocation(970, 10 + i * 90);
            ry[i].setSize(230, 40);
            totalGUI.add(ry[i]);
            ryt[i].setLocation(970, 60 + i * 90);
            ryt[i].setSize(230, 40);
            totalGUI.add(ryt[i]);


        }
        ButtonGroup gr = new ButtonGroup();

        JButton button = new JButton("Test");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                double mz[] = new double[k];
                double vxz[] = new double[k];
                double vyz[] = new double[k];
                double rxz[] = new double[k];
                double ryz[] = new double[k];
                for (int i = 0; i < k; i++) {
                    mz[i] = Double.parseDouble(mt[i].getText());
                    vxz[i] = Double.parseDouble(vxt[i].getText());
                    vyz[i] = Double.parseDouble(vyt[i].getText());
                    rxz[i] = Double.parseDouble(rxt[i].getText());
                    ryz[i] = Double.parseDouble(ryt[i].getText());
                }
                int SO = 0;
                for (int i = 0; i < k + 2; i++) {
                    if (r[i].isSelected())
                        SO = i + 1;

                }

                showMovingCircle(mz, vxz, vyz, rxz, ryz, SO,k);
            }
        });


        for (int i = 0; i < k + 2; i++) {
            r[i].setLocation(1210, 10 + i * 45);
            r[i].setSize(230, 40);
            totalGUI.add(r[i]);
            gr.add(r[i]);
        }
        button.setLocation(1210, 10 + (k + 2) * 45);
        button.setSize(60, 40);
        totalGUI.add(button);
        r[0].setSelected(true);
        return (totalGUI);
    }
        else
            if(radb==2) {

                double mz[] = {1,1,1,1};
                double vxz[] = {0,35753.4848,0,-35753.4848};
                double vyz[] =  {-35753.4848,0,35753.4848,0};
                double rxz[] = {-1,0,1,0};
                double ryz[] = {0,-1,0,1};
                JLabel zn0 = new JLabel("Значения по умолчанию");
                JLabel zn1 = new JLabel("Первое тело."+" Координата x: "+rxz[0]+ "; Координата y: "+ryz[0]+ "; Скорость по x: "+vxz[0]+"; Скорость по y: "+vyz[0]+ "; Масса: "+mz[0]);
                JLabel zn2 = new JLabel("Второе тело."+" Координата x: "+rxz[1]+ "; Координата y: "+ryz[1]+ "; Скорость по x: "+vxz[1]+"; Скорость по y: "+vyz[1]+ "; Масса: "+mz[1]);
                JLabel zn3 = new JLabel("Третье тело."+" Координата x: "+rxz[2]+ "; Координата y: "+ryz[2]+ "; Скорость по x: "+vxz[2]+"; Скорость по y: "+vyz[2]+ "; Масса: "+mz[2]);
                JLabel zn4 = new JLabel("Четвертое тело."+" Координата x: "+rxz[3]+ "; Координата y: "+ryz[3]+ "; Скорость по x: "+vxz[3]+"; Скорость по y: "+vyz[3]+ "; Масса: "+mz[3]);
                zn0.setLocation(10, 20 );
                zn0.setSize(230, 40);

                zn1.setLocation(10, 70 );
                zn1.setSize(1000, 50);
                zn2.setLocation(10, 120 );
                zn2.setSize(1000, 50);
                zn3.setLocation(10, 170 );
                zn3.setSize(1000, 50);
                zn4.setLocation(10, 230 );
                zn4.setSize(1000, 25);

                showMovingCircle(mz, vxz, vyz, rxz, ryz, 1,4);
                totalGUI.add(zn0);
                totalGUI.add(zn1);
                totalGUI.add(zn2);
                totalGUI.add(zn3);
                totalGUI.add(zn4);
            }
        if(radb==1) {

            double mz[] = {1,1};
            double vxz[] = {0, 20000};
            double vyz[] = {0, -20000};
            double rxz[] = {0,0};
            double ryz[] = {3,0};
            showMovingCircle(mz, vxz, vyz, rxz, ryz, 1,2);
            JLabel zn0 = new JLabel("Значения по умолчанию");
            JLabel zn1 = new JLabel("Первое тело."+" Координата x: "+rxz[0]+ "; Координата y: "+ryz[0]+ "; Скорость по x: "+vxz[0]+"; Скорость по y: "+vyz[0]+ "; Масса: "+mz[0]);
            JLabel zn2 = new JLabel("Второе тело."+" Координата x: "+rxz[1]+ "; Координата y: "+ryz[1]+ "; Скорость по x: "+vxz[1]+"; Скорость по y: "+vyz[1]+ "; Масса: "+mz[1]);

            zn0.setLocation(10, 20 );
            zn0.setSize(230, 40);

            zn1.setLocation(10, 70 );
            zn1.setSize(1000, 50);
            zn2.setLocation(10, 120 );
            zn2.setSize(1000, 50);


            totalGUI.add(zn0);
            totalGUI.add(zn1);
            totalGUI.add(zn2);

        }
        if(radb==3) {
            double mz[] = {1,1,1};
            double vxz[] = {-21969.3886,0,-21969.3886};
            double vyz[] = {33700.974,0,33700.974};
            double rxz[] = {-2,0,2};
            double ryz[] = {0,0,0};
            showMovingCircle(mz, vxz, vyz, rxz, ryz, 1,3);
            JLabel zn0 = new JLabel("Значения по умолчанию");
            JLabel zn1 = new JLabel("Первое тело."+" Координата x: "+rxz[0]+ "; Координата y: "+ryz[0]+ "; Скорость по x: "+vxz[0]+"; Скорость по y: "+vyz[0]+ "; Масса: "+mz[0]);
            JLabel zn2 = new JLabel("Второе тело."+" Координата x: "+rxz[1]+ "; Координата y: "+ryz[1]+ "; Скорость по x: "+vxz[1]+"; Скорость по y: "+vyz[1]+ "; Масса: "+mz[1]);
            JLabel zn3 = new JLabel("Третье тело."+" Координата x: "+rxz[2]+ "; Координата y: "+ryz[2]+ "; Скорость по x: "+vxz[2]+"; Скорость по y: "+vyz[2]+ "; Масса: "+mz[2]);

            zn0.setLocation(10, 20 );
            zn0.setSize(230, 40);

            zn1.setLocation(10, 70 );
            zn1.setSize(1000, 50);
            zn2.setLocation(10, 120 );
            zn2.setSize(1000, 50);
            zn3.setLocation(10, 170 );
            zn3.setSize(1000, 50);


            totalGUI.add(zn0);
            totalGUI.add(zn1);
            totalGUI.add(zn2);
            totalGUI.add(zn3);

        }
        if(radb==4) {
            double mz[] = {1,1,1};
            double vxz[] = {-21969.3886,0,-21969.3886};
            double vyz[] = {33700.974,0,33700.974};
            double rxz[] = {-2,0,2};
            double ryz[] = {0,0,0};
            showMovingCircle(mz, vxz, vyz, rxz, ryz, 4,3);
            JLabel zn0 = new JLabel("Значения по умолчанию");
            JLabel zn1 = new JLabel("Первое тело."+" Координата x: "+rxz[0]+ "; Координата y: "+ryz[0]+ "; Скорость по x: "+vxz[0]+"; Скорость по y: "+vyz[0]+ "; Масса: "+mz[0]);
            JLabel zn2 = new JLabel("Второе тело."+" Координата x: "+rxz[1]+ "; Координата y: "+ryz[1]+ "; Скорость по x: "+vxz[1]+"; Скорость по y: "+vyz[1]+ "; Масса: "+mz[1]);
            JLabel zn3 = new JLabel("Третье тело."+" Координата x: "+rxz[2]+ "; Координата y: "+ryz[2]+ "; Скорость по x: "+vxz[2]+"; Скорость по y: "+vyz[2]+ "; Масса: "+mz[2]);

            zn0.setLocation(10, 20 );
            zn0.setSize(230, 40);

            zn1.setLocation(10, 70 );
            zn1.setSize(1000, 50);
            zn2.setLocation(10, 120 );
            zn2.setSize(1000, 50);
            zn3.setLocation(10, 170 );
            zn3.setSize(1000, 50);


            totalGUI.add(zn0);
            totalGUI.add(zn1);
            totalGUI.add(zn2);
            totalGUI.add(zn3);

        }
        if(radb==5) {

            double[] mz = {1, 1, 1, 1};
            double vxz[] = {0, 29753.4848, 0, -29753.4848};
            double vyz[] = {-29753.4848, 0, 29753.4848, 0};
            double rxz[] = {-0.7, 0, 0.7, 0};
            double ryz[] = {0, -0.7, 0, 0.7};
            showMovingCircle(mz, vxz, vyz, rxz, ryz, 1,4);
            JLabel zn0 = new JLabel("Значения по умолчанию");
            JLabel zn1 = new JLabel("Первое тело."+" Координата x: "+rxz[0]+ "; Координата y: "+ryz[0]+ "; Скорость по x: "+vxz[0]+"; Скорость по y: "+vyz[0]+ "; Масса: "+mz[0]);
            JLabel zn2 = new JLabel("Второе тело."+" Координата x: "+rxz[1]+ "; Координата y: "+ryz[1]+ "; Скорость по x: "+vxz[1]+"; Скорость по y: "+vyz[1]+ "; Масса: "+mz[1]);
            JLabel zn3 = new JLabel("Третье тело."+" Координата x: "+rxz[2]+ "; Координата y: "+ryz[2]+ "; Скорость по x: "+vxz[2]+"; Скорость по y: "+vyz[2]+ "; Масса: "+mz[2]);
            JLabel zn4 = new JLabel("Четвертое тело."+" Координата x: "+rxz[3]+ "; Координата y: "+ryz[3]+ "; Скорость по x: "+vxz[3]+"; Скорость по y: "+vyz[3]+ "; Масса: "+mz[3]);
            zn0.setLocation(10, 20 );
            zn0.setSize(230, 40);

            zn1.setLocation(10, 70 );
            zn1.setSize(1000, 50);
            zn2.setLocation(10, 120 );
            zn2.setSize(1000, 50);
            zn3.setLocation(10, 170 );
            zn3.setSize(1000, 50);
            zn4.setLocation(10, 230 );
            zn4.setSize(1000, 25);

            totalGUI.add(zn0);
            totalGUI.add(zn1);
            totalGUI.add(zn2);
            totalGUI.add(zn3);
            totalGUI.add(zn4);
        }
        if(radb==6) {

            double mz[] = {1, 0.3233,0};
            double vxz[] = {0, 0, 0};
            double vyz[] = {0, 26244, 47100};
            double rxz[] = {0, -1.92,-2.6};
            double ryz[] = {0, 0, 0};
            showMovingCircle(mz, vxz, vyz, rxz, ryz, 3,3);
            JLabel zn0 = new JLabel("Значения по умолчанию");
            JLabel zn1 = new JLabel("Первое тело."+" Координата x: "+rxz[0]+ "; Координата y: "+ryz[0]+ "; Скорость по x: "+vxz[0]+"; Скорость по y: "+vyz[0]+ "; Масса: "+mz[0]);
            JLabel zn2 = new JLabel("Второе тело."+" Координата x: "+rxz[1]+ "; Координата y: "+ryz[1]+ "; Скорость по x: "+vxz[1]+"; Скорость по y: "+vyz[1]+ "; Масса: "+mz[1]);
            JLabel zn3 = new JLabel("Третье тело."+" Координата x: "+rxz[2]+ "; Координата y: "+ryz[2]+ "; Скорость по x: "+vxz[2]+"; Скорость по y: "+vyz[2]+ "; Масса: "+mz[2]);

            zn0.setLocation(10, 20 );
            zn0.setSize(230, 40);

            zn1.setLocation(10, 70 );
            zn1.setSize(1000, 50);
            zn2.setLocation(10, 120 );
            zn2.setSize(1000, 50);
            zn3.setLocation(10, 170 );
            zn3.setSize(1000, 50);

            totalGUI.add(zn0);
            totalGUI.add(zn1);
            totalGUI.add(zn2);
            totalGUI.add(zn3);

        }
        return(totalGUI);

}
}