package pe.ibk.cpe.dependencies.common.util;

import org.apache.commons.codec.binary.Hex;
import pe.ibk.cpe.dependencies.common.exception.BaseException;
import pe.ibk.cpe.dependencies.common.exception.DependencyException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil extends Util {
    public static final String SHA256 = "SHA-256";
    public static final String SHA512 = "SHA-512";

    public String digest(String data, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] result = md.digest(data.getBytes());

            return Hex.encodeHexString(result);
        } catch (NoSuchAlgorithmException ex) {
            throw new DependencyException(BaseException.Error.builder()
                    .systemMessage(ex.getMessage())
                    .userMessage("Hash util error")
                    .build());
        }
    }
}
