# Selenium Docker Test Automation for Notion

This project provides a Dockerized environment for running Selenium tests against Notion.

## How to Run

1. Start the Docker containers:
   ```bash
   docker compose up
   ```

2. Execute tests in the container:
   ```bash
   docker compose exec ubuntu bash
   cd /home/selenium/tests/notion_selenium_test
   gradle clean test
   ```

## Project Structure

- `tests/notion_selenium_test/` - Main test automation project
- `Dockerfile` - Container configuration
- `docker-compose.yml` - Multi-container setup
