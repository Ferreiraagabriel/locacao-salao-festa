package br.ufms.locacao.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class SenhaUtil {
    private static final int ITERACOES = 120_000;
    private static final int TAMANHO_CHAVE = 256;

    private SenhaUtil() { }

    public static String gerarHash(String senha) {
        try {
            byte[] sal = new byte[16];
            new SecureRandom().nextBytes(sal);
            byte[] hash = gerarChave(senha.toCharArray(), sal);
            return Base64.getEncoder().encodeToString(sal) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível proteger a senha.", e);
        }
    }

    public static boolean conferir(String senha, String valorSalvo) {
        try {
            String[] partes = valorSalvo.split(":");
            byte[] sal = Base64.getDecoder().decode(partes[0]);
            byte[] hashSalvo = Base64.getDecoder().decode(partes[1]);
            byte[] hashDigitado = gerarChave(senha.toCharArray(), sal);
            return java.security.MessageDigest.isEqual(hashSalvo, hashDigitado);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] gerarChave(char[] senha, byte[] sal) throws Exception {
        PBEKeySpec especificacao = new PBEKeySpec(senha, sal, ITERACOES, TAMANHO_CHAVE);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(especificacao).getEncoded();
        } finally {
            especificacao.clearPassword();
        }
    }
}
