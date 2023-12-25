package com.poly.bean;

import java.util.ArrayList;
import java.util.List;

public class MonthUtils {
	  public static List<Integer> getAllMonths() {
	        List<Integer> months = new ArrayList<>();
	        for (int i = 1; i <= 12; i++) {
	            months.add(i);
	        }
	        return months;
	    }
}
