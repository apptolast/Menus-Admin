# ============================================
# Dockerfile for MenuAdmin (Kotlin Multiplatform - WASM)
# Multi-stage build for optimized production image
# ============================================

# Stage 1: Build the WASM application
FROM gradle:8.10-jdk21 AS builder

# Build argument for API URL (passed from CI/CD)
ARG API_BASE_URL
ENV API_BASE_URL=${API_BASE_URL}

# Install libatomic1 for Node.js v25+ (required by Kotlin/WASM)
USER root
RUN apt-get update && apt-get install -y --no-install-recommends \
    libatomic1 \
    && rm -rf /var/lib/apt/lists/*
USER gradle

WORKDIR /app

# Configure JVM/Gradle memory
ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m"

# Copy Gradle configuration files first (for better caching)
COPY --chown=gradle:gradle gradle/ gradle/
COPY --chown=gradle:gradle gradlew gradlew.bat settings.gradle.kts build.gradle.kts gradle.properties ./

# Copy the compose app module
COPY --chown=gradle:gradle composeApp/ composeApp/

# Make gradlew executable
RUN chmod +x gradlew

# Create local.properties with API_BASE_URL from build argument
RUN echo "API_BASE_URL=${API_BASE_URL}" > local.properties && \
    echo "Created local.properties with API_BASE_URL"

# Upgrade Yarn lock files (required after dependency changes)
RUN ./gradlew kotlinUpgradeYarnLock kotlinWasmUpgradeYarnLock --no-daemon

# Build the WASM distribution
RUN ./gradlew :composeApp:wasmJsBrowserDistribution --no-daemon --stacktrace

# Stage 2: Serve with Nginx
FROM nginx:alpine

RUN apk add --no-cache tzdata

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Copy the built WASM application
COPY --from=builder /app/composeApp/build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html/

# Create a simple health check endpoint
RUN echo "OK" > /usr/share/nginx/html/health

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost/health || exit 1

CMD ["nginx", "-g", "daemon off;"]
