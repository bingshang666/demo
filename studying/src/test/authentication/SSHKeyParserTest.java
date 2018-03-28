package authentication;

import authentication.SSHKeyParser;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by eefijjt on 4/12/2017.
 */
public class SSHKeyParserTest {

    private String publicKeyStrRSA = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDE2B3Snp5tghCmgr2fm04fvDDRkPxVsF9zmGjAYApPWkmzZ2KMd02xfgp4WYrXfU+M39zWg9eVF2YKeNqMzyRkPmBcT4IkxfkU1uhJSJUkrvhM9W6dXTpiSQ8VwOznF2CjMpb0Gw2i623HOKNOv21VR7jTpr2SS2xqWXXo+kclI+zENzaonK9Hf45h16yfAyj7V0bljhtci7uALY2xO00V41h6VjgRAW8C4GewHsxyy48V2EMTu3Bmvr5vcWd4U2/YDaXiyHCDBZiTSjHfvsdvWFR33qL0mZ+aqX8kngsjcQiInwzM5Wu28GKQJFR1qpIcMQICL6m32CyiqQBO6KbJ eefijjt@CN00119138\n";
    private String publicKeyStrDSA = "ssh-dss AAAAB3NzaC1kc3MAAACBAJLNaXmhFelBg+qSFfxr45p1L41zEU17QwR39y3RrVXB1j+eEpj4DfHpE/ABk4Miixa2cy+zCDMXGT9Et5M2cwoOReAGqX8uLucGeRJfqqHpFvOIQ3dr4u60jXrDN8A47yZxeoYEl+HI1WwQelh+wTF1ezS39XS28nb5/dsuK7PPAAAAFQCZuU/AGka4Xz6RH7EEfb+0YYaA8wAAAIBXKQTlcfE719oxkHkDt7x3VIfkyZCbGnb9Zl4S5psv1eXwaOeOGf08ip4XxeM2FXuOdhpuT9WNxqoCmDuhyqtL48w/e5HCym4qZ6lfRQqAU+eOynBdecdFm0bQorYGOn5pNydns1j4BGt7sJEfkxqKLrwXoCqWwOsxsh1GV8Qs5gAAAIB7YzhIs6v8b3F61H+imXkPz7E/LysfC6LIsY5Pv+jbF99OXOPveTBNtawooKfJ5LYoHQBwxTIDeUkXMRPyCpPy0B1c4RfIDSC10PwqzcsMcAMO/YTWH3zaXDIuiMg2Al6TKVvomZf39bKc3zJNdUZsozzaomqWdPIsHkeNIMF2BA== root@singlevm";

    @Test
    public void testGeneratePublicKeyFromString() throws Exception {
        PublicKey rsaKey = SSHKeyParser.generatePublicKeyFromString(publicKeyStrRSA);
        PublicKey dsaKey = SSHKeyParser.generatePublicKeyFromString(publicKeyStrDSA);

        assertTrue(rsaKey instanceof RSAPublicKey);
        assertTrue(dsaKey instanceof DSAPublicKey);
    }

    @Test
    public void testPublicKeyConsistence() throws Exception {
        PublicKey rsaKeyA = SSHKeyParser.generatePublicKeyFromString(publicKeyStrRSA);
        PublicKey rsaKeyB = SSHKeyParser.generatePublicKeyFromString(publicKeyStrRSA);

        assertEquals(rsaKeyA, rsaKeyB);
    }

    @Test
    public void testPublicKeyStrMatch() throws Exception {
        PublicKey rsaKeyA = SSHKeyParser.generatePublicKeyFromString(publicKeyStrRSA);
        try {
            assertEquals(getPublicKeyCode(publicKeyStrRSA), encodePublicKey(rsaKeyA));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGeneratePublicKeyFromStringWithException() throws Exception {
        String negRsaStr = "ssh-rsa";
        try {
            PublicKey rsaKey = SSHKeyParser.generatePublicKeyFromString(negRsaStr);
            fail("should throw excpetion here");
        } catch (Exception e) {
            assertEquals("public key format invalid", e.getMessage());
        }
    }

    private String getPublicKeyCode(String publicKeyStr) {
        String[] contents = publicKeyStr.split(" ");
        return contents[1];
    }

    private String encodePublicKey(final PublicKey publicKey) throws IOException {
        if (publicKey.getAlgorithm().equals("RSA")) {
            RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
            ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(byteOs);
            dos.writeInt("ssh-rsa".getBytes().length);
            dos.write("ssh-rsa".getBytes());
            dos.writeInt(rsaPublicKey.getPublicExponent().toByteArray().length);
            dos.write(rsaPublicKey.getPublicExponent().toByteArray());
            dos.writeInt(rsaPublicKey.getModulus().toByteArray().length);
            dos.write(rsaPublicKey.getModulus().toByteArray());
            return new String(Base64.getEncoder().encode(byteOs.toByteArray()));
        } else {
            throw new IllegalArgumentException("Unknown public key encoding: " + publicKey.getAlgorithm());
        }
    }

}