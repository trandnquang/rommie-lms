# ==========================================================
# STAGE 1: Build the React Application (Vite)
# ==========================================================
FROM node:24-alpine AS builder
WORKDIR /app

# Install dependencies using clean install for reliability
COPY package*.json ./
RUN npm ci

# Copy the rest of the application and build static files
COPY . .
RUN npm run build

# ==========================================================
# STAGE 2: Serve via Nginx
# ==========================================================
FROM nginx:alpine

# Copy built static files to Nginx default public folder
COPY --from=builder /app/dist /usr/share/nginx/html

# Expose HTTP port
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]