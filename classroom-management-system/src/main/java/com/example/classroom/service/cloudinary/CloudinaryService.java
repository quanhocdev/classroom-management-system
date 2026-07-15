package com.example.classroom.service.cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {


    private final Cloudinary cloudinary;


    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }



    public UploadResult uploadImage(
            MultipartFile file,
            String folder
    ) {

        try {

            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder
                    )
            );


            String url = result.get("secure_url").toString();

            String publicId = result.get("public_id").toString();


            return new UploadResult(url, publicId);


        } catch (IOException e) {

            throw new RuntimeException(
                    "Upload image failed",
                    e
            );
        }

    }

}