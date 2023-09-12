package com.benkitoumiraouycoders.ecommerce.services.inter;

import com.benkitoumiraouycoders.ecommerce.dtos.ImageDto;
import com.benkitoumiraouycoders.ecommerce.handlers.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    List<ImageDto> getImagesByQuery(Long imageId, String imageName, String imageType, String imageFilePath, Long productId, Long categoryId);

    //void uploadImage(MultipartFile file, Long categoryId) throws IOException;

    void uploadCategoryImage(MultipartFile file, Long categoryId) throws IOException;

    void uploadProductImage(MultipartFile file, Long productId) throws IOException;

    void uploadProductImages(List<MultipartFile> images, Long productId) throws IOException;

    ImageDto getImageById(Long id);

    ImageDto addImage(ImageDto imageDto);

    ImageDto updateImage(Long id, ImageDto imageDto);

    ResponseDto deleteImageById(Long id);
    ResponseDto deleteImagesByProductId(Long productId);

    ResponseDto deleteImageByCategoryId(Long categoryId);
}
