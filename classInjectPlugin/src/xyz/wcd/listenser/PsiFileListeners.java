package xyz.wcd.listenser;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.impl.file.PsiFileImplUtil;
import com.intellij.psi.util.PsiClassUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import xyz.wcd.perpare.InjectClassesNames;
import xyz.wcd.utils.CreateMethodOrFieldUtil;

import java.util.*;

public class PsiFileListeners implements PsiTreeChangeListener {
    Set<PsiFile> fileSet=new HashSet<>();
    Map<PsiFile,String> fileToPath=new HashMap();
    Map<PsiFile,List<PsiElement>> fileToElements=new HashMap<>();
    String injectClass="InjectClass";
    @Override
    public void beforeChildAddition(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) { }
    @Override
    public void beforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) { }
    @Override
    public void beforeChildReplacement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) { }
    @Override
    public void beforeChildMovement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) { }
    @Override
    public void beforeChildrenChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) { }
    @Override
    public void beforePropertyChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) { }
    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {}
    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {}

    @Override
    public void childReplaced(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        System.out.println("childReplace------------------------");
        PsiFile file = psiTreeChangeEvent.getFile();

        PsiClass targetClass = PsiTreeUtil.findChildOfAnyType(file.getOriginalElement(), PsiClass.class);
        System.out.println("targetClass "+targetClass.getQualifiedName());
        if (fileSet.contains(file)){
            System.out.println("包含");
            PsiElement oldChild = psiTreeChangeEvent.getOldChild();
            // 是否是该注解被删除
            if (oldChild.getText().equals(injectClass) && targetClass.getAnnotation(InjectClassesNames.INJECTCLASS)==null){
                deleteElement(file);
                fileSet.remove(file);
            }else{
                PsiAnnotation annotation = targetClass.getAnnotation(InjectClassesNames.INJECTCLASS);
                PsiAnnotationMemberValue path = annotation.findAttributeValue("path");
                if (path==null){
                    System.out.println("path 为null");
                    deleteElement(file);
                }else{
                    String pathText = path.getText().substring(1,path.getTextLength()-1);
                    System.out.println("pathTest "+pathText);
                    if (!pathText.equals(fileToPath.get(file))){
                        deleteElement(file);
                        Project project = file.getProject();
                        System.out.println("文件包名 "+((PsiJavaFile) file).getPackageName());
                        if(!((PsiJavaFile) file).getPackageName().equals(pathText)) {
                            System.out.println("添加 元素");
                            addElement(file,targetClass,project,pathText);
                        }

                    }
                }
            }
        }else{
            System.out.println("不包含");
            PsiElement newChild = psiTreeChangeEvent.getNewChild();
            System.out.println("newChild "+newChild.getText());
            if (newChild.getText().equals(injectClass) ||targetClass.getAnnotation(InjectClassesNames.INJECTCLASS)!=null){
                PsiAnnotation annotation = targetClass.getAnnotation(InjectClassesNames.INJECTCLASS);
                if (annotation!=null){
                    joinInjectAnno(file,null,null);
                }
            }
        }
    }

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {}
    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) { }
    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) { }

    private void addElement(PsiFile file,PsiClass targetClass,Project project,String path){
        System.out.println("目录 "+file.getParent().getName());
        System.out.println("项目目录 "+project.getProjectFile().getPath());
        System.out.println("javaFile包名"+((PsiJavaFile) file).getPackageName());
        PsiPackage aPackage = JavaPsiFacade.getInstance(project).findPackage(((PsiJavaFile) file).getPackageName());
        String basePath=project.getBasePath()+"/src/main/java/";
        System.out.println("工程路径 "+basePath);
        String newPath = basePath + path.replace(".", "/");
        System.out.println("new Path "+newPath);
        VirtualFile dir = LocalFileSystem.getInstance().findFileByPath(newPath);
        if (dir!=null)  System.out.println("dir 路径名 "+dir.getPath());
        if (dir!=null){
            System.out.println("package 不 null");
            if(dir.isDirectory()){
                VirtualFile[] children = dir.getChildren();
                List<PsiElement> elements=new ArrayList<>();
                for (VirtualFile child:children){
                    if (!child.isDirectory()){
                        PsiFile file1 = PsiManager.getInstance(project).findFile(child);
                        System.out.println("file1 名字 "+file1.getName());
                        PsiClass aClass = PsiTreeUtil.findChildOfAnyType(file1, PsiClass.class);
                        file.get
                        System.out.println("import 类 "+CreateMethodOrFieldUtil.createImport(aClass).getQualifiedName());
                        WriteCommandAction.runWriteCommandAction(project, () -> {
                            elements.add(file.add(CreateMethodOrFieldUtil.createImport(aClass)));
                        });

                        System.out.println("field添加 "+CreateMethodOrFieldUtil.createInjectField(aClass).getName());
                        PsiElement fi = targetClass.add(CreateMethodOrFieldUtil.createInjectField(aClass));
                        System.out.println("fi 名字 "+fi.getText());
                        elements.add(fi);
                        PsiElement setter = targetClass.add(CreateMethodOrFieldUtil.createInjectSetterMethod((PsiField) fi, targetClass));
                        System.out.println("setter方法 "+setter.getText());
                        elements.add(setter);
                        PsiElement getter = targetClass.add(CreateMethodOrFieldUtil.createInjectGetterMethod((PsiField) fi, targetClass));
                        System.out.println("getter方法 "+getter.getText());
                        elements.add(getter);
                    }
                    fileToElements.put(file,elements);
                }
            }
        }else{ System.out.println("null pack");        }
    }

    private void deleteElement(PsiFile file){
        if (fileToPath.containsKey(file)) fileToPath.remove(file);
        if (fileToElements.containsKey(file)){
            List<PsiElement> elements = fileToElements.get(file);
            if (elements!=null){ for (PsiElement e:elements) { e.delete(); }   }
        }
    }
    private void joinInjectAnno(PsiFile file,String path,List<PsiElement> elements){
        fileSet.add(file);
        fileToPath.put(file,path);
        fileToElements.put(file,elements);
    }
}
