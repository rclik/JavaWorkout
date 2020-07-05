package com.rcelik.java.core;

import java.util.Objects;

public class EmployeeWithHashCode extends Employee{
    public EmployeeWithHashCode(String name, long id) {
        super(name, id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
