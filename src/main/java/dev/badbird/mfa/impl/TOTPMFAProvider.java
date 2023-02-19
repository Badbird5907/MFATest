package dev.badbird.mfa.impl;

import com.google.gson.JsonObject;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import dev.badbird.mfa.MFAData;
import dev.badbird.mfa.MFAProvider;
import dev.badbird.mfa.MFAType;
import lombok.Getter;

@Getter
public class TOTPMFAProvider implements MFAProvider<TOTPMFAProvider.TOTPMFAData> {

    private GoogleAuthenticator gAuth = new GoogleAuthenticator();
    private GoogleAuthenticatorKey key = gAuth.createCredentials();
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isValid(String code) {
        // check if code is a integer
        try {
            Integer.parseInt(code.replace(" ", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean isCorrect(String code, TOTPMFAData data) {
        return gAuth.authorize(data.secret, Integer.parseInt(code.replace(" ", "")));
    }

    @Override
    public MFAType getType() {
        return MFAType.TOTP;
    }

    @Override
    public String generateSecret(String username) {
        return key.getKey();
    }

    public static class TOTPMFAData implements MFAData {
        private String secret;

        @Override
        public MFAType getType() {
            return MFAType.TOTP;
        }

        @Override
        public JsonObject serialize() {
            JsonObject object = new JsonObject();
            object.addProperty("secret", secret);
            return object;
        }

        @Override
        public MFAData deserialize(JsonObject object) {
            this.secret = object.get("secret").getAsString();
            return this;
        }
    }
}
