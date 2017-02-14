package com.xuan.gsonapt.complier;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

/**
 * Created by chenxiaoxuan1 on 17/2/5.
 */

public class JsonParseCreator {
    private static boolean hasCreateGsonAPT;

    public static void create(ProcessingEnvironment processingEnv, List<JsonClassProxyInfo> list) {
        for (JsonClassProxyInfo jsonClassProxyInfo : list) {
            writeCode(processingEnv, jsonClassProxyInfo.getProxyClassFullName(), jsonClassProxyInfo.generateJavaCode(), jsonClassProxyInfo.getElement());
        }
        if (!hasCreateGsonAPT) {
            hasCreateGsonAPT = true;
            GsonAptCreator gsonAptCreator = new GsonAptCreator();
            writeCode(processingEnv, gsonAptCreator.getFullName(), gsonAptCreator.createCode(list), null);
        }
    }

    private static void writeCode(ProcessingEnvironment processingEnv, String name, String code, Element element) {
        try {
            GsonAPTProcessor.print("写文件: " + name);
            JavaFileObject jfo;
            if (element == null) {
                jfo = processingEnv.getFiler().createSourceFile(
                        name);
            } else {
                jfo = processingEnv.getFiler().createSourceFile(
                        name,
                        element);
            }
            Writer writer = jfo.openWriter();
            writer.write(code);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
