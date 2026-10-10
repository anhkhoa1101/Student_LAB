package Utilities.Validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public interface CourseValidation {

    String DATE_FORMAT = "dd/MM/yyyy";
    int MIN_DURATION = 5;
    int MAX_DURATION = 10;

    static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    static boolean isValidDuration(int d) {
        return d >= MIN_DURATION && d <= MAX_DURATION;
    }

    /** Ngày bắt đầu phải SAU hôm nay (hôm nay không tính). */
    static boolean isFutureDate(Date d) {
        if (d == null) return false;
        LocalDate date = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date.isAfter(LocalDate.now());
    }

    /** Trả về null nếu sai định dạng hoặc ngày không tồn tại (31/02/2026). */
    static Date parseDate(String s) {
        if (s == null || !s.trim().matches("^\\d{2}/\\d{2}/\\d{4}$")) return null;
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setLenient(false);
        try {
            return df.parse(s.trim());
        } catch (ParseException e) {
            return null;
        }
    }
}