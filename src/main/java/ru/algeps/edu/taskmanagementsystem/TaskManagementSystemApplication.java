package ru.algeps.edu.taskmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * todo
 *  1) следующим коммитом сделать swagger и дополнительные методы
 *  2) сделать JWT токен авторизации
 */
@SpringBootApplication
public class TaskManagementSystemApplication {
  public static void main(String[] args) {
    SpringApplication.run(TaskManagementSystemApplication.class, args);
  }
}
