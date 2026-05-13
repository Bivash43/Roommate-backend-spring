# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Comprehensive unit and integration tests for the Household module.
- Fine-grained method-level security using `SecurityService` and `@PreAuthorize`.
- Structured logging with SLF4J in `HouseholdService` and `SecurityService`.
- `spring-security-test` dependency for robust security testing.
- Initial Javadoc documentation for `SecurityService`.

### Changed
- Migrated `@MockBean` to `@MockitoBean` for Spring Boot 3.4+ compatibility.
- Updated `README.md` with detailed security and observability features.
- Replaced deprecated `@Where` with `@SQLRestriction` for soft delete implementation.

### Fixed
- Unused imports in test classes.
