package org.badges.service;

import org.badges.api.domain.ImportBadgeAssignment;
import org.badges.db.Badge;
import org.badges.db.BadgeAssignment;
import org.badges.db.News;
import org.badges.db.User;
import org.badges.db.repository.BadgeAssignmentRepository;
import org.badges.db.repository.BadgeRepository;
import org.badges.db.repository.UserRepository;
import org.badges.security.RequestContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BadgeAssignmentServiceTest {

    @InjectMocks
    private BadgeAssignmentService badgeAssignmentService;

    @Mock
    private NewsService newsService;

    @Mock
    private BadgeAssignmentRepository badgeAssignmentRepository;

    @Mock
    private BadgeRepository badgeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestContext requestContext;

    @Captor
    private ArgumentCaptor<BadgeAssignment> captor;

    @Test
    public void shouldSaveBadgeAssignmentWithNews() {
        // given
        ImportBadgeAssignment assignment = new ImportBadgeAssignment().setComment("comment")
                .setBadgeId(2L)
                .setAssignerId(3L)
                .addUsers(4L, 5L);
        when(userRepository.findOne(4L)).thenReturn(new User().setId(4L));
        when(userRepository.findOne(5L)).thenReturn(new User().setId(5L));
        when(badgeRepository.getOne(2L)).thenReturn(new Badge().setId(2L));
        when(newsService.prepareNews(captor.capture())).thenReturn(new News().setId(6L));

        // when
        News news = badgeAssignmentService.assignBadge(assignment);

        // then
        assertThat(news.getId(), is(6L));
        BadgeAssignment value = captor.getValue();
        verify(badgeAssignmentRepository).save(value);
        assertThat(value.getNews().getId(), is(6L));
        assertThat(value.getComment(), is("comment"));
        assertThat(value.getBadge().getId(), is(2L));
        assertThat(value.getToUsers().size(), is(2));
    }
}
