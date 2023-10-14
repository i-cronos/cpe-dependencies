package pe.ibk.cpe.dependencies.common.util;

import java.util.regex.Pattern;

public final class RegExpressionUtil extends Util {
    public static final String PHONE = "^[0-9]{9}$";
    public static final String EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public boolean match(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}
