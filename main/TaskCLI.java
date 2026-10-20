package main;

import java.util.Scanner;

import main.cmd.HandleUserInput;

public class TaskCLI {
    public static void main(String[] args) {
        HandleUserInput handler = new HandleUserInput();
        System.out.println("[TASK-CLI] Type 'quit' to end program");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> handler.handleQuitCommand()));

        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                if (!in.hasNextLine())
                    break;
                String input = in.nextLine();
                if (input.equals("quit"))
                    break;
                handler.handleUserInput(input);
            }
        }
    }
}
