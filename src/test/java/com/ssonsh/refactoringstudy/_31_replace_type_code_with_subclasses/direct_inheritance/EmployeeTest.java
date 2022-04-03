package com.ssonsh.refactoringstudy._31_replace_type_code_with_subclasses.direct_inheritance;

import com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.direct_inheritance.Engineer;
import com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.direct_inheritance.Manager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeTest {

    @Test
    void employeeType() {
        assertEquals("engineer", new Engineer("keesun").getType());
        assertEquals("manager", new Manager("keesun").getType());
    }

}