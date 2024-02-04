package ru.marthastudios.robloxcasino.util;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class DiceRollUtil {
    public static double generateRandomNumber(String serverSeed, String clientSeed, String salt) {
        try {
            String seed = serverSeed + clientSeed + salt;
            byte[] hash = getSHA256(seed.getBytes());

            BigInteger bigInteger = new BigInteger(1, hash);
            BigInteger maxValue = BigInteger.valueOf(Long.MAX_VALUE);
            BigInteger randomBigInteger = bigInteger.mod(maxValue);

            double randomDouble = randomBigInteger.doubleValue() / maxValue.doubleValue();

            return Math.max(0.0, randomDouble);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static String generateSecret() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        return bytesToHex(saltBytes);
    }

    private static byte[] getSHA256(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
