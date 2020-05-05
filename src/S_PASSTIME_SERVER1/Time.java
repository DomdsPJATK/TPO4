/**
 *
 *  @author Suchner Dominik S19036
 *
 */

package S_PASSTIME_SERVER1;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Time {

    private Locale locale;
    private LocalDate firstLocalDate;
    private LocalDate secondLocalDate;
    private LocalTime firstLocalTime;
    private LocalTime secondLocalTime;

    public Time(Locale locale) {
        this.locale = locale;
    }

    public static String passed(String firstDate, String secondDate) {
        Time time = new Time(new Locale("pl"));

        if (time.IsWithTimeRegex(firstDate) && time.IsWithTimeRegex(secondDate)) {
            try {
                LocalDateTime localFirstDate = LocalDateTime.parse(firstDate);
                LocalDateTime localSecondDate = LocalDateTime.parse(secondDate);
                time.firstLocalDate = localFirstDate.toLocalDate();
                time.secondLocalDate = localSecondDate.toLocalDate();
                time.firstLocalTime = localFirstDate.toLocalTime();
                time.secondLocalTime = localSecondDate.toLocalTime();
            } catch (DateTimeParseException e) {
                return "*** " + e.toString();
            }
        } else {
            try {
                time.firstLocalDate = LocalDate.parse(firstDate);
                time.secondLocalDate = LocalDate.parse(secondDate);
            } catch (DateTimeParseException e) {
                return "*** " + e.toString();
            }
        }

        return time.printDate();
    }

    public boolean IsWithTimeRegex(String value) {
        if (value.split("T").length > 1) return true;
        return false;
    }

    public String getTextFromDateToDate(LocalDate firstDate, LocalDate secondDate, LocalTime firstLocalTime, LocalTime secondLocalTime) throws DateTimeParseException {
        return "Od " + getFormatedTranslatedDate(firstDate, firstLocalTime) + " do " + getFormatedTranslatedDate(secondDate, secondLocalTime);
    }

    public String getFormatedTranslatedDate(LocalDate localDate, LocalTime localTime) {
        if (localTime != null) {
            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
            return localDateTime.format(DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE) 'godz.' HH:mm", this.locale));
        }
        return localDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE)", this.locale));
    }

    public String getHowManyDaysAndWeeksHavePassed(LocalDate localFirstDate, LocalDate localSecondDate) {
        float days = ChronoUnit.DAYS.between(localFirstDate, localSecondDate);
        float weeks = days / 7;
        if (weeks % 1 == 0) {
            return String.format(new Locale("xx"), "- mija: %d dni, tygodni %d", (int) days, (int) weeks);
        }
        return String.format(new Locale("xx"), "- mija: %d dni, tygodni %.2f", (int) days, weeks);
    }

    public String getHowManyHoursAndMinutesHavePassed(ZonedDateTime zonedLocalFirstDate, ZonedDateTime zonedLocalSecondDate) {
        return String.format(new Locale("xx"), "- godzin: %d, minut: %d", ChronoUnit.HOURS.between(zonedLocalFirstDate, zonedLocalSecondDate), ChronoUnit.MINUTES.between(zonedLocalFirstDate, zonedLocalSecondDate));
    }

    public String getHowManyHavePassedByCalendar(Period period) {
        String periodString = "- kalendarzowo: ";
        if (period.getYears() > 0) {
            if (period.getYears() == 1) {
                periodString += period.getYears();
                periodString += " rok";
            } else if (period.getYears() > 1 && period.getYears() <= 4) {
                periodString += period.getYears();
                periodString += " lata";
            } else {
                periodString += period.getYears();
                periodString += " lat";
            }
            if (period.getMonths() > 0) periodString += ", ";
        }
        if (period.getMonths() > 0) {
            if (period.getMonths() == 1) {
                periodString += period.getMonths();
                periodString += " miesiąc";
            } else if (period.getMonths() > 1 && period.getMonths() <= 4) {
                periodString += period.getMonths();
                periodString += " miesiące";
            } else {
                periodString += period.getMonths();
                periodString += " miesięcy";
            }
            if (period.getDays() > 0) periodString += ", ";
        }
        if (period.getDays() > 0) {
            if (period.getDays() == 1) {
                periodString += period.getDays();
                periodString += " dzień";
            } else {
                periodString += period.getDays();
                periodString += " dni";
            }
        }
        return periodString;
    }


    public String printDate() {
        String toPrint = "";
        toPrint += getTextFromDateToDate(this.firstLocalDate, this.secondLocalDate, this.firstLocalTime, this.secondLocalTime);
        toPrint += "\n" + getHowManyDaysAndWeeksHavePassed(this.firstLocalDate, this.secondLocalDate);
        if (this.firstLocalTime != null && this.secondLocalTime != null) {
            ZonedDateTime firstZoned = ZonedDateTime.of(LocalDateTime.of(this.firstLocalDate, firstLocalTime), ZoneId.of("Europe/Warsaw"));
            ZonedDateTime secondZoned = ZonedDateTime.of(LocalDateTime.of(this.secondLocalDate, this.secondLocalTime),ZoneId.of("Europe/Warsaw"));
            toPrint += "\n" + getHowManyHoursAndMinutesHavePassed(firstZoned,secondZoned);
        }
        Period period = Period.between(this.firstLocalDate, this.secondLocalDate);
        if (period.getDays() > 0 || period.getYears() > 0 || period.getMonths() > 0) {
            toPrint += "\n" + getHowManyHavePassedByCalendar(period);
        }
        return toPrint;
    }

}
