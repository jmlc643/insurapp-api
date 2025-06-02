package com.upao.insurApp.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class QrCodeService {

    @Autowired private Cloudinary cloudinary;

    public QrCodeService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String generateQRAndUpload(String data, String publicId) throws Exception {
        // Generate QR as BufferedImage
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 300, 300);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);

        // Temporarily save it as a file
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", outputStream);
        byte[] qrBytes = outputStream.toByteArray();

        // Upload to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(qrBytes, ObjectUtils.asMap(
                "resource_type", "image",
                "public_id", publicId,
                "use_filename", false,
                "unique_filename", false,
                "folder", "qr-codes"
        ));

        // Return the QR to the URL
        return uploadResult.get("secure_url").toString();
    }


}

