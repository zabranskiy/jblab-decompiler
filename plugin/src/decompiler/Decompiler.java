package decompiler;

import com.decompiler.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import config.PluginConfigComponent;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class Decompiler {

    @Nullable
    public static VirtualFile decompile(final PluginConfigComponent config, final VirtualFile virtualFile) {
        //Number of bytes CAFEBABE
        final int CAFEBABE = 4;

        LightVirtualFile decompiledFile = null;

        try {
            InputStream is = virtualFile.getInputStream();
            byte[] bytes = new byte[CAFEBABE];
            is.mark(CAFEBABE);
            is.read(bytes);
            final int magic = ByteBuffer.wrap(bytes).getInt();
            if (magic == 0xCAFEBABE) {
                is.reset();
                final Language lang = config.getChosenLanguage();

                final String initialClassFilePath = virtualFile.getPath();
                final int jarIndex = initialClassFilePath.indexOf(".jar!/");
                final String jarPath = jarIndex == -1 ? "" : initialClassFilePath.substring(0, jarIndex + 4);

                decompiledFile = new LightVirtualFile(virtualFile.getNameWithoutExtension() + lang.getExtension(),
                        getDecompiledCode(lang.getName(), is, jarPath, config.getTextWidth(), config.getTabSize()));
            }
            is.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return decompiledFile;
    }

    private static String getDecompiledCode(final String languageName, final InputStream is, final String classFilesJarPath, final Integer textWidth, final Integer tabSize) throws IOException {
        return com.decompiler.Decompiler.getDecompiledCode(languageName, new ClassReader(is), classFilesJarPath, textWidth, tabSize);
    }
}
