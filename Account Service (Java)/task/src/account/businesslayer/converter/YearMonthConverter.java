package account.businesslayer.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

//@Converter
public class YearMonthConverter {// implements AttributeConverter<String, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");
    private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("LLLL-yyyy");

    //    @Override
    public static String convertToDatabaseColumn(String attribute) {
        return YearMonth.parse(attribute, FORMATTER).format(FORMATTER2);
    }

    //    @Override
    public static String convertToEntityAttribute(String dbData) {
        return dbData;
    }

}
