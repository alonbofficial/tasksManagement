package com.lancellot.tasks.service.transformer;

import com.lancellot.tasks.api.enums.FileType;
import org.springframework.stereotype.Component;

@Component
public class TxtTaskTransformer implements TaskTransformer {

    // For work with CSV or TXT use any libraries by your preference

    @Override
    public FileType supports() {
        return FileType.TXT;
    }

    @Override
    public void transform(String linkToS3) {
        System.out.print("TxtTaskTransformer");
    }
}
