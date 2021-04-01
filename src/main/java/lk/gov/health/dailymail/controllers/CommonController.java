/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.dailymail.controllers;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import lk.gov.health.dailymail.enums.Role;

/**
 *
 * @author pdhssp
 */
@Named(value = "commonController")
@SessionScoped
public class CommonController implements Serializable {

    /**
     * Creates a new instance of CommonController
     */
    public CommonController() {
    }

    public Role[] getRoles() {
        return Role.values();
    }

    public static Date startOfTheDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date endOfTheDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.clear(Calendar.MINUTE);
        c.clear(Calendar.MILLISECOND);
        
        c.add(Calendar.DATE, 1);
        c.add(Calendar.MILLISECOND, -1);
        return c.getTime();
    }

//    public static Date startOfWeek() {
//        Calendar c = new GregorianCalendar();
//        c.set(Calendar.We, c.getFirstDayOfWeek());
//        c.set(Calendar.HOUR_OF_DAY, 0);
//        c.clear(Calendar.MINUTE);
//        c.clear(Calendar.MILLISECOND);
//        c.clear(Calendar.AM_PM);
//        c.clear(Calendar.MILLISECOND);
//        return c.getTime();
//    }

    public static Date startOfMonth() {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date startOfYear() {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

}
