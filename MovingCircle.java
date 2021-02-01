package com.company;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.*;

public class MovingCircle extends JComponent implements ActionListener {
    private double f =0;
    private double scale;
    private Color color;
    private Timer timer;
    public double x1 =100;
    public double y1 =100;
    public double a1 =100;
    public double e1 =0.70;
    double m1=Math.pow(10,15);
    double m2=Math.pow(10,15);
    double m3=Math.pow(10,15);
    Vect v1;
    Vect v2;
    Vect v3;
    Vect r1;
    Vect r2;
    Vect r3;
    public MovingCircle(Color color, int delay) {
        scale = 1.0;
        timer = new Timer(delay, this);
        this.color = color;
        setPreferredSize(new Dimension(1000, 1000));
        v1=new Vect();
        v1.x=20;
        v1.y=-10;
        r1=new Vect();
        r1.x=450;//*1.5*Math.pow(10,11);
        r1.y=350;//*1.5*Math.pow(10,11);;
        v2=new Vect();
        v2.x=0;
        v2.y=-10;
        r2=new Vect();
        r2.x=200;//*1.5*Math.pow(10,11);;
        r2.y=100;//*1.5*Math.pow(10,11);;
        v3=new Vect();
        v3.x=0;
        v3.y=10;
        r3=new Vect();
        r3.x=300;//*1.5*Math.pow(10,11);;
        r3.y=100; //*1.5*Math.pow(10,11);;
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }


    public static Vect a(Vect r1,Vect r2,Vect r3,double m1, double m2, double m3 ) {
        Vect r21 = r1.VMi(r2);
        Vect r31 = r1.VMi(r3);
        Vect f2=r21.VSp(6.67*Math.pow(10,-11)*m1*m2/Math.pow(r21.Le(),2)/r21.Le());
        Vect f3=r31.VSp(6.67*Math.pow(10,-11)*m1*m2/Math.pow(r31. Le(),2)/r31.Le());
        Vect f=f2.VSum(f3);
        Vect a=f.VSp(1/m1);
        return(a);
    }
    public static Vect v(Vect v,Vect a,double ti) {
        Vect dv = a.VSp(ti);
        Vect v1 = v.VSum(dv);
        return v1;
    }
    public static Vect r(Vect r,Vect v,double ti) {
        Vect dr = v.VSp(ti*315000000);
        Vect r1 = v.VSum(dr);
        return r1;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        repaint();
    }

    @Override

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        int width = 1000;
        int height = 1000;
        g.fillRect(0, 0, width, height);
        g2d.setColor(Color.black);
        g2d.drawRect(0, 0, width - 1, height - 1);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.scale(scale, scale);

        Vect u1=a(r1,r2,r3,m1,m2,m3);
        Vect u2=a(r2,r1,r3,m2,m3,m1);
        Vect u3=a(r3,r1,r2,m3,m1,m2);

        v1=v(v1,u1,0.02);
        v2=v(v2,u2,0.02);
        v3=v(v3,u3,0.02);

        r1=v(r1,v1,0.02);
        r2=v(r2,v2,0.02);
        r3=v(r3,v3,0.02);


        g2d.setColor(Color.BLUE);
        Ellipse2D el1 = new Ellipse2D.Double(r1.x+200, r1.y+200, 20, 20);
        g2d.fill(el1);
        g2d.setColor(Color.GREEN);
        Ellipse2D el2 = new Ellipse2D.Double(r2.x+200, r2.y+200, 20, 20);
        g2d.fill(el2);
        g2d.setColor(Color.GRAY);
        Ellipse2D el3 = new Ellipse2D.Double(r3.x+200, r3.y+200, 20, 20);
        g2d.fill(el3);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                JFrame frame = new JFrame("Moving Circle");
                JPanel panel = new JPanel();
                final MovingCircle MovingCircleB = new MovingCircle(Color.BLUE, 5);
                panel.add(MovingCircleB);
                frame.getContentPane().add(panel);
                final JButton bst = new JButton("Start");

                final JTextField tfm1 = new JTextField(10);
                final JTextField tfm2 = new JTextField (10);
                final JTextField tfm3 = new JTextField (10);
                final JTextField tfa1 = new JTextField (10);
                final JTextField tfa2 = new JTextField (10);
                final JTextField tfa3 = new JTextField (10);
                final JTextField tfe1 = new JTextField (10);;
                final JTextField tfe2 = new JTextField (10);
                final JTextField tfe3 = new JTextField (10 );

                bst.addActionListener(new ActionListener() {
                    private boolean pulsing = false;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (pulsing) {
                            pulsing = false;
                            MovingCircleB.stop();
                            bst.setText("Start");

                        } else {
                            pulsing = true;
                            MovingCircleB.start();
                            bst.setText("Stop");
                        }

                    }
                });
                panel.add(bst);
                panel.add(tfm1);
                panel.add(tfm2);
                panel.add(tfm3);
                panel.add(tfa1);
                panel.add(tfa2);
                panel.add(tfa3);
                panel.add(tfe1);
                panel.add(tfe2);
                panel.add(tfe3);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1100, 1100);
                frame.setVisible(true);
            }
        });
    }
}
