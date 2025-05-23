package com.example.comment.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findBySubject(String subject);

    List<Comment> findByUserId(Long id);

    List<Comment> findByPostId(Long postId);
}
