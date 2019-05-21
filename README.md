# Scheduler Conversion

![conversion](/doc/img/conversion_128.png)

> Convert reactor thread into elastic thread

## How to use 


### Mode

- include (default)

```
@EnableSchedulerConversion(mode = Mode.INCLUDE)

```

- exclude

```
@EnableSchedulerConversion(mode = Mode.EXCLUDE)

```

### Config

```
@EnableSchedulerConversion
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

```

### application.yml

```
scheduler.conversion.patterns: '/api/**, /batch/**'
```