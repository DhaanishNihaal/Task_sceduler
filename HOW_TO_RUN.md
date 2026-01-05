# 🚀 How to Run and Test the Task Scheduler Application

## ✅ Prerequisites
- Java 17 installed at: `C:\Program Files\Amazon Corretto\jdk17.0.17_10`
- Maven installed

---

## 📋 Step-by-Step Guide

### **Step 1: Build the Application**

Open PowerShell in the project directory and run:

```powershell
$env:JAVA_HOME = "C:\Program Files\Amazon Corretto\jdk17.0.17_10"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
mvn clean install -DskipTests
```

**Expected output:** `BUILD SUCCESS`

---

### **Step 2: Run the Application**

```powershell
$env:JAVA_HOME = "C:\Program Files\Amazon Corretto\jdk17.0.17_10"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
java -jar target/taskscheduler-0.0.1-SNAPSHOT.jar
```

**Expected output:**
```
Started TaskSchedulerApplication in X.XXX seconds
```

The server is now running at: **http://localhost:8080**

---

### **Step 3: Test the API**

#### **Option A: Use the Test Script (Easiest)**

In a **new PowerShell window**, run:

```powershell
cd d:\Task_scheduler
.\test-api.ps1
```

**Expected output:**
```
Execution Order: [1,2,3,4]
```

#### **Option B: Manual Testing with cURL**

**Add Task 1:**
```bash
curl -X POST http://localhost:8080/tasks -H "Content-Type: application/json" -d "{\"id\":1,\"priority\":10,\"executionTime\":5,\"dependencies\":[]}"
```

**Add Task 2:**
```bash
curl -X POST http://localhost:8080/tasks -H "Content-Type: application/json" -d "{\"id\":2,\"priority\":7,\"executionTime\":3,\"dependencies\":[1]}"
```

**Add Task 3:**
```bash
curl -X POST http://localhost:8080/tasks -H "Content-Type: application/json" -d "{\"id\":3,\"priority\":4,\"executionTime\":8,\"dependencies\":[1]}"
```

**Add Task 4:**
```bash
curl -X POST http://localhost:8080/tasks -H "Content-Type: application/json" -d "{\"id\":4,\"priority\":9,\"executionTime\":6,\"dependencies\":[2,3]}"
```

**Get Schedule:**
```bash
curl http://localhost:8080/tasks/schedule
```

**Expected:** `[1,2,3,4]`

#### **Option C: Use Postman**

1. Open Postman
2. Import the requests from `API_TESTING_GUIDE.md`
3. Send each POST request to add tasks
4. Send GET request to `/tasks/schedule`
5. Verify response is `[1,2,3,4]`

---

## 🛑 How to Stop the Application

Press **Ctrl + C** in the terminal where the app is running.

Or run this command:
```powershell
Get-Process -Name java | Where-Object {$_.Path -like "*jdk17*"} | Stop-Process -Force
```

---

## 🔍 Verify It's Working

### **Check if server is running:**
```powershell
Test-NetConnection -ComputerName localhost -Port 8080
```

Should show: `TcpTestSucceeded : True`

### **Quick health check:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/tasks/schedule" -UseBasicParsing
```

Should return: `StatusCode: 200`

---

## 📊 What the Application Does

1. **Accepts tasks** via POST `/tasks`
2. **Builds dependency graph** from task relationships
3. **Detects cycles** to prevent deadlocks
4. **Computes optimal execution order** using:
   - Kahn's algorithm (topological sort)
   - Priority queue (highest priority first)
5. **Returns execution order** via GET `/tasks/schedule`

---

## 🎯 Expected Results

For the test scenario:
- **Task 1**: No dependencies, priority 10
- **Task 2**: Depends on 1, priority 7
- **Task 3**: Depends on 1, priority 4
- **Task 4**: Depends on 2 and 3, priority 9

**Execution Order:** `[1, 2, 3, 4]`

**Why?**
1. Task 1 executes first (no dependencies)
2. Task 2 executes before Task 3 (higher priority: 7 > 4)
3. Task 4 executes last (needs both 2 and 3)

---

## 🐛 Troubleshooting

### **Port 8080 already in use**
```powershell
Get-Process -Name java | Stop-Process -Force
```

### **Java version error**
```powershell
java -version  # Should show 17.x.x
```

If not, set JAVA_HOME:
```powershell
$env:JAVA_HOME = "C:\Program Files\Amazon Corretto\jdk17.0.17_10"
```

### **Build fails**
```powershell
mvn clean install -X  # Verbose output
```

---

## 📁 Project Structure

```
d:\Task_scheduler\
├── src/main/java/com/scheduler/taskscheduler/
│   ├── TaskSchedulerApplication.java    # Main class
│   ├── controller/
│   │   └── TaskController.java          # REST endpoints
│   ├── service/
│   │   └── TaskService.java             # Task storage
│   ├── model/
│   │   └── Task.java                    # Task model
│   └── util/
│       ├── GraphBuilder.java            # Build dependency graph
│       ├── CycleDetector.java           # Detect cycles (DFS)
│       └── Scheduler.java               # Kahn's algorithm
├── test-api.ps1                         # Test script
├── API_TESTING_GUIDE.md                 # Detailed API docs
└── README.md                            # Project overview
```

---

## 🎓 Algorithms Implemented

- ✅ **HashMap** - O(1) task lookup
- ✅ **Adjacency List** - Graph representation
- ✅ **DFS** - Cycle detection
- ✅ **Kahn's Algorithm** - Topological sort
- ✅ **Priority Queue** - Priority-based scheduling

**Time Complexity:** O((V + E) log V)

---

## 🚀 Next Steps

1. ✅ Run the application
2. ✅ Test with provided script
3. ✅ Verify execution order is correct
4. 📖 Read `API_TESTING_GUIDE.md` for more test scenarios
5. 🧪 Try creating your own task dependencies!

---

**Application is ready to use!** 🎉
