package xyz.wcd.psi;

import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightIdentifier;

public class InjectLightIdentifier extends LightIdentifier {
    private String myText;

    public InjectLightIdentifier(PsiManager manager, String text) {
        super(manager, text);
        myText = text;
    }
}
