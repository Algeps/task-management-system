package ru.algeps.edu.taskmanagementsystem.enums;

import lombok.Getter;

@Getter
public enum TaskSort {
  CREATION_TIMESTAMP("creationTimestamp"),
  UPDATE_TIMESTAMP("updateTimestamp"),
  STATUS("status"),
  PRIORITY("priority");

  final String title;

  TaskSort(String val) {
    this.title = val;
  }
}
