# Task Scheduler API

A Spring Boot REST API for task scheduling using core data structures and algorithms.

## Technologies
- **Java**: 17
- **Spring Boot**: 3.2.1
- **Build Tool**: Maven
- **Storage**: In-memory (ConcurrentHashMap)

## Project Structure
```
com.scheduler.taskscheduler
├── controller/      # REST endpoints
├── service/         # Business logic & algorithms
├── repository/      # In-memory data storage
└── model/           # Domain models
```

## API Endpoints

### Task Management
- `POST /api/tasks` - Create a new task
- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `PUT /api/tasks/{id}` - Update a task
- `DELETE /api/tasks/{id}` - Delete a task

### Task Filtering & Scheduling
- `GET /api/tasks/status/{status}` - Get tasks by status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
- `GET /api/tasks/priority` - Get tasks sorted by priority (uses PriorityQueue)
- `GET /api/tasks/deadline` - Get tasks sorted by deadline

## Task Model
```json
{
  "id": 1,
  "title": "Task Title",
  "description": "Task Description",
  "priority": 5,
  "deadline": "2025-12-31T23:59:59",
  "status": "PENDING",
  "createdAt": "2025-12-29T10:00:00",
  "updatedAt": "2025-12-29T10:00:00"
}
```

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Data Structures & Algorithms Used
- **ConcurrentHashMap**: Thread-safe in-memory storage
- **PriorityQueue**: Priority-based task scheduling
- **Streams & Comparators**: Sorting and filtering tasks

## Example Usage

### Create a Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project",
    "description": "Finish the task scheduler",
    "priority": 10,
    "deadline": "2025-12-31T23:59:59"
  }'
```

### Get All Tasks
```bash
curl http://localhost:8080/api/tasks
```

### Get Tasks by Priority
```bash
curl http://localhost:8080/api/tasks/priority
```