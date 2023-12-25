package com.poly.util;

import org.springframework.core.convert.converter.Converter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateConverter implements Converter<String, Date> {
    private SimpleDateFormat dateFormat;

    public StringToDateConverter(String dateFormatPattern) {
        this.dateFormat = new SimpleDateFormat(dateFormatPattern);
    }

    @Override
    public Date convert(String source) {
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            // Xử lý lỗi khi không thể chuyển đổi ngày tháng
            throw new IllegalArgumentException("Invalid date format. Expected: " + dateFormat.toPattern());
        }
    }
}
