package hu.unideb;

import java.math.BigInteger;

public class Alice {

    private EC curve;
    private BigInteger private_key;
    private Point public_key;
    private ECDSA ecdsa;

    public Alice(EC curve) {
        this.ecdsa = new ECDSA();
        this.curve = curve;
    }

    public void generate_keys() {
        this.private_key = this.ecdsa.generatePrivateKey(this.curve);
        this.public_key = this.ecdsa.generatePublicKey(this.curve, this.private_key);

        System.out.println("Private Key:");
        System.out.println(this.private_key);

        System.out.println("Public Key:");
        System.out.println(this.public_key.getX());
        System.out.println(this.public_key.getY());

    }

    public Point getPublic_key() {
        return public_key;
    }

    public Point getSigniture(BigInteger msg_hash) {
        return ecdsa.getSigniture(this.curve, this.private_key, msg_hash);
    }

}
