# Tasks Service

Spring Boot REST API that processes task lists from CSV, TXT, and image files in AWS S3. Uses AWS Textract for OCR and stores extracted tasks in MongoDB. GraalVM native image ready.

## Overview

This service automatically transforms task documents uploaded to AWS S3 into structured data. It supports multiple file formats and uses AWS Textract for intelligent text extraction from images.

## Features

- **Multi-format Support**: Processes CSV, TXT, and image files (PNG/JPG)
- **AWS Integration**:
  - Retrieves files from AWS S3
  - Uses AWS Textract for OCR text extraction from images
- **Intelligent Parsing**: Extracts numbered task lists from various text formats
- **RESTful API**: Simple endpoints for triggering transformations and retrieving tasks
- **MongoDB Storage**: Persists extracted tasks with their metadata
- **GraalVM Native Image Ready**: Configured for compilation to native executable

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.9**
  - Spring Data MongoDB
  - Spring Web
  - Spring Validation
- **AWS SDK 2.x**
  - S3 Client
  - Textract Client
- **MongoDB** - Document storage
- **OpenCSV** - CSV parsing
- **ModelMapper** - DTO mapping
- **Lombok** - Boilerplate reduction
- **GraalVM Native Image** - Native compilation support

## Architecture

The service implements a clean, extensible architecture with the following components:

- **Controllers**: Handle HTTP requests and responses
- **Services**: Contain business logic and orchestrate transformers
- **Transformers**: Process different file types (CSV, TXT, Image)
- **Repositories**: Manage data persistence with MongoDB
- **DTOs**: Transfer data between layers

### Design Patterns

This application implements the **Strategy Pattern** for file processing:

- **Strategy Interface**: `TaskTransformer` defines the contract for all file processors
- **Concrete Strategies**:
  - `CsvTaskTransformer` - Handles CSV files using OpenCSV
  - `TxtTaskTransformer` - Handles plain text files
  - `ImageTaskTransformer` - Handles images using AWS Textract OCR
- **Context**: The service dynamically selects the appropriate transformer based on file type at runtime

**Benefits:**
- Easy to add new file formats without modifying existing code
- Follows the Open/Closed Principle (open for extension, closed for modification)
- Each transformer is independently testable and maintainable

**Additional Patterns Used:**
- **Template Method** - `BaseTransformerTask` provides common functionality (download, parse, save)
- **Repository Pattern** - Data access abstraction via `TaskRepository`
- **DTO Pattern** - Separates domain models from API contracts
- **Dependency Injection** - Spring manages component lifecycle and dependencies

## API Endpoints

### Transform Tasks
```
POST /tasks/transform
```
Triggers task extraction from a file in S3.

**Request Body:**
```json
{
  "linkToS3": "tasks.csv",
  "fileType": "CSV"
}
```

### Get Tasks by Filename
```
GET /tasks/{fileName}
```
Retrieves extracted tasks for a specific file.

**Response:**
```json
{
  "fileName": "tasks.csv",
  "taskItems": [
    {
      "number": 1,
      "description": "Clean the living room"
    }
  ]
}
```

## File Format Requirements

### CSV Format
```csv
Number,Task
1,Mow the lawn
2,Clean the windows
```

### TXT Format
```
1. Mow the lawn
2. Clean the windows
```

### Image Format
Images should contain clearly readable numbered task lists. Supported formats: PNG, JPG, JPEG.

## Configuration

Required environment variables or `application.properties`:
```properties
aws.s3.bucket.name=your-bucket-name
aws.region=us-east-1
spring.data.mongodb.uri=mongodb://localhost:27017/tasksdb
```

## AWS Permissions Required

The service requires the following IAM permissions:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "textract:DetectDocumentText"
      ],
      "Resource": "*"
    }
  ]
}
```

## Running the Application

### Prerequisites
- JDK 21
- MongoDB running on localhost:27017
- AWS credentials configured
  - `AWS_ACCESS_KEY_ID`
  - `AWS_SECRET_ACCESS_KEY`
- S3 bucket with task files

### AWS Credentials Setup

Set your AWS credentials as system environment variables:

**Windows (PowerShell):**
```powershell
$env:AWS_ACCESS_KEY_ID = "your-access-key-id"
$env:AWS_SECRET_ACCESS_KEY = "your-secret-access-key"
```

### Standard Run
```bash
.\gradlew.bat bootRun
```

### Build
```bash
.\gradlew.bat build
```

### Native Image Build (GraalVM)
```bash
.\gradlew.bat nativeCompile
```

## Error Handling

The service includes custom exception handling:
- `NotFoundException` - When task document or file is not found
- `UnsupportedTypeException` - When file type is not supported
- Global exception handler provides consistent error responses

## Future Enhancements

- Support for additional file formats (PDF, Excel)
- Batch processing of multiple files
- Webhooks for S3 event notifications
- Task completion tracking
- User authentication and authorization

## Documentation

<img width="1495" height="2431" alt="Untitled Diagram" src="https://github.com/user-attachments/assets/ed76ecb6-f0b8-49c3-867e-362feea36268" />

