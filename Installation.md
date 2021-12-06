# Buy Odd Yucca Concert Installation Notes

[Sdk-Man](https://sdkman.io/):

```shell
sdk update
sdk install micronaut
```

## Design time

```shell
mn create-app buy-oyc-commons --build=maven --lang=kotlin
mn create-app buy-oyc-ticket-service --build=maven --lang=kotlin
mn create-app buy-oyc-parking-service --build=maven --lang=kotlin
mn create-app buy-oyc-catering-service --build=maven --lang=kotlin
```