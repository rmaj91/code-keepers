package com.rmaj91.file.processing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;

@Service
@Slf4j
public class CertificateService {

    public static final String X_509 = "X.509";
    public static final String EMPTY = "";
    public static final String COMMA_SEPARATOR = ", ";
    public static final String ORGANIZATION_MATCHER = "O=";
    public static final String COMMON_NAME_MATCHER = "CN=";

    public String getCommonName(X509Certificate certificate) {
        String subjectDN = certificate.getSubjectDN().getName();
        return Arrays.stream(subjectDN.split(", "))
                .filter(dn -> dn.contains(COMMON_NAME_MATCHER))
                .map(dn -> dn.substring(3))
                .findAny()
                .orElse(EMPTY);
    }

    public String getOrganizationName(X509Certificate certificate) {
        String subjectDN = certificate.getSubjectDN().getName();
        return Arrays.stream(subjectDN.split(COMMA_SEPARATOR))
                .filter(dn -> dn.contains(ORGANIZATION_MATCHER))
                .map(dn -> dn.substring(2))
                .findAny()
                .orElse(EMPTY);
    }

    public void validateCertificate(String base64X509Certificate) {
        X509Certificate x509Certificate = getX509Certificate(base64X509Certificate);
        validateCertificate(x509Certificate);
    }

    public void validateCertificate(X509Certificate certificate) {
        try {
            PublicKey caPublicKey = fetchCaPublicKey(certificate);
            certificate.verify(caPublicKey);
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    private PublicKey fetchCaPublicKey(X509Certificate certificate) {
        PublicKey publicKey;
        if (certificate.getSubjectDN().equals(certificate.getIssuerDN())) {
            publicKey = certificate.getPublicKey();
        } else {
            publicKey = getCaCertificate(certificate.getIssuerDN());
        }
        return publicKey;
    }

    private PublicKey getCaCertificate(Principal issuerDN) {
        // todo to implement
        return null;
    }

    public X509Certificate getX509Certificate(String base64Certificate) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(X_509);
            byte[] certificateBytes = Base64.getDecoder().decode(base64Certificate);
            return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));
        } catch (CertificateException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }
}
