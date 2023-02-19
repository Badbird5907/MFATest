package dev.badbird.mfa;

import dev.badbird.mfa.impl.TOTPMFAProvider;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MFAManager {
    @Getter
    private static final MFAManager instance = new MFAManager();
    private MFAManager() {}

    private List<MFAProvider<? extends MFAData>> providers = new ArrayList<>(Arrays.asList(
            new TOTPMFAProvider()
    ));

    public <T extends MFAData> MFAProvider<T> getProvider(MFAType type) {
        for (MFAProvider<? extends MFAData> provider : providers) {
            if (provider.getType() == type) {
                return (MFAProvider<T>) provider;
            }
        }
        return null;
    }

    public <T extends MFAData> boolean validateCode(String code, List<T> dataList) {
        for (T mfaData : dataList) {
            MFAProvider<T> provider = getProvider(mfaData.getType());
            boolean correct = false;
            if (provider != null && provider.isValid(code)) {
                correct = provider.isCorrect(code, mfaData);
                if (correct) return true;
                continue;
            }
        }
        return false;
    }
}
