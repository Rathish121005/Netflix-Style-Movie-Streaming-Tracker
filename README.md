# Netflix-like (simplified) Spring Boot backend + Thymeleaf frontend

This is a minimal example project implementing the core requested features:
- User registration and login (passwords stored in plain text — **INSECURE**, see warning below)
- Admin panel to add/edit/delete movies (upload mp4/mkv/avi files stored on server)
- Home page showing movies with subscription logic
- Movie detail page with like/watchlist/stream/download (streaming checks subscription)
- Profile page with profile picture and subscription change
- Search by movie name

## Important security note
You explicitly asked for plain-text passwords. This is **very insecure** for any real app. Never store passwords in plain text in production.

## How to run

1. Install Java 17+ and Maven.
2. Create a MySQL database `touringtalkiesdb` and set credentials in `src/main/resources/application.properties`.
3. Install Maven Dependencies
   ```
   mvn clean install
   '''
5. Build and run:
   ```
   mvn spring-boot:run
   ```
6. Open `http://localhost:8080` in your browser.

Default admin user: none. Create a user and set role to ADMIN directly in database or via a simple SQL:
```sql
INSERT INTO users (username,email, password, role) VALUES ('admin','admin@gmail.com,'admin123','ROLE_ADMIN');
```

Uploaded videos are stored in the folder specified by `app.video.dir` in `application.properties` (default `videos`).

This is a simplified demo and not production-ready.
