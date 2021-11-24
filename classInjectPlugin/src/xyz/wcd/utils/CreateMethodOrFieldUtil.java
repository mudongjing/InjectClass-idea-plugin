package xyz.wcd.utils;

import com.intellij.lang.LighterAST;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.impl.light.LightModifierList;
import com.intellij.psi.util.PsiUtil;
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

    public static LightFieldBuilder createInjectField(PsiClass psiClass,PsiClass targetClass){
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
//        String type=psiClass.getName();
        PsiClassType typeClass = elementFactory.createType(psiClass);
        String name=psiClass.getName().substring(0,1).toLowerCase()+psiClass.getName().substring(1);
//        PsiField field = elementFactory.createField(name, elementFactory.createType(psiClass));
//        field.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
        InjectLightFieldBuilder injectLightFieldBuilder = new InjectLightFieldBuilder(name,typeClass,psiClass.getManager());
        injectLightFieldBuilder.setModifiers(PsiModifier.PRIVATE);
        return injectLightFieldBuilder;
//        PsiType typeFromText = elementFactory.createTypeFromText(targetClass.getQualifiedName(), targetClass);
//        System.out.println("1");
//        LightFieldBuilder lightFieldBuilder = new LightFieldBuilder(psiClass.getManager(), name, typeFromText);
//        System.out.println("2");
//        LightModifierList modifierList = (LightModifierList) lightFieldBuilder.getModifierList();
//        System.out.println("3");
//        modifierList.addModifier(PsiModifier.PRIVATE);
//        System.out.println("4");
//        return lightFieldBuilder;
    }
    public static PsiImportStatement createImport(PsiClass psiClass){
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        return elementFactory.createImportStatement(psiClass);
    }
}
