import javax.swing.*;  // Import Swing components
import java.awt.event.*; // Import event handling
// Node class
class TaskNode {
    String taskName;
    int priority;
    TaskNode next, prev;

    TaskNode(String name, int priority) {
        this.taskName = name;
        this.priority = priority;
    }
}

// Abstract class
abstract class AbstractTaskList {
    abstract void addTask(String name, int priority);
    abstract String showTasks();
    abstract String showTasksBackward();
    abstract void deleteTask(String name);
}

// Doubly Linked List Implementation
class DoublyTaskList extends AbstractTaskList {
    private TaskNode head;

    void addTask(String name, int priority) {
        TaskNode newNode = new TaskNode(name, priority);
        if (head == null) {
            head = newNode;
        } else {
            TaskNode temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
            newNode.prev = temp;
        }
    }

    String showTasks() {
        if (head == null) return "No tasks to display!";
        StringBuilder sb = new StringBuilder();
        TaskNode temp = head;
        while (temp != null) {
            sb.append("[Task:\"").append(temp.taskName).append("\", Priority:").append(temp.priority).append("]");
            if (temp.next != null) sb.append(" <-> ");
            temp = temp.next;
        }
        sb.append(" -> null");
        return sb.toString();
    }

    String showTasksBackward() {
        if (head == null) return "No tasks to display!";
        StringBuilder sb = new StringBuilder();
        TaskNode temp = head;
        while (temp.next != null) temp = temp.next; // move to last
        while (temp != null) {
            sb.append("[Task:\"").append(temp.taskName).append("\", Priority:").append(temp.priority).append("]");
            if (temp.prev != null) sb.append(" <-> ");
            temp = temp.prev;
        }
        sb.append(" -> null");
        return sb.toString();
    }

    void deleteTask(String name) {
        if (head == null) return;

        TaskNode temp = head;
        while (temp != null && !temp.taskName.equalsIgnoreCase(name)) {
            temp = temp.next;
        }
        if (temp == null) return;

        if (temp.prev != null) temp.prev.next = temp.next;
        else head = temp.next; // deleting first node

        if (temp.next != null) temp.next.prev = temp.prev;
    }
}

// GUI Application
public class TaskManagerGUI extends Application {
    private DoublyTaskList taskList = new DoublyTaskList();
    private TextArea outputArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        // Input fields
        TextField taskField = new TextField();
        taskField.setPromptText("Enter Task Name");

        TextField priorityField = new TextField();
        priorityField.setPromptText("Enter Priority");

        // Buttons
        Button addBtn = new Button("Add Task");
        Button showFwdBtn = new Button("Show Forward");
        Button showBwdBtn = new Button("Show Backward");
        Button deleteBtn = new Button("Delete Task");

        // Actions
        addBtn.setOnAction(e -> {
            String name = taskField.getText().trim();
            String priorityText = priorityField.getText().trim();
            if (!name.isEmpty() && !priorityText.isEmpty()) {
                try {
                    int priority = Integer.parseInt(priorityText);
                    taskList.addTask(name, priority);
                    outputArea.setText("Task added: " + name + " (Priority: " + priority + ")");
                    taskField.clear();
                    priorityField.clear();
                } catch (NumberFormatException ex) {
                    outputArea.setText("Invalid priority! Please enter a number.");
                }
            } else {
                outputArea.setText("Please enter both Task Name and Priority.");
            }
        });

        showFwdBtn.setOnAction(e -> outputArea.setText(taskList.showTasks()));
        showBwdBtn.setOnAction(e -> outputArea.setText(taskList.showTasksBackward()));

        deleteBtn.setOnAction(e -> {
            String name = taskField.getText().trim();
            if (!name.isEmpty()) {
                taskList.deleteTask(name);
                outputArea.setText("Task \"" + name + "\" deleted (if it existed).");
                taskField.clear();
            } else {
                outputArea.setText("Enter Task Name to delete.");
            }
        });

        // Layout
        HBox inputBox = new HBox(10, taskField, priorityField, addBtn, deleteBtn);
        VBox root = new VBox(10, inputBox, showFwdBtn, showBwdBtn, outputArea);
        root.setPadding(new Insets(15));

        outputArea.setEditable(false);
        outputArea.setPrefHeight(250);

        // Scene
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("JavaFX Task Manager (DLL)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
