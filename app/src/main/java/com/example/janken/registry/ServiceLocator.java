package com.example.janken.registry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class ServiceLocator {

    private static Map<Class<?>, ClassInstancePair> registry = new HashMap<>();

    /**
     * インターフェースと実装クラスを登録する.
     * <p>
     * 登録時点ではインスタンスは作成しない.
     */
    public static <T, U> void register(Class<T> interfaceClass, Class<U> implementationClass) {
        val classInstancePair = new ClassInstancePair(implementationClass, null);
        registry.put(interfaceClass, classInstancePair);
    }

    /**
     * インターフェースを指定して、実装クラスを取得する.
     * <p>
     * 初めて取得されるクラスの場合、この時点で引数のないコンストラクタによってインスタンスが生成される.
     *
     * @param interfaceClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T resolve(Class<T> interfaceClass) {
        val classInstancePair = registry.get(interfaceClass);

        // インスタンス未作成の場合、作成して登録する
        if (!classInstancePair.existInstance()) {
            val newClassInstancePair = classInstancePair.ofInstanceCreatedByNoArgsConstructor();
            registry.put(interfaceClass, newClassInstancePair);
        }
        return (T) registry.get(interfaceClass).getInstance();
    }
}

@AllArgsConstructor
class ClassInstancePair {
    private Class<?> clazz;
    @Getter
    private Object instance;

    boolean existInstance() {
        return instance != null;
    }

    ClassInstancePair ofInstanceCreatedByNoArgsConstructor() {
        try {
            val constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            val instance = constructor.newInstance();
            return new ClassInstancePair(clazz, instance);
        } catch (NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}