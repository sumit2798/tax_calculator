@echo off
echo ==========================================
echo Building Docker image 'taxcalculation:latest'...
echo ==========================================
docker build -t taxcalculation:latest .
if %errorlevel% neq 0 (
    echo Build failed!
    exit /b %errorlevel%
)

echo.
echo ==========================================
echo Stopping any existing container...
echo ==========================================
docker stop taxcalculation 2>NUL
docker rm taxcalculation 2>NUL

echo.
echo ==========================================
echo Running new container...
echo ==========================================
docker run -d -p 8080:8080 --name taxcalculation taxcalculation:latest

echo.
echo ==========================================
echo Deployment successful!
echo Access the application at: http://localhost:8080/tax/calculate
echo ==========================================
pause
