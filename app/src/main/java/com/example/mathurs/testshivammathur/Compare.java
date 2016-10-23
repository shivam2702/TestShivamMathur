package com.example.mathurs.testshivammathur;

import java.util.Comparator;

/**
 * Created by Mathurs on 23-10-2016.
 */

public class Compare implements Comparator<Structure> {
    @Override
    public int compare(Structure o1, Structure o2) {
        if(o1.totalPoint<o2.totalPoint)
            return 1;
        if(o1.totalPoint>o2.totalPoint)
            return -1;
        if(o1.totalPoint==o2.totalPoint){
            if (o1.gd<o2.gd)
                return 1;
            else
                return 0;
        }
        return 0;
    }
}
