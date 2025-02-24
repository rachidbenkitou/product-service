package com.benkitoucoders.ecommerce.quiz.dao;

import com.benkitoucoders.ecommerce.quiz.entity.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuizCategoryDao extends JpaRepository<QuizCategory, Long>, JpaSpecificationExecutor<QuizCategory> {
}
