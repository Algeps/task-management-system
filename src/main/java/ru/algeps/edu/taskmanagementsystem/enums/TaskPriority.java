package ru.algeps.edu.taskmanagementsystem.enums;

import lombok.Getter;

@Getter
public enum TaskPriority {
  HIGH("Высокий"),
  MEDIUM("Средний"),
  LOW("Низкий");
  final String title;

  TaskPriority(String val) {
    this.title = val;
  }
}
