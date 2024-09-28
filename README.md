# [ Authvault ] 
## Spring Boot Authentication Boilerplate

A boilerplate Spring Boot application for implementing authentication with features such as social login, SSO, and user role management. 
This project is built with Spring Boot, Spring Security, Spring Data JPA, PostgresSQL, and Thymeleaf.


## Features
- [x] Add form based login registration.
- [x] Add Social Login (Google, GitHub).
- [ ] Implement Email verification
- [ ] Implement 2FA
- [x] Create admin dashboard.
- [ ] Build a user, role, and permission management module.
- [ ] Add more test coverage, including security and integration tests.
- [ ] Improve logging and exception handling.
- [ ] Implement Single Sign-On (SSO).

## Requirements
- **Docker:** You only need docker and docker compose.

## How To Run?

### To run and go through the project
```shell
git clone https://github.com/zim0101/authvault.git
```
```shell
docker compose up --build -d
```

### For development:

Clone the repository
```shell
git clone https://github.com/zim0101/authvault.git
```

Get into the repository
```shell
cd authvault
```
Change the .env.example to .env
```shell
cp .env.example .env
```

Before running the application, update your .env file with the following environment variables. 
Make sure to use an appropriate database name and, most importantly, 
choose a strong password! 🚨 Don't forget: .env should always remain in .gitignore—never commit sensitive information!

Many of the variables below come with default values. However, you'll need to customize certain fields according 
to your setup:

* **Database:** Update `POSTGRES_USER`, `POSTGRES_DB`, and `POSTGRES_PASSWORD` with your database credentials.
* **Server Port:** Modify the `SERVER_PORT` as needed. 
* **Encryption Key:** Set a strong `ENCRYPTION_SECRET_KEY` for secure data encryption.
* **OAuth Credentials:** Get your Google and GitHub client IDs and secrets from their respective developer accounts and 
  fill them in `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `GITHUB_CLIENT_ID`, `GITHUB_CLIENT_SECRET`.
```
APPLICATION_NAME=AuthVault
ENCRYPTION_SECRET_KEY=MySecretKey12345
SERVER_PORT=5000
SPRING_DATASOURCE_HOST=postgres-db
POSTGRES_PORT=5432
POSTGRES_DB=authvault_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
SPRING_DATASOURCE_URL=jdbc:postgresql://${SPRING_DATASOURCE_HOST}:${POSTGRES_PORT}/${POSTGRES_DB}
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
GITHUB_CLIENT_ID=
GITHUB_CLIENT_SECRET=
```
You will also have a ```application-dev.properties``` file. Use this file only for development.

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

Now open your browser and visit: ```http://localhost:5000/``` or replace the port with the one you used in `.env`

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

## License
This project is licensed under the MIT License