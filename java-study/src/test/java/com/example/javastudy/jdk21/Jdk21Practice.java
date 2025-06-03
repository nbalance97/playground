package com.example.javastudy.jdk21;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Jdk 21 Practice
 *
 * @Link <a href="https://openjdk.org/projects/jdk/21/">JDK 21</a>
 * @Link <a href="https://www.infoworld.com/article/3689880/jdk-21-the-new-features-in-java-21.html">참고 문서</a>
 */
public class Jdk21Practice {


    /**
     * <p>참고</p>
     *
     * @see java.util.SequencedCollection List now has SequencedCollection as its immediate superinterface, Deque now
     * has SequencedCollection as its immediate superinterface, LinkedHashSet additionally implements SequencedSet,
     * SortedSet now has SequencedSet as its immediate superinterface, LinkedHashMap additionally implements
     * SequencedMap, and SortedMap now has SequencedMap as its immediate superinterface.
     */
    @Test
    void reversed_hashSet() {
        var a = new LinkedHashSet<>(List.of(1, 2, 3));
        var b = a.reversed();

        assertThat(b).containsExactly(3, 2, 1);
    }

    @Test
    void list_new_method() {
        var a = List.of(1, 2, 3);
        a.reversed();
    }

    // ----

    /**
     * record pattern
     *
     * record를 instanceof와 함께 사용하였을때 record를 destructure 할수 있음.
     */
    record Location(int x, int y) {
    }

    @Test
    void record_pattern() {
        var location = new Location(1, 2);

        if (location instanceof Location l) {
            System.out.println("record pattern -> " + (l.x() + l.y()));
        }

        if (location instanceof Location(int x, int y)) {
            System.out.println("new record pattern -> " + (x + y));
        }
    }

    /**
     * Pattern Matching for Switch
     */

    static class Animal {
        String name;

        public Animal(String name) {
            this.name = name;
        }
    }

    static class Dog extends Animal {

        public Dog(String name) {
            super(name);
        }

        public void say() {
            System.out.println("Woof");
        }
    }

    static class Cat extends Animal {

        public Cat(String name) {
            super(name);
        }

        public void say() {
            System.out.println("Meow");
        }
    }

    @Test
    void pattern_matching_for_switch() {
        var dog = new Dog("dog");
        var animal = (Animal) dog;

        switch (animal) {
            case null -> throw new NullPointerException();
            case Dog d -> d.say();
            case Cat c -> c.say();
            default -> System.out.println(animal.name + " is an unknown animal");
        }
    }

    @Test
    void string_match() {
        var s = "hello world!";

        switch (s) {
            case null -> System.out.println("null");
            case String st when st.equals("blaaaa") -> System.out.println("blaaaa");
            case String st when st.equals("hello world!") -> System.out.println("hello world!");
            default -> System.out.println(s);
        }
    }

    // -------

    /**
     * JEP 430 : String Literal
     * java 23에서 제거됨.
     *
     * @see <a href="https://bugs.openjdk.org/browse/JDK-8329949">issue</a>
     * - 문제점이 무엇이었을까?
     * 1. as-is에서 $을 이미 사용하고 있었던 경우, 모두 이스케이프 처리해주어야 함. (특히 EL에서)
     */
    @Test
    void string_literal_test() {
        //var name = "KIM";
        //var s = STR."nice to meet you, \{name}";

        //System.out.println(s);
    }

    /**
     * record는 final class / 각 필드들은 private final
     *
     */
    @Test
    void record() {
        var p = new Person("kim", 10);
        var age = p.age;
        var getterAge = p.age();

        // p.age vs p.age()
        // 컴파일러에 의해 p.age는 GETFIELD 바이트코드로 변경되고, p.age()는 INVOKEVIRTUAL 바이트코드로 변경됨
        // getter 오버라이드 시 p.age에 적용되지는 않음. p.age()에만 적용됨
        // p.age는 필드에 대한 direct access인거 같음..?
        System.out.println(age + " " + getterAge);
    }

    /**
     * https://docs.oracle.com/en/java/javase/15/language/records.html
     * nested record class -> record class는 항상 static이다
     */
    record Person(String name, int age) {

        @Override
        public int age() {
            return 500 + age;
        }
    }
}
