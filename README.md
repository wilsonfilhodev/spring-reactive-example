# spring-reactive-example
Demo API Rest using Spring Boot and Spring Reactive

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)
- Configure data base in file `application.properties`

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `br.com.wilsonfilho.springreactive.SpringReactiveApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```
## What's included

* List all products

```shell
  $ curl http://localhost:8080/products
```

```
  Example return data.
  
  [  
     {  
        "id":"95a460f5-f16c-425d-8c27-670f0034b5b3",
        "name":"Notebook Dell 5570"
     },
     {  
        "id":"673c80d3-6f2b-4bb7-8a5f-eac48c9dee63",
        "name":"Monitor LCD 15.6"
     },
     {  
        "id":"e5f2dcf6-9c7b-4b22-bf37-12a02c9df4c0",
        "name":"Mouse Microsoft T3"
     },
     {  
        "id":"19d1ef84-a559-4d0c-addf-bbb35e5ee1c2",
        "name":"Roteador Intelbras WRN301"
     },
     {  
        "id":"11248d2b-ec2b-44e2-972d-a649b2f8569b",
        "name":"Headphone Gamer"
     }
  ]
```

* Show one product

```shell
  $ curl http://localhost:8080/products/{productId}
```

```
  Example return data.
  
  {  
    "id":"11248d2b-ec2b-44e2-972d-a649b2f8569b",
    "name":"Headphone Gamer"
  }
```

* Stream events of product

```shell
  $ curl http://localhost:8080/products/{productId}/events
```

```
  Example return data.
  
  data:{"product":{"id":"19d1ef84-a559-4d0c-addf-bbb35e5ee1c2","name":"Roteador Intelbras WRN301"},"when":"2019-08-08T15:00:05.255+0000"}

  data:{"product":{"id":"19d1ef84-a559-4d0c-addf-bbb35e5ee1c2","name":"Roteador Intelbras WRN301"},"when":"2019-08-08T15:00:06.254+0000"}

  data:{"product":{"id":"19d1ef84-a559-4d0c-addf-bbb35e5ee1c2","name":"Roteador Intelbras WRN301"},"when":"2019-08-08T15:00:07.256+0000"}

  data:{"product":{"id":"19d1ef84-a559-4d0c-addf-bbb35e5ee1c2","name":"Roteador Intelbras WRN301"},"when":"2019-08-08T15:00:08.255+0000"}

  .
  .
  .
```

