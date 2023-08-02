package top.openyuan.sdt.keypair;

import top.openyuan.sdt.keypair.model.KeyPair;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RSAKeyPairGeneratorTest {

    @Test
    public void createKeyPair() {
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        KeyPair keyPair = rsaKeyPairGenerator.createKeyPair();
        assertNotNull(keyPair);
    }
}
