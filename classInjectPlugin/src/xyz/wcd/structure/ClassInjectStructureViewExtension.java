package xyz.wcd.structure;

import com.intellij.ide.structureView.StructureViewExtension;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.java.PsiFieldTreeElement;
import com.intellij.ide.structureView.impl.java.PsiMethodTreeElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;
import xyz.wcd.psi.InjectLightFieldBuilder;
import xyz.wcd.psi.InjectLightMethodBuilder;

import java.util.Arrays;
import java.util.stream.Stream;

public class ClassInjectStructureViewExtension implements StructureViewExtension {
    @Nullable
    @Override
    public Object getCurrentEditorElement(Editor editor, PsiElement psiElement) { return null;    }
    @Override
    public Class<? extends PsiElement> getType() { return PsiClass.class;  }

    @Override
    public StructureViewTreeElement[] getChildren(PsiElement psiElement) {
        final PsiClass parentClass = (PsiClass) psiElement;
        final Stream<PsiFieldTreeElement> InjectFields = Arrays.stream(parentClass.getFields())
                .filter(InjectLightFieldBuilder.class::isInstance)
                .map(psiField -> new PsiFieldTreeElement(psiField, false));
        final Stream<PsiMethodTreeElement> InjectMethods = Arrays.stream(parentClass.getMethods())
                .filter(InjectLightMethodBuilder.class::isInstance)
                .map(psiMethod -> new PsiMethodTreeElement(psiMethod, false));
        return Stream.concat(InjectFields, InjectMethods).toArray(StructureViewTreeElement[]::new);
    }
}
