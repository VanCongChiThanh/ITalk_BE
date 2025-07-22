Maitalk

Maitalk is a website for practicing speaking English with AI and real-time conversation.

It helps users practice daily English speaking, receive AI scoring and feedback, join speaking challenges, view leaderboards, post articles, upload English short videos (reels) for qualified users, and chat in real-time community groups.

Features:

Daily Speaking Challenges with AI scoring (Gemini)

Leaderboard tracking speaking performance

Real-time Group Chat using WebSocket

Post Articles with comments and likes

Post English Reels for qualified users

AI Feedback on pronunciation, fluency, vocabulary

File Uploads via Cloudinary or Google Cloud Storage

Push Notifications using RabbitMQ and WebSocket

Email Notifications for verification and updates

JWT and OAuth 2.0 (Google, Facebook) Authentication

Tech Stack:
Spring Boot with multi-module structure, RESTful APIs, WebSocket
Spring Security with JWT and OAuth 2.0
Spring Data JPA with PostgreSQL
RabbitMQ for push notifications
Cloudinary or Google Cloud Storage for file handling
Gemini API for AI speech feedback
Spring Mail for email services
Docker and Docker Compose for containerization

Project Structure:
maitalk/

ai-service/

chat-service/

common-service/

email-service/

file-service/

notification-service/

security-service/

talk-service/

user-service/

web/

Quick Start with Docker Compose:

Clone the repository:
git clone https://github.com/yourusername/maitalk.git
cd maitalk

Configure environment variables using application.yml or .env files per service:
POSTGRES_URL
POSTGRES_USER
POSTGRES_PASSWORD
RABBITMQ_HOST
RABBITMQ_USER
RABBITMQ_PASSWORD
CLOUDINARY_URL
GEMINI_API_KEY
GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET
FACEBOOK_CLIENT_ID
FACEBOOK_CLIENT_SECRET
JWT_SECRET

Run with Docker Compose:
docker compose up --build

Access the application:
http://localhost:5173
or
http://localhost:8080

Contributing:
Pull requests and contributions are welcome. Feel free to open issues to improve Maitalk.

License:
MIT
