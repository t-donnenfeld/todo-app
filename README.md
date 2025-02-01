# Todo App - Notes

## Initial Problem

This is a skeleton of Spring Boot application which should be used as a start point to create a working one.
The goal of this task is to create simple REST API  which allows users to manage TODOs. 
The API should allow to create/delete/update TODOs and categories as well as search for user, name, description, deadline and category in any combination. *For example find all todos for an user X where deadline is today and name contains test.* 
The API should also implement basic authorization/authentication: *User X cannot access TODOs of user Y as long as he doesn't have admin role.*

You are free to use any library or testing framework in the project.

Below you may find a proposition of the DB model:

![DB model](DBModel.png)

Once you are ready, please send me **link to your git repository** which contains complete solution

## Proposition

### Limitations

- For the sake of simplicity users are authenticated with Basic Auth on this application.
- Timezones not handled
- Disabled CSRF
- Can only create users with role USER ( no admins )

### Issues encountered

- Gradle management of java modules : cleanly adding generated sources
- Conflict of slf4j implementation : `org.openapitools:openapi-generator` transitively imported `slf4j.simple` which implements SLF4JServiceProvider and collided with default spring-boot-starter-logging SLF4JServiceProvider
