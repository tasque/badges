package org.badges.it;


import org.badges.api.domain.ImportBadgeAssignment;
import org.badges.api.domain.NewsDto;
import org.badges.db.Company;
import org.badges.db.News;
import org.badges.db.repository.NewsRepository;
import org.badges.security.TenantContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BadgeAssignmentTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NewsRepository newsRepository;

    @MockBean
    private TenantContext tenantContext;


    @Test
    public void shouldAssignBadge() {
        // given
        ImportBadgeAssignment assignment = new ImportBadgeAssignment().setBadgeId(1L)
                .setComment("new comment")
                .addEmployees(2L, 3L)
                .setAssignerId(1L);
        when(tenantContext.getCurrentCompany()).thenReturn(new Company().setId(1L));

        // when
        ResponseEntity<NewsDto> response = restTemplate.postForEntity("/api/badges/assign", assignment, NewsDto.class);

        // then
        NewsDto body = response.getBody();
        News dbNews = newsRepository.findById(body.getId()).get();
        assertThat(body.getComment(), is(dbNews.getComment()));
        assertThat(body.getToEmployees().size(), is(dbNews.getToEmployees().size()));
        assertThat(body.getNewsType(), is(dbNews.getNewsType()));
    }
}
