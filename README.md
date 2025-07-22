# Maitalk

Maitalk is a website for practicing speaking English with AI and real-time conversation.

It helps users:
- Practice daily English speaking
- Receive AI scoring and feedback
- Join speaking challenges
- View leaderboards
- Post articles
- Upload English short videos (reels) for qualified users
- Chat in real-time community groups

## Features

- Daily Speaking Challenges with AI scoring (Gemini)
- Leaderboard tracking speaking performance
- Real-time Group Chat using WebSocket
- Post Articles with comments and likes
- Post English Reels for qualified users
- AI Feedback on pronunciation, fluency, vocabulary
- File Uploads via Cloudinary or Google Cloud Storage
- Push Notifications using RabbitMQ and WebSocket
- Email Notifications for verification and updates
- JWT and OAuth 2.0 (Google, Facebook) Authentication

## Tech Stack

- Spring Boot (multi-module, RESTful APIs, WebSocket)
- Spring Security (JWT, OAuth 2.0)
- Spring Data JPA with PostgreSQL
- WebSocket (Spring Messaging)
- RabbitMQ
- Cloudinary or Google Cloud Storage
- Gemini API (AI speech feedback)
- Spring Mail
- Docker and Docker Compose


## Quick Start with Docker Compose

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/maitalk.git
   cd maitalk
2 .Configure environment variables using application.yml or .env files per service
3.Run with Docker Compose:
docker compose up --build
<img width="1823" height="822" alt="image" src="https://github.com/user-attachments/assets/eedb0f75-ed9a-41b5-9135-0ad4578e7c87" />

