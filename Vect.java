package com.company;

public class Vect {
        double x;
        double y;
    public double Le() {
        return Math.sqrt(this.x*this.x+this.y*this.y);

    }
    public Vect VSum(Vect a) {
            Vect b=new Vect()  ;
            b.x=a.x+this.x;
            b.y=a.y+this.y;
            return b;
        }
    public Vect VMi(Vect a) {
        Vect b=new Vect()  ;
        b.x=-a.x+this.x;
        b.y=-a.y+this.y;
        return b;
    }
        public Vect VSp(double  b) {
            Vect a=new Vect()  ;
            a.x=this.x*b;
            a.y=this.y*b;
            return a;
        }

}
