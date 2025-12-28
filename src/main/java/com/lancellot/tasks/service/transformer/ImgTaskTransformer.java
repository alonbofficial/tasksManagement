package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.api.enums.FileType;
import org.springframework.stereotype.Component;

@Component
public class ImgTaskTransformer implements TaskTransformer {

    // For image recognition use Amazon Textract.

    @Override
    public FileType supports() {
        return FileType.IMG;
    }

    @Override
    public void transform(String linkToS3) {
        System.out.print("ImgTaskTransformer");
    }
}
