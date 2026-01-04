# To-Do Workspace Application

A full-stack task management application designed to manage tasks across multiple workspaces. Built using **Spring Boot** for the backend, **React (Vite)** for the frontend, and **PostgreSQL** for persistent storage. The application is fully containerized with **Docker Compose** for easy local development and testing.

## Live Demo
🔗 https://todo.lokeshkaushik.in/

## Project Demo
[![Watch Demo on YouTube](https://img.shields.io/badge/Watch%20Demo-YouTube-red?logo=youtube&logoColor=white)](https://youtu.be/NtlmArhN-4I)

> **Note:**  
> The backend is hosted on a **Render free-tier instance**, which may enter a sleep state during periods of inactivity. Initial startup can take **30–50 seconds**.  
> On mobile devices, a page refresh may be required after the backend becomes active.

## Local Development
Instructions for running the project locally using Docker Compose are provided in the **Prerequisites for Local Testing** section below.

---

## Tech Stack

### Backend
![Java](https://img.shields.io/badge/Java-25-007396?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-JPA-59666C?logo=hibernate&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-336791?logo=postgresql&logoColor=white)

### Frontend
![React](https://img.shields.io/badge/React-19-61DAFB?logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-Build-646CFF?logo=vite&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-HTTP-5A29E4)
![React Router](https://img.shields.io/badge/React%20Router-Routing-CA4245?logo=reactrouter&logoColor=white)
![React Query](https://img.shields.io/badge/React%20Query-Data%20Fetching-FF4154?logo=reactquery&logoColor=white)

### Infrastructure
![Docker](https://img.shields.io/badge/Docker-Container-2496ED?logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker%20Compose-Orchestration-2496ED?logo=docker&logoColor=white)


---
## Prerequisites For Local Testing

Make sure you have the following installed:

![Docker](https://img.shields.io/badge/Docker-v28+-2496ED?logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker%20Compose-v2+-2496ED?logo=docker&logoColor=white)

No local Java, Node.js, or PostgreSQL installation is required.

---

## How To Test Locally

Create a `.env` file in the root directory (same level as `docker-compose.yml`)  
by copying the example file:<br>
*use copy instead of cp for windows*

```
cp .env.example .env
```
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