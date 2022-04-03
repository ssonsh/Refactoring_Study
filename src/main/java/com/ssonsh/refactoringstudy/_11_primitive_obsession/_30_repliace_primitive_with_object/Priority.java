package com.ssonsh.refactoringstudy._11_primitive_obsession._30_repliace_primitive_with_object;

import java.util.List;

public class Priority {

    private String value;

    // Priority 객체의 값에 대해 Type Safty를 보장하기 위해 허용가능한 값을 선언하여
    // 활용할 수 있다.
    private List<String> legalValues = List.of("low", "normal", "high", "rush");

    public Priority(String value) {
        if(legalValues.contains(value)){
            this.value = value;
        }else{
            throw new IllegalArgumentException("illegal value for priority " + value);
        }
    }

    @Override
    public String toString() {
        return this.value;
    }

    private int index(){
        return this.legalValues.indexOf(this.value);
    }

    public boolean higherThan(Priority other){
        return this.index() > other.index();
    }
}
