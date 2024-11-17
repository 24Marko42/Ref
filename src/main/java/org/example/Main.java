package org.example;

import java.lang.annotation.*;
import java.lang.reflect.*;

// Аннотация с целочисленным параметром
// Аннотация @Retention(RetentionPolicy.RUNTIME) указывает, что аннотация доступна во время выполнения через рефлексию.
// Аннотация @Target(ElementType.METHOD) ограничивает область применения аннотации только методами.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface CallCount {
    int value(); // Целочисленное поле для указания количества вызовов метода
}

// Класс с методами различных модификаторов доступа
class MyClass {
    // Защищенный метод с аннотацией @CallCount, указывающей, что метод должен быть вызван 3 раза
    @CallCount(3)
    protected void protectedMethod(String message) {
        System.out.println("Protected method called with message: " + message);
    }

    // Приватный метод с аннотацией @CallCount, указывающей, что метод должен быть вызван 2 раза
    @CallCount(2)
    private void privateMethod(int number) {
        System.out.println("Private method called with number: " + number);
    }

    // Публичный метод без аннотации
    public void publicMethod() {
        System.out.println("Public method called");
    }

    // Еще один защищенный метод без аннотации
    protected void anotherProtectedMethod() {
        System.out.println("Another protected method called");
    }

    // Еще один приватный метод без аннотации
    private void anotherPrivateMethod() {
        System.out.println("Another private method called");
    }
}

// Класс для вызова аннотированных методов через рефлексию
public class Main {
    public static void main(String[] args) {
        // Создаем экземпляр класса MyClass
        MyClass myClass = new MyClass();

        // Получаем класс объекта для анализа через рефлексию
        Class<?> clazz = myClass.getClass();

        // Получаем список всех методов класса, включая приватные
        Method[] methods = clazz.getDeclaredMethods();

        // Проходим по каждому методу
        for (Method method : methods) {
            // Проверяем, аннотирован ли метод аннотацией @CallCount
            if (method.isAnnotationPresent(CallCount.class)) {
                // Извлекаем аннотацию и ее параметр value (количество вызовов)
                CallCount annotation = method.getAnnotation(CallCount.class);
                int count = annotation.value();

                // Делаем метод доступным для вызова, если он имеет модификатор private
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }

                // Вызываем метод заданное количество раз
                for (int i = 0; i < count; i++) {
                    try {
                        // Определяем параметры метода и передаем значения
                        if (method.getParameterCount() == 1) { // Если у метода 1 параметр
                            Class<?> paramType = method.getParameterTypes()[0];
                            if (paramType == String.class) { // Если параметр типа String
                                method.invoke(myClass, "Hello Reflection!");
                            } else if (paramType == int.class) { // Если параметр типа int
                                method.invoke(myClass, 42);
                            }
                        } else {
                            // Если метод не требует параметров
                            method.invoke(myClass);
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // Обработка исключений при вызове метода
                    }
                }
            }
        }
    }
}
