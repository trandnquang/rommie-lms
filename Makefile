# Makefile for Roomie Lodging Management System

# Variables
DOCKER_COMPOSE = docker compose
PROJECT_NAME = roomie-lms

.PHONY: help up down logs clean db-shell

# Default target
help:
	@echo "Roomie LMS - Deployment Automation Commands"
	@echo "--------------------------------------------------------"
	@echo "  make up       : Build and start all services in detached mode"
	@echo "  make down     : Stop and remove all containers"
	@echo "  make logs     : View output logs from all containers"
	@echo "  make clean    : Stop containers and wipe project images/volumes"
	@echo "  make db-shell : Access the PostgreSQL interactive terminal (psql)"

up:
	$(DOCKER_COMPOSE) up -d --build
	@echo "--------------------------------------------------------"
	@echo "Roomie LMS is starting up!"
	@echo "Frontend Access: http://localhost"
	@echo "Backend API:     http://localhost:8080"

down:
	$(DOCKER_COMPOSE) down

logs:
	$(DOCKER_COMPOSE) logs -f

clean:
	$(DOCKER_COMPOSE) down -v --rmi all --remove-orphans

db-shell:
	docker exec -it roomie-db psql -U roomie_admin -d roomie_lms_db