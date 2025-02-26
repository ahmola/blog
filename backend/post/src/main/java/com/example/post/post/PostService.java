package com.example.post.post;

import com.example.post.user.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserClient userClient;

    public void DTOtoPost(Post post, PostDTO postDTO){
        Arrays.stream(PostDTO.class.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("get"))
                .forEach(getter -> {
                    try {
                        Object value = getter.invoke(postDTO);
                        if (value != null){
                            String fieldName = getter.getName().substring(3);
                            String setterName = "set"+fieldName;

                            Method setter = Post.class.getMethod(setterName, getter.getReturnType());
                            setter.invoke(post, value);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
    }

    public List<Post> getAllPostsByUserId(long userId) {
        return postRepository.findByUserId(userId);
    }

    public Optional<Post> getPostById(long postId) {
        return postRepository.findById(postId);
    }

    public Post save(PostDTO postDTO) {
        Post post = new Post();
        DTOtoPost(post, postDTO);
        return postRepository.save(post);
    }

    public Post fixPost(PostDTO postDTO) {
        Post post = new Post();
        DTOtoPost(post, postDTO);
        return postRepository.save(post);
    }

    public Boolean deleteById(long postId) {
        try {
            postRepository.deleteById(postId);
        }catch (Exception e){
            throw new RuntimeException("Error Occurs during deleting post : " + postId);
        }
        return true;
    }
}
