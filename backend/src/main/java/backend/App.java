package main.java.backend;

import io.vertx.core.Vertx;

public class App {
  public static void main(String[] args) {
    System.out.println("App...");
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new TodoApiVerticle());
  }
}