package xyz.wcd.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import org.jetbrains.annotations.NotNull;

public class ClassInjectUtil {
    private static final String CLASS_INJECT_PACKAGE = "io.github.mudongjing.annotation";
    public static boolean hasClassInjectLibrary(@NotNull Project project) {
        ApplicationManager.getApplication().assertReadAccessAllowed();
        return CachedValuesManager.getManager(project).getCachedValue(project, () -> {
            PsiPackage aPackage = JavaPsiFacade.getInstance(project).findPackage(CLASS_INJECT_PACKAGE);
            return new CachedValueProvider.Result<>(aPackage, ProjectRootManager.getInstance(project));
        }) != null;
    }
}
