/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.simplefileencryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author zidna
 */
public class Encryption {
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;
    private static final byte[] iv = { 11, 22, 33, 44, 99, 88, 77, 66 };
    public static void encryption(File file, File encryptedFile, File decryptedFile) throws IOException {
        try {
        	// create SecretKey using KeyGenerator
		SecretKey key = KeyGenerator.getInstance("DES").generateKey();
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

		// get Cipher instance and initiate in encrypt mode
		encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		encryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
                
                // get Cipher instance and initiate in decrypt mode
		decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		decryptCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
                
		// method to encrypt clear text file to encrypted file
		encrypt(new FileInputStream(file), new FileOutputStream(encryptedFile));
                
                // method to decrypt clear text file to decrypted file
		decrypt(new FileInputStream(encryptedFile), new FileOutputStream(decryptedFile));
                
		System.out.println("DONE");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
            | InvalidAlgorithmParameterException | IOException e) {
            e.printStackTrace();
	}
    }
    
    private static void decrypt(InputStream in, OutputStream out) throws IOException {
	// create CipherInputStream to decrypt the data using decryptCipher
	in = new CipherInputStream(in, decryptCipher);
	writeData(in, out);
    }
    
    private static void encrypt(InputStream in, OutputStream out) throws IOException {
	// create CipherOutputStream to encrypt the data using encryptCipher
	out = new CipherOutputStream(out, encryptCipher);
	writeData(in, out);
    }
    
    // utility method to read data from input stream and write to output stream
    private static void writeData(InputStream in, OutputStream out) throws IOException {
	byte[] buffer = new byte[1024];
	int length;
	// read and write operation
	while ((length = in.read(buffer)) > 0) {
        	out.write(buffer, 0, length);
	}
	in.close();
        out.close();
    }
}
