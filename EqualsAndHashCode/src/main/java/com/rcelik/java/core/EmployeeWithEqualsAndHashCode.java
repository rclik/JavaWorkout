package com.rcelik.java.core;

import java.util.Objects;

public class EmployeeWithEqualsAndHashCode extends EmployeeWithEquals{
    public EmployeeWithEqualsAndHashCode(String name, long id) {
        super(name, id);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
