# Docker image settings
IMAGE_NAME:=cozy-rentals
IMAGE_TAG:=latest

# Backend-specific variables
BACKEND_DIR:=backend
BACKEND_PORT:=5000

# ------------
# Default target
# ------------
.PHONY: default
default:  
	cat ./Makefile  

# ------------
# Backend targets
# ------------

# Build the backend
.PHONY: backend-build
backend-build:  
	cd $(BACKEND_DIR) && ./mvnw clean package  

# Create a Docker image for the backend
.PHONY: backend-image
backend-image:  
	cd $(BACKEND_DIR) && docker build --no-cache -t  $(IMAGE_NAME):$(IMAGE_TAG) .  

# Run the backend in Docker
.PHONY: backend-run
backend-run:  
	docker run -p $(BACKEND_PORT):$(BACKEND_PORT) $(IMAGE_NAME):$(IMAGE_TAG)  

# Start a bash session in the backend Docker container
.PHONY: backend-run-bash
backend-run-bash:  
	docker run -i -t $(IMAGE_NAME):$(IMAGE_TAG) /bin/bash  

# Build and create an image for the backend
.PHONY: backend-all
backend-all: backend-build \
             backend-image  

# Push the backend Docker image
.PHONY: backend-push
backend-push:  
	docker push $(IMAGE_NAME):$(IMAGE_TAG)  
	docker push $(IMAGE_NAME):latest  

# Stop and remove the backend Docker container
.PHONY: backend-clean
backend-clean:
	docker stop $(IMAGE_NAME):$(IMAGE_TAG)
	docker rm $(IMAGE_NAME):$(IMAGE_TAG)

# ------------
# Backend object creation and manipulation targets
# ------------
.PHONY: backend-create-offer
backend-create-offer:
	curl -X POST http://localhost:5000/api/offers/no-image \
		-u poster:poster \
		-H 'Content-Type: application/json' \
		-d '{ \
				"userId": 2, \
				"title": "Sample offer", \
				"description": "This is a sample offer.", \
				"quantity": 1, \
				"price": 100.00, \
				"timestamp": "2023-06-06T15:00:00Z"\
			}'

.PHONY: backend-create-messages
backend-create-messages:
	curl -X POST http://localhost:5000/api/messages \
		-u poster:poster \
		-H 'Content-Type: application/json' \
		-d '{ \
				"senderId": 2, \
				"recipientId": 3, \
				"offerId": 1, \
				"content": "Hello renter, this is a message from poster.", \
				"timestamp": "2023-06-06T15:00:00Z" \
			}'
	curl -X POST http://localhost:5000/api/messages \
		-u renter:renter \
		-H 'Content-Type: application/json' \
		-d '{ \
				"senderId": 3, \
				"recipientId": 2, \
				"offerId": 1, \
				"content": "Hello poster, this is a reply from renter.", \
				"timestamp": "2023-06-06T16:00:00Z" \
			}'

.PHONY: backend-create-image-offer
backend-create-image-offer:
	curl -X POST http://localhost:5000/api/offers \
		-u poster:poster \
		-F "offerDTO.userId=2" \
		-F "offerDTO.title=Sample multiple image offer" \
		-F "offerDTO.description=This is a sample offer with images." \
		-F "offerDTO.quantity=1" \
		-F "offerDTO.price=100.00" \
		-F "images=@backend/static/cirquit_board_compressed_01.jpg" \
		-F "images=@backend/static/turtle_01.jpg"

.PHONY: backend-create-rental-application
backend-create-rental-application:
	curl -X POST http://localhost:5000/api/rentalApplications \
		-u renter:renter \
		-H 'Content-Type: application/json' \
		-d '{ \
				"offerId": 2 \
			}'

.PHONY: backend-approve-rental-application
backend-approve-rental-application:
	curl -X PUT http://localhost:5000/api/rentalApplications/1/approve \
		-u poster:poster

.PHONY: backend-create-and-approve-rental-application
backend-create-and-approve-rental-application: backend-create-rental-application \
                                              backend-approve-rental-application

.PHONY: backend-create-rental
backend-create-rental:
	curl -X POST http://localhost:5000/api/rentals \
		-u poster:poster \
		-H "Content-Type: application/json" \
		-d '{ \
			"renterId": 2, \
			"offerId": 1, \
			"startDate": "2023-06-06T13:00:00+00:00", \
			"endDate": "2023-06-13T13:00:00+00:00", \
			"isFinished": false \
		}'

.PHONY: backend-delete-offer
backend-delete-offer:
	curl -X DELETE http://localhost:5000/api/offers/2 \
		-u admin:admin

.PHONY: backend-update-images
backend-update-images:
	curl -X PATCH http://localhost:5000/api/offers/3/images \
		-u poster:poster \
		-F "imageIdsToDelete=2" \
		-F "images=@backend/static/turtle_01.jpg" \
		-F "images=@backend/static/turtle_02.jpg" \
		-F "images=@backend/static/cirquit_board_compressed_02.jpg"


.PHONY: backend-create-all
backend-create-all: backend-create-offer \
                    backend-create-messages \
                    backend-create-image-offer \
                    backend-create-and-approve-rental-application \
                    backend-create-rental

# ------------
# Docker Compose targets
# ------------

# Start the services defined in the Docker Compose configuration in the background
.PHONY: compose-up
compose-up:
	docker-compose up -d

# Stop and remove the services defined in the Docker Compose configuration
.PHONY: compose-down
compose-down:
	docker-compose down

# Build the services defined in the Docker Compose configuration
.PHONY: compose-build
compose-build:
	docker-compose build

# Rebuild and restart the services defined in the Docker Compose configuration
.PHONY: compose-rebuild
compose-rebuild: compose-down \
                 compose-build \
                 compose-up
