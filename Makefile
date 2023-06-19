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

## Backend object creation and manipulation
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

backend-create-image-offer:
	curl -X POST http://localhost:5000/api/offers \
		-u poster:poster \
		-F "offerDTO.userId=2" \
		-F "offerDTO.title=Sample multiple image offer" \
		-F "offerDTO.description=This is a sample offer with images." \
		-F "offerDTO.quantity=1" \
		-F "offerDTO.price=100.00" \
		-F "images=@backend/static/cirquit_board_compressed_01.jpg" \
		-F "images=@backend/static/turtle.jpg"

backend-create-rental-application:
	curl -X POST http://localhost:5000/api/rentalApplications \
		-u renter:renter \
		-H 'Content-Type: application/json' \
		-d '{ \
				"applicantId": 2, \
				"offerId": 1 \
			}'

backend-approve-rental-application:
	curl -X PUT http://localhost:5000/api/rentalApplications/1/approve \
		-u poster:poster

backend-create-and-approve-rental-application: backend-create-rental-application backend-approve-rental-application

	
backend-create-all: backend-create-offer backend-create-messages backend-create-image-offer backend-create-and-approve-rental-application

# Docker Compose targets
compose-up:
	docker-compose up -d

compose-down:
	docker-compose down

compose-build:
	docker-compose build

compose-rebuild: compose-down compose-build compose-up

