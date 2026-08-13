---
name: testcontainers for java
description: Migration guide and conventions to apply when using Testcontainers for Java.
---

# Testcontainers for Java Version 2 Migration Guide

This guide provides the necessary steps to migrate from Testcontainers 1.x to 2.x.

## 1. Update Maven Dependencies

In Testcontainers 2.x, all artifact IDs have been standardized with a `testcontainers-` prefix.

### Dependency Management (BOM)
It is recommended to use the Bill of Materials (BOM) to manage versions.

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>testcontainers-bom</artifactId>
            <version>2.0.5</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### Artifact Renaming
Update your dependencies to use the new naming convention:

| Old Artifact ID | New Artifact ID |
|-----------------|-----------------|
| `testcontainers` | `testcontainers` |
| `postgresql` | `testcontainers-postgresql` |
| `mysql` | `testcontainers-mysql` |
| `mongodb` | `testcontainers-mongodb` |
| `junit-jupiter` | `testcontainers-junit-jupiter` |
| `kafka` | `testcontainers-kafka` |

## 2. API Changes

### Explicit Image Specification
Constructors that previously relied on default images now require an explicit image name and tag.

**Before:**
```java
private static PostgreSQLContainer postgres = new PostgreSQLContainer<>();
```

**After:**
```java
private static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");
```

### Networking Changes
`ContainerState.getContainerIpAddress()` has been removed. Use `getHost()` instead.

**Before:**
```java
String ip = container.getContainerIpAddress();
```

**After:**
```java
String host = container.getHost();
```

### Docker Compose Migration
`DockerComposeContainer` (which used Compose V1) is deprecated. Use `ComposeContainer` (which uses Compose V2).

**Example:**
```java
public ComposeContainer environment = new ComposeContainer(
    DockerImageName.parse("docker:25.0.5"),
    new File("src/test/resources/docker-compose.yml")
).withExposedService("redis-1", 6379);
```

## 3. Resources

For full details and further documentation, refer to the official Testcontainers for Java page:
- [Testcontainers for Java Official Website](https://java.testcontainers.org/)
- [Release Notes](https://github.com/testcontainers/testcontainers-java/releases)
