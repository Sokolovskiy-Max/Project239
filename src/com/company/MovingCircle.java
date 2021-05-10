package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class MovingCircle extends JComponent implements ActionListener {

    private int k = 0;

    private double scale;
    private Color color;
    private Timer timer;
    private double[] m;

    private Vect[] v;
    private Vect[] r;
    private Vect[] a;


    private int[] del;
    private int kol;

    private static boolean[] Bodyes;
    private static boolean ax;
    private static boolean rv;
    private static boolean vv;
    private static boolean av;
    private static boolean tr;
    private static boolean infXY;
    private static boolean infV;
    private static int traectories_st = 1;
    private static int fast;
    private static int it;
    private Vect Vcm, rcm;
    private int s;
    private double[][] traektoriesx;
    private double[][] traektoriesy;


    private MovingCircle(Color color, int delay, double[] m0, double[] vx, double[] vy, double[] rx, double[] ry, int SO, int count) {

        kol = count;
        del = new int[kol];
        a = new Vect[kol];
        v = new Vect[kol];
        r = new Vect[kol];
        m = new double[kol];


        traektoriesx = new double[kol][100000];
        traektoriesy = new double[kol][100000];
        s = SO;

        for (int i = 0; i < kol; i++) {
            v[i] = new Vect();
            a[i] = new Vect();
            v[i].x = vx[i];
            v[i].y = vy[i];
            a[i].x = 0;
            a[i].y = 0;
            del[i] = -1;
            r[i] = new Vect();
            r[i].x = rx[i] * 1.5 * Math.pow(10, 11);
            r[i].y = ry[i] * 1.5 * Math.pow(10, 11);
            m[i] = m0[i] * 2 * Math.pow(10, 30);

        }
        Vcm = new Vect();
        double M = 0;
        for (int i = 0; i < kol; i++) {
            Vcm = Vcm.VSum(v[i].VSp(m[i]));
            M = M + m[i];
        }
        Vcm = Vcm.VSp(1 / M);
        scale = 1.0;
        timer = new Timer(delay, this);
        this.color = color;
        rcm = new Vect();
        for (int i = 0; i < kol; i++) {
            rcm = rcm.VSum(r[i].VSp(m[i]));
        }
        rcm = rcm.VSp(1 / M);


    }

    private void restart(double[] rsx, double[] rsy, double[] vsx, double[] vsy, double[] ms) {
        for (int i = 0; i < kol; i++) {
            v[i].x = vsx[i];
            v[i].y = vsy[i];
            r[i].x = rsx[i] * 1.5 * Math.pow(10, 11);
            r[i].y = rsy[i] * 1.5 * Math.pow(10, 11);
            m[i] = ms[i] * 2 * Math.pow(10, 30);
            del[i] = 1000;
        }

        traectories_st = it + 1;
    }

    private void start() {
        timer.start();
    }

    private void stop() {
        timer.stop();
    }


    private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        g.drawLine(0, 0, len, 0);
        int ARR_SIZE = 8;
        g.fillPolygon(new int[] {len, len- ARR_SIZE, len- ARR_SIZE, len},
                new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }

    private static Vect a(Vect[] r, double[] m, int kol, int n) {
        Vect[] a = new Vect[kol];
        Vect ao = new Vect();
        ao.x = 0;
        ao.y = 0;
        for (int i = 0; i < kol; i++) {
            a[i] = new Vect();
            if (i != n) {
                a[i] = (r[n].VMi(r[i])).VSp((m[i] * (-6.67 * Math.pow(10, -11)) / Math.pow((r[n].VMi(r[i])).Le(), 3)));
            } else {
                a[i].x = 0;
                a[i].y = 0;
            }
            ao = ao.VSum(a[i]);
        }
        return (ao);
    }

    private static Vect v(Vect v, Vect a, double ti) {
        Vect dv = a.VSp(ti);
        return v.VSum(dv);
    }

    private static Vect r(Vect r, Vect v, double ti) {
        Vect dr = (v).VSp(ti);
        return r.VSum(dr);
    }

    private static Vect rcm(Vect rcm, Vect Vcm, double ti) {
        Vect drcm = Vcm.VSp(ti);
        return rcm.VSum(drcm);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        repaint();
    }
    @Override

    public void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        int width = 1300;
        int height = 1000;
        g.fillRect(0, 0, width, height);
        g2d.setColor(Color.black);
        g2d.drawRect(0, 0, width - 1, height - 1);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.scale(scale, scale);
        double[] rad=new double[kol];

        rcm = rcm(rcm, Vcm, fast);


        for (int i = 0; i < kol; i++) {
            for (int j = i + 1; j < kol; j++) {
                boolean b=true;
                for(int k=0;k<kol;k++) {
                    if (del[k] == i || del[k] == j) {
                        b = false;
                        break;
                    }
                }
                if (b) {
                    if (Math.abs((r[i].VMi(r[j])).Le()) < 1 * Math.pow(10, 10)) {
                        if(j!=s-3) //чтобы в СО i-го тела не пропадал центр
                        {
                            del[k] = j;
                            v[i] = ((v[i].VSp(m[i])).VSum(v[j].VSp(m[j]))).VSp(1 / (m[i] + m[j]));
                            m[i] = m[j] + m[i];
                            m[j] = 0;
                            r[j].y = 0;
                            r[j].x = 5000;
                            v[j].y = 0;
                            v[j].x = 0;
                        }
                        else {
                            del[k] = i;
                            v[j] = ((v[j].VSp(m[j])).VSum(v[i].VSp(m[i]))).VSp(1 / (m[i] + m[j]));
                            m[j] = m[j] + m[i];
                            m[i] = 0;
                            r[i].y = 0;
                            r[i].x = 5000;
                            v[i].y = 0;
                            v[i].x = 0;
                        }
                        k++;
                    }
                }

            }
         rad[i]=10*(1+Math.log10(m[i]/2/Math.pow(10,29)));
            if(rad[i]<8)
                rad[i]=8;

        }

//оси
        if(ax) {
            g2d.setColor(Color.black);
            g2d.drawString("X",984,520);
            g2d.drawString("Y",482,19);
            drawArrow(g2d,500,990,500,10);
            drawArrow(g2d,10,500,990,500);

            g2d.drawLine(900,496,900,504);
            g2d.drawLine(800,496,800,504);
            g2d.drawLine(700,496,700,504);
            g2d.drawLine(600,496,600,504);
            g2d.drawLine(400,496,400,504);
            g2d.drawLine(300,496,300,504);
            g2d.drawLine(200,496,200,504);
            g2d.drawLine(100,496,100,504);

            g2d.drawLine(496,900,504,900);
            g2d.drawLine(496,800,504,800);
            g2d.drawLine(496,700,504,700);
            g2d.drawLine(496,600,504,600);
            g2d.drawLine(496,400,504,400);
            g2d.drawLine(496,300,504,300);
            g2d.drawLine(496,200,504,200);
            g2d.drawLine(496,100,504,100);

        }
        if(infXY){
            g2d.setColor(new Color(240,240,240));
            g.fillRect(1002,0,298,1000);
            g2d.setColor(new Color(183, 127,0));
            g.fillRect(1000,0,2,1000);
            g.fillRect(1298,0,2,1000);
            g2d.setColor(Color.black);
            g.drawString("Декартова система координат", 1020, 20);
            }
        if(infV){
            g2d.setColor(new Color(240,240,240));
            g.fillRect(1002,0,298,1000);
            g2d.setColor(new Color(183, 127,0));
            g.fillRect(1000,0,2,1000);
            g.fillRect(1298,0,2,1000);
            g2d.setColor(Color.black);
            g.drawString("Полярная система координат", 1020, 20);
        }
        if(s==1) //центр масс

            for (int i = 0; i < kol; i++) {
                //Отрисовка траекторий
                if(tr) {
                    g2d.setColor(new Color(255-i * 255/kol, i * 255/kol, i * 255/kol));
                    for (int j = traectories_st; j < it + 1; j++) {
                        if ((traektoriesx[i][j + 1] != 0) && (traektoriesy[i][j + 1] != 0)) {
                            boolean b=true;
                            for(int k=0;k<kol;k++) {
                                if (del[k] == i) {
                                    b = false;
                                    break;
                                }
                            }
                            if (!b)
                                g2d.setColor(Color.red);
                            g2d.drawLine((int) (traektoriesx[i][j]), (int) traektoriesy[i][j], (int) traektoriesx[i][j + 1], (int) traektoriesy[i][j + 1]);
                        }
                    }
                }
                //Отрисовка тел и вывод значений
                boolean b=true;
                for(int k=0;k<kol;k++) {
                    if (del[k] == i) {
                        b = false;
                        break;
                    }
                }
                if (b) {

                    //реакция на нажатие
                    if(Bodyes[i]) {
                        g2d.setColor(new Color(255-i * 255/kol, i * 255/kol, i * 255/kol));
                        Ellipse2D el = new Ellipse2D.Double(
                                (r[i].x - rcm.x) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))),
                                (r[i].y - rcm.y) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))),
                                rad[i]*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))),
                                rad[i]*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))));
                        g2d.fill(el);
                    }
                    g2d.setColor(new Color(255-i * 255/kol, i * 255/kol, i * 255/kol));
                    Ellipse2D el = new Ellipse2D.Double((r[i].x - rcm.x) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2,
                            (r[i].y - rcm.y) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2, rad[i], rad[i]);
                    if(!Bodyes[i])
                    g2d.fill(el);

                    traektoriesx[i][it+1]=(r[i].x - rcm.x) / (1.5 * Math.pow(10, 9)) + 500;
                    traektoriesy[i][it+1]=(r[i].y - rcm.y) / (1.5 * Math.pow(10, 9)) + 500;
                    //вектора скоростей
                    if(vv){
                        g2d.setColor(Color.GREEN);
                        drawArrow(g2d,(int)((r[i].x - rcm.x) / (1.5 * Math.pow(10, 9)) + 500), (int)((r[i].y - rcm.y) / (1.5 * Math.pow(10, 9)) + 500),
                                (int)((r[i].x - rcm.x) / (1.5 * Math.pow(10, 9)) + 500+v[i].x/200),(int)((r[i].y - rcm.y) / (1.5 * Math.pow(10, 9)) + 500+v[i].y/200));
                    }
                    //вектора ускорений
                    if(av){
                        g2d.setColor(Color.BLUE);
                        drawArrow(g2d,(int)((r[i].x - rcm.x) / (1.5 * Math.pow(10, 9)) + 500), (int)((r[i].y - rcm.y) / (1.5 * Math.pow(10, 9)) + 500),
                                (int)((r[i].x - rcm.x) / (1.5 * Math.pow(10, 9)) + 500+300*(Math.pow((a[i]).Le(),0.2)*a[i].x/(a[i]).Le())),
                                (int)((r[i].y - rcm.y) / (1.5 * Math.pow(10, 9)) + 500+300*(Math.pow((a[i]).Le(),0.2)*a[i].y/(a[i]).Le())));
                    }
                    //радиус вектора
                    if(rv){
                        g2d.setColor(Color.ORANGE);
                        drawArrow(g2d,500,500,(int)((r[i].x - rcm.x) / (1.5 * Math.pow(10, 9)) + 500), (int)((r[i].y - rcm.y) / (1.5 * Math.pow(10, 9)) + 500));
                    }
                    if(infXY) {
                        g2d.setColor(new Color(0, 0, 255));
                        if(Bodyes[i])
                            g2d.setColor(new Color(191, 0, 64));
                        //g.drawString("Координатная форма", 1010, 20);
                        g.drawString("Координата x тела " + (i + 1) + ": " + String.format("%.2f", (r[i].x - rcm.x) / (1.5 * Math.pow(10, 11))) + " а.е", 1020, 60 + 40 * i);
                        g.drawString("Координата y тела " + (i + 1) + ": " + String.format("%.2f", (r[i].y - rcm.y) / (1.5 * Math.pow(10, 11))) + " а.е", 1020, 80 + 40 * i);
                        g.drawString("Скорось по оси x тела " + (i + 1) + ": " + String.format("%.2f", (v[i].x - Vcm.x)) + " м/с", 1020, 80 + kol * 40 + 40 * i);
                        g.drawString("Скорось по оси y тела " + (i + 1) + ": " + String.format("%.2f", (v[i].y - Vcm.y)) + " м/с", 1020, 100 + kol * 40 + 40 * i);
                        g.drawString("Ускорение по оси x тела " + (i + 1) + ": " + String.format("%.2f", (a[i].x) * 1000) + " м/с^2 * 10^-3", 1020, 100 + kol * 80 + 40 * i);
                        g.drawString("Ускорение  по оси y тела " + (i + 1) + ": " + String.format("%.2f", (a[i].y) * 1000) + " м/с^2 * 10^-3", 1020, 120 + kol * 80 + 40 * i);
                        g.drawString("Масса тела " + (i + 1) + ": " + String.format("%.2f", m[i] / Math.pow(10, 30) / 2) + " м.С.", 1020, 120 + kol * 120 + 20 * i);
                    }
                    if(infV) {
                        g2d.setColor(new Color(0, 0, 255));
                        if(Bodyes[i])
                            g2d.setColor(new Color(191, 0, 64));
                        g.drawString("Радиус-вектор " + (i + 1) + "-го тела: " + String.format("%.2f", (r[i].VMi(rcm)).Le() / (1.5 * Math.pow(10, 11)))+ " а.е", 1010, 60 + 40 * i);
                        g.drawString("Угол р-в " + (i + 1) + "-го тела : " + String.format("%.2f", Math.atan((r[i].y - rcm.y)/(r[i].x - rcm.x))*180/3.14 )+ " гр.", 1010, 80 + 40 * i);
                        g.drawString("Вектор скорости " + (i + 1) + "-го тела: " + String.format("%.2f", (v[i].VMi(Vcm)).Le() )+ " м/с", 1010, 80 + kol * 40 + 40 * i);
                        g.drawString("Угол в.с. " + (i + 1) + "-го тела: " + String.format("%.2f", Math.atan((v[i].y - Vcm.y)/(v[i].x - Vcm.x))*180/3.14 )+ " гр.", 1010, 100 + kol * 40 + 40 * i);
                        g.drawString("Вектор ускорения " + (i + 1) + "-го тела: " + String.format("%.2f", (a[i]).Le()*1000 )+ " м/с^2 * 10^-3", 1010, 100 + kol * 80 + 40 * i);
                        g.drawString("Угол в.с. " + (i + 1) + "-го тела: " + String.format("%.2f", Math.atan(a[i].y/a[i].x)*180/3.14 )+ " гр.", 1010, 120 + kol * 80 + 40 * i);
                        g.drawString("Масса тела " + (i + 1) + ": " + String.format("%.2f", m[i] / Math.pow(10, 30) / 2)+ " м.С.", 1010, 120 + kol * 120 + 20 * i);
                    }
                }

            }
        else
            if(s==2) //ЛСО
                for (int i = 0; i < kol; i++) {
                    //Отрисовка траекторий
                    if(tr) {
                        g2d.setColor(new Color(255-i * 255/kol, i * 255/kol, i * 255/kol));
                        for (int j = traectories_st; j < it + 1; j++) {
                            if ((traektoriesx[i][j + 1] != 0) && (traektoriesy[i][j + 1] != 0)) {
                                boolean b=true;
                                for(int k=0;k<kol;k++) {
                                    if (del[k] == i) {
                                        b = false;
                                        break;
                                    }
                                }
                                if (!b)
                                    g2d.setColor(Color.red);
                                g2d.drawLine((int) (traektoriesx[i][j]), (int) traektoriesy[i][j], (int) traektoriesx[i][j + 1], (int) traektoriesy[i][j + 1]);
                            }
                        }
                    }
                    //Отрисовка тел и вывод значений
                    boolean b=true;
                    for(int k=0;k<kol;k++) {
                        if (del[k] == i) {
                            b = false;
                            break;
                        }
                    }
                    if (b) {
                        if(infXY) {
                            g2d.setColor(new Color(0, 0, 255));
                            if(Bodyes[i])
                                g2d.setColor(new Color(191, 0, 64));
                            //g.drawString("Координатная форма", 1010, 20);
                            g.drawString("Координата x тела " + (i + 1) + ": " + String.format("%.2f", (r[i].x) / (1.5 * Math.pow(10, 11))) + " а.е", 1020, 60 + 40 * i);
                            g.drawString("Координата y тела " + (i + 1) + ": " + String.format("%.2f", (r[i].y) / (1.5 * Math.pow(10, 11))) + " а.е", 1020, 80 + 40 * i);
                            g.drawString("Скорось по оси x тела " + (i + 1) + ": " + String.format("%.2f", v[i].x ) + " м/с", 1020, 80 + kol * 40 + 40 * i);
                            g.drawString("Скорось по оси y тела " + (i + 1) + ": " + String.format("%.2f",v[i].y)+ " м/с", 1020, 100 + kol * 40 + 40 * i);
                            g.drawString("Ускорение по оси x тела " + (i + 1) + ": " + String.format("%.2f", (a[i].x) * 1000) + " м/с^2 * 10^-3", 1020, 100 + kol * 80 + 40 * i);
                            g.drawString("Ускорение  по оси y тела " + (i + 1) + ": " + String.format("%.2f", (a[i].y) * 1000) + " м/с^2 * 10^-3", 1020, 120 + kol * 80 + 40 * i);
                            g.drawString("Масса тела " + (i + 1) + ": " + String.format("%.2f", m[i] / Math.pow(10, 30) / 2) + " м.С.", 1020, 120 + kol * 120 + 20 * i);
                        }
                        if(infV) {
                            g2d.setColor(new Color(0, 0, 255));
                            if(Bodyes[i])
                                g2d.setColor(new Color(191, 0, 64));
                            g.drawString("Радиус-вектор " + (i + 1) + "-го тела: " + String.format("%.2f", (r[i]).Le() / (1.5 * Math.pow(10, 11)))+ " а.е", 1010, 60 + 40 * i);
                            g.drawString("Угол р-в " + (i + 1) + "-го тела : " + String.format("%.2f", Math.atan((r[i].y)/r[i].x)*180/3.14 )+ " гр.", 1010, 80 + 40 * i);
                            g.drawString("Вектор скорости " + (i + 1) + "-го тела: " + String.format("%.2f", (v[i]).Le() )+ " м/с", 1010, 80 + kol * 40 + 40 * i);
                            g.drawString("Угол в.с. " + (i + 1) + "-го тела: " + String.format("%.2f", Math.atan(v[i].y /(v[i].x ))*180/3.14 )+ " гр.", 1010, 100 + kol * 40 + 40 * i);
                            g.drawString("Вектор ускорения " + (i + 1) + "-го тела: " + String.format("%.2f", (a[i]).Le()*1000 )+ " м/с^2 * 10^-3", 1010, 100 + kol * 80 + 40 * i);
                            g.drawString("Угол в.с. " + (i + 1) + "-го тела: " + String.format("%.2f", Math.atan(a[i].y/a[i].x)*180/3.14 )+ " гр.", 1010, 120 + kol * 80 + 40 * i);
                            g.drawString("Масса тела " + (i + 1) + ": " + String.format("%.2f", m[i] / Math.pow(10, 30) / 2)+ " м.С.", 1010, 120 + kol * 120 + 20 * i);
                        }
                        //реакция на нажатие
                        if(Bodyes[i]) {
                            g2d.setColor(new Color(255-i * 255/kol, i * 255/kol, i * 255/kol));
                            Ellipse2D el = new Ellipse2D.Double(
                                    (r[i].x) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))),
                                    (r[i].y) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))),
                                    rad[i]*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))),
                                    rad[i]*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))));
                            g2d.fill(el);
                        }
                        g2d.setColor(new Color(255-i * 255/kol, i * 255/kol, i * 255/kol));
                        Ellipse2D el = new Ellipse2D.Double((r[i].x) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2,
                                (r[i].y) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2, rad[i], rad[i]);
                        if(!Bodyes[i])
                            g2d.fill(el);

                        traektoriesx[i][it+1]=(r[i].x) / (1.5 * Math.pow(10, 9)) + 500;
                        traektoriesy[i][it+1]=(r[i].y) / (1.5 * Math.pow(10, 9)) + 500;
                        //вектора скоростей
                        if(vv){
                            g2d.setColor(Color.GREEN);
                            drawArrow(g2d,(int)((r[i].x) / (1.5 * Math.pow(10, 9)) + 500), (int)((r[i].y) / (1.5 * Math.pow(10, 9)) + 500),
                                    (int)((r[i].x) / (1.5 * Math.pow(10, 9)) + 500+v[i].x/200),(int)((r[i].y) / (1.5 * Math.pow(10, 9)) + 500+v[i].y/200));
                        }
                        //вектора ускорений
                        if(av){
                            g2d.setColor(Color.BLUE);
                            drawArrow(g2d,(int)((r[i].x) / (1.5 * Math.pow(10, 9)) + 500), (int)((r[i].y) / (1.5 * Math.pow(10, 9)) + 500),
                                    (int)((r[i].x) / (1.5 * Math.pow(10, 9)) + 500+300*(Math.pow((a[i]).Le(),0.2)*a[i].x/(a[i]).Le())),
                                    (int)((r[i].y) / (1.5 * Math.pow(10, 9)) + 500+300*(Math.pow((a[i]).Le(),0.2)*a[i].y/(a[i]).Le())));
                        }
                        //радиус вектора
                        if(rv){
                            g2d.setColor(Color.ORANGE);
                            drawArrow(g2d,500,500,(int)((r[i].x) / (1.5 * Math.pow(10, 9)) + 500), (int)((r[i].y) / (1.5 * Math.pow(10, 9)) + 500));
                        }
                    }

                }

            else
                if(s>2) //i-е тело
                    for (int i = 0; i < kol; i++) {
                        //Отрисовка траекторий
                        if(tr) {
                            g2d.setColor(new Color(255-i * 255/kol, i * 255/kol, i * 255/kol));

                            for (int j = traectories_st+1; j < it + 1; j++) {
                                if ((traektoriesx[i][j + 1] != 0) && (traektoriesy[i][j + 1] != 0)) {
                                    boolean b=true;
                                    for(int k=0;k<kol;k++) {
                                        if (del[k] == i) {
                                            b = false;
                                            break;
                                        }
                                    }
                                    if (!b)
                                        g2d.setColor(Color.red);
                                    g2d.drawLine((int) (traektoriesx[i][j]), (int) traektoriesy[i][j], (int) traektoriesx[i][j + 1], (int) traektoriesy[i][j + 1]);
                                }
                            }
                        }
                        //Отрисовка тел и вывод значений
                        boolean b=true;
                        for(int k=0;k<kol;k++) {
                            if (del[k] == i) {
                                b = false;
                                break;
                            }
                        }
                        if (b) {
                            if(infXY) {
                                g2d.setColor(new Color(0, 0, 255));
                                if(Bodyes[i])
                                    g2d.setColor(new Color(191, 0, 64));
                                //g.drawString("Координатная форма", 1010, 20);
                                g.drawString("Координата x тела " + (i + 1) + ": " + String.format("%.2f", (r[i].x - r[s-3].x) / (1.5 * Math.pow(10, 11))) + " а.е", 1020, 60 + 40 * i);
                                g.drawString("Координата y тела " + (i + 1) + ": " + String.format("%.2f", (r[i].y - r[s-3].y) / (1.5 * Math.pow(10, 11))) + " а.е", 1020, 80 + 40 * i);
                                g.drawString("Скорось по оси x тела " + (i + 1) + ": " + String.format("%.2f", (v[i].x - v[s-3].x)) + " м/с", 1020, 80 + kol * 40 + 40 * i);
                                g.drawString("Скорось по оси y тела " + (i + 1) + ": " + String.format("%.2f", (v[i].y - v[s-3].y)) + " м/с", 1020, 100 + kol * 40 + 40 * i);
                                g.drawString("Ускорение по оси x тела " + (i + 1) + ": " + String.format("%.2f", (a[i].x) * 1000) + " м/с^2 * 10^-3", 1020, 100 + kol * 80 + 40 * i);
                                g.drawString("Ускорение  по оси y тела " + (i + 1) + ": " + String.format("%.2f", (a[i].y) * 1000) + " м/с^2 * 10^-3", 1020, 120 + kol * 80 + 40 * i);
                                g.drawString("Масса тела " + (i + 1) + ": " + String.format("%.2f", m[i] / Math.pow(10, 30) / 2) + " м.С.", 1020, 120 + kol * 120 + 20 * i);
                            }
                            if(infV) {
                                g2d.setColor(new Color(0, 0, 255));
                                if(Bodyes[i])
                                    g2d.setColor(new Color(191, 0, 64));
                                g.drawString("Радиус-вектор " + (i + 1) + "-го тела: " + String.format("%.2f", (r[i].VMi(r[s-3])).Le() / (1.5 * Math.pow(10, 11)))+ " а.е", 1010, 60 + 40 * i);
                                g.drawString("Угол р-в " + (i + 1) + "-го тела : " + String.format("%.2f", Math.atan((r[i].y - r[s-3].y)/(r[i].x - r[s-3].x))*180/3.14 )+ " гр.", 1010, 80 + 40 * i);
                                g.drawString("Вектор скорости " + (i + 1) + "-го тела: " + String.format("%.2f", (v[i].VMi(v[s-3])).Le() )+ " м/с", 1010, 80 + kol * 40 + 40 * i);
                                g.drawString("Угол в.с. " + (i + 1) + "-го тела: " + String.format("%.2f", Math.atan((v[i].y - v[s-3].y)/(v[i].x - v[s-3].x))*180/3.14 )+ " гр.", 1010, 100 + kol * 40 + 40 * i);
                                g.drawString("Вектор ускорения " + (i + 1) + "-го тела: " + String.format("%.2f", (a[i]).Le()*1000 )+ " м/с^2 * 10^-3", 1010, 100 + kol * 80 + 40 * i);
                                g.drawString("Угол в.с. " + (i + 1) + "-го тела: " + String.format("%.2f", Math.atan(a[i].y/a[i].x)*180/3.14 )+ " гр.", 1010, 120 + kol * 80 + 40 * i);
                                g.drawString("Масса тела " + (i + 1) + ": " + String.format("%.2f", m[i] / Math.pow(10, 30) / 2)+ " м.С.", 1010, 120 + kol * 120 + 20 * i);
                            }
                            //реакция на нажатие
                            if(Bodyes[i]) {
                                g2d.setColor(new Color(255-i * 255/kol, i * 255/kol, i * 255/kol));
                                Ellipse2D el = new Ellipse2D.Double(
                                        (r[i].x - r[s-3].x) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))),
                                        (r[i].y - r[s-3].y) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))),
                                        rad[i]*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))),
                                        rad[i]*(0.5+Math.abs(Math.sin(3.14/2+(double)(it+100*i)/50))));
                                g2d.fill(el);
                            }
                            g2d.setColor(new Color(255-i * 255/kol, i * 255/kol, i * 255/kol));
                            Ellipse2D el = new Ellipse2D.Double((r[i].x - r[s-3].x) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2,
                                    (r[i].y - r[s-3].y) / (1.5 * Math.pow(10, 9)) + 500-rad[i]/2, rad[i], rad[i]);
                            if(!Bodyes[i])
                                g2d.fill(el);

                            traektoriesx[i][it+1]=(r[i].x - r[s-3].x) / (1.5 * Math.pow(10, 9)) + 500;
                            traektoriesy[i][it+1]=(r[i].y - r[s-3].y) / (1.5 * Math.pow(10, 9)) + 500;
                            //вектора скоростей
                            if(vv){
                                g2d.setColor(Color.GREEN);
                                if(i!=s-3)
                                drawArrow(g2d,(int)((r[i].x - r[s-3].x) / (1.5 * Math.pow(10, 9)) + 500), (int)((r[i].y - r[s-3].y) / (1.5 * Math.pow(10, 9)) + 500),
                                        (int)((r[i].x - r[s-3].x) / (1.5 * Math.pow(10, 9)) + 500+v[i].x/200),(int)((r[i].y - r[s-3].y) / (1.5 * Math.pow(10, 9)) + 500+v[i].y/200));
                            }
                            //вектора ускорений
                            if(av){
                                g2d.setColor(Color.BLUE);
                                if(i!=s-3)
                                drawArrow(g2d,(int)((r[i].x - r[s-3].x) / (1.5 * Math.pow(10, 9)) + 500), (int)((r[i].y - r[s-3].y) / (1.5 * Math.pow(10, 9)) + 500),
                                        (int)((r[i].x - r[s-3].x) / (1.5 * Math.pow(10, 9)) + 500+300*(Math.pow((a[i]).Le(),0.2)*a[i].x/(a[i]).Le())),
                                        (int)((r[i].y - r[s-3].y) / (1.5 * Math.pow(10, 9)) + 500+300*(Math.pow((a[i]).Le(),0.2)*a[i].y/(a[i]).Le())));
                            }
                            //радиус вектора
                            if(rv){
                                g2d.setColor(Color.ORANGE);
                                if(i!=s-3)
                                drawArrow(g2d,500,500,(int)((r[i].x - r[s-3].x) / (1.5 * Math.pow(10, 9)) + 500), (int)((r[i].y - r[s-3].y) / (1.5 * Math.pow(10, 9)) + 500));
                            }
                        }

                    }
        it++;


        for (int i = 0; i < kol; i++)
            a[i] = a(r, m, kol, i);

        for (int i = 0; i < kol; i++)
            v[i] = v(v[i], a[i], fast);
        for (int i = 0; i < kol; i++)
            r[i] = r(r[i], v[i], fast);

    }

    static void showMovingCircle(double[] m, double[] vx, double[] vy, double[] rx, double[] ry, int s, int kol) {

        fast=(int)(400*219.1445);
        JFrame frame = new JFrame("Решение задачи");
        JPanel panel = new JPanel();
        final JButton bst = new JButton();
        Icon play = new ImageIcon("D:\\play.PNG");
        bst.setIcon(play);
        bst.setSize(60,60);
        bst.setLocation(1320,10);
        bst.setBorderPainted(false);
        bst.setFocusPainted(false);
        bst.setContentAreaFilled(false);
        frame.add(bst);

        MovingCircle MovingCircleB = new MovingCircle(Color.BLUE, 10,m,vx,vy,rx,ry,s,kol);
        JButton[] body = new JButton[kol];
        Icon iconbody = new ImageIcon("D:\\body.PNG");
        Icon diconbody = new ImageIcon("D:\\darkbody.PNG");
        for(int i=0;i<kol;i++){

            body[i]=new JButton();
           body[i].setFont(new Font("Arial", Font.PLAIN, 18));
            body[i].setText("Тело "+(i+1));
            body[i].setLocation(1310,500+35*i);
            body[i].setSize(104,34);
            body[i].setBorderPainted(false);
            body[i].setFocusPainted(false);
            body[i].setContentAreaFilled(false);

            body[i].setIcon(iconbody);
            body[i].setHorizontalTextPosition(SwingConstants.CENTER);
            frame.add(body[i]);
        }
        //слайдер для ti
        JSlider slider = new JSlider(JSlider.VERTICAL, 0, 1400, 400);
        slider.setInverted(true);
        slider.setMinorTickSpacing(100);
        slider.setMajorTickSpacing(200);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLocation(1320,80);
        slider.setSize(80,200);
        frame.add(slider);
        slider.addChangeListener(e -> fast = (int)(slider.getValue()*219.1445));
        JLabel text = new JLabel("<html>Скорость<br>(часы/сек)</html>");
        text.setLocation(1400,70);
        text.setSize(100,60);
        frame.add(text);
//оси
        JButton axis = new JButton();
        axis.setSize(34,34);
        axis.setLocation(1320,450);
        Icon iconax = new ImageIcon("D:\\axis.PNG");
        Icon diconax = new ImageIcon("D:\\darkaxis.PNG");
        axis.setIcon(iconax);
        axis.setBorderPainted(false);
        axis.setFocusPainted(false);
        axis.setContentAreaFilled(false);
        frame.add(axis);
        axis.addActionListener(new ActionListener() {
            private boolean pushax = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pushax) {
                    pushax= false;
                    axis.setIcon(iconax);
                    ax= !ax;
                } else {
                    pushax = true;
                    axis.setIcon(diconax);
                    ax = !ax;
                }
            }
        });
