package com.nguyentudien.mycalculate;

public class MyCal {
    Double a;
    Double b;
    char c;
    MyCal(){};
    public MyCal(Double a, Double b) {
        this.a = a;
        this.b = b;
    }
    public void set(char c){
        this.c = c;
    }
    public Double  getResult(){
        if(c == '+') return a+ b;
        if(c == '-') return a- b;
        if(c == '*') return a* b;
        if(c == '/') return (double)a/ b;
        return 0.0;
    }
}
