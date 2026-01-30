# Task Tracker CLI

A simple command-line task management application to help you organize your work.  
Track what you need **to do**, what you're **currently working on**, and what you've **completed**.

Built as a learning project to practice:
- Command-line argument parsing
- File I/O (JSON persistence)
- Basic data modeling and manipulation in Java
- Error handling and user-friendly feedback

## Features

- Add new tasks
- Update existing tasks (description or status)
- Delete tasks
- Mark tasks as **TODO** / **In Progress** / **Done**
- List tasks with filtering:
  - All tasks
  - Only tasks that are **Done**
  - Only tasks that are **To Do** (TODO + In Progress)
  - Only tasks that are **In Progress**

Tasks are stored persistently in a JSON file (`data.json` by default) in the specified output folder.

## Requirements

- Java 8 or higher (developed with Java 11+ in mind)
- Maven (recommended) or Gradle for dependency management
- Optional: `.env` file for configuration (e.g., output folder path)
