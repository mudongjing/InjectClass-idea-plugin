package xyz.wcd.psi;

import com.intellij.lang.Language;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightReferenceListBuilder;

public class InjectLightReferenceListBuilder extends LightReferenceListBuilder {
    public InjectLightReferenceListBuilder(PsiManager manager, Language language, Role role) {
        super(manager, language, role);
    }
}
