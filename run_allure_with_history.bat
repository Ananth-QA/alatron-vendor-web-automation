@echo off
set PROJECT_DIR=%cd%

echo =====================================
echo   ALLURE AUTOMATED HISTORY EXECUTION
echo =====================================

REM 1. Preserve previous history
if exist "%PROJECT_DIR%\allure-report\history" (
    echo Copying previous Allure history...
    xcopy "%PROJECT_DIR%\allure-report\history" "%PROJECT_DIR%\allure-results\history" /E /I /Y
) else (
    echo No previous history found. First run.
)

REM 2. Run tests
echo Running TestNG tests...
mvn clean test

REM 3. Generate Allure report (NO CLEAN)
echo Generating Allure report...
allure generate "%PROJECT_DIR%\allure-results" -o "%PROJECT_DIR%\allure-report"

REM 4. Open Allure report
echo Opening Allure report...
allure open "%PROJECT_DIR%\allure-report"

echo =====================================
echo   EXECUTION COMPLETED SUCCESSFULLY
echo =====================================
pause
