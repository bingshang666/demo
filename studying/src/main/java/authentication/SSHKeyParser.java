/*
 * Created : 4/7/17
 *
 * Copyright (c) 2017 Ericsson AB, Sweden.
 * All rights reserved.
 * The Copyright to the computer program(s) herein is the property of Ericsson AB, Sweden.
 * The program(s) may be used and/or copied with the written permission from Ericsson AB
 * or in accordance with the terms and conditions stipulated in the agreement/contract
 * under which the program(s) have been supplied.
 */
package authentication;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import static java.security.KeyFactory.getInstance;

/**
 * Created by eefijjt on 4/12/2017.
 */
public class SSHKeyParser {

    public static final String RSA = "RSA";
    public static final String DSA = "DSA";
    public static final String SSH_RSA = "ssh-rsa";
    public static final String SSH_DSA = "ssh-dss";

    private SSHKeyParser(){}

    public static byte[] parsePublicKeyString(String body) throws Exception {
        String[] contents = body.split(" ");
        if (contents == null || contents.length < 2) {
            throw new Exception("public key format invalid");
        }
        Base64 b64 = new Base64();
        return b64.decode(contents[1]);
    }

    /**
     * parse string to PublicKey object
     *
     * @param pub
     * @return
     * @throws Exception
     */
    public static PublicKey generatePublicKeyFromString(String pub) throws Exception {
        try {
            byte[] bytes = parsePublicKeyString(pub);
            try(DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))){
                byte[] bType = readFully(in);
                String type = new String(bType);
                switch (type) {
                    case SSH_RSA:
                        return readRSAPublicKey(in);
                    case SSH_DSA:
                        return readDSAPublicKey(in);
                    default:
                        throw new Exception("Only support RSA/DSA public key authentication");
                }
            }
        } catch (IOException e) {
            throw new Exception("Generate public key failed. " + e);
        }
    }

    public static PublicKey readRSAPublicKey(DataInputStream in) throws Exception{
        try{
            byte[] exp = readFully(in);
            byte[] mod = readFully(in);

            RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(mod), new BigInteger(exp));
            return getInstance(RSA).generatePublic(spec);
        }catch (Exception e){
            throw new Exception(e);
        }

    }

    public static PublicKey readDSAPublicKey(DataInputStream in) throws Exception {
        try{
            byte[] pb = readFully(in);
            byte[] qb = readFully(in);
            byte[] gb = readFully(in);
            byte[] yb = readFully(in);

            DSAPublicKeySpec dsaSpec = new DSAPublicKeySpec(new BigInteger(yb), new BigInteger(pb), new BigInteger(qb), new BigInteger(gb));
            return getInstance(DSA).generatePublic(dsaSpec);
        }catch (Exception e){
            throw new Exception(e);
        }

    }

    private static byte[] readFully(DataInputStream in) throws IOException {
        byte[] bytes = new byte[in.readInt()];
        in.readFully(bytes);
        return bytes;
    }

}
