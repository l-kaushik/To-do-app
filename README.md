# To-Do Workspace Application

A full-stack task management application built with **Spring Boot**, **React (Vite)**, and **PostgreSQL**, containerized using **Docker Compose** for easy local setup.

---

## Tech Stack

### Backend
- Java 25
- Spring Boot
- Spring Security (JWT, Cookies)
- JPA / Hibernate
- PostgreSQL

### Frontend
- React
- Vite
- Axios
- ReactRouter
- ReactQuery

### Infrastructure
- Docker
- Docker Compose

---

## Prerequisites

Make sure you have the following installed:

- **Docker** (v28+)
- **Docker Compose** (v2+)

No local Java, Node.js, or PostgreSQL installation is required.

---

## Project Structure

## Environment Configuration

Create a `.env` file in the root directory (same level as `docker-compose.yml`)  
by copying the example file:

```
cp .env.example .env
```

**Note:**
Frontend and backend communicate via exposed localhost ports.

```
docker compose up --build
```
Docker will:
- build frontend and backend images
- start PostgreSQL
- expose required port

## Accessing the Application

| Service     | URL                                                                  |
| ----------- | -------------------------------------------------------------------- |
| Frontend    | [http://localhost:5173](http://localhost:5173)                       |
| Backend API | [http://localhost:8080](http://localhost:8080)                       |
| Swagger UI  | [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui) |

**NOTE**: Make sure to login first before accessing swagger-ui.

## Stopping the Application

```
docker compose down
```
To remove volumes as well:
```
docker compose down -v
```