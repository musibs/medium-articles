package io.springbytes;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.querydsl.jpa.impl.JPAQuery;

import io.springbytes.model.QPocket;

@DataJpaTest
class SpringRestBasicAuthApplicationTests {

    @Autowired
    private EntityManager entityManager;
    
    @Test
    void givenPocketsCreatedWhenQueriesForCategoryThenGetTheCategoryCount() {
    	QPocket pocket = QPocket.pocket;
    	JPAQuery<QPocket> query1 = new JPAQuery<>(entityManager);
    	query1.from(pocket).where(pocket.category.eq("Food"));
    	assertThat(query1.fetch().size()).isEqualTo(1);
    }
    
    @Test
    void givenPocketsCreatedWhenQueriesForAllCategoriesThenGetAllCategoriesCount() {
    	QPocket pocket = QPocket.pocket;
    	JPAQuery<QPocket> query1 = new JPAQuery<>(entityManager);
    	query1.from(pocket).where(pocket.category.in("LifeStyle", "Food"));
    	assertThat(query1.fetch().size()).isEqualTo(2);
    }

}
