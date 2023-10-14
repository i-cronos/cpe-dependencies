package pe.ibk.cpe.dependencies.common.util;

import java.util.*;

public final class UtilFactory {
    private static final Map<Util.Type, Util> utils = new HashMap<>();

    static {
        utils.put(Util.Type.JSON, new JsonUtil());
        utils.put(Util.Type.REGEX, new RegExpressionUtil());
        utils.put(Util.Type.DIGEST, new DigestUtil());
    }

    public Util solve(Util.Type type) {
        return utils.get(type);
    }
}
