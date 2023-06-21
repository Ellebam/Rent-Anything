# Backend

This app was created with Bootify.io - tips on working with the code [can be found here](https://bootify.io/next-steps/). Feel free to contact us for further questions.

## Development

During development it is recommended to use the profile `local`. In IntelliJ, `-Dspring.profiles.active=local` can be added in the VM options of the Run Configuration after enabling this property in "Modify options".

Update your local database connection in `application.yml` or create your own `application-local.yml` file to override settings for development.

After starting the application it is accessible under `localhost:8080`.

## Build

The application can be built using the following command:

```
mvnw clean package
```

The application can then be started with the following command - here with the profile `production`:

```
java -Dspring.profiles.active=production -jar ./target/backend-0.0.1-SNAPSHOT.jar
```

## Further readings

* [Maven docs](https://maven.apache.org/guides/index.html)  
* [Spring Boot reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  
* [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)  



Ok that worked thank you. Now I want to test the new functionalities regarding the new Endpoints. First I want to try to update the images on an offer. I have created an offer with images that looks like this in the database:

	{
		"id": 2,
		"title": "Sample multiple image offer",
		"description": "This is a sample offer with images.",
		"location": null,
		"price": 100.00,
		"quantity": null,
		"imageUrls": [
			"/app/images/2/cirquit_board_compressed_01.jpg",
			"/app/images/2/turtle.jpg"
		],
		"userId": 2,
		"deactivated": false
	}

I used the following curl request in my Makefile for that:


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
		-F "images=@backend/static/turtle.jpg"

I want to make use of this endpoint that we built:

    @PatchMapping("/{id}/images")
    @PreAuthorize("@offerService.get(#id).getUserId() == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> addOrDeleteImages(@PathVariable(name = "id") final Long id, 
                                                @ModelAttribute OfferRequestDTO offerRequestDTO) {
        List<MultipartFile> imagesToAdd = offerRequestDTO.getImages();
        List<Long> imageIdsToDelete = offerRequestDTO.getImageIdsToDelete();

        try {
            if (imagesToAdd != null && !imagesToAdd.isEmpty()) {
                offerImageService.saveImages(id, imagesToAdd);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while saving images", e);
        }

        if (imageIdsToDelete != null && !imageIdsToDelete.isEmpty()) {
            offerImageService.deleteImages(id, imageIdsToDelete);
        }

        offerImageService.cleanupImages(id, offerRequestDTO.getImageIdsToDelete());

        return ResponseEntity.ok().build();
    }


help me write a nice curl request for my makefile to 