package org.badges.it;


import org.badges.api.domain.ImportBadgeAssignment;
import org.badges.api.domain.news.BadgeNewsDto;
import org.badges.api.domain.news.NewsDto;
import org.badges.db.Badge;
import org.badges.db.News;
import org.badges.db.User;
import org.badges.db.repository.BadgeRepository;
import org.badges.db.repository.NewsRepository;
import org.badges.security.RequestContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BadgeAssignmentTest {

    @Autowired
    private RestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private BadgeRepository badgeRepository;

    @MockBean
    private RequestContext requestContext;


    @Test
    public void shouldAssignBadge() {
        // given
        ImportBadgeAssignment assignment = new ImportBadgeAssignment().setBadgeId(1L)
                .setComment("new cIomment")
                .addUsers(2L, 3L);
        Mockito.when(requestContext.getCurrentUser()).thenReturn(new User().setId(4L));

        // when
        ResponseEntity<NewsDto> response = restTemplate.postForEntity("http://localhost:" + port +
                "/api/badges/assign", assignment, NewsDto.class);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful(), is(true));
        NewsDto body = response.getBody();
        News dbNews = newsRepository.findOne(body.getId());
        assertThat(body.getComment(), is(dbNews.getComment()));
        assertThat(body.getToUsers().size(), is(dbNews.getToUsers().size()));
        assertThat(body.getNewsType(), is(dbNews.getNewsType()));

        Badge badge = badgeRepository.getByDeletedFalseAndId(1l);
        Map reason = (Map) body.getReason();
        assertThat(reason.get("id").toString(), is(badge.getId().toString()));
    }
}
