MakeMyTrip Automation Project

About

This is an automation testing project for the MakeMyTrip website using Selenium WebDriver and TestNG.
The project is designed to validate core functionalities like flight search, hotel booking, and user interactions.

Features

Automated test cases for flight search and booking flows.
Validates UI elements and page navigations.
Uses TestNG framework for structured test execution and reporting.
Generates HTML reports for test results.
Cross-browser testing ready (Chrome/Firefox).

Installation

Prerequisites
Java JDK 8 or higher
Maven 3.x
ChromeDriver / GeckoDriver for browser automation
IDE: Eclipse/IntelliJ IDEA

Steps
1.Clone the repository:
git clone https://github.com/yourusername/MakeMyTrip_Assignment.git

2.Navigate to the project folder:
cd MakeMyTrip_Assignment

3.Install dependencies using Maven:
mvn clean install

4.Run tests using TestNG XML suite:
mvn test


Test Structure

src/main/java → Page Objects and utility classes
src/test/java → TestNG test classes
config/config.properties → Environment-specific configurations
testng.xml → TestNG suite file to organize test execution

Usage

Open testng.xml and select the tests you want to run.
Execute using Maven or directly from the IDE.
Check the TestNG reports under test-output folder for results

Technologies
Java
Selenium WebDriver
TestNG
Maven
Git & GitHub
