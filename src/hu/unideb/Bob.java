package hu.unideb;

import java.math.BigInteger;

public class Bob {

    private EC curve;
    private Point public_key;
    private ECDSA ecdsa;

    public Bob(EC curve) {
        this.curve = curve;
        this.ecdsa = new ECDSA();
    }

    public void setPublic_key(Point public_key) {
        this.public_key = public_key;
    }

    public Boolean isSignitureValid(Point signiture, BigInteger msg_hash) {
        return this.ecdsa.isSignitureValid(this.curve, this.public_key, signiture, msg_hash);
    }

}
