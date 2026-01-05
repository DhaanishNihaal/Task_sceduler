# 🎨 Frontend UI Guide

## ✅ UI is Ready!

Your Task Scheduler now has a beautiful web interface!

---

## 🌐 How to Access the UI

### **Step 1: Make sure the server is running**

The server should already be running. If not, start it:

```powershell
$env:JAVA_HOME = "C:\Program Files\Amazon Corretto\jdk17.0.17_10"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
java -jar target/taskscheduler-0.0.1-SNAPSHOT.jar
```

Wait for: `Started TaskSchedulerApplication`

### **Step 2: Open your browser**

Navigate to:
```
http://localhost:8080/index.html
```

Or simply:
```
http://localhost:8080/
```

---

## 🎯 How to Use the UI

### **Adding Tasks:**

1. **Task ID**: Enter a unique number (e.g., 1, 2, 3)
2. **Priority**: Higher number = higher priority (e.g., 10)
3. **Execution Time**: Time units needed (e.g., 5)
4. **Dependencies**: Comma-separated task IDs (e.g., "1,2" or leave empty)
5. Click **"➕ Add Task"**

### **Getting Schedule:**

1. After adding all tasks, click **"📊 Get Schedule"**
2. The execution order will appear below (e.g., `[1 → 2 → 3 → 4]`)

### **Example Workflow:**

```
Add Task 1: ID=1, Priority=10, Time=5, Dependencies=(empty)
Add Task 2: ID=2, Priority=7, Time=3, Dependencies=1
Add Task 3: ID=3, Priority=4, Time=8, Dependencies=1
Add Task 4: ID=4, Priority=9, Time=6, Dependencies=2,3

Click "Get Schedule"
Result: [1 → 2 → 3 → 4]
```

---

## 🔧 How the UI Communicates with Backend

### **Architecture:**

```
┌─────────────────────────────────────────┐
│         Browser (Frontend)              │
│  - HTML form for input                  │
│  - JavaScript Fetch API                 │
│  - Display results                      │
└──────────────┬──────────────────────────┘
               │
               │ HTTP Requests
               │ (JSON payload)
               │
               ▼
┌─────────────────────────────────────────┐
│    Spring Boot Backend (Port 8080)      │
│  - TaskController REST endpoints        │
│  - TaskService (business logic)         │
│  - Graph algorithms                     │
└─────────────────────────────────────────┘
```

### **Communication Flow:**

#### **1. Adding a Task (POST /tasks)**

**Frontend JavaScript:**
```javascript
const task = {
    id: 1,
    priority: 10,
    executionTime: 5,
    dependencies: []
};

fetch('http://localhost:8080/tasks', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(task)
})
```

**Backend receives:**
```java
@PostMapping
public Task addTask(@RequestBody Task task) {
    taskService.addTask(task);
    return task;
}
```

**Response:** `{"id":1,"priority":10,"executionTime":5,"dependencies":[]}`

#### **2. Getting Schedule (GET /tasks/schedule)**

**Frontend JavaScript:**
```javascript
fetch('http://localhost:8080/tasks/schedule')
    .then(response => response.json())
    .then(schedule => {
        // schedule = [1, 2, 3, 4]
        display(schedule);
    })
```

**Backend processes:**
```java
@GetMapping("/schedule")
public List<Integer> getSchedule() {
    List<Task> tasks = taskService.getAllTasks();
    Map<Integer, List<Integer>> graph = GraphBuilder.buildGraph(tasks);
    if (CycleDetector.hasCycle(graph)) {
        throw new IllegalStateException("Cycle detected");
    }
    return Scheduler.schedule(tasks);
}
```

**Response:** `[1, 2, 3, 4]`

---

## 📂 How Spring Boot Serves the HTML

### **File Location:**
```
src/main/resources/static/index.html
```

### **Spring Boot Magic:**

Spring Boot automatically serves static files from `src/main/resources/static/`:

