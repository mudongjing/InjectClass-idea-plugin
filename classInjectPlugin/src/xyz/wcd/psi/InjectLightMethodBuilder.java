package xyz.wcd.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightMethodBuilder;
import com.intellij.psi.impl.light.LightTypeParameterListBuilder;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class InjectLightMethodBuilder extends LightMethodBuilder {
    private PsiMethod myMethod;
    private ASTNode myASTNode;
    private PsiCodeBlock myBodyCodeBlock;
    // used to simplify comparing of returnType in equal method
    private String myReturnTypeAsText;

    public InjectLightMethodBuilder(PsiManager manager, String name){
        super(manager, JavaLanguage.INSTANCE, name,
                new InjectLightParameterListBuilder(manager, JavaLanguage.INSTANCE),new InjectLightModifierList(manager),
                new InjectLightReferenceListBuilder(manager, JavaLanguage.INSTANCE, PsiReferenceList.Role.THROWS_LIST),
                new LightTypeParameterListBuilder(manager, JavaLanguage.INSTANCE));
    }
    public InjectLightMethodBuilder withMethodReturnType(PsiType returnType) {
        setMethodReturnType(returnType);
        return this;
    }
    public InjectLightMethodBuilder withMethodReturnType(String returnType) {
        setMethodReturnType(returnType);
        return this;
    }
    public InjectLightMethodBuilder withModifier(@PsiModifier.ModifierConstant @NotNull @NonNls String modifier) {
        addModifier(modifier);
        return this;
    }
    public InjectLightMethodBuilder withNavigationElement(PsiElement navigationElement) {
        setNavigationElement(navigationElement);
        return this;
    }
    public InjectLightMethodBuilder withContainingClass(@NotNull PsiClass containingClass) {
        setContainingClass(containingClass);
        return this;
    }

    public InjectLightMethodBuilder withParameter( String name, PsiType type) {
        InjectLightParameter parameter = createParameter(name, type);
        addParameter(parameter);
        return this;
    }
    public InjectLightMethodBuilder withBody(@NotNull PsiCodeBlock codeBlock) {
        this.myBodyCodeBlock = codeBlock;
        return this;
    }
    private InjectLightParameter createParameter(@NotNull String name, @NotNull PsiType type) { return new InjectLightParameter(name, type, this, JavaLanguage.INSTANCE);    }


    @Override
    public LightMethodBuilder setMethodReturnType(PsiType returnType) {
        myReturnTypeAsText = returnType.getPresentableText();
        return super.setMethodReturnType(returnType);
    }
}
