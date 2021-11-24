package xyz.wcd.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.light.LightFieldBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class InjectLightFieldBuilder extends LightFieldBuilder {
    private String name;
    public InjectLightFieldBuilder(@NotNull String name, PsiType type, PsiManager manager) {
        super(manager,name, type);
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
