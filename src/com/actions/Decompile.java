package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.sdc.DecompilerService;
import sun.misc.IOUtils;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Decompile extends AnAction {

/*    private String getFileExtension(final DataContext dataContext) {
        final VirtualFile file = DataKeys.VIRTUAL_FILE.getData(dataContext);
        return file == null ? null : file.getExtension();
    }*/

/*    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        final String extension = getFileExtension(e.getDataContext());
        this.getTemplatePresentation().setEnabled(extension != null && "class".equals(extension));
    }*/

    public void actionPerformed(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        Project project = DataKeys.PROJECT.getData(dataContext);
        VirtualFile virtualFile = DataKeys.VIRTUAL_FILE.getData(dataContext);
        assert virtualFile != null;
        VirtualFile classRootForFile = ProjectRootManager.getInstance(project).getFileIndex().getClassRootForFile(virtualFile);

        String path = virtualFile.getPresentableName();


        String filePath = virtualFile.getPath();
        String basePath = classRootForFile.getPresentableUrl();
        String internalClassName = filePath.substring(filePath.indexOf('!') + 2, filePath.length());


        try {
//            InputStream in = virtualFile.getInputStream();
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(internalClassName);
            DataInputStream data = new DataInputStream(in);
            int magic = data.readInt();
            if (magic == 0xCAFEBABE) {
                DecompilerService decompilerService = new DecompilerService();
                decompilerService.decompile(in);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }


//        URL url = new URL(classRootForFile.getPresentableUrl());
//        InputStream is = url.openStream();
//        decompilerService.decompile(is);

    }
}

