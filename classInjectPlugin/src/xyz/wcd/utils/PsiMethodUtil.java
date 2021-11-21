package xyz.wcd.utils;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import org.jetbrains.annotations.NotNull;

public class PsiMethodUtil {
    public static PsiCodeBlock createCodeBlockFromText(@NotNull String blockText, @NotNull PsiElement psiElement) {
        final PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiElement.getProject());
        return elementFactory.createCodeBlockFromText("{" + blockText + "}", psiElement);
    }
}
