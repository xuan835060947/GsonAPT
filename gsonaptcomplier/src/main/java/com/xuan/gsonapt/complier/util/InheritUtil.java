package com.xuan.gsonapt.complier.util;

import com.xuan.gsonapt.complier.GsonAPTProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.xuan.gsonapt.complier.GsonAPTProcessor.TYPE_UTILS;

/**
 * Created by chenxiaoxuan1 on 17/2/15.
 */

public class InheritUtil {
    private static final InheritTree INHERIT_TREE = new InheritTree();

    public static void init(List<TypeElement> types) {
        for (TypeElement typeElement : types) {
            INHERIT_TREE.add(typeElement);
        }
    }

    public static List<TypeMirror> getAllChildrenBySort(TypeMirror typeMirror) {
        Node node = INHERIT_TREE.getNode(typeMirror);
        TreeSet<Node> treeSet = new TreeSet<>();
        getAllChildren(node, treeSet);
        List<TypeMirror> list = new ArrayList<>(treeSet.size());
        for(Node item : treeSet){
            list.add(item.nodeType);
        }
        return list;
    }


    public static void getAllChildren(Node node, Set<Node> set) {
        for (Node item : node.children) {
            set.add(item);
            getAllChildren(item, set);
        }
    }
}

class InheritTree {
    Map<String, Node> nodeMap = new HashMap<>();
    Node rootObject;

    public InheritTree() {
        init();
    }

    private void init() {
        rootObject = new Node();
        rootObject.nodeType = GsonAPTProcessor.ELEMENT_UTILS.getTypeElement("java.lang.Object").asType();
        rootObject.height = 0;
        nodeMap.put(rootObject.nodeType.toString(), rootObject);
    }

    public void add(TypeElement typeElement) {
        Stack<TypeElement> stack = new Stack<>();
        Element superElemets = null;
        do {
            if (superElemets != null) {
                typeElement = (TypeElement) superElemets;
            }
            stack.push(typeElement);
            superElemets = TYPE_UTILS.asElement(typeElement.getSuperclass());
        }
        while (superElemets != null);

        TypeElement parent = stack.pop();
        while (!stack.isEmpty()) {
            TypeElement child = stack.pop();
            Node parentNode = nodeMap.get(parent.toString());
            addNode(parentNode, child.asType());
            parent = child;
        }

    }

    public Node getNode(TypeMirror child) {
        return nodeMap.get(child.toString());
    }

    private Node addNode(Node parent, TypeMirror child) {
        Node node = nodeMap.get(child.toString());
        if (node == null) {
            node = new Node();
        }
        node.nodeType = child;
        node.height = parent.height + 1;
        parent.children.add(node);
        nodeMap.put(child.toString(), node);
        return node;
    }
}

class Node implements Comparable<Node> {
    TypeMirror nodeType;
    int height;
    Set<Node> children = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {//just compare with nodeType
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (nodeType != null) {
            if (node == null) {
                return false;
            }
            if (!nodeType.toString().equals(node.nodeType.toString())) {
                return false;
            }
        } else {
            if (node != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = nodeType != null ? nodeType.hashCode() : 0;
        result = 31 * result + height;
        return result;
    }

    @Override
    public int compareTo(Node o) {
        if (o == null) {
            return 1;
        }
        if (height == o.height) {
            return 0;
        }
        return height > o.height ? 1 : 0;
    }
}