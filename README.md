# Episode Renamer

## About The Project

Episode Renamer is a Kotlin-based tool designed to simplify the process of renaming and organizing multimedia files, specifically tailored for TV shows and series. This tool ensures that media files are named in a way that is compatible with various media servers like Plex, making them easier to manage and access.

The application parses filenames using configurable patterns, renames them according to a standardized format, and moves or copies them into structured directories for each show. It supports a variety of filename formats through extendable file matchers, and offers both copy and move functionalities to suit different organization needs.

## Features

- **File Parsing**: Analyzes filenames based on configurable regex patterns to extract show information.
- **File Renaming**: Standardizes filenames into a consistent format that enhances media library organization.
- **File Moving/Copying**: Offers options to either move or copy files to new locations based on the show and episode details.
- **Configurable Matchers**: Allows users to define custom matching patterns to accommodate different naming conventions.
- **Interactive Mode**: Provides prompts to the user when decisions on file handling are needed, such as overwriting existing files.

## Getting Started

### Prerequisites

- JDK 11 or newer
- Kotlin 1.4 or newer
- Gradle 6.5 or newer (if building from source)

### Installation

#### 1. Clone the repository and setup the application

- Create a `.env` file in the root directory based on the example `.env.dist` 
- Configuring Show Matchers

Show matchers can be configured by creating a `config/shows.yml` file in the root directory:

```yaml
shows:
  - name: "My Show"
    aliases:
      - "MyShow"
  - name: "Another Show"
```

#### 2. Build the project

```bash
./gradlew build
```

#### 3. Run the application

```bash
./gradlew run --args="--source=<source_directory> --target=<target_directory> [options]"
```
Replace <source_directory> and <target_directory> with the paths where your files are currently stored and where you want them to be moved/copied to after renaming.

### Usage
The tool can be configured to run in different modes based on command-line arguments:

- `--dry-run`: Performs a trial run without making any actual changes.
- `--copy`: Copies files to the target directory instead of moving them.
- `--replace`: Replaces files if they exist in the target directory.



Add new shows and aliases according to the file naming conventions you encounter.