//радиус-вектор
        JButton rvect = new JButton();

        rvect.setLocation(1320,400);
        Icon iconr = new ImageIcon("D:\\vv.PNG");
        Icon diconr = new ImageIcon("D:\\darkvv.PNG");
        rvect.setIcon(iconr);
        rvect.setSize(34,34);
        rvect.setBorderPainted(false);
        rvect.setFocusPainted(false);
        rvect.setContentAreaFilled(false);

        frame.add(rvect);
        rvect.addActionListener(new ActionListener() {
            private boolean pushax = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pushax) {
                    pushax= false;
                    rvect.setIcon(iconr);
                    rv= !rv;
                } else {
                    pushax = true;
                    rvect.setIcon(diconr);
                    rv = !rv;
                }
            }
        });
        //вектор скорости
        JButton vvect = new JButton();
        vvect.setLocation(1320,350);
        Icon iconv = new ImageIcon("D:\\rv.PNG");
        Icon diconv = new ImageIcon("D:\\darkvr.PNG");
        vvect.setIcon(iconv);
        vvect.setSize(34,34);
        vvect.setBorderPainted(false);
        vvect.setFocusPainted(false);
        vvect.setContentAreaFilled(false);
        frame.add(vvect);
        vvect.addActionListener(new ActionListener() {
            private boolean pushax = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pushax) {
                    pushax= false;
                    vvect.setIcon(iconv);
                    vv= !vv;
                } else {
                    pushax = true;
                    vvect.setIcon(diconv);
                    vv = !vv;
                }
            }
        });
        //вектор ускорения
        JButton avect = new JButton();
        avect.setSize(34,34);
        avect.setLocation(1320,300);
        Icon icona = new ImageIcon("D:\\va.PNG");
        Icon dicona = new ImageIcon("D:\\darkva.PNG");
        avect.setIcon(icona);
        avect.setBorderPainted(false);
        avect.setFocusPainted(false);
        avect.setContentAreaFilled(false);
        frame.add(avect);
        avect.addActionListener(new ActionListener() {
            private boolean pushax = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pushax) {
                    pushax= false;
                    avect.setIcon(icona);
                    av= !av;
                } else {
                    pushax = true;
                    avect.setIcon(dicona);
                    av = !av;
                }
            }
        });

        //траектории
        JButton traectories = new JButton();
        traectories.setSize(34,34);
        traectories.setLocation(1380,350);
        Icon icontr = new ImageIcon("D:\\tr.PNG");
        Icon dicontr = new ImageIcon("D:\\darktr.PNG");
        traectories.setIcon(icontr);
        traectories.setBorderPainted(false);
        traectories.setFocusPainted(false);
        traectories.setContentAreaFilled(false);
        frame.add(traectories);
        traectories.addActionListener(new ActionListener() {
            private boolean pushax = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pushax) {
                    pushax= false;
                    traectories.setIcon(icontr);
                    tr= !tr;
                } else {
                    pushax = true;
                    traectories.setIcon(dicontr);
                    tr = !tr;
                }
            }
        });

        //стерка
        JButton clean = new JButton();
        clean.setSize(34,34);
        clean.setLocation(1380,300);
        Icon iconclean = new ImageIcon("D:\\clean.PNG");
        clean.setRolloverIcon(new ImageIcon("D:\\darkclean.PNG"));
        clean.setIcon(iconclean);
        clean.setBorderPainted(false);
        clean.setFocusPainted(false);
        clean.setContentAreaFilled(false);
        frame.add(clean);
        clean.addActionListener(e -> traectories_st = it);
        //данные  в коорд форме
        JButton info = new JButton();
        info.setSize(34,34);
        info.setLocation(1380,400);
        Icon iconinfo = new ImageIcon("D:\\infoXY.PNG");
        Icon diconinfo = new ImageIcon("D:\\darkinfoXY.PNG");
        info.setIcon(iconinfo);
        info.setBorderPainted(false);
        info.setFocusPainted(false);
        info.setContentAreaFilled(false);
        frame.add(info);
        info.addActionListener(new ActionListener() {
            private boolean pushax = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pushax) {
                    pushax= false;
                    info.setIcon(iconinfo);
                    infXY= !infXY;
                } else if(!infV){
                    pushax = true;
                    info.setIcon(diconinfo);
                    infXY = !infXY;
                }
            }
        });
        //данные в векторной форме
        JButton infoV = new JButton();
        infoV.setSize(34,34);
        infoV.setLocation(1380,450);
        Icon iconinfoV = new ImageIcon("D:\\infoV.PNG");
        Icon diconinfoV = new ImageIcon("D:\\darkinfoV.PNG");
        infoV.setIcon(iconinfoV);
        infoV.setBorderPainted(false);
        infoV.setFocusPainted(false);
        infoV.setContentAreaFilled(false);
        frame.add(infoV);
        infoV.addActionListener(new ActionListener() {
            private boolean pushax = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pushax) {
                    pushax= false;
                    infoV.setIcon(iconinfoV);
                    infV= !infV;
                } else if(!infXY){
                    pushax = true;
                    infoV.setIcon(diconinfoV);
                    infV = !infV;
                }
            }
        });
        //перезапуск
        JButton restart = new JButton();
        restart.setSize(56,56);
        restart.setLocation(1390,10);
        Icon iconrestart = new ImageIcon("D:\\restar.PNG");
        restart.setRolloverIcon(new ImageIcon("D:\\darkrestart.PNG"));
        restart.setIcon(iconrestart);
        restart.setBorderPainted(false);
        restart.setFocusPainted(false);
        restart.setContentAreaFilled(false);
        frame.add(restart);



        //нажатие выделения
       Bodyes =new boolean[kol];
        for(int i = 0; i<kol; i++){
            int I = i;
            body[i].addActionListener(new ActionListener() {
                private boolean pushax = false;
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (pushax) {
                        pushax= false;
                        body[I].setIcon(iconbody);
                        Bodyes[I] = !Bodyes[I];
                    } else {
                        pushax = true;
                        body[I].setIcon(diconbody);
                        Bodyes[I] = !Bodyes[I];
                    }
                }
            });
        }

        MovingCircleB.setSize(1500,1100);
        MovingCircleB.setLocation(10,10);
        frame.add(MovingCircleB);
        frame.getContentPane().add(panel);

        restart.addActionListener(e -> MovingCircleB.restart(rx,ry,vx,vy,m));

        bst.addActionListener(new ActionListener() {
            private boolean pulsing = false;
           @Override
            public void actionPerformed(ActionEvent e) {
                if (pulsing) {
                    pulsing = false;
                    MovingCircleB.stop();
                    Icon play = new ImageIcon("D:\\play.PNG");
                    bst.setIcon(play);

                } else {
                    pulsing = true;

                    Icon play = new ImageIcon("D:\\pause.PNG");
                    bst.setIcon(play);
                    MovingCircleB.start();

                }

            }
        });




        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1100);
        frame.setVisible(true);
    }
}