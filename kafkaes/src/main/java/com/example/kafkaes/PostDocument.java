package com.example.kafkaes;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDocument {
    @Id
    private Long id;

    private String title;

    private String content;
}
