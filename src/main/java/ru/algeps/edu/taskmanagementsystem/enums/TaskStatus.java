package ru.algeps.edu.taskmanagementsystem.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {
  IN_WAIT("В ожидании"),
  PROGRESS("В процессе"),
  COMPLETE("Завершено");

  final String title;

  TaskStatus(String val) {
    this.title = val;
  }

}
