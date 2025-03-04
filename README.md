<p align="center">
  <picture>
    <img alt="Java Game Box icon" src="./src/main/resources/icon.png" width=25% height=25%>
  </picture>
</p>
<h1 align="center">Java Game Box</h1>

[![Releases](https://img.shields.io/github/v/release/IvanNovR/JavaGameBox.svg)](https://github.com/IvanNovR/JavaGameBox/releases)
[![Status](https://img.shields.io/github/actions/workflow/status/IvanNovR/JavaGameBox/maven.yml.svg)](#)
[![License](https://img.shields.io/badge/license-GPL%20V3-blue.svg?longCache=true)](https://www.gnu.org/licenses/gpl-3.0.en.html)
[![Russian Readme](https://img.shields.io/badge/russian-readme-blue)](/README_RU.md)

JavaGameBox is a simple game platform that allows users to play classic games. It includes features like user authentication, score tracking, and a leaderboard system.

## Project Structure

The project is organized as follows:
- `src/main/java/ru/ivannovr`: Contains the main application code.
    - `games`: Logic for individual games.
    - `gui`: Graphical user interface components.
    - `utils`: Utility classes, including database management.
- `src/main/resources`: Contains resources like icons and configuration files.
- `pom.xml`: Maven configuration file for dependency management and project building.

## Building the Project

To build the project, you need to have Maven installed. You can download Maven from the [official website](https://maven.apache.org/download.cgi).

Once Maven is installed, navigate to the project root directory and run:

```bash
mvn clean package
```

This command compiles the code, runs tests, and packages the application into a JAR file located in the `target` directory.

## Running the Project

To run the project, you need Java installed. You can download Java from the [official website](https://www.java.com/en/download/).

Additionally, the project requires PostgreSQL to be installed and running. You can download PostgreSQL from the [official website](https://www.postgresql.org/download/).

Before launching the application, set up the database:
1. Create a new database in PostgreSQL.
2. Update the database URL, username, and password in the command-line arguments when running the JAR file.

Run the application with the following command:

```bash
java -jar target/JavaGameBox-1.1.1.jar --db-url=<database_url> --db-user=<database_user> --db-password=<database_password>
```

Replace `<database_url>`, `<database_user>`, and `<database_password>` with your actual PostgreSQL connection details.

## Additional Information

- The project uses Maven for dependency management (see `pom.xml` for details).
- Logging is handled by Log4j, with configuration defined in `src/main/resources/log4j2.xml`.
- The GUI is built using Swing.
- Each game is implemented using object-oriented principles, extending an `AbstractGame` class.

For more details, refer to the source code and comments.