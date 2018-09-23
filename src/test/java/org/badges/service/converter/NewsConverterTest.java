package org.badges.service.converter;

import com.google.common.collect.Sets;
import org.badges.api.domain.news.BadgeNewsDto;
import org.badges.api.domain.news.NewsDto;
import org.badges.api.domain.news.UserNewsDto;
import org.badges.db.News;
import org.badges.db.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NewsConverterTest {

    @InjectMocks
    private NewsConverter newsConverter;

    @Mock
    private UserConverter userConverter;

    @Mock
    private BadgeConverter badgeConverter;

    @Test
    public void shouldConvertNews() {
        // given
        User user1 = new User().setId(1L);
        User user2 = new User().setId(2L);
        User user3 = new User().setId(3L);
        News news = new News()
                .setComment("comment")
                .setAuthor(user1)
                .setToUsers(Sets.newHashSet(user2, user3));
        UserNewsDto empDto1 = new UserNewsDto().setId(1L);
        UserNewsDto empDto2 = new UserNewsDto().setId(2L);
        UserNewsDto empDto3 = new UserNewsDto().setId(3L);
        when(userConverter.convertForNews(user1)).thenReturn(empDto1);
        when(userConverter.convertForNews(user2)).thenReturn(empDto2);
        when(userConverter.convertForNews(user3)).thenReturn(empDto3);
        BadgeNewsDto badgeDto = new BadgeNewsDto().setId(1L);
        when(badgeConverter.badgeNews(news)).thenReturn(badgeDto);

        // when
        NewsDto result = newsConverter.shortConvert(news);

        // then
        assertThat(result.getComment(), is("comment"));
        assertThat(result.getAuthor(), is(empDto1));
        assertThat(result.getToUsers(), containsInAnyOrder(empDto2, empDto3));
        assertThat(result.getReason(), is(badgeDto));
    }
}
