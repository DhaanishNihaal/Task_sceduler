# API Testing Guide

## Base URL
```
http://localhost:8080
```

## Test Scenario: Task Scheduling with Dependencies

### Step 1: Add Task 1 (No dependencies, High Priority)

**Endpoint:** `POST /tasks`

**Request Body:**
```json
{
  "id": 1,
  "priority": 10,
  "executionTime": 5,
  "dependencies": []
}
```

**Description:** Task 1 has no dependencies and highest priority (10)

---

### Step 2: Add Task 2 (Depends on Task 1, Medium Priority)

**Endpoint:** `POST /tasks`

**Request Body:**
```json
{
  "id": 2,
  "priority": 7,
  "executionTime": 3,
  "dependencies": [1]
}
```

**Description:** Task 2 depends on Task 1, priority 7

---

### Step 3: Add Task 3 (Depends on Task 1, Low Priority)

**Endpoint:** `POST /tasks`

**Request Body:**
```json
{
  "id": 3,
  "priority": 4,
  "executionTime": 8,
  "dependencies": [1]
}
```

**Description:** Task 3 depends on Task 1, priority 4

---

### Step 4: Add Task 4 (Depends on Tasks 2 and 3, High Priority)

**Endpoint:** `POST /tasks`

**Request Body:**
```json
{
  "id": 4,
  "priority": 9,
  "executionTime": 6,
  "dependencies": [2, 3]
}
```

**Description:** Task 4 depends on both Task 2 and Task 3, priority 9

---

### Step 5: Get Execution Schedule

**Endpoint:** `GET /tasks/schedule`

**Expected Response:**
```json
[1, 2, 3, 4]
```

---

## Dependency Graph Visualization

```
        Task 1 (priority=10, time=5)
        /                \
       /                  \
   Task 2                Task 3
(priority=7, time=3)  (priority=4, time=8)
       \                  /
        \                /
         \              /
          Task 4 (priority=9, time=6)
```

---

## Why This Execution Order is Correct

### Analysis of [1, 2, 3, 4]

#### **Step 1: Task 1 executes first**
- **Indegree:** 0 (no dependencies)
- **Reason:** Only task ready to execute initially
- **After execution:** Tasks 2 and 3 become eligible

#### **Step 2: Task 2 executes second**
- **Indegree:** 0 (Task 1 completed)
- **Eligible tasks:** Task 2 (priority=7), Task 3 (priority=4)
- **Reason:** Task 2 has higher priority than Task 3
- **Priority Queue selects:** Task 2

#### **Step 3: Task 3 executes third**
- **Indegree:** 0 (Task 1 completed)
- **Eligible tasks:** Task 3 only
- **Reason:** Only remaining eligible task
- **After execution:** Task 4 becomes eligible (both dependencies met)

#### **Step 4: Task 4 executes last**
- **Indegree:** 0 (Tasks 2 and 3 completed)
- **Reason:** All dependencies satisfied
- **Final task to execute**

---

## Alternative Valid Orders (Without Priority)

If we used a regular queue instead of priority queue:

- `[1, 2, 3, 4]` ✓ Valid
- `[1, 3, 2, 4]` ✓ Valid (but lower priority task first)

**Our algorithm guarantees:** `[1, 2, 3, 4]` because Task 2 has higher priority than Task 3.

---

## Test Scenario 2: Cycle Detection

### Add Tasks with Circular Dependency

**Task 5:**
```json
{
  "id": 5,
  "priority": 5,
  "executionTime": 4,
  "dependencies": [6]
}
```

**Task 6:**
```json
{
  "id": 6,
  "priority": 6,
  "executionTime": 3,
  "dependencies": [5]
}
```

**Expected Response from GET /tasks/schedule:**
```
HTTP 500 Internal Server Error

{
  "error": "Cycle detected in task dependencies"
}
```

**Reason:** Task 5 → Task 6 → Task 5 (circular dependency)

---

## Test Scenario 3: Complex Dependency Graph

### Tasks:

**Task 10:**
```json
{
  "id": 10,
  "priority": 8,
  "executionTime": 2,
  "dependencies": []
}
```

**Task 11:**
```json
{
  "id": 11,
  "priority": 9,
  "executionTime": 3,
  "dependencies": []
}
```

**Task 12:**
```json
{
  "id": 12,
  "priority": 7,
  "executionTime": 4,
  "dependencies": [10, 11]
}
```

**Task 13:**
```json
{
  "id": 13,
  "priority": 10,
  "executionTime": 5,
  "dependencies": [11]
}
```

**Task 14:**
```json
{
  "id": 14,
  "priority": 6,
  "executionTime": 2,
  "dependencies": [12, 13]
}
```

**Expected Execution Order:**
```json
[11, 10, 13, 12, 14]
```

**Explanation:**
1. **Tasks 10 and 11** have no dependencies
   - Task 11 (priority=9) > Task 10 (priority=8)
   - Execute Task 11 first
2. **After Task 11:** Task 10 and Task 13 are eligible
   - Task 10 (priority=8) < Task 13 (priority=10)
   - Execute Task 13 next
3. **After Task 13:** Only Task 10 is eligible
   - Execute Task 10
4. **After Task 10:** Task 12 is eligible (both 10 and 11 done)
   - Execute Task 12
5. **After Task 12:** Task 14 is eligible (both 12 and 13 done)
   - Execute Task 14

---

## cURL Commands for Testing

### Add Task 1:
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"id":1,"priority":10,"executionTime":5,"dependencies":[]}'
```

### Add Task 2:
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"id":2,"priority":7,"executionTime":3,"dependencies":[1]}'
```

### Add Task 3:
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"id":3,"priority":4,"executionTime":8,"dependencies":[1]}'
```

### Add Task 4:
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"id":4,"priority":9,"executionTime":6,"dependencies":[2,3]}'
```

### Get Schedule:
```bash
curl http://localhost:8080/tasks/schedule
```

---

## Postman Collection Setup

1. Create a new collection: "Task Scheduler API"
2. Add requests in order:
   - POST Task 1
   - POST Task 2
   - POST Task 3
   - POST Task 4
   - GET Schedule
3. Use "Run Collection" to execute all requests sequentially
4. Verify final response is `[1, 2, 3, 4]`

---

## Time Complexity Verification

**For 4 tasks with 5 edges:**
- V = 4 (vertices/tasks)
- E = 5 (edges/dependencies: 1→2, 1→3, 2→4, 3→4)
- Time: O((V + E) log V) = O((4 + 5) log 4) = O(9 × 2) = O(18)

**Very efficient even for large task graphs!**
