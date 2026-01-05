# Test the Task Scheduler API
Write-Host "=== Testing Task Scheduler API ===" -ForegroundColor Cyan

# Add Task 1
Write-Host "`nAdding Task 1 (no dependencies, priority=10)..." -ForegroundColor Yellow
$task1 = '{"id":1,"priority":10,"executionTime":5,"dependencies":[]}'
$response1 = Invoke-WebRequest -Uri "http://localhost:8080/tasks" -Method POST -Body $task1 -ContentType "application/json" -UseBasicParsing
Write-Host "Response: $($response1.Content)" -ForegroundColor Green

# Add Task 2
Write-Host "`nAdding Task 2 (depends on 1, priority=7)..." -ForegroundColor Yellow
$task2 = '{"id":2,"priority":7,"executionTime":3,"dependencies":[1]}'
$response2 = Invoke-WebRequest -Uri "http://localhost:8080/tasks" -Method POST -Body $task2 -ContentType "application/json" -UseBasicParsing
Write-Host "Response: $($response2.Content)" -ForegroundColor Green

# Add Task 3
Write-Host "`nAdding Task 3 (depends on 1, priority=4)..." -ForegroundColor Yellow
$task3 = '{"id":3,"priority":4,"executionTime":8,"dependencies":[1]}'
$response3 = Invoke-WebRequest -Uri "http://localhost:8080/tasks" -Method POST -Body $task3 -ContentType "application/json" -UseBasicParsing
Write-Host "Response: $($response3.Content)" -ForegroundColor Green

# Add Task 4
Write-Host "`nAdding Task 4 (depends on 2,3, priority=9)..." -ForegroundColor Yellow
$task4 = '{"id":4,"priority":9,"executionTime":6,"dependencies":[2,3]}'
$response4 = Invoke-WebRequest -Uri "http://localhost:8080/tasks" -Method POST -Body $task4 -ContentType "application/json" -UseBasicParsing
Write-Host "Response: $($response4.Content)" -ForegroundColor Green

# Get Schedule
Write-Host "`n`n=== Getting Optimized Schedule ===" -ForegroundColor Cyan
$schedule = Invoke-WebRequest -Uri "http://localhost:8080/tasks/schedule" -Method GET -UseBasicParsing
Write-Host "Execution Order: $($schedule.Content)" -ForegroundColor Magenta
Write-Host "`nExpected: [1,2,3,4]" -ForegroundColor White

Write-Host "`n=== Test Complete! ===" -ForegroundColor Cyan
