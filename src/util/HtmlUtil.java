package util;

import model.Company;

public class HtmlUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String formatDates(Company.Post position) {
        return DateUtil.format(position.getStartWork()) + " - " + DateUtil.format(position.getEndWork());
    }
}
