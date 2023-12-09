package ru.algeps.edu.taskmanagementsystem.enums;

public enum TaskPriority {
  HIGH("Высокий"),
  MEDIUM("Средний"),
  LOW("Низкий");
  final String title;

  TaskPriority(String val) {
    this.title = val;
  }

  public String getTitle() {
    return title;
  }
}
