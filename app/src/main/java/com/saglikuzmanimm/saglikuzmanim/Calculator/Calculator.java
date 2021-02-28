package com.saglikuzmanimm.saglikuzmanim.Calculator;

import java.util.List;

public abstract class Calculator {
    public float calculatorPoint(List<Float> list) {
        float total = 0;
        for (int i = 0; i < list.size(); i++) {
            total = total + list.get(i);

        }
        float average = total/list.size();
        return average;
    }
}