1. **Build Process:**
   - Maven copies `src/main/resources/static/` to `target/classes/static/`
   - Files are packaged into the JAR

2. **Runtime:**
   - Spring Boot's embedded Tomcat serves files from `/static/`
   - URL mapping: `http://localhost:8080/index.html` → `static/index.html`

3. **Default Behavior:**
   - `http://localhost:8080/` → serves `static/index.html` (if exists)
   - No controller needed!
   - No configuration needed!

### **Why This Works:**

Spring Boot has a built-in **ResourceHttpRequestHandler** that:
- Looks for static resources in predefined locations
- Serves them directly without going through controllers
- Locations checked (in order):
  1. `/static/`
  2. `/public/`
  3. `/resources/`
  4. `/META-INF/resources/`

---

## 🎨 UI Features

### **Modern Design:**
- ✅ Gradient background
- ✅ Clean card-based layout
- ✅ Smooth animations on buttons
- ✅ Color-coded results (success/error)
- ✅ Responsive design
- ✅ Helper text for each field

### **User Experience:**
- ✅ Real-time feedback on task addition
- ✅ List of added tasks displayed
- ✅ Clear error messages
- ✅ Visual execution order with arrows (→)
- ✅ Form auto-clears after adding task

### **No External Dependencies:**
- ✅ Pure HTML/CSS/JavaScript
- ✅ No React, Angular, Vue
- ✅ No Bootstrap, Tailwind
- ✅ No jQuery
- ✅ Works offline (after initial load)

---

## 🔍 Technical Details

### **JavaScript Fetch API:**

The UI uses the modern **Fetch API** for HTTP requests:

```javascript
// Async/await syntax for cleaner code
async function addTask() {
    const response = await fetch(url, options);
    const data = await response.json();
    return data;
}
```

**Advantages:**
- Promise-based (modern JavaScript)
- Built into all modern browsers
- No external libraries needed
- Clean async/await syntax

### **CORS (Cross-Origin Resource Sharing):**

Since the HTML is served by the same server (localhost:8080), **no CORS issues**!

- Frontend: `http://localhost:8080/index.html`
- Backend API: `http://localhost:8080/tasks`
- Same origin → No CORS headers needed

### **JSON Communication:**

**Request:**
```json
POST /tasks
Content-Type: application/json

{
  "id": 1,
  "priority": 10,
  "executionTime": 5,
  "dependencies": []
}
```

**Response:**
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 1,
  "priority": 10,
  "executionTime": 5,
  "dependencies": []
}
```

---

## 🐛 Troubleshooting

### **UI doesn't load:**
1. Check server is running: `http://localhost:8080/tasks/schedule`
2. Clear browser cache (Ctrl + Shift + R)
3. Check file exists: `src/main/resources/static/index.html`

### **"Failed to fetch" error:**
1. Server must be running
2. Check URL is `http://localhost:8080` (not https)
3. Check browser console (F12) for errors

### **Tasks not appearing in schedule:**
1. Make sure you clicked "Add Task" for each task
2. Check "Added Tasks" section shows your tasks
3. Verify no circular dependencies

---

## 🚀 Quick Start

1. **Start server:**
   ```powershell
   java -jar target/taskscheduler-0.0.1-SNAPSHOT.jar
   ```

2. **Open browser:**
   ```
   http://localhost:8080/
   ```

3. **Add tasks and get schedule!**

---

## 📊 Example Test Case

Try this in the UI:

1. **Task 1:** ID=1, Priority=10, Time=5, Deps=(empty) → Add
2. **Task 2:** ID=2, Priority=7, Time=3, Deps=1 → Add
3. **Task 3:** ID=3, Priority=4, Time=8, Deps=1 → Add
4. **Task 4:** ID=4, Priority=9, Time=6, Deps=2,3 → Add
5. Click **"Get Schedule"**

**Expected Result:** `[1 → 2 → 3 → 4]`

---

**Your full-stack Task Scheduler is complete!** 🎉
