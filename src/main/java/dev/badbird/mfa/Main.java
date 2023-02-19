package dev.badbird.mfa;

import com.google.gson.JsonObject;
import dev.badbird.mfa.impl.TOTPMFAProvider;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            MFAManager manager = MFAManager.getInstance();
            TOTPMFAProvider totpmfaProvider = TOTPMFAProvider.class.cast(manager.getProvider(MFAType.TOTP));
            System.out.print("Generate > ");
            boolean b = scanner.nextLine().equalsIgnoreCase("true");
            if (b) {
                String secret = totpmfaProvider.generateSecret("Bob");
                System.out.println("Secret: " + secret);
                System.out.print("Code > ");
                String code = scanner.nextLine();
                System.out.println("Code is valid: " + totpmfaProvider.isValid(code));
                JsonObject object = new JsonObject();
                object.addProperty("secret", secret);
                TOTPMFAProvider.TOTPMFAData data = (TOTPMFAProvider.TOTPMFAData) new TOTPMFAProvider.TOTPMFAData().deserializeFully(object);
                System.out.println("Code is correct: " + totpmfaProvider.isCorrect(code, data));
            } else {
                System.out.print("Secret > ");
                String secret = scanner.nextLine();
                System.out.print("Code > ");
                String code = scanner.nextLine();
                System.out.println("Code is valid: " + totpmfaProvider.isValid(code));
                JsonObject object = new JsonObject();
                object.addProperty("secret", secret);
                TOTPMFAProvider.TOTPMFAData data = (TOTPMFAProvider.TOTPMFAData) new TOTPMFAProvider.TOTPMFAData().deserializeFully(object);
                System.out.println("Code is correct: " + totpmfaProvider.isCorrect(code, data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
