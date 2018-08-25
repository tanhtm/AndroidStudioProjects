package com.myandroid.tuanh.hex;

/**
 * Created by TuAnh on 5/13/2017.
 */

public class Basis {
    public String strIn;
    public int basIn;
    public String strOut;
    public int basOut;
    public Basis(String in,String sint,String sout)
    {
        strIn = in;
        basIn = 0;
        for( int i=0; i<sint.length();i++)
            basIn = basIn*10 + ((int) sint.charAt(i)-48);
        basOut= 0;
        for( int i=0; i<sout.length();i++)
            basOut = basOut*10 + ((int) sout.charAt(i)-48);
    }
    // chuyen sang co so 10
     int Num10 () {
         int num=0,x=0;
         int len = strIn.length();
         if(strIn.charAt(0) == '-')
             x=1;
         for (int i = x; i < len; i++){
             int z=(int) strIn.charAt(i)-48;
             num = num + z * (int) Math.pow(basIn,len-i-1);
         }
         if(x == 1)
             num = num * -1;
         return num;
    }
    void Run()
    {
        int so = Num10() , x=0;
        if(so<0){
            x=1;
            so = so * -1;
        }
        char[] a = new char [37];
        for(int i=0;i<36;i++)
        {
            if(i<10)
                a[i] = (char) (i+48);
            else
                a[i]=(char) (55+i);
        }
        char[] res = new char [100];
        int j=0;
        while (so != 0)
        {
            res[j++] = a[so % basOut];
            so = so / basOut;
        }
        if(x==1)
            res[j++] = '-';
        for (int i=0;i<j/2;i++)
        {
            char tg = res[i];
            res[i]=res[j-i-1];
            res[j-i-1]=tg;
        }
        strOut = new String(res);
    }

}
