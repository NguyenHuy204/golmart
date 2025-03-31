package com.golmart.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class DataUtils {
    private static Calendar calendar = Calendar.getInstance();
    private static final NumberFormat formatter = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));


    public static String formatToVND(@NotNull Object amount) {
        return formatter.format(amount);
    }

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        ModelMapper modelMapper = new ModelMapper();
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public static boolean patternMatches(String str, String regexPattern) {
        return Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE)
                .matcher(str)
                .matches();
    }

    public static boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Date convertStringToDate(String dateStr, String format) {
        try {
            DateFormat parser = new SimpleDateFormat(format);
            Date date = (Date) parser.parse(dateStr);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    private static final DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date convertStringToDate(String dateStr) {
        try {
            Date date = (Date) parser.parse(dateStr);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertDateToStr(Date date, String format) {
        try {
            if (!ObjectUtils.isEmpty(date)) {
                DateFormat dateFormat = new SimpleDateFormat(format);
                return dateFormat.format(date);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static Double formatDouble(Double number){
        try{
            return (double) Math.round(number * 1000) / 1000;
        }catch (Exception e){
            return number;
        }
    }
    public static String formateCode(String code, Long id){
        try{
            String idCode = String.format("%03d", id);
            return code.concat(idCode);
        }catch (Exception e){
            return code.concat(id.toString());
        }
    }
    public static String getCurrentDateTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date timenow = new Date();
        String strtimenow = sdf.format(timenow);
        return strtimenow;
    }

    public static String getExt(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i >= 0) { extension = fileName.substring(i+1); }
        return extension;
    }

    public static Date addOneMoth(Date date) {
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return  calendar.getTime();
    }

    public static Date addOneDay(Date date) {
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return  calendar.getTime();
    }

    public static Date minusOneMoth(Date date) {
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return  calendar.getTime();
    }

    public static String genCode(long number, int code_len) {
        var str_num = String.valueOf(number);
        var num_len = str_num.length();
        if(num_len > code_len)
            return str_num;
        else {
            return IntStream.range(0, code_len - num_len)
                    .mapToObj(i -> "0")
                    .collect(Collectors.joining()).concat(str_num);
        }
    }
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static Double parseDouble(String str, Double defaultReturn) {
        try{
            return Double.parseDouble(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return defaultReturn;
    }

    public static String formatStr(Double obj, String format) {
        try{
            return String.format(format, obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    } public static String formatNumber(Double obj) {
       return formatStr(obj,"%,.0f");
    }
}
