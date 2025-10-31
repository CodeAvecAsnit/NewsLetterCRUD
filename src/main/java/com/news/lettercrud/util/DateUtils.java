package com.news.lettercrud.util;

import java.time.LocalDateTime;

/**
 * @author : Asnit Bakhati
 */

public class DateUtils {

    public static String buildDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "Unknown date";
        }
        int day = localDateTime.getDayOfMonth();
        int month = localDateTime.getMonthValue();
        int year = localDateTime.getYear();
        String suffix = getDaySuffix(day);
        String monthName = getMonth(month);
        return String.format("%d%s, %s %d", day, suffix, monthName, year);
    }

    private static String getMonth(int monthInInt){
        return switch (monthInInt){
            case 1 -> "Jan";
            case 2->"Feb";
            case 3->"Mar";
            case 4->"April";
            case 5->"May";
            case 6->"June";
            case 7 ->"July";
            case 8->"Aug";
            case 9->"Sept";
            case 10->"Oct";
            case 11-> "Nov";
            case 12->"Dec";
            default -> "Invalid";
        };
    }



    private static String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        return switch (day % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}