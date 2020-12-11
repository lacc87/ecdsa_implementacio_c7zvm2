package hu.unideb;

import java.math.BigInteger;

public class Point {

    private BigInteger x;
    private BigInteger y;

    public static final Point ZERO = new Point(BigInteger.ZERO, BigInteger.ZERO);

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }
}
