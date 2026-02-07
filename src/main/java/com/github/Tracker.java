package com.github;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import com.github.types.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import io.github.cdimascio.dotenv.Dotenv;

public class Tracker {
    static List<Task> taskList;
    static String folderPath;
    static String filePath;
    static HashSet<String> options;

    private static void initialize() {
        
        prepareFile();

        options = new HashSet<>();

        for (Status s: Status.values()) {
            options.add(String.valueOf(s));
        }

        readJSON();
    }

    private static void prepareFile() {
        Dotenv dotenv = Dotenv.load();

        folderPath = dotenv.get("FOLDER_PATH");

        if (folderPath == null) {
            System.out.println("FOLDER_PATH is not set! Please set and restart.");
            System.exit(1);
        }

        filePath = folderPath + File.separator + "data.json";
        
        try {
            File dataFile = new File(folderPath);

            dataFile.mkdirs();

            dataFile = new File(filePath);

            if (!dataFile.exists()) {
                if (!dataFile.createNewFile()) {
                    System.err.println("File already exists or could not be created in path: " + filePath);
                }
            }

        } catch (IOException e) {
            System.err.println("Error preparing file: " + e);

            System.exit(1);
        }
    }

    private static void readJSON() {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

        JsonReader reader = null;

        try {
            reader = new JsonReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }

        Type listType = new TypeToken<List<Task>>() {}.getType();

        taskList =  gson.fromJson(reader, listType);

        if (taskList == null) {
            taskList = new ArrayList<>();
        }
    }

    private static void writeJSON() {
         Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()  // optional, for nicer JSON output
            .create();

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(taskList, writer);
        } catch (Exception e) {
            System.err.println(e);
        }        
    }

    private static void add(String desc) {
        taskList.add(new Task(desc));

        System.out.println("TaskList size: " + taskList.size());
    }

    private static void delete(int id) {
        taskList.remove(id-1);
    }

    private static void update(int id, String desc) {
        Task t = taskList.get(id-1);
        t.updateDescription(desc);
    }

    private static void listHelper(String listType) {
        if (!options.contains(listType.toUpperCase())) {
            System.out.println("Invalid option.");
            return;
        }

        try {
            int count = 0;

            for (int i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).status == Status.valueOf(listType.toUpperCase())) {
                    System.out.println((i+1) + " | " + taskList.get(i).toString());
                    count++;
                }
            }

            if (count == 0) {
                System.out.println("No tasks with that Status: " + listType);
            }
        } catch (Exception e) {
            System.out.println("Invalid status.");
        }
        
    }

    private static void listAll() {
        if (taskList.isEmpty()) {
            System.out.println("Task List is empty, please add a task.");
            return;
        }

        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i+1) + " | " + taskList.get(i).toString());
        }
    }

    private static void markInProgress(int id) {
        taskList.get(id-1).setInProgress();
    }

    private static void markDone(int id) {
        taskList.get(id-1).setDone();
    }

    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Writing to memory.");
            writeJSON();

            input.close();
        }));

        initialize();
        
        try {

            System.out.println("Welcome to Task Tracker");
            
            while (true) {
                System.out.println("Please enter a command");


                if (input.hasNextLine()) {
                    String option = input.nextLine();

                    String[] values = option.split(" ");

                    switch (values[0].toLowerCase()) {
                        case "add": 
                            add(values[1]);
                            break;
                        case "update": 
                            update(Integer.parseInt(values[1]), values[2]);
                            break;
                        case "delete": 
                            delete(Integer.parseInt(values[1]));
                            break;
                        case "list":
                            if (values.length == 1) {
                                listAll();
                            } else {
                                listHelper(values[1]);
                            }
                            break;
                        case "mark-in-progress":
                            markInProgress(Integer.parseInt(values[1]));
                            break;
                        case "mark-done":
                            markDone(Integer.parseInt(values[1]));
                            break;
                        default:
                            System.out.println("Illegal action, please try again.");
                            break;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Fail Sys.in: " + e);
        }
    }
}