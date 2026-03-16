package com.laoliu.system.entity;

import org.springframework.stereotype.Component;


/**
 * 
 * @author 25516
 * @TableName user
 */
@Component
public class User {
    /**
     * Primary key, auto-increment
     */
    private Long id;

    /**
     * User name, cannot be null
     */
    private String name;

    /**
     * Academic grade or class, optional
     */
    private String grade;

    /**
     * Gender: male/female/other
     */
    private String sex;

    /**
     * Age, unsigned (0–127), optional
     */
    private Integer age;

    /**
     * Email, must be unique if provided
     */
    private String email;

    /**
     * Hashed password (e.g., BCrypt), never store plain text!
     */
    private String password;

    /**
     * Your role in this system.1 is admin,0 is user.
     */
    private Integer role;

    // there is no need to store the service in the user,just select by controller is ok.
    //add an attribute to store the service
//    List<Service> services;

    /**
     * Primary key, auto-increment
     */
    public Long getId() {
        return id;
    }

    /**
     * Primary key, auto-increment
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * User name, cannot be null
     */
    public String getName() {
        return name;
    }

    /**
     * User name, cannot be null
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Academic grade or class, optional
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Academic grade or class, optional
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * Gender: male/female/other
     */
    public String getSex() {
        return sex;
    }

    /**
     * Gender: male/female/other
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Age, unsigned (0–127), optional
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Age, unsigned (0–127), optional
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * Email, must be unique if provided
     */
    public String getEmail() {
        return email;
    }

    /**
     * Email, must be unique if provided
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Hashed password (e.g., BCrypt), never store plain text!
     */
    public String getPassword() {
        return password;
    }

    /**
     * Hashed password (e.g., BCrypt), never store plain text!
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Your role in this system.1 is admin,0 is user.
     */
    public Integer getRole() {
        return role;
    }

    /**
     * Your role in this system.1 is admin,0 is user.
     */
    public void setRole(Integer role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getGrade() == null ? other.getGrade() == null : this.getGrade().equals(other.getGrade()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getAge() == null ? other.getAge() == null : this.getAge().equals(other.getAge()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getRole() == null ? other.getRole() == null : this.getRole().equals(other.getRole()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getGrade() == null) ? 0 : getGrade().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getAge() == null) ? 0 : getAge().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getRole() == null) ? 0 : getRole().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", grade=").append(grade);
        sb.append(", sex=").append(sex);
        sb.append(", age=").append(age);
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", role=").append(role);
        sb.append("]");
        return sb.toString();
    }
}