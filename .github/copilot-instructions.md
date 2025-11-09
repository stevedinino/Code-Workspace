## Quick orientation for code-writing agents

This repository contains two distinct projects at the workspace root:

- `catalyst_test/` — a Gradle-based Java/Cucumber/Selenium test project (JUnit 5 + Cucumber).
- `catalyst_website/` — a static website (HTML/PHP assets) with a GitHub Actions FTP deploy workflow.

Use this file to find the most relevant places to read and to follow the project's conventions when editing, adding tests, or changing CI.

### Big picture (what touches what)
- Tests live in `catalyst_test/`. They are Cucumber feature files + Java step definitions that drive Selenium.
- The `catalyst_website/` folder is a static site that the repo's workflow deploys via FTP (`.github/workflows/deploy.yml` inside that folder).

### Key files to inspect before making changes
- `catalyst_test/build.gradle` — dependency management and test engine configuration (JUnit Platform + Cucumber). It declares Selenium and WebDriverManager.
- `catalyst_test/gradlew` and `gradlew.bat` — use the Gradle wrapper in that folder when building or running tests.
- `catalyst_test/src/test/resources/features/` — Cucumber feature files (e.g. `LaunchCatalyst.feature`).
- `catalyst_test/src/test/java/runners/TestRunner.java` — JUnit Platform suite: it selects the `features` classpath resource and sets the GLUE to `stepdefinitions` and the HTML report to `build/cucumber-report.html`.
- `catalyst_test/src/test/java/stepdefinitions/CatalystSteps.java` — example step definitions showing how WebDriverManager and driver lifecycle are used.
- `catalyst_website/.github/workflows/deploy.yml` — deployment action that expects FTP credentials in repository secrets.

### Build & test commands (how developers run things locally)
- On Windows (PowerShell):
  - cd into the test project: `cd "catalyst_test"`
  - Run tests: `.\gradlew.bat clean test` (this will execute the Cucumber/JUnit tests and produce `build/cucumber-report.html`).
  - Or run `.\\gradlew.bat test` for a faster run without clean.

Note: the tests use the Gradle wrapper inside `catalyst_test/` — do not call a global gradle unless intentional.

### Project conventions & important patterns
- Cucumber glue package is `stepdefinitions`. If you add step implementations, place them under `catalyst_test/src/test/java/stepdefinitions` and do not rename the package without updating `TestRunner.java`'s GLUE config.
- Feature files belong in `catalyst_test/src/test/resources/features/` and are executed by the classpath-based runner (`@SelectClasspathResource("features")`).
- Test report: `catalyst_test/build/cucumber-report.html` is produced by the runner's PLUGIN property — reference or open this file after test runs.
- WebDriver usage: `CatalystSteps.java` uses `WebDriverManager` and constructs a `FirefoxDriver()` in the field: be mindful that driver setup must happen before instantiation. When modifying driver lifecycle, prefer calling `WebDriverManager.<browser>()setup()` before creating the driver instance.

### CI / deploy notes
- `catalyst_website/.github/workflows/deploy.yml` deploys the static site via FTP to `ftp.catalystlegalnurse.com`. The workflow expects `FTP_USERNAME` and `FTP_PASSWORD` in repository secrets. Do not add secrets to the repo.

### Safe edit checklist for agents
1. If changing tests, run them locally using the Gradle wrapper in `catalyst_test/` before committing.
2. If moving step definitions or features, update `TestRunner.java` GLUE/SelectClasspathResource values.
3. If changing browser logic, ensure `WebDriverManager` setup is executed before `new ChromeDriver()`/`new FirefoxDriver()`.
4. Avoid editing `catalyst_website/.github/workflows/deploy.yml` unless you understand FTP secrets and the intended deployment target.

### Examples (copyable patterns from the repo)
- Test runner registration (see `TestRunner.java`):
  - GLUE package: `stepdefinitions`
  - Report plugin: `html:build/cucumber-report.html`
- Feature location example: `catalyst_test/src/test/resources/features/LaunchCatalyst.feature`

### When you need human help
- Ask for clarification if you need to change deployment targets or secrets.
- Ask if you plan to change the testing strategy (e.g., migrate from Firefox to Chrome grid) — that can impact CI and required secrets/infrastructure.

If anything here is unclear or missing, tell me which area you'd like expanded (build, tests, test data, CI, or browser setup) and I'll refine this file.
