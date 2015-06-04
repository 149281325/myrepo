/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wlstest.functional.java8;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fwang
 */
public class Person {
    
    public static final int FEMALE = 0;
    public static final int MALE = 1;
    
    private String name;
    private int age;
    private int sex;
    private String location;
    
    public Person() {}
    
    public Person(String name, int age, int sex) {
        this(name, age, sex, null);
    }
    
    public Person(String name, int age, int sex, String location) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.location = location;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        if(age < 0) {
            throw new IllegalArgumentException("Age can't be less than 0");
        }
        this.age = age;
    }

    /**
     * @return the sex
     */
    public int getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(int sex) {
        if(sex != MALE && sex != FEMALE) {
            throw new IllegalArgumentException("Please set to either Person.MALE or Person.FEMALE");
        }
        this.sex = sex;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String toString() {
        return '(' + name + ", " + age + ", " + (sex==MALE?'M':'F') + ", " + location + ')';
    }
    
    public static Person randomPerson() {
        double rand = Math.random();
        String strRand = String.valueOf(rand);
        int idx = strRand.indexOf('.');
        String name = "Name" + (idx>=0?strRand.substring(idx+1):strRand);
        int age = (int)(rand * 100);
        int sex = age % 2 == 0 ? MALE : FEMALE;
        String location = "BJ";
        return new Person(name, age, sex, location);
    }
    
    public static List<Person> randomPersons(int howMany) {
        List<Person> ret = new ArrayList<>();
        for(int i=0; i<howMany; i++) {
            ret.add(randomPerson());
        }
        return ret;
    }
    
}
