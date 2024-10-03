package com.saseum.myblog.service;

import com.saseum.myblog.domain.Article;
import com.saseum.myblog.dto.article.AddArticleRequest;
import com.saseum.myblog.dto.article.UpdateArticleRequest;
import com.saseum.myblog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article save(AddArticleRequest request, String userName) {
        return articleRepository.save(request.toEntity(userName));
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));
    }

    public void delete(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));

        authorizeArticleAuthor(article);
        articleRepository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("Not authorized.");
        }
    }
}
