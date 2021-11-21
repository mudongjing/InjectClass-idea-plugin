package xyz.wcd.utils;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import xyz.wcd.psi.InjectLightFieldBuilder;
import xyz.wcd.psi.InjectLightMethodBuilder;

public class CreateMethodOrFieldUtil {
    public static PsiMethod createInjectSetterMethod(@NotNull PsiField psiField, @NotNull PsiClass psiClass){
        String setterInjectMethod = StringNameUtil.createSetterInjectMethod(psiField.getName());
        InjectLightMethodBuilder injectLightMethodBuilder = new InjectLightMethodBuilder(psiField.getManager(), setterInjectMethod)
                                                            .withMethodReturnType(PsiType.VOID).withContainingClass(psiClass)
                                                            .withParameter(psiField.getName(),psiField.getType())
                                                            .withNavigationElement(psiField)
                                                            .withModifier(PsiModifier.PUBLIC);
        PsiParameter setterParameter = injectLightMethodBuilder.getParameterList().getParameter(0);
        String codeBlockText = createCodeBlockText(psiField, setterParameter);
        injectLightMethodBuilder.withBody(PsiMethodUtil.createCodeBlockFromText(codeBlockText,injectLightMethodBuilder));
        return injectLightMethodBuilder;
    }
    private static String createCodeBlockText(@NotNull PsiField psiField, PsiParameter methodParameter) { return  String.format("%s.%s = %s; ", "this", psiField.getName(), methodParameter.getName());    }

    public static PsiMethod createInjectGetterMethod(@NotNull PsiField psiField, @NotNull PsiClass psiClass){
        String getterInjectMethod = StringNameUtil.createGetterInjectMethod(psiField.getName());
        InjectLightMethodBuilder injectLightMethodBuilder = new InjectLightMethodBuilder(psiField.getManager(), getterInjectMethod)
                .withMethodReturnType(psiField.getType()).withContainingClass(psiClass).withNavigationElement(psiField);
        injectLightMethodBuilder.withModifier(PsiModifier.PUBLIC);
        String blockText = String.format("return %s.%s;", "this", psiField.getName());
        injectLightMethodBuilder.withBody(PsiMethodUtil.createCodeBlockFromText(blockText,injectLightMethodBuilder));

        return injectLightMethodBuilder;
    }

    public static PsiField createInjectField(PsiClass psiClass){
        String type=psiClass.getName();
        String name=psiClass.getName().substring(0,1).toLowerCase()+psiClass.getName().substring(1);
        InjectLightFieldBuilder injectLightFieldBuilder = new InjectLightFieldBuilder(name, type, psiClass.getNavigationElement());
        injectLightFieldBuilder.setModifiers(PsiModifier.PRIVATE);
        return injectLightFieldBuilder;
    }
    public static PsiImportStatement createImport(PsiClass psiClass){
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        return elementFactory.createImportStatement(psiClass);
    }
}
