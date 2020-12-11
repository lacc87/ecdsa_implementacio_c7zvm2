package hu.unideb;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ECDSA {

    public Point getSigniture(EC curve, BigInteger private_key, BigInteger msg_hash) {
        BigInteger r = BigInteger.ZERO;
        BigInteger s = BigInteger.ZERO;
        BigInteger k;
        Point p;
        while(r.compareTo(BigInteger.ZERO) == 0 || s.compareTo(BigInteger.ZERO) == 0) {
            k = this.generatePrivateKey(curve);
            p = curve.point_multiplication(curve.getG(), k);
            r = p.getX().mod(curve.getN());
            s = k.modInverse(curve.getN()).multiply(r.multiply(private_key).add(msg_hash)).mod(curve.getN());
        }

        System.out.println("Signiture:");
        System.out.println(r);
        System.out.println(s);

        return new Point(r, s);
    }

    public BigInteger generatePrivateKey(EC curve) {
        Random rnd = new Random();
        BigInteger result = new BigInteger(curve.getN().bitLength(), rnd);
        while( result.compareTo(curve.getN()) >= 0  || result.compareTo(BigInteger.ZERO) == 0) {
            result = new BigInteger(curve.getN().bitLength(), rnd);
        }
        return result;
    }

    public Point generatePublicKey(EC curve, BigInteger private_key) {
        return curve.point_multiplication(curve.getG(), private_key);
    }

    public Boolean isSignitureValid(EC curve, Point public_key, Point signiture, BigInteger msg_hash) {
        BigInteger r = signiture.getX();
        BigInteger s = signiture.getY();

        BigInteger w = s.modInverse(curve.getN());

        Point u1 = curve.point_multiplication(curve.getG(), msg_hash.multiply(w).mod(curve.getN()));
        Point u2 = curve.point_multiplication(public_key, r.multiply(w).mod(curve.getN()));
        Point R = curve.point_addition(u1, u2);

        return r.compareTo(R.getX()) == 0;
    }

    public static BigInteger getMsgHash(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            BigInteger number = new BigInteger(1, md.digest(value.getBytes(StandardCharsets.UTF_8)));
            return number;
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

}
