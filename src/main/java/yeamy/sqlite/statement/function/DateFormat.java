package yeamy.sqlite.statement.function;

import yeamy.sqlite.statement.AbstractColumn;

import java.util.ArrayList;

/**
 * see more about:
 * <a href="https://www.sqlite.org/lang_datefunc.html">Date And Time Functions</a>
 * e.g.
 * <pre>
 * DateFormat.datetime().unixEpoch(1669061360).startOfMonth().localTime();
 * // sql:  datetime(1669061360, 'unixepoch', 'start of month', 'localtime')
 * // result:
 * // unixepoch        -> 2022-11-21 20:09:20
 * // start of month   -> 2022-01-01 00:00:00
 * // localtime(+0800) -> 2022-11-01 08:00:00
 * </pre>
 */
public final class DateFormat extends AbstractColumn<DateFormat> {
    private final Src src = new Src();
    private final String function;
    private String column;
    private final ArrayList<String> modifiers = new ArrayList<>();

    public final class Src {

        /**
         * format string
         *
         * @param time the time value to format, and it can be in any of the following formats shown below.
         *             <tr><td>YYYY-MM-DD</td>               <td>2010-12-30</td></tr>
         *             <tr><td>YYYY-MM-DD HH:MM</td>         <td>2010-12-30 12:10</td></tr>
         *             <tr><td>YYYY-MM-DD HH:MM:SS.SSS</td>  <td>2010-12-30 12:10:04.100</td></tr>
         *             <tr><td>MM-DD-YYYY HH:MM</td>         <td>12-30-2010 12:10</td></tr>
         *             <tr><td>HH:MM</td>                    <td>12:10</td></tr>
         *             <tr><td>YYYY-MM-DDTHH:MM</td>         <td>2010-12-30 12:10</td></tr>
         *             <tr><td>HH:MM:SS</td>                 <td>12:10:04</td></tr>
         *             <tr><td>YYYYMMDD HHMMSS</td>          <td>20101230 121001</td></tr>
         *             <tr><td>now</td>                      <td>2010-12-30</td></tr>
         */
        public DateFormat str(String time) {
            DateFormat.this.column = "'" + time + "'";
            return DateFormat.this;
        }

        /**
         *
         * @param time DDDDDDDDDD format time value immediately follow unixepoch
         */
        public DateFormat unixEpoch(int time) {
            DateFormat.this.column = String.valueOf(time);
            add("unixepoch");
            return DateFormat.this;
        }

        /**
         * format column
         *
         * @param column the column to format
         */
        public DateFormat column(String column) {
            DateFormat.this.column = "`" + column + "`";
            return DateFormat.this;
        }
    }

    private DateFormat(String function) {
        this.function = function;
    }

    private DateFormat add(String raw) {
        modifiers.add(raw);
        return this;
    }

    private DateFormat add(int num, String unit) {
        modifiers.add((num > 0 ? "+" : "-") + num + ' ' + unit);
        return this;
    }

    /**
     * datetime(1669061360, 'unixepoch', 'start of month', 'localtime')
     * @param years
     * @return
     */
    public DateFormat addYears(int years) {
        return add(years, "years");
    }

    public DateFormat addMonths(int months) {
        return add(months, "months");
    }

    public DateFormat addDays(int days) {
        return add(days, "days");
    }

    public DateFormat addHours(int hours) {
        return add(hours, "hours");
    }

    public DateFormat addMinutes(int minutes) {
        return add(minutes, "minutes");
    }

    public DateFormat addSeconds(int seconds) {
        return add(seconds, "seconds");
    }

    /**
     * @param seconds  second
     * @param fraction fraction-of-second
     * @return this
     */
    public DateFormat addSeconds(int seconds, int fraction) {
        modifiers.add((seconds > 0 ? "+" : "-") + seconds + '.' + fraction + " seconds");
        return this;
    }

    public DateFormat startOfDay() {
        return add("start of day");
    }

    public DateFormat startOfMonth() {
        return add("start of month");
    }

    public DateFormat startOfYear() {
        return add("start of year");
    }

    /**
     * only works if it immediately follows a time value in the DDDDDDDDDD format.
     */
    public DateFormat unixEpoch() {
        return add("unixepoch");
    }

    /**
     * local time zone
     */
    public DateFormat localTime() {
        return add("localtime");
    }

    /**
     * utc time zone
     */
    public DateFormat utc() {
        return add("utc");
    }

    /**
     * the day of week
     *
     * @param weekday 0(Sunday) ~ 6(Saturday)
     * @return this
     */
    public DateFormat weekday(int weekday) {
        return add("weekday " + weekday);
    }

    @Override
    public void toSQL(StringBuilder sql) {
        sql.append(function).append(column);
        for (String modifier : modifiers) {
            sql.append(", '").append(modifier).append('\'');
        }
        sql.append(')');
    }

    /**
     * sqlite date(), the same with strftime('%Y-%m-%d', ...)
     * e.g. 2010-12-30
     */
    public static Src date() {
        return new DateFormat("date(").src;
    }

    /**
     * sqlite time(), the same with strftime('%H:%M:%S', ...)
     * e.g. 12:10:01
     */
    public static Src time() {
        return new DateFormat("time(").src;
    }

    /**
     * sqlite datetime(), the same with strftime('%Y-%m-%d %H:%M:%S', ...)
     * e.g. 2010-12-30 12:10
     */
    public static Src datetime() {
        return new DateFormat("datetime(").src;
    }

    /**
     * sqlite julianday(), the same with strftime('%J', ...)
     */
    public static Src julianDay() {
        return new DateFormat("julianday(").src;
    }

    /**
     * sqlite strftime()
     *
     * @param pattern the pattern describing the date and time format
     *                <tr><td>%d</td>		<td>day of month: 00</td></tr>
     *                <tr><td>%f</td>		<td>fractional seconds: SS.SSS</td></tr>
     *                <tr><td>%H</td>		<td>hour: 00-24</td></tr>
     *                <tr><td>%j</td>		<td>day of year: 001-366</td></tr>
     *                <tr><td>%J</td>		<td>Julian day number (fractional)</td></tr>
     *                <tr><td>%m</td>		<td>month: 01-12</td></tr>
     *                <tr><td>%M</td>		<td>minute: 00-59</td></tr>
     *                <tr><td>%s</td>		<td>seconds since 1970-01-01</td></tr>
     *                <tr><td>%S</td>		<td>seconds: 00-59</td></tr>
     *                <tr><td>%w</td>		<td>day of week 0-6 with Sunday==0</td></tr>
     *                <tr><td>%W</td>		<td>week of year: 00-53</td></tr>
     *                <tr><td>%Y</td>		<td>year: 0000-9999</td></tr>
     *                <tr><td>%%</td>		<td>%</td></tr>
     */
    public static Src format(String pattern) {
        return new DateFormat("strftime(" + pattern + ", ").src;
    }

}
