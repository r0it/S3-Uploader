/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author Naga Srinivas
 */
public class MyCalendar {

    static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");
    Calendar calendar;

    /**
     * Making private to avoid creating objects directly
     */
    private MyCalendar(TimeZone zone) {
        this(Calendar.getInstance(zone));
    }

    private MyCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    private MyCalendar(TimeZone zone, long timestamp) {
        this.calendar = Calendar.getInstance(zone);
        this.calendar.setTimeInMillis(timestamp);
    }

    public long getTimeStamp() {
        return calendar.getTimeInMillis();
    }

    public int getDayOfMonth() {
        return calendar.get(java.util.Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        //add 1, bcoz gregorian calender starts from 0 (january)
        return (calendar.get(java.util.Calendar.MONTH) + 1);
    }

    public int getYear() {
        return calendar.get(java.util.Calendar.YEAR);
    }

    /**
     * Moves the current time to the starting of the day time exactly 12:00 A.M
     *
     * @return
     */
    public MyCalendar moveToStartingOfDay() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return this;
    }

    /**
     * Moves the current time to the ending of the day time exactly 11:59 P.M
     *
     * @return
     */
    public MyCalendar moveToEndingOfDay() {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return this;
    }

    /**
     * Moves the current time to starting of this month this do not
     * automatically moves the day to starting
     *
     * @return
     */
    public MyCalendar moveToStartingOfMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return this;
    }

    /**
     * This method sets the UTC time for the date
     */
    public MyCalendar setUTCTime(int hours, int minutes, int seconds, int millis) {
        return this;
    }

    /**
     * Moves the current time to ending of this month this do not automatically
     * moves the day to ending
     *
     * @return
     */
    public MyCalendar moveToEndingOfMonth() {
        moveToStartingOfMonth(); // first moving to the starting of the month
        calendar.add(Calendar.MONTH, 1); // adding one month
        calendar.add(Calendar.DAY_OF_MONTH, -1); // removing one day to make sure the end of of the month
        return this;
    }

    public MyCalendar addDays(int days) {
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return this;
    }

    /**
     *
     * @param noOfMonths
     * @return
     */
    public MyCalendar addMonths(int noOfMonths) {
        calendar.add(Calendar.MONTH, noOfMonths);
        return this;
    }

    @Override
    public String toString() {
        return calendar.getTime().toString();
    }

    /**
     * This method gives the bound of the current day
     */
    public static long[] getBounds(Date date) {
        MyCalendar inst = getInstance();
        inst.calendar.setTime(date); // setting the date
        long sTime = inst.moveToStartingOfDay().getTimeStamp();
        long eTime = inst.moveToEndingOfDay().getTimeStamp();
        long[] bounds = new long[]{sTime, eTime};
        return bounds;
    }

    /**
     *
     * @return
     */
    public static MyCalendar getInstance() {
        return new MyCalendar(DEFAULT_TIMEZONE);
    }

    public static MyCalendar getInstance(Calendar cal) {
        return new MyCalendar(cal);
    }

    public static MyCalendar getInstance(long timestamp) {
        return new MyCalendar(DEFAULT_TIMEZONE, timestamp);
    }

    public static void main(String[] args) {
        //MyCalendar cal = MyCalendar.getInstance(1417564799000L);        
        MyCalendar cal = MyCalendar.getInstance(1417564799000L);
        System.out.println(cal);
    }

    public static void main2(String[] args) {
        Calendar c1 = GregorianCalendar.getInstance();
        Calendar c2 = GregorianCalendar.getInstance(DEFAULT_TIMEZONE);
        MyCalendar cal1 = MyCalendar.getInstance(c1);
        MyCalendar cal2 = MyCalendar.getInstance(c2);
        cal1.moveToStartingOfDay();
        cal2.moveToStartingOfDay();
        cal1.moveToEndingOfDay();
        cal2.moveToEndingOfDay();

        System.out.println(cal1);
        System.out.println(cal2);
    }
}
