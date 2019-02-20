package org.monet.space.kernel.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class VersionHelper {
    private static final String REGEXP_VERSION = "([^\\.]*)\\.([^\\.]*)\\.?([^\\.]*)?";

    public static String getMajorVersion(String version) {
        Pattern pattern = Pattern.compile(REGEXP_VERSION);
        Matcher matcher = pattern.matcher(version);

        if (!matcher.find())
            throw new RuntimeException(String.format("App version variable is wrong! Current value: %s", version));

        if (matcher.groupCount() < 2)
            throw new RuntimeException(String.format("App version variable is wrong! Current value: %s", version));

        return matcher.group(1) + "." + matcher.group(2);
    }

    public static String getMinorVersion(String version) {
        Pattern pattern = Pattern.compile(REGEXP_VERSION);
        Matcher matcher = pattern.matcher(version);

        if (!matcher.find())
            throw new RuntimeException(String.format("App version variable is wrong! Current value: %s", version));

        if (matcher.groupCount() <= 2)
            return "0";

        if (matcher.group(3).isEmpty())
            return "0";

        return matcher.group(3);
    }

    public static int versionToNumber(String version) {
        version = version.replaceAll("\\.", "").replaceAll("v", "");

        if (version.length() < 3 || version.length() > 4)
            throw new RuntimeException(String.format("Monet version not supported %s", version));

        String minorVersionNumber = version.substring(2);

        String versionNumber = version.substring(0, 2);
        versionNumber = (4-minorVersionNumber.length() > 0) ? StringUtils.rightPad(versionNumber, 4-minorVersionNumber.length(), "0") : "";

        return Integer.valueOf(versionNumber + minorVersionNumber);
    }

}
