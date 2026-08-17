# Contributing

## Bug Reports
Submit bug reports via the [Issue Tracker](https://github.com/yigit-guven/Chymistry/issues).
Required information:
- Chymistry version
- Modloader & version
- Exact steps to reproduce
- Relevant crash reports or stack traces

## Code Contributions

### Workspace Setup
Requirement: Java 21

```bash
git clone https://github.com/yigit-guven/Chymistry.git
cd Chymistry
./gradlew build
```
Import the repository into your IDE as a Gradle project.

### Coding Standard
- Conform to the existing code formatting and naming conventions.
- Code must be self-documenting. Do not write conversational or redundant comments. Use inline documentation strictly for complex logic or public APIs.
- Remove all temporary scripts, test classes, and debug outputs before committing.

### Pull Requests
- Base your feature branch off the relevant active development branch (e.g., `26.2-neoforge`, or the respective legacy branch if submitting a port).
- Ensure commits are atomic and messages are descriptive.
- Submit Pull Requests against the same target branch you are modifying or porting to.
- Outline the technical changes and rationale in the PR description. Include the related issue number if applicable.