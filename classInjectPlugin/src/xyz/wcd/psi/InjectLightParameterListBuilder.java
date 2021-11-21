package xyz.wcd.psi;

import com.intellij.lang.Language;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightParameterListBuilder;

public class InjectLightParameterListBuilder extends LightParameterListBuilder {
    public InjectLightParameterListBuilder(PsiManager manager, Language language) {
        super(manager, language);
    }
}
