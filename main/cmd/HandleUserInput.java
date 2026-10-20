package main.cmd;

import java.util.Map;
import java.util.List;
import java.time.Instant;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import dto.Task;
import dto.TaskStatus;
import config.TaskConfig;
import main.util.StringHelper;
import main.util.SimpleJsonParser;

public class HandleUserInput {
    TaskConfig config = new TaskConfig();
    Map<Integer, Task> taskList = new HashMap<>();
    private final String jsonPath = "repo/task.json";

    public HandleUserInput() {
        config.loadProperties();
        loadTask();
    }

    public void handleQuitCommand() {
        config.saveProperties();
        saveTask();
    }

    void loadTask() {
        try (FileInputStream in = new FileInputStream(jsonPath)) {
            config.ensureFileExists(jsonPath);
            byte[] jsonBytes = in.readAllBytes();
            String jsonString = new String(jsonBytes);
            if (jsonString.isBlank() || jsonString.isEmpty())
                return;
            List<Map<String, String>> objectList = SimpleJsonParser.parseJsonArray(jsonString);
            for (int i = 0; i < objectList.size(); i++) {
                int id = Integer.parseInt(objectList.get(i).get("id"));
                String description = objectList.get(i).get("description");
                TaskStatus status = TaskStatus.fromJson(objectList.get(i).get("status"));
                Instant createdAt = Instant.parse(objectList.get(i).get("createdAt"));
                Instant updatedAt = Instant.parse(objectList.get(i).get("updatedAt"));
                taskList.put(Integer.valueOf(id), new Task(id, description, status, createdAt, updatedAt));
            }
        } catch (FileNotFoundException exception) {
            System.err.println("[HANDLER] File not found: " + exception.getMessage());
        } catch (IOException exception) {
            System.err.println("[HANDLER] Error while reading file: " + exception.getMessage());
        } catch (NumberFormatException exception) {
            System.err.println("[HANDLER] Failed to parse id: " + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
        }
    }

    void saveTask() {
        StringBuilder builder = new StringBuilder();
        builder.append("[\n");
        int i = 0;
        int size = taskList.size();
        for (Task task : taskList.values()) {
            builder.append("{");
            builder.append("\"id\":\"" + task.getId() + "\",");
            builder.append("\"description\":\"" + task.getDescription() + "\",");
            builder.append("\"status\":\"" + task.getStatus().getValue() + "\",");
            builder.append("\"createdAt\":\"" + task.getCreatedAt().toString() + "\",");
            builder.append("\"updatedAt\":\"" + task.getUpdatedAt().toString() + "\"");
            builder.append("}");
            if (++i < size)
                builder.append(",\n");
        }
        builder.append("\n]");
        try (BufferedWriter out = new BufferedWriter(new FileWriter(jsonPath))) {
            config.ensureFileExists(jsonPath);
            out.write(builder.toString());
        } catch (IOException exception) {
            System.err.println("[JSON] Error while writing file: " + exception.getMessage());
        }
    }

    public void handleUserInput(String input) {
        try {
            if (input.isBlank() || input.isEmpty())
                return;
            String[] parts = StringHelper.parseString(input.trim());
            switch (parts[0]) {
                case "add" -> {
                    if (parts.length != 2)
                        throw new IllegalArgumentException("Usage: add \"description\" (Unsupported quote in description)");
                    handleAddCommand(parts[1]);
                }
                case "update" -> {
                    if (parts.length != 3)
                        throw new IllegalArgumentException("Usage: update id \"description\" (Unsupported quote in description)");
                    int id = Integer.parseInt(parts[1]);
                    handleUpdateCommand(id, parts[2]);
                }
                case "delete" -> {
                    if (parts.length != 2)
                        throw new IllegalArgumentException("Usage: delete id");
                    int id = Integer.parseInt(parts[1]);
                    handleDeleteCommand(id);
                }
                case "mark" -> {
                    if (parts.length != 3)
                        throw new IllegalArgumentException("Usage: mark-status id");
                    TaskStatus status = TaskStatus.fromJson(parts[1]);
                    int id = Integer.parseInt(parts[2]);
                    handleMarkCommand(id, status);
                }
                case "list" -> {
                    if (parts.length > 2)
                        throw new IllegalArgumentException("Usage: list | list status");
                    handleListCommand(parts);
                }
                default -> System.out.println("[HANDLER] Unknow command: " + parts[0]);
            }
        } catch (NumberFormatException exception) {
            System.err.println("[HANDLER] Invalid id");
        } catch (IllegalArgumentException exception) {
            System.err.println("[HANDLER] " + exception.getMessage());
        }
    }

    void handleAddCommand(String description) {
        int id = config.getCurrentId();
        taskList.put(Integer.valueOf(id), new Task(id, description));
        System.out.println("[HANDLER] Task added successfully (ID: " + id + ")");
    }

    void handleUpdateCommand(int id, String description) {
        Integer taskId = Integer.valueOf(id);
        if (!taskList.containsKey(taskId)) {
            System.err.println("[HANDLER] Task not exist");
            return;
        }
        Task task = taskList.get(taskId);
        task.setDescription(description);
        task.setUpdatedAt(Instant.now());
        System.out.println("[HANDLER] Task updated successfully (ID: " + id + ")");
    }

    void handleDeleteCommand(int id) {
        Integer taskId = Integer.valueOf(id);
        if (!taskList.containsKey(taskId)) {
            System.err.println("[HANDLER] Task not exist");
            return;
        }
        taskList.remove(taskId);
        System.out.println("[HANDLER] Task " + id + "removed");
    }

    void handleMarkCommand(int id, TaskStatus status) {
        Integer taskId = Integer.valueOf(id);
        if (!taskList.containsKey(taskId)) {
            System.err.println("[HANDLER] Task not exist");
            return;
        }
        taskList.get(taskId).setStatus(status);
        System.out.println("[HANDLER] Mark task status successfully (ID: " + id + ")");
    }

    void handleListCommand(String[] parts) {  
        if (parts.length < 2) {
            for (Task task : taskList.values())
                printTaskInfo(task);
            return;
        }
        TaskStatus status = TaskStatus.fromJson(parts[1]);
        for (Task task : taskList.values())
            if (task.getStatus().equals(status))
                printTaskInfo(task);
    }
    
    void printTaskInfo(Task task) {
        System.out.println("    Id:          " + task.getId());
        System.out.println("    Description: " + task.getDescription());
        System.out.println("    Status:      " + task.getStatus());
        System.out.println("    Created At:  " + task.getCreatedAt());
        System.out.println("    Updated At:  " + task.getUpdatedAt());
        System.out.println();
    }
}
