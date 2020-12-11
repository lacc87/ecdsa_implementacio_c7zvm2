package hu.unideb;

import java.math.BigInteger;

public class EC {

    private BigInteger a, b, p, n, h;
    private Point G;

    public EC() {
        // NIST P-256, https://nvlpubs.nist.gov/nistpubs/SpecialPublications/NIST.SP.800-186-draft.pdf 12. oldal

        a = new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948");
        b = new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291");
        p = new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951");
        G = new Point(new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"),
                new BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109"));
        n = new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369");
        h = new BigInteger("1");

        /* secp256k1
        a = new BigInteger("0");
        b = new BigInteger("7");
        p = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);
        G = new Point(new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16),
                new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16));
        n = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);*/

    }

    public Point point_double(Point a) {
        if(a.getX().compareTo(BigInteger.ZERO) == 0 && a.getY().compareTo(BigInteger.ZERO) == 0) {
            return Point.ZERO;
        }
        BigInteger s1 = a.getY().multiply(BigInteger.TWO).modInverse(getP());
        BigInteger s = a.getX().multiply(new BigInteger("3")).multiply(a.getX()).add(getA()).multiply(s1).mod(getP());
        BigInteger x = s.multiply(s).subtract(a.getX().multiply(BigInteger.TWO)).mod(getP());
        BigInteger y = s.multiply(a.getX().subtract(x)).subtract(a.getY()).mod(getP());

        return new Point(x, y);
    }

    public Point point_addition(Point a, Point b) {
        if(a.getY().compareTo(BigInteger.ZERO) == 0 && a.getX().compareTo(BigInteger.ZERO) == 0) {
            return b;
        }
        if(b.getY().compareTo(BigInteger.ZERO) == 0 && b.getX().compareTo(BigInteger.ZERO) == 0) {
            return a;
        }
        if(a.getX().compareTo(b.getX()) == 0) {
            if(a.getY().add(b.getY()).mod(getP()).compareTo(BigInteger.ZERO) == 0) {
                return Point.ZERO;
            }
            return point_double(a);
        }
        BigInteger s1 = b.getX().subtract(a.getX()).modInverse(getP());
        BigInteger s = b.getY().subtract(a.getY()).multiply(s1).mod(getP());
        BigInteger x = s.multiply(s).subtract(a.getX()).subtract(b.getX()).mod(getP());
        BigInteger y = s.multiply(a.getX().subtract(x)).subtract(a.getY()).mod(getP());
        return new Point(x, y);
    }

    public Point point_multiplication(Point p, BigInteger private_key) {
        if((private_key.compareTo(BigInteger.ZERO) == 0) ||
                (p.getX().compareTo(BigInteger.ZERO) == 0 && p.getY().compareTo(BigInteger.ZERO) == 0)) {
            return Point.ZERO;
        }
        BigInteger e = new BigInteger("3").multiply(private_key.mod(getN()));
        Point inv = new Point(p.getX(), p.getY().multiply(new BigInteger("-1")));
        Point R = new Point(p.getX(), p.getY());
        int length = e.bitLength();
        for(int i = length-2; i >= 1; --i) {
            R = point_double(R);
            if(e.testBit(i) && !private_key.testBit(i)) {
                R = point_addition(R, p);
            }
            if(!e.testBit(i) && private_key.testBit(i)) {
                R = point_addition(R, inv);
            }
        }
        return R;
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getH() {
        return h;
    }

    public Point getG() {
        return G;
    }
}
