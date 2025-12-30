package com.lancellot.tasks.api.dto;

import com.lancellot.tasks.api.enums.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record  TransformRequest(

        @NotBlank(message = "S3 link cannot be blank")
        @Pattern(
                regexp = "^[a-zA-Z0-9._-]+\\.(csv|txt|png|jpg|jpeg)$",
                message = "Invalid file name format. Must be a valid filename with extension (csv, txt, png, jpg, jpeg)"
        )
        String linkToS3,

        @NotNull(message = "File type cannot be null")
        FileType fileType
){}
