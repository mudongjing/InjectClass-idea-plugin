package xyz.wcd.argument;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.wcd.perpare.InjectClassesNames;
import xyz.wcd.utils.ClassInjectUtil;
import xyz.wcd.utils.CreateMethodOrFieldUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AugmentProviderInject extends PsiAugmentProvider {
    @NotNull
    @Override
    protected <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element, @NotNull Class<Psi> type) {
       return getAugments(element,type,null);
    }

    @NotNull
    @Override
    protected <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element, @NotNull Class<Psi> type, @Nullable String nameHint) {
        final List<Psi> emptyResult = Collections.emptyList();
        if (!(element instanceof PsiClass)) { return emptyResult;
        }else{
            PsiClass targetClass=(PsiClass)element;
            if (type==PsiClass.class || targetClass.isAnnotationType()||targetClass.isInterface()) return emptyResult;
            if (!ClassInjectUtil.hasClassInjectLibrary(element.getProject()))return emptyResult;
            if (type==PsiField.class || type==PsiMethod.class){
                final List<Psi> result = getFieldAndMethod(targetClass,type);
                return result;
            }
            return emptyResult;
        }
    }

    private static <Psi extends PsiElement> List<Psi> getFieldAndMethod(PsiClass psiClass, Class<Psi> type){
        List<Psi> result = new ArrayList<>();
        PsiAnnotation annotation = psiClass.getAnnotation(InjectClassesNames.INJECTCLASS);
        if (annotation!=null){
            PsiAnnotationMemberValue path = annotation.findAttributeValue("path");
            if (path!=null){
                String pathv = path.getText().substring(1, path.getTextLength() - 1);
                if(!((PsiJavaFile)psiClass.getContainingFile()).getPackageName().equals(pathv)){
                    VirtualFile dir = isDirAndTrue(pathv, psiClass.getProject());
                    if (dir!=null){
                        VirtualFile[] children = dir.getChildren();
                        for (VirtualFile child:children) {
                            if (!child.isDirectory()){
                                PsiFile file = PsiManager.getInstance(psiClass.getProject()).findFile(child);
                                PsiClass aClass = PsiTreeUtil.findChildOfAnyType(file, PsiClass.class);
                                if (aClass!=null){
                                    LightFieldBuilder injectField = CreateMethodOrFieldUtil.createInjectField(aClass,psiClass);
                                    if (type==PsiField.class)
                                    result.add((Psi) injectField);
                                    else if (type==PsiMethod.class){
                                        result.add((Psi)CreateMethodOrFieldUtil.createInjectSetterMethod(injectField,psiClass));
                                        result.add((Psi)CreateMethodOrFieldUtil.createInjectGetterMethod(injectField,psiClass));
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    private static VirtualFile isDirAndTrue(String path, Project project){
        String basePath=project.getBasePath()+"/src/main/java/";
        String newPath = basePath + path.replace(".", "/");
        VirtualFile dir = LocalFileSystem.getInstance().findFileByPath(newPath);
        if (dir!=null && dir.isDirectory()) return dir;
        else return null;
    }
}
