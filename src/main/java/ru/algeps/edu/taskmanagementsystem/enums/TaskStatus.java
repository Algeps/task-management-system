package ru.algeps.edu.taskmanagementsystem.enums;

public enum TaskStatus {
  IN_WAIT("В ожидании"),
  PROGRESS("В процессе"),
  COMPLETE("Завершено");

  final String title;

  TaskStatus(String val) {
    this.title = val;
  }

  public String getTitle() {
    return title;
  }
}
