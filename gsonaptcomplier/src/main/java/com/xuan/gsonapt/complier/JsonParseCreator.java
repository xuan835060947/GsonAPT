package com.xuan.gsonapt.complier;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;

/**
 * Created by chenxiaoxuan1 on 17/2/5.
 */

public class JsonParseCreator {
    public static void create(ProcessingEnvironment processingEnv, List<JsonClassProxyInfo> list) {
        for (JsonClassProxyInfo jsonClassProxyInfo : list) {
            writeCode(processingEnv, jsonClassProxyInfo);
        }
    }

    private static void writeCode(ProcessingEnvironment processingEnv, JsonClassProxyInfo proxyInfo) {
        try {
            GsonAPTProcessor.print("写文件: " + proxyInfo.getProxyClassFullName());
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                    proxyInfo.getProxyClassFullName(),
                    proxyInfo.getElement());
            Writer writer = jfo.openWriter();
            writer.write(proxyInfo.generateJavaCode());
            writer.flush();
            writer.close();
        } catch (IOException e) {

        }
    }
}
