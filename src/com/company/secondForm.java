package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class secondForm {
    public JPanel createContentPane() {
        JPanel kolGUI = new JPanel();
        kolGUI.setLayout(null);

        kolGUI.setBounds(1000, 200, 1000, 250);
        JTextField kol = new JTextField();
        JLabel vvod = new JLabel();
        vvod = new JLabel("Введите колличество тел:");
        vvod.setLocation(20, 260);
        vvod.setSize(200, 40);
        kolGUI.add(vvod);
        kol.setLocation(180, 260);
        kol.setSize(100, 40);
        kolGUI.add(kol);
        JLabel label = new JLabel("Несколько примеров:");
        label.setLocation(20, 10);
        label.setSize(200, 20);
        kolGUI.add(label);
        ButtonGroup gr = new ButtonGroup();
        JRadioButton r0 = new JRadioButton("Задать свою систему");
        JRadioButton r1 = new JRadioButton("2 тела");
        JRadioButton r2 = new JRadioButton("4 тела в вершине квадрата");
        JRadioButton r3 = new JRadioButton("Восьмерка в СО ц.м.");
        JRadioButton r4 = new JRadioButton("Восьмерка в СО тела");
        JRadioButton r5 = new JRadioButton("4 тела, неустойчивая система");
        JRadioButton r6 = new JRadioButton("Планета со спутником");
        gr.add(r0);
        gr.add(r1);
        gr.add(r2);
        gr.add(r3);
        gr.add(r4);
        gr.add(r5);
        gr.add(r6);
        r0.setLocation(20,230);
        r0.setSize(180, 30);
        r1.setLocation(20,30);
        r1.setSize(250, 40);
        r2.setLocation(20,60);
        r2.setSize(250, 40);
        r3.setLocation(20,90);
        r3.setSize(250, 40);
        r4.setLocation(20,120);
        r4.setSize(250, 40);
        r5.setLocation(20,150);
        r5.setSize(250, 40);
        r6.setLocation(20,180);
        r6.setSize(250, 40);
        kolGUI.add(r0);
        kolGUI.add(r1);
        kolGUI.add(r2);
        kolGUI.add(r3);
        kolGUI.add(r4);
        kolGUI.add(r5);
        kolGUI.add(r6);
        JButton button = new JButton("Начать");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int radb=0;
                if(r0.isSelected())
                    radb=0;
                if(r1.isSelected())
                    radb=1;
                if(r2.isSelected())
                    radb=2;
                if(r3.isSelected())
                    radb=3;
                if(r4.isSelected())
                    radb=4;
                if(r5.isSelected())
                    radb=5;
                if(r6.isSelected())
                    radb=6;
                int k=1;
                if(!kol.getText().equals(""))
                k= Integer.parseInt(kol.getText());

                JFrame.setDefaultLookAndFeelDecorated(true);
                JFrame frame=new JFrame("Начальные параметры");
                MainForm demo=new MainForm();
                frame.setContentPane(demo.createContentPane(k,radb));
                int w;
                if(k==1) w =(3)*45+50;
                else
                    w= (k)*90+50;

                frame.setSize( 1450,w);
                frame.setVisible(true);
            }
        });

        button.setLocation(180,300);
        button.setSize(90, 40);
        kolGUI.add(button);

        return (kolGUI);
    }
}
