package dto;

public enum TaskStatus {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done");
    
    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TaskStatus fromJson(String jsonValue) {
        return switch (jsonValue) {
            case "todo" -> TODO;
            case "in-progress" -> IN_PROGRESS;
            case "done" -> DONE;
            default -> throw new IllegalArgumentException("[HANDLER] Invalid task status");
        };
    }
}
