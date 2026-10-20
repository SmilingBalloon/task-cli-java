# Task CLI Application

This is a simple interactive command-line application for managing tasks. You can add, update, delete, mark, and list tasks directly from the terminal.

## Features

- **Add a task:** Add a new task with a description.
- **Update a task:** Update the description of an existing task.
- **Delete a task:** Remove a task by its ID.
- **Mark a task:** Mark a task as `todo`, `in-progress`, or `done`.
- **List tasks:** List all tasks or filter them by status.
- **Persistent storage:** Tasks are stored in [`repo/task.json`](repo/task.json), and the next task ID is stored in [`config/app.properties`](config/app.properties).

## Requirements

- Java Development Kit (JDK) 14 or later.

## Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/SmilingBalloon/task-cli-java.git
   ```

2. **Compile the source code:**

   Run this command from the project root:
   ```ps
      javac -d out `
         config/TaskConfig.java `
         dto/Task.java `
         dto/TaskStatus.java `
         main/util/SimpleJsonParser.java `
         main/util/StringHelper.java `
         main/cmd/HandleUserInput.java `
         main/TaskCLI.java
   ```
   Or one line command:
   ```ps
   javac -d out config/TaskConfig.java dto/Task.java dto/TaskStatus.java main/util/SimpleJsonParser.java main/util/StringHelper.java main/cmd/HandleUserInput.java main/TaskCLI.java
   ```

3. **Run the application:**

   ```bash
   java -cp out main.TaskCLI
   ```

   The application displays a `>` prompt. Type `quit` to save the current tasks and exit.

## Usage

Enter the following commands at the application prompt:

```text
# Add a new task
> add "Buy groceries"
# Output: [HANDLER] Task added successfully (ID: 1)

# Update a task
> update 1 "Buy groceries and cook dinner"
# Output: [HANDLER] Task updated successfully (ID: 1)

# Mark a task as in progress
> mark-in-progress 1
# Output: [HANDLER] Mark task status successfully (ID: 1)

# Mark a task as done
> mark-done 1
# Output: [HANDLER] Mark task status successfully (ID: 1)

# Mark a task as todo
> mark-todo 1
# Output: [HANDLER] Mark task status successfully (ID: 1)

# List all tasks
> list

# List tasks by status
> list todo
> list in-progress
> list done

# Exit and save changes
> quit
```

Descriptions containing spaces must be enclosed in double quotes. Double quotes inside a description are not supported by the command parser.

## Project Structure

```text
config/   Application configuration and task ID persistence
dto/      Task model and task status definitions
main/     Application entry point, command handling, and utility classes
repo/     JSON file used for task persistence
```
