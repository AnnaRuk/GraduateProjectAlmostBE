package de.ait.gp.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.ait.gp.dto.StandardResponseDto;
import de.ait.gp.models.FileInfo;
import de.ait.gp.repositories.FilesInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilesService {

    private final AmazonS3 amazonS3;

    private final FilesInfoRepository filesInfoRepository;

    @Transactional
    @SneakyThrows
    public StandardResponseDto upload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        String extension;

        if (originalFileName != null) {
            extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        } else {
            throw new IllegalArgumentException("null original file name");
        }

        String uuid = UUID.randomUUID().toString();
        String newFileName = uuid + "." + extension;

        InputStream inputStream = file.getInputStream();



        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());

        PutObjectRequest request =
                new PutObjectRequest("kita-connection-files", "kindergarten-images/" + newFileName, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(request);

        String link = amazonS3.getUrl("kita-connection-files", "kindergarten-images/" + newFileName).toString();

        FileInfo fileInfo = FileInfo.builder()
                .link(link)
                .build();

        filesInfoRepository.save(fileInfo);

        return StandardResponseDto.builder()
                .message(link)
                .build();
    }
}
