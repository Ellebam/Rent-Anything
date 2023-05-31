IMAGE_NAME:=cozy-rentals
IMAGE_TAG:=latest

# Backend-specific variables
BACKEND_DIR:=backend
BACKEND_PORT:=5000

default:  
	cat ./Makefile  

## Backend targets
backend-build:  
	cd $(BACKEND_DIR) && ./mvnw clean package  

backend-image:  
	cd $(BACKEND_DIR) && docker build --no-cache -t  $(IMAGE_NAME):$(IMAGE_TAG) .  

backend-run:  
	docker run -p $(BACKEND_PORT):$(BACKEND_PORT) $(IMAGE_NAME):$(IMAGE_TAG)  

backend-run-bash:  
	docker run -i -t $(IMAGE_NAME):$(IMAGE_TAG) /bin/bash  

backend-all: backend-build backend-image  

backend-push:  
	docker push $(IMAGE_NAME):$(IMAGE_TAG)  
	docker push $(IMAGE_NAME):latest  

backend-clean:
	docker stop $(IMAGE_NAME):$(IMAGE_TAG)
	docker rm $(IMAGE_NAME):$(IMAGE_TAG)


# Docker Compose targets
compose-up:
	docker-compose up -d

compose-down:
	docker-compose down

compose-build:
	docker-compose build

compose-rebuild: compose-down compose-build compose-up

