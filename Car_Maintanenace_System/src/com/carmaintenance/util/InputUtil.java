package com.carmaintenance.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import com.carmaintenance.exception.ValidationException;

public final class InputUtil {
    private static final Scanner SCANNER = new Scanner(System.in);
    private InputUtil() {}

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }
    public static String readNonEmptyString(String prompt) {
        String value = readLine(prompt);
        if (value.isEmpty()) throw new ValidationException("Value cannot be blank.");
        return value;
    }
    public static int readInt(String prompt) {
        try { return Integer.parseInt(readNonEmptyString(prompt)); }
        catch (NumberFormatException e) { throw new ValidationException("Enter a valid whole number."); }
    }
    public static int readPositiveInt(String prompt) {
        int value = readInt(prompt);
        if (value <= 0) throw new ValidationException("Value must be greater than zero.");
        return value;
    }
    public static BigDecimal readNonNegativeMoney(String prompt) {
        try {
            BigDecimal value = new BigDecimal(readNonEmptyString(prompt));
            if (value.signum() < 0) throw new ValidationException("Amount cannot be negative.");
            return value;
        } catch (NumberFormatException e) {
            throw new ValidationException("Enter a valid amount, for example 1250.00.");
        }
    }
    public static LocalDate readDate(String prompt) {
        try { return LocalDate.parse(readNonEmptyString(prompt)); }
        catch (DateTimeParseException e) { throw new ValidationException("Use date format YYYY-MM-DD."); }
    }
}
