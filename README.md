# Native Spring Boot microservice

## This project shows how to build a native, i.e., native as opposed to bytecode, therefore no JVM is needed, microservice using Spring Boot.

### Why tho?
Although the JVM has implemented many optimizations that allows to run programs that perform comparably to native programs there are occasions where a native program is preferable:

* **Quick initialization**. JVM programs are known to need a warm-up, i.e., takes considerable time, in the order of seconds or even minutes, from launching a program to the point where the program is functional, i.e., can handle requests.  This service usually starts in less than 100ms.　Although there are some workarounds like optimizing the scanning of the `classpath`, separating the client written in C and the server in Java like [nailgun](https://github.com/facebook/nailgun) does, these are workarounds.
  
* **Removing an additional layer: the JVM**. No need to install the JVM in the host, e.g., server, container, where the program runs. Increasing operational efficiency.

* **Native process**. The process can be handled like any other process in the machine, i.e., standard tools like `ps(1)` can be used; on the other hand the tooling for observability available for the JVM, e.g., VisualVM is gone.

* **Smaller program size**. The image is only XXXMB, compared to XXXMB plus the JVM. Think about cheaper hosting, faster copying and generation of images.

* **Smaller memory footprint** 75% according to [this video](https://youtu.be/7WYVJyuy00g?t=256).

* **Less vulenrabilities** Because there is less code as only the reachable code of the JVM is compiled into the image.

* **Fun!!**

### Okay, how?
Using [Spring Native, which is still in beta](https://spring.io/blog/2021/03/11/announcing-spring-native-beta).

#### Prerequisites
* Your JVM supports GraalVM.
```
$ java -version
Picked up JAVA_TOOL_OPTIONS: -Dfile.encoding=UTF8
openjdk version "11.0.12" 2021-07-20
OpenJDK Runtime Environment GraalVM CE 21.2.0 (build 11.0.12+6-jvmci-21.2-b08)
OpenJDK 64-Bit Server VM GraalVM CE 21.2.0 (build 11.0.12+6-jvmci-21.2-b08, mixed mode, sharing)
```
See the [Spring Native docs](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-native-build-tools)

* Maven 3


#### Let's do it!
The code is very simple
```java
        @GetMapping("/hello")
        public ResponseEntity<String> greeting(@RequestParam(value = "name",
                defaultValue = "World") final String name) {

            return new ResponseEntity<>(format("Hello %s", name), OK);
        }
```

##### Build
Creates a package using the `native` profile and completely skips the tests `mvn -Pnative -DskipTests package`.
The building process takes considerably longer than the usual `mvn package` as the build process is more complex.

The native binary is created!
```
$ file target/spring-native-microservice
target/spring-native-microservice: Mach-O 64-bit executable x86_64
```

```
$ls -lh target/spring-native-microservice
-rwxr-xr-x  1 user45432  staff    66M Aug 27 22:23 target/spring-native-microservice*
```  


#### Run
`target/spring-native-microservice` launches our microservice.


Then we can send a request
```
$ curl -X GET "http://localhost:8080/hello?name=user454322"
Hello user454322
```

#### Important points to keep in mind
> there is no class lazy loading (everything shipped in the executable is loaded in memory on startup) and some code can be invoked at build time.

> A static analysis of your application from the main entry point is performed at build time.

> The unused parts are removed at build time.

> Configuration is required for reflection, resources, and dynamic proxies.

> Classpath is fixed at build time.

### References
* https://docs.spring.io/spring-native/docs/current/reference/htmlsingle
* https://spring.io/blog/2021/03/11/announcing-spring-native-beta
* https://youtu.be/7WYVJyuy00g
