package xyz.wcd.utils;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

public class PsiAnnotationSearchUtil {
    public static PsiAnnotation findAnnotation(@NotNull PsiModifierListOwner psiModifierListOwner, String ... annotationFQNs) {
        return Stream.of(annotationFQNs).map(psiModifierListOwner::getAnnotation).filter(Objects::nonNull).findAny().orElse(null);
    }
}
