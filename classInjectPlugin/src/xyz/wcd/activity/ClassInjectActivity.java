package xyz.wcd.activity;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import xyz.wcd.listenser.PsiFileListeners;

public class ClassInjectActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        System.out.println("project activity11"+project.getName());
        PsiManager.getInstance(project).addPsiTreeChangeListener(new PsiFileListeners());

    }
}
