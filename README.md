# Entity-filters

Spring Boot library that allows to automatically generate filter classes for given entities. 

For any class T annotated with @Entity and @GenerateFilters class TFilters will be generated.
TFilters will have intoSpecification() method that allows easy integration with Spring Boot
Specification API.


## Project organization
| module | description |
| --- | --- |
| filters | classes that allow filtering by some type |
| generator-filters | classes that allow automatic TFilters generation |
| tests | library tests |

## Example
For given entity:
```Java
@Entity
@GenerateFilters
public class CodeGenerationTestEntity {
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private Integer version;
}
```
This filters class will be generated
```Java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeGenerationTestEntityFilters {
    @Valid private NumericValueFilter<java.lang.Long> id;
    @Valid private StringValueFilter name;
    @Valid private NumericValueFilter<java.math.BigDecimal> price;
    @Valid private NumericValueFilter<java.time.LocalDate> createdAt;
    @Valid private NumericValueFilter<java.time.LocalDateTime> updatedAt;
    @Valid private NumericValueFilter<java.lang.Integer> version;

    public Specification<CodeGenerationTestEntity> intoSpecification() {
        return Specification.allOf(
            mapFilterToSpecification(CodeGenerationTestEntity_.id, id),
            mapFilterToSpecification(CodeGenerationTestEntity_.name, name),
            mapFilterToSpecification(CodeGenerationTestEntity_.price, price),
            mapFilterToSpecification(CodeGenerationTestEntity_.createdAt, createdAt),
            mapFilterToSpecification(CodeGenerationTestEntity_.updatedAt, updatedAt),
            mapFilterToSpecification(CodeGenerationTestEntity_.version, version)
        );
    }

    private static <T extends Comparable<T>> Specification<CodeGenerationTestEntity> mapFilterToSpecification(
        SingularAttribute<CodeGenerationTestEntity, ? extends T> attribute,
        NumericValueFilter<? extends T> filter
    ) {
        if (filter == null) {
            return (root, query, cb) -> cb.conjunction();
        }

        return switch (filter.getType()) {
            case LESS          -> (root, query, cb) -> cb.lessThan(root.get(attribute), filter.getV1());
            case LESS_EQUAL    -> (root, query, cb) -> cb.lessThanOrEqualTo(root.get(attribute), filter.getV1());
            case EQUAL         -> (root, query, cb) -> cb.equal(root.get(attribute), filter.getV1());
            case GREATER       -> (root, query, cb) -> cb.greaterThan(root.get(attribute), filter.getV1());
            case GREATER_EQUAL -> (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(attribute), filter.getV1());
            case BETWEEN       -> (root, query, cb) -> cb.between(root.get(attribute), filter.getV1(), filter.getV2());
            case IS_NULL       -> (root, query, cb) -> cb.isNull(root.get(attribute));
            case IS_NOT_NULL   -> (root, query, cb) -> cb.isNotNull(root.get(attribute));
        };
    }

    private static <T extends Comparable<T>> Specification<CodeGenerationTestEntity> mapFilterToSpecification(
        SingularAttribute<CodeGenerationTestEntity, ? extends T> attribute,
        StringValueFilter filter
    ) {
        if (filter == null) {
            return (root, query, cb) -> cb.conjunction();
        }

        return switch (filter.getType()) {
            case EQUAL             -> (root, query, cb) -> cb.equal(root.get(attribute), filter.getV());
            case EQUAL_IGNORE_CASE -> (root, query, cb) -> cb.equal(
                cb.upper(
                    root
                        .get(attribute)
                        .as(String.class)
                ),
                filter.getV().toUpperCase()
            );
            case IS_NULL     -> (root, query, cb) -> cb.isNull(root.get(attribute));
            case IS_NOT_NULL -> (root, query, cb) -> cb.isNotNull(root.get(attribute));
        };
    }

}
```

## How to run tests
```bash
mvn test
```

If for some reason there's problem with running test using onlny ```mvn test``` try this:

```bash
mvn clean
mvn test-compile
mvn test
```

## How to use it (Maven)
First compile tests (without it there may be errors related to code not being generated at correct time )
```bash
mvn test-compile
```
Then install library
```bash
mvn install
```

Add dependencies to pom.xml
```xml
<!-- Filters -->
<dependency>
  <groupId>com.example.filter</groupId>
  <artifactId>filters</artifactId>
  <version>0.0.2</version>
</dependency>

<!-- Filters automatic generation -->
<dependency>
  <groupId>com.example.filter</groupId>
  <artifactId>generator-filters</artifactId>
  <version>0.0.2</version>
</dependency>

<!-- It's necessary to also add lombok dependency -->
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <version>1.18.36</version>
  <scope>provided</scope>
</dependency>
```

Add plugins to pom.xml
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.13.0</version>
      <configuration>
        <annotationProcessorPaths>
          <!--
            Generates T_ class that allows to access entity field names
            required by Specification API
          -->
          <path>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-jpamodelgen</artifactId>
            <version>6.6.6.Final</version>
          </path>
          <!-- Lombok generator -->
          <path>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.36</version>
          </path>
          <!-- Filters automatic generation -->
          <path>
            <groupId>com.example.filter</groupId>
            <artifactId>generator-filters</artifactId>
            <version>0.0.2</version>
          </path>
        </annotationProcessorPaths>
      </configuration>
    </plugin>
  </plugins>
</build>
```