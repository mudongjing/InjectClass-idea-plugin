package xyz.wcd.psi;

import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightModifierList;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InjectLightModifierList extends LightModifierList {
    private final Map<String, PsiAnnotation> myAnnotations;
    private final Set<String> myImplicitModifiers;
    public InjectLightModifierList(@NotNull PsiManager manager) {
        this(manager, JavaLanguage.INSTANCE);
    }
    public InjectLightModifierList(PsiManager manager, @NotNull Language language){
        this(manager, language, Collections.emptyList());
    }
    public InjectLightModifierList(PsiManager manager, final Language language, Collection<String> implicitModifiers, String... modifiers) {
        super(manager, language, modifiers);
        this.myAnnotations = new HashMap<>();
        this.myImplicitModifiers = new HashSet<>(implicitModifiers);
    }

    @Override
    public void setModifierProperty(@NotNull String name, boolean value) throws IncorrectOperationException {
        if (value) addModifier(name);
    }
}
