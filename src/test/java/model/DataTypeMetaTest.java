package model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DataTypeMetaTest {

  @Test
  void 정상적인_값이면_예외가_발생하지_않는다() {
    assertDoesNotThrow(() -> new DataTypeMeta("SampleMT", "SampleDT", "urn:sample", "D:/target"));
  }

  @Test
  void DT명에_상대경로_표기가_있으면_예외가_발생한다() {
    assertThrows(IllegalArgumentException.class,
        () -> new DataTypeMeta("", "../etc/SampleDT", "urn:sample", "D:/target"));
  }

  @Test
  void DT명에_파일명으로_사용할_수_없는_문자가_있으면_예외가_발생한다() {
    assertThrows(IllegalArgumentException.class,
        () -> new DataTypeMeta("", "Sample:DT", "urn:sample", "D:/target"));
  }

  @Test
  void MT명에_파일명으로_사용할_수_없는_문자가_있으면_예외가_발생한다() {
    assertThrows(IllegalArgumentException.class,
        () -> new DataTypeMeta("Sample*MT", "SampleDT", "urn:sample", "D:/target"));
  }

  @Test
  void MT명이_비어있으면_검증을_건너뛴다() {
    assertDoesNotThrow(() -> new DataTypeMeta("", "SampleDT", "urn:sample", "D:/target"));
  }
}
