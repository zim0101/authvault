# [ Authvault ] - Spring Boot Authentication Boilerplate

A boilerplate Spring Boot application for implementing authentication with features such as social login, SSO, and user role management. 
This project is built with Spring Boot, Spring Security, Spring Data JPA, PostgreSQL, and Thymeleaf.


## Features
### Completed
1. Basic sign in and sign up

### TODO
1. Social Login (Google)
2. Single Sign On (SSO)
3. Admin and User Dashboard
4. User, Role and Permission Management

## Requirements
- **Docker:** You only need docker and docker compose.

## How To Run?
### Clone the repository
```shell
git clone https://github.com/zim0101/authvault.git
```
### To run and go through the project
```shell
docker compose up --build -d
```

### For development:

### Clone the repository
```shell
git clone https://github.com/zim0101/authvault.git
```

Change the .env.example to .env
```shell
mv .env.example .env
```

Update your .env file like the below code, but please use appropiate database name and please...please use a strong 
password :-) 
```
POSTGRES_DB=authvault_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=root
```
Create a ```application-dev.properties``` file and update the values. For example:

```
spring.application.name=AuthVault

# SERVER
server.port=${SERVER_PORT:5000}
encryption.secret-key=${ENCRYPTION_SECRET_KEY:MySecretKey12345}

# DATABASE
spring.jpa.database=POSTGRESQL
spring.sql.init.platform=postgres
spring.datasource.url=jdbc:postgresql://${SPRING_DATASOURCE_HOST:localhost}:${SPRING_DATASOURCE_PORT:5432}/${POSTGRES_DB:authvault_db}
spring.datasource.username=${POSTGRES_USER:postgres}
spring.datasource.password=${POSTGRES_PASSWORD:root}

# Always initialize the DB with data.sql.
# TODO: Turn off in production
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.mvc.hiddenmethod.filter.enabled=true

logging.level.org.springframework.web.socket=DEBUG

```

Run the project
```shell
docker compose up --build -d
```

After changing the source code or adding new features, build the spring boot app.
```shell
docker compose build authvault-app
```

And finally run the project
```shell
docker compose up
```

## Logs
```shell
docker compose logs authvault-app
```

## Contributing
Contributions are welcome! Here's how you can help:

1. Fork the repository.
2. Create a new feature branch: git checkout -b feature/your-feature-name.
3. Commit your changes: git commit -m 'Add some feature'.
4. Push to the branch: git push origin feature/your-feature-name.
5. Open a pull request.

Please ensure your code follows proper coding conventions and is well-tested.

## Roadmap
1. [ ] Add Social Login (Google).
2. [ ] Implement Single Sign-On (SSO).
3. [ ] Create admin and user-specific dashboards.
4. [ ] Build a user, role, and permission management module.
5. [ ] Add more test coverage, including security and integration tests.
6. [ ] Improve logging and exception handling.

## License
This project is licensed under the MIT License