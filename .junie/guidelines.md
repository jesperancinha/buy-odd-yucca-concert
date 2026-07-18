# AI Coding Guidelines - Buy Odd Yucca Concert

## General Rules
- Maintain consistency with the existing tech stack (Kotlin/Micronaut for backend, React/TypeScript for frontend).
- Use clear, descriptive variable and function names.
- Follow the existing project structure (microservices architecture).

## Backend (Kotlin & Micronaut)
- Use Kotlin Coroutines for asynchronous operations where applicable.
- Follow Micronaut best practices for dependency injection and configuration.
- Use Kotest and MockK for testing.
- Ensure all new API endpoints are documented with Swagger/OpenAPI.
- Use Flyway for database migrations.
- Prefer R2DBC for reactive database access.

## Frontend (React & TypeScript)
- Use functional components and hooks.
- Maintain TypeScript type safety.
- Use Jest and React Testing Library for tests.

## Documentation
- Keep Readme.md and TechStack.md updated when adding new technologies or features.
- Use Mermaid for sequence diagrams.

## Infrastructure
- Use Docker and Docker Compose for local development and integration testing.
- Maintain Kong Gateway configurations if API routes change.
