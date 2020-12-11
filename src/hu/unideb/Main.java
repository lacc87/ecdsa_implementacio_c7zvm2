package hu.unideb;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        EC curve = new EC();


        Alice alice = new Alice(curve);
        alice.generate_keys();

        String from_alice_to_bob = "Hello Bob!";
        BigInteger from_alice_to_bob_hash = ECDSA.getMsgHash(from_alice_to_bob);

        Bob bob = new Bob(curve);
        bob.setPublic_key(alice.getPublic_key());

        Point signiture = alice.getSigniture(from_alice_to_bob_hash);

        if (bob.isSignitureValid(signiture, from_alice_to_bob_hash)) {
            System.out.println("The message is from Alice");
        } else {
            System.out.println("The message is NOT from Alice");
        }

        String modified_from_alice_to_bob = "Hello Bob?!";
        BigInteger from_modified_alice_to_bob_hash = ECDSA.getMsgHash(modified_from_alice_to_bob);

        if (bob.isSignitureValid(signiture, from_modified_alice_to_bob_hash)) {
            System.out.println("The message is from Alice");
        } else {
            System.out.println("The message is NOT from Alice");
        }

    }
}
