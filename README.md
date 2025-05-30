# Pak_it_app

## About

Pak_it_app is a system with a public-facing site that displays company data, and a backoffice used to manage commodities, clients, and declarations.

## Setup

1. **Database**
    - Create a MariaDB Docker container:
      ```bash
      docker run --detach --name pak-it-db --env MARIADB_USER=pakitapp --env MARIADB_PASSWORD=password --env MARIADB_DATABASE=pak_it_app --env MARIADB_ROOT_PASSWORD=password -p 6306:3306 mariadb:latest
      ```
    - The container must expose port `6306`.

2. **Backend**
    - Run the Spring Boot app.
    - Flyway will automatically handle DB migrations (tables and populate data).

3. **Login**
    - Access the back office:
      ```
      Username: admin
      Password: password
      ```

## Notes

- Make sure the DB is up before starting the app.
- Credentials above are defaults — THEY WILL BE CHANGED IN PRODUCTION.