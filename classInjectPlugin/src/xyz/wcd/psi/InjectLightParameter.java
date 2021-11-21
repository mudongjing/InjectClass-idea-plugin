package xyz.wcd.psi;

import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.light.LightParameter;
import org.jetbrains.annotations.NotNull;

public class InjectLightParameter extends LightParameter {
    private final InjectLightIdentifier myNameIdentifier;
    public InjectLightParameter(@NotNull String name, @NotNull PsiType type, @NotNull PsiElement declarationScope) {
        this(name, type, declarationScope, JavaLanguage.INSTANCE);
    }

    public InjectLightParameter(@NotNull String name,
                                @NotNull PsiType type,
                                @NotNull PsiElement declarationScope,
                                @NotNull Language language) {
        super(name, type, declarationScope, language);
        super.setModifierList(new InjectLightModifierList(declarationScope.getManager(), language));
        myNameIdentifier = new InjectLightIdentifier(declarationScope.getManager(), name);
    }
}
