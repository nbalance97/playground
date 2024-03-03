package com.example.javastudy.jdk21;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Jdk 21 Practice
 * @Link <a href="https://openjdk.org/projects/jdk/21/">JDK 21</a>
 * @Link <a href="https://www.infoworld.com/article/3689880/jdk-21-the-new-features-in-java-21.html">참고 문서</a>
 */
public class Jdk21Practice {


  /**
   * <p>참고</p>
   * @see java.util.SequencedCollection
   * List now has SequencedCollection as its immediate superinterface,
   * Deque now has SequencedCollection as its immediate superinterface,
   * LinkedHashSet additionally implements SequencedSet,
   * SortedSet now has SequencedSet as its immediate superinterface,
   * LinkedHashMap additionally implements SequencedMap, and
   * SortedMap now has SequencedMap as its immediate superinterface.
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
}
