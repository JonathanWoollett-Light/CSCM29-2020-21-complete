package LabUtils;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class CryptoUtils {

    /**
     * Not for use by students
     */
    public static PublicKey rsaPubKeyFromModAndExponent(BigInteger modulus,
                                                        BigInteger exponent) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    /**
     * Not for use by students
     */
    public static PrivateKey rsaPrivKeyFromModAndExponent(BigInteger modulus,
                                                          BigInteger exponent) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    /**
     * Not for use by students
     */
    public static KeyPair generateKeyPair(int numBits) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(numBits);
        return keyGen.genKeyPair();
    }

    /**
     * Not for use by students
     */
    public static byte[] sign(PrivateKey privKey, byte[] message)
            throws NoSuchAlgorithmException, SignatureException,
            InvalidKeyException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privKey);
        signature.update(message);
        return signature.sign();
    }

}
