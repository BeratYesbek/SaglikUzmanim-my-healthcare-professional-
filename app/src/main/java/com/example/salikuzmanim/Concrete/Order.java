package com.example.salikuzmanim.Concrete;

public class Order {
    private String department;
    private String location;
    private String according_to_what;
    private Object orderBy;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAccording_to_what() {
        return according_to_what;
    }

    public void setAccording_to_what(String according_to_what) {
        this.according_to_what = according_to_what;
    }

    public Object getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Object orderBy) {
        this.orderBy = orderBy;
    }
}
