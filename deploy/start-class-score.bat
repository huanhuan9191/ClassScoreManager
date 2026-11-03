@echo off
chcp 65001 >nul
title 班级学生成绩管理系统 启动器

REM ===== 请按你的实际安装位置修改以下变量 =====
set "JAVA_HOME=E:\jdk1.8.0_504"
set "CATALINA_HOME=E:\soft\tomcat9"
set "MYSQL_HOME=E:\soft\mysql\mysql-8.0.29-winx64"
set "MYSQL_INI=%MYSQL_HOME%\my.ini"
set "APP_URL=http://localhost:8080/ClassScoreManager/"
REM ===========================================

echo ============================================
echo   班级学生成绩管理系统 - 一键启动
echo   MySQL + Tomcat + 浏览器
echo ============================================
echo.

echo [1/3] 启动 MySQL（端口 3306）...
start "MySQL Server" "%MYSQL_HOME%\bin\mysqld.exe" --defaults-file="%MYSQL_INI%" --console
timeout /t 10 /nobreak >nul

echo [2/3] 启动 Tomcat（端口 8080）...
set "CATALINA_BASE=%CATALINA_HOME%"
start "Tomcat 9" cmd /k call "%CATALINA_HOME%\bin\catalina.bat" run
timeout /t 14 /nobreak >nul

echo [3/3] 打开浏览器...
start "" %APP_URL%

echo.
echo 访问地址：%APP_URL%
echo 测试账号（密码均 123456）：管理员 admin   教师 teacher1   学生 stu230101
echo.
pause
