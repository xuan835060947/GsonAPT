package com.xuan.gsonapt.complier;

import com.google.auto.service.AutoService;
import com.xuan.gsonapt.JsonBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by chenxiaoxuan on 17/2/10.
 */

@AutoService(Processor.class)
public class GsonAPTProcessor extends AbstractProcessor {
    private static final String LIST_TYPE = List.class.getCanonicalName();
    private static final String MAP_TYPE = Map.class.getCanonicalName();

    private static Messager MESSAGER;
    public static Elements ELEMENT_UTILS;
    public static Types TYPE_UTILS;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        MESSAGER = processingEnv.getMessager();
        ELEMENT_UTILS = processingEnv.getElementUtils();
        TYPE_UTILS = processingEnv.getTypeUtils();
        print("处理 init");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        print("处理 getSupportedAnnotationTypes");
        Set<String> set = new HashSet<>();
        set.add(JsonBean.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        print("处理");
        processJsonParse(roundEnv);
        return true;
    }

    private void processJsonParse(RoundEnvironment roundEnv) {
        //json 处理
        List<JsonClassProxyInfo> list = new ArrayList<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(JsonBean.class)) {
            TypeElement classElement = (TypeElement) element;
            print(""+classElement.getSimpleName());
            PackageElement packageElement = ELEMENT_UTILS.getPackageOf(classElement);
            String className = classElement.getSimpleName().toString();
            String packageName = packageElement.getQualifiedName().toString();

            list.add(new JsonClassProxyInfo(packageName, className, element));
            print("add");
        }
        JsonParseCreator.create(processingEnv, list);

    }

    public static void print(String message) {
        MESSAGER.printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        MESSAGER.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

}
