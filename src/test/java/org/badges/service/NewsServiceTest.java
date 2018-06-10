package org.badges.service;

import com.google.common.collect.Sets;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.Company;
import org.badges.db.Employee;
import org.badges.db.News;
import org.badges.db.NewsType;
import org.badges.db.repository.NewsRepository;
import org.badges.security.RequestContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NewsServiceTest {

    @InjectMocks
    private NewsService newsService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private RequestContext requestContext;

    @Test
    public void shouldSaveNewsProperly() {
        // given
        BadgeAssignment badgeAssignment = new BadgeAssignment().setBadge(new Badge().setId(1L))
                .setComment("comment")
                .setAssigner(new Employee().setId(1L))
                .setToEmployees(Sets.newHashSet(new Employee().setId(2L), new Employee().setId(3L)))
                .setId(1L);
        when(newsRepository.save(any(News.class))).then(invocation -> invocation.getArguments()[0]);
        when(requestContext.getCurrentTenant()).thenReturn(new Company().setId(4L));

        // when
        News result = newsService.prepareNews(badgeAssignment);

        // then
        assertThat(result.getComment(), is("comment"));
        assertThat(result.getNewsType(), is(NewsType.BADGE_ASSIGNMENT));
        assertThat(result.getEntityId(), is(1L));
        assertThat(result.getCompany().getId(), is(4L));
        assertThat(result.getToEmployees().size(), is(2));
        assertThat(result.getAuthor().getId(), is(1L));
    }
}
