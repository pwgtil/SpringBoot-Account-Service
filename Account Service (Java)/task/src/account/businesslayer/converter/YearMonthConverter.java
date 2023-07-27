package account.businesslayer.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Converter
public class YearMonthConverter implements AttributeConverter<String, YearMonth> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");

    @Override
    public YearMonth convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return YearMonth.parse(attribute, FORMATTER);
    }

    @Override
    public String convertToEntityAttribute(YearMonth dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.format(FORMATTER);
    }
}
