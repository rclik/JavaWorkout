package com.rcelik.java.core;

public class EmployeeWithEquals extends Employee {
    public EmployeeWithEquals(String name, long id) {
        super(name, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        EmployeeWithEquals employeeWithEquals = (EmployeeWithEquals) obj;

        return this.getId() == employeeWithEquals.getId();
    }
}
