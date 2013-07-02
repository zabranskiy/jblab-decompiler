package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * Action for triggering decompilation.
 */
public class DecompileAction extends AnAction {

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        String txt = Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
        Messages.showMessageDialog(project, "Hello, " + txt + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
/*        if (IntelliJadConstants.CLASS_EXTENSION.equals(DataContextUtil.getFileExtension(dataContext))) {
            IntelliJad intelliJad = PluginUtil.getComponent(IntelliJad.class);
            VirtualFile file = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());
            if (file != null) {
                DecompilationDescriptor descriptor = DecompilationDescriptorFactory.getFactoryForFile(file).create(file);
                intelliJad.decompile(new EnvironmentContext(DataKeys.PROJECT.getData(e.getDataContext())),
                        descriptor);
            }
        }*/
    }
}
