# Hospital Management System

A Spring Boot application for managing hospital operations including appointments, patients, doctors, prescriptions, and medical history.

## Technology Stack

- **Java 21**
- **Spring Boot 3.4.4**
- **Spring Security** with JWT authentication
- **MySQL Database**
- **Hibernate/JPA**
- **Maven**

## Railway Deployment Guide

This project is configured for deployment on Railway. Follow these steps to deploy:

### Prerequisites

1. A Railway account (sign up at [railway.app](https://railway.app))
2. A GitHub account (for connecting your repository)

### Deployment Steps

#### 1. Prepare Your Repository

1. Push your code to a GitHub repository
2. Ensure all files are committed and pushed

#### 2. Create a New Project on Railway

1. Go to [railway.app](https://railway.app) and sign in
2. Click "New Project"
3. Select "Deploy from GitHub repo"
4. Choose your repository
5. Railway will automatically detect the Dockerfile

#### 3. Set Up MySQL Database

1. In your Railway project, click "New" → "Database" → "Add MySQL"
2. Railway will create a MySQL database instance
3. Note the connection details (they'll be available as environment variables)

#### 4. Link Database to Application

1. In Railway dashboard, go to your MySQL database service
2. Click on the MySQL service to view its details
3. Railway automatically provides these environment variables to your application service:
   - `MYSQLDATABASE` - Database name
   - `MYSQLUSER` - Database username
   - `MYSQLPASSWORD` - Database password
   - `MYSQLHOST` - Database host
   - `MYSQLPORT` - Database port

4. **Important:** Make sure your application service is linked to the MySQL service:
   - In your application service, go to "Settings"
   - Under "Service Connections", ensure your MySQL service is connected
   - This automatically shares the MYSQL* environment variables

#### 5. Configure Additional Environment Variables

In your Railway application service settings, add the following environment variables:

**Required Environment Variables:**

- `PORT` - **DO NOT SET MANUALLY** - Railway automatically sets this
- `JWT_SECRET` - Secret key for JWT token generation (generate a secure random string)

**Optional Environment Variables:**

- `CORS_ALLOWED_ORIGINS` - Allowed CORS origins for your frontend (default: `http://localhost:4200`)
  - For production, set this to your frontend URL (e.g., `https://your-frontend.railway.app`)
  - For multiple origins, use comma-separated values (e.g., `https://app1.com,https://app2.com`)
- `HIBERNATE_DDL_AUTO` - Hibernate DDL mode (default: `update`)
  - Use `update` for development (auto-creates/updates tables)
  - Use `validate` or `none` for production (requires manual schema management)
- `SHOW_SQL` - Show SQL queries in logs (default: `false`)
  - Set to `true` for debugging, `false` for production

**How to Set Environment Variables:**

1. Go to your application service in Railway dashboard
2. Click on "Variables" tab
3. Add `JWT_SECRET` with a secure random string
4. Add any optional variables if needed
5. Railway will automatically redeploy when variables change

**Example JWT Secret Generation:**

You can generate a secure JWT secret using:
```bash
openssl rand -base64 64
```

Or use an online generator to create a random 64-character hexadecimal string.

**Note:** The database connection is automatically configured using Railway's `MYSQL*` environment variables. No manual database URL configuration is needed!

#### 6. Deploy

1. Railway will automatically build and deploy when you push to your connected branch
2. Monitor the build logs in the Railway dashboard
3. Once deployed, Railway will provide a public URL for your application

#### 7. Access Your Application

1. Railway provides a default domain (e.g., `your-app.railway.app`)
2. You can also set up a custom domain in the Railway dashboard
3. Your API will be available at: `https://your-app.railway.app`

### Local Development

To run locally:

1. Install Java 21 and Maven
2. Set up a local MySQL database
3. Update `application.properties` with your local database credentials
4. Run: `./mvnw spring-boot:run`

Or use Docker Compose:

```bash
docker-compose up
```

### Project Structure

```
src/
├── main/
│   ├── java/com/project/hospitalReport/
│   │   ├── controller/     # REST controllers
│   │   ├── service/         # Business logic
│   │   ├── repository/      # Data access layer
│   │   ├── entity/          # JPA entities
│   │   ├── dto/             # Data transfer objects
│   │   ├── security/        # Security configuration
│   │   └── configuration/   # Spring configuration
│   └── resources/
│       └── application.properties
```

### API Endpoints

- `/auth/signup` - User registration
- `/auth/login` - User login
- `/auth/logout` - User logout
- Additional endpoints for appointments, patients, doctors, prescriptions, etc.

### Security

- JWT-based authentication
- Spring Security for authorization
- Password encryption using BCrypt

### Database

The application uses MySQL with JPA/Hibernate for ORM. Tables are automatically created/updated based on entity definitions.

### Troubleshooting

**Build fails:**
- Check that Java 21 is specified in Dockerfile
- Verify Maven wrapper files are present
- Check build logs in Railway dashboard

**Database connection fails:**
- Verify environment variables are set correctly
- Check that MySQL service is running in Railway
- Ensure database credentials are correct

**Application won't start:**
- Check Railway logs for errors
- Verify PORT environment variable is not manually set (Railway sets it automatically)
- Ensure all required environment variables are configured

### Support

For Railway-specific issues, check [Railway Documentation](https://docs.railway.app)